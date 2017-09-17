package com.jk.ndtetl.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtil {

    public static List<Sheet> getNumberOfSheets(String filePath) throws IOException {
        if ("".equals(filePath)) {
            throw new IOException("文件路径不能为空！");
        }
        File file = new File(filePath);
        if (!file.exists()) {
            throw new IOException("文件不存在！");
        }
        return getNumberOfSheets(file);
    }

    public static List<Sheet> getNumberOfSheets(File file) throws IOException {
        if (!file.exists()) {
            throw new IOException("文件不存在！");
        }
        // 获取扩展名
        String filePath = file.getPath();
        String ext = filePath.substring(filePath.lastIndexOf(".") + 1);

        // 使用xls方式读取
        if ("xls".equals(ext)) {
            return getNumberOfSheets_xls(file);
            // 使用xlsx方式读取
        }
        else if ("xlsx".equals(ext)) {
            return getNumberOfSheets_xlsx(file);
            // 依次尝试xls、xlsx方式读取
        }
        else {
            try {
                return getNumberOfSheets_xls(file);
            }
            catch (IOException e1) {
                try {
                    return getNumberOfSheets_xlsx(file);
                }
                catch (IOException e2) {
                    throw e2;
                }
            }
        }
    }

    private static List<Sheet> getNumberOfSheets_xls(File file) throws IOException {
        // 读取Excel 97-03版，xls格式
        HSSFWorkbook wb = new HSSFWorkbook(new FileInputStream(file));
        return getNumberOfSheets(wb);
    }

    private static List<Sheet> getNumberOfSheets_xlsx(File file) throws IOException {
        // 读取Excel 2007版，xlsx格式
        XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(file));
        return getNumberOfSheets(wb);
    }

    private static List<Sheet> getNumberOfSheets(Workbook wb) {
        // 获取可以操作的总数量
        int sheetCount = wb.getNumberOfSheets();
        List<Sheet> sheets = new ArrayList<>();
        for (int i = 0; i < sheetCount; i++) {
            Sheet sheet = wb.getSheetAt(i);
            int lastRowNum = sheet.getLastRowNum();
            if (lastRowNum > 0) {
                sheets.add(sheet);
            }
        }
        return sheets;
    }

    public static List<Integer> getIndexOfSheets(String filePath) throws IOException {
        if ("".equals(filePath)) {
            throw new IOException("文件路径不能为空！");
        }
        File file = new File(filePath);
        if (!file.exists()) {
            throw new IOException("文件不存在！");
        }
        return getIndexOfSheets(file);
    }

    public static List<Integer> getIndexOfSheets(File file) throws IOException {
        if (!file.exists()) {
            throw new IOException("文件不存在！");
        }
        // 获取扩展名
        String filePath = file.getPath();
        String ext = filePath.substring(filePath.lastIndexOf(".") + 1);

        // 使用xls方式读取
        if ("xls".equals(ext)) {
            return getIndexOfSheets_xls(file);
            // 使用xlsx方式读取
        }
        else if ("xlsx".equals(ext)) {
            return getIndexOfSheets_xlsx(file);
            // 依次尝试xls、xlsx方式读取
        }
        else {
            try {
                return getIndexOfSheets_xls(file);
            }
            catch (IOException e1) {
                try {
                    return getIndexOfSheets_xlsx(file);
                }
                catch (IOException e2) {
                    throw e2;
                }
            }
        }
    }

    private static List<Integer> getIndexOfSheets_xls(File file) throws IOException {
        // 读取Excel 97-03版，xls格式
        HSSFWorkbook wb = new HSSFWorkbook(new FileInputStream(file));
        return getIndexOfSheets(wb);
    }

    private static List<Integer> getIndexOfSheets_xlsx(File file) throws IOException {
        // 读取Excel 2007版，xlsx格式
        XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(file));
        return getIndexOfSheets(wb);
    }

    private static List<Integer> getIndexOfSheets(Workbook wb) {
        // 获取可以操作的总数量
        int sheetCount = wb.getNumberOfSheets();
        List<Integer> indexOfSheets = new ArrayList<>();
        for (int i = 0; i < sheetCount; i++) {
            Sheet sheet = wb.getSheetAt(i);
            int lastRowNum = sheet.getLastRowNum();
            // 有内容才添加到indexOfSheets
            if (lastRowNum > 0) {
                indexOfSheets.add(i);
            }
        }
        return indexOfSheets;
    }

}
