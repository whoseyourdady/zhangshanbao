package com.scut.exguide.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.example.exguide.R;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * 这是热门商品列表页的适配器
 * 
 * @author yejianhong
 * 
 */
public class ExhotProductAdapter extends BaseAdapter {

	private LayoutInflater mInflater;// Item的容量
	private List<Map<String, Object>> mData;// 数据源

	public ExhotProductAdapter(Context context, List<Map<String, Object>> _Data) {
		mInflater = LayoutInflater.from(context);
		mData = _Data;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		HotProViewHolder holder = null;
		if (convertView == null) {

			holder = new HotProViewHolder();

			convertView = mInflater.inflate(R.layout.linearlayout_elistitem,
					null);
			holder.exhimage = (ImageView) convertView
					.findViewById(R.id.exhimage);
			holder.exhname = (TextView) convertView.findViewById(R.id.exhname);
			holder.exhschedule = (TextView) convertView
					.findViewById(R.id.exhschedule);
			holder.exhaddress = (TextView) convertView
					.findViewById(R.id.exhaddress);

			convertView.setTag(holder);

		} else {

			holder = (HotProViewHolder) convertView.getTag();
		}

		holder.exhimage.setBackgroundResource((Integer) mData.get(position)
				.get("img"));
		holder.exhname.setText((String) mData.get(position).get("name"));
		holder.exhschedule
				.setText((String) mData.get(position).get("schedule"));
		holder.exhaddress.setText((String) mData.get(position).get("address"));

		return convertView;
	}

	

}
