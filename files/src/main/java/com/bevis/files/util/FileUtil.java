package com.bevis.files.util;

import com.bevis.files.dto.File;
import com.bevis.files.exception.FileException;
import io.github.pixee.security.Filenames;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import static com.bevis.common.util.StringUtil.substringAfterLastIndexOf;

@Slf4j
public class FileUtil {

    public static void upload(MultipartFile multipartFile, java.io.File outputFile) throws IOException {
        try (InputStream inputStream = multipartFile.getInputStream()) {
            try (FileOutputStream fileOutputStream = new FileOutputStream(outputFile)) {
                IOUtils.copy(inputStream, fileOutputStream);
            } catch (IOException e) {
                String error = String.format("Error uploading file %s ", outputFile.getAbsolutePath());
                log.error(error);
                throw new IOException(error);
            }
            log.debug(String.format("File %s uploaded to server", outputFile.getAbsolutePath()));
        } catch (Exception e2) {
            throw new IOException(String.format("Error uploading file %s ", outputFile.getAbsolutePath()));
        }
    }

    public static String getFileExtension(String fileName) {
        return substringAfterLastIndexOf(fileName, ".");
    }

    public static File createTempFile(MultipartFile multipartFile) {
        try {
            java.io.File uploadedFile = java.io.File.createTempFile("file_", getFileExtension(Filenames.toSimpleFileName(multipartFile.getOriginalFilename())));
            uploadedFile.deleteOnExit();
            FileUtil.upload(multipartFile, uploadedFile);
            return File.builder()
                    .fileName(multipartFile.getOriginalFilename())
                    .file(uploadedFile)
                    .build();
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new FileException("Error creating temp file", e);
        }
    }

    public static File createEncryptedFile(MultipartFile multipartFile, String password) {
        try {
            java.io.File uploadedFile = java.io.File.createTempFile("file_", getFileExtension(Filenames.toSimpleFileName(multipartFile.getOriginalFilename())));
            FileUtil.upload(multipartFile, uploadedFile);

            java.io.File encryptedFile = generateTmpPath("file_", ".zip").toFile();
            encryptedFile.deleteOnExit();

            ZipEncryptUtil.encryptWithPassword(uploadedFile, encryptedFile, password);
            uploadedFile.delete();
            return File.builder()
                    .fileName(Filenames.toSimpleFileName(multipartFile.getOriginalFilename()) + ".zip")
                    .file(encryptedFile)
                    .build();
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new FileException("Error creating temp file", e);
        }
    }

    public static String calculateSha256Hash(java.io.File file) {
        try {
            MessageDigest sha256 = MessageDigest.getInstance("SHA256");
            FileInputStream fis = new FileInputStream(file);
            byte[] data = new byte[1024];
            int read;
            while ((read = fis.read(data)) != -1) {
                sha256.update(data, 0, read);
            }
            byte[] hashBytes = sha256.digest();
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < hashBytes.length; i++) {
                sb.append(Integer.toString((hashBytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            String fileHash = sb.toString();
            return fileHash;
        } catch (NoSuchAlgorithmException | IOException e) {
            log.error(e.getMessage());
            throw new FileException("Error calculating sha256 hash of file", e);
        }
    }

    private static Path generateTmpPath(String prefix, String suffix) {
        final Path tmpdir = Path.of(System.getProperty("java.io.tmpdir"));
        return generatePath(prefix, suffix, tmpdir);
    }

    private static Path generatePath(String prefix, String suffix, Path dir) {
        Random random = new Random();
        long n = random.nextLong();
        String s = prefix + Long.toUnsignedString(n) + suffix;
        Path name = dir.getFileSystem().getPath(s);
        // the generated name should be a simple file name
        if (name.getParent() != null)
            throw new IllegalArgumentException("Invalid prefix or suffix");
        return dir.resolve(name);
    }
}
