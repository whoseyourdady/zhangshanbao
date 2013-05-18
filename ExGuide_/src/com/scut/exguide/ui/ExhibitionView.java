package com.scut.exguide.ui;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import android.widget.TextView;

import com.example.exguide.R;
import com.scut.exguide.entity.Exhibition;

import com.scut.exguide.entity.TaskForLogo;
import com.scut.exguide.mulithread.LoaderImageTask;

import com.scut.exguide.utility.Constant;
import com.scut.exguide.utility.MyActivity;
import com.scut.exguide.utility.TaskHandler;

public class ExhibitionView {
	private Activity mActivity;
	private LayoutInflater mInflater;
	private View mLoading;
	private View mView;

	public ExhibitionView(Activity _activity) {
		mActivity = _activity;
		initial();
	}

	// 初始化布局
	public void initial() {
		mInflater = LayoutInflater.from(mActivity);
		mView = mInflater.inflate(R.layout.description_exh, null);
		mLoading = mView.findViewById(R.id._dp_loading);

	}

	public View getView() {
		return mView;
	}

	public void SetUI(Exhibition data) {
		mLoading.setVisibility(View.GONE);
		
		ImageView _imageview = (ImageView) mView
				.findViewById(R.id._dp_exhlogo);
		
		TaskHandler task = new TaskForLogo();
		((TaskForLogo)task).mImageView = _imageview;
		String s = String.valueOf(data.logo_url.toCharArray(), 1, data.logo_url.length() - 1);
		String remotepath = Constant.urlPrefix_getLogo+s;
		((TaskForLogo)task).path = remotepath;
		((TaskForLogo)task).myActivity = (MyActivity) mActivity;
		LoaderImageTask.addTask(task);

		TextView _e_name = (TextView) mView
				.findViewById(R.id._dp_exhname);
		_e_name.setText(data.name_cn);

		TextView _e_hall = (TextView) mView
				.findViewById(R.id._dp_hallname);
		_e_hall.setText(data.hall);

		TextView _e_schedule = (TextView) mView
				.findViewById(R.id._dp_exhsechedule);
		_e_schedule.setText(data.period_start + "至" + data.period_end);

		TextView _e_date = (TextView) mView
				.findViewById(R.id._dp_exhdate);
		_e_date.setText(data.day_start + "至" + data.day_end);

		TextView _e_desc = (TextView) mView
				.findViewById(R.id._dp_description);
		_e_desc.setText(data.description);
		
	}
}
