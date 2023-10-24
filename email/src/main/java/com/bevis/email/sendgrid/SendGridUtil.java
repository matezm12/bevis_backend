package com.bevis.email.sendgrid;

import com.sendgrid.helpers.mail.objects.Attachments;

import java.io.File;
import java.io.IOException;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;

final class SendGridUtil {

    static Attachments convertFileToAttachment(File file) throws IOException {
        Path path = file.toPath();

        Attachments attachments = new Attachments();
        attachments.setFilename(path.getFileName().toString());

        String mimeType = URLConnection.guessContentTypeFromName(file.getName());
        attachments.setType(mimeType);

        attachments.setDisposition("attachment");
        byte[] attachmentContentBytes = Files.readAllBytes(path);
        String attachmentContent = Base64.getEncoder().encodeToString(attachmentContentBytes);
        attachments.setContent(attachmentContent);

        return attachments;
    }

}
