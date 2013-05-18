package com.scut.exguide.adapter;



import com.example.exguide.R;

import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.widget.ImageView;

public class PicsPageChangeListener implements OnPageChangeListener {

	private ImageView[] mImageViews;// Ð¡Ô²µãµÄview
	
	public PicsPageChangeListener(ImageView[] imageViews ) {
		mImageViews = imageViews;
	}
	
	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageSelected(int arg0) {
		// TODO Auto-generated method stub
		for (int i = 0; i < mImageViews.length; i++) {
			mImageViews[arg0]
					.setBackgroundResource(R.drawable.page_indicator_focused);

			if (arg0 != i) {
				mImageViews[i].setBackgroundResource(R.drawable.page_indicator);
			}
		}
	}

}
