package edu.thu.mobilestudy.model;

import edu.thu.mobilestudy.http.MLHttpClient;

/**
 * 所有bean的父类，抽象出MobileLearning，设置里面的httpClient
 * 
 * @author hujiawei
 * 
 */
public class MobileLearning {

	public MLHttpClient httpClient = new MLHttpClient();

}
