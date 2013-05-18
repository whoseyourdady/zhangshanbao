package com.scut.exguide.entity;

import java.util.ArrayList;

import com.scut.exguide.mulithread.GetProduct;

public class Product {
	public String LogoUrl;
	public String Name;
	public ArrayList<KeyValue> mList = null;

	public Product(boolean flag) {
		if(flag) {
			mList = new ArrayList<Product.KeyValue>();
		}
	}

	public class KeyValue {
		public String Key;
		public String Value;
	}
	
	public void addKeyValue(KeyValue kv) {
		mList.add(kv);
	}
	
	public int size() {
		return mList.size() + 1;
	}
}
