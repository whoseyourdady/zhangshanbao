package com.scut.exguide.mulithread;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.exguide.R;
import com.scut.exguide.utility.BitmapCache;
import com.scut.exguide.utility.Constant;
import com.scut.exguide.utility.TaskParam;

public class LoadImageThread extends Thread {
	private String url;
	private ImageView mImageView;
	private View mView;


	/*
	 * 0�ǻ����ͼͼ��ģʽ 1�ǻ�ȡ�ٲ���ͼƬ��ģʽ
	 */
	private int FLAG = 0;

	private int itemWidth;
	


	public LoadImageThread(ImageView view, String path) {
		mImageView = view;
		url = Constant.urlPrefix_getLogo + path.substring(1);

	}

	public LoadImageThread(View _view, String path, int _falg, int width) {

		url =  path;
		FLAG = _falg;

		mView =  _view;
		itemWidth = width;

	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		try {

			if (0 == FLAG) {
				Bitmap bitmap = getImage(url);
				mImageView.setImageBitmap(bitmap);
			} else if (1 == FLAG) {

				Bitmap bitmap = getImage(url);

				if (bitmap != null) {
					int width = bitmap.getWidth();// ��ȡ��ʵ���
					int height = bitmap.getHeight();
					LayoutParams lp = mView.getLayoutParams();
					lp.height = (height * itemWidth) / width;// �����߶�
					mView.setLayoutParams(lp);
					ImageView imageview = (ImageView) mView
							.findViewById(R.id.waterfall_image);
					View _view = (View) mView.findViewById(R.id.waterloading);
					_view.setVisibility(View.GONE);
					imageview.setImageBitmap(bitmap);
				}
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * ��������ͼƬ
	 * 
	 * @param url
	 * @return
	 * @throws IOException
	 * @throws IllegalStateException
	 */
	public Bitmap getImage(String remotepath) throws IllegalStateException,
			IOException {
		URL url = new URL(remotepath);// ��ȡ��·��
		Bitmap bitmap = null;
		// // httpЭ�����Ӷ���
		// HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		// conn.setRequestMethod("GET");// �����ǲ�����д�ģ��꿴API����
		// conn.setConnectTimeout(6 * 1000);
		//
		// Bitmap bitmap = null;
		// if (conn.getResponseCode() == 200) {
		// InputStream inputStream = conn.getInputStream();
		// byte[] data = readStream(inputStream);
		// File file = new File("smart.jpg");// ��ͼƬ������
		// bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
		// FileOutputStream outStream = new FileOutputStream(file);// д������
		// outStream.write(data);// д��
		// outStream.close(); // �ر���
		// }
		// return bitmap;

		HttpGet httpRequest = new HttpGet(remotepath);
		// ȡ��HttpClient ����
		HttpClient httpclient = new DefaultHttpClient();

		// ����httpClient ��ȡ��HttpRestponse
		HttpResponse httpResponse = httpclient.execute(httpRequest);
		if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			// ȡ�������Ϣ ȡ��HttpEntiy
			HttpEntity httpEntity = httpResponse.getEntity();
			// ���һ��������
			InputStream is = httpEntity.getContent();

			bitmap = BitmapFactory.decodeStream(is);
			Log.d("TEST", "����һ��ͼƬ");

		}
		return bitmap;

	}

	private byte[] readStream(InputStream inStream) throws IOException {
		// TODO Auto-generated method stub
		ByteArrayOutputStream outstream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024]; // ������װ
		int len = -1;
		while ((len = inStream.read(buffer)) != -1) {
			outstream.write(buffer, 0, len);
		}
		outstream.close();
		inStream.close();
		// �ر���һ��Ҫ�ǵá�
		return outstream.toByteArray();

	}

}
