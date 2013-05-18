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
import com.scut.exguide.ui.ExhibitionView;
import com.scut.exguide.ui.HomeActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

//第一个是参数，第二个是进度，第三个是结果
public class GetExhDescription extends
		AsyncTask<String, Integer, Exhibition> {

	private ExhibitionView mExhibitionView;

	public GetExhDescription(ExhibitionView _ExhibitionView) {
		mExhibitionView = _ExhibitionView;
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		
		  
		
		super.onPreExecute();

	}

	/**
	 * 在exhactivity中将参数写入excute函数中
	 */
	@Override
	protected Exhibition doInBackground(String... params) {
		// TODO Auto-generated method stub

		Exhibition data = null;
		try {
			data = getDescripition(params[0]);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return data;
	}

	@Override
	protected void onPostExecute(Exhibition result) {
		// TODO Auto-generated method stub

		mExhibitionView.SetUI(result);

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
	public Exhibition getDescripition(String remotepath) throws Exception {
		Exhibition data = new Exhibition();

		HttpGet request = new HttpGet(remotepath);

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
				JSONObject innerdata = outerdata.getJSONObject("data");
				// for (int i = 0; i < innerdata.length(); i++) {
				Exhibition exh = new Exhibition();
				// 实际数据
				// JSONObject entity = innerdata.getJSONObject(i);
				JSONObject entity = innerdata;
				data.mID = entity.getInt("id");
				data.name_cn = entity.getString("name_cn");
				data.name_en = entity.getString("name_en");
				data.logo_url = entity.getString("logo_url");

				if (0 == entity.getInt("is_on_show")) {
					exh.onshow = false;
				} else {
					exh.onshow = true;
				}
				data.address = entity.getString("address");
				data.hall = entity.getString("hall");
				data.period_start = String.copyValueOf(
						entity.getString("period_start").toCharArray(), 0, 10);
				data.period_end = String.copyValueOf(
						entity.getString("period_end").toCharArray(), 0, 10);
				data.day_start = entity.getString("day_start");
				data.day_end = entity.getString("day_end");
				data.description = entity.getString("description");
				data.mobilephone = entity.getString("mobilephone");
				data.organizer = entity.getString("organizer");
				data.fax = entity.getString("fax");
				data.province = entity.getString("province_name");
				data.city = entity.getString("city_name");				
				data.district = entity.getString("district_name");

				// }

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return data;
	}
}
