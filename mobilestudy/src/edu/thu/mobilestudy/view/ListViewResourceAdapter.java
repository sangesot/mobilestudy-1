package edu.thu.mobilestudy.view;

import java.util.Date;
import java.util.List;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import edu.thu.mobilestudy.activity.HomeActivity;
import edu.thu.mobilestudy.activity.R;
import edu.thu.mobilestudy.activity.ResourceActivity;
import edu.thu.mobilestudy.model.Resource;
import edu.thu.mobilestudy.util.CommonUtil;
import edu.thu.mobilestudy.util.MLUtil;

/**
 * resource adapter for specific use
 * 
 * @author hujiawei
 */
public class ListViewResourceAdapter extends BaseAdapter {

	private List<Resource> listResource;

	public ListViewResourceAdapter(List<Resource> listResource) {
		this.listResource = listResource;
	}

	@Override
	public int getCount() {
		return listResource.size();
	}

	@Override
	public Object getItem(int arg0) {// no need to override
		return null;
	}

	@Override
	public long getItemId(int arg0) {// no need to override
		return 0;
	}

	@Override
	public View getView(final int position, View itemView, ViewGroup viewGroup) {// return a weibo item view
		ResourceHolder holder = null;
		if (itemView == null) {
			holder = new ResourceHolder();
			itemView = HomeActivity.layoutInflater.inflate(R.layout.item_listview_resource, null);
			holder.iv_item_info = (ImageView) itemView.findViewById(R.id.iv_item_info);
			holder.tv_item_name = (TextView) itemView.findViewById(R.id.tv_item_name);
			holder.tv_item_author = (TextView) itemView.findViewById(R.id.tv_item_author);
			holder.tv_item_catalog = (TextView) itemView.findViewById(R.id.tv_item_catalog);
			holder.tv_item_date = (TextView) itemView.findViewById(R.id.tv_item_date);
			holder.tv_item_desc = (TextView) itemView.findViewById(R.id.tv_item_desc);
			itemView.setTag(holder);
		} else {
			holder = (ResourceHolder) itemView.getTag();// tag is used to cache the specific view
		}

		final Resource resource = listResource.get(position);
		holder.tv_item_author.setText(resource.getAuthor());
		if (resource.getCatalogName() != null) {
			holder.tv_item_catalog.setText(resource.getCatalogName());
		} else {
			holder.tv_item_catalog.setText("");
		}
		holder.tv_item_name.setText(resource.getName());
		if (resource.getCatalogName() != null) {
			holder.tv_item_desc.setText(resource.getDesc());
		} else {
			holder.tv_item_desc.setText("");
		}
		holder.tv_item_date.setText(MLUtil.formatSimpleDate(parseResourceDate(resource.getFilename())));// TODO

		holder.iv_item_info.setBackgroundResource(MLUtil.determineResourceType(resource.getKeyword(), false));

		itemView.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(HomeActivity.instance, ResourceActivity.class);
				intent.putExtra("resource", resource);
				HomeActivity.instance.startActivity(intent);
			}
		});

		return itemView;
	}

	// parse resource date by filename 912635916239569/.....
	private Date parseResourceDate(String filename) {
		if (filename == null) {
			return new Date();
		}
		String longdate = filename.substring(0, filename.indexOf("/"));
		return new Date(Long.valueOf(longdate));
	}

}

/**
 * view holder for resource item
 */
class ResourceHolder {
	public ImageView iv_item_info;

	public TextView tv_item_name;
	public TextView tv_item_date;
	public TextView tv_item_author;
	public TextView tv_item_catalog;
	public TextView tv_item_desc;

}