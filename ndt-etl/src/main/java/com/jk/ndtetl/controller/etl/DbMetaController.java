package com.jk.ndtetl.controller.etl;/**
 * Created by polite on 2017/6/23.
 */

import java.util.List;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.github.pagehelper.Page;
import com.jk.ndtetl.controller.BaseController;
import com.jk.ndtetl.dbmeta.BusinessType;
import com.jk.ndtetl.dbmeta.ColumnDef;
import com.jk.ndtetl.dbmeta.service.IBusinessTypeService;
import com.jk.ndtetl.dbmeta.service.IColumnDefService;
import com.jk.ndtetl.dbmeta.service.IDbMetaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.alibaba.fastjson.JSONObject;
import com.jk.ndtetl.controller.BasePage;
import com.jk.ndtetl.dbmeta.TableDef;
import com.jk.ndtetl.dbmeta.service.ITableDefService;
import com.jk.ndtetl.etl.service.ICommonService;
import com.jk.ndtetl.etl.service.IDDLExecutorService;
import com.jk.ndtetl.util.Checker;
import com.jk.ndtetl.util.ErrorUtil;
import com.jk.ndtetl.util.PageData;
import com.jk.ndtetl.util.StringUtils;

/**
 * 朱生
 *
 * @create 2017-06-23 16:49
 **/
@Controller
@RequestMapping("/api")
public class DbMetaController extends BaseController {
    @Autowired
    private ICommonService iCommonService;
    @Autowired
    private ITableDefService iTableDefService;
    @Autowired
    private IColumnDefService iColumnDefService;
    @Autowired
    private IDbMetaService iDbMetaService;
    @Autowired
    private IDDLExecutorService iddlExecutorService;
    @Autowired
    private IBusinessTypeService iBusinessTypeService;

    /**
     * 根据表名查询表详细信息
     *
     * @param request
     * @param basePage
     * @return
     */
    @RequestMapping(value = "/dbmeta", method = RequestMethod.GET)
    @ResponseBody
    public Object queryDbMetaByTableName(BasePage basePage, HttpServletRequest request) {
//        return ErrorUtil.getRequestError(null, "该接口已过时，请调用/tableDefs");
        JSONObject rs = new JSONObject();
        basePage.setParam(PageData.getParamMap(request));
//        rs.put("tableDefs",assembleResult(iTableDefService.listByPage(basePage)));
        rs.put("tableDefs", assembleResult(iTableDefService.listAll()));
        rs.put("businessTypes", iBusinessTypeService.listAll());
        return rs;
    }

    /**
     * 组装结果
     *
     * @param tableDefs
     * @return
     */
    private Object assembleResult(List<TableDef> tableDefs) {

        JSONArray rs = new JSONArray();
        if (Checker.isNotNullOrEmpty(tableDefs)) {
            for (TableDef tableDef_r : tableDefs) {
                JSONObject tableDefObject = new JSONObject();
                tableDefObject.put("etlTableId", tableDef_r.getEtlTableId());
                tableDefObject.put("name", tableDef_r.getName());
                tableDefObject.put("tableName", tableDef_r.getTableName());
                tableDefObject.put("description", tableDef_r.getDescription());
                tableDefObject.put("category", tableDef_r.getDescription());
                tableDefObject.put("businessTypeId", tableDef_r.getBusinessType() == null ? "" : tableDef_r.getBusinessType().getEtlBusinessTypeId());
                tableDefObject.put("businessTypeName", tableDef_r.getBusinessType() == null ? "" : tableDef_r.getBusinessType().getName());
                JSONArray columnList=new JSONArray();
                if (Checker.isNotNullOrEmpty(tableDef_r.getColumns())) {
                    for (ColumnDef columnDef : tableDef_r.getColumns()) {
                        JSONObject column = new JSONObject();
                        column.put("etlColumnId", columnDef.getEtlColumnId());
                        column.put("columnName", columnDef.getColumnName());
                        column.put("dataType", columnDef.getDataType());
                        column.put("fieldLength", columnDef.getFieldLength());
                        column.put("name", columnDef.getName());
                        column.put("description", columnDef.getDescription());
                        column.put("seqNo", columnDef.getSeqNo());
                        column.put("isSearch", columnDef.getIsSearch());
                        columnList.add(column);
                    }
                }
                tableDefObject.put("columns", columnList);
                rs.add(tableDefObject);
            }
        }
        return rs;
    }

    /**
     * 创建表
     *
     * @param request
     * @param tableDef 参数
     * @return
     */
    @RequestMapping(value = "/dbmeta", method = RequestMethod.POST)
    @ResponseBody
    public Object createTable(HttpServletRequest request, @Valid @RequestBody TableDef tableDef, BindingResult result) {
//        return ErrorUtil.getRequestError(null, "请调用/tableDefs  POST");
        JSONObject errors = new JSONObject();
        String msg = checkParam(tableDef, errors);
        if (Checker.isNotNullOrEmpty(errors)) {
            return ErrorUtil.getRequestError(errors, msg);
        }
        Integer rs_talbleId = null;
        tableDef.setTableName(tableDef.getTableName().toUpperCase());
        String tableNameTmp = addPrefixForTableName(tableDef);
        BusinessType businessType = new BusinessType();
        businessType.setEtlBusinessTypeId(tableDef.getEtlBusinessTypeId());
        tableDef.setBusinessType(businessType);
        try {
            saveTableDefAndColumns(tableDef);
            rs_talbleId = tableDef.getEtlTableId();
            if (null != tableNameTmp) {
                if (StringUtils.equals(TableDef.CATEGORY_CACHE, tableDef.getCategory())) {
                    tableDef.setCategory(TableDef.CATEGORY_CLEAN);
                }
                if (StringUtils.equals(TableDef.CATEGORY_CONVERT, tableDef.getCategory())) {
                    tableDef.setCategory(TableDef.CATEGORY_VERIFY);
                }
                tableDef.setTableName(tableNameTmp);
                tableDef.setEtlTableId(null);
                saveTableDefAndColumns(tableDef);
            }

        } catch (Exception e) {

            errors.put("createTableFailed", "创建表失败");
            errors.put("createTableFailedDetail", e.getMessage());
            logger.debug("创建表失败：", e.getMessage());
            logger.debug("创建表失败：", e.getCause());
            if (null == msg) {
                msg = "创建表失败";
            }
            e.printStackTrace();
        }
        if (Checker.isNotNullOrEmpty(errors)) {
            return ErrorUtil.getRequestError(errors, msg);
        }
        return rs_talbleId;
    }

    /**
     * 添加表名前缀
     *
     * @param tableDef
     * @return
     */
    private String addPrefixForTableName(TableDef tableDef) {
        String tableNameTmp = null;
        if (StringUtils.equals(TableDef.CATEGORY_CACHE, tableDef.getCategory())) {
            if (tableDef.isCreateCleanTable()) {
                tableNameTmp = TableDef.CLEANTABLE_PREFIX + tableDef.getTableName();
            }
            tableDef.setTableName(TableDef.CACHETABLE_PREFIX + tableDef.getTableName());
        }
        if (StringUtils.equals(TableDef.CATEGORY_CLEAN, tableDef.getCategory())) {
            tableDef.setTableName(TableDef.CLEANTABLE_PREFIX + tableDef.getTableName());
        }
        if (StringUtils.equals(TableDef.CATEGORY_CONVERT, tableDef.getCategory())) {
            if (tableDef.isCreateVerifyTable()) {
                tableNameTmp = TableDef.VERIFYTABLE_PREFIX + tableDef.getTableName();
            }
            tableDef.setTableName(TableDef.CONVERTTABLE_PREFIX + tableDef.getTableName());
        }
        if (StringUtils.equals(TableDef.CATEGORY_VERIFY, tableDef.getCategory())) {
            tableDef.setTableName(TableDef.VERIFYTABLE_PREFIX + tableDef.getTableName());
        }
        return tableNameTmp;
    }

    /**
     * before保存表,参数检测
     *
     * @param tableDef
     * @param errors
     * @return
     */
    private String checkParam(TableDef tableDef, JSONObject errors) {
        String msg = null;

        if (StringUtils.isBlank(tableDef.getTableName())) {
            errors.put(tableDef.getTableName(), "表名不能为空");
            msg = "表名不能为空";
        }
        if (null == tableDef.getEtlBusinessTypeId()) {
            errors.put("etlBusinessTypeId", "请选择业务类型");
            if (null == msg) {
                msg = "请选择业务类型";
            }
        }
        if (StringUtils.isBlank(tableDef.getName())) {
            errors.put(tableDef.getName(), "别名不能为空");
            if (null == msg) {
                msg = "别名不能为空";
            }
        }
        if (!iCommonService.isNewTableNameAvailable(tableDef.getTableName())) {
            errors.put(tableDef.getTableName(), "表名重复");
            if (null == msg) {
                msg = "表名重复";
            }
        }
        if (Checker.isNullOrEmpty(tableDef.getColumns())) {
            errors.put("noColumns", "请添加表字段");
            if (null == msg) {
                msg = "请添加表字段";
            }
        }
        if (Checker.isNotNullOrEmpty(tableDef.getColumns())) {
            TreeSet<String> columnDefsSet = new TreeSet<>();
            for (ColumnDef columnDef : tableDef.getColumns()) {
                if (!columnDefsSet.add(columnDef.getName())) {
                    errors.put(tableDef.getTableName(), "字段重复");
                    if (null == msg) {
                        msg = "字段重复";
                    }
                    break;
                }
            }
        }
        TableDef tableDefExist = iTableDefService.getTableByCategoryAndBusinessType(tableDef.getCategory(), tableDef.getEtlBusinessTypeId());
        if (null != tableDefExist) {
            errors.put("categoryBussinessTypeId", "请重新选择业务类型");
            if (null == msg) {
                msg = "请重新选择业务类型";
            }
        }

        return msg;
    }

    /**
     * 保存表和字段、如果保存失败则删除对应的表
     *
     * @param tableDef
     * @throws Exception
     */
    private void saveTableDefAndColumns(TableDef tableDef) throws Exception {
        try {
            iddlExecutorService.createTable(tableDef);
            iDbMetaService.saveTableDefAndColumns(tableDef);
        } catch (Exception e) {
            iddlExecutorService.deleteTable(tableDef.getTableName());
            iddlExecutorService.deleteSeq(tableDef.getTableName());
            e.printStackTrace();
            throw new Exception(e);
        }
    }

    /**
     * 修改表
     *
     * @param request
     * @param response
     * @param tableDef 参数
     * @return
     */
    @RequestMapping(value = "/dbmeta", method = RequestMethod.PUT)
    @ResponseBody
    public Object alterTable(HttpServletRequest request, HttpServletResponse response, @Valid @RequestBody TableDef tableDef, BindingResult result) {
        JSONObject errors = new JSONObject();
        String msg = null;
        if (Checker.isNotNullOrEmpty(errors)) {
            return ErrorUtil.getRequestError(errors, msg);
        }
        iDbMetaService.updateTableAndColumn(tableDef);
        return null;
    }

    /**
     * 添加表字段
     *
     * @param request
     * @param tableDef 参数
     * @return
     */
    @RequestMapping(value = "/dbmeta/colunms", method = RequestMethod.POST)
    @ResponseBody
    public Object addColumns(HttpServletRequest request, @RequestBody @Valid TableDef tableDef, BindingResult result) {
//        return ErrorUtil.getRequestError(null, "请调用/tableDefs/{id}/colunms  POST");
        JSONObject errors = new JSONObject();
        String msg = null;
        if (Checker.isNotNullOrEmpty(tableDef.getColumns())) {
            for (ColumnDef columnDef : tableDef.getColumns()) {
                if (!iCommonService.isNewColumnNameAvailable(tableDef.getTableName(), columnDef.getColumnName())) {
                    // 重复
                    errors.put(columnDef.getColumnName(), "列名重复");
                    if (null == msg) {
                        msg = "列名重复";
                    }
                }
            }
            if (Checker.isNotNullOrEmpty(errors)) {
                return ErrorUtil.getRequestError(errors, msg);
            }
            iddlExecutorService.addColumn(tableDef.getTableName(), tableDef.getColumns());
            iColumnDefService.saveColumns(tableDef);
        }
        return null;
    }


}
