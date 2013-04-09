package edu.thu.mobilestudy.http;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;

/**
 * Http请求操作类
 * 
 * @author hujiawei
 * 
 */
public class MLHttpClient {

	private static final int OK = 200; // OK: Success!
	// private static final int NOT_MODIFIED = 304; // Not Modified: There was no new data to return.
	// private static final int BAD_REQUEST = 400; // Bad Request: The request was invalid. An accompanying error message
	// // will explain why. This is the status code will be returned during
	// // rate limiting.
	// private static final int NOT_AUTHORIZED = 401; // Not Authorized: Authentication credentials were missing or
	// // incorrect.
	// private static final int FORBIDDEN = 403; // Forbidden: The request is understood, but it has been refused. An
	// // accompanying error message will explain why.
	// private static final int NOT_FOUND = 404; // Not Found: The URI requested is invalid or the resource requested, such
	// // as a user, does not exists.
	// private static final int NOT_ACCEPTABLE = 406; // Not Acceptable: Returned by the Search API when an invalid format
	// // is specified in the request.
	// private static final int INTERNAL_SERVER_ERROR = 500;// Internal Server Error: Something is broken. Please post to
	// // the group so the Weibo team can investigate.
	// private static final int BAD_GATEWAY = 502;// Bad Gateway: Weibo is down or being upgraded.
	// private static final int SERVICE_UNAVAILABLE = 503;// Service Unavailable: The Weibo servers are up, but overloaded
	// // with requests. Try again later. The search and trend methods
	// // use this to indicate when you are being rate limited.

	// private HttpClient httpClient = new DefaultHttpClient();
	// private DefaultHttpClient httpClient = new DefaultHttpClient();
	// private HttpClient httpClient = new HttpClient(new HttpClientParams(), new SimpleHttpConnectionManager(true));

	// private MultiThreadedHttpConnectionManager connectionManager;

	private HttpClient httpClient = new HttpClient();

	// private int maxSize;

	public MLHttpClient() {
		// this(150, 30000, 30000, 1024 * 1024);
	}

	public MLHttpClient(int maxConPerHost, int conTimeOutMs, int soTimeOutMs, int maxSize) {
		// connectionManager = new MultiThreadedHttpConnectionManager();
		// HttpConnectionManagerParams params = connectionManager.getParams();
		// params.setDefaultMaxConnectionsPerHost(maxConPerHost);
		// params.setConnectionTimeout(conTimeOutMs);
		// params.setSoTimeout(soTimeOutMs);

		// HttpClientParams clientParams = new HttpClientParams();
		// clientParams.setCookiePolicy(CookiePolicy.IGNORE_COOKIES);// 忽略cookie 避免 Cookie rejected 警告
		// httpClient = new HttpClient(clientParams, connectionManager);
		// this.maxSize = maxSize;
		// Protocol myhttps = new Protocol("https", new MySSLSocketFactory(), 443);
		// Protocol.registerProtocol("https", myhttps);
		// // 支持proxy
		// if (proxyHost != null && !proxyHost.equals("")) {
		// client.getHostConfiguration().setProxy(proxyHost, proxyPort);
		// client.getParams().setAuthenticationPreemptive(true);
		// if (proxyAuthUser != null && !proxyAuthUser.equals("")) {
		// client.getState().setProxyCredentials(AuthScope.ANY,
		// new UsernamePasswordCredentials(proxyAuthUser, proxyAuthPassword));
		// log("Proxy AuthUser: " + proxyAuthUser);
		// log("Proxy AuthPassword: " + proxyAuthPassword);
		// }
		// }
	}

	// send http get request
	public MLResponse httpGet(String url) {
		return httpGet(url, null);
	}

	// send http get request with parameters
	public MLResponse httpGet(String url, MLParameter[] mlParameters) {
		if (null != mlParameters && mlParameters.length > 0) {
			String encodedParams = encodeParameters(mlParameters);
			url += "?" + encodedParams;
		}
		System.out.println("http get url: " + url);
		GetMethod getMethod = new GetMethod(url);
		return httpRequest(getMethod);
	}

	// do all http request
	public MLResponse httpRequest(HttpMethod method) {
		int responseCode = -1;
		try {
			httpClient.executeMethod(method);
			responseCode = method.getStatusCode();
			MLResponse response = new MLResponse();
//			String responseString = method.getResponseBodyAsString();
			InputStream is = method.getResponseBodyAsStream();
			response.setIs(is);
			
//			System.out.println("response string = " + responseString);
//			response.setResponseAsString(responseString);
			if (responseCode != OK) {
				System.out.println("response code = " + responseCode);
			}
//			method.releaseConnection();//do not close here!!!!
			return response;
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
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
						.append(URLEncoder.encode(mlParameters[j].value, "UTF-8"));
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
