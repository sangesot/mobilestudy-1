package edu.thu.mobilestudy.util;

import android.content.Context;
import android.widget.Toast;

/**
 * ��ʾToast��Ϣ������
 * 
 * @author hujiawei
 */
public class ToastUtil {

	public static void showLongToast(Context context, String message) {
		Toast.makeText(context, message, Toast.LENGTH_LONG).show();
	}

	public static void showShortToast(Context context, String message) {
		Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
	}

}
