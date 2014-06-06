package cn.jian.whybygenng.db;

import java.util.ArrayList;
import java.util.List;

import cn.jian.whybygenng.bean.WhyBean;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class WhyDBTool {
   private DBOpenHelper dbHelper ;
   public WhyDBTool(Context context){
	   dbHelper = new DBOpenHelper(context);
   }
   
   /**
    * 查询所有
    * @return
    */
   public List<WhyBean> find(){
		List<WhyBean> list=new ArrayList<WhyBean>();
		SQLiteDatabase db= dbHelper.getReadableDatabase();
		Cursor cursor=db.rawQuery("select * from tb_why order by _id desc",null);
		while(cursor.moveToNext()){
			int id=cursor.getInt(cursor.getColumnIndex("_id"));
			String question=cursor.getString(cursor.getColumnIndex("question"));
			String msg = cursor.getString(cursor.getColumnIndex("msg"));
			String type = cursor.getString(cursor.getColumnIndex("type"));
			WhyBean why = new WhyBean();
			why.setId(id);
			why.setMsg(msg);
			why.setQuestion(question);
			why.setType(type);
			list.add(why);
		}
		cursor.close();
		db.close();
		return list;
   }
   
   /**
    * 查询所有  分页
    * @return
    */
   public List<WhyBean> findByPage(int page,String types){
	   List<WhyBean> list=new ArrayList<WhyBean>();
		SQLiteDatabase db= dbHelper.getReadableDatabase();
		Cursor cursor=db.rawQuery("select * from tb_why where type=? order by _id desc limit 10 offset ?",new String[]{types,String.valueOf(page*10-10)});
		while(cursor.moveToNext()){
			int id=cursor.getInt(cursor.getColumnIndex("_id"));
			String question=cursor.getString(cursor.getColumnIndex("question"));
			String msg = cursor.getString(cursor.getColumnIndex("msg"));
			String type = cursor.getString(cursor.getColumnIndex("type"));
			WhyBean why = new WhyBean();
			why.setId(id);
			why.setMsg(msg);
			why.setQuestion(question);
			why.setType(type);
			list.add(why);
		}
		cursor.close();
		db.close();
		return list;
   }
   
   public List<WhyBean> findByType(String types){
		List<WhyBean> list=new ArrayList<WhyBean>();
		SQLiteDatabase db= dbHelper.getReadableDatabase();
		Cursor cursor=db.rawQuery("select * from tb_why where type=? order by _id desc",new String[]{types});
		while(cursor.moveToNext()){
			int id=cursor.getInt(cursor.getColumnIndex("_id"));
			String question=cursor.getString(cursor.getColumnIndex("question"));
			String msg = cursor.getString(cursor.getColumnIndex("msg"));
			String type = cursor.getString(cursor.getColumnIndex("type"));
			WhyBean why = new WhyBean();
			why.setId(id);
			why.setMsg(msg);
			why.setQuestion(question);
			why.setType(type);
			list.add(why);
		}
		cursor.close();
		db.close();
		return list;
  }
   
   
   
   /**
	 * 查询是否已经收藏
	 * @param id 为服务器数据id
	 * @return
	 */
	public boolean isfind(int type_id){
		boolean flag=false;
		SQLiteDatabase db=dbHelper.getReadableDatabase();
		Cursor cursor=db.rawQuery("select * from tb_info where type=?",new String[]{String.valueOf(type_id)});
		if(cursor.getCount()>0){
			flag=true;
		}
		cursor.close();
		db.close();
		return flag;
	}
	
	/**
	 * 插入一条收藏
	 * @param why
	 * @return
	 * @throws SQLException
	 */
	public boolean insert(WhyBean why) throws SQLException{

		SQLiteDatabase db=dbHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("type",why.getType());
		values.put("msg", why.getMsg());
		values.put("question",why.getQuestion());
		long id = db.insert("tb_why", null, values);
		db.close();
		return id != -1 ? true :false ; 
	}
	
	public boolean upload(Info info) throws SQLException{

		SQLiteDatabase db=dbHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("top",info.getTop());
		values.put("tips", info.getTips());
		values.put("type",info.getType());
		values.put("name", info.getName());
		values.put("ms",info.getMs());
		values.put("time", info.getTime());
		values.put("time2", info.getTime2());
		long id = db.update("tb_info", values, " _id=? ", new String[]{String.valueOf(info.getId())});
		System.out.println(id+"-----------up"+"---"+info.getId());
		db.close();
		return id != -1 ? true :false ; 
	}
	
	public boolean deleteById(Integer id){
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		db.delete("tb_why", " _id=? ", new String[]{String.valueOf(id)});
		return id != -1 ? true :false ; 
	}
	
	
	/**
	 * 删除根据id
	 * @param id
	 */
	public void delete(Integer id){
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		db.execSQL("delete from tb_why where _id=?",new Object[]{id});
		db.close();
	}
	
}
