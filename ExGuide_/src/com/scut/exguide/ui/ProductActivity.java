package com.scut.exguide.ui;

import java.io.IOException;

import com.example.exguide.R;
import com.scut.exguide.mulithread.GetProPics;
import com.scut.exguide.mulithread.GetProduct;
import com.scut.exguide.mulithread.LoaderImageTask;
import com.scut.exguide.utility.Constant;
import com.scut.exguide.utility.MyActivity;
import com.scut.exguide.utility.TaskHandler;
import com.scut.exguide.utility.TransistionUtil;

import android.app.Activity;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.NfcA;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

public class ProductActivity extends Activity implements MyActivity {

	private ImageButton mComment;
	private ImageButton mShare;
	private ImageButton mMore;

	private ProductContentView mProuductListView;

	private View mLoading;
	private View top;
	private View bottom;

	private int mID;
	
	public LoaderImageTask LoaderImage1 = new LoaderImageTask();
	public LoaderImageTask LoaderImage2 = new LoaderImageTask();
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_product);
		LoaderImage1.start();
		LoaderImage2.start();
		initialUI();

		// Intent intent = getIntent();
		// Bundle bundle = intent.getExtras();
		// mID = bundle.getInt("id");

		//GetProduct gp = new GetProduct(mProuductListView);
		//gp.execute("http://0.myexguide.duapp.com/exguide/index.php?s=Home/Webservice/getExhibit/id/4");

		GetProPics gpp = new GetProPics(mProuductListView, this);
		gpp.execute("http://0.myexguide.duapp.com/exguide/index.php?s=Home/Webservice/getExhibitPics/id/5");
		//gpp.execute(Constant.urlPrefix_getPics+1);
		
		resolveIntent(getIntent());
		
	}

	// 初始化组件
	private void initialUI() {
		top = findViewById(R.id.top_view_product);
		mLoading = findViewById(R.id.product_loading);
		bottom = findViewById(R.id.bottom_view_product);

		mProuductListView = new ProductContentView(top, this);

		mComment = (ImageButton) bottom.findViewById(R.id.comment);
		mShare = (ImageButton) bottom.findViewById(R.id.share);
		mMore = (ImageButton) bottom.findViewById(R.id.more);

	}

	public Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case Constant.UpdateUI:
				TaskHandler task = (TaskHandler) msg.obj;
				task.Update(LayoutInflater.from(ProductActivity.this), ProductActivity.this);
				break;

			default:
				break;
			}
		}

	};

	@Override
	public String getTag() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void Update(Object... param) {
		// TODO Auto-generated method stub
		Message msg = new Message();
		msg.what = Constant.UpdateUI;
		msg.obj = param[0];
		mHandler.sendMessage(msg);
	}
	
	void resolveIntent(Intent intent) {
		// Parse the intent
		String action = intent.getAction();
		if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)) {
			// Get an instance of the TAG from the NfcAdapter
			Tag productTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

			MifareClassic mfc = MifareClassic.get(productTag);
			NfcA nfc = NfcA.get(productTag);
			byte[] a = nfc.getAtqa();
			short b = nfc.getSak();
			try {
				// Conncet to card
				mfc.connect();
				boolean auth = false;
				auth = mfc.authenticateSectorWithKeyA(0,
						MifareClassic.KEY_DEFAULT);

				if (auth) {
					byte[] data = mfc.readBlock(1);
					char[] cData = TransistionUtil.getChars(data);
					Integer tid = Integer
							.parseInt(String.valueOf(cData).trim()); // 注意要去掉空格。。
					
					

				}
			} catch (IOException ex) {
				ex.printStackTrace();
				Toast.makeText(getApplicationContext(), "读卡失败\n请重新刷取卡片",
						Toast.LENGTH_SHORT).show();
			}
		}

	}

}
