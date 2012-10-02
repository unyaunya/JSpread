package com.unyaunya.gantt;

import java.util.Calendar;

public class CalendarUtil {
	public static Calendar round(Calendar c) {
		c.set(Calendar.HOUR, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		return c;
	}

	public static Calendar today() {
		return round(Calendar.getInstance());
	}

	public static long getDiffInDate(Calendar c1, Calendar c2) {
		return (c1.getTimeInMillis() - c2.getTimeInMillis()) / 86400000;
	}
}
