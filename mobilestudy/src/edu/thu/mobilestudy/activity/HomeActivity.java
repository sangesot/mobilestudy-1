package edu.thu.mobilestudy.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * Ö÷½çÃæ
 * 
 * @author hujiawei
 * 
 */
public class HomeActivity extends Activity {

	private LayoutInflater layoutInflater;

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
//	private ImageView iv_home_cloud;

	// private int currentIndex;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_activity_home);

		layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		vp_home_center = (ViewPager) findViewById(R.id.vp_home_center);

		ll_home_resource = (LinearLayout) findViewById(R.id.ll_home_resource);
		ll_home_resource.setOnClickListener(new HomeOnClickListener(0));
		ll_home_search = (LinearLayout) findViewById(R.id.ll_home_search);
		ll_home_search.setOnClickListener(new HomeOnClickListener(1));
		ll_home_download = (LinearLayout) findViewById(R.id.ll_home_download);
		ll_home_download.setOnClickListener(new HomeOnClickListener(2));
		ll_home_setting = (LinearLayout) findViewById(R.id.ll_home_setting);
		ll_home_setting.setOnClickListener(new HomeOnClickListener(3));

		iv_home_resource = (ImageView) findViewById(R.id.iv_home_resource);
		iv_home_search = (ImageView) findViewById(R.id.iv_home_search);
		iv_home_download = (ImageView) findViewById(R.id.iv_home_download);
		iv_home_setting = (ImageView) findViewById(R.id.iv_home_setting);
//		iv_home_cloud = (ImageView) findViewById(R.id.iv_home_cloud);

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
		//set view pager not to slide left or right
		vp_home_center.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				return true;
			}
		});
		vp_home_center.setCurrentItem(0);//default
		iv_home_resource.setImageResource(R.drawable.star_purple);//first tab 
	}

	// page change listener
	public class HomeOnPageChangeListener implements OnPageChangeListener {
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

	// click listener
	public class HomeOnClickListener implements View.OnClickListener {
		private int index = 0;

		public HomeOnClickListener(int i) {
			index = i;
		}

		@Override
		public void onClick(View v) {
			vp_home_center.setCurrentItem(index);
		}
	}

}
