package com.jk.ndt.etl.controller.mirror;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.jk.ndt.etl.controller.BaseController;
import com.jk.ndt.etl.entity.CommonQueryObj;
import com.jk.ndt.etl.entity.mirror.MirrorTable;
import com.jk.ndt.etl.service.common.CommonService;
import com.jk.ndt.etl.service.mirror.MirrorTableService;
import com.jk.ndt.etl.utility.Checker;
import com.jk.ndt.etl.utility.ErrorUtil;

/**
 * 
 * @ClassName: MirrorTableController
 * @Description: 镜像业务表相关操作
 * @author fangwei
 * @date 2017年5月31日 下午2:10:01
 *
 */
@RequestMapping("/api")
@Controller
public class MirrorTableController extends BaseController {

    private static final String MAP_KEY_COLUMN = "column";
    private static final String MAP_KEY_IN = "in";
    private static final String PARAM = "param";
    private static final String MESSAGE = "传入的参数不正确";

    @Autowired
    private MirrorTableService mirrorTableService;
    @Autowired
    private CommonService commonService;

    /**
     * 
     * @Description: 获取镜像业务表列表
     * @author fangwei
     * @date 2017年5月31日 下午2:15:26
     * @return
     */
    @RequestMapping(value = "/mirrors", method = RequestMethod.GET)
    @ResponseBody
    public Object listAll() {
        return this.mirrorTableService.listAll();
    }

    /**
     * 
     * @Description: 根据镜像表id和数据查询制定业务表的内容
     * @author fangwei
     * @date 2017年6月5日 上午10:39:22
     * @return
     */
    @RequestMapping(value = "/mirrors/{id}/query", method = RequestMethod.POST)
    @ResponseBody
    public Object queryMirrorDataById(@RequestBody Map<String, Object> map, @PathVariable("id") Integer mirrorTableId) {
        if (Checker.isNullOrEmpty(map) || Checker.isNullOrEmpty(map.get(MAP_KEY_COLUMN))
                || Checker.isNullOrEmpty(map.get(MAP_KEY_IN))) {
            JSONObject errorMessage = new JSONObject();
            errorMessage.put(PARAM, MESSAGE);
            return ErrorUtil.getRequestError(errorMessage);
        }
        MirrorTable mirrorTable = mirrorTableService.getById(mirrorTableId);
        if(mirrorTable == null){
            JSONObject errorMessage = new JSONObject();
            errorMessage.put(PARAM, MESSAGE);
            return ErrorUtil.getRequestError(errorMessage);
        }
        
        String tableName = mirrorTable.getName();
        String column = (String) map.get(MAP_KEY_COLUMN);
        @SuppressWarnings("unchecked")
        List<String> in = (List<String>) map.get(MAP_KEY_IN);
        List<CommonQueryObj> queryList = new ArrayList<CommonQueryObj>();
        CommonQueryObj query = new CommonQueryObj(column, "in", in);
        queryList.add(query);
        return commonService.listByQueryObj(tableName, queryList);
    }
}
