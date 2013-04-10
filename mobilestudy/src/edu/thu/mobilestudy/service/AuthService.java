package edu.thu.mobilestudy.service;

import edu.thu.mobilestudy.http.MLParameter;
import edu.thu.mobilestudy.model.JSONResult;
import edu.thu.mobilestudy.model.MLException;
import edu.thu.mobilestudy.model.MobileLearning;
import edu.thu.mobilestudy.util.CommonUtil;

/**
 * 认证服务接口
 * 
 * @author hujiawei
 * 
 */
public class AuthService extends MobileLearning {

	// do login action,and catch exception,for here knows what to response to the user
	public JSONResult login(String url, MLParameter[] mlParameters) {
		try {
			return new JSONResult(httpClient.httpGet(url, mlParameters).asJSONObject());
		} catch (MLException e) {
			e.printStackTrace();
			return new JSONResult(CommonUtil.RESULT_CODE_EXCEPTION, e.getMessage(), null);
		}
	}

}
