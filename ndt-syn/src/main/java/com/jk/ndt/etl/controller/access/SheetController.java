package com.jk.ndt.etl.controller.access;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import com.jk.ndt.etl.constant.Constant;
import com.jk.ndt.etl.controller.BaseController;
import com.jk.ndt.etl.entity.Page;
import com.jk.ndt.etl.entity.Paging;
import com.jk.ndt.etl.entity.access.SheetInfo;
import com.jk.ndt.etl.entity.logs.SheetLog;
import com.jk.ndt.etl.service.access.SheetInfoService;
import com.jk.ndt.etl.service.common.CommonService;
import com.jk.ndt.etl.service.logs.SheetLogService;
import com.jk.ndt.etl.utility.Checker;
import com.jk.ndt.etl.utility.TransStatusUtil;

/**
 * 
 * @ClassName: SheetController
 * @Description: sheet相关的api
 * @author lianhanwen
 * @date 2017年5月26日 上午10:16:43
 *
 */
@Controller

@RequestMapping(value = "/api")
public class SheetController extends BaseController {
    private final static String MAP_KEY_SHEETS = "sheets";
    private final static String MAP_KEY_DATAS = "data";
    private final static String SHEET_STATUS_CACHED = "cached";
    private final static String SHEET_STATUS_CLEANED = "cleaned";
    private final static String SHEET_STATUS_TRANSFORMED = "transformed";

    @Autowired
    private SheetInfoService sheetInfoService;
    @Autowired
    private CommonService commonService;
    @Autowired
    private SheetLogService sheetLogService;

    /**
     * 
     * @Description: 数据处理 / 工作表-Sheet / 获取单个工作表
     * @author lianhanwen
     * @date 2017年5月26日 上午11:12:30
     * @param id
     * @return
     */
    @RequestMapping(value = "/sheets/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Object getSheetById(@PathVariable Integer id) {
        SheetInfo sheetInfo = sheetInfoService.getById(id);
        TransStatusUtil.sheetsStatusToString(sheetInfo);
        return sheetInfo;
    }

    /**
     * 
     * @Description: 数据处理 / 工作表-Sheet / 获取工作表列表
     * @author lianhanwen
     * @date 2017年5月26日 下午12:50:26
     * @param search
     * @param filter
     * @param page
     * @return
     */
    @RequestMapping(value = "/sheets", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> listSheets(String search, String filter, Integer page) {
        Page pageObj = new Page();
        if (Checker.isNotNullOrEmpty(page)) {
            pageObj.setCurrent(page);
        }
        Map<String, Object> param = new HashMap<>();
        if (SHEET_STATUS_CACHED.equals(filter)) {
            param.put("status1", -1);
            param.put("status2", 0);
        }
        if (SHEET_STATUS_CLEANED.equals(filter)) {
            param.put("status", 1);
        }
        if (SHEET_STATUS_TRANSFORMED.equals(filter)) {
            param.put("status", 2);
        }
        if (Checker.isNotNullOrEmpty(search)) {
            param.put("keyword", search.toLowerCase());
        }
        pageObj.setParam(param);
        List<SheetInfo> listByPage = sheetInfoService.listByPage(pageObj);
        for (SheetInfo sheetInfo : listByPage) {
            TransStatusUtil.sheetsStatusToString(sheetInfo);
        }
        PageInfo pageInfo = new PageInfo(listByPage);
        Paging paging = new Paging();
        paging.setCurrent(pageInfo.getPageNum());
        paging.setPage_size(pageInfo.getPageSize());
        paging.setTotal(pageInfo.getTotal());
        Map<String, Object> map = new HashMap<>();  
        map.put(MAP_KEY_SHEETS, listByPage);
        map.put(Constant.BASE_PAGE, paging);
        return map;
    }

    /**
     * @Description: 数据处理 / 工作表-Sheet / 获取单个工作表的未清洗前的数据
     * @author lianhanwen
     * @date 2017年5月26日 下午12:51:36
     * @param filter
     * @param page
     * @return
     */
    @RequestMapping(value = "/sheets/{id}/originalData", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getUnCleanSheetData(@PathVariable Integer id, Integer page) {
        Page pageObj = new Page();
        if (Checker.isNotNullOrEmpty(page)) {
            pageObj.setCurrent(page);
        }
        // if (Checker.isNotNullOrEmpty(page)) {
        // pageObj.setPage_size(limit);
        // }
        String tableName = Constant.ORIGINAL_TABLE_PREFIX + id;
        String columnsJson = sheetInfoService.getById(id).getColumns();
        List<Map<String, Object>> listByPage = commonService.listByPage(tableName, null, pageObj);
        Map<String, String> columnsMap = JSON.parseObject(columnsJson, Map.class);

        List<Map<String, Object>> newList = transColumnsToCh(columnsMap, listByPage);

        PageInfo pageInfo = new PageInfo(listByPage);
        Paging paging = new Paging();
        paging.setCurrent(pageInfo.getPageNum());
        paging.setPage_size(pageInfo.getPageSize());
        paging.setTotal(pageInfo.getTotal());
        Map<String, Object> map = new HashMap<>();
        map.put(MAP_KEY_DATAS, newList);
        map.put(Constant.BASE_PAGE, paging);
        return map;
    }
    /**
     * 
     * @Description:数据处理 / 工作表-Sheet / 获取单个工作表的清洗后的数据
     * @author lianhanwen
     * @date 2017年6月6日 上午11:27:50 
     * @param id
     * @param page
     * @return
     */
    @RequestMapping(value = "/sheets/{id}/cleanedData", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getCleanedSheetData(@PathVariable Integer id, Integer page) {
        Page pageObj = new Page();
        if (Checker.isNotNullOrEmpty(page)) {
            pageObj.setCurrent(page);
        }
        // if (Checker.isNotNullOrEmpty(page)) {
        // pageObj.setPage_size(limit);
        // }
        String tableName = Constant.CLEANED_TABLE_PREFIX + id;
        String columnsJson = sheetInfoService.getById(id).getColumns();
        List<Map<String, Object>> listByPage = commonService.listByPage(tableName, null, pageObj);
        Map<String, String> columnsMap = JSON.parseObject(columnsJson, Map.class);
        
        List<Map<String, Object>> newList = transColumnsToCh(columnsMap, listByPage);
        
        PageInfo pageInfo = new PageInfo(listByPage);
        Paging paging = new Paging();
        paging.setCurrent(pageInfo.getPageNum());
        paging.setPage_size(pageInfo.getPageSize());
        paging.setTotal(pageInfo.getTotal());
        Map<String, Object> map = new HashMap<>();
        map.put(MAP_KEY_DATAS, newList);
        map.put(Constant.BASE_PAGE, paging);
        return map;
    }

    private List<Map<String, Object>> transColumnsToCh(Map<String, String> columnsMap,
            List<Map<String, Object>> listByPage) {

        List<Map<String, Object>> newList = new ArrayList<>();
        for (Map<String, Object> map : listByPage) {

            Map<String, Object> newMap = new LinkedHashMap<>();
            Set<Entry<String, Object>> entrySet = map.entrySet();
            for (Entry<String, Object> entry : entrySet) {

                if (Checker.isNotNullOrEmpty(columnsMap.get(entry.getKey()))) {
                    newMap.put(columnsMap.get(entry.getKey()), entry.getValue()==null?"":entry.getValue());
                } else {
                    if (!"ROW_ID".equals(entry.getKey())) {
                        newMap.put(entry.getKey(), entry.getValue());
                    }
                }
            }
            newList.add(newMap);
        }
        return newList;
    }

    /**
     * @Description: 数据处理 / 工作表-Sheet / 获取单个工作表的数据
     * @author lianhanwen
     * @date 2017年5月26日 下午12:51:36
     * @param filter
     * @param page
     * @return
     */
    @RequestMapping(value = "/sheets/{id}/data", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> listSheetDataById(@PathVariable Integer id, Integer limit, Integer page) {
        Page pageObj = new Page();
        if (Checker.isNotNullOrEmpty(page)) {
            pageObj.setCurrent(page);
        }
        if (Checker.isNotNullOrEmpty(limit)) {
            pageObj.setPage_size(limit);
        }
        SheetInfo sheetInfo = sheetInfoService.getById(id);
        String tableName = Constant.ORIGINAL_TABLE_PREFIX + id;
        if (sheetInfo!=null) {
            Integer status = Integer.valueOf(sheetInfo.getStatus().toString());
            // -1:未验证,0:已验证,1:已清洗,2:已转换
            if (status == 1) {
                tableName = Constant.CLEANED_TABLE_PREFIX + id;
            }
            if (status == 2) {
                tableName = Constant.TRANSFORMED_TABLE_PREFIX + id;
            }
            
        }
        String columnsJson = sheetInfoService.getById(id).getColumns();
        Map<String, String> columnsMap = JSON.parseObject(columnsJson, Map.class);
        List<Map<String, Object>> listByPage = commonService.listByPage(tableName, null, pageObj);

        List<Map<String, Object>> newList = transColumnsToCh(columnsMap, listByPage);

        PageInfo pageInfo = new PageInfo(listByPage);
        Paging paging = new Paging();
        paging.setCurrent(pageInfo.getPageNum());
        paging.setPage_size(pageInfo.getPageSize());
        paging.setTotal(pageInfo.getTotal());
        Map<String, Object> map = new HashMap<>();
        map.put(MAP_KEY_DATAS, newList);
        map.put(Constant.BASE_PAGE, paging);
        return map;
    }

    /**
     * @Description: 数据处理 / 工作表-Sheet / 查询单个工作表的数据
     * @author lianhanwen
     * @date 2017年5月26日 下午12:51:36
     * @param filter
     * @param page
     * @return
     */
    @RequestMapping(value = "/sheets/{id}/data/query", method = RequestMethod.POST)
    @ResponseBody
    public List<Map<String, Object>> getSheetDataByIds(@PathVariable Integer id, @RequestBody Map<String, Object> map) {

        List<Integer> ids = (ArrayList<Integer>) map.get("ids");
        SheetInfo sheetInfo = sheetInfoService.getById(id);
        String tableName = Constant.ORIGINAL_TABLE_PREFIX + id;
        if (sheetInfo!=null) {
            Integer status = Integer.valueOf(sheetInfo.getStatus().toString());
            // -1:未验证,0:已验证,1:已清洗,2:已转换
            if (status == 1) {
                tableName = Constant.CLEANED_TABLE_PREFIX + id;
            }
            if (status == 2) {
                tableName = Constant.TRANSFORMED_TABLE_PREFIX + id;
            }
            
        }

        String columnsJson = sheetInfoService.getById(id).getColumns();
        Map<String, String> columnsMap = JSON.parseObject(columnsJson, Map.class);
        List<Map<String, Object>> listByIds = commonService.listByIds(tableName, null, ids);
        List<Map<String, Object>> newList = transColumnsToCh(columnsMap, listByIds);
        return newList;
    }
    /**
     * 
     * @Description: 查询单个sheet的操作日志 
     * @author lianhanwen
     * @date 2017年6月7日 下午12:24:30 
     * @param id
     * @return
     */
    @RequestMapping(value = "/sheets/{id}/logs", method = RequestMethod.GET)
    @ResponseBody
    public List<SheetLog> listLogsBySheetId(@PathVariable Integer id) {
        Page pageObj=new Page();
        Map<String, Object> param=new HashMap<>();
        param.put("sheet_id", id);
        pageObj.setParam(param);
        List<SheetLog> listByPage = sheetLogService.listByPage(pageObj);
        return listByPage;
        
        
    }

}
