/*
 *��ȡ�ٲ����߳�
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

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

//��һ���ǲ������ڶ����ǽ��ȣ��������ǽ��
public class AsyGetPics extends AsyncTask<String, Integer, ArrayList<String>> {

	private Activity mActivity;

	public AsyGetPics(Activity _a) {
		mActivity = _a;
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();

	}

	/**
	 * ��exhactivity�н�����д��excute������
	 */
	@Override
	protected ArrayList<String> doInBackground(String... params) {
		// TODO Auto-generated method stub

		ArrayList<String> data = null;
		try {

			data = getPics(params[0]);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return data;
	}

	@Override
	protected void onPostExecute(ArrayList<String> result) {
		// TODO Auto-generated method stub

		((ExhActivity) mActivity).SetWaterfallUI(result);

		super.onPostExecute(result);
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
		// TODO Auto-generated method stub
		super.onProgressUpdate(values);
	}

	/**
	 * ���ػ��ٲ����ĵ�ַ
	 * 
	 * @param romtepath
	 * @return
	 * @throws Exception
	 */
	public ArrayList<String> getPics(String remotepath) throws Exception {
		ArrayList<String> data = new ArrayList<String>();

		HttpGet request = new HttpGet(remotepath);

		/* StringBulder,jdk_1.5�������÷�������StringBufferһ������Ч��Ҫ��StringBuffer�ߵö� */
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
				// �������
				JSONObject outerdata = new JSONObject(sbuilder.toString());
				JSONArray innerdata = outerdata.getJSONArray("data");
				for (int i = 0; i < innerdata.length(); i++) {

					JSONObject entity = innerdata.getJSONObject(i);
					data.add(entity.getString("file_path"));

				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return data;
	}
}
