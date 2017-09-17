package com.jk.ndt.etl.utility.promission;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jk.ndt.etl.constant.Constant;
import com.jk.ndt.etl.entity.system.Admin;
import com.jk.ndt.etl.entity.system.Menu;
import com.jk.ndt.etl.entity.system.Resource;
import com.jk.ndt.etl.entity.system.Role;
import com.jk.ndt.etl.utility.Checker;
import com.jk.ndt.etl.utility.EHCacheUtil;
import com.jk.ndt.etl.utility.ErrorUtil;
import com.jk.ndt.etl.utility.StringUtils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacProvider;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.PeriodType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.Key;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * Created by 朱生 on 2017/5/26.
 */
public class ResourceMenusUtil {

    protected static Logger logger = LoggerFactory.getLogger(ResourceMenusUtil.class);

    /**
     * 设置登录用户的角色和权限、菜单
     *
     * @param login_admin
     */
    public static Map<String, Object> setRoleAndPermission(Admin login_admin, HttpServletRequest request, Map<String, Object> loginSessionMap) {

        Map<String, Object> permisson = new HashMap<>();
        List<Role> roleList = login_admin.getRoles();
        permisson.put(Constant.LOGIN_USER_ROLE_SESSION, roleList);

        //读取文件的所有菜单
        JSONArray menuArray = new JSONArray();
        List<Menu> menuResult = new ArrayList<>();
        Map<String, String> resourceMap = new HashMap<>();
        if (null != roleList && !roleList.isEmpty()) {
            Set<String> resourceSet = new TreeSet<>();
            for (Role role : roleList) {
                if (StringUtils.isBlank(role.getPermissions())) {
                    continue;
                }
                List<Resource> resourceTmp = JSONArray.parseArray(role.getPermissions().trim(), Resource.class);
                List<Resource> rsList = new ArrayList<>();
                if (null != resourceTmp && !resourceTmp.isEmpty()) {
                    for (Resource resource : resourceTmp) {
                        // c u r d 存在其一才会授予访问的 权限,否则当作无权限处理
                        //System.out.println(resource.getAvailable_operations());
                        if (null != resource.getAvailable_operations() && !StringUtils.isBlank(resource.getAvailable_operations().replaceAll(" ", ""))) {
                            resourceSet.add(resource.getKey());
                            rsList.add(resource);
                            resourceMap.put(resource.getKey(), resource.getAvailable_operations());
                        }
                    }
                }
                role.setPermissions(JSONObject.toJSONString(rsList));
            }
            String realPath = request.getServletContext().getRealPath("/json/menus.json");
            if (Checker.isNotNullOrEmpty(resourceSet)) {
                Map<String, Object> rs = getResourceMenus(realPath, resourceSet);
                menuArray = rs.get("menuArray") == null ? new JSONArray() : (JSONArray) rs.get("menuArray");
                menuResult = (List<Menu>) rs.get("menuList");
                loginSessionMap.put(Constant.LOGIN_USER_RESOURCE_SESSION, resourceMap);
            }
        }
        permisson.put(Constant.LOGIN_USER_MENU_SESSION, menuArray);
        loginSessionMap.put(Constant.LOGIN_USER_MENU_SESSION, menuResult);
        login_admin.setRoles(roleList);
        return permisson;
    }

    /**
     * 获取当前资源对应的菜单列表
     *
     * @param path
     * @param resourceSet
     * @return
     */
    public static Map<String, Object> getResourceMenus(String path, Set<String> resourceSet) {
//        String realPath = "F:\\workspace\\GoldCreditControl\\jk2\\trunk\\code\\ndt-etl\\src\\main\\webapp\\json\\menus.json";
        List<Menu> menuArray = JSONObject.parseArray(readFile(path), Menu.class);

        List<Menu> menuResult = new ArrayList<>();
        parseMenus(menuArray, resourceSet,menuResult);
        Map<String, Object> rs = new HashMap<>();

        clearNoChilds(menuArray, menuResult);
        rs.put("menuArray", JSON.parseArray(JSON.toJSON(menuArray).toString()));
        rs.put("menuList", menuResult);
        return rs;
    }

    /**
     * 清除childs为空的父节点
     *
     * @param menuList
     */
    private static void clearNoChilds(List<Menu> menuList, List<Menu> menuResult) {
        if (null != menuList) {
            for (int i = 0; i < menuList.size(); i++) {
                Menu menu = menuList.get(i);
                //   父类不会包含key
                if (!StringUtils.isBlank(menu.getKey())) {
                    menuResult.add(menu);
                }
                if (null == menu.getKey() && Checker.isNullOrEmpty(menu.getItems())) {
                    menuList.remove(i);
                    i--;
                } else {
                    clearNoChilds(menu.getItems(), menuResult);
                }
            }
        }
    }

    /**
     * 获取当前用户所有权限所对应的菜单
     *
     * @param menuArray
     * @param resourceSet
     */
    private static void parseMenus(List<Menu> menuArray, Set<String> resourceSet,List<Menu> menuResult) {
        for (int i = 0; i < menuArray.size(); i++) {
            Menu menuObject = menuArray.get(i);
            if (!StringUtils.isBlank(menuObject.getNotTree())) {
                List<Menu> notTreeMenus = menuObject.getItems();
                if (null != notTreeMenus && notTreeMenus.size()>0) {
                    for (Menu menu: notTreeMenus) {
                        if (!StringUtils.isBlank(menu.getKey()) && resourceSet.contains(menu.getKey())) {
                            //父类不会包含key
                            menuResult.add(menu);
                        }
                    }
                }
                menuArray.remove(i);
                i--;
                continue;
            }
            if (Checker.isNotNullOrEmpty(menuObject.getItems())) {
                parseMenus(menuObject.getItems(), resourceSet,menuResult);
            }
            if (null != menuObject.getKey() && !resourceSet.contains(menuObject.getKey())) {
                menuArray.remove(i);
                i--;
                continue;
            }
        }
    }

    /**
     * 读取配置的menu.json文件
     *
     * @param Path
     * @return
     */
    private static String readFile(String Path) {
        BufferedReader reader = null;
        InputStreamReader inputStreamReader = null;
        FileInputStream fileInputStream = null;
        StringBuffer ss = new StringBuffer();
        try {
            fileInputStream = new FileInputStream(Path);
            inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");
            reader = new BufferedReader(inputStreamReader);
            String tempString = null;
            while ((tempString = reader.readLine()) != null) {
                ss.append(tempString);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (inputStreamReader != null) {
                try {
                    inputStreamReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return ss.toString();
    }

    /**
     * token校验
     *
     * @param loginSessionMap 缓存用户相关信息
     * @param compactJws token
     * @param name 用户名
     * @param response
     * @return
     */
    public static boolean jjtwValid(Map<String, Object> loginSessionMap, String compactJws, String name, HttpServletResponse response) {
        try {
        	logger.debug("jjtw验证详情");
            Key key = (Key) loginSessionMap.get(Constant.LOGIN_USER_JJTW_KEY);
            System.out.println("获取到的key对象："+key);
            logger.debug("获取到的key对象："+key);
            assert Jwts.parser().setSigningKey(key).parseClaimsJws(compactJws).getBody().getSubject().equals(name);
            System.out.println("token校验通过---->");
            logger.debug("token校验通过---->");
            Claims claims = Jwts.parser().setSigningKey(key).parseClaimsJws(compactJws).getBody();
            DateTime issuedAt = new DateTime(claims.getIssuedAt().getTime());
            DateTime end = issuedAt.plusMinutes(Constant.LOGIN_USER_CLIENT_TOKEN_EXPIRED);
            Period p = new Period(DateTime.now(), end, PeriodType.minutes());
            int minutes = p.getMinutes();
            //System.out.println("时间：" + minutes);
            if (minutes < 10) {
            	System.out.println("token过期---->");
            	logger.debug("token过期---->");
                Key n_key = MacProvider.generateKey();
                String n_compactJws = Jwts.builder()
                        .setSubject(name)
                        .signWith(SignatureAlgorithm.HS512, n_key)
                        .setIssuedAt(DateTime.now().toDate())
                        .setExpiration(DateTime.now().plusSeconds(Constant.LOGIN_USER_CLIENT_TOKEN_EXPIRED).toDate())
                        .compact();
                response.addHeader(Constant.LOGIN_USER_CLIENT_TOKEN_RESPONSE, n_compactJws);
            }
        } catch (Exception e) {
        	e.printStackTrace();
            return false;
        }
        logger.debug("jjtw验证成功");
        return true;
    }

    /**
     * 权限控制
     * @param loginSessionMap
     * @param response
     * @param request
     * @return
     */
    public static boolean permissionValid(Map<String, Object> loginSessionMap, HttpServletResponse response, HttpServletRequest request) {
        Object menuObj = loginSessionMap.get(Constant.LOGIN_USER_MENU_SESSION);
        Object resourceObj = loginSessionMap.get(Constant.LOGIN_USER_RESOURCE_SESSION);
        if (null == menuObj || null == resourceObj) {
            response.setStatus(Constant.NO_PERMISSON);
            return false;
        }
        String requset_method = request.getMethod();
        String request_url = request.getRequestURI();
        List<Menu> menuList = (List<Menu>) menuObj;
        Map<String, String> resourceMap = (HashMap<String, String>) resourceObj;
        boolean permiss_result = false;
        if (null != menuList && !menuList.isEmpty()) {
            for (Menu menu : menuList) {
                String aa=menu.getUrl();
                if (StringUtils.isBlank(menu.getUrl())) {
                    continue;
                }
                if (request_url.contains("/etl") && !menu.getUrl().contains("/etl")) {
                    menu.setUrl("/etl"+menu.getUrl());
                }
                if (pathMatch(menu, request_url, request)) {
                    String avalible_operations = resourceMap.get(menu.getKey());
                    if (null != avalible_operations) {
                        String avalible_operations_temp = avalible_operations.replace(" ", "");
                        if (StringUtils.isBlank(avalible_operations_temp)) {
                            continue;
                        }
                        avalible_operations = avalible_operations_temp.toLowerCase();
                        switch (requset_method) {
                            case "GET":
                                if (avalible_operations.contains("r")) {
                                    permiss_result = true;
                                }
                                break;
                            case "POST":
                                if (avalible_operations.contains("c")) {
                                    permiss_result = true;
                                }
                                break;
                            case "PUT":
                                if (avalible_operations.contains("u")) {
                                    permiss_result = true;
                                }
                                break;
                            case "DELETE":
                                if (avalible_operations.contains("d")) {
                                    permiss_result = true;
                                }
                                break;
                        }
                    }
                    break;
                }
            }
        }
        return permiss_result;
    }

    /**
     * 获取页面权限,新增到每个controller对应的list列表请求方法中，作为返回值返回给前端，
     * 用于细粒度控制，如 添加按钮 有  c则显示 无则隐藏
     * @param request
     * @return
     */
    public static String getPagePermission(HttpServletRequest request) {
        String compactJws = request.getHeader(Constant.LOGIN_USER_CLIENT_TOKEN);
        if (StringUtils.isBlank(compactJws) || null == EHCacheUtil.get(compactJws)) {
            return "";
        }
        Map<String, Object> loginSessionMap = (Map<String, Object>) EHCacheUtil.get(compactJws);
        Object menuObj = loginSessionMap.get(Constant.LOGIN_USER_MENU_SESSION);
        Object resourceObj = loginSessionMap.get(Constant.LOGIN_USER_RESOURCE_SESSION);
        if (null == menuObj || null == resourceObj) {
            return "";
        }
        String requset_method = request.getMethod();
        String request_url = request.getRequestURI();
        List<Menu> menuList = (List<Menu>) menuObj;
        Map<String, String> resourceMap = (HashMap<String, String>) resourceObj;
        if (null != menuList && !menuList.isEmpty()) {
            for (Menu menu : menuList) {
                String aa=menu.getUrl();
                if (StringUtils.isBlank(menu.getUrl())) {
                    continue;
                }
                if (request_url.contains("/etl") && !menu.getUrl().contains("/etl")) {
                    menu.setUrl("/etl"+menu.getUrl());
                }
                if (pathMatch(menu, request_url, request)) {
                    String avalible_operations = resourceMap.get(menu.getKey());
                    return avalible_operations;
                }
            }
        }
        return "";
    }

    /**
     * 匹配请求路径
     *
     * @param menu
     * @param reqUrl
     * @return true 匹配成功
     */
    private static boolean pathMatch(Menu menu, String reqUrl, HttpServletRequest request) {
        String menuUrl=menu.getUrl();
        if (StringUtils.isBlank(menuUrl)) {
            return false;
        }
        try {
            if (menuUrl.indexOf("?") > 0) {

                List<String> menuUrls=new ArrayList<>();
                List<String> reqUrls=new ArrayList<>();
                moreQuestionMark( menuUrl,reqUrl,menuUrls,reqUrls);
                if (Checker.isNotNullOrEmpty(menuUrls)
                        && Checker.isNotNullOrEmpty(reqUrls)
                        && (menuUrls.size()==reqUrls.size())
                        ) {
                    boolean rsBoolean=true;
                    for (int i = 0; i < menuUrls.size(); i++) {
                        if (!StringUtils.equals(menuUrls.get(i), reqUrls.get(i))) {
                            rsBoolean=false;
                        }
                    }
                    return  rsBoolean;
                }
            } else {
                return reqUrl.contains(menuUrl);
            }
        } catch (Exception e) {
            System.out.println(e.getCause());
        }
        return false;
    }


    /**
     * 深度解析菜单url和请求路径
     * @param menuUrl
     * @param reqUrl
     */
    private static void moreQuestionMark(String menuUrl, String reqUrl, List<String> menuUrls, List<String> reqUrls) {
        if (menuUrl.indexOf("?") > 0) {
            int s1 = menuUrl.indexOf("?");
            String t1 = menuUrl.substring(0, s1);
            String t2 = menuUrl.substring(s1 + 1, menuUrl.length());

            String u1 = reqUrl.substring(0, s1);
            String u2_temp = reqUrl.substring(s1, reqUrl.length());
            String u2 = u2_temp.substring(u2_temp.indexOf("/"), u2_temp.length());
            menuUrls.add(t1);
            reqUrls.add(u1);
            if (t2.indexOf("?") > 0) {
                moreQuestionMark( t2,u2,menuUrls,reqUrls);
            } else {
                menuUrls.add(t2);
                reqUrls.add(u2);
            }
        }
    }

}
