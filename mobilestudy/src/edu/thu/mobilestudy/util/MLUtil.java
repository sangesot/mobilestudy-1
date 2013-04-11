package edu.thu.mobilestudy.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MLUtil {

	private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("M-dd");
	private static SimpleDateFormat fullDateFormat = new SimpleDateFormat("M-dd HH:mm");

	// format the date
	public static String formatFullDate(Date date) {
		return fullDateFormat.format(date);
	}

	// format the date
	public static String formatSimpleDate(Date date) {
		return simpleDateFormat.format(date);
	}
}
