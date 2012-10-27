package com.unyaunya.gantt;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CalendarUtil {
	private static List<DateFormat> dateFormatList;
	
	public static Calendar round(Calendar c) {
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		return c;
	}

	public static Calendar round(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return round(c);
	}

	public static Calendar today() {
		return round(Calendar.getInstance());
	}

	public static long getDiffInDate(Date c1, Date c2) {
		long t1, t2;
		t1 = c1.getTime();
		t2 = c2.getTime();
		long delta = t1 - t2;
		delta = delta / 86400000;
		return delta;
		//return (c1.getTime() - c2.getTime()) / 86400000;
	}

	public static Date toDate(Object value) {
		if(value == null) {
			return null;
		}
		if(value instanceof Date) {
			return (Date)value;
		}
		String source;
		if(value instanceof String) {
			source = (String)value;
		}
		else {
			source = value.toString();
		}
		for(DateFormat df : getDateFormatList()) {
			try {
				return df.parse(source);
			}
			catch(ParseException e) {
				//NOP
			}
		}
		return null; 
	}

	private static List<DateFormat> getDateFormatList() {
		if(dateFormatList == null) {
			dateFormatList = new ArrayList<DateFormat>();
			dateFormatList.add(new SimpleDateFormat("yyyy/MM/dd"));
			dateFormatList.add(new SimpleDateFormat("yyyy-MM-dd"));
			dateFormatList.add(new SimpleDateFormat("yyyyMMdd"));
			//dateFormatList.add(new SimpleDateFormat("MM/dd"));
			//dateFormatList.add(new SimpleDateFormat("MM-dd"));
			//dateFormatList.add(new SimpleDateFormat("MMdd"));
		}
		return dateFormatList;
	}
}
