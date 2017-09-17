package com.jk.ndt.etl.converter.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.jk.ndt.etl.utility.Checker;

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

    public DataTable(List<Map<String, Object>> data,String tableName) {
        this.tableName = tableName;
        this.header = new ArrayList<String>();
        this.content = new ArrayList<List<String>>();
        if(Checker.isNotNullOrEmpty(data)){
            Map<String,Object> map = data.get(0);
            this.header = new ArrayList<String>(map.keySet());
            for (Map<String, Object> mapData : data) {
                List<String> contentList = new ArrayList<String>();
                for (String column : header) {
                    contentList.add(mapData.get(column).toString());
                }
                content.add(contentList);
            }
        }
    }


    /**
     * 表名
     */
    private String tableName;
    /**
     * 表头列表,扩展使用
     */
    private List<String> header;

    /**
     * 表内容
     */
    private List<List<String>> content;

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

}
