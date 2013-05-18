package com.scut.exguide.utility;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

public class DBUtility {

	private static SQLiteDatabase mDb;

	public static void setDb(SQLiteDatabase db) {
		mDb = db;
	}

	public static void CreateDB() {
		String create = " CREATE TABLE IF NOT EXISTS  contact   (id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR, " +
				"phone VARCHAR, home_num VARCHAR, company_add VARCHAR, company_nam VARCHAR, " +
				"qq VARCHAR, rec_tag VARCHAR, image BLOB) ";
		mDb.execSQL(create);
	}
	
	public static int insertContact(ContentValues values) {
		return 0;
	}

}
