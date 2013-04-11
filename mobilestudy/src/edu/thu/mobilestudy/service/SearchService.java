package edu.thu.mobilestudy.service;

import edu.thu.mobilestudy.http.MLParameter;
import edu.thu.mobilestudy.model.JSONResult;
import edu.thu.mobilestudy.model.MLException;
import edu.thu.mobilestudy.model.MobileLearning;
import edu.thu.mobilestudy.util.CommonUtil;

/**
 * ��������ӿ�
 * 
 * @author hujiawei
 * 
 */
public class SearchService extends MobileLearning {

	public JSONResult search(MLParameter[] mlParameters) {
		try {
			return new JSONResult(httpClient.httpGet(CommonUtil.getValue("baseUrl") + CommonUtil.getValue("service_search"), mlParameters)
					.asJSONObject());
		} catch (MLException e) {
			e.printStackTrace();
		}
		return null;
	}

}
