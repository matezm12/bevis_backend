package com.bevis.pdf;


import com.bevis.resources.ResourcePathLoader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.IOException;
import java.io.OutputStream;

import static com.lowagie.text.pdf.BaseFont.EMBEDDED;
import static com.lowagie.text.pdf.BaseFont.IDENTITY_H;

@Service
@Slf4j
@RequiredArgsConstructor
class PdfRendererImpl implements PdfRenderer {

    public static final String SEGOE_UI_TTF = "Segoe UI.ttf";
    public static final String SEGOE_UI_LIGHT_TTF = "Segoe UI Light.ttf";
    public static final String SEGOE_UI_SEMIBOLD_TTF = "Segoe UI Semibold.ttf";

    private final ResourcePathLoader resourcePathLoader;

    @Override
    public void renderHtmlToPdf(String renderedHtmlContent, String resourcesFolder, OutputStream outputStream) {
        log.debug("Start rendering PDF from HTML");
        String xhtml = convertHtmlToXhtml(renderedHtmlContent);
        log.debug("HTML file successfully converted to XHTML ");
        ITextRenderer renderer = new ITextRenderer();
        try {
            final String fontsBaseUrl = resourcePathLoader.getFontsFolder();
            log.debug("fontsBaseUrl: {}", fontsBaseUrl);
            renderer.getFontResolver().addFont(fontsBaseUrl + "/" + SEGOE_UI_TTF, IDENTITY_H, EMBEDDED);
            renderer.getFontResolver().addFont(fontsBaseUrl + "/" + SEGOE_UI_LIGHT_TTF, IDENTITY_H, EMBEDDED);
            renderer.getFontResolver().addFont(fontsBaseUrl + "/" + SEGOE_UI_SEMIBOLD_TTF, IDENTITY_H, EMBEDDED);
        } catch (IOException e) {
            e.printStackTrace();
        }
        renderer.setDocumentFromString(xhtml, resourcesFolder);
        renderer.layout();
        renderer.createPDF(outputStream);
        log.info("PDF file generated successfully");
    }

    private String convertHtmlToXhtml(String renderedHtmlContent) {
        final Document document = Jsoup.parse(renderedHtmlContent);
        document.outputSettings().syntax(Document.OutputSettings.Syntax.xml);
        String xhtml = document.html();
        log.trace(xhtml);
        return xhtml;
    }

}
