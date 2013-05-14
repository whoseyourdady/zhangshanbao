/*
 *获得展会列表的线程
 */
package com.scut.exguide.mulithread;

import java.io.BufferedReader;

import java.io.InputStreamReader;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import com.scut.exguide.adapter.ExhListAdapter;
import com.scut.exguide.entity.Exhibition;
import com.scut.exguide.ui.ExhActivity;
import com.scut.exguide.ui.HomeActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

//第一个是参数，第二个是进度，第三个是结果
public class AsyGetExhList extends
		AsyncTask<String, Integer, ArrayList<Exhibition>> {

	private View loading;
	private ListView mList;
	private Activity mActivity;

	public AsyGetExhList(View _l, ListView _li, Activity _a) {
		loading = _l;
		mList = _li;
		mActivity = _a;

	}

	public AsyGetExhList(Activity _a) {

		mActivity = _a;

	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub

		super.onPreExecute();

	}

	/**
	 * 在homeactivity中将参数写入excute函数中
	 */
	@Override
	protected ArrayList<Exhibition> doInBackground(String... params) {
		// TODO Auto-generated method stub

		ArrayList<Exhibition> data = null;
		try {
			data = getList(params[0]);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return data;
	}

	@Override
	protected void onPostExecute(ArrayList<Exhibition> result) {
		// TODO Auto-generated method stub
		// mList.setAdapter(new ExhListAdapter(mActivity, result));
		//
		// OnItemClickListener listener = new OnItemClickListener() {
		//
		//
		// @Override
		// public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
		// long arg3) {
		// // TODO Auto-generated method stub
		// Intent intent = new Intent();
		// intent.setClass(mActivity, ExhActivity.class);
		// intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		// Bundle bundle = new Bundle();
		// int id = ((HomeActivity) mActivity).GetExhibitonID(arg2);
		// bundle.putInt("id", id);
		// intent.putExtras(bundle);
		// mActivity.startActivity(intent);
		//
		// }
		// };
		// mList.setOnItemClickListener(listener);
		// loading.setVisibility(View.GONE);
		// mList.setVisibility(View.VISIBLE);

		((HomeActivity) mActivity).SetListUI(result);
		super.onPostExecute(result);
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
		// TODO Auto-generated method stub
		super.onProgressUpdate(values);
	}

	/**
	 * 下载回展会列表
	 * 
	 * @param romtepath
	 * @return
	 * @throws Exception
	 */
	public ArrayList<Exhibition> getList(String romtepath) throws Exception {
		ArrayList<Exhibition> data = new ArrayList<Exhibition>();

		HttpGet request = new HttpGet(romtepath);

		/* StringBulder,jdk_1.5新增，用法基本跟StringBuffer一样，但效率要比StringBuffer高得多 */
		StringBuilder sbuilder = new StringBuilder();
		try {

			HttpClient client = new DefaultHttpClient();

			HttpResponse response = client.execute(request);

			if (response.getStatusLine().getStatusCode() == 200) {
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(
								response.getEntity().getContent(), "UTF-8"));
				for (String s = reader.readLine(); s != null; s = reader
						.readLine()) {
					// s=new String(s.getBytes("UNICODE"), "UTF-8");
					sbuilder.append(s);
				}
				// 外层数据
				JSONObject outerdata = new JSONObject(sbuilder.toString());
				JSONArray innerdata = outerdata.getJSONArray("data");
				for (int i = 0; i < innerdata.length(); i++) {
					Exhibition exh = new Exhibition();
					// 实际数据
					JSONObject entity = innerdata.getJSONObject(i);
					
					exh.mID = entity.getInt("id");
					exh.name_cn = entity.getString("name_cn");
					exh.name_en = entity.getString("name_en");
					String url = String.copyValueOf(entity.getString("logo_url")
							.toCharArray(), 1, entity.getString("logo_url")
							.length() - 1);
					exh.logo_url = url;

					if (0 == entity.getInt("is_on_show")) {
						exh.onshow = false;
					} else {
						exh.onshow = true;
					}
					exh.address = entity.getString("address");
					exh.hall = entity.getString("hall");
					exh.period_start = String.copyValueOf(
							entity.getString("period_start").toCharArray(), 0,
							10);
					exh.period_end = String
							.copyValueOf(entity.getString("period_end")
									.toCharArray(), 0, 10);
					exh.day_start = entity.getString("day_start");
					exh.day_end = entity.getString("day_end");

					data.add(exh);
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return data;
	}
}
