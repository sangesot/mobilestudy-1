package edu.thu.mobilestudy.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import edu.thu.mobilestudy.util.LoginUtil;
import edu.thu.mobilestudy.util.ToastUtil;

/**
 * 设置界面的退出界面
 * 
 * @author hujiawei
 * 
 */
public class DialogSettingsExitActivity extends Activity {
	private LinearLayout ll_settings_exit;

	// exit cancle
	public void btn_settings_exitcancle(View v) {
		finish();
	}

	// exit ok
	public void btn_settings_exitok(View v) {
		LoginUtil.deleteUserInfo();
		finish();
		HomeActivity.instance.finish();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_settings_exit);
		ll_settings_exit = (LinearLayout) findViewById(R.id.ll_settings_exit);
		ll_settings_exit.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				ToastUtil.showShortToast(getApplicationContext(), "点击窗口外面关闭窗口");
			}
		});
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		finish();
		return true;
	}

}
