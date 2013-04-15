package edu.thu.mobilestudy.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * 关于作者界面
 * 
 * @author hujiawei
 * 
 */
public class AboutActivity extends Activity {

	private Button btn_about_back;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_settings_about);
		btn_about_back = (Button) findViewById(R.id.btn_about_back);

		btn_about_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				AboutActivity.this.finish();
			}
		});

	}

}
