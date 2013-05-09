package com.scut.exguide.listener;

import android.app.Activity;
import android.os.Message;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.scut.exguide.ui.HomeActivity;
import com.scut.exguide.utility.Constant;
import com.scut.exguide.utility.Location;

public class MyLocationListener implements BDLocationListener {

	private Activity mActivity;

	public MyLocationListener(Activity _activity) {
		mActivity = _activity;
	}

	/**
	 * BDLocationListener接口有2个方法需要实现： 1.接收异步返回的定位结果，参数是BDLocation类型参数。
	 * 2.接收异步返回的POI查询结果，参数是BDLocation类型参数。
	 */
	@Override
	public void onReceiveLocation(BDLocation location) {
		// TODO Auto-generated method stub
		if (location == null)
			return;
		StringBuffer sb = new StringBuffer(256);
		sb.append("time : ");
		sb.append(location.getTime());
		sb.append("\nerror code : ");
		sb.append(location.getLocType());
		sb.append("\nlatitude : ");
		sb.append(location.getLatitude());
		sb.append("\nlontitude : ");
		sb.append(location.getLongitude());
		sb.append("\nradius : ");
		sb.append(location.getRadius());
		if (location.getLocType() == BDLocation.TypeGpsLocation) {
			sb.append("\nspeed : ");
			sb.append(location.getSpeed());
			sb.append("\nsatellite : ");
			sb.append(location.getSatelliteNumber());
		} else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
			sb.append("\naddr : ");
			sb.append(location.getAddrStr());

			Message msg = new Message();
			msg.what = Constant.SetLoaction;
			Location l = new Location(location.getProvince(),
					location.getCity(), location.getDistrict());
			msg.obj = l;
			((HomeActivity) mActivity).mhandler.sendMessage(msg);

		}

		// logMsg(sb.toString());

		Log.d("loca", sb.toString());

	}

	@Override
	public void onReceivePoi(BDLocation poiLocation) {
		// TODO Auto-generated method stub
		if (poiLocation == null) {
			return;
		}
		StringBuffer sb = new StringBuffer(256);
		sb.append("Poi time : ");
		sb.append(poiLocation.getTime());
		sb.append("\nerror code : ");
		sb.append(poiLocation.getLocType());
		sb.append("\nlatitude : ");
		sb.append(poiLocation.getLatitude());
		sb.append("\nlontitude : ");
		sb.append(poiLocation.getLongitude());
		sb.append("\nradius : ");
		sb.append(poiLocation.getRadius());
		if (poiLocation.getLocType() == BDLocation.TypeNetWorkLocation) {
			sb.append("\naddr : ");
			sb.append(poiLocation.getAddrStr());
		}
		if (poiLocation.hasPoi()) {
			sb.append("\nPoi:");
			sb.append(poiLocation.getPoi());
		} else {
			sb.append("noPoi information");
		}

		// logMsg(sb.toString());
		Log.d("loca", sb.toString());
	}

}
