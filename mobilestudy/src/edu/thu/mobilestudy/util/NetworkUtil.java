package edu.thu.mobilestudy.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;

/**
 * 网络监测的工具类
 * 
 * @author hujiawei
 */
public class NetworkUtil {

	public final static int NETWORK_STATE_NONE = 0; // no network

	public final static int NETWORK_STATE_WIFI = 1; // Wi-Fi

	public final static int NETWORK_STATE_MOBILE = 2; // 3G,GPRS

	public static int getNetworkState(Context context) {
		ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		// mobile
		State state = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
		if (state == State.CONNECTED || state == State.CONNECTING) {
			return NETWORK_STATE_MOBILE;
		}
		// Wifi
		state = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
		if (state == State.CONNECTED || state == State.CONNECTING) {
			return NETWORK_STATE_WIFI;
		}
		return NETWORK_STATE_NONE;
	}
}