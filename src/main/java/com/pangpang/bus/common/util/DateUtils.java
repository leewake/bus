/**
 *
 */
package com.pangpang.bus.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * @author lijingwei
 *
 */
public class DateUtils {

	public static final String PATTERN_SIMPLE = "yyyy-MM-dd";
	public static final String PATTERN_SIMPLE_SHORT = "yyyyMMdd";
	public static final String PATTERN_SIMPLE_SLASH = "yyyy/MM/dd";
	public static final String PATTERN_COMMON_SLASH = "yyyy/MM/dd HH:mm:ss";
	public static final String PATTERN_COMMON = "yyyy-MM-dd HH:mm:ss";

    public static final List<String> dateFormats = new ArrayList<String>();

    static {
        dateFormats.add(PATTERN_COMMON_SLASH);
        dateFormats.add(PATTERN_COMMON);
        dateFormats.add(PATTERN_SIMPLE);
        dateFormats.add(PATTERN_SIMPLE_SLASH);
        dateFormats.add(PATTERN_SIMPLE_SHORT);
    }

	public static Date getDateStartTime(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		return c.getTime();
	}

	public static Date getDateEndTime(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.HOUR_OF_DAY, 23);
		c.set(Calendar.MINUTE, 59);
		c.set(Calendar.SECOND, 59);
		c.set(Calendar.MILLISECOND, 999);
		return c.getTime();
	}

	public static Calendar getDateStartTime(Calendar c) {
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		return c;
	}

	public static Calendar getDateEndTime(Calendar c) {
		c.set(Calendar.HOUR_OF_DAY, 23);
		c.set(Calendar.MINUTE, 59);
		c.set(Calendar.SECOND, 59);
		c.set(Calendar.MILLISECOND, 999);
		return c;
	}

	private static SimpleDateFormat getDateFormat(String pattern) {
		return new SimpleDateFormat(pattern);

	}

	public static synchronized Date parse(String pattern, String date) {
		if(date == null || date.equals(""))
			return null;
		try {
			return getDateFormat(pattern).parse(date);
		} catch (ParseException e) {
			return null;
		}
	}

	public static String parse(Date date) {
		if(date != null) {
			return  getDateFormat(PATTERN_COMMON).format(date);
		}
		return null;
	}

	public static String format(String pattern, Date date) {
		return getDateFormat(pattern).format(date);
	}

	public static Date addDay(Date date, int day) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DATE, day);
		return c.getTime();
	}

	public static Date addWeek(Date date, int week) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.WEEK_OF_YEAR, week);
		return c.getTime();
	}

	public static Date addMonth(Date date, int month) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.MONTH, month);
		return c.getTime();
	}

	public static int getMonth(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c.get(Calendar.MONTH)+1;
	}

	public static int getWeek(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c.get(Calendar.WEEK_OF_YEAR);
	}

	public static Calendar getWeek(int week, int day) {
		Calendar c = Calendar.getInstance();
		if (day >= 7) {
			day = 0;
			c.set(Calendar.WEEK_OF_YEAR, week+1);
		} else {
			c.set(Calendar.WEEK_OF_YEAR, week);
		}
		c.set(Calendar.DAY_OF_WEEK, day+1);
		return c;
	}

	public static int getDate(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c.get(Calendar.DATE);
	}

	public static Calendar getDate(int month, int date) {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.MONTH, month-1);
		c.set(Calendar.DATE, date);
		return c;
	}

	public static Date getMonthStartTime(int month) {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.MONTH, month-1);
		c.set(Calendar.DATE, c.getMaximum(Calendar.DATE));
		return getDateEndTime(c).getTime();
	}

	public static Date getMonthEndTime(int month) {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.MONTH, month-1);
		c.set(Calendar.DATE, 1);
		return getDateStartTime(c).getTime();
	}

	public static Date getStartTimeByMonthAndDate(int month, int date) {
		Calendar c = getDate(month, date);
		return getDateStartTime(c).getTime();
	}

	public static Date getEndTimeByMonthAndDate(int month, int date) {
		Calendar c = getDate(month, date);
		return getDateEndTime(c).getTime();
	}

	public static Date getStartTimeByWeekAndDay(int week, int day) {
		Calendar c = getWeek(week, day);
		return getDateStartTime(c).getTime();
	}

	public static Date getEndTimeByWeekAndDay(int week, int day) {
		Calendar c = getWeek(week, day);
		return getDateEndTime(c).getTime();
	}

	public static Date getNormalQuarterStartTime() {
		return getQuarterStartTime(0,1);
	}

	public static Date getNormalQuarterEndTime() {
		return getQuarterEndTime(0,31);
	}

	/**
	 * 季度的开始时间，即2017-01-1 00:00:00
	 * @param change 增减的月份 比如 change=1 代表比常规季度月份延后一月 即 2-4为一季度 5-7为二季度 以此类推
	 * @return Date 计算后的季度起始时间
	 */
	public static Date getQuarterStartTime(int change, int date) {
		Calendar c = Calendar.getInstance();
		int currentMonth = c.get(Calendar.MONTH) + 1;
		Date now = null;
		try {
			if (currentMonth >= 1 && currentMonth <= 3)
				c.set(Calendar.MONTH, 0+change);
			else if (currentMonth >= 4 && currentMonth <= 6)
				c.set(Calendar.MONTH, 3+change);
			else if (currentMonth >= 7 && currentMonth <= 9)
				c.set(Calendar.MONTH, 4+change);
			else if (currentMonth >= 10 && currentMonth <= 12)
				c.set(Calendar.MONTH, 9+change);
			c.set(Calendar.DATE, date);
			now = getDateStartTime(c).getTime();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return now;
	}

	/**
	 * 季度的结束时间，即2017-03-31 23:59:59
	 * @param change 增减的月份 比如 change=1 代表比常规季度月份延后一月 即 2-4为一季度 5-7为二季度 以此类推
	 * @return Date 计算后的季度结束时间
	 */
	public static Date getQuarterEndTime(int change, int date) {
		Calendar c = Calendar.getInstance();
		int currentMonth = c.get(Calendar.MONTH) + 1;
		Date now = null;
		try {
			if (currentMonth >= 1 && currentMonth <= 3) {
				c.set(Calendar.MONTH, 2+change);
				c.set(Calendar.DATE, 31);
			} else if (currentMonth >= 4 && currentMonth <= 6) {
				c.set(Calendar.MONTH, 5+change);
				c.set(Calendar.DATE, 30);
			} else if (currentMonth >= 7 && currentMonth <= 9) {
				c.set(Calendar.MONTH,7+change);
				c.set(Calendar.DATE, 30);
			} else if (currentMonth >= 10 && currentMonth <= 12) {
				c.set(Calendar.MONTH, 11+change);
				c.set(Calendar.DATE, 31);
			}
			c.set(Calendar.DATE, date);
			now = getDateStartTime(c).getTime();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return now;
	}

	public static String getPreviousOrAfterDate(Date date,int day) {
		return format(PATTERN_SIMPLE, addDay(date, day));
	}

	public static List<Date> toDateList(String ... dateString) {
		if (dateString == null)
			return null;

		List<Date> dates = new ArrayList<>();
		for (String str : dateString) {
			dates.add(parse(PATTERN_SIMPLE, str));
		}
		return dates;
	}


	public static final Date parsePatternCommonString(String aDateStr) throws ParseException {
		return parser(aDateStr, PATTERN_COMMON);
	}

	public static final Date parser(String aDateStr, String formatter) throws ParseException {
		if (StringUtils.isBlank(aDateStr)) {
			return null;
		}

		SimpleDateFormat sdf = new SimpleDateFormat(formatter);
		return sdf.parse(aDateStr);
	}

	public static Date getEndDate(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.HOUR_OF_DAY, 23);
		c.set(Calendar.MINUTE, 59);
		c.set(Calendar.SECOND, 59);
		c.set(Calendar.MILLISECOND, 999);
		return c.getTime();
	}
	public static void main(String[] args) throws Exception {
		System.out.println(getQuarterStartTime(1,1));
		System.out.println(getQuarterEndTime(1,31));
	}

	public static <T> List<Predicate> getPredicatesForTime(String[] time, String name ,
														   Root<T> root, CriteriaBuilder cb){
		List<Predicate> predicates = new ArrayList<>();

		if(time!=null) {
			if (time.length>=1 && org.springframework.util.StringUtils.hasLength(time[0])) {
				Date date = DateUtils.parse(DateUtils.PATTERN_SIMPLE, time[0]);
				predicates.add(cb.greaterThanOrEqualTo(root.<Date>get(name), date));
			}

			if (time.length>=2 && org.springframework.util.StringUtils.hasLength(time[1])) {
				Date date = DateUtils.getEndDate(DateUtils.parse(DateUtils.PATTERN_SIMPLE, time[1]));
				predicates.add(cb.lessThanOrEqualTo(root.<Date>get(name), date));
			}
		}
		return predicates;
	}


    /**
     * 有的日期会解析成为excel date，形似：43034.484143518515 或 43056
     * @param dateString
     * @return
     */
    public static boolean isExcelDateFormat(String dateString) {
        return dateString.matches("[\\d]+\\.[\\d]+") || dateString.matches("[\\d]+");
    }
}