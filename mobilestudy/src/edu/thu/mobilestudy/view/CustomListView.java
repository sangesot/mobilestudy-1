package edu.thu.mobilestudy.view;

import java.util.Date;

import edu.thu.mobilestudy.activity.R;
import edu.thu.mobilestudy.util.MLUtil;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * resource listview
 * 
 * @author hjw
 * 
 */
public class CustomListView extends ListView implements OnScrollListener {

	// refresh listener
	public interface OnRefreshListener {
		public void onRefresh();
	}

	private final static int RATIO = 3;// hjw: related to how long can be pulled!!!

	// states
	private final static int RELEASE_To_REFRESH = 0;// release refresh
	private final static int PULL_To_REFRESH = 1;// pull down refresh
	private final static int REFRESHING = 2;// refreshing
	private final static int DONE = 3;// complete refreshing
	private final static int LOADING = 4;

	private RotateAnimation animation;
	private RotateAnimation reverseAnimation;
	private LayoutInflater inflater;
	private ImageView iv_listview_arrow;
	private LinearLayout item_listview;
	private TextView tv_listview_tip;
	private TextView tv_listview_update;
	private ProgressBar pb_listview_item;
	private OnRefreshListener refreshListener;
	private boolean isBack;// isBack = true,means there is no need to refresh! head view can be back
	private boolean isRecored;// whether starty is already recorded
	private boolean isRefreshable;// whether the listview can be freshed or not
	private int firstItemIndex;
	private int headContentHeight;
	private int startY;
	private int state;

	public CustomListView(Context context) {
		super(context);
		init(context);
	}

	public CustomListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	// change view by current state
	private void changeHeaderViewByState() {
		switch (state) {
		case RELEASE_To_REFRESH:
			iv_listview_arrow.setVisibility(View.VISIBLE);
			pb_listview_item.setVisibility(View.GONE);
			tv_listview_tip.setVisibility(View.VISIBLE);
			tv_listview_update.setVisibility(View.VISIBLE);
			iv_listview_arrow.clearAnimation();
			iv_listview_arrow.startAnimation(animation);
			tv_listview_tip.setText("释放刷新");
			break;
		case PULL_To_REFRESH:
			pb_listview_item.setVisibility(View.GONE);
			tv_listview_tip.setVisibility(View.VISIBLE);
			tv_listview_update.setVisibility(View.VISIBLE);
			iv_listview_arrow.clearAnimation();
			iv_listview_arrow.setVisibility(View.VISIBLE);
			if (isBack) {
				isBack = false;
				iv_listview_arrow.clearAnimation();
				iv_listview_arrow.startAnimation(reverseAnimation);
			}
			break;
		case REFRESHING:
			item_listview.setPadding(0, 0, 0, 0);
			pb_listview_item.setVisibility(View.VISIBLE);
			iv_listview_arrow.clearAnimation();
			iv_listview_arrow.setVisibility(View.GONE);
			tv_listview_tip.setText("正在刷新...");
			tv_listview_update.setVisibility(View.VISIBLE);
			break;
		case DONE:
			item_listview.setPadding(0, -1 * headContentHeight, 0, 0);// hjw
			pb_listview_item.setVisibility(View.GONE);
			iv_listview_arrow.clearAnimation();
			iv_listview_arrow.setImageResource(R.drawable.arrow_down);
			tv_listview_tip.setText("刷新完成");
			tv_listview_update.setVisibility(View.VISIBLE);
			break;
		}
	}

	private void init(Context context) {
		// setCacheColorHint(context.getResources().getColor(R.color.transparent));//hjw : xml do this
		inflater = LayoutInflater.from(context);
		item_listview = (LinearLayout) inflater.inflate(R.layout.item_listview_refresh, null);
		iv_listview_arrow = (ImageView) item_listview.findViewById(R.id.iv_listview_arrow);
		pb_listview_item = (ProgressBar) item_listview.findViewById(R.id.pb_listview_item);
		tv_listview_tip = (TextView) item_listview.findViewById(R.id.tv_listview_tip);
		tv_listview_update = (TextView) item_listview.findViewById(R.id.tv_listview_update);

		measureView(item_listview);
		headContentHeight = item_listview.getMeasuredHeight();
		item_listview.setPadding(0, -1 * 1000, 0, 0);// hjw
		// headView.setPadding(0, -1 * paddingHeight, 0, 0);// hjw
		item_listview.invalidate();
		addHeaderView(item_listview, null, false);
		setOnScrollListener(this);

		animation = new RotateAnimation(0, -180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		animation.setInterpolator(new LinearInterpolator());
		animation.setDuration(250);
		animation.setFillAfter(true);

		reverseAnimation = new RotateAnimation(-180, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		reverseAnimation.setInterpolator(new LinearInterpolator());
		reverseAnimation.setDuration(200);
		reverseAnimation.setFillAfter(true);

		state = DONE;
		isRefreshable = false;
	}

	// Tip: Use View.isInEditMode() in your custom views to skip code when shown in Eclipse
	@Override
	public boolean isInEditMode() {// hjw :no use
		return true;
	}

	// hjw: measure head view --> I do not understand! // for hide this!
	private void measureView(View child) {
		ViewGroup.LayoutParams p = child.getLayoutParams();
		if (p == null) {
			p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		}
		int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
		int lpHeight = p.height;
		int childHeightSpec;
		if (lpHeight > 0) {
			childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight, MeasureSpec.EXACTLY);
		} else {// this is important ! here lpHight <0
			childHeightSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
		}
		child.measure(childWidthSpec, childHeightSpec);
	}

	private void onRefresh() {
		if (refreshListener != null) {
			refreshListener.onRefresh();// call the listview on refresh method ,to load new datas
		}
	}

	public void onRefreshComplete() {
		state = DONE;
		tv_listview_update.setText("上次更新：" + MLUtil.formatFullDate(new Date()));
		changeHeaderViewByState();
	}

	public void onScroll(AbsListView arg0, int firstVisiableItem, int arg2, int arg3) {
		firstItemIndex = firstVisiableItem;
	}

	public void onScrollStateChanged(AbsListView arg0, int arg1) {
	}

	public boolean onTouchEvent(MotionEvent event) {
		if (isRefreshable) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				if ((firstItemIndex == 0) && !isRecored) {
					isRecored = true;
					startY = (int) event.getY();
				}
				break;
			case MotionEvent.ACTION_UP:
				if ((state != REFRESHING) && (state != LOADING)) {
					if (state == DONE) {
					}
					if (state == PULL_To_REFRESH) {
						state = DONE;
						changeHeaderViewByState();
					}
					if (state == RELEASE_To_REFRESH) {
						state = REFRESHING;
						changeHeaderViewByState();
						onRefresh();
					}
				}
				isRecored = false;
				isBack = false;
				break;
			case MotionEvent.ACTION_MOVE:
				int tempY = (int) event.getY();
				if (!isRecored && (firstItemIndex == 0)) {
					isRecored = true;
					startY = tempY;
				}
				if ((state != REFRESHING) && isRecored && (state != LOADING)) {
					if (state == RELEASE_To_REFRESH) {
						setSelection(0);
						if ((((tempY - startY) / RATIO) < headContentHeight) && ((tempY - startY) > 0)) {// hjw
							state = PULL_To_REFRESH;
							changeHeaderViewByState();
						} else if ((tempY - startY) <= 0) {
							state = DONE;
							changeHeaderViewByState();
						}
					}
					if (state == PULL_To_REFRESH) {
						setSelection(0);
						if (((tempY - startY) / RATIO) >= headContentHeight) {// hjw
							state = RELEASE_To_REFRESH;
							isBack = true;
							changeHeaderViewByState();
						} else if ((tempY - startY) <= 0) {
							state = DONE;
							changeHeaderViewByState();
						}
					}
					if (state == DONE) {
						if ((tempY - startY) > 0) {
							state = PULL_To_REFRESH;
							changeHeaderViewByState();
						}
					}
					if (state == PULL_To_REFRESH) {
						item_listview.setPadding(0, (-1 * headContentHeight) + ((tempY - startY) / RATIO), 0, 0);// hjw
					}
					if (state == RELEASE_To_REFRESH) {
						item_listview.setPadding(0, ((tempY - startY) / RATIO) - headContentHeight, 0, 0);// hjw
					}
				}
				break;
			}
		}
		return super.onTouchEvent(event);
	}

	public void setAdapter(BaseAdapter adapter) {
		tv_listview_update.setText("上次刷新:" + MLUtil.formatFullDate(new Date()));
		super.setAdapter(adapter);
	}

	public void setOnRefreshListener(OnRefreshListener refreshListener) {
		this.refreshListener = refreshListener;
		isRefreshable = true;
	}
}