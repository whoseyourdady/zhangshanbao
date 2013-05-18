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
 * ������Ų�Ʒ�б�
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
	 * ���ػ����Ų�Ʒ�б�
	 * 
	 * @param romtepath
	 * @return
	 * @throws Exception
	 */
	public Product getProduct(String romtepath) throws Exception {
		Product data = new Product(true);

		HttpGet request = new HttpGet(romtepath);

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
				data.Name = outerdata.getString("name_cn");
				
				String jsondata = outerdata.getString("jsondata");
				JSONObject innerdata = new JSONObject(jsondata);
				JSONArray values = innerdata.getJSONArray("values");
				
				for (int i = 0; i < values.length(); i++) {					
					// ʵ������
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
