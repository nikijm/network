package com.jk.ndt.etl.entity.logs;

import java.util.Date;

import com.jk.ndt.etl.entity.access.SheetInfo;
import com.jk.ndt.etl.entity.system.Admin;

public class SheetLog {
    private Integer id;
    private SheetInfo sheet;// 操作数据所属sheet
    private Admin user;// 操作用户
    private String operation; // 缓存，验证，清洗，转换，校验
    private Date opDate;// 操作时间
    private String request; // 把操作的请求参数原样保存
    private String source; // 数据源的表,如 etl_original_xxx
    private String target; // 数据从数据源到的表,如 etl_cleaned_xxx

    public SheetLog() {
        super();
    }

    public SheetLog(SheetInfo sheet, Admin user, String operation, Date opDate, String request, String source,
            String target) {
        super();
        this.sheet = sheet;
        this.user = user;
        this.operation = operation;
        this.opDate = opDate;
        this.request = request;
        this.source = source;
        this.target = target;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public SheetInfo getSheet() {
        return sheet;
    }

    public void setSheet(SheetInfo sheet) {
        this.sheet = sheet;
    }

    public Admin getUser() {
        return user;
    }

    public void setUser(Admin user) {
        this.user = user;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public Date getOpDate() {
        return opDate;
    }

    public void setOpDate(Date opDate) {
        this.opDate = opDate;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    @Override
    public String toString() {
        return "SheetLog [id=" + id + ", sheet=" + sheet + ", user=" + user + ", operation=" + operation + ", opDate="
                + opDate + ", request=" + request + ", source=" + source + ", target=" + target + "]";
    }

}
