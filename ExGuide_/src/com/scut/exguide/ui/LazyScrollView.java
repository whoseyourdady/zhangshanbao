package com.scut.exguide.ui;

//来自：http://blog.csdn.net/listening_music/article/details/7192629
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;

/**
 * 自定义ScrollView，
 * 
 * @author way
 * 
 */
public class LazyScrollView extends ScrollView {
	// private static final String tag = "LazyScrollView";
	private Handler handler;
	private View view;

	// 三个构造器
	public LazyScrollView(Context context) {
		super(context);
	}

	public LazyScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public LazyScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	// 这个获得总的高度
	public int computeVerticalScrollRange() {
		return super.computeHorizontalScrollRange();
	}

	public int computeVerticalScrollOffset() {
		return super.computeVerticalScrollOffset();
	}

	private void init() {
		this.setOnTouchListener(onTouchListener);
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.what) {
				case 0x001:
					if (view.getMeasuredHeight() <= getScrollY() + getHeight()) {
						if (onScrollListener != null) {
							onScrollListener.onBottom();
						}
					} else if (getScrollY() == 0) {
						if (onScrollListener != null) {
							onScrollListener.onTop();
						}
					} else {
						if (onScrollListener != null) {
							onScrollListener.onScroll();
						}
					}
					break;
				default:
					break;
				}
			}
		};
	}

	// ScrollView的触摸事件
	OnTouchListener onTouchListener = new OnTouchListener() {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				break;
			case MotionEvent.ACTION_UP:
				if (view != null && onScrollListener != null) {
					handler.sendMessageDelayed(handler.obtainMessage(0x001), 200);
				}
				break;
			default:
				break;
			}
			return false;
		}
	};

	/**
	 * 获得参考的View，主要是为了获得它的MeasuredHeight，然后和滚动条的ScrollY+getHeight作比较。
	 */
	public void getView() {
		this.view = getChildAt(0);
		if (view != null) {
			init();
		}
	}

	/**
	 * 定义接口,监听ScrollView是否滚动到低端，
	 * 
	 * @author way
	 * 
	 */
	public interface OnScrollListener {
		void onBottom();

		void onTop();

		void onScroll();
	}

	private OnScrollListener onScrollListener;//接口对象

	public void setOnScrollListener(OnScrollListener onScrollListener) {
		this.onScrollListener = onScrollListener;
	}
}
