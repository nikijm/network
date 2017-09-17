package com.jk.ndtetl.controller.etl;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.github.pagehelper.PageInfo;
import com.jk.ndtetl.BaseSystemEntity;
import com.jk.ndtetl.dbmeta.ColumnDef;
import com.jk.ndtetl.util.CommUtil;
import com.jk.ndtetl.util.etl.TransformerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.jk.ndtetl.controller.BaseController;
import com.jk.ndtetl.controller.BasePage;
import com.jk.ndtetl.dbmeta.TableDef;
import com.jk.ndtetl.dbmeta.service.IBusinessTypeService;
import com.jk.ndtetl.dbmeta.service.IDataFileService;
import com.jk.ndtetl.dbmeta.service.ITableDefService;
import com.jk.ndtetl.etl.service.ICommonService;
import com.jk.ndtetl.system.service.IOrganizationService;
import com.jk.ndtetl.util.Checker;
import com.jk.ndtetl.util.ErrorUtil;
import com.jk.ndtetl.util.PageData;

/**
 * 
 * @ClassName: AnalysisController
 * @Description: 解析文件的controller
 * @author lianhanwen
 * @date 2017年5月20日 下午3:43:25
 *
 */
@Controller
@RequestMapping(value = "/api")
public class ConvertController extends BaseController {

    @Autowired
    private IDataFileService iDataFileService;
    @Autowired
    private IBusinessTypeService iBusinessTypeService;
    @Autowired
    private IOrganizationService iOrganizationService;
    @Autowired
    private ITableDefService iTableDefService;
    @Autowired
    private ICommonService iCommonService;
    /**
     * 待缓存列表
     * keyword 查询内容
     * @param request
     * @param orgId 机构id
     * @param etlBusinessTypeId 业务类型id
     * @param basePage
     * @return
     */
    @RequestMapping(value = "/converts",method = RequestMethod.GET)
    @ResponseBody
    public Object listCacheDataFiles(HttpServletRequest request, Integer orgId, Integer etlBusinessTypeId, BasePage basePage){
        JSONObject errors = new JSONObject();
        String msg = null;

        JSONObject rs = new JSONObject();

        rs.put("bussinessTypes",listBusinessTypeOptions(iBusinessTypeService,null));
        rs.put("orgs", listOrgOptions(iOrganizationService,null));
        rs.put("datas", "");
        rs.put("header", "");

        Map<String, Object> param = PageData.getParamMap(request);
        param.put("keyword", CommUtil.getSpecialCharQuery(param.get("keyword")==null?"":param.get("keyword").toString()).toLowerCase());
        basePage.setParam(param);
        TableDef tableDef = iTableDefService.getTableByCategoryAndBusinessType(TableDef.CATEGORY_CONVERT, etlBusinessTypeId);
        if (Checker.isNullOrEmpty(tableDef)) {
            return rs;
        }
        List<ColumnDef> columns = tableDef.getColumns();
        List<Map<String, Object>> dataList = iTableDefService.getDataByOrgAndBusinessType(TableDef.CATEGORY_CACHE,
                orgId, etlBusinessTypeId, basePage);


        PageInfo pageInfo = new PageInfo(dataList);
        basePage.setTotal(pageInfo.getTotal());
        basePage.setParam(null);

        rs.put(BaseSystemEntity.BASE_PAGE, basePage);
        rs.put("datas", dataList);
        rs.put("header", TransformerUtil.getHeader(columns));
        return rs;
    }

}
