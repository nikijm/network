package com.jk.ndtetl;

/**
 * 
 * @ClassName: Constant
 * @Description: 常量池
 * @author fangwei
 * @date 2017年5月22日 上午10:31:00
 *
 */
@Deprecated
public class Constant {

    // ######################### etl文件处理状态 #########################
    // -1:删除,0:未缓存,1:已缓存,2:处理中,3:清洗完毕,4:全部处理完毕;
    public static final int FILE_STATUS_DELETE = -1;

    public static final int FILE_STATUS_UNCACHE = 0;

    public static final int FILE_STATUS_CACHED = 1;

    public static final int FILE_STATUS_PROCESS = 2;

    public static final int FILE_STATUS_CLEAN_COMPLETE = 3;

    public static final int FILE_STATUS_ALL_COMPLETE = 4;

    // ######################### sheet处理状态 #########################
    // -2:废弃,-1:未验证,0:已验证,1:已清洗,2:已转换,3：已校验
    public static final int SHEET_STATUS_DELETE = -2;

    public static final int SHEET_STATUS_UNVALIDATED = -1;

    public static final int SHEET_STATUS_VALIDATED = 0;

    public static final int SHEET_STATUS_CLEANED = 1;

    public static final int SHEET_STATUS_TRANSFORMED = 2;

    public static final int SHEET_STATUS_VERIFIED = 3;

    // ######################### 数据操作日志相关 ########################
    // 缓存，验证，清洗，转换，校验
    public static final String SHEET_LOG_CACHED = "缓存";

    public static final String SHEET_LOG_VALIDATED = "验证";

    public static final String SHEET_LOG_CLEANED = "清洗";

    public static final String SHEET_LOG_TRANSFORMED = "转换";

    public static final String SHEET_LOG_CHECKED = "校验";

    public static final String SHEET_LOG_TRASH = "废弃";

    public static final String SHEET_LOG_REVERTTOCLEANED = "回退到已清洗状态";

    public static final String SHEET_LOG_REVERTTOUNVALIDATED = "回退到未验证状态";

    // ######################### 表的前缀 #########################
    /**
     * 源数据表前缀
     */
    public static final String ORIGINAL_TABLE_PREFIX = "ETL_ORIGINAL_";

    /**
     * 清洗表前缀
     */
    public static final String CLEANED_TABLE_PREFIX = "ETL_CLEANED_";

    /**
     * 转换表前缀
     */
    public static final String TRANSFORMED_TABLE_PREFIX = "ETL_TRANSFORMED_";

    // ######################### 校验规则常量 #########################
    /**
     * 电话
     */
    public static final String REG_PHONE = "^$|^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(17[0])|(18[0,5-9]))\\d{8}$";

    /**
     * 邮箱
     */
    public static final String REG_EMAIL = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";

    /**
     * 密码
     */
    public static final String REG_PASSWORD = "^((?=.*[a-z])(?=.*\\d)|(?=[a-z])(?=.*[#@!~%^&*\\-+])|(?=.*\\d)(?=.*[#@!~%^&*\\-+]))[a-zA-Z\\d#@!~%^&*\\-+]{9,20}$";

    // ######################### 请求返回json中key的定义 #########################
    /**
     * 请求出错
     */
    public static final String REQ_ERRORS = "errors";

    /**
     * 响应信息
     */
    public static final String RES_MESSAGE = "message";

    // ######################### httpstatus 常量 #########################
    /**
     * 请求失败:参数校验失败等，保存、更新、获取失败等;
     */
    public static final int FAILED_400 = 400;

    /**
     * 无权限
     */
    public static final int NO_PERMISSON = 401;

    /**
     * 登录失效
     */
    public static final int LOGIN_INVALID = 430;

    /**
     * 参数校验错误，用户名重复等；
     */
    public static final int PARAMS_ERROR = 422;

    /**
     * 客户端请求信息的先决条件错误,如未带上jjtw-token或jjtw-token校验失败
     */
    public static final int PRECONDITION_FAILED = 412;

    // ######################### 主机的类型 #########################
    /**
     * 主机的类型 1，web 2, app 3,数据库 4， 监控
     */
    public static final int HOSTNAME_TYPE_WEB = 1;

    public static final int HOSTNAME_TYPE_APP = 2;

    public static final int HOSTNAME_TYPE_DB = 3;

    public static final int HOSTNAME_TYPE_MONITOR = 4;

    // ######################### 主机资源的类型 #########################
    /**
     * 主机资源的类型 1，web 2, app 3,数据库 4， 监控
     */
    public static final int HOSTNAME_TYPE_CPU = 1;

    public static final int HOSTNAME_TYPE_MEMORY = 2;

    public static final int HOSTNAME_TYPE_DISK = 3;

    public static final int HOSTNAME_TYPE_LOG = 4;

    // ######################### 表达式的类型 #########################
    /**
     * 表达式的类型 1，web 2, app 3,数据库 4， 监控
     */
    public static final int EXPRESSION_TYPE_LT = 1;

    public static final int EXPRESSION_TYPE_GT = 2;

    public static final int EXPRESSION_TYPE_CONTAIN = 3;

    // ######################### 监控主机资源的类型 #########################
    /**
     * 监控主机资源的类型
     */
    public static final String RESOURCE_TYPE_CPU = "cpu";

    public static final String RESOURCE_TYPE_MEMORY = "memory";

    public static final String RESOURCE_TYPE_DISK = "disk";

    // ######################### 查看监控信息的时间段 #########################
    /**
     * 查看监控信息的时间段
     */
    public static final String MONITORY_INFO_HOUR = "one_hour";

    public static final String MONITORY_INFO_DAY = "one_day";

    public static final String MONITORY_INFO_SEVEN_DAYS = "seven_days";

    public static final String MONITORY_INFO_FIFTEEN_DAYS = "fifteen_days";

    // ######################### 运行日志 #########################
    /**
     * 日志的级别
     */
    public static final Integer LOG_LEVEL_ALL = 0;

    public static final Integer LOG_LEVEL_INFO = 1;

    public static final Integer LOG_LEVEL_WARN = 2;

    public static final Integer LOG_LEVEL_ERROR = 3;

    public static final Integer LOG_LEVEL_FATAL = 4;

    /**
     * 日志的类别
     */
    public static final Integer LOG_TYPE_OPERATION = 1;

    public static final Integer LOG_TYPE_RUNTIME = 2;

    // ######################### 服务的类别 #########################
    /**
     * 服务的类别 1,缓存，2，清理，3，转化，4，校验
     */
    public static final Integer SERVICE_TYPE_CACHE = 1;

    public static final Integer SERVICE_TYPE_CLEAR = 2;

    public static final Integer SERVICE_TYPE_CONTRAST = 3;

    public static final Integer SERVICE_TYPE_CONVERSION = 4;

    /**
     * 服务的状态1,运行中，2，处理中，3，停止，
     */
    public static final Integer SERVICE_STATUS_RUN = 1;

    public static final Integer SERVICE_STATUS_PROCESSING = 2;

    public static final Integer SERVICE_STATUS_STOP = 3;

    /**
     * 日志的类型
     */
    public static final Integer ALARM_TYPE_BUSINESS = 2;

    public static final Integer ALARM_TYPE_MONITOR = 1;

    // ######################### session key #########################
    /**
     * JJTW_KEY
     */
    public static final String LOGIN_USER_JJTW_KEY = "login_user_jjtw_key";

    /**
     * 登录用户session key
     */
    public static final String LOGIN_USER_SESSION = "login_user_session";

    /**
     * 登录用户session Role key
     */
    public static final String LOGIN_USER_ROLE_SESSION = "login_user_role_session";

    /**
     * 登录用户session Menu key
     */
    public static final String LOGIN_USER_MENU_SESSION = "login_user_menu_session";

    /**
     * 登录用户session Resource key
     */
    public static final String LOGIN_USER_RESOURCE_SESSION = "login_user_resource_session";

    /**
     * 客户端传递的 key
     */
    public static final String LOGIN_USER_CLIENT_TOKEN = "X_ETL_TOKEN";

    /**
     * 返回给 客户端 key
     */
    public static final String LOGIN_USER_CLIENT_TOKEN_RESPONSE = "token";

    /**
     * token 失效时间
     */
    public static final int LOGIN_USER_CLIENT_TOKEN_EXPIRED = 30;

    /**
     * 分页常量
     */
    public static final String BASE_PAGE = "paging";

    /**
     * 密码提示信息
     */
    public static final String PWD_NOTICE = "请输入9-20位字母数字,可选特殊符号#@!~%^&*-+，空格单双引号除外";

    /**
     * 页面curd的key
     */
    public static final String PAGE_PERMISSIONS = "page_permissions";

    /**
     * 上传logo文件保存路径
     */
    public static final String UPLOAD_FILE_LOGO_FILEPATH = "/upload/org_logo/";

    /**
     * 对称加密的key
     */
    public static final String encodeKey = "jinkongzhengxin";
}
