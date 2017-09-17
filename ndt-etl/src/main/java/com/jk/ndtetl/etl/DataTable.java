package com.jk.ndtetl.etl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.jk.ndtetl.util.Checker;

/**
 * 
 * @ClassName: DataTable
 * @Description: 封装文件数据，形成一个虚拟的表结构
 * @author fangwei
 * @date 2017年5月13日 下午2:20:32
 *
 */
public class DataTable {

    public DataTable() {
        super();
    }

    public DataTable(List<Map<String, Object>> data, String tableName) {
        this.tableName = tableName;
        this.header = new ArrayList<String>();
        this.content = new ArrayList<List<String>>();
        if (Checker.isNotNullOrEmpty(data)) {
            Map<String, Object> map = data.get(0);
            this.header = new ArrayList<String>(map.keySet());
            for (Map<String, Object> mapData : data) {
                List<String> contentList = new ArrayList<String>();
                for (String column : header) {
                    contentList.add(mapData.get(column) == null ? null : mapData.get(column).toString());
                }
                content.add(contentList);
            }
        }
    }

    private List<Integer> unUseColumnNums = new ArrayList<>();

    /**
     * 表名
     */
    private String tableName;

    /**
     * 总行数,判断文件是否解析完成
     */
    private Integer lastRowNum;

    /**
     * 文件的全局唯一编码uuid
     */
    private String datafile_unid;

    /**
     * 表头列表,扩展使用
     */
    private List<String> header = new ArrayList<>();

    /**
     * 表内容
     */
    private List<List<String>> content = new ArrayList<>();

    public List<String> getHeader() {
        return header;
    }

    public void setHeader(List<String> header) {
        this.header = header;
    }

    public List<List<String>> getContent() {
        return content;
    }

    public void setContent(List<List<String>> content) {
        this.content = content;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public Integer getLastRowNum() {
        return lastRowNum;
    }

    public void setLastRowNum(Integer lastRowNum) {
        this.lastRowNum = lastRowNum;
    }

    public String getDatafile_unid() {
        return datafile_unid;
    }

    public void setDatafile_unid(String datafile_unid) {
        this.datafile_unid = datafile_unid;
    }

    public List<Integer> getUnUseColumnNums() {
        return unUseColumnNums;
    }

    public void setUnUseColumnNums(List<Integer> unUseColumnNums) {
        this.unUseColumnNums = unUseColumnNums;
    }
    
    public void addHeader(String header){
    	this.header.add(header);
    }
    
    public void addContent(List<String> list){
    	this.content.add(list);
    }

}
