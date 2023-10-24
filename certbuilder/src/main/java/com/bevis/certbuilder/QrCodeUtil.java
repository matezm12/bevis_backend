package com.bevis.certbuilder;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.Hashtable;

public final class QrCodeUtil {

    public static String generateBase64QrCodeFromText(String text) throws Exception {
        BufferedImage bi = generateEAN13BarcodeImage(text);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(bi, "jpg", baos);
        byte[] bytes = baos.toByteArray();
        return "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(bytes);
    }

    private static BufferedImage generateEAN13BarcodeImage(String barcodeText) throws Exception {
        String data = barcodeText;
        String charset = "UTF-8";
        int h = 300;
        int w = 300;
        String contents = new String(data.getBytes(charset), charset);
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        Hashtable<EncodeHintType, Object> hintMap = new Hashtable<>();
        hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
        hintMap.put(EncodeHintType.MARGIN, 0);
        BitMatrix matrix = qrCodeWriter.encode(contents, BarcodeFormat.QR_CODE, w, h, hintMap);
       return MatrixToImageWriter.toBufferedImage(matrix);
    }
}
