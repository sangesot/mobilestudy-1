package edu.thu.mobilestudy.http;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.httpclient.params.HttpConnectionParams;

import edu.thu.mobilestudy.model.MLException;

/**
 * Http请求操作类
 * 
 * @author hujiawei
 * 
 */
public class MLHttpClient implements java.io.Serializable {

	private static final long serialVersionUID = 7322586370995910376L;
	private static final int OK = 200; // OK: Success!
	private final static int CONNECTION_TIMEOUT = 30000;// 30 seconds
	private final static int SO_TIMEOUT = 30000;
	private MultiThreadedHttpConnectionManager connectionManager;
	private HttpClient httpClient = new HttpClient();

	public MLHttpClient() {
		this(CONNECTION_TIMEOUT, SO_TIMEOUT);
	}

	// new mlhttpclient with timeout...
	public MLHttpClient(int conTimeOutMs, int soTimeOutMs) {
		connectionManager = new MultiThreadedHttpConnectionManager();
		HttpConnectionParams params = connectionManager.getParams();
		params.setConnectionTimeout(conTimeOutMs);
		params.setSoTimeout(soTimeOutMs);
		HttpClientParams clientParams = new HttpClientParams();
		clientParams.setCookiePolicy(CookiePolicy.IGNORE_COOKIES);// 忽略cookie 避免 Cookie rejected 警告
		httpClient = new HttpClient(clientParams, connectionManager);
	}

	// send http get request
	public MLResponse httpGet(String url) throws MLException {
		return httpGet(url, null);
	}

	// send http get request with parameters
	public MLResponse httpGet(String url, MLParameter[] mlParameters) throws MLException {
		if (null != mlParameters && mlParameters.length > 0) {
			String encodedParams = encodeParameters(mlParameters);
			url += "?" + encodedParams;
		}
		System.out.println("http get url: " + url);
		GetMethod getMethod = new GetMethod(url);
		return httpRequest(getMethod);
	}

	// do all http request
	public MLResponse httpRequest(HttpMethod method) throws MLException {
		int responseCode = -1;
		// try {
		try {
			httpClient.executeMethod(method);
		} catch (Exception e) {
			e.printStackTrace();// TODO comment line --> no route to host --> restart PC wifi
			throw new MLException("请求发送失败！", e);
		}
		responseCode = method.getStatusCode();
		if (responseCode != OK) {
			System.out.println("response code = " + responseCode);
			throw new MLException("exception:response code = " + responseCode);
		}
		MLResponse response = new MLResponse();
		InputStream is = null;
		try {
			is = method.getResponseBodyAsStream();
		} catch (IOException e) {
			e.printStackTrace();// TODO comment line
			throw new MLException("获取反馈失败！", e);
		}
		response.setIs(is);
		// method.releaseConnection();//do not close here!!!!
		return response;
	}

	// encode parameters using UTF-8 -- every parameter starts with '&'
	private String encodeParameters(MLParameter[] mlParameters) {
		StringBuffer buffer = new StringBuffer("");
		for (int j = 0; j < mlParameters.length; j++) {
			if (j != 0) {
				buffer.append("&");
			}
			try {
				buffer.append(URLEncoder.encode(mlParameters[j].name, "UTF-8")).append("=")
						.append(URLEncoder.encode(String.valueOf(mlParameters[j].value), "UTF-8"));
			} catch (java.io.UnsupportedEncodingException neverHappen) {
			}
		}
		return buffer.toString();
	}

	// get cause from status code
	// private static String getCause(int statusCode) {
	// String cause = null;
	// switch (statusCode) {
	// case NOT_MODIFIED:
	// break;
	// case BAD_REQUEST:
	// cause =
	// "The request was invalid.  An accompanying error message will explain why. This is the status code will be returned during rate limiting.";
	// break;
	// case NOT_AUTHORIZED:
	// cause = "Authentication credentials were missing or incorrect.";
	// break;
	// case FORBIDDEN:
	// cause = "The request is understood, but it has been refused.  An accompanying error message will explain why.";
	// break;
	// case NOT_FOUND:
	// cause = "The URI requested is invalid or the resource requested, such as a user, does not exists.";
	// break;
	// case NOT_ACCEPTABLE:
	// cause = "Returned by the Search API when an invalid format is specified in the request.";
	// break;
	// case INTERNAL_SERVER_ERROR:
	// cause = "Something is broken.  Please post to the group so the Weibo team can investigate.";
	// break;
	// case BAD_GATEWAY:
	// cause = "Weibo is down or being upgraded.";
	// break;
	// case SERVICE_UNAVAILABLE:
	// cause =
	// "Service Unavailable: The Weibo servers are up, but overloaded with requests. Try again later. The search and trend methods use this to indicate when you are being rate limited.";
	// break;
	// default:
	// cause = "";
	// }
	// return statusCode + ":" + cause;
	// }

}
