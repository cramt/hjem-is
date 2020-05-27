package hjem.is;

import hjem.is.controller.regression.*;
import hjem.is.db.*;
import hjem.is.model.Product;
import hjem.is.model.Supplier;
import hjem.is.ui.StoragePlanListUI;
import hjem.is.ui.StoragePlanListUITwo;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.*;

public class Main {
    private static void regressionExample() {
        List<Observation> observations = new ArrayList<>();
        Observation o = new Observation();
        o.putFeature("heat", 6.4);
        o.putFeature("price", 10);
        o.putFeature("sold", 100);
        observations.add(o);
        o = new Observation();
        o.putFeature("heat", 6);
        o.putFeature("price", 10);
        o.putFeature("sold", 90);
        observations.add(o);
        o = new Observation();
        o.putFeature("heat", 5.5);
        o.putFeature("price", 10);
        o.putFeature("sold", 86);
        observations.add(o);
        o = new Observation();
        o.putFeature("heat", 5);
        o.putFeature("price", 10);
        o.putFeature("sold", 80);
        observations.add(o);
        o = new Observation();
        o.putFeature("heat", 4);
        o.putFeature("price", 10);
        o.putFeature("sold", 70);
        observations.add(o);
        ModelFinder finder = new ModelFinder(new NormalEquationFit(), (Observation[]) observations.toArray(), "sold");
        Model model = finder.run();
        o = new Observation();

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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            IProductStore productStore = new ProductSqlStore();
            ISupplierStore supplierStore = new SupplierSqlStore();
            if (productStore.getAll().size() == 0) {
                Supplier supplier = new Supplier(4, 100, "these dudes");
                supplierStore.add(supplier);
                List<Product> products = new ArrayList<>();
                products.add(new Product(30, "isbåd", supplier));
                products.add(new Product(20, "guldhorn", supplier));
                products.add(new Product(10, "kong-fu", supplier));
                productStore.add(products);
            }
        } catch (DataAccessException ignored) {
        }
        //new StoragePlanListUI();
        new StoragePlanListUITwo();
    }
}
