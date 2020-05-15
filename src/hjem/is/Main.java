package hjem.is;

import hjem.is.utilities.Combination;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Main {
    private static void regressionExample() {
        ArrayList<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        List<List<Integer>> result = Combination.repeated(list, 5);
        for (List<Integer> integers : result) {
            System.out.println();
            for (Integer integer : integers) {
                System.out.print(integer + ", ");
            }
        }
    }

    private static void excelExample() {
        //https://poi.apache.org/components/spreadsheet/quick-guide.html#NewWorkbook
        Workbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet("new sheet");
        CreationHelper createHelper = wb.getCreationHelper();
        Row row = sheet.createRow(0);
        Cell cell = row.createCell(0);
        cell.setCellValue(1);
        cell = row.createCell(1);
        cell.setCellValue(1.2);
        cell = row.createCell(2);
        cell.setCellValue(createHelper.createRichTextString("string gang"));
        cell = row.createCell(3);
        cell.setCellValue(true);
        try (OutputStream fileOut = new FileOutputStream("workbook.xlsx")) {
            wb.write(fileOut);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        //excelExample();
        
    }
}
