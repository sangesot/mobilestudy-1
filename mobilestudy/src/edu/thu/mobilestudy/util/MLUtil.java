package edu.thu.mobilestudy.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.R.raw;

import edu.thu.mobilestudy.activity.R;

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

	// determine resource type
	public static int determineResourceType(String keyword, boolean isSmall) {
		if (keyword == null) {
			if (isSmall) {
				return R.drawable.resource32;
			} else {
				return R.drawable.resource64;
			}
		}
		String key = keyword.toLowerCase();
		if (key.indexOf("java") != 0) {
			if (isSmall) {
				return R.drawable.java32;
			} else {
				return R.drawable.java64;
			}
		} else if (key.indexOf("c") != 0) {
			if (isSmall) {
				return R.drawable.c32;
			} else {
				return R.drawable.c64;
			}
		} else if (key.indexOf("c++") != 0) {
			if (isSmall) {
				return R.drawable.cpp32;
			} else {
				return R.drawable.cpp64;
			}
		} else {
			if (isSmall) {
				return R.drawable.resource32;
			} else {
				return R.drawable.resource64;
			}
		}
	}

}
