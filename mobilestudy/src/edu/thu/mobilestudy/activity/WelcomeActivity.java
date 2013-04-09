package edu.thu.mobilestudy.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.widget.ImageView;
import edu.thu.mobilestudy.model.User;
import edu.thu.mobilestudy.util.CommonUtil;
import edu.thu.mobilestudy.util.LoginUtil;
import edu.thu.mobilestudy.util.NetworkUtil;
import edu.thu.mobilestudy.util.ToastUtil;

/**
 * ª∂”≠ΩÁ√Ê
 * 
 * @author hujiawei
 * 
 */
public class WelcomeActivity extends Activity {

	// private ProgressBar pb_welcome;
	private ImageView iv_welcome_loading;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_activity_welcome);
		// pb_welcome = (ProgressBar) findViewById(R.id.pb_welcome);
		iv_welcome_loading = (ImageView) findViewById(R.id.iv_welcome_loading);
		iv_welcome_loading.setBackgroundResource(R.anim.iv_loading);
		new WelcomeTask().execute();
	}

	// welcome task
	class WelcomeTask extends AsyncTask<Void, Void, Integer> {

		public int RESULT_SUCCESS = 0;
		public int RESULT_NONETWORK = 1;
		public int RESULT_NOSDCARD = 2;
		public User user;

		@Override
		protected void onPreExecute() {
			AnimationDrawable animationDrawable = (AnimationDrawable) iv_welcome_loading.getBackground();
			animationDrawable.start();
			super.onPreExecute();
		}

		@Override
		protected Integer doInBackground(Void... params) {
			// check network,sd card,user info...
			if (NetworkUtil.getNetworkState(getApplicationContext()) == NetworkUtil.NETWORK_STATE_NONE) {
				return RESULT_NONETWORK;
			}
			if (Environment.getExternalStorageState() != Environment.MEDIA_MOUNTED) {
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
				gotoHomeCache();//here still have a lot to do
			}

			if (user != null && user.getLoginMode() == LoginUtil.AUTO_LOGIN) {
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

		}

		// app exit
		private void appExit() {
			WelcomeActivity.this.finish();
		}
	}

}
