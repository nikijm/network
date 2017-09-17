package com.jk.ndtetl.etl.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.jk.ndtetl.etl.DataTable;
import com.jk.ndtetl.etl.service.FileConverter;

/**
 * 
 * @ClassName: ExcelConverter
 * @Description: Excel文件解析类
 * @author lianhanwen
 * @date 2017年5月14日 下午6:48:44
 *
 */
public class ExcelConverter implements FileConverter {

    // ====================== 常量部分 ======================
    private final static String UNIQUE_UUID = "合并单元格标识" + "ad62-57e0d4640894";

    /**
     * 默认的开始读取的行位置为第一行（索引值为0）
     */
    private final static int READ_START_POS = 0;

    /**
     * 默认结束读取的行位置为最后一行（索引值=0，用负数来表示倒数第n行）
     */
    private final static int READ_END_POS = Integer.MAX_VALUE;

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

    // ====================== 字段部分 ======================
    private int readPosSize;

    /**
     * 设定开始读取的位置，默认为0
     */
    private int startReadPos = READ_START_POS;

    /**
     * 设定结束读取的位置，默认为0，用负数来表示倒数第n行
     */
    private int endReadPos = READ_END_POS;

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

    public ExcelConverter() {
        super();
    }

    /**
     * 构造方法
     * 
     * @param readPosSize
     * @param onlyReadOneSheet
     */
    public ExcelConverter(int readPosSize, boolean onlyReadOneSheet) {
        super();
        this.readPosSize = readPosSize;
        this.onlyReadOneSheet = onlyReadOneSheet;
    }

    @Override
    public List<DataTable> convert(String filePath) throws Exception {
        List<DataTable> dtList = readExcel(filePath);
        return dtList;
    }

    @Override
    public List<DataTable> convert(File file) throws Exception {
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
    public List<DataTable> readExcel(String filePath) throws IOException {
        // 文件路径为空时
        if ("".equals(filePath)) {
            throw new IOException("文件路径不能为空！");
        }
        File file = new File(filePath);
        if (!file.exists()) {
            throw new IOException("文件不存在！");
        }
        return readExcel(file);
    }

    public List<DataTable> readExcel(File file) throws IOException {
        if (!file.exists()) {
            throw new IOException("文件不存在！");
        }
        // 获取扩展名
        String xlsPath = file.getPath();
        String ext = xlsPath.substring(xlsPath.lastIndexOf(".") + 1);

        if ("xls".equals(ext)) {
            // 使用xls方式读取
            return readExcel_xls(xlsPath);
        }
        else if ("xlsx".equals(ext)) {
            // 使用xlsx方式读取
            return readExcel_xlsx(xlsPath);
        }
        else {
            // 依次尝试xls、xlsx方式读取
            try {
                return readExcel_xls(xlsPath);
            }
            catch (IOException e1) {
                try {
                    return readExcel_xlsx(xlsPath);
                }
                catch (IOException e2) {
                    throw e2;
                }
            }
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
        // 判断文件是否存在
        File file = new File(xlsPath);
        if (!file.exists()) {
            throw new IOException("文件名为" + file.getName() + "Excel文件不存在！");
        }

        // 读取Excel 2007版，xlsx格式
        FileInputStream fis = new FileInputStream(file);
        XSSFWorkbook wb = new XSSFWorkbook(fis);
        List<DataTable> dtList = readExcel(wb);

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
        // 判断文件是否存在
        File file = new File(xlsPath);
        if (!file.exists()) {
            throw new IOException("文件名为" + file.getName() + "Excel文件不存在！");
        }
        // 读取Excel 97-03版，xls格式
        HSSFWorkbook wb = new HSSFWorkbook(new FileInputStream(file));
        List<DataTable> dtList = readExcel(wb);

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
                if (HSSFDateUtil.isCellDateFormatted(cell)) {
                    // 处理日期格式、时间格式
                    SimpleDateFormat sdf = null;
                    if (cell.getCellStyle().getDataFormat() == HSSFDataFormat.getBuiltinFormat("h:mm")) {
                        sdf = new SimpleDateFormat("HH:mm");
                    }
                    else {
                        // 日期
                        sdf = new SimpleDateFormat("yyyy-MM-dd");
                    }
                    Date date = cell.getDateCellValue();
                    result = sdf.format(date);
                }
                else if (cell.getCellStyle().getDataFormat() == 58) {

                    // 处理自定义日期格式：m月d日(通过判断单元格的格式id解决，id的值是58)
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    double value = cell.getNumericCellValue();
                    Date date = org.apache.poi.ss.usermodel.DateUtil.getJavaDate(value);
                    result = sdf.format(date);
                }
                else {
                    double value = cell.getNumericCellValue();
                    CellStyle style = cell.getCellStyle();
                    DecimalFormat format = new DecimalFormat();
                    String temp = style.getDataFormatString();
                    // 单元格设置成常规
                    if (temp.equals("General")) {
                        format.applyPattern("#");
                    }
                    result = format.format(value);
                }
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
                // result="";
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
        // 需要操作的sheet数量
        int sheetCount = 1;

        Sheet sheet = null;
        // 只操作一个sheet
        if (onlyReadOneSheet) {
            // 获取设定操作的sheet(如果设定了名称，按名称查，否则按索引值查)
            sheet = selectedSheetName.equals("") ? wb.getSheetAt(selectedSheetIdx) : wb.getSheet(selectedSheetName);
            // 操作多个sheet
        }
        else {
            // 获取可以操作的总数量
            sheetCount = wb.getNumberOfSheets();
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

                Row row = null;
                DataTable dataTable = new DataTable();
                dataTable.setLastRowNum(lastRowNum);
                short size = 0;
                // 循环读取
                List<List<String>> data = new ArrayList<>();
                List<List<String>> content = new ArrayList<>();
                endReadPos = this.getEndReadPos();
                for (int i = startReadPos; i <= (endReadPos < lastRowNum ? endReadPos : lastRowNum); i++) {
                    row = sheet.getRow(i);
                    if (row != null) {
                        // 计算数据中的最长size
                        short lastCellNum = row.getLastCellNum();
                        size = lastCellNum > size ? lastCellNum : size;
                        // 获取每一单元格的值
                        ArrayList<String> list = new ArrayList<>();
                        for (int j = 0; j < row.getLastCellNum(); j++) {

                            boolean mergedColumn = isMergedColumn(sheet, i, j);
                            boolean mergedRow = isMergedRow(sheet, i, j);
                            boolean mergedRegion = isMergedRegion(sheet, i, j);

                            // 如果有列合并的
                            if (mergedRegion && !mergedRow) {
                                String mergedRegionValue = getMergedRegionValue(sheet, i, j);
                                if (!list.contains(mergedRegionValue)) {
                                    list.add(mergedRegionValue);
                                }
                                else {
                                    list.add(UNIQUE_UUID);
                                }
                                continue;
                            }
                            // 如果只有行合并的
                            if (!mergedColumn && mergedRow) {
                                String mergedRegionValue = getMergedRegionValue(sheet, i, j);
                                list.add(mergedRegionValue);
                                continue;
                            }
                            String value = getCellValue(row.getCell(j));
                            list.add(value);
                        }
                        // 判断一行数据是否全部为空,不为空才添加到content中
                        boolean flag = false;
                        for (String str : list) {
                            if (!"".equals(str.trim())) {
                                flag = true;
                                continue;
                            }
                        }
                        if (flag && !list.contains(UNIQUE_UUID) && !data.contains(list)) {
                            data.add(list);
                            List<String> list2 = new ArrayList<>(list);
                            // 添加行号
                            list2.add(0, i + "");
                            content.add(list2);
                        }
                    }
                }

                List<String> header = new ArrayList<>();
                int headerLine = 0;
                if (content.size() > 0) {
                    // 将每一条数据的长度都设置成所有列中最长长度,不足用空字符串补齐
                    for (List<String> list2 : content) {
                        list2.addAll(Arrays.asList(new String[size + 1 - list2.size()]));
                    }
                    if (startReadPos == 0) {
                        header = content.get(headerLine);
                        header.remove(0);
                        content.remove(headerLine);
                        dataTable.setHeader(header);
                    }
                    dataTable.setContent(content);
                }
                dtList.add(dataTable);
            }
        }
        if (dtList == null || dtList.size() <= 0) {
            throw new IOException("文件内容为空");
        }
        // if (dtList.get(0).getContent() == null &&
        // dtList.get(0).getContent().size() <= 0 && startReadPos == 0) {
        // throw new IOException("无效内容,请检查文件内容是否规范,正文内容不能出现合并列!!");
        // }
        return dtList;

    }

    /**
     * 
     * @Description: 判断只有合并的列
     * @author lianhanwen
     * @date 2017年5月15日 下午2:57:21
     * @param sheet
     * @param row
     * @param column
     * @return
     */
    private boolean isMergedColumn(Sheet sheet, int row, int column) {

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
     * @Description: 判断是只有合并的行
     * @author lianhanwen
     * @date 2017年6月14日 下午3:26:35
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
            if (column == firstColumn && column == lastColumn) {
                if (row >= firstRow && row <= lastRow) {
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

    public int getStartReadPos() {
        return startReadPos;
    }

    public void setStartReadPos(int startReadPos) {
        this.startReadPos = startReadPos;
    }

    public int getEndReadPos() {
        if (readPosSize != 0) {
            endReadPos = startReadPos + readPosSize-1;
        }
        return endReadPos;
    }

    public void setEndReadPos(int endReadPos) {
        this.endReadPos = endReadPos;
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

    public int getReadPosSize() {
        return readPosSize;
    }

    public void setReadPosSize(int readPosSize) {
        this.readPosSize = readPosSize;
    }

}
