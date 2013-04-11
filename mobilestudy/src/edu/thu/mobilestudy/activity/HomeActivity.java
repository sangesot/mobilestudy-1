package edu.thu.mobilestudy.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import edu.thu.mobilestudy.http.MLParameter;
import edu.thu.mobilestudy.model.JSONResult;
import edu.thu.mobilestudy.model.ResourceWrapper;
import edu.thu.mobilestudy.service.DocumentService;
import edu.thu.mobilestudy.util.CommonUtil;
import edu.thu.mobilestudy.util.ToastUtil;
import edu.thu.mobilestudy.view.CustomListView;
import edu.thu.mobilestudy.view.CustomListView.OnRefreshListener;
import edu.thu.mobilestudy.view.ResourceListViewAdapter;

/**
 * 主界面
 * 
 * @author hujiawei
 * 
 */
public class HomeActivity extends Activity {

	public static HomeActivity instance;
	public static LayoutInflater layoutInflater;

	private ViewPager vp_home_center;

	private View tab_resource;
	private View tab_search;
	private View tab_download;
	private View tab_setting;

	private LinearLayout ll_home_resource;
	private LinearLayout ll_home_search;
	private LinearLayout ll_home_download;
	private LinearLayout ll_home_setting;

	private ImageView iv_home_resource;
	private ImageView iv_home_search;
	private ImageView iv_home_download;
	private ImageView iv_home_setting;

	// private ImageView iv_home_cloud;

	// private int currentIndex;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_activity_home);
		instance = this;

		layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		vp_home_center = (ViewPager) findViewById(R.id.vp_home_center);

		ll_home_resource = (LinearLayout) findViewById(R.id.ll_home_resource);
		ll_home_resource.setOnClickListener(new HomeTabOnClickListener(0));
		ll_home_search = (LinearLayout) findViewById(R.id.ll_home_search);
		ll_home_search.setOnClickListener(new HomeTabOnClickListener(1));
		ll_home_download = (LinearLayout) findViewById(R.id.ll_home_download);
		ll_home_download.setOnClickListener(new HomeTabOnClickListener(2));
		ll_home_setting = (LinearLayout) findViewById(R.id.ll_home_setting);
		ll_home_setting.setOnClickListener(new HomeTabOnClickListener(3));

		iv_home_resource = (ImageView) findViewById(R.id.iv_home_resource);
		iv_home_search = (ImageView) findViewById(R.id.iv_home_search);
		iv_home_download = (ImageView) findViewById(R.id.iv_home_download);
		iv_home_setting = (ImageView) findViewById(R.id.iv_home_setting);
		// iv_home_cloud = (ImageView) findViewById(R.id.iv_home_cloud);

		tab_resource = layoutInflater.inflate(R.layout.home_tab_resource, null);
		tab_search = layoutInflater.inflate(R.layout.home_tab_search, null);
		tab_download = layoutInflater.inflate(R.layout.home_tab_download, null);
		tab_setting = layoutInflater.inflate(R.layout.home_tab_setting, null);

		final ArrayList<View> views = new ArrayList<View>();
		views.add(tab_resource);
		views.add(tab_search);
		views.add(tab_download);
		views.add(tab_setting);
		// viewpager page adapter
		PagerAdapter mPagerAdapter = new PagerAdapter() {
			@Override
			public void destroyItem(View container, int position, Object object) {
				((ViewPager) container).removeView(views.get(position));
			}

			@Override
			public int getCount() {
				return views.size();
			}

			@Override
			public Object instantiateItem(View container, int position) {
				((ViewPager) container).addView(views.get(position));
				return views.get(position);
			}

			@Override
			public boolean isViewFromObject(View arg0, Object arg1) {
				return arg0 == arg1;
			}
		};
		vp_home_center.setAdapter(mPagerAdapter);
		vp_home_center.setOnPageChangeListener(new HomeOnPageChangeListener());
		// set view pager not to slide left or right
		vp_home_center.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				return true;
			}
		});
		vp_home_center.setCurrentItem(0);// default
		iv_home_resource.setImageResource(R.drawable.star_purple);// first tab

		initTabResourceUI();
	}

	// page change listener
	private class HomeOnPageChangeListener implements OnPageChangeListener {
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}

		@Override
		public void onPageSelected(int arg0) {
			switch (arg0) {
			case 0:
				iv_home_resource.setImageResource(R.drawable.star_purple);
				iv_home_search.setImageResource(R.drawable.search);
				iv_home_download.setImageResource(R.drawable.download);
				iv_home_setting.setImageResource(R.drawable.setting);
				break;
			case 1:
				iv_home_resource.setImageResource(R.drawable.star);
				iv_home_search.setImageResource(R.drawable.search_purple);
				iv_home_download.setImageResource(R.drawable.download);
				iv_home_setting.setImageResource(R.drawable.setting);
				break;
			case 2:
				iv_home_resource.setImageResource(R.drawable.star);
				iv_home_search.setImageResource(R.drawable.search);
				iv_home_download.setImageResource(R.drawable.download_purple);
				iv_home_setting.setImageResource(R.drawable.setting);
				break;
			case 3:
				iv_home_resource.setImageResource(R.drawable.star);
				iv_home_search.setImageResource(R.drawable.search);
				iv_home_download.setImageResource(R.drawable.download);
				iv_home_setting.setImageResource(R.drawable.setting_purple);
				break;
			}
			// currentIndex = arg0;
		}
	}

	// home tab click listener
	private class HomeTabOnClickListener implements View.OnClickListener {
		private int index = 0;

		public HomeTabOnClickListener(int i) {
			index = i;
		}

		@Override
		public void onClick(View v) {
			vp_home_center.setCurrentItem(index);
		}
	}

	// ///////////////////// tab resource ////////////////////////

	private Button btn_resource_new;
	private Button btn_resource_hot;
	private CustomListView lv_resource_new;
	private CustomListView lv_resource_hot;
	private RelativeLayout rl_resource_loading;
	private int currentResourceType = CommonUtil.SUGGESTION_TYPE_NEW;

	private void initTabResourceUI() {
		btn_resource_new = (Button) tab_resource.findViewById(R.id.btn_resource_new);
		btn_resource_hot = (Button) tab_resource.findViewById(R.id.btn_resource_hot);
		lv_resource_new = (CustomListView) tab_resource.findViewById(R.id.lv_resource_new);
		lv_resource_hot = (CustomListView) tab_resource.findViewById(R.id.lv_resource_hot);
		rl_resource_loading = (RelativeLayout) tab_resource.findViewById(R.id.rl_resource_loading);
		btn_resource_new.setSelected(true);
		btn_resource_new.setOnClickListener(new ResourceOnClickListener(CommonUtil.SUGGESTION_TYPE_NEW));
		btn_resource_hot.setOnClickListener(new ResourceOnClickListener(CommonUtil.SUGGESTION_TYPE_HOT));
		lv_resource_new.setOnRefreshListener(new OnRefreshListener() {
			public void onRefresh() {
				return;
				// new LoadResourceTask().execute(new Integer[] { CommonUtil.SUGGESTION_TYPE_NEW, CommonUtil.SUGGESTION_DEFAULT_COUNT });
			}
		});
		lv_resource_hot.setOnRefreshListener(new OnRefreshListener() {
			public void onRefresh() {
				return;
				// new LoadResourceTask().execute(new Integer[] { CommonUtil.SUGGESTION_TYPE_HOT, CommonUtil.SUGGESTION_DEFAULT_COUNT });
			}
		});

		new LoadResourceTask().execute(new Integer[] { CommonUtil.SUGGESTION_TYPE_NEW, CommonUtil.SUGGESTION_DEFAULT_COUNT });
	}

	// resource type click listener
	private class ResourceOnClickListener implements View.OnClickListener {
		private int type = CommonUtil.SUGGESTION_TYPE_NEW;

		public ResourceOnClickListener(int type) {
			this.type = type;
		}

		@Override
		public void onClick(View v) {
			if (type == CommonUtil.SUGGESTION_TYPE_NEW) {
				btn_resource_new.setSelected(true);
				btn_resource_hot.setSelected(false);
			} else {
				btn_resource_new.setSelected(false);
				btn_resource_hot.setSelected(true);
			}
			loadResources(type);
		}
	}

	// load resources by type
	public void loadResources(int type) {

	}

	// login task
	class LoadResourceTask extends AsyncTask<Integer[], Void, JSONResult> {

		@Override
		protected void onCancelled() {
			super.onCancelled();
		}

		@Override
		protected void onPreExecute() {
			System.out.println("onPreExecute");
			rl_resource_loading.setVisibility(View.VISIBLE);
			lv_resource_hot.setVisibility(View.GONE);
			lv_resource_new.setVisibility(View.GONE);
		}

		@Override
		protected JSONResult doInBackground(Integer[]... params) {
			currentResourceType = params[0][0];
			System.out.println("doInBackground");
			MLParameter[] mlParameters = new MLParameter[] { new MLParameter("action", CommonUtil.getValue("action_suggestion")),
					new MLParameter("repository", CommonUtil.getValue("repository_des")), new MLParameter("suggestionType", params[0][0]),
					new MLParameter("count", params[0][1]) };
			return new DocumentService().suggestion(mlParameters);
		}

		@Override
		protected void onProgressUpdate(Void... values) {
			super.onProgressUpdate(values);
		}

		@Override
		protected void onPostExecute(JSONResult jsonResult) {
			rl_resource_loading.setVisibility(View.GONE);
			System.out.println("onPostExecute");
			if (jsonResult.getCode() == CommonUtil.RESULT_CODE_SUCCEED) {
				try {
					ResourceWrapper resourceWrapper = ResourceWrapper.constructResourceWrapper(jsonResult.getContent());

					if (currentResourceType == CommonUtil.SUGGESTION_TYPE_NEW) {
						lv_resource_new.setVisibility(View.VISIBLE);
						lv_resource_new.setAdapter(new ResourceListViewAdapter(resourceWrapper.getResourceList()));
					} else {
						lv_resource_hot.setVisibility(View.VISIBLE);
						lv_resource_hot.setAdapter(new ResourceListViewAdapter(resourceWrapper.getResourceList()));
					}

				} catch (Exception e) {
					e.printStackTrace();
					ToastUtil.showShortToast(getApplicationContext(), "解析结果出错！");
				}

			} else {// exception occurs or fail
				ToastUtil.showShortToast(getApplicationContext(), jsonResult.getMessage());
			}
		}
	}

}
