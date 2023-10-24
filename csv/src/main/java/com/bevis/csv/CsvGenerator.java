package com.bevis.csv;

import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.List;
import java.util.Objects;

@Slf4j
public final class CsvGenerator {

    public static <T> String generateCsvStringFromList(List<T> objList, String[] fieldsOrder) {
        Writer writer = new StringWriter();
        generateCsvForList(objList, fieldsOrder, writer);
        return writer.toString();
    }

    public static <T> void generateCsvForList(List<T> objList, String[] fieldsOrder, Writer writer) {
        StatefulBeanToCsv statefulBeanToCsv = new StatefulBeanToCsvBuilder(writer)
                .withSeparator(',')
                .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
                .build();
        try {
            writer.write(String.join(",", fieldsOrder) + "\n");
            if (Objects.nonNull(statefulBeanToCsv)) {
                statefulBeanToCsv.write(objList);
            }
        } catch (IOException e) {
            log.error("IOException");
            throw new CsvException("IOException", e);
        } catch (CsvDataTypeMismatchException e) {
            log.error("CsvDataTypeMismatchException");
            throw new CsvException("CsvDataTypeMismatchException", e);
        } catch (CsvRequiredFieldEmptyException e) {
            log.error("CsvRequiredFieldEmptyException");
            throw new CsvException("CsvDataTypeMismatchException", e);
        }
    }
}
