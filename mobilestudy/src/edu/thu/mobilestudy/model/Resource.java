package edu.thu.mobilestudy.model;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

import edu.thu.mobilestudy.util.CommonUtil;

/**
 * 资源类
 * 
 * @author hujiawei
 * 
 */
public class Resource implements Serializable {

	private static final long serialVersionUID = 3706071648466093655L;
	private long id;//
	private long catalogId;// catalog id may be null
	private String catalogName;// catalog name may be null
	private String name;
	private String desc;// may be null
	private String keyword;
	private String author;
	private String userId;
	private String filename;
	private String fileext;
	private String lompath;// may be null
	private int status;

	public Resource(String jsonsString) throws JSONException, MLException {
		this(new JSONObject(jsonsString));
	}

	public Resource(JSONObject json) throws MLException {
		init(json);
	}

	// 通过JSONObject创建User对象
	private void init(JSONObject json) throws MLException {
		if (json != null) {
			try {
				id = json.getLong("id");
				name = json.getString("name");
				author = json.getString("author");
				fileext = json.getString("fileext");
				filename = json.getString("filename");
				status = json.getInt("status");
				userId = json.getString("userId");
				if (!json.isNull("keyword")) {
					keyword = json.getString("keyword");
				}
				if (!json.isNull("desc")) {
					desc = json.getString("desc");
				}
				if (!json.isNull("lompath")) {
					lompath = json.getString("lompath");
				}
				if (!json.isNull("catalogId")) {
					catalogId = json.getLong("catalogId");
				}
				if (!json.isNull("catalogName")) {
					catalogName = json.getString("catalogName");
				}
			} catch (JSONException jsone) {
				throw new MLException(jsone.getMessage() + ":" + json.toString(), jsone);
			}
		}
	}

	@Override
	public String toString() {
		return "";
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public long getCatalogId() {
		return catalogId;
	}

	public void setCatalogId(long catalogId) {
		this.catalogId = catalogId;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getFileext() {
		return fileext;
	}

	public void setFileext(String fileext) {
		this.fileext = fileext;
	}

	public String getLompath() {
		return lompath;
	}

	public void setLompath(String lompath) {
		this.lompath = lompath;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getCatalogName() {
		return catalogName;
	}

	public void setCatalogName(String catalogName) {
		this.catalogName = catalogName;
	}

}
