package edu.thu.mobilestudy.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
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

	// resource suggestion
	public static final int SUGGESTION_TYPE_NEW = 0;
	public static final int SUGGESTION_TYPE_HOT = 1;
	public static final int SUGGESTION_DEFAULT_COUNT = 20;

	// search type
	public static final int SEARCH_TYPE_COURSE = 0;
	public static final int SEARCH_TYPE_RESOURCE = 1;
	public static final int SEARCH_DEFAULT_COUNT = 20;

	// setting
	public static final boolean SETTING_SOUND_ON = true;

	public static File SDFOLDER;
	private static final String CONFIG_FILENAME = "config.properties";

	public static User currentUser;

	private static Properties props = new Properties();
	static {
		try {
			props.load(Thread.currentThread().getContextClassLoader().getResourceAsStream(CONFIG_FILENAME));

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

	// update property and save it !  --- hujiawei not useful,read-only file!
	public static void updateProperties(String key, String value) throws Exception {
		props.setProperty(key, value);
		OutputStream os = new FileOutputStream(CONFIG_FILENAME);
		props.store(os, "config updated:" + MLUtil.formatFullDate(new Date()));
	}


}
