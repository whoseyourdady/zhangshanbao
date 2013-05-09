package com.scut.exguide.utility;

import java.lang.ref.WeakReference;

import com.example.exguide.R;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;

/**
 * 异步任务获取assets目录下images下的图片资源
 * 
 * @author way
 * 
 */
public class ImageLoaderTask extends AsyncTask<TaskParam, Void, Bitmap> {

	private TaskParam param;
	private final WeakReference<View> imageViewReference; // 防止内存溢出

	public ImageLoaderTask(View _view) {
		// "弱引用，即在引用对象的同时仍然允许对该对象进行垃圾回收。可有效防止内存溢出
		imageViewReference = new WeakReference<View>(_view);

	}

	@Override
	protected Bitmap doInBackground(TaskParam... params) {

		param = params[0];
		return loadImageFile(param.getFilename(), param.getAssetManager());
	}

	// 从assets中获取图片并返回Bitmap对象
	private Bitmap loadImageFile(final String filename,
			final AssetManager manager) {
		try {

			Bitmap bmp = BitmapCache.getInstance().getBitmap(filename,
					param.getAssetManager());
			return bmp;
		} catch (Exception e) {
			Log.e(this.getClass().getSimpleName(), "fetchDrawable failed", e);
		}
		return null;
	}

	@Override
	protected void onPostExecute(Bitmap bitmap) {
		if (isCancelled()) {
			bitmap = null;
		}

		if (imageViewReference != null) {
			View View = imageViewReference.get();
			if (View != null) {
				if (bitmap != null) {
					int width = bitmap.getWidth();// 获取真实宽高
					int height = bitmap.getHeight();
					LayoutParams lp = View.getLayoutParams();
					lp.height = (height * param.getItemWidth()) / width;// 调整高度
					View.setLayoutParams(lp);
					ImageView imageview = (ImageView) View
							.findViewById(R.id.waterfall_image);
					View _view = (View) View
							.findViewById(R.id.waterloading);
					_view.setVisibility(View.GONE);
					imageview.setImageBitmap(bitmap);
				}
			}
		}
	}
}