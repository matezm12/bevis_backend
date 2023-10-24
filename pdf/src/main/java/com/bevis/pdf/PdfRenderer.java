package com.bevis.pdf;

import java.io.OutputStream;

public interface PdfRenderer {
    void renderHtmlToPdf(String renderedHtmlContent, String resourcesFolder, OutputStream outputStream);
}
