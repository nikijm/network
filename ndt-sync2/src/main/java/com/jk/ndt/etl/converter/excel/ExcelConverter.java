package com.jk.ndt.etl.converter.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import com.jk.ndt.etl.converter.FileConverter;
import com.jk.ndt.etl.converter.model.DataTable;

/**
 * 
 * @ClassName: ExcelConverter
 * @Description: Excel文件解析类
 * @author lianhanwen
 * @date 2017年5月14日 下午6:48:44
 *
 */
@Component
public class ExcelConverter implements FileConverter {

    // %%%%%%%%-------常量部分 开始----------%%%%%%%%%
    private final static String UNIQUE_UUID = "合并单元格标识" + "a1ee26d7-87bd-4e92-ad62-57e0d4640894";
    /**
     * 默认的开始读取的行位置为第一行（索引值为0）
     */
    private final static int READ_START_POS = 0;

    /**
     * 默认结束读取的行位置为最后一行（索引值=0，用负数来表示倒数第n行）
     */
    private final static int READ_END_POS = 0;

    /**
     * 默认Excel内容的开始比较列位置为第一列（索引值为0）
     */
    private final static int COMPARE_POS = 0;

    /**
     * 默认多文件合并的时需要做内容比较（相同的内容不重复出现）
     */
    private final static boolean NEED_COMPARE = true;

    /**
     * 默认多文件合并的新文件遇到名称重复时，进行覆盖
     */
    private final static boolean NEED_OVERWRITE = true;

    /**
     * 默认只操作一个sheet
     */
    private final static boolean ONLY_ONE_SHEET = false;

    /**
     * 默认读取第一个sheet中（只有当ONLY_ONE_SHEET = true时有效）
     */
    private final static int SELECTED_SHEET = 0;

    /**
     * 默认从第一个sheet开始读取（索引值为0）
     */
    private final static int READ_START_SHEET = 0;

    /**
     * 默认在最后一个sheet结束读取（索引值=0，用负数来表示倒数第n行）
     */
    private final static int READ_END_SHEET = 0;

    /**
     * 默认打印各种信息
     */
    private final static boolean PRINT_MSG = true;

    // %%%%%%%%-------常量部分 结束----------%%%%%%%%%

    // %%%%%%%%-------字段部分 开始----------%%%%%%%%%

    /**
     * 设定开始读取的位置，默认为0
     */
    private int startReadPos = READ_START_POS;

    /**
     * 设定结束读取的位置，默认为0，用负数来表示倒数第n行
     */
    private int endReadPos = READ_END_POS;

    /**
     * 设定开始比较的列位置，默认为0
     */
    private int comparePos = COMPARE_POS;

    /**
     * 设定汇总的文件是否需要替换，默认为true
     */
    private boolean isOverWrite = NEED_OVERWRITE;

    /**
     * 设定是否需要比较，默认为true(仅当不覆写目标内容是有效，即isOverWrite=false时有效)
     */
    private boolean isNeedCompare = NEED_COMPARE;

    /**
     * 设定是否只操作第一个sheet
     */
    private boolean onlyReadOneSheet = ONLY_ONE_SHEET;

    /**
     * 设定操作的sheet在索引值
     */
    private int selectedSheetIdx = SELECTED_SHEET;

    /**
     * 设定操作的sheet的名称
     */
    private String selectedSheetName = "";

    /**
     * 设定开始读取的sheet，默认为0
     */
    private int startSheetIdx = READ_START_SHEET;

    /**
     * 设定结束读取的sheet，默认为0，用负数来表示倒数第n行
     */
    private int endSheetIdx = READ_END_SHEET;

    /**
     * 设定是否打印消息
     */
    private boolean printMsg = PRINT_MSG;

    // %%%%%%%%-------字段部分 结束----------%%%%%%%%%

    @Override
    public List<DataTable> convert(String filePath) throws IOException {
        List<DataTable> dtList = readExcel(filePath);

        return dtList;
    }

    @Override
    public List<DataTable> convert(File file) throws IOException {
        List<DataTable> dtList = readExcel(file);
        return dtList;
    }

    /**
     * 
     * @Description: 自动根据文件扩展名，调用对应的读取方法
     * @author lianhanwen
     * @date 2017年5月14日 下午6:54:55
     * @param xlsPath
     * @return
     * @throws IOException
     */
    public List<DataTable> readExcel(String xlsPath) throws IOException {
        // 扩展名为空时
        if ("".equals(xlsPath)) {
            throw new IOException("文件路径不能为空！");
        }
        File file = new File(xlsPath);
        if (!file.exists()) {
            throw new IOException("文件不存在！");
        }
        return readExcel(file);
    }

    public List<DataTable> readExcel(File file) throws IOException {
        if (!file.exists()) {
            throw new IOException("文件不存在！");
        }
        String xlsPath = file.getPath();
        // 获取扩展名
        String ext = xlsPath.substring(xlsPath.lastIndexOf(".") + 1);

        try {

            if ("xls".equals(ext)) { // 使用xls方式读取
                return readExcel_xls(xlsPath);
            } else if ("xlsx".equals(ext)) { // 使用xlsx方式读取
                return readExcel_xlsx(xlsPath);
            } else { // 依次尝试xls、xlsx方式读取
                System.out.println("您要操作的文件没有扩展名，正在尝试以xls方式读取...");
                try {
                    return readExcel_xls(xlsPath);
                } catch (IOException e1) {
                    System.out.println("尝试以xls方式读取，结果失败！，正在尝试以xlsx方式读取...");
                    try {
                        return readExcel_xlsx(xlsPath);
                    } catch (IOException e2) {
                        System.out.println("尝试以xls方式读取，结果失败！\n请您确保您的文件是Excel文件，并且无损，然后再试。");
                        throw e2;
                    }
                }
            }
        } catch (IOException e) {
            throw e;
        }
    }

    /**
     * 
     * @Description:读取Excel 2007版，xlsx格式
     * @author lianhanwen
     * @date 2017年5月14日 下午6:58:02
     * @param xlsPath
     * @return
     * @throws IOException
     */
    public List<DataTable> readExcel_xlsx(String xlsPath) throws IOException {
        File file = new File(xlsPath);
        // 判断文件是否存在

        if (!file.exists()) {
            throw new IOException("文件名为" + file.getName() + "Excel文件不存在！");
        }

        XSSFWorkbook wb = null;
        List<DataTable> dtList = null;
        FileInputStream fis = new FileInputStream(file);
        // 去读Excel
        wb = new XSSFWorkbook(fis);

        // 读取Excel 2007版，xlsx格式
        dtList = readExcel(wb);

        return dtList;
    }

    /**
     * 
     * @Description: 读取Excel(97-03版，xls格式)
     * @author lianhanwen
     * @date 2017年5月14日 下午6:58:31
     * @param xlsPath
     * @return
     * @throws IOException
     */
    public List<DataTable> readExcel_xls(String xlsPath) throws IOException {
        File file = new File(xlsPath);

        // 判断文件是否存在

        if (!file.exists()) {
            throw new IOException("文件名为" + file.getName() + "Excel文件不存在！");
        }

        HSSFWorkbook wb = null;// 用于Workbook级的操作，创建、删除Excel

        List<DataTable> dtList = null;
        // 读取Excel
        wb = new HSSFWorkbook(new FileInputStream(file));

        // 读取Excel 97-03版，xls格式
        dtList = readExcel(wb);

        return dtList;
    }

    /**
     * 
     * @Description: 读取单元格的值
     * @author lianhanwen
     * @date 2017年5月14日 下午6:58:51
     * @param cell
     * @return
     */
    private String getCellValue(Cell cell) {
        Object result = "";
        if (cell != null) {
            switch (cell.getCellType()) {
            case Cell.CELL_TYPE_STRING:
                result = cell.getStringCellValue();
                break;
            case Cell.CELL_TYPE_NUMERIC:
                result = cell.getNumericCellValue();
                break;
            case Cell.CELL_TYPE_BOOLEAN:
                result = cell.getBooleanCellValue();
                break;
            case Cell.CELL_TYPE_FORMULA:
                result = cell.getCellFormula();
                break;
            case Cell.CELL_TYPE_ERROR:
                result = cell.getErrorCellValue();
                break;
            case Cell.CELL_TYPE_BLANK:
                break;
            default:
                break;
            }
        }
        return result.toString();
    }

    /**
     * 
     * @Description: 通用读取Excel
     * @author lianhanwen
     * @date 2017年5月14日 下午6:59:17
     * @param wb
     * @return
     * @throws IOException
     */
    private List<DataTable> readExcel(Workbook wb) throws IOException {
        List<DataTable> dtList = new ArrayList<>();
        int sheetCount = 1;// 需要操作的sheet数量

        Sheet sheet = null;
        if (onlyReadOneSheet) { // 只操作一个sheet
            // 获取设定操作的sheet(如果设定了名称，按名称查，否则按索引值查)
            sheet = selectedSheetName.equals("") ? wb.getSheetAt(selectedSheetIdx) : wb.getSheet(selectedSheetName);
        } else { // 操作多个sheet
            sheetCount = wb.getNumberOfSheets();// 获取可以操作的总数量
        }

        // 获取sheet数目
        for (int t = startSheetIdx; t < sheetCount + endSheetIdx; t++) {
            // 获取设定操作的sheet
            if (!onlyReadOneSheet) {
                sheet = wb.getSheetAt(t);
            }

            // 获取最后行号
            int lastRowNum = sheet.getLastRowNum();
            if (lastRowNum > 0) { // 如果>0，表示有数据
                // System.out.println("\n开始读取名为【" + sheet.getSheetName() +
                // "】的内容：");

                Row row = null;
                // 循环读取

                List<List<String>> data = new ArrayList<>();
                DataTable dataTable = null;
                short size = 0;
                // Map<String,Boolean> merged=new HashMap<>();
                // merged.put("mergedRow", false);
                // merged.put("mergedColumn", false);
                for (int i = startReadPos; i <= lastRowNum + endReadPos; i++) {
                    row = sheet.getRow(i);
                    if (row != null) {
                        // 计算数据中的最长size
                        short lastCellNum = row.getLastCellNum();
                        size = lastCellNum > size ? lastCellNum : size;
                        // 获取每一单元格的值
                        ArrayList<String> list = new ArrayList<>();
                        for (int j = 0; j < row.getLastCellNum(); j++) {

                            boolean mergedRegion = isMergedRegion(sheet, i, j);
                            boolean mergedRow = isMergedRow(sheet, i, j);

                            // if (mergedRow) {
                            // merged.put("mergedRow", true);
                            // }
                            // if (mergedRegion) {
                            // merged.put("mergedColumn", true);
                            // }

                            if (mergedRow || mergedRegion) {
                                String mergedRegionValue = getMergedRegionValue(sheet, i, j);
                                if (!list.contains(mergedRegionValue)) {
                                    list.add(getMergedRegionValue(sheet, i, j));
                                } else {
                                    list.add(UNIQUE_UUID);
                                }
                                continue;
                                // if (i == startReadPos||i==startReadPos+1) {
                                // list.add("");
                                // continue;
                                // } else {
                                // throw new
                                // IOException("解析文件出错,请检查文件内容格式是否规范!!");
                                // }
                            }
                            String value = getCellValue(row.getCell(j));
                            list.add(value);

                        }
                        // 判断一行数据是否全部为空,不为空就添加到content中
                        boolean flag = false;
                        for (String str : list) {
                            if (!"".equals(str.trim())) {
                                flag = true;
                                continue;
                            }
                        }
                        if (flag) {
                            data.add(list);
                        }

                    }

                }
                dataTable = new DataTable();
                List<List<String>> content = new ArrayList<>();
                List<String> header = new ArrayList<>();
                int headerLine = 0;
                // 只有行合并
                // if (merged.get("mergedRow")&& !merged.get("mergedColumn")) {
                // if (data.size() > 0) {
                // // 将每一条数据的长度都设置成所有列中最长长度,不足用空字符串补齐
                // for (List<String> list2 : data) {
                // list2.addAll(Arrays.asList(new String[size - list2.size()]));
                // if (!content.contains(list2)) {
                // content.add(list2);
                // }
                // }
                // header=content.get(headerLine);
                // content.remove(headerLine);
                // }
                //
                // }
                // //只有列合并
                // if (!merged.get("mergedRow")&& merged.get("mergedColumn")) {
                // if (data.size() > 0) {
                // // 将每一条数据的长度都设置成所有列中最长长度,不足用空字符串补齐
                // for (List<String> list2 : data) {
                // list2.addAll(Arrays.asList(new String[size - list2.size()]));
                // if (!list2.contains(UNIQUE_UUID)) {
                // content.add(list2);
                // }
                // }
                // }
                // header=content.get(headerLine);
                // content.remove(headerLine);
                //
                // }
                // 行和列合并都有
                // if (merged.get("mergedRow")&& merged.get("mergedColumn")) {
                // }
                if (data.size() > 0) {
                    // 将每一条数据的长度都设置成所有列中最长长度,不足用空字符串补齐
                    for (List<String> list2 : data) {
                        list2.addAll(Arrays.asList(new String[size - list2.size()]));
                        if (!content.contains(list2) && !list2.contains(UNIQUE_UUID)) {
                            content.add(list2);
                        }
                    }
                }
                if (content.size() > 0) {
                    header = content.get(headerLine);
                    content.remove(headerLine);
                    dataTable.setHeader(header);
                    dataTable.setContent(content);
                    dtList.add(dataTable);
                }
            }
        }
        if (dtList != null && dtList.size() > 0) {
            return dtList;
        } else {
            throw new IOException("无效内容,请检查文件内容是否规范,正文内容不能出现合并列!!");
        }

    }

    /**
     * 
     * @Description: 判断合并的行
     * @author lianhanwen
     * @date 2017年5月15日 下午2:57:21
     * @param sheet
     * @param row
     * @param column
     * @return
     */
    private boolean isMergedRow(Sheet sheet, int row, int column) {

        int sheetMergeCount = sheet.getNumMergedRegions();
        for (int i = 0; i < sheetMergeCount; i++) {
            CellRangeAddress range = sheet.getMergedRegion(i);
            int firstColumn = range.getFirstColumn();
            int lastColumn = range.getLastColumn();
            int firstRow = range.getFirstRow();
            int lastRow = range.getLastRow();
            if (row == firstRow && row == lastRow) {
                if (column >= firstColumn && column <= lastColumn) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 
     * @Description: 判断指定的单元格是否是合并单元格
     * @author lianhanwen
     * @date 2017年5月15日 下午2:57:09
     * @param sheet
     * @param row
     * @param column
     * @return
     */
    private boolean isMergedRegion(Sheet sheet, int row, int column) {

        int sheetMergeCount = sheet.getNumMergedRegions();
        for (int i = 0; i < sheetMergeCount; i++) {

            CellRangeAddress range = sheet.getMergedRegion(i);
            int firstColumn = range.getFirstColumn();
            int lastColumn = range.getLastColumn();
            int firstRow = range.getFirstRow();
            int lastRow = range.getLastRow();
            if (row >= firstRow && row <= lastRow) {
                if (column >= firstColumn && column <= lastColumn) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 获取合并单元格的值
     * 
     * @param sheet
     * @param row
     * @param column
     * @return
     */
    public String getMergedRegionValue(Sheet sheet, int row, int column) {
        int sheetMergeCount = sheet.getNumMergedRegions();

        for (int i = 0; i < sheetMergeCount; i++) {
            CellRangeAddress ca = sheet.getMergedRegion(i);
            int firstColumn = ca.getFirstColumn();
            int lastColumn = ca.getLastColumn();
            int firstRow = ca.getFirstRow();
            int lastRow = ca.getLastRow();

            if (row >= firstRow && row <= lastRow) {

                if (column >= firstColumn && column <= lastColumn) {
                    Row fRow = sheet.getRow(firstRow);
                    Cell fCell = fRow.getCell(firstColumn);
                    return getCellValue(fCell);
                }
            }
        }

        return null;
    }

    public boolean isNeedCompare() {
        return isNeedCompare;
    }

    public void setNeedCompare(boolean isNeedCompare) {
        this.isNeedCompare = isNeedCompare;
    }

    public int getComparePos() {
        return comparePos;
    }

    public void setComparePos(int comparePos) {
        this.comparePos = comparePos;
    }

    public int getStartReadPos() {
        return startReadPos;
    }

    public void setStartReadPos(int startReadPos) {
        this.startReadPos = startReadPos;
    }

    public int getEndReadPos() {
        return endReadPos;
    }

    public void setEndReadPos(int endReadPos) {
        this.endReadPos = endReadPos;
    }

    public boolean isOverWrite() {
        return isOverWrite;
    }

    public void setOverWrite(boolean isOverWrite) {
        this.isOverWrite = isOverWrite;
    }

    public boolean isOnlyReadOneSheet() {
        return onlyReadOneSheet;
    }

    public void setOnlyReadOneSheet(boolean onlyReadOneSheet) {
        this.onlyReadOneSheet = onlyReadOneSheet;
    }

    public int getSelectedSheetIdx() {
        return selectedSheetIdx;
    }

    public void setSelectedSheetIdx(int selectedSheetIdx) {
        this.selectedSheetIdx = selectedSheetIdx;
    }

    public String getSelectedSheetName() {
        return selectedSheetName;
    }

    public void setSelectedSheetName(String selectedSheetName) {
        this.selectedSheetName = selectedSheetName;
    }

    public int getStartSheetIdx() {
        return startSheetIdx;
    }

    public void setStartSheetIdx(int startSheetIdx) {
        this.startSheetIdx = startSheetIdx;
    }

    public int getEndSheetIdx() {
        return endSheetIdx;
    }

    public void setEndSheetIdx(int endSheetIdx) {
        this.endSheetIdx = endSheetIdx;
    }

    public boolean isPrintMsg() {
        return printMsg;
    }

    public void setPrintMsg(boolean printMsg) {
        this.printMsg = printMsg;
    }

}
