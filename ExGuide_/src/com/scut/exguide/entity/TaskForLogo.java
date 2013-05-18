package com.scut.exguide.entity;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import com.scut.exguide.utility.MyActivity;
import com.scut.exguide.utility.TaskHandler;

public class TaskForLogo implements TaskHandler {
	public String path;
	public MyActivity myActivity;
	public ImageView mImageView;
	public final int Tag = 1;

	@Override
	public void Update(Object... param) {
		// TODO Auto-generated method stub
		Bitmap bm = (Bitmap) param[0];
		mImageView.setImageBitmap(bm);
	}

	@Override
	public View getView() {
		// TODO Auto-generated method stub
		return mImageView;
	}

	@Override
	public MyActivity getActivity() {
		// TODO Auto-generated method stub
		return myActivity;
	}

	@Override
	public String getPath() {
		// TODO Auto-generated method stub
		return path;
	}

	@Override
	public int getTag() {
		// TODO Auto-generated method stub
		return Tag;
	}

	@Override
	public int getSize() {
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public String getPath(int position) {
		// TODO Auto-generated method stub
		return null;
	}

}
