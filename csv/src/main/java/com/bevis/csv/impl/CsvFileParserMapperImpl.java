package com.bevis.csv.impl;

import com.bevis.csv.CsvFileParserMapper;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Collections;
import java.util.List;

@Slf4j
@Component
final class CsvFileParserMapperImpl implements CsvFileParserMapper {
    @Override
    public <T> List<T> loadObjectList(Class<T> type, File file) {
        try {
            try {
                return parseObjectsListBySchema(type, file, getDefaultBootstrapSchema());
            } catch (Exception e) {
                return parseObjectsListBySchema(type, file, getCustomBootstrapSchema());
            }
        } catch (Exception e) {
            log.error("Error occurred while loading object list from file " + file.getName(), e);
            return Collections.emptyList();
        }
    }

    private <T> List<T> parseObjectsListBySchema(Class<T> type, File file, CsvSchema bootstrapSchema) throws java.io.IOException {
        CsvMapper mapper = new CsvMapper();
        MappingIterator<T> readValues =
                mapper.readerFor(type).with(bootstrapSchema).readValues(file);
        return readValues.readAll();
    }

    private CsvSchema getDefaultBootstrapSchema() {
        return CsvSchema.emptySchema()
                .withColumnSeparator(';')
                .withQuoteChar('"')
                .withHeader();
    }

    private CsvSchema getCustomBootstrapSchema() {
        return CsvSchema.emptySchema()
                .withColumnSeparator(',')
                .withQuoteChar('"')
                .withHeader();
    }
}
