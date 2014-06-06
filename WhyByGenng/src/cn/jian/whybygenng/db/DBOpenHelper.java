package cn.jian.whybygenng.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBOpenHelper extends SQLiteOpenHelper {
	private static final String DATABASENAME = "why.db"; //数据库名称
	private static final int DATABASEVERSION = 1;//数据库版本
	public DBOpenHelper(Context context) {
		super(context, DATABASENAME, null, DATABASEVERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		//创建为什么表
		db.execSQL("CREATE TABLE tb_why(" +
				"_id integer primary key autoincrement," +
				"type varchar(20)," +
				"question text," +
				"msg text)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}

}
