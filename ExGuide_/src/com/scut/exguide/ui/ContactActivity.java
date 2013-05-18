package com.scut.exguide.ui;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.example.exguide.R;
import com.scut.exguide.utility.Constant;
import com.scut.exguide.utility.ToastShow;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Bundle;

import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class ContactActivity extends Activity {

	ImageButton mButton;

	private NfcAdapter mAdapter;

	private NdefMessage mMessage1;
	private NdefMessage mMessage2;

	final static int BUFFER_SIZE = 4096;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contact);
		ToastShow.setActivity(this);

		mAdapter = NfcAdapter.getDefaultAdapter(this);

		mButton = (ImageButton) findViewById(R.id.addcontact);

		mButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				ContactsChooseDialog ccd = new ContactsChooseDialog(
						ContactActivity.this);
				ccd.show();

			}
		});
		// startActivityForResult(intent, requestCode)
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub

		switch (requestCode) {
		case Constant.CONTACTS_PICK: {

			if (null == data)
				return;
			Uri uri = data.getData();

			if (null == uri) {
				return;
			}

			Cursor cursor = getContentResolver().query(uri, null, null, null,
					null);

			cursor.moveToFirst();
			String name = cursor.getString(cursor
					.getColumnIndexOrThrow(Phone.DISPLAY_NAME));
			// cursor.getString(cursor.getColumnIndexOrThrow(Phone.PHOTO_ID));
			ToastShow.Show(name);
			
			// Intent intent = new Intent(Intent.ACTION_DEFAULT,uri);
			// startActivity(intent);
		}
			break;

		case Constant.CONTACTS_ADD: {
			ToastShow.Show("我回来了");
			if (null == data)
				return;
			Uri uri = data.getData();
			if (null == uri) {
				return;
			}
			ToastShow.Show(uri.getPath());
		}
			break;

		default:
			break;
		}

		super.onActivityResult(requestCode, resultCode, data);
	}

	public static InputStream StringTOInputStream(String in) throws Exception {

		ByteArrayInputStream is = new ByteArrayInputStream(
				in.getBytes("ISO-8859-1"));
		return is;
	}

	/**
	 * 将InputStream转换成byte数组
	 * 
	 * @param in
	 *            InputStream
	 * @return byte[]
	 * @throws IOException
	 */
	public static byte[] InputStreamTOByte(InputStream in) throws IOException {

		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] data = new byte[BUFFER_SIZE];
		int count = -1;
		while ((count = in.read(data, 0, BUFFER_SIZE)) != -1)
			outStream.write(data, 0, count);

		data = null;
		return outStream.toByteArray();
	}

}
