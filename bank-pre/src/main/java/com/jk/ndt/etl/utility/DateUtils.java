package com.jk.ndt.etl.utility;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @ClassName: DateUtils
 * @Description: 日期工具包
 * @author fangwei
 * @date 2017年5月15日 下午3:23:30
 *
 */
public final class DateUtils {

	public static final long SECOND = 1000;

	public static final long MINUTE = SECOND * 60;

	public static final long HOUR = MINUTE * 60;

	public static final long DAY = HOUR * 24;

	public static final long WEEK = DAY * 7;

	public static final long YEAR = DAY * 365;

	public static final String FOMTER_TIMES = "yyyy-MM-dd HH:mm:ss";

	public static final String FOMTER_DATE = "yyyy-MM-dd";

	public static final String FOMTER_SIMPLE_DATE = "y-M-d";

	public static final String FOMTER_MON_DAY = "M/d";

	private static Log log = LogFactory.getLog(DateUtils.class);

	private static final Map<Integer, String> WEEK_DAY = new HashMap<Integer, String>();
	static {
		WEEK_DAY.put(7, "星期六");
		WEEK_DAY.put(1, "星期天");
		WEEK_DAY.put(2, "星期一");
		WEEK_DAY.put(3, "星期二");
		WEEK_DAY.put(4, "星期三");
		WEEK_DAY.put(5, "星期四");
		WEEK_DAY.put(6, "星期五");
	}

	/**
	 * 解析日期
	 * 
	 * @param date
	 *            日期字符串
	 * @param pattern
	 *            日期格式
	 * @return
	 */
	public static Date parse(String date, String pattern) {
		Date resultDate = null;
		try {
			resultDate = new SimpleDateFormat(pattern).parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return resultDate;
	}

	/**
	 * 解析日期 yyyy-MM-dd
	 * 
	 * @param date
	 *            日期字符串
	 * @param pattern
	 *            日期格式
	 * @return
	 */
	public static Timestamp parseSimple(String date) {
		Date result = null;
		try {
			DateFormat yyyyMMdd = new SimpleDateFormat(FOMTER_DATE);
			result = yyyyMMdd.parse(date);
		} catch (ParseException e) {
			log.error(e);
		}
		return result != null ? new Timestamp(result.getTime()) : null;
	}

	/**
	 * 解析日期字符串
	 * 
	 * @param date
	 * @return
	 */
	public static Timestamp parseFull(String date) {
		Date result = null;
		try {
			DateFormat yyyyMMddHHmmss = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			result = yyyyMMddHHmmss.parse(date);
		} catch (ParseException e) {
			log.error(e);
		}
		return result != null ? new Timestamp(result.getTime()) : null;
	}

	/**
	 * 解析日期 yyyyMMdd
	 * 
	 * @param date
	 *            日期字符串
	 * @return
	 */
	public static Timestamp parse(String date) {
		/*
		 * if (StringUtils.isEmpty(date)) return null;
		 */
		try {
			if (date.length() == 10) {
				return parseSimple(date);
			} else if (date.length() == 8) {
				DateFormat yyyyMMdd = new SimpleDateFormat("yyyyMMdd");
				Date d = yyyyMMdd.parse(date);
				return new Timestamp(d.getTime());
			} else if (date.length() == 19) {
				return parseFull(date);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 格式化日期字符串
	 * 
	 * @param date
	 *            日期
	 * @param pattern
	 *            日期格式
	 * @return
	 */
	public static String format(Date date, String pattern) {
		if (date == null) {
			return null;
		}
		DateFormat format = new SimpleDateFormat(pattern);
		return format.format(date);
	}

	/**
	 * 格式化日期
	 * 
	 * @param date
	 * @return yyyy年MM月dd日
	 */
	public static String formatCHS(Date date) {
		if (date == null) {
			return null;
		}
		DateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
		return format.format(date);
	}

	/**
	 * 格式化日期字符串
	 * 
	 * @param date
	 *            日期
	 * @return
	 */
	public static String format(Date date) {
		if (date == null) {
			return null;
		}
		DateFormat YYYY_MM_DD = new SimpleDateFormat(FOMTER_DATE);
		return YYYY_MM_DD.format(date);
	}

	/**
	 * 格式化日期
	 * 
	 * @param date
	 * @return
	 */
	public static String formatFull(Date date) {
		DateFormat YYYY_MM_DD_HH_MM_SS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return YYYY_MM_DD_HH_MM_SS.format(date);
	}

	/**
	 * 取得当前日期
	 * 
	 * @return
	 */
	public static Timestamp getNow() {
		return new Timestamp(System.currentTimeMillis());
	}

	/**
	 * 取得当前日期
	 * 
	 * @return
	 */
	public static Integer getNowYear() {
		return getYear(getNow());
	}

	/**
	 * 取得当前月份
	 * 
	 * @return
	 */
	public static Integer getNowMonth() {
		return getMonth(getNow());
	}

	/**
	 * 取得年度
	 * 
	 * @param value
	 * @return
	 */
	public static Integer getYear(Object value) {
		Calendar c = Calendar.getInstance();
		Date date = getDate(value);
		c.setTime(date);
		return c.get(Calendar.YEAR);
	}

	/**
	 * 取得月份
	 * 
	 * @param value
	 * @return
	 */
	public static Integer getMonth(Object value) {
		Calendar c = Calendar.getInstance();
		Date date = getDate(value);
		c.setTime(date);
		return c.get(Calendar.MONTH) + 1;
	}

	/**
	 * 取得日
	 * 
	 * @param value
	 * @return
	 */
	public static Integer getDay(Object value) {
		Calendar c = Calendar.getInstance();
		Date date = getDate(value);
		c.setTime(date);
		return c.get(Calendar.DAY_OF_MONTH);
	}

	/**
	 * 取得日期对象
	 * 
	 * @param value
	 * @return
	 */
	private static Date getDate(Object value) {
		Date date = null;
		if (value instanceof Date) {
			date = (Date) value;
		} else {
			date = parse((String) value);
		}
		if (date == null) {
			throw new RuntimeException("日期格式解析错误!date=" + value);
		}
		return date;
	}

	/**
	 * @param offsetYear
	 * @return 当前时间 + offsetYear
	 */
	public static Timestamp getNowExpiredYear(int offsetYear) {
		Calendar now = Calendar.getInstance();
		now.add(Calendar.YEAR, offsetYear);
		return new Timestamp(now.getTime().getTime());
	}

	/**
	 * @param offset
	 * @return 当前时间 + offsetMonth
	 */
	public static Timestamp getNowExpiredMonth(int offset) {
		Calendar now = Calendar.getInstance();
		now.add(Calendar.MONTH, offset);
		return new Timestamp(now.getTime().getTime());
	}

	/**
	 * @param offset
	 * @return 当前时间 + offsetDay
	 */
	public static Timestamp getNowExpiredDay(int offset) {
		Calendar now = Calendar.getInstance();
		now.add(Calendar.DATE, offset);
		return new Timestamp(now.getTime().getTime());
	}

	/**
	 * @param offset
	 * @return 当前时间 + offsetDay
	 */
	public static Timestamp getNowExpiredHour(int offset) {
		Calendar now = Calendar.getInstance();
		now.add(Calendar.HOUR, offset);
		return new Timestamp(now.getTime().getTime());
	}

	/**
	 * @param offsetSecond
	 * @return 当前时间 + offsetSecond
	 */
	public static Timestamp getNowExpiredSecond(int offsetSecond) {
		Calendar now = Calendar.getInstance();
		now.add(Calendar.SECOND, offsetSecond);
		return new Timestamp(now.getTime().getTime());
	}

	/**
	 * @param offset
	 * @return 当前时间 + offset
	 */
	public static Timestamp getNowExpiredMinute(int offset) {
		Calendar now = Calendar.getInstance();
		now.add(Calendar.MINUTE, offset);
		return new Timestamp(now.getTime().getTime());
	}

	/**
	 * @param offset
	 * @return 指定时间 + offsetDay
	 */
	public static Timestamp getExpiredDay(Date givenDate, int offset) {
		Calendar date = Calendar.getInstance();
		date.setTime(givenDate);
		date.add(Calendar.DATE, offset);
		return new Timestamp(date.getTime().getTime());
	}

	/**
	 * 实现ORACLE中ADD_MONTHS函数功能
	 * 
	 * @param offset
	 * @return 指定时间 + offsetMonth
	 */
	public static Timestamp getExpiredMonth(Date givenDate, int offset) {
		Calendar date = Calendar.getInstance();
		date.setTime(givenDate);
		date.add(Calendar.MONTH, offset);
		return new Timestamp(date.getTime().getTime());
	}

	/**
	 * @param second
	 * @return 指定时间 + offsetSecond
	 */
	public static Timestamp getExpiredSecond(Date givenDate, int second) {
		Calendar date = Calendar.getInstance();
		date.setTime(givenDate);
		date.add(Calendar.SECOND, second);
		return new Timestamp(date.getTime().getTime());
	}

	/**
	 * 根据日期取得日历
	 * 
	 * @param date
	 * @return
	 */
	public static Calendar getCalendar(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c;
	}

	/**
	 * @param offsetSecond
	 * @return 指定时间 + offsetSecond
	 */
	public static Timestamp getExpiredHour(Date givenDate, int offsetHour) {
		Calendar date = Calendar.getInstance();
		date.setTime(givenDate);
		date.add(Calendar.HOUR, offsetHour);
		return new Timestamp(date.getTime().getTime());
	}

	/**
	 * @return 给出指定日期的月份的第一天
	 */
	public static Date getMonthFirstDay(Date givenDate) {
		Date date = DateUtils.parse(DateUtils.format(givenDate, "yyyy-MM"), "yyyy-MM");
		return date;
	}

	/**
	 * 取得当前是周几？
	 * 
	 * @param givenDate
	 * @return
	 */
	public static int getDayOfWeek(Date givenDate) {
		Calendar c = Calendar.getInstance();
		c.setTime(givenDate);
		int day = c.get(Calendar.DAY_OF_WEEK);
		return day;
	}

	/**
	 * 取得中文星期？
	 * 
	 * @param dayOfWeek
	 * @return
	 */
	public static String getChineseDayOfWeek(int dayOfWeek) {
		return WEEK_DAY.get(dayOfWeek);
	}

	/**
	 * 给定日期是否在范围内
	 * 
	 * @param date
	 *            给定日期
	 * @param begin
	 *            开始日期
	 * @param end
	 *            结束日期
	 * @return true 在指定范围内
	 */
	public static Boolean between(Date date, Date begin, Date end) {
		if (date == null || begin == null || end == null) {
			return true;
		}
		return date.after(begin) && date.before(end);
	}

	/**
	 * 当前日期是否在范围内
	 * 
	 * @param begin
	 *            开始日期
	 * @param end
	 *            结束日期
	 * @return true 在指定范围内
	 */
	public static Boolean between(Date begin, Date end) {
		Date now = getNow();
		return between(now, begin, end);
	}

	/**
	 * 取得今天零点日期
	 * 
	 * @return
	 */
	public static Calendar getTodayZero() {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		return c;
	}

	/**
	 * <p>
	 * Parses a string representing a date by trying a variety of different
	 * parsers.
	 * </p>
	 * 
	 * <p>
	 * The parse will try each parse pattern in turn. A parse is only deemed
	 * sucessful if it parses the whole of the input string. If no parse
	 * patterns match, a ParseException is thrown.
	 * </p>
	 * 
	 * @param str
	 *            the date to parse, not null
	 * @param parsePatterns
	 *            the date format patterns to use, see SimpleDateFormat, not
	 *            null
	 * @return the parsed date
	 * @throws IllegalArgumentException
	 *             if the date string or pattern array is null
	 * @throws ParseException
	 *             if none of the date patterns were suitable
	 */
	public static Date parseDate(String str, String[] parsePatterns) throws ParseException {
		if (str == null || parsePatterns == null) {
			throw new IllegalArgumentException("Date and Patterns must not be null");
		}
		SimpleDateFormat parser = null;
		ParsePosition pos = new ParsePosition(0);
		for (int i = 0; i < parsePatterns.length; i++) {
			if (i == 0) {
				parser = new SimpleDateFormat(parsePatterns[0]);
			} else {
				parser.applyPattern(parsePatterns[i]);
			}
			pos.setIndex(0);
			Date date = parser.parse(str, pos);
			if (date != null && pos.getIndex() == str.length()) {
				return date;
			}
		}
		throw new ParseException("Unable to parse the date: " + str, -1);
	}

	/**
	 * 取得java.sql.Date
	 * 
	 * @param value
	 * @return
	 */
	public static java.sql.Date getSqlDate(Object value) {
		if (value == null) {
			return null;
		}
		if (value instanceof Date) {
			return new java.sql.Date(((Date) value).getTime());
		} else if (value instanceof String) {
			Timestamp date = parse((String) value);
			return date != null ? new java.sql.Date(date.getTime()) : null;
		}
		return null;
	}

	/**
	 * 取得系统日期变量
	 * 
	 * @return
	 */
	public static Map<String, Object> getDateVariableMap() {
		Map<String, Object> dateMap = new HashMap<String, Object>();
		Date now = getNow();
		dateMap.put("now", now);// 当前时间
		dateMap.put("year", getYear(now));// 年
		dateMap.put("month", getMonth(now));// 月
		dateMap.put("day", getDay(now));// 日
		dateMap.put("simple", format(now));// 简单日期
		dateMap.put("full", formatFull(now));// 全日期
		dateMap.put("chs", formatCHS(now));// 中文日期
		dateMap.put("month_next", getExpiredMonth(now, 1));// 下月
		dateMap.put("month_pre", getExpiredMonth(now, -1));// 上月
		dateMap.put("quarter_next", getExpiredMonth(now, 3));// 下季度
		dateMap.put("quarter_pre", getExpiredMonth(now, -3));// 上季度
		dateMap.put("year_next", getExpiredMonth(now, 12));// 明年
		dateMap.put("year_pre", getExpiredMonth(now, -12));// 今年
		dateMap.put("day_next", getExpiredDay(now, 1));// 明天
		dateMap.put("day_pre", getExpiredDay(now, -1));// 昨天
		int month = DateUtils.getMonth(now);
		int year = DateUtils.getYear(now);
		int yearjs = (month > 9 ? year + 1 : year);
		dateMap.put("yearjs", yearjs);// 计生年度
		dateMap.put("yearjs_pre", yearjs - 1);// 计生年度去年
		dateMap.put("yearjs_next", yearjs + 1);// 计生年度明年
		return dateMap;
	}

	/**
	 * 日期是否正确,格式必须为“YYYY-MM-DD”
	 * 
	 * @param sDate
	 * @return
	 */
	public static boolean isValidDate(String sDate) {
		String datePattern1 = "\\d{4}-\\d{2}-\\d{2}";
		String datePattern2 = "^((\\d{2}(([02468][048])|([13579][26]))"
				+ "[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|"
				+ "(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?"
				+ "((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?("
				+ "(((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?"
				+ "((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))";
		if ((sDate != null)) {
			Pattern pattern = Pattern.compile(datePattern1);
			Matcher match = pattern.matcher(sDate);
			if (match.matches()) {
				pattern = Pattern.compile(datePattern2);
				match = pattern.matcher(sDate);
				return match.matches();
			} else {
				return false;
			}
		}
		return false;
	}

	/**
	 * 
	 * 获得两个日期之间的时间，并封装到map中
	 * 
	 * @param begin
	 * @param end
	 * @return
	 * 
	 */
	public static Map<String, Long> cal_time_space(Date begin, Date end) {
		long l = end.getTime() - begin.getTime();
		long day = l / 86400000L;
		long hour = l / 3600000L - day * 24L;
		long min = l / 60000L - day * 24L * 60L - hour * 60L;
		long second = l / 1000L - day * 24L * 60L * 60L - hour * 60L * 60L - min * 60L;
		Map<String, Long> map = new HashMap<String, Long>();
		map.put("day", Long.valueOf(day));
		map.put("hour", Long.valueOf(hour));
		map.put("min", Long.valueOf(min));
		map.put("second", Long.valueOf(second));
		return map;
	}

	/**
	 * 
	 * 将日期的类型解析成对应的数字
	 * 
	 * @param type
	 * @param date
	 * @return
	 * 
	 */
	public static int parseDate(String type, Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		if (type.equals("y")) {
			return cal.get(1);
		}
		if (type.equals("M")) {
			return cal.get(2) + 1;
		}
		if (type.equals("d")) {
			return cal.get(5);
		}
		if (type.equals("H")) {
			return cal.get(11);
		}
		if (type.equals("m")) {
			return cal.get(12);
		}
		if (type.equals("s")) {
			return cal.get(13);
		}
		return 0;
	}

	/**
	 * 获取过去的天数
	 * 
	 * @param date
	 * @return
	 */
	public static long pastDays(Date date) {
		long t = new Date().getTime() - date.getTime();
		return t / (24 * 60 * 60 * 1000);
	}

	/**
	 * 获取过去的小时
	 * 
	 * @param date
	 * @return
	 */
	public static long pastHour(Date date) {
		long t = new Date().getTime() - date.getTime();
		return t / (60 * 60 * 1000);
	}

	/**
	 * 获取过去的分钟
	 * 
	 * @param date
	 * @return
	 */
	public static long pastMinutes(Date date) {
		long t = new Date().getTime() - date.getTime();
		return t / (60 * 1000);
	}

	/**
	 * 获得date时间的前后day天的时间
	 *
	 * @param date
	 * @param day
	 * @return
	 * @author shiy
	 * @version v1.0, 2016年5月28日 下午1:15:37
	 */
	public static Date datetool(Date date, Integer day) {
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date); // 设置当前日期
		calendar.add(Calendar.DATE, day); // 日期加day天
		date = calendar.getTime();
		return date;
	}

	public static Date getNextDay(Date date, Integer days) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH, +days - 1);// +1今天的时间加一天
		date = calendar.getTime();
		return date;
	}

	public static int hour2seconds(BigDecimal hour) {
		return hour.multiply(new BigDecimal(3600)).intValue();
	}

	public static int minutes2seconds(BigDecimal minutes) {
		return minutes.multiply(new BigDecimal(60)).intValue();
	}

	/**
	 * 当月第一天
	 * 
	 * @return
	 * @author hky
	 */
	public static String getFirstDay() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		Date theDate = calendar.getTime();

		GregorianCalendar gcLast = (GregorianCalendar) Calendar.getInstance();
		gcLast.setTime(theDate);
		gcLast.set(Calendar.DAY_OF_MONTH, 1);
		String day_first = df.format(gcLast.getTime());
		StringBuffer str = new StringBuffer().append(day_first).append(" 00:00:00");
		return str.toString();

	}

	/**
	 * 当月最后一天
	 * 
	 * @return
	 * @author hky
	 */
	public static String getLastDay() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		Date theDate = calendar.getTime();
		String s = df.format(theDate);
		StringBuffer str = new StringBuffer().append(s).append(" 23:59:59");
		return str.toString();

	}

	/**
	 * 获取两个日期之间的天数
	 */
	public static int getDaysBetweenDate(Date beginTime, Date endTime) {
		Date date1 = parse(format(beginTime));
		Date date2 = parse(format(endTime));
		Calendar cal = Calendar.getInstance();
		cal.setTime(date1);
		long time1 = cal.getTimeInMillis();
		cal.setTime(date2);
		long time2 = cal.getTimeInMillis();
		long between_days = (time2 - time1) / (1000 * 3600 * 24);
		return Integer.parseInt(String.valueOf(between_days));
	}

	/**
	 * 判断时间是否在工作日期（周一至周五9:30—18:00）
	 * 
	 * @param date
	 * @return boolean
	 * @throws Exception
	 */
	public static boolean checkDateToWorkDay(Date date) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String strDate = sdf.format(date);
		String strDateBegin = "09:30:00";
		String strDateEnd = "18:00:00";
		int workday;
		try {
			workday = dayForWeek(strDate);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		if (workday >= 1 && workday <= 5) {
			// 截取当前时间时分秒
			int strDateH = Integer.parseInt(strDate.substring(11, 13));
			int strDateM = Integer.parseInt(strDate.substring(14, 16));
			int strDateS = Integer.parseInt(strDate.substring(17, 19));
			// 截取开始时间时分秒
			int strDateBeginH = Integer.parseInt(strDateBegin.substring(0, 2));
			int strDateBeginM = Integer.parseInt(strDateBegin.substring(3, 5));
			int strDateBeginS = Integer.parseInt(strDateBegin.substring(6, 8));
			// 截取结束时间时分秒
			int strDateEndH = Integer.parseInt(strDateEnd.substring(0, 2));
			int strDateEndM = Integer.parseInt(strDateEnd.substring(3, 5));
			int strDateEndS = Integer.parseInt(strDateEnd.substring(6, 8));
			if ((strDateH >= strDateBeginH && strDateH <= strDateEndH)) {
				// 当前时间小时数在开始时间和结束时间小时数之间
				if (strDateH > strDateBeginH && strDateH < strDateEndH) {
					return true;
					// 当前时间小时数等于开始时间小时数，分钟数在开始和结束之间
				} else if (strDateH == strDateBeginH && strDateM >= strDateBeginM && strDateM <= strDateEndM) {
					return true;
					// 当前时间小时数等于开始时间小时数，分钟数等于开始时间分钟数，秒数在开始和结束之间
				} else if (strDateH == strDateBeginH && strDateM == strDateBeginM && strDateS >= strDateBeginS
						&& strDateS <= strDateEndS) {
					return true;
				}
				// 当前时间小时数大等于开始时间小时数，等于结束时间小时数，分钟数小等于结束时间分钟数
				else if (strDateH >= strDateBeginH && strDateH == strDateEndH && strDateM <= strDateEndM) {
					return true;
					// 当前时间小时数大等于开始时间小时数，等于结束时间小时数，分钟数等于结束时间分钟数，秒数小等于结束时间秒数
				} else if (strDateH >= strDateBeginH && strDateH == strDateEndH && strDateM == strDateEndM
						&& strDateS <= strDateEndS) {
					return true;
				} else {
					return false;
				}
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	/**
	 * 判断当前日期是星期几
	 * 
	 * @param pTime
	 *            修要判断的时间
	 * @return dayForWeek 判断结果
	 * @Exception 发生异常
	 */
	public static int dayForWeek(String pTime) throws Exception {

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		c.setTime(format.parse(pTime));
		int dayForWeek = 0;
		if (c.get(Calendar.DAY_OF_WEEK) == 1) {
			dayForWeek = 7;
		} else {
			dayForWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
		}
		return dayForWeek;
	}

	/**
	 * 时间 加 秒
	 * 
	 * @param date
	 * @param seconds
	 * @return
	 */
	public static Date addSecond(Date date, int seconds) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.SECOND, seconds);
		return calendar.getTime();
	}

	/**
	 * 时间转化为字符串
	 * 
	 * @param date
	 * @return String
	 */
	public static String getDateToString(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(date);
	}

	/**
	 * 字符串转化为时间
	 * 
	 * @param date
	 * @return String
	 * @throws ParseException
	 */
	public static Date getStringToDate(String dateTime) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.parse(dateTime);
	}

}