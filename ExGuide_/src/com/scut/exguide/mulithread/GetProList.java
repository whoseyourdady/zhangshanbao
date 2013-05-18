package com.scut.exguide.mulithread;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import com.scut.exguide.entity.Exhibition;
import com.scut.exguide.entity.Product;
import com.scut.exguide.ui.ProuductListView;
import com.scut.exguide.utility.Constant;

import android.os.AsyncTask;
import android.os.Message;

/*
 * 
 * 获得热门产品列表
 *
 */
public class GetProList extends AsyncTask<String, Integer, ArrayList<Product>> {

	private ProuductListView mProuductListView;
	
	public GetProList(ProuductListView _ProuductListView) {
		mProuductListView = _ProuductListView;
	}
	@Override
	protected ArrayList<Product> doInBackground(String... params) {
		// TODO Auto-generated method stub
		try {
			return getList(params[0]);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	
	
	@Override
	protected void onPostExecute(ArrayList<Product> result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		mProuductListView.setAdapter(result);
		
	}






	/**
	 * 下载回热门产品列表
	 * 
	 * @param romtepath
	 * @return
	 * @throws Exception
	 */
	public ArrayList<Product> getList(String romtepath) throws Exception {
		ArrayList<Product> data = new ArrayList<Product>();

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
					Product pro = new Product(false);
					// 实际数据
					JSONObject entity = innerdata.getJSONObject(i);
					pro.Name = entity.getString("name_cn");	
					
					String st = entity.getString("pic_url");
					String s = String.copyValueOf(
							st.toCharArray(), 1,
							st.length() - 1);
					
					pro.LogoUrl = Constant.urlPrefix_getLogo + s;
					
					data.add(pro);
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return data;
	}

}
