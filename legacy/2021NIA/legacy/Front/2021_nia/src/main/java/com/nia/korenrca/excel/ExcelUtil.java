package com.nia.korenrca.excel;

import com.nia.korenrca.service.util.Utils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Mobidigm on 2019-01-23.
 */
public class ExcelUtil {

    private static String defaultExcelDir  = "./file-access/excel/";

    public static <T> String createXlsx(List<T> pojoObjectList, List<T> columns,  List<T>... columnNames) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, IOException {
        Utils.mkDirs(defaultExcelDir);
        Utils.DeleteOldFiles(defaultExcelDir);
        return ExcelUtil.createXlsx(pojoObjectList, columns,  defaultExcelDir , columnNames);
    }

    private static <T> String createXlsx(List<T> pojoObjectList, List<T> columns,  String filePath, List<T>... columnNames) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, IOException {

        Workbook workbook = new SXSSFWorkbook();
        SXSSFSheet sheet = (SXSSFSheet) workbook.createSheet();

        Row hRow = sheet.createRow(0);
        RowFilledWithPojoHeader(hRow, columns, columnNames[0]);

        for (int i = 0; i < pojoObjectList.size(); i++) {
            Row row = sheet.createRow(i + 1);
            RowFilledWithPojoData(pojoObjectList.get(i), row, columns);
        }
        String fileName = System.currentTimeMillis() + ".xlsx";
        FileOutputStream fos = new FileOutputStream(filePath + fileName);
        workbook.write(fos);
        fos.close();
        return fileName;
    }

    private static <T> Row RowFilledWithPojoHeader(Row row, List<T> columns, List<T>... columnNames) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
        for (int i = 0; i < columns.size(); i++) {
            String cellValue;
            if(columnNames[0] != null){
                cellValue = String.valueOf(columnNames[0].get(i));
            }
            else{
                cellValue = String.valueOf(columns.get(i)).toUpperCase();
            }
            row.createCell(i).setCellValue(cellValue);
        }
        return row;
    }

    private static <T> Row RowFilledWithPojoData(Object pojoObject, Row row, List<T> columns) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
        HashMap<String, Object> list = (HashMap<String, Object>)pojoObject;
        for (int i = 0; i < columns.size(); i++) {
            String cellValue;
            String formatResult = ticketDataFormat(columns.get(i), list);

            if(formatResult != null) {
                cellValue = formatResult;
            }
            else {
                cellValue = String.valueOf(list.get(columns.get(i)));
            }

            if(cellValue == "null"){
                cellValue = "";
            }
            row.createCell(i).setCellValue(cellValue);
        }
        return row;
    }

    public static String getCellValue(Row row, int cellIndex) {
        Cell cell = row.getCell(cellIndex);
        if (cell == null) {
            return null;
        }
        cell.setCellType(Cell.CELL_TYPE_STRING);
        return cell.getStringCellValue();
    }

    public static String getCellValue(Object object) {
        if (object == null) {
            return "";
        }
        return object.toString();
    }

    public static String ticketDataFormat(Object object, HashMap<String, Object> list) {
        if (object == null) {
            return "";
        }
        if(object.equals("status")) {
            String result;
            switch (list.get(object).toString()) {
                case "INIT":
                    result = "발생";
                    break;
                case "ACK":
                    result = "인지";
                    break;
                case "FIN":
                    result = "마감";
                    break;
                case "AUTO_FIN":
                    result = "자동마감";
                    break;
                case "UNKNOWN":
                    result = "미분석";
                    break;
                default:
                    result = null;
                    break;
            }
            return result;
        }
        if(object.equals("handling_time")) {
            String fieldName = (list.get("status").toString().equals("FIN") || list.get("status").toString().equals("AUTO_FIN")) ? "handling_time" : "" ;
            if(list.get(fieldName) == null) {
                return "";
            } else {
                return list.get(fieldName).toString();
            }
        }
        if(object.equals("nature_restoration")) {
            if(list.get(object) == null) {
                return "";
            }
            if(list.get(object).toString().equals("1")) {
                return "자연회복";
            } else {
                return "";
            }
        }
        if(object.equals("topas_list")) {
            if(list.get(object) == null) {
                return "";
            } else {
                return list.get(object).toString().replaceAll("[\\[|\\]|\"]", "");
            }
        }

        return null;
    }
}
