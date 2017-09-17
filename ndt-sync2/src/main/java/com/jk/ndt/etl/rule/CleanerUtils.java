package com.jk.ndt.etl.rule;

import java.util.ArrayList;
import java.util.List;

import com.jk.ndt.etl.entity.rule.CleanerPO;
import com.jk.ndt.etl.entity.rule.ParamInput;

/**
 * 
 * @ClassName: CleanerUtils
 * @Description: 清洗器工具类，处理内容hard code在代码里面（设计要求，不要问为什么）
 * @author fangwei
 * @date 2017年5月26日 下午1:44:03
 *
 */
public class CleanerUtils {
    private static List<CleanerPO> cleanerList = new ArrayList<CleanerPO>();
    private static final String SEARCH_NAME = "查找值";
    private static final String REPLACE_NAME = "替换值";
    private static final String POSITION_NAME = "插入位置";
    private static final String INSER_VALUE_NAME = "插入值";
    static {
        // 正则替换清理器
        CleanerPO replaceCleaner = new CleanerPO();
        List<ParamInput> paramInputs = new ArrayList<ParamInput>();
        // 正则验证器封装
        replaceCleaner.setName("替换");
        replaceCleaner.setCleanerName("replaceStringOperation");
        ParamInput searchParam = new ParamInput();
        searchParam.setName(SEARCH_NAME);
        searchParam.setType("text");
        paramInputs.add(searchParam);

        ParamInput replaceParam = new ParamInput();
        replaceParam.setName(REPLACE_NAME);
        replaceParam.setType("text");
        paramInputs.add(replaceParam);

        replaceCleaner.setParamInputs(paramInputs);
        cleanerList.add(replaceCleaner);

        // 在制定位置插入字符串清洗器
        CleanerPO insertCleaner = new CleanerPO();
        insertCleaner.setName("插入");
        insertCleaner.setCleanerName("insertStringOperation");

        List<ParamInput> inserPparamInputs = new ArrayList<ParamInput>();
        ParamInput positionParam = new ParamInput();
        positionParam.setName(POSITION_NAME);
        positionParam.setType("text");
        inserPparamInputs.add(positionParam);

        ParamInput insertValueParam = new ParamInput();
        insertValueParam.setName(INSER_VALUE_NAME);
        insertValueParam.setType("text");
        inserPparamInputs.add(insertValueParam);
        insertCleaner.setParamInputs(inserPparamInputs);
        cleanerList.add(insertCleaner);

    }

    /**
     * 
     * @Description: 查询清洗器的model列表
     * @author fangwei
     * @date 2017年5月23日 下午4:18:57
     * @return
     */
    public static List<CleanerPO> getCleanerList() {
        return cleanerList;
    }

    /**
     * 
     * @Description: 根据清洗器的名字查询指定清洗器modle对象
     * @author fangwei
     * @date 2017年5月23日 下午4:19:11
     * @param name
     * @return
     */
    public static CleanerPO findCleanerByName(String name) {
        CleanerPO obj = null;
        for (CleanerPO cleanerPO : cleanerList) {
            if (cleanerPO.getName().equals(name)) {
                obj = cleanerPO;
                break;
            }
        }
        return obj;
    }

    /**
     * 
     * @Description: 根据清洗器的名字查询指定清洗器处理类名
     * @author fangwei
     * @date 2017年5月23日 下午4:19:11
     * @param name
     * @return
     */
    public static String findCleanerNameByName(String name) {
        CleanerPO obj = findCleanerByName(name);
        return obj.getCleanerName();
    }

}
