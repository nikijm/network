package com.jk.ndtetl.etl.service.impl;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.jk.ndtetl.etl.DataTable;
import com.jk.ndtetl.etl.service.FileConverter;
import com.jk.ndtetl.util.Checker;

import au.com.bytecode.opencsv.CSVReader;

/**
 * 
 * @ClassName: CsvFileConverter
 * @Description: Csv文件转换器
 * @author fangwei
 * @date 2017年5月13日 下午4:30:19
 *
 */
@Component
public class CSVConverter implements FileConverter {
    private static final String COMMA = ",";
    // 从第二行开始读取，跳过表头
    private final static int START_ROW = 2;
    private final static String CHARACTERS = ",";

    @Override
    public List<DataTable> convert(String filePath) {
        File file = new File(filePath);
        return this.convert(file);
    }

    @Override
    public List<DataTable> convert(File file) {
        List<DataTable> list = new ArrayList<DataTable>();
        Map<String, List<List<String>>> tableMap = new HashMap<String, List<List<String>>>();
        try {
            tableMap = readCSVFileWithMap(file, START_ROW, CHARACTERS);
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<List<String>> headers = tableMap.get("csvFileFirstRow");
        List<List<String>> contents = tableMap.get("csvFileContent");
        if (Checker.isNotNullOrEmpty(headers) && Checker.isNotNullOrEmpty(contents)) {
            List<String> headerList = headers.get(0);
            DataTable dataTable = new DataTable();
            dataTable.setContent(contents);
            dataTable.setHeader(headerList);
            list.add(dataTable);
        }
        return list;
    }

    /**
     * 
     * @Description: 根据分割符读取制定csv文件内容
     * @author fangwei
     * @date 2017年5月13日 下午4:18:44
     * @param file
     * @param startRow
     * @param characters
     * @return
     * @throws IOException
     */
    @SuppressWarnings("unused")
    private List<List<String>> readCSVFile(File file, int startRow, String characters) throws IOException {
        List<List<String>> strArrayList = new ArrayList<List<String>>();
        CSVReader reader = null;
        if (COMMA.equalsIgnoreCase(characters)) {
            DataInputStream in = new DataInputStream(new FileInputStream(file));
            reader = new CSVReader(new InputStreamReader(in, "UTF-8"), ',');
        } else {
            // 带分隔符
            DataInputStream in = new DataInputStream(new FileInputStream(file));
            reader = new CSVReader(new InputStreamReader(in, "UTF-8"), characters.toCharArray()[0]);
        }
        try {
            String[] nextLine;
            int i = 1;
            while ((nextLine = reader.readNext()) != null) {
                if (i >= startRow) {
                    strArrayList.add(Arrays.asList(nextLine));
                }
                i++;
            }
        } finally {
            reader.close();
        }

        return strArrayList;
    }

    /**
     * 
     * @Description: 读取csv中的内容 Map key:csvFileFirstRow csv文件，内容(除去表头内容)，表头标题；
     *               key:csvFileContent
     * @author fangwei
     * @date 2017年5月13日 下午4:24:21
     * @param file
     * @param startRow
     * @param characters
     * @return
     * @throws IOException
     */
    private Map<String, List<List<String>>> readCSVFileWithMap(File file, int startRow, String characters)
            throws IOException {
        List<List<String>> csvFileFirstRowArrayList = new ArrayList<List<String>>();
        List<List<String>> strArrayList = new ArrayList<List<String>>();
        CSVReader reader = null;
        if (COMMA.equalsIgnoreCase(characters)) {
            reader = new CSVReader(new FileReader(file));
        } else {
            // 带分隔符
            reader = new CSVReader(new FileReader(file), characters.toCharArray()[0]);
        }
        Map<String, List<List<String>>> map = new HashMap<String, List<List<String>>>();
        try {
            String[] nextLine;
            int i = 1;
            while ((nextLine = reader.readNext()) != null) {
                if (i >= startRow) {
                    strArrayList.add(Arrays.asList(nextLine));
                } else {
                    csvFileFirstRowArrayList.add(Arrays.asList(nextLine));
                }
                i++;
            }

            map.put("csvFileFirstRow", csvFileFirstRowArrayList);
            map.put("csvFileContent", strArrayList);
        } finally {
            reader.close();
        }
        return map;
    }

}
