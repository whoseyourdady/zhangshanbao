package com.scut.exguide.listener;

import com.example.exguide.R;

import android.app.FragmentTransaction;
import android.app.ActionBar.Tab;
import android.app.Fragment;

public class TabListener implements android.app.ActionBar.TabListener {

	private Fragment mFragment;  

	
	public TabListener (Fragment _Fragment) {

		mFragment = _Fragment;
	}
	
	@Override
	public void onTabReselected(Tab arg0, FragmentTransaction arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTabSelected(Tab arg0, FragmentTransaction arg1) {
		// TODO Auto-generated method stub
		//arg1.add(R.id.fragment_place, mFragment, null); 
		
	}

	@Override
	public void onTabUnselected(Tab arg0, FragmentTransaction arg1) {
		// TODO Auto-generated method stub

	}

}
