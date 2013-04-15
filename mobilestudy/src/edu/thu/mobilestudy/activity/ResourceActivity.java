package edu.thu.mobilestudy.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import edu.thu.mobilestudy.model.Resource;
import edu.thu.mobilestudy.util.MLUtil;

/**
 * 资源详情界面
 * 
 * @author hujiawei
 * 
 */
public class ResourceActivity extends Activity {

	private Resource resource;

	private TextView tv_resource_name;
	private TextView tv_resource_catalog;
	private TextView tv_resource_author;
	private TextView tv_resource_desc;
	private TextView tv_resource_fileext;
	private ImageView iv_resource_image;

	private Button btn_resource_back;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_activity_resource);
		resource = getIntent().getParcelableExtra("resource");

		tv_resource_author = (TextView) findViewById(R.id.tv_resource_author);
		tv_resource_catalog = (TextView) findViewById(R.id.tv_resource_catalog);
		tv_resource_desc = (TextView) findViewById(R.id.tv_resource_desc);
		tv_resource_fileext = (TextView) findViewById(R.id.tv_resource_fileext);
		tv_resource_name = (TextView) findViewById(R.id.tv_resource_name);
		btn_resource_back = (Button) findViewById(R.id.btn_resource_back);
		iv_resource_image = (ImageView) findViewById(R.id.iv_resource_image);

		tv_resource_author.setText(resource.getAuthor());
		tv_resource_name.setText(resource.getName());
		tv_resource_catalog.setText(resource.getCatalogName());
		tv_resource_desc.setText(resource.getDesc());
		tv_resource_fileext.setText(resource.getFileext());
		iv_resource_image.setBackgroundResource(MLUtil.determineResourceType(resource.getKeyword(), false));

		btn_resource_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				ResourceActivity.this.finish();
			}
		});
	}

}
