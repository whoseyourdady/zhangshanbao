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
import com.scut.exguide.ui.ProductContentView;
import com.scut.exguide.ui.ProuductListView;
import com.scut.exguide.utility.Constant;

import android.os.AsyncTask;
import android.os.Message;

/*
 * 
 * 获得热门产品列表
 *
 */
public class GetProduct extends AsyncTask<String, Integer, Product> {

	private ProductContentView mContentView;
	
	public GetProduct(ProductContentView pcv) {
		mContentView = pcv;
	}
	
	@Override
	protected Product doInBackground(String... params) {
		// TODO Auto-generated method stub
		try {
			return getProduct(params[0]);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	
	
	@Override
	protected void onPostExecute(Product result) {
		// TODO Auto-generated method stub
		mContentView.UpdateListView(result);
		super.onPostExecute(result);
			
	}






	/**
	 * 下载回热门产品列表
	 * 
	 * @param romtepath
	 * @return
	 * @throws Exception
	 */
	public Product getProduct(String romtepath) throws Exception {
		Product data = new Product(true);

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
				data.Name = outerdata.getString("name_cn");
				
				String jsondata = outerdata.getString("jsondata");
				JSONObject innerdata = new JSONObject(jsondata);
				JSONArray values = innerdata.getJSONArray("values");
				
				for (int i = 0; i < values.length(); i++) {					
					// 实际数据
					JSONObject entity = values.getJSONObject(i);
					Product.KeyValue kv = data.new KeyValue();
					
					kv.Key = entity.getString("name");	
					kv.Value = entity.getString("value");	
					
					data.addKeyValue(kv);				
					
					
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return data;
	}

}
