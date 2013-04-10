package edu.thu.mobilestudy.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import edu.thu.mobilestudy.http.MLParameter;
import edu.thu.mobilestudy.model.JSONResult;
import edu.thu.mobilestudy.model.MLException;
import edu.thu.mobilestudy.model.User;
import edu.thu.mobilestudy.service.AuthService;
import edu.thu.mobilestudy.util.CommonUtil;
import edu.thu.mobilestudy.util.LoginUtil;
import edu.thu.mobilestudy.util.ToastUtil;

/**
 * 登录界面
 * 
 * @author hujiawei
 * 
 */
public class LoginActivity extends Activity {

	private EditText et_login_userid;
	private EditText et_login_password;
	private Button btn_login_login;
	private CheckBox cb_login_mode;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_activity_login);
		et_login_userid = (EditText) findViewById(R.id.et_login_userid);
		et_login_password = (EditText) findViewById(R.id.et_login_password);
		btn_login_login = (Button) findViewById(R.id.btn_login_login);
		cb_login_mode = (CheckBox) findViewById(R.id.cb_login_mode);

		// 进入立即显示软键盘
		// getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);// better

		btn_login_login.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (et_login_userid.getText().toString().equals("") || et_login_password.getText().toString().equals("")) {
					ToastUtil.showShortToast(getApplicationContext(), "请输入账号密码！");
					return;
				}
				new LoginTask().execute(new String[] { et_login_userid.getText().toString(), et_login_password.getText().toString() });
			}
		});
	}

	// login task
	class LoginTask extends AsyncTask<String[], Void, JSONResult> {

		@Override
		protected void onCancelled() {
			super.onCancelled();
		}

		@Override
		protected void onPreExecute() {
			System.out.println("onPreExecute");
			btn_login_login.setEnabled(false);
		}

		@Override
		protected JSONResult doInBackground(String[]... params) {
			System.out.println("doInBackground");
			MLParameter[] mlParameters = new MLParameter[] { new MLParameter("action", CommonUtil.getValue("action_login")),
					new MLParameter("repository", CommonUtil.getValue("repository_des")), new MLParameter("userId", params[0][0]),
					new MLParameter("password", params[0][1]) };
			return new AuthService().login(CommonUtil.getValue("baseUrl") + CommonUtil.getValue("service_auth"), mlParameters);
			// direct request,just for test

			// HttpClient httpClient = new HttpClient();
			// GetMethod method = new GetMethod(
			// "http://192.168.0.110:8080/mobileapp/auth?repository=DES&action=login&userId=zhengli&password=zhengli");
			// try {
			// httpClient.executeMethod(method);
			// String responseString = method.getResponseBodyAsString();
			// System.out.println(responseString);
			// } catch (Exception e) {
			// e.printStackTrace();
			// }
			// return null;
		}

		@Override
		protected void onProgressUpdate(Void... values) {
			super.onProgressUpdate(values);
		}

		@Override
		protected void onPostExecute(JSONResult jsonResult) {
			System.out.println("onPostExecute");
			if (jsonResult.getCode() == CommonUtil.RESULT_CODE_SUCCEED) {
				try {
					CommonUtil.currentUser = new User(jsonResult.getContent());// userId,userName
					CommonUtil.currentUser.setPassword(et_login_password.getText().toString());
					if (cb_login_mode.isChecked()) {
						CommonUtil.currentUser.setLoginMode(LoginUtil.AUTO_LOGIN);
					} else {
						CommonUtil.currentUser.setLoginMode(LoginUtil.NOT_LOGIN);
					}
					LoginUtil.writeUserInfo(CommonUtil.currentUser);
					System.out.println("write user info ok! user name = " + CommonUtil.currentUser.getUserName());
				} catch (Exception e) {
					// e.printStackTrace();
					ToastUtil.showShortToast(getApplicationContext(), "保存用户信息出错！");
				}
				Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
				startActivity(intent);
				LoginActivity.this.finish();
			}
			/*
			 * else if (jsonResult.getCode() == CommonUtil.RESULT_CODE_FAIL) { ToastUtil.showShortToast(getApplicationContext(),
			 * jsonResult.getMessage()); btn_login_login.setEnabled(true); }
			 */
			else {// exception occurs
				ToastUtil.showShortToast(getApplicationContext(), jsonResult.getMessage());
				btn_login_login.setEnabled(true);
			}
		}
	}

}
