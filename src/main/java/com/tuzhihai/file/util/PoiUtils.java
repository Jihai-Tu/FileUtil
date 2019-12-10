/*
 * Copyright (c) 2018-2025 tuzhihai(涂志海) All Rights Reserved
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.tuzhihai.file.util;
/*
 * Copyright (c) 2018-2025 tuzhihai(涂志海) All Rights Reserved
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */


import cn.hutool.core.date.DateUtil;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author: zhihai.tu
 * create: 2018/8/6
 */
public class PoiUtils{
    public static final String XLSX_SUFFIX = ".xlsx";
    public static final String XLS_SUFFIX = ".xls";
    public PoiUtils() {
    }

    public static <T> List<T> readExcel(Workbook wb, int sheetNo, int ignore, Class<T> tClass) throws Exception {
        Sheet hs = wb.getSheetAt(sheetNo);
        List<T> list = new ArrayList(200);
        int totalRowNum = hs.getPhysicalNumberOfRows();
        try {
            Row sheetHeader = hs.getRow(0);
            for(int rowIndex = ignore; rowIndex < totalRowNum; ++rowIndex) {
                Row row = hs.getRow(rowIndex);
                if (row == null) {
                    break;
                }
                int cellIndex = row.getPhysicalNumberOfCells();
                T t = tClass.newInstance();
                for(int i = 0; i < cellIndex; ++i) {
                    Cell cell = row.getCell(i);
                    String cellValue = cell.getStringCellValue();

                    String fileName = sheetHeader.getCell(i).getStringCellValue();
                    Field curField = tClass.getDeclaredField(lineToHump(fileName));
                    Class fieldTypeClass = curField.getType();
                    if(curField == null || StringUtils.isEmpty(cellValue)){
                        continue;
                    }
                    curField.setAccessible(true);
                    Object value;
                    if(fieldTypeClass.getName().equals("java.util.Date")){
                        value = DateUtil.parse(cellValue).toJdkDate();
                    }else {
                        value = ConvertUtils.convert(cellValue,fieldTypeClass);
                    }
                    curField.set(t, value);
                }
                list.add(t);
            }
        } catch (Exception e) {
            throw e;
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

    public static <T> List<T> readXLS(InputStream src, int sheetNo, int ignore, Class<T> tClass) throws Exception {
        Workbook workbook = new HSSFWorkbook(src);
        return readExcel(workbook, sheetNo, ignore, tClass);
    }

    public static <T> List<T> readXLSX(InputStream src, int sheetNo, int ignore, Class<T> tClass) throws Exception {
        Workbook workbook = new XSSFWorkbook(src);
        return readExcel(workbook, sheetNo, ignore, tClass);
    }


    /**
     * 驼峰转下划线
     * @param str
     * @return
     */
    private static Pattern humpPattern = Pattern.compile("[A-Z]");
    private static String humpToLine(String str) {
        Matcher matcher = humpPattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, "_" + matcher.group(0).toLowerCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }


    /**
     * 下划线转驼峰
     */
    private static Pattern linePattern = Pattern.compile("_(\\w)");
    private static String lineToHump(String str) {
        str = str.toLowerCase();
        Matcher matcher = linePattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, matcher.group(1).toUpperCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }


}