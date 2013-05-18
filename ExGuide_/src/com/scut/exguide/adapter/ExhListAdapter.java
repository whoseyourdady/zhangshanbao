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

	private LayoutInflater mInflater;// Item������
	private ArrayList<Exhibition> mData;// ����Դ
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
			holder.exhdate.setText("��Ļ����");
			holder.exhschedule.setText(mData.get(position).period_end);
			holder.exhstate.setText("�ٰ���");

		} else {
			holder.exhdate.setText("��Ļ����");
			holder.exhschedule.setText(mData.get(position).period_start);
			holder.exhstate.setText("�ﱸ��");
		}
		// holder.exhimage.setBackgroundResource((Integer) mData.get(position)
		// .get("img"));
		holder.exhname.setText((String) mData.get(position).name_cn);

		holder.exhaddress.setText((String) mData.get(position).address);
		
		return convertView;
	}

//	/**
//	 * ��������ͼƬ
//	 * 
//	 * @param url
//	 * @return
//	 * @throws IOException 
//	 * @throws IllegalStateException 
//	 */
//	public Bitmap getImage(String romtepath) throws IllegalStateException, IOException {
//		URL url = new URL(romtepath);// ��ȡ��·��
//		Bitmap bitmap = null;
//		// // httpЭ�����Ӷ���
//		// HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//		// conn.setRequestMethod("GET");// �����ǲ�����д�ģ��꿴API����
//		// conn.setConnectTimeout(6 * 1000);
//		//
//		// Bitmap bitmap = null;
//		// if (conn.getResponseCode() == 200) {
//		// InputStream inputStream = conn.getInputStream();
//		// byte[] data = readStream(inputStream);
//		// File file = new File("smart.jpg");// ��ͼƬ������
//		// bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
//		// FileOutputStream outStream = new FileOutputStream(file);// д������
//		// outStream.write(data);// д��
//		// outStream.close(); // �ر���
//		// }
//		// return bitmap;
//
//		HttpGet httpRequest = new HttpGet(romtepath);
//		// ȡ��HttpClient ����
//		HttpClient httpclient = new DefaultHttpClient();
//
//		// ����httpClient ��ȡ��HttpRestponse
//		HttpResponse httpResponse = httpclient.execute(httpRequest);
//		if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
//			// ȡ�������Ϣ ȡ��HttpEntiy
//			HttpEntity httpEntity = httpResponse.getEntity();
//			// ���һ��������
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
//		byte[] buffer = new byte[1024]; // ������װ
//		int len = -1;
//		while ((len = inStream.read(buffer)) != -1) {
//			outstream.write(buffer, 0, len);
//		}
//		outstream.close();
//		inStream.close();
//		// �ر���һ��Ҫ�ǵá�
//		return outstream.toByteArray();
//
//	}
//
//	/**
//	 * ��ȡͼƬ�ڲ���
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
