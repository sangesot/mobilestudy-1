package edu.thu.mobilestudy.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import edu.thu.mobilestudy.http.MLResponse;

/**
 * 资源列表的包装类
 * 
 * @author hujiawei
 * 
 */

public class ResourceWrapper implements Serializable {

	private static final long serialVersionUID = 5784396349159493301L;
	private List<Resource> resourceList;

	public ResourceWrapper(List<Resource> resourceList) {
		this.resourceList = resourceList;
	}

	// construct resource wrapper by string
	public static ResourceWrapper constructResourceWrapper(String jsonString) throws JSONException, MLException {
		return constructResourceWrapper(new JSONArray(jsonString));
	}

	// construct resource wrapper
	public static ResourceWrapper constructResourceWrapper(JSONArray jsonArray) throws MLException, JSONException {
		List<Resource> list = new ArrayList<Resource>();
		for (int i = 0; i < jsonArray.length(); i++) {
			list.add(new Resource(jsonArray.getJSONObject(i)));
		}
		return new ResourceWrapper(list);
	}

	public List<Resource> getResourceList() {
		return resourceList;
	}

	public void setResourceList(List<Resource> resourceList) {
		this.resourceList = resourceList;
	}

}
