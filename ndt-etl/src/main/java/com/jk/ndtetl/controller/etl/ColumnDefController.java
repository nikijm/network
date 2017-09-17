package com.jk.ndtetl.controller.etl;/**
 * Created by polite on 2017/7/3.
 */

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.jk.ndtetl.BaseSystemEntity;
import com.jk.ndtetl.controller.BaseController;
import com.jk.ndtetl.controller.BasePage;
import com.jk.ndtetl.dbmeta.ColumnDef;
import com.jk.ndtetl.dbmeta.TableDef;
import com.jk.ndtetl.dbmeta.service.IColumnDefService;
import com.jk.ndtetl.dbmeta.service.ITableDefService;
import com.jk.ndtetl.etl.service.ICommonService;
import com.jk.ndtetl.etl.service.IDDLExecutorService;
import com.jk.ndtetl.util.Checker;
import com.jk.ndtetl.util.ErrorUtil;
import com.jk.ndtetl.util.PageData;
import com.sun.xml.internal.ws.addressing.EndpointReferenceUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * 朱生
 * 字段维护
 *
 * @create 2017-07-03 14:10
 **/
@Controller
@RequestMapping(value = "/api")
public class ColumnDefController extends BaseController {

    @Autowired
    private IDDLExecutorService iddlExecutorService;
    @Autowired
    private IColumnDefService iColumnDefService;
    @Autowired
    private ICommonService iCommonService;
    @Autowired
    private ITableDefService iTableDefService;
    /**
     * 保存
     * @param columnDef
     * @return
     */
    @RequestMapping(value = "/columDefs", method = RequestMethod.POST)
    @ResponseBody
    public Object saveColumDef(@RequestBody @Valid ColumnDef columnDef,BindingResult result) {
        JSONObject errors = new JSONObject();
        String msg=null;
        TableDef tableDef=new TableDef();
        tableDef.setEtlTableId(columnDef.getTableDefId());
        columnDef.setTableDef(tableDef);
        TableDef queryTableDef = iTableDefService.getById(columnDef.getTableDefId());
        if (!iCommonService.isNewColumnNameAvailable(queryTableDef.getTableName(), columnDef.getColumnName())) {
            errors.put(columnDef.getColumnName(),"字段名重复");
            if (null == msg) {
                msg = "字段名重复";
            }
        }
        if (Checker.isNotNullOrEmpty(errors)) {
            return ErrorUtil.getRequestError(errors, msg);
        }
        iColumnDefService.save(columnDef);
        return columnDef.getEtlColumnId();
    }

    /**
     * 修改
     *
     * @param columnDef
     * @return
     */
    @RequestMapping(value = "/columDefs/{id}", method = RequestMethod.PUT)
    @ResponseBody
    public Object updateColumDef(@PathVariable("id") Integer id,@RequestBody @Valid ColumnDef columnDef,BindingResult result) {
        columnDef.setEtlColumnId(id);

        TableDef tableDef=new TableDef();
        tableDef.setEtlTableId(columnDef.getTableDefId());
        columnDef.setTableDef(tableDef);

        iColumnDefService.update(columnDef);
        return null;
    }

    /**
     * 列表
     * @return
     */
    @RequestMapping(value = "/columDefs", method = RequestMethod.GET)
    @ResponseBody
    public Object listColumDef(BasePage basePage, HttpServletRequest request) {
        JSONObject rs = new JSONObject();
        basePage.setParam(PageData.getParamMap(request));
        PageInfo pageInfo = new PageInfo(iColumnDefService.listByPage(basePage));
        rs.put("columDefs", pageInfo.getList());
        rs.put(BaseSystemEntity.BASE_PAGE, pageInfo.getTotal());
        return  rs;
    }

    /**
     * 删除
     * @return
     */
    @RequestMapping(value = "/columDefs/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public Object deleteColumDef(@PathVariable("id") Integer id) {
        iColumnDefService.deleteById(id);
        return null;
    }

}
