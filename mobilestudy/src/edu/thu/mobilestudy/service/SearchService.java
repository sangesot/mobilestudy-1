package edu.thu.mobilestudy.service;

import edu.thu.mobilestudy.http.MLParameter;
import edu.thu.mobilestudy.model.JSONResult;
import edu.thu.mobilestudy.model.MLException;
import edu.thu.mobilestudy.model.MobileLearning;
import edu.thu.mobilestudy.util.CommonUtil;

/**
 * 搜索服务接口
 * 
 * @author hujiawei
 * 
 */
public class SearchService extends MobileLearning {

	// same as suggestion
	public JSONResult search(MLParameter[] mlParameters) {
		try {
			return new JSONResult(httpClient
					.httpGet(CommonUtil.getValue("baseUrl") + CommonUtil.getValue("service_search"), mlParameters).asJSONObject());
		} catch (MLException e) {
			e.printStackTrace();
			return new JSONResult(CommonUtil.RESULT_CODE_EXCEPTION, e.getMessage(), null);
		}
	}
}
