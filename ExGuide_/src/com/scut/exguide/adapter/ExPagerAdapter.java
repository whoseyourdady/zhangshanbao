package com.scut.exguide.adapter;

import java.util.ArrayList;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

/**
 * 这是展览会页的viewpager
 * 
 * @author yejianhong
 * 
 */
public class ExPagerAdapter extends PagerAdapter {

	private ArrayList<View> mView;
	private ArrayList<String> mTitle;

	public ExPagerAdapter(ArrayList<View> _List, ArrayList<String> _Title) {
		mView = _List;
		mTitle = _Title;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mView.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		// TODO Auto-generated method stub
		return arg0 == arg1;
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		// TODO Auto-generated method stub
		container.addView(mView.get(position));		
		return mView.get(position);
		
	}

	@Override
	public CharSequence getPageTitle(int position) {
		// TODO Auto-generated method stub
		return mTitle.get(position);
	}
	
	

}
