package com.scut.exguide.adapter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.example.exguide.R;
import com.scut.exguide.entity.Exhibition;
import com.scut.exguide.entity.TaskForLogo;

import com.scut.exguide.mulithread.LoaderImageTask;

import com.scut.exguide.ui.HomeActivity;
import com.scut.exguide.utility.Constant;
import com.scut.exguide.utility.MyActivity;
import com.scut.exguide.utility.TaskHandler;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ExhListAdapter extends BaseAdapter  {

	private LayoutInflater mInflater;// Item的容量
	private ArrayList<Exhibition> mData;// 数据源
	public Activity mActivity;
	
	public ExhListAdapter(Activity _Activity, ArrayList<Exhibition> _Data) {
		mInflater = LayoutInflater.from(_Activity);
		mData = _Data;
		mActivity = _Activity;
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

			convertView = mInflater.inflate(R.layout.item_exhibition,
					null);
			holder.exhimage = (ImageView) convertView
					.findViewById(R.id.exhimage);
			holder.exhname = (TextView) convertView.findViewById(R.id.exhname);

			holder.exhaddress = (TextView) convertView
					.findViewById(R.id.exhaddress);

			holder.exhdate = (TextView) convertView.findViewById(R.id.exhdate);
			holder.exhstate = (TextView) convertView
					.findViewById(R.id.exhstate);
			holder.exhschedule = (TextView) convertView
					.findViewById(R.id.exhschedule);
			convertView.setTag(holder);

		} else {

			holder = (ExViewHolder) convertView.getTag();
			
		}

		TaskHandler task = new TaskForLogo();
		String path = Constant.urlPrefix_getLogo+mData.get(position).logo_url;
		((TaskForLogo)task).path = path;
		((TaskForLogo)task).myActivity = (MyActivity) mActivity;
		((TaskForLogo)task).mImageView = holder.exhimage;
		
		LoaderImageTask.addTask(task);
		
		
//		LoadImageThread t = new LoadImageThread(holder.exhimage,
//				mData.get(position).logo_url);
//		t.run();

		if (mData.get(position).onshow) {
			holder.exhdate.setText("闭幕日期");
			holder.exhschedule.setText(mData.get(position).period_end);
			holder.exhstate.setText("举办中");

		} else {
			holder.exhdate.setText("开幕日期");
			holder.exhschedule.setText(mData.get(position).period_start);
			holder.exhstate.setText("筹备中");
		}
		// holder.exhimage.setBackgroundResource((Integer) mData.get(position)
		// .get("img"));
		holder.exhname.setText((String) mData.get(position).name_cn);

		holder.exhaddress.setText((String) mData.get(position).address);
		
		return convertView;
	}

//	/**
//	 * 下载网上图片
//	 * 
//	 * @param url
//	 * @return
//	 * @throws IOException 
//	 * @throws IllegalStateException 
//	 */
//	public Bitmap getImage(String romtepath) throws IllegalStateException, IOException {
//		URL url = new URL(romtepath);// 获取到路径
//		Bitmap bitmap = null;
//		// // http协议连接对象
//		// HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//		// conn.setRequestMethod("GET");// 这里是不能乱写的，详看API方法
//		// conn.setConnectTimeout(6 * 1000);
//		//
//		// Bitmap bitmap = null;
//		// if (conn.getResponseCode() == 200) {
//		// InputStream inputStream = conn.getInputStream();
//		// byte[] data = readStream(inputStream);
//		// File file = new File("smart.jpg");// 给图片起名子
//		// bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
//		// FileOutputStream outStream = new FileOutputStream(file);// 写出对象
//		// outStream.write(data);// 写入
//		// outStream.close(); // 关闭流
//		// }
//		// return bitmap;
//
//		HttpGet httpRequest = new HttpGet(romtepath);
//		// 取得HttpClient 对象
//		HttpClient httpclient = new DefaultHttpClient();
//
//		// 请求httpClient ，取得HttpRestponse
//		HttpResponse httpResponse = httpclient.execute(httpRequest);
//		if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
//			// 取得相关信息 取得HttpEntiy
//			HttpEntity httpEntity = httpResponse.getEntity();
//			// 获得一个输入流
//			InputStream is = httpEntity.getContent();
//
//			bitmap = BitmapFactory.decodeStream(is);
//
//		}
//		return bitmap;
//
//	}
//
//	private byte[] readStream(InputStream inStream) throws IOException {
//		// TODO Auto-generated method stub
//		ByteArrayOutputStream outstream = new ByteArrayOutputStream();
//		byte[] buffer = new byte[1024]; // 用数据装
//		int len = -1;
//		while ((len = inStream.read(buffer)) != -1) {
//			outstream.write(buffer, 0, len);
//		}
//		outstream.close();
//		inStream.close();
//		// 关闭流一定要记得。
//		return outstream.toByteArray();
//
//	}
//
//	/**
//	 * 获取图片内部类
//	 */
//	class LoadImageThread extends Thread {
//
//		private String url;
//		private ImageView mView;
//
//		public LoadImageThread(ImageView view, String path) {
//			mView = view;
//			url = Constant.urlPrefix_getLogo + path.substring(1);
//
//		}
//
//		@Override
//		public void run() {
//			// TODO Auto-generated method stub
//			super.run();
//			try {
//				Bitmap bitmap = getImage(url);
//				mView.setImageBitmap(bitmap);
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//
//		}
//
//	}
	
	public int getId(int position) {
		Exhibition ex = mData.get(position);
		return ex.mID;
	}

}
