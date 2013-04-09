package edu.thu.mobilestudy.model;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

import edu.thu.mobilestudy.util.LoginUtil;

/**
 * 用户类
 * 
 * @author hujiawei
 * 
 */
public class User implements Serializable {

	private static final long serialVersionUID = 7757943022030658224L;

	private String userId;
	private String userName;
	private String password;
	private int loginMode;//登录方式

	public User(String userId, String userName) {
		this(userId, userName, "", LoginUtil.NOT_LOGIN);
	}

	public User(String userId, String userName, String password, int loginMode) {
		this.userId = userId;
		this.userName = userName;
		this.password = password;
		this.loginMode = loginMode;
	}

	public User(String jsonsString) throws JSONException, MLException {
		this(new JSONObject(jsonsString));
	}

	public User(JSONObject json) throws MLException {
		init(json);
	}

	// 通过JSONObject创建User对象
	private void init(JSONObject json) throws MLException {
		if (json != null) {
			try {
				userId = json.getString("userId");
				userName = json.getString("userName");
				System.out.println("userid=" + userId + " userName=" + userName);
			} catch (JSONException jsone) {
				throw new MLException(jsone.getMessage() + ":" + json.toString(), jsone);
			}
		}
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getLoginMode() {
		return loginMode;
	}

	public void setLoginMode(int loginMode) {
		this.loginMode = loginMode;
	}

}
