/*
Copyright (c) 2007-2009, Yusuke Yamamoto
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:
 * Redistributions of source code must retain the above copyright
      notice, this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright
      notice, this list of conditions and the following disclaimer in the
      documentation and/or other materials provided with the distribution.
 * Neither the name of the Yusuke Yamamoto nor the
      names of its contributors may be used to endorse or promote products
      derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY Yusuke Yamamoto ``AS IS'' AND ANY
EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL Yusuke Yamamoto BE LIABLE FOR ANY
DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package edu.thu.mobilestudy.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;

import edu.thu.mobilestudy.model.MLException;

/**
 * 代表HTTP response的数据类
 * 
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @author hujiawei
 */
public class MLResponse {

	// 这个是用来生成XML Document Builder的单线程，保证每次取的时候都是不同的
	// private static ThreadLocal<DocumentBuilder> builders = new ThreadLocal<DocumentBuilder>() {
	// @Override
	// protected DocumentBuilder initialValue() {
	// try {
	// return DocumentBuilderFactory.newInstance().newDocumentBuilder();
	// } catch (ParserConfigurationException ex) {
	// throw new ExceptionInInitializerError(ex);
	// }
	// }
	// };

	private int statusCode;// 状态码
	// private Document responseAsDocument = null;// 对应的XML
	private String responseAsString = null;// 返回的String
	private InputStream is;// response对应的输入流
	// private HttpURLConnection con;// http 连接
	private boolean streamConsumed = false;// 流已经consume了，意思就是流已经部分或者全部被读取了

	public MLResponse() {
	}

	// public MLResponse(HttpURLConnection con) throws IOException {
	// this.con = con;
	// this.statusCode = con.getResponseCode();
	// if (null == (is = con.getErrorStream())) {
	// is = con.getInputStream();
	// }
	// // if (null != is && "gzip".equals(con.getContentEncoding())) {
	// // // the response is gzipped
	// // is = new GZIPInputStream(is);
	// // }
	// }

	/**
	 * Returns the response stream.<br>
	 * This method cannot be called after calling asString() or asDcoument()<br>
	 * It is suggested to call disconnect() after consuming the stream.
	 * 
	 * Disconnects the internal HttpURLConnection silently.
	 */
	public InputStream asStream() {
		if (streamConsumed) {
			throw new IllegalStateException("Stream has already been consumed.");
		}
		return is;
	}

	/**
	 * Returns the response body as string.<br>
	 * Disconnects the internal HttpURLConnection silently.
	 * 
	 */
	public String asString() /* throws MLException */{
		if (null == responseAsString) {
			BufferedReader br;
			try {
				InputStream stream = asStream();
				if (null == stream) {
					return null;
				}
				br = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
				StringBuffer buf = new StringBuffer();
				String line;
				while (null != (line = br.readLine())) {
					buf.append(line).append("\n");
				}
				this.responseAsString = buf.toString();
				System.out.println(responseAsString);
				stream.close();
				// con.disconnect();//hjw
				streamConsumed = true;
			} catch (NullPointerException npe) {
				// throw new MLException(npe.getMessage(), npe);
				npe.printStackTrace();
			} catch (IOException ioe) {
				// throw new MLException(ioe.getMessage(), ioe);
				ioe.printStackTrace();
			}
		}
		return responseAsString;
	}

	/**
	 * Returns the response body as org.w3c.dom.Document.<br>
	 * Disconnects the internal HttpURLConnection silently.
	 * 
	 */
	// public Document asDocument() throws MLException {
	// if (null == responseAsDocument) {
	// try {
	// // it should be faster to read the inputstream directly.
	// // but makes it difficult to troubleshoot
	// this.responseAsDocument = builders.get().parse(new ByteArrayInputStream(asString().getBytes("UTF-8")));
	// } catch (SAXException saxe) {
	// throw new MLException("The response body was not well-formed:\n" + responseAsString, saxe);
	// } catch (IOException ioe) {
	// throw new MLException("There's something with the connection.", ioe);
	// }
	// }
	// return responseAsDocument;
	// }

	// public InputStreamReader asReader() {
	// try {
	// return new InputStreamReader(is, "UTF-8");
	// } catch (java.io.UnsupportedEncodingException uee) {
	// return new InputStreamReader(is);
	// }
	// }

	/**
	 * Returns the response body as edu.thu.istudy.org.json.JSONObject.<br>
	 * Disconnects the internal HttpURLConnection silently.
	 */
	public JSONObject asJSONObject() throws MLException {
		try {
			return new JSONObject(asString());
		} catch (JSONException jsone) {
			throw new MLException(jsone.getMessage() + ":" + this.responseAsString, jsone);
		}
	}

	/**
	 * Returns the response body as edu.thu.istudy.org.json.JSONArray.<br>
	 * Disconnects the internal HttpURLConnection silently.
	 */
	public JSONArray asJSONArray() throws MLException {
		try {
			return new JSONArray(asString());
		} catch (Exception jsone) {
			throw new MLException(jsone.getMessage() + ":" + this.responseAsString, jsone);
		}
	}

	// public void disconnect() {
	// con.disconnect();
	// }

	// @Override
	// public String toString() {
	// if (null != responseAsString) {
	// return responseAsString;
	// }
	// return "Response{" + "statusCode=" + statusCode + ", response=" + responseAsDocument + ", responseString='" + responseAsString
	// + '\'' + ", is=" + is + ", con=" + con + '}';
	// }
	//
	// public String getResponseHeader(String name) {
	// if (con != null)
	// return con.getHeaderField(name);
	// else
	// return null;
	// }

	public String getResponseAsString() {
		return responseAsString;
	}

	public void setResponseAsString(String responseAsString) {
		this.responseAsString = responseAsString;
	}

	// encode responseString with UTF-8 //hjw
	// private void encodeResponseString(String responseString) {
	// try {
	// System.out.println(responseString);
	// this.responseAsString = new String(responseString.getBytes(), "UTF-8");
	// System.out.println(responseAsString);
	// } catch (UnsupportedEncodingException e) {
	// e.printStackTrace();
	// }
	// }

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public InputStream getIs() {
		return is;
	}

	public void setIs(InputStream is) {
		this.is = is;
	}

	// hjw do not use it
	// private static Pattern escaped = Pattern.compile("&#([0-9]{3,5});");
	//
	// /**
	// * Unescape UTF-8 escaped characters to string.
	// *
	// * @author pengjianq...@gmail.com
	// *
	// * @param original
	// * The string to be unescaped.
	// * @return The unescaped string
	// */
	// public static String unescape(String original) {
	// Matcher mm = escaped.matcher(original);
	// StringBuffer unescaped = new StringBuffer();
	// while (mm.find()) {
	// mm.appendReplacement(unescaped, Character.toString((char) Integer.parseInt(mm.group(1), 10)));
	// }
	// mm.appendTail(unescaped);
	// return unescaped.toString();
	// }

}
