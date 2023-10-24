package com.bevis.csv;

import java.io.File;
import java.util.List;

public interface CsvFileParserMapper {
    <T> List<T> loadObjectList(Class<T> type, File file);
}
