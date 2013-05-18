package com.scut.exguide.entity;

import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;

import com.example.exguide.R;
import com.scut.exguide.utility.MyActivity;
import com.scut.exguide.utility.TaskHandler;

public class TaskForWaterFall implements TaskHandler {

	public String path;
	public MyActivity myActivity;
	public View mView;
	public final int Tag = 2;
	
	@Override
	public void Update(Object... param) {
		// TODO Auto-generated method stub
		Bitmap bm = (Bitmap) param[0];
		int itemWidth = (Integer) param[1];
		
		ImageView iv = (ImageView) mView.findViewById(R.id.waterfall_image);
		View loading = (View) mView.findViewById(R.id.waterloading);
		
		int width = bm.getWidth();// 获取真实宽高
		int height = bm.getHeight();
		LayoutParams lp = mView.getLayoutParams();
		lp.height = (height * itemWidth) / width;// 调整高度
		iv.setLayoutParams(lp);
		
		
		loading.setVisibility(View.GONE);
		iv.setImageBitmap(bm);
		iv.setVisibility(View.VISIBLE);
		
	}

	@Override
	public View getView() {
		// TODO Auto-generated method stub
		return mView;
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
