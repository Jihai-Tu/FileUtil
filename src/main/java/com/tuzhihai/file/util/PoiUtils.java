package com.tuzhihai.file.util;

import java.io.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * @author: zhihai.tu
 * create: 2018/8/6
 */
public class PoiUtils {
    public static final String XLSX_SUFFIX = ".xlsx";
    public static final String XLS_SUFFIX = ".xls";

    public PoiUtils() {
    }

    public static <T> List<T> readExcel(Workbook wb, int sheetNo, int ignore, Class<T> tClass) throws IOException {
        Sheet hs = wb.getSheetAt(sheetNo);
        List<T> list = new ArrayList(200);
        int rowNo = hs.getPhysicalNumberOfRows();

        try {
            for(int rowi = ignore; rowi < rowNo; ++rowi) {
                Row row = hs.getRow(rowi);
                if (row == null) {
                    break;
                }

                int cellIndex = row.getPhysicalNumberOfCells();
                Field[] fields = tClass.getDeclaredFields();
                T t = tClass.newInstance();

                for(int i = 0; i < cellIndex; ++i) {
                    Field curField = fields[i];
                    curField.setAccessible(true);
                    Cell cell = row.getCell(i);
                    if (cell != null) {
                        switch(cell.getCellTypeEnum()) {
                            case NUMERIC:
                                double tmp = cell.getNumericCellValue();
                                if (tmp % 1.0D == 0.0D) {
                                    curField.set(t, String.valueOf((long)tmp));
                                } else {
                                    curField.set(t, String.valueOf(tmp));
                                }
                                break;
                            case STRING:
                                curField.set(t, cell.getStringCellValue());
                                break;
                            case BOOLEAN:
                                curField.set(t, cell.getBooleanCellValue());
                                break;
                            case FORMULA:
                                curField.set(t, cell.getCellFormula());
                        }
                    }
                }

                list.add(t);
            }
        } catch (Exception var17) {
            System.out.println("java");
        }

        return list;
    }

    public static <T> String writeExcel(Workbook workbook, List<T> src, String path, String filename, String suffix, Class<T> tClass, List<String> header) throws IOException, IllegalAccessException {
        assert workbook != null : "workbook not be null";

        assert src != null : "src not be null";

        Field[] fields = tClass.getDeclaredFields();
        if (header != null && header.size() != fields.length) {
            throw new IllegalArgumentException("the header length must be equals with the num of properties T");
        } else {
            for(int fi = 0; fi < fields.length; ++fi) {
                fields[fi].setAccessible(true);
            }

            Sheet sheet = workbook.createSheet();
            Row rHeader = sheet.createRow(0);
            int i;
            if (header != null) {
                for(i = 0; i < header.size(); ++i) {
                    rHeader.createCell(i).setCellValue((String)header.get(i));
                }
            } else {
                for(i = 0; i < fields.length; ++i) {
                    rHeader.createCell(i).setCellValue(tClass.getDeclaredFields()[i].getName());
                }
            }

            for(i = 0; i < src.size(); ++i) {
                Row curRow = sheet.createRow(i + 1);

                for(int f = 0; f < fields.length; ++f) {
                    Object ob = fields[f].get(src.get(i));
                    if (ob != null && !"".equals(String.valueOf(ob))) {
                        curRow.createCell(f).setCellValue(String.valueOf(ob));
                    } else {
                        curRow.createCell(f).setCellValue("");
                    }
                }
            }

            String fullName = filename + suffix;
            File file = new File(path + fullName);
            // compatibility windows
            if(!file.getParentFile().exists()){
                try {
                    file.getParentFile().mkdirs();
                    file.createNewFile();
                } catch (IOException e) {
                    throw new IOException("file mkdir wrong");
                }
            }
//            if (!file.exists()){
//                try {
//                    file.createNewFile();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
            OutputStream outputStream = new FileOutputStream(path + fullName);
            workbook.write(outputStream);
            return path + fullName;
        }
    }

    public static <T> String writeXLS(List<T> src, String path, String filename, Class<T> tClass, List<String> header) throws IOException, IllegalAccessException {
        Workbook workbook = new HSSFWorkbook();
        return writeExcel(workbook, src, path, filename, ".xls", tClass, header);
    }

    public static <T> String writeXLSX(List<T> src, String path, String filename, Class<T> tClass, List<String> header) throws IOException, IllegalAccessException {
        Workbook workbook = new XSSFWorkbook();
        return writeExcel(workbook, src, path, filename, ".xlsx", tClass, header);
    }

    public static <T> List<T> readXLS(InputStream src, int sheetNo, int ignore, Class<T> tClass) throws IOException {
        Workbook workbook = new HSSFWorkbook(src);
        return readExcel(workbook, sheetNo, ignore, tClass);
    }

    public static <T> List<T> readXLSX(InputStream src, int sheetNo, int ignore, Class<T> tClass) throws IOException {
        Workbook workbook = new XSSFWorkbook(src);
        return readExcel(workbook, sheetNo, ignore, tClass);
    }
}
