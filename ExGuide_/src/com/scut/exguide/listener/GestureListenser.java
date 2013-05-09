package com.scut.exguide.listener;

import android.app.ActionBar;
import android.util.Log;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class GestureListenser implements OnTouchListener, OnGestureListener {

	private ActionBar mActionBar;

	public GestureListenser(ActionBar _mActionBar) {
		mActionBar = _mActionBar;
	}

	@Override
	public boolean onDown(MotionEvent arg0) {
		// TODO Auto-generated method stub
		Log.d("Gesture", "get");
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// TODO Auto-generated method stuby
		Log.d("Gesture", "get");

		final int FLING_MIN_DISTANCE = 100;
		final int FLING_MIN_VELOCITY = 200;
		if (e1.getX() - e2.getX() > FLING_MIN_DISTANCE
				&& Math.abs(velocityX) > FLING_MIN_VELOCITY) {

			// Fling left
			int index = mActionBar.getSelectedNavigationIndex();
			if (index < 2 && index >= 0) {				
				mActionBar.setSelectedNavigationItem(index + 1);
			}

		} else if (e2.getX() - e1.getX() > FLING_MIN_DISTANCE
				&& Math.abs(velocityX) > FLING_MIN_VELOCITY) {

			// Fling right
			int index = mActionBar.getSelectedNavigationIndex();
			if (index <= 2 && index >0 ) {
				mActionBar.setSelectedNavigationItem(index - 1);
			}

		} else if (e2.getY() - e1.getY() > FLING_MIN_DISTANCE
				&& Math.abs(velocityY) > FLING_MIN_VELOCITY) {
			// Fling down

		} else if (e1.getY() - e2.getY() > FLING_MIN_DISTANCE
				&& Math.abs(velocityY) > FLING_MIN_VELOCITY) {
			// Fling up

		}

		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub
		Log.d("Gesture", "get");
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		Log.d("Gesture", "get");
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub
		Log.d("Gesture", "get");
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		Log.d("Gesture", "get");
		return false;
	}

	@Override
	public boolean onTouch(View arg0, MotionEvent arg1) {
		// TODO Auto-generated method stub
		Log.d("Gesture", "get");
		return false;
	}

}
