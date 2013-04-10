package edu.thu.mobilestudy.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import android.os.Environment;
import edu.thu.mobilestudy.model.User;

/**
 * 公共参数工具类 V0.1
 * 
 * @author hujiawei
 * 
 */
public class CommonUtil {

	// result code
	public static final int RESULT_CODE_EXCEPTION = -1;
	public static final int RESULT_CODE_SUCCEED = 1;
	public static final int RESULT_CODE_FAIL = 0;

	public static File SDFOLDER;

	public static User currentUser;

	private static Properties props = new Properties();
	static {
		try {
			props.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("config.properties"));

			if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
				SDFOLDER = new File(Environment.getExternalStorageDirectory().getCanonicalPath() + getValue("sd_folder"));
				if (!SDFOLDER.exists()) {
					SDFOLDER.mkdir();
				}
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String getValue(String key) {
		return props.getProperty(key);
	}

	public static void updateProperties(String key, String value) {
		props.setProperty(key, value);
	}

}
