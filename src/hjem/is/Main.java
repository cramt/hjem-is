package hjem.is;

import hjem.is.utilities.Combination;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Main {
    public static void main(String[] args) {
        Workbook wb = new XSSFWorkbook();
        try (OutputStream fileOut = new FileOutputStream("workbook.xlsx")) {
            wb.write(fileOut);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        /*
        double[] x = new double[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19};
        double[] y = new double[]{0, 1, 4, 9, 16, 25, 36, 49, 64, 81, 100, 121, 144, 169, 196, 225, 256, 289, 324, 361};
        TrendLine line = OLSTrendLine.assumeTrend(y, x);
        System.out.println(line.toString());
        System.out.println(line.predict(5.5));
        */
        /*
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
         */
    }
}
