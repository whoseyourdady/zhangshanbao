package com.scut.exguide.ui;

import com.example.exguide.R;
import com.scut.exguide.utility.Constant;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.view.Window;
import android.widget.Button;

public class ContactsChooseDialog extends Dialog {

	private Button mPick;
	private Button mAdd;
	private Activity mContext;

	public ContactsChooseDialog(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		mContext = (Activity) context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_contacts);
		mPick = (Button) findViewById(R.id.contactspick);
		mAdd = (Button) findViewById(R.id.contactsadd);

		mPick.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Intent.ACTION_PICK,
						ContactsContract.Contacts.CONTENT_URI);
				mContext.startActivityForResult(intent, Constant.CONTACTS_PICK);
				cancel();
			}
		});

		mAdd.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Intent.ACTION_INSERT,
						ContactsContract.Contacts.CONTENT_URI);
				mContext.startActivityForResult(intent, Constant.CONTACTS_ADD);
				cancel();
			}
		});
	}

}
