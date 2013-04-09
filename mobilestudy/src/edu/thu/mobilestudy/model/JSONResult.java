package edu.thu.mobilestudy.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * HTTP���󷵻ص�json���ݽ���ķ�װ�� V0.1
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

	// ͨ��JSONObject����JSONResult����
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

	// ����json��������
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
