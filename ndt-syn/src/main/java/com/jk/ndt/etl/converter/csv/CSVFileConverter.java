package com.jk.ndt.etl.converter.csv;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.jk.ndt.etl.converter.FileConverter;
import com.jk.ndt.etl.converter.model.DataTable;
import com.jk.ndt.etl.utility.Checker;

/**
 * 
 * @ClassName: CsvFileConverter
 * @Description: Csv文件转换器
 * @author fangwei
 * @date 2017年5月13日 下午4:30:19
 *
 */
@Component
public class CSVFileConverter implements FileConverter {

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
            tableMap = CSVFileUtil.readCSVFileWithMap(file, START_ROW, CHARACTERS);
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

}
