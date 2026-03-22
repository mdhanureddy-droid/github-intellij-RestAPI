package files;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class DataDrivenReview {

    private static final Logger logger = LogManager.getLogger(DataDrivenReview.class);

    public ArrayList<String> getData(String testcaseName, String sheetName) throws IOException {
        ArrayList<String> data = new ArrayList<String>();

        FileInputStream fis = new FileInputStream("testdata/demodata.xlsx");
        XSSFWorkbook workbook = new XSSFWorkbook(fis);

        int sheets = workbook.getNumberOfSheets();
        for (int i = 0; i < sheets; i++) {
            if (workbook.getSheetName(i).equalsIgnoreCase(sheetName)) {
                XSSFSheet sheet = workbook.getSheetAt(i);

                Iterator<Row> rows = sheet.iterator();
                Row firstrow = rows.next();
                Iterator<Cell> ce = firstrow.cellIterator();
                int k = 0;
                int column = 0;
                while (ce.hasNext()) {
                    Cell value = ce.next();

                    if (value.getStringCellValue().equalsIgnoreCase("TestCases")) {
                        column = k;
                    }

                    k++;
                }
                logger.info("TestCases column index: {}", column);

                while (rows.hasNext()) {
                    Row r = rows.next();

                    if (r.getCell(column).getStringCellValue().equalsIgnoreCase(testcaseName)) {
                        Iterator<Cell> cv = r.cellIterator();
                        while (cv.hasNext()) {
                            Cell c = cv.next();
                            if (c.getCellType() == CellType.STRING) {
                                data.add(c.getStringCellValue());
                            } else {
                                data.add(NumberToTextConverter.toText(c.getNumericCellValue()));
                            }
                        }
                    }
                }
            }
        }
        return data;
    }

    public static void main(String[] args) throws IOException {
        DataDrivenReview d = new DataDrivenReview();
        ArrayList<String> data = d.getData("Purchase", "testdata");
        logger.info("Retrieved data: {}", data);
    }

}
