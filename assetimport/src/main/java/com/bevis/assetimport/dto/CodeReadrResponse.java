package com.bevis.assetimport.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@JacksonXmlRootElement(localName = "xml")
public class CodeReadrResponse {
    private CodeReadrMessage message;
}
