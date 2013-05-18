package com.scut.exguide.ui;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.example.exguide.R;
import com.scut.exguide.adapter.ExViewHolder;
import com.scut.exguide.adapter.ExhListAdapter;
import com.scut.exguide.adapter.HotProViewHolder;
import com.scut.exguide.entity.Exhibition;
import com.scut.exguide.entity.Product;

import com.scut.exguide.entity.TaskForLogo;
import com.scut.exguide.mulithread.LoaderImageTask;

import com.scut.exguide.utility.Constant;
import com.scut.exguide.utility.MyActivity;
import com.scut.exguide.utility.TaskHandler;

public class ExhListView {

	private Activity mActivity;
	private ListView mListView;
	private LayoutInflater mInflater;
	private View mLoading;
	private View mView;
	private MyAdapter mAdapter;

	public ExhListView(Activity _activity) {
		mActivity = _activity;
		initial();
	}

	// 初始化布局
	public void initial() {
		mInflater = LayoutInflater.from(mActivity);

		mView = mInflater.inflate(R.layout.listview_exhibition, null);
		mListView = (ListView) mView.findViewById(R.id.homelistview);
		mLoading = mView.findViewById(R.id.homeloading);

	}

	public View getView() {
		return mView;
	}

	public void setAdapter(ArrayList<Exhibition> data) {
		mAdapter = new MyAdapter(data);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(listener);
		mLoading.setVisibility(View.GONE);

	}

	public class MyAdapter extends BaseAdapter {

		private ArrayList<Exhibition> mData;// 数据源

		public MyAdapter(ArrayList<Exhibition> _Data) {

			mData = _Data;

		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mData.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ExViewHolder holder = null;
			if (convertView == null) {

				holder = new ExViewHolder();

				convertView = mInflater.inflate(R.layout.item_exhibition, null);
				holder.exhimage = (ImageView) convertView
						.findViewById(R.id.exhimage);
				holder.exhname = (TextView) convertView
						.findViewById(R.id.exhname);

				holder.exhaddress = (TextView) convertView
						.findViewById(R.id.exhaddress);

				holder.exhdate = (TextView) convertView
						.findViewById(R.id.exhdate);
				holder.exhstate = (TextView) convertView
						.findViewById(R.id.exhstate);
				holder.exhschedule = (TextView) convertView
						.findViewById(R.id.exhschedule);
				convertView.setTag(holder);

			} else {

				holder = (ExViewHolder) convertView.getTag();

			}

			TaskHandler task = new TaskForLogo();
			((TaskForLogo) task).path = mData.get(position).logo_url;
			((TaskForLogo) task).myActivity = (MyActivity) mActivity;
			((TaskForLogo) task).mImageView = holder.exhimage;

			LoaderImageTask.addTask(task);

			if (mData.get(position).onshow) {
				holder.exhdate.setText("闭幕日期");
				holder.exhschedule.setText(mData.get(position).period_end);
				holder.exhstate.setText("举办中");

			} else {
				holder.exhdate.setText("开幕日期");
				holder.exhschedule.setText(mData.get(position).period_start);
				holder.exhstate.setText("筹备中");
			}

			holder.exhname.setText((String) mData.get(position).name_cn);

			holder.exhaddress.setText((String) mData.get(position).address);

			return convertView;
		}

		public int getId(int position) {
			Exhibition ex = mData.get(position);
			return ex.mID;
		}

	}

	OnItemClickListener listener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			Intent intent = new Intent();
			intent.setClass(mActivity.getApplicationContext(),
					ExhActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
			Bundle bundle = new Bundle();
			int id = GetExhibitonID(arg2);
			bundle.putInt("id", id);
			intent.putExtras(bundle);
			mActivity.startActivity(intent);

		}
	};

	public int GetExhibitonID(int position) {
		MyAdapter ad = (MyAdapter) mListView.getAdapter();
		return ad.getId(position);
	}
}
