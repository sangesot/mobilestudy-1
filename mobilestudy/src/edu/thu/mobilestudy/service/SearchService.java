package edu.thu.mobilestudy.service;

import edu.thu.mobilestudy.http.MLParameter;
import edu.thu.mobilestudy.model.JSONResult;
import edu.thu.mobilestudy.model.MLException;
import edu.thu.mobilestudy.model.MobileLearning;

/**
 * 搜索服务接口
 * 
 * @author hujiawei
 * 
 */
public class SearchService extends MobileLearning {

	public JSONResult search(String url, MLParameter[] mlParameters) {
		try {
			return new JSONResult(httpClient.httpGet(url, mlParameters).asJSONObject());
		} catch (MLException e) {
			e.printStackTrace();
		}
		return null;
	}

}
