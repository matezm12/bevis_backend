package com.bevis.files.util;

import lombok.extern.slf4j.Slf4j;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.model.enums.AesKeyStrength;
import net.lingala.zip4j.model.enums.EncryptionMethod;

import java.io.File;
import java.util.List;

@Slf4j
public final class ZipEncryptUtil {

    public static void encryptWithPassword(File inputFile, File encryptedFile, String password) throws ZipException {
        ZipParameters zipParameters = new ZipParameters();
        zipParameters.setEncryptFiles(true);
        zipParameters.setEncryptionMethod(EncryptionMethod.AES);
        // Below line is optional. AES 256 is used by default.
        // You can override it to use AES 128. AES 192 is supported only for extracting.
        zipParameters.setAesKeyStrength(AesKeyStrength.KEY_STRENGTH_256);

        ZipFile zipFile = new ZipFile(encryptedFile, password.toCharArray());

        List<File> filesToAdd = List.of(inputFile);

        zipFile.addFiles(filesToAdd, zipParameters);

        log.debug("Zip file created: {}", zipFile);
    }
}
