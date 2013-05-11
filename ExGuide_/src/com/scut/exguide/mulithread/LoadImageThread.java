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
	 * 0是获得缩图图的模式 1是获取瀑布流图片的模式
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
					int width = bitmap.getWidth();// 获取真实宽高
					int height = bitmap.getHeight();
					LayoutParams lp = mView.getLayoutParams();
					lp.height = (height * itemWidth) / width;// 调整高度
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
	 * 下载网上图片
	 * 
	 * @param url
	 * @return
	 * @throws IOException
	 * @throws IllegalStateException
	 */
	public Bitmap getImage(String remotepath) throws IllegalStateException,
			IOException {
		URL url = new URL(remotepath);// 获取到路径
		Bitmap bitmap = null;
		// // http协议连接对象
		// HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		// conn.setRequestMethod("GET");// 这里是不能乱写的，详看API方法
		// conn.setConnectTimeout(6 * 1000);
		//
		// Bitmap bitmap = null;
		// if (conn.getResponseCode() == 200) {
		// InputStream inputStream = conn.getInputStream();
		// byte[] data = readStream(inputStream);
		// File file = new File("smart.jpg");// 给图片起名子
		// bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
		// FileOutputStream outStream = new FileOutputStream(file);// 写出对象
		// outStream.write(data);// 写入
		// outStream.close(); // 关闭流
		// }
		// return bitmap;

		HttpGet httpRequest = new HttpGet(remotepath);
		// 取得HttpClient 对象
		HttpClient httpclient = new DefaultHttpClient();

		// 请求httpClient ，取得HttpRestponse
		HttpResponse httpResponse = httpclient.execute(httpRequest);
		if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			// 取得相关信息 取得HttpEntiy
			HttpEntity httpEntity = httpResponse.getEntity();
			// 获得一个输入流
			InputStream is = httpEntity.getContent();

			bitmap = BitmapFactory.decodeStream(is);
			Log.d("TEST", "下载一张图片");

		}
		return bitmap;

	}

	private byte[] readStream(InputStream inStream) throws IOException {
		// TODO Auto-generated method stub
		ByteArrayOutputStream outstream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024]; // 用数据装
		int len = -1;
		while ((len = inStream.read(buffer)) != -1) {
			outstream.write(buffer, 0, len);
		}
		outstream.close();
		inStream.close();
		// 关闭流一定要记得。
		return outstream.toByteArray();

	}

}
