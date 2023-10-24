package com.bevis.emailcore;

public interface EmailBuilder {
    String buildHtml(String templateName, Object data);
}
