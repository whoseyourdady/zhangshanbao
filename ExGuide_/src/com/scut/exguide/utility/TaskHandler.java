package com.scut.exguide.utility;

import android.view.View;

public interface TaskHandler {
	public void Update(Object ...param);
	public View getView();
	public MyActivity getActivity();
	public String getPath();
	public String getPath(int position);
	public int getTag();
	public int getSize();
	
}
