package com.restapi.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ExcelDataReader {

    private static final Logger logger = LogManager.getLogger(ExcelDataReader.class);
    private static final String DEFAULT_FILE = "testdata/demodata.xlsx";

    public ArrayList<String> getData(String testcaseName, String sheetName) throws IOException {
        try (FileInputStream fis = new FileInputStream(DEFAULT_FILE);
             XSSFWorkbook workbook = new XSSFWorkbook(fis)) {

            XSSFSheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) return new ArrayList<>();

            int column = findColumnIndex(sheet.getRow(0), "TestCases");
            logger.info("TestCases column index: {}", column);

            return extractMatchingRows(sheet, column, testcaseName);
        }
    }

    public Map<String, String> getDataAsMap(String testcaseName, String sheetName) throws IOException {
        try (FileInputStream fis = new FileInputStream(DEFAULT_FILE);
             XSSFWorkbook workbook = new XSSFWorkbook(fis)) {

            XSSFSheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) return new HashMap<>();

            Row headerRow = sheet.getRow(0);
            int column = findColumnIndex(headerRow, "TestCases");

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row != null && row.getCell(column).getStringCellValue().equalsIgnoreCase(testcaseName)) {
                    return mapRowToHeaders(headerRow, row);
                }
            }
            return new HashMap<>();
        }
    }

    private int findColumnIndex(Row headerRow, String columnName) {
        for (Cell cell : headerRow) {
            if (cell.getStringCellValue().equalsIgnoreCase(columnName)) {
                return cell.getColumnIndex();
            }
        }
        return 0;
    }

    private ArrayList<String> extractMatchingRows(XSSFSheet sheet, int column, String testcaseName) {
        ArrayList<String> data = new ArrayList<>();
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row != null && row.getCell(column).getStringCellValue().equalsIgnoreCase(testcaseName)) {
                addRowData(row, data);
            }
        }
        return data;
    }

    private void addRowData(Row row, ArrayList<String> data) {
        for (Cell cell : row) {
            data.add(cell.getCellType() == CellType.STRING
                    ? cell.getStringCellValue()
                    : NumberToTextConverter.toText(cell.getNumericCellValue()));
        }
    }

    private Map<String, String> mapRowToHeaders(Row headerRow, Row dataRow) {
        Map<String, String> map = new HashMap<>();
        for (Cell cell : dataRow) {
            String header = headerRow.getCell(cell.getColumnIndex()).getStringCellValue();
            String value = cell.getCellType() == CellType.STRING
                    ? cell.getStringCellValue()
                    : NumberToTextConverter.toText(cell.getNumericCellValue());
            map.put(header, value);
        }
        return map;
    }

}
