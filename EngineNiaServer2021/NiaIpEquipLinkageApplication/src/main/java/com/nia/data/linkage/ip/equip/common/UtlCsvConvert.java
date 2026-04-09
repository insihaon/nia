package com.nia.data.linkage.ip.equip.common;

import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.dataformat.csv.CsvGenerator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.List;

public class UtlCsvConvert {
    public static void csvFileOut(Class<?> clazz, File csvFile, List<?> dataList) throws Exception{
        CsvMapper csvMapper = new CsvMapper();
        CsvSchema csvSchema = csvMapper.enable(CsvGenerator.Feature.ALWAYS_QUOTE_STRINGS)
                                .schemaFor(clazz).withColumnSeparator(',').withLineSeparator("\n");
        ObjectWriter writer = csvMapper.writer(csvSchema);

//        FileOutputStream tempFileOutputStream = new FileOutputStream(csvFile);
//        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(tempFileOutputStream, 1024);
//        OutputStreamWriter writerOutputStream = new OutputStreamWriter(bufferedOutputStream, "UTF-8");
//        writer.writeValue(writerOutputStream, dataList);

        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new FileOutputStream(csvFile), "UTF8");
        writer.writeValues(outputStreamWriter).writeAll(dataList);
    }
}
