package edu.thu.mobilestudy.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * HTTP请求返回的json数据结果的封装类 V0.1
 * 
 * @author hujiawei
 */
public class JSONResult {

	private int code;// result code
	private String message;// result message
	private String content;// result content

	public JSONResult(int code, String message, String content) {
		this.code = code;
		this.message = message;
		this.content = content;
	}

	public JSONResult(JSONObject json) throws MLException {
		super();
		init(json);
	}

	// 通过JSONObject创建JSONResult对象
	private void init(JSONObject json) throws MLException {
		if (json != null) {
			try {
				code = json.getInt("code");
				message = json.getString("message");
				content = json.getString("content");
			} catch (JSONException jsone) {
				throw new MLException(jsone.getMessage() + ":" + json.toString(), jsone);
			}
		}
	}

	// 构建json数据内容
	public String buildJsonContent() {
		StringBuffer result = new StringBuffer();
		// {"code":0/-1/1,"message":"message","content":{content}}
		result.append("{\"code\":").append(code).append(",\"message\":\"").append(message).append("\",\"content\":").append(content)
				.append("}");
		return result.toString();
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
