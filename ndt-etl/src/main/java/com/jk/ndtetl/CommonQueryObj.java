package com.jk.ndtetl;

/**
 * 
 * @ClassName: CommonQueryObj
 * @Description: 公共dao的查询对象封装
 * @author fangwei
 * @date 2017年6月5日 下午1:54:53
 *
 */
public class CommonQueryObj {

    public CommonQueryObj() {
        super();
    }

    public CommonQueryObj(String colunmName, String operator, Object param) {
        super();
        this.colunmName = colunmName;
        this.operator = operator;
        this.param = param;
    }

    /**
     * 查询的列名
     */
    private String colunmName;
    /**
     * 查询的操作符
     */
    private String operator;
    /**
     * 查询的参数
     */
    private Object param;

    public String getColunmName() {
        return colunmName;
    }

    public void setColunmName(String colunmName) {
        this.colunmName = colunmName;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public Object getParam() {
        return param;
    }

    public void setParam(Object param) {
        this.param = param;
    }

}
