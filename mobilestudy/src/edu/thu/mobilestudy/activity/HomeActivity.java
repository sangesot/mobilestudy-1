package edu.thu.mobilestudy.activity;

import java.util.ArrayList;
import java.util.List;

import android.R.bool;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import edu.thu.mobilestudy.http.MLParameter;
import edu.thu.mobilestudy.model.JSONResult;
import edu.thu.mobilestudy.model.Resource;
import edu.thu.mobilestudy.model.ResourceWrapper;
import edu.thu.mobilestudy.service.DocumentService;
import edu.thu.mobilestudy.service.SearchService;
import edu.thu.mobilestudy.util.CommonUtil;
import edu.thu.mobilestudy.util.ToastUtil;
import edu.thu.mobilestudy.view.CustomListView;
import edu.thu.mobilestudy.view.CustomListView.OnRefreshListener;
import edu.thu.mobilestudy.view.ListViewResourceAdapter;

/**
 * 主界面
 * 
 * @author hujiawei
 * 
 */
public class HomeActivity extends Activity {

	public static HomeActivity instance;
	public static LayoutInflater layoutInflater;
	
	private int sound_refresh = R.raw.listview_refresh;
	public MediaPlayer musicPlayer;
	private SharedPreferences sharedPreferences;
	private InputMethodManager inputMethodManager;

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

	private TextView tv_home_resource;
	private TextView tv_home_search;
	private TextView tv_home_download;
	private TextView tv_home_setting;

	// private ImageView iv_home_cloud;

	// private int currentIndex;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_activity_home);
		instance = this;
		inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
		layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		sharedPreferences = getSharedPreferences("mobilestudy", MODE_PRIVATE);
		musicPlayer = MediaPlayer.create(getApplicationContext(), sound_refresh);
		musicPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

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

		tv_home_resource = (TextView) findViewById(R.id.tv_home_resource);
		tv_home_search = (TextView) findViewById(R.id.tv_home_search);
		tv_home_download = (TextView) findViewById(R.id.tv_home_download);
		tv_home_setting = (TextView) findViewById(R.id.tv_home_setting);

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
		iv_home_resource.setSelected(true);// first tab
		tv_home_resource.setTextColor(getResources().getColor(R.color.text_toolbar_select));
		initTabResourceUI();

		initTabSearchUI();

		initTabSettingUI();
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
			unSelectAllTab();
			switch (arg0) {
			case 0:
				iv_home_resource.setSelected(true);
				tv_home_resource.setTextColor(getResources().getColor(R.color.text_toolbar_select));
				break;
			case 1:
				iv_home_search.setSelected(true);
				tv_home_search.setTextColor(getResources().getColor(R.color.text_toolbar_select));
				break;
			case 2:
				iv_home_download.setSelected(true);
				tv_home_download.setTextColor(getResources().getColor(R.color.text_toolbar_select));
				break;
			case 3:
				iv_home_setting.setSelected(true);
				tv_home_setting.setTextColor(getResources().getColor(R.color.text_toolbar_select));
				break;
			}
			// currentIndex = arg0;
		}

		private void unSelectAllTab() {
			iv_home_download.setSelected(false);
			iv_home_resource.setSelected(false);
			iv_home_search.setSelected(false);
			iv_home_setting.setSelected(false);
			tv_home_download.setTextColor(getResources().getColor(R.color.text_toolbar_normal));
			tv_home_resource.setTextColor(getResources().getColor(R.color.text_toolbar_normal));
			tv_home_search.setTextColor(getResources().getColor(R.color.text_toolbar_normal));
			tv_home_setting.setTextColor(getResources().getColor(R.color.text_toolbar_normal));
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

	private List<Resource> listResourceNew;// resource list
	private List<Resource> listResourceHot;
	private ListViewResourceAdapter listViewAdapterNew;// list adapter
	private ListViewResourceAdapter listViewAdapterHot;
	private boolean loadedNew = false;// whether the listview has been loaded
	private boolean loadedHot = false;

	private void initTabResourceUI() {
		btn_resource_new = (Button) tab_resource.findViewById(R.id.btn_resource_new);
		btn_resource_hot = (Button) tab_resource.findViewById(R.id.btn_resource_hot);
		lv_resource_new = (CustomListView) tab_resource.findViewById(R.id.lv_resource_new);
		lv_resource_hot = (CustomListView) tab_resource.findViewById(R.id.lv_resource_hot);
		rl_resource_loading = (RelativeLayout) tab_resource.findViewById(R.id.rl_resource_loading);
		listResourceNew = new ArrayList<Resource>();
		listResourceHot = new ArrayList<Resource>();
		btn_resource_new.setSelected(true);
		btn_resource_new.setOnClickListener(new ResourceOnClickListener(CommonUtil.SUGGESTION_TYPE_NEW));
		btn_resource_hot.setOnClickListener(new ResourceOnClickListener(CommonUtil.SUGGESTION_TYPE_HOT));
		// if refresh listener not set,then listview can not be pulled to refresh
		lv_resource_new.setOnRefreshListener(new OnRefreshListener() {
			public void onRefresh() {
				loadOrRefreshResources(CommonUtil.SUGGESTION_TYPE_NEW, true);
			}
		});
		lv_resource_hot.setOnRefreshListener(new OnRefreshListener() {
			public void onRefresh() {
				loadOrRefreshResources(CommonUtil.SUGGESTION_TYPE_HOT, true);
			}
		});

		loadOrRefreshResources(CommonUtil.SUGGESTION_TYPE_NEW, false);
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
				if (loadedNew) {
					hideAllResourceListView();
					lv_resource_new.setVisibility(View.VISIBLE);
				} else {
					loadOrRefreshResources(type, false);
				}
			} else {
				btn_resource_new.setSelected(false);
				btn_resource_hot.setSelected(true);
				if (loadedHot) {
					hideAllResourceListView();
					lv_resource_hot.setVisibility(View.VISIBLE);
				} else {
					loadOrRefreshResources(type, false);
				}
			}
		}
	}

	// load resources by type
	public void loadOrRefreshResources(int type, boolean flag) {
		if (type == CommonUtil.SUGGESTION_TYPE_NEW) {
			if (flag) {
				new RefreshResourceTask().execute(CommonUtil.SUGGESTION_TYPE_NEW);
			} else {
				new LoadResourceTask().execute(CommonUtil.SUGGESTION_TYPE_NEW);
			}
		} else {
			if (flag) {
				new RefreshResourceTask().execute(CommonUtil.SUGGESTION_TYPE_HOT);
			} else {
				new LoadResourceTask().execute(CommonUtil.SUGGESTION_TYPE_HOT);
			}
		}
	}

	// hide all resource list view, just show loading page
	private void hideAllResourceListView() {
		lv_resource_hot.setVisibility(View.GONE);
		lv_resource_new.setVisibility(View.GONE);
	}

	// load resource task
	class LoadResourceTask extends AsyncTask<Integer, Void, JSONResult> {

		@Override
		protected void onCancelled() {
			super.onCancelled();
		}

		@Override
		protected void onPreExecute() {
			System.out.println("onPreExecute");
			hideAllResourceListView();
			rl_resource_loading.setVisibility(View.VISIBLE);
		}

		@Override
		protected JSONResult doInBackground(Integer... params) {
			System.out.println("doInBackground");
			currentResourceType = params[0];
			MLParameter[] mlParameters = new MLParameter[] { new MLParameter("action", CommonUtil.getValue("action_suggestion")),
					new MLParameter("repository", CommonUtil.getValue("repository_des")), new MLParameter("suggestionType", params[0]),
					new MLParameter("count", CommonUtil.SUGGESTION_DEFAULT_COUNT) };
			return new DocumentService().suggestion(mlParameters);
		}

		@Override
		protected void onProgressUpdate(Void... values) {
			super.onProgressUpdate(values);
		}

		@Override
		protected void onPostExecute(JSONResult jsonResult) {
			System.out.println("onPostExecute");
			rl_resource_loading.setVisibility(View.GONE);
			if (sharedPreferences.getBoolean("setting_sound", true)) {
				musicPlayer.start();
			}
			if (jsonResult.getCode() == CommonUtil.RESULT_CODE_SUCCEED) {
				try {
					ResourceWrapper resourceWrapper = ResourceWrapper.constructResourceWrapper(jsonResult.getContent());
					if (currentResourceType == CommonUtil.SUGGESTION_TYPE_NEW) {
						lv_resource_new.setVisibility(View.VISIBLE);
						listResourceNew = resourceWrapper.getResourceList();
						listViewAdapterNew = new ListViewResourceAdapter(listResourceNew);
						lv_resource_new.setAdapter(listViewAdapterNew);
						loadedNew = true;
					} else {
						lv_resource_hot.setVisibility(View.VISIBLE);
						listResourceHot = resourceWrapper.getResourceList();
						listViewAdapterHot = new ListViewResourceAdapter(listResourceHot);
						lv_resource_hot.setAdapter(listViewAdapterHot);
						loadedHot = true;
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

	// refresh resource task
	class RefreshResourceTask extends AsyncTask<Integer, Void, JSONResult> {

		@Override
		protected void onCancelled() {
			super.onCancelled();
		}

		@Override
		protected void onPreExecute() {
			System.out.println("onPreExecute");
		}

		@Override
		protected JSONResult doInBackground(Integer... params) {
			System.out.println("doInBackground");
			currentResourceType = params[0];
			MLParameter[] mlParameters = new MLParameter[] { new MLParameter("action", CommonUtil.getValue("action_suggestion")),
					new MLParameter("repository", CommonUtil.getValue("repository_des")), new MLParameter("suggestionType", params[0]),
					new MLParameter("count", CommonUtil.SUGGESTION_DEFAULT_COUNT) };
			return new DocumentService().suggestion(mlParameters);
		}

		@Override
		protected void onProgressUpdate(Void... values) {
			super.onProgressUpdate(values);
		}

		@Override
		protected void onPostExecute(JSONResult jsonResult) {
			System.out.println("onPostExecute");
			if (jsonResult.getCode() == CommonUtil.RESULT_CODE_SUCCEED) {
				if (sharedPreferences.getBoolean("setting_sound", true)) {
					musicPlayer.start();
				}
				try {
					ResourceWrapper resourceWrapper = ResourceWrapper.constructResourceWrapper(jsonResult.getContent());
					if (currentResourceType == CommonUtil.SUGGESTION_TYPE_NEW) {
						listResourceNew = resourceWrapper.getResourceList();
						listViewAdapterNew.notifyDataSetChanged();
						lv_resource_new.onRefreshComplete();
					} else {
						listResourceHot = resourceWrapper.getResourceList();
						listViewAdapterHot.notifyDataSetChanged();
						lv_resource_hot.onRefreshComplete();
					}
				} catch (Exception e) {
					e.printStackTrace();
					ToastUtil.showShortToast(getApplicationContext(), "解析结果出错！");
				}

			} else {// exception occurs or fail
				// complete the refresh
				if (currentResourceType == CommonUtil.SUGGESTION_TYPE_NEW) {
					lv_resource_new.onRefreshComplete();
				} else {
					lv_resource_hot.onRefreshComplete();
				}
				ToastUtil.showShortToast(getApplicationContext(), jsonResult.getMessage());
			}
		}
	}

	// //////////////////// tab search //////////////////////////

	private FrameLayout fl_search_type;
	private ImageView iv_search_slideround;
	private ImageView iv_search_searchicon;
	private CustomListView lv_search_resource;
	private CustomListView lv_search_course;
	private RelativeLayout rl_search_loading;
	private EditText et_search_key;

	private Animation leftToRightAnimation;
	private Animation rightToLeftAnimation;

	private boolean isResource = true;
	private int currentPage = 1;
	private List<Resource> listResourceSearch;

	private ListViewResourceAdapter listViewAdapterResourceSearch;

	// init tab search UI
	private void initTabSearchUI() {
		fl_search_type = (FrameLayout) tab_search.findViewById(R.id.fl_search_type);
		iv_search_searchicon = (ImageView) tab_search.findViewById(R.id.iv_search_searchicon);
		iv_search_slideround = (ImageView) tab_search.findViewById(R.id.iv_search_slideround);
		lv_search_course = (CustomListView) tab_search.findViewById(R.id.lv_search_course);
		lv_search_resource = (CustomListView) tab_search.findViewById(R.id.lv_search_resource);
		rl_search_loading = (RelativeLayout) tab_search.findViewById(R.id.rl_search_loading);
		et_search_key = (EditText) tab_search.findViewById(R.id.et_search_key);

		leftToRightAnimation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, -0.6f,
				Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
		leftToRightAnimation.setInterpolator(new LinearInterpolator());
		leftToRightAnimation.setDuration(200);
		leftToRightAnimation.setFillAfter(true);

		rightToLeftAnimation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, -0.6f, Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
		rightToLeftAnimation.setInterpolator(new LinearInterpolator());
		rightToLeftAnimation.setDuration(200);
		rightToLeftAnimation.setFillAfter(true);

		fl_search_type.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (isResource) {
					fl_search_type.setBackgroundResource(R.drawable.slidebtn_course);
					isResource = false;
					iv_search_slideround.clearAnimation();
					iv_search_slideround.startAnimation(leftToRightAnimation);
				} else {
					fl_search_type.setBackgroundResource(R.drawable.slidebtn_resource);
					isResource = true;
					iv_search_slideround.clearAnimation();
					iv_search_slideround.startAnimation(rightToLeftAnimation);
				}
			}
		});

		iv_search_searchicon.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (et_search_key.getText().toString().equalsIgnoreCase("")) {
					ToastUtil.showShortToast(getApplicationContext(), "请输入关键词");
					return;
				}
				inputMethodManager.hideSoftInputFromWindow(et_search_key.getWindowToken(), 0);// hide input method
				new SearchTask().execute(et_search_key.getText().toString());
			}
		});

		//

	}

	// search task
	class SearchTask extends AsyncTask<String, Void, JSONResult> {

		@Override
		protected void onCancelled() {
			super.onCancelled();
		}

		@Override
		protected void onPreExecute() {
			System.out.println("onPreExecute");
			lv_search_course.setVisibility(View.GONE);
			lv_search_resource.setVisibility(View.GONE);
			rl_search_loading.setVisibility(View.VISIBLE);
		}

		@Override
		protected JSONResult doInBackground(String... params) {
			System.out.println("doInBackground");
			System.out.println(params[0]);
			// search?action=search&repository=&searchType=&catalog=&keyword=&count=&page=
			MLParameter[] mlParameters = new MLParameter[] { new MLParameter("action", CommonUtil.getValue("action_search")),
					new MLParameter("repository", CommonUtil.getValue("repository_des")),
					new MLParameter("searchType", isResource ? CommonUtil.SEARCH_TYPE_RESOURCE : CommonUtil.SEARCH_TYPE_COURSE),
					new MLParameter("keyword", params[0]), new MLParameter("page", currentPage),
					new MLParameter("count", CommonUtil.SEARCH_DEFAULT_COUNT) };
			return new SearchService().search(mlParameters);
		}

		@Override
		protected void onProgressUpdate(Void... values) {
			super.onProgressUpdate(values);
		}

		@Override
		protected void onPostExecute(JSONResult jsonResult) {
			System.out.println("onPostExecute");
			rl_search_loading.setVisibility(View.GONE);
			if (jsonResult.getCode() == CommonUtil.RESULT_CODE_SUCCEED) {
				if (sharedPreferences.getBoolean("setting_sound", true)) {
					musicPlayer.start();
				}
				try {
					if (isResource) {// resource
						ResourceWrapper resourceWrapper = ResourceWrapper.constructResourceWrapper(jsonResult.getContent());
						lv_search_resource.setVisibility(View.VISIBLE);
						listResourceSearch = resourceWrapper.getResourceList();
						listViewAdapterResourceSearch = new ListViewResourceAdapter(listResourceSearch);
						lv_search_resource.setAdapter(listViewAdapterResourceSearch);
					} else {// course

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

	// ///////////////////// tab setting ///////////////////////

	private CheckBox cb_settings_sound;
	private Button btn_settings_exit;

	private RelativeLayout rl_setting_sound;
	private RelativeLayout rl_setting_suggestion;
	private RelativeLayout rl_setting_about;
	private RelativeLayout rl_setting_version;
	private RelativeLayout rl_setting_follow;

	private void initTabSettingUI() {
		rl_setting_about = (RelativeLayout) tab_setting.findViewById(R.id.rl_settings_about);
		rl_setting_suggestion = (RelativeLayout) tab_setting.findViewById(R.id.rl_settings_suggestion);
		rl_setting_sound = (RelativeLayout) tab_setting.findViewById(R.id.rl_settings_sound);
		rl_setting_version = (RelativeLayout) tab_setting.findViewById(R.id.rl_settings_version);
		rl_setting_follow = (RelativeLayout) tab_setting.findViewById(R.id.rl_settings_follow);
		cb_settings_sound = (CheckBox) tab_setting.findViewById(R.id.cb_settings_sound);
		btn_settings_exit = (Button) tab_setting.findViewById(R.id.btn_setting_exit);

		if (sharedPreferences.getBoolean("setting_sound", CommonUtil.SETTING_SOUND_ON) == CommonUtil.SETTING_SOUND_ON) {
			cb_settings_sound.setChecked(true);
		}

		cb_settings_sound.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton btn, boolean flag) {
				changeSettingSound(flag);
			}
		});

		// sound
		rl_setting_sound.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				changeSettingSound(cb_settings_sound.isChecked());
			}
		});

		// about
		rl_setting_about.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(HomeActivity.this, AboutActivity.class);
				startActivity(intent);
			}
		});

		// exit
		btn_settings_exit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(HomeActivity.this, DialogSettingsExitActivity.class);
				startActivity(intent);
			}
		});

	}

	// settting sound
	private void changeSettingSound(boolean flag) {
		// int oldValue = Integer.parseInt(CommonUtil.getValue("setting_sound"));
		boolean oldValue = sharedPreferences.getBoolean("setting_sound", CommonUtil.SETTING_SOUND_ON);
		if (oldValue != flag) {
			try {
				// CommonUtil.updateProperties("setting_sound", String.valueOf(newValue));
				sharedPreferences.edit().putBoolean("setting_sound", flag).commit();
			} catch (Exception e) {
				e.printStackTrace();
				ToastUtil.showShortToast(getApplicationContext(), "保存设置失败！");
			}
		}
	}
}
