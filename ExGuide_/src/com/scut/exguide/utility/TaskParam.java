package com.scut.exguide.utility;

import android.content.res.AssetManager;

public class TaskParam {
	private String filename;//名称
	private AssetManager assetManager;//assets目录管理对象
	private int ItemWidth;

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public AssetManager getAssetManager() {
		return assetManager;
	}

	public void setAssetManager(AssetManager assetManager) {
		this.assetManager = assetManager;
	}

	public int getItemWidth() {
		return ItemWidth;
	}

	public void setItemWidth(int itemWidth) {
		ItemWidth = itemWidth;
	}
}
