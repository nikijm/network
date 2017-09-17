package com.jk.ndtetl.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @ClassName: StringUtils
 * @Description: 字符串处理包
 * @author fangwei
 * @date 2017年5月15日 下午3:27:16
 *
 */
public final class StringUtils extends org.apache.commons.lang3.StringUtils {

    /**
     * 空字符串
     */
    public static final String EMPTY = "";

    private static Log log = LogFactory.getLog(StringUtils.class);

    private StringUtils() {
    }

    /**
     * 首字母小写
     * 
     * @param s
     *            String
     * @return String
     */
    public static String firstCharLowerCase(String s) {
        if (isValid(s)) {
            return s.substring(0, 1).toLowerCase() + s.substring(1);
        }
        return s;
    }

    /**
     * 删除前缀
     * 
     * @param s
     * @param prefix
     * @return
     */
    public static String removePrefix(String s, String prefix) {
        int index = s.indexOf(prefix);
        return index == 0 ? s.substring(prefix.length()) : s;
    }

    /**
     * 删除后缀
     * 
     * @param s
     * @param suffix
     * @return
     */
    public static String removeSuffix(String s, String suffix) {
        return s.endsWith(suffix) ? s.substring(0, s.length() - suffix.length()) : s;
    }

    /**
     * 首字母大写
     * 
     * @param s
     *            String
     * @return String
     */
    public static String firstCharUpperCase(String s) {
        if (isValid(s)) {
            return s.substring(0, 1).toUpperCase() + s.substring(1);
        }
        return s;
    }

    /**
     * 检查对象是否有效 obj != null && obj.toString().length() > 0
     * 
     * @param obj
     * @return boolean
     */
    public static boolean isValid(Object obj) {
        return obj != null && obj.toString().trim().length() > 0;
    }

    /**
     * 是否是空的
     * 
     * @param obj
     * @return
     */
    public static boolean isEmpty(Object obj) {
        return obj == null || obj.toString().trim().length() == 0;
    }

    /**
     * 转化为String对象
     * 
     * @param obj
     * @return boolean
     */
    public static String asString(Object obj) {
        return obj != null ? obj.toString() : "";
    }

    /**
     * 返回其中一个有效的对象 value != null && value.toString().length() > 0
     * 
     * @param values
     */
    public static String tryThese(String... values) {
        for (int i = 0; i < values.length; i++) {
            String value = values[i];
            if (value != null && value.trim().length() > 0) {
                return value;
            }
        }
        return null;
    }

    /**
     * EL表达式提供的定义方法
     * 
     * @param v1
     * @param v2
     * @return
     */
    public static String tryThese(String v1, String v2) {
        return tryThese(new String[] { v1, v2 });
    }

    /**
     * 连接字符串
     * 
     * @param list
     * @param split
     * @return 字符串
     */
    // public static <T> String join(T[] list, String split)
    // {
    // return join(list, split, "");
    // }

    /**
     * 连接字符串
     * 
     * @param list
     * @param split
     * @return 字符串
     */
    public static <T> String join(T[] list, String split, String wrap) {
        if (list == null)
            return null;
        StringBuilder s = new StringBuilder(128);
        for (int i = 0; i < list.length; i++) {
            if (i > 0) {
                s.append(split);
            }
            s.append(wrap + list[i] + wrap);
        }
        return s.toString();
    }

    /**
     * 连接
     * 
     * @param list
     * @param split
     * @param wrap
     * @return
     */
    public static <T> String join(List<T> list, String split, String wrap) {
        return join(list.toArray(), split, wrap);
    }

    /**
     * 连接字符串
     * 
     * @param list
     * @param split
     * @return 字符串
     */
    public static String join(List<?> list, String split) {
        return join(list.toArray(), split);
    }

    /**
     * 取得匹配的字符串
     * 
     * @param input
     * @param regex
     * @return
     */
    public static List<String> matchs(String input, String regex) {
        return matchs(input, regex, 0);
    }

    /**
     * 取得匹配的字符串
     * 
     * @param input
     * @param regex
     * @return
     */
    public static List<String> matchs(String input, String regex, int group) {
        Pattern pattern = Pattern.compile(regex);
        Matcher match = pattern.matcher(input);
        List<String> matches = new ArrayList<String>();
        while (match.find()) {
            matches.add(match.group(group));
        }
        return matches;
    }

    /**
     * 找到匹配的第一个字符串
     * 
     * @param input
     * @param regex
     * @param group
     * @return
     */
    public static String matchFirst(String input, String regex, int group) {
        List<String> matches = matchs(input, regex, group);
        return matches.isEmpty() ? null : matches.get(0);
    }

    /**
     * 截取指定长度字符串
     * 
     * @param input
     * @param tail
     * @param length
     * @return
     */
    public static String getShorterString(String str, int maxLength) {
        return getShorterString(str, "...", maxLength);
    }

    /**
     * 截取指定长度字符串
     * 
     * @param input
     * @param tail
     * @param length
     * @return
     */
    public static String getShorterString(String input, String tail, int length) {
        tail = isValid(tail) ? tail : "";
        StringBuffer buffer = new StringBuffer(512);
        try {
            int len = input.getBytes("GBK").length;
            if (len > length) {
                int ln = 0;
                for (int i = 0; ln < length; i++) {
                    String temp = input.substring(i, i + 1);
                    if (temp.getBytes("GBK").length == 2)
                        ln += 2;
                    else
                        ln++;

                    if (ln <= length)
                        buffer.append(temp);
                }
            } else {
                return input;
            }
            buffer.append(tail);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return buffer.toString();
    }

    /**
     * 取得GBK编码
     * 
     * @return
     */
    public static String getBytesString(String input, String code) {
        try {
            byte[] b = input.getBytes(code);
            return Arrays.toString(b);
        } catch (UnsupportedEncodingException e) {
            return String.valueOf(code.hashCode());
        }
    }

    /**
     * 转换格式 CUST_INFO_ID - > custInfoId
     * 
     * @param input
     * @return
     */
    public static String getFieldString(String input) {
        String field = input.toLowerCase();
        String[] values = field.split("\\_");
        StringBuffer b = new StringBuffer(input.length());
        for (int i = 0; i < values.length; i++) {
            if (i == 0)
                b.append(values[i]);
            else
                b.append(firstCharUpperCase(values[i]));
        }
        return b.toString();
    }

    /**
     * 转化为JSON值
     * 
     * @param value
     * @return
     * @throws IOException
     */
    public static String toJsonValue(Object value) throws IOException {
        if (value instanceof Number) {
            return value.toString();
        } else {
            return "'" + value.toString() + "'";
        }
    }

    /**
     * 字符串转化为UUID
     * 
     * @param value
     * @return
     */
    public static String toUUID(String value) {
        if (value == null)
            throw new RuntimeException("value is null!");
        return UUID.nameUUIDFromBytes(value.getBytes()).toString();
    }

    /**
     * 获取Style样式中样式的值
     * 
     * @param styleString
     * @param styleName
     * @return 相应的值
     */
    public static String getStyleValue(String styleString, String styleName) {
        String[] styles = styleString.split(";");
        for (int i = 0; i < styles.length; i++) {
            String tempValue = styles[i].trim();
            if (tempValue.startsWith(styleName)) {
                String[] style = tempValue.split(":");
                return style[1];
            }
        }
        return "";
    }

    /**
     * 生成重复次字符
     * 
     * @param charactor
     * @param repeat
     * @return
     */
    public static String getRepeat(String charactor, int repeat) {
        StringBuilder s = new StringBuilder(charactor.length() * repeat);
        for (int i = 0; i < repeat; i++) {
            s.append(charactor);
        }
        return s.toString();
    }

    public static boolean containsOR(String str1, String... args) {
        for (String arg : args) {
            if (str1.contains(arg))
                return true;
        }
        return false;
    }

    // ===========================对字符串的编码解码操作===============================================
    /**
     * 
     * 使用URLDecolder对字符串进行解码
     * 
     * @param s
     * @return
     * 
     */
    public static String decode(String s) {
        String ret = s;
        try {
            ret = URLDecoder.decode(s.trim(), "UTF-8");
        } catch (Exception localException) {
        }
        return ret;
    }

    /**
     * 
     * 使用URLDecolder对字符串进行编码
     * 
     * @param s
     * @return
     * 
     */
    public static String encode(String s) {
        String ret = s;
        try {
            ret = URLEncoder.encode(s.trim(), "UTF-8");
        } catch (Exception localException) {
        }
        return ret;
    }

    /**
     * 
     * 将ISO-8859-1编码格式的字符串，转换成自定义的编码格式
     * 
     * @param str
     * @param coding
     * @return
     * 
     */
    public static String convert(String str, String coding) {
        String newStr = "";
        if (str != null)
            try {
                newStr = new String(str.getBytes("ISO-8859-1"), coding);
            } catch (Exception e) {
                return newStr;
            }
        return newStr;
    }

    // ============================================================================

    /**
     * 
     * 获得随机的字符
     * 
     * @return
     * 
     */
    public static char randomChar() {
        char[] chars = { 'a', 'A', 'b', 'B', 'c', 'C', 'd', 'D', 'e', 'E', 'f', 'F', 'g', 'G', 'h', 'H', 'i', 'I', 'j',
                'J', 'k', 'K', 'l', 'L', 'm', 'M', 'n', 'N', 'o', 'O', 'p', 'P', 'q', 'Q', 'r', 'R', 's', 'S', 't', 'T',
                'u', 'U', 'v', 'V', 'w', 'W', 'x', 'X', 'y', 'Y', 'z', 'Z' };
        int index = (int) (Math.random() * 52.0D) - 1;
        if (index < 0) {
            index = 0;
        }
        return chars[index];
    }

    /**
     * 
     * 生成制定长度的随机字符串 <功能详细描述>
     * 
     * @param length
     * @return
     * 
     */
    public static final String randomString(int length) {
        char[] numbersAndLetters = "0123456789abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                .toCharArray();
        if (length < 1) {
            return "";
        }
        Random randGen = new Random();
        char[] randBuffer = new char[length];
        for (int i = 0; i < randBuffer.length; i++) {
            randBuffer[i] = numbersAndLetters[randGen.nextInt(71)];
        }
        return new String(randBuffer);
    }

    /**
     * 
     * 生成制定长度的随机数字字符串
     * 
     * @param length
     * @return
     * 
     */
    public static final String randomInt(int length) {
        if (length < 1) {
            return null;
        }
        Random randGen = new Random();
        char[] numbersAndLetters = "0123456789".toCharArray();

        char[] randBuffer = new char[length];
        for (int i = 0; i < randBuffer.length; i++) {
            randBuffer[i] = numbersAndLetters[randGen.nextInt(10)];
        }
        return new String(randBuffer);
    }

    /**
     * 
     * 随机字符串的Set集合 <功能详细描述>
     * 
     * @param a
     * @param length
     * @return
     * 
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static Set<Integer> randomInt(int a, int length) {
        Set list = new TreeSet();
        int size = length;
        if (length > a) {
            size = a;
        }
        while (list.size() < size) {
            Random random = new Random();
            int b = random.nextInt(a);
            list.add(Integer.valueOf(b));
        }
        return list;
    }

    /**
     * 
     * 将字符串分割成数组
     * 
     * @param s
     * @param c
     * @return
     * 
     */
    public static String[] splitByChar(String s, String c) {
        String[] list = s.split(c);
        return list;
    }

    /**
     * 获得用户远程地址
     */
    public static String getRemoteAddr(HttpServletRequest request) {
        String ip = request.getHeader("X-Real-IP");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Forwarded-For");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip != null ? ip : request.getRemoteAddr();
    }

    /**
     * 获取当前网络ip
     * 
     * @param request
     * @return
     */
    public String getIpAddr(HttpServletRequest request) {
        String ipAddress = request.getHeader("x-forwarded-for");
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
            if (ipAddress.equals("127.0.0.1") || ipAddress.equals("0:0:0:0:0:0:0:1")) {
                // 根据网卡取本机配置的IP
                InetAddress inet = null;
                try {
                    inet = InetAddress.getLocalHost();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
                ipAddress = inet.getHostAddress();
            }
        }
        // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        if (ipAddress != null && ipAddress.length() > 15) { // "***.***.***.***".length()
                                                            // = 15
            if (ipAddress.indexOf(",") > 0) {
                ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
            }
        }
        return ipAddress;
    }

    /**
     *   
     * @Description: 转义正则特殊字符  
     * @author fangwei
     * @date 2017年5月20日 下午3:44:56 
     * @param keyword
     * @return
     */
    public static String escapeExprSpecialWord(String keyword) {  
        if (StringUtils.isNotBlank(keyword)) {
            String[] fbsArr = { "\\", "$", "(", ")", "*", "+", ".", "[", "]", "?", "^", "{", "}", "|" };  
            for (String key : fbsArr) {  
                if (keyword.contains(key)) {  
                    keyword = keyword.replace(key, "\\" + key);
                }  
            }  
        }  
        return keyword==null?keyword:keyword.toLowerCase();
    }
}
