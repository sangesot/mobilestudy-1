package edu.thu.mobilestudy.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import edu.thu.mobilestudy.model.User;
import edu.thu.mobilestudy.util.CommonUtil;
import edu.thu.mobilestudy.util.LoginUtil;
import edu.thu.mobilestudy.util.NetworkUtil;
import edu.thu.mobilestudy.util.ToastUtil;

/**
 * ��ӭ����
 * 
 * @author hujiawei
 * 
 */
public class WelcomeActivity extends Activity {

	// private ImageView iv_welcome_loading;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_activity_welcome);
		// iv_welcome_loading = (ImageView) findViewById(R.id.iv_welcome_loading);

		// for test
		// AnimationDrawable animationDrawable = (AnimationDrawable) iv_welcome_loading.getBackground();
		// animationDrawable.start();

		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				new WelcomeTask().execute();
			}
		}, 2000);// wait 2 seconds

	}

	// welcome task
	class WelcomeTask extends AsyncTask<Void, Void, Integer> {

		public int RESULT_SUCCESS = 0;
		public int RESULT_NONETWORK = 1;
		public int RESULT_NOSDCARD = 2;
		public User user;

		@Override
		protected void onPreExecute() {
			// AnimationDrawable animationDrawable = (AnimationDrawable) iv_welcome_loading.getBackground();
			// animationDrawable.start();
			super.onPreExecute();
		}

		@Override
		protected Integer doInBackground(Void... params) {
			// check network,sd card,user info,may take some time...
			if (NetworkUtil.getNetworkState(getApplicationContext()) == NetworkUtil.NETWORK_STATE_NONE) {
				return RESULT_NONETWORK;
			}
			if (Environment.getExternalStorageState() == Environment.MEDIA_UNMOUNTED) {
				return RESULT_NOSDCARD;
			}
			user = LoginUtil.readUserInfo();
			return RESULT_SUCCESS;
		}

		@Override
		protected void onPostExecute(Integer result) {
			// AnimationDrawable animationDrawable = (AnimationDrawable) iv_welcome_loading.getBackground();
			// animationDrawable.stop();

			if (result.intValue() == RESULT_NOSDCARD) {
				System.out.println("no sdcard");
				ToastUtil.showShortToast(getApplicationContext(), "no sdcard!");
				appExit();
			}

			if (result.intValue() == RESULT_NONETWORK) {
				System.out.println("no network");
				ToastUtil.showShortToast(getApplicationContext(), "no network!");
				gotoHomeCache();// here still have a lot to do
			}

			if (user != null && user.getLoginMode() == LoginUtil.AUTO_LOGIN) {
				System.out.println(user.getUserName());
				CommonUtil.currentUser = user;
				ToastUtil.showShortToast(getApplicationContext(), "home");
				Intent intent = new Intent(WelcomeActivity.this, HomeActivity.class);
				startActivity(intent);
				WelcomeActivity.this.finish();
			} else {
				ToastUtil.showShortToast(getApplicationContext(), "login");
				Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
				startActivity(intent);
				WelcomeActivity.this.finish();
			}
		}

		// go to Home Cache page
		private void gotoHomeCache() {
			// current nothing to do
		}

		// app exit
		private void appExit() {
			WelcomeActivity.this.finish();
		}
	}

}
