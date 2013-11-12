package com.util;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.fragment.HomeFragment;
import com.weibo.sdk.android.net.RequestListener;

public class Parse extends Thread{
	
	private RequestListener requestListener;
	private String arg0;
	FragmentActivity fragmentActivity;
	
	private String[] image_urls, screen_name, statuses_text, statuses_id, retweeted_statuses, reposts_comments, 
	pic_url1, pic_url2, bmiddle_pic, retweeted_status_bmiddle_pic ;
	
	private String user ,text , reposts_count , comments_count , retweeted_status , retweeted_status_user;
	
	private Bundle bundle;
	private Message message;
	DBOpenHelper dboHelper;
	//String image_urls, statuses_id, screen_name, user, text, reposts_count, comments_count, reposts_comments, pic_url1, bmiddle_pic, pic_url2, retweeted_status_bmiddle_pic;
	
	public Parse(RequestListener requestListener, String arg0, FragmentActivity fragmentActivity){
		
		this.requestListener = requestListener;
		this.arg0 = arg0;
		this.fragmentActivity = fragmentActivity;
		
		initArray();
		dboHelper = new DBOpenHelper(fragmentActivity);
	}
	
	private void initArray() {
		// TODO Auto-generated method stub
		image_urls = new String[50];
		screen_name = new String[50];
		statuses_text = new String[50];
		statuses_id = new String[50];
		retweeted_statuses = new String[50];
		pic_url1 = new String[50];
		pic_url2 = new String[50];
		bmiddle_pic = new String[50];
		retweeted_status_bmiddle_pic = new String[50];
		reposts_comments = new String[50];
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		parseJson();
	}
	
	public void parseJson(){
		try{
			JSONObject jsonObject = new JSONObject(arg0);
			JSONArray jsonArray = jsonObject.getJSONArray("statuses");
			JSONObject jobj, jsonOb;
			
			for(int i = 0; i < 50 ; i++ ){
				jsonOb = jsonArray.getJSONObject(i);
				
				statuses_id[i] = jsonOb.getString("id");
				//用户头像
				image_urls[i] = jsonOb.getJSONObject("user").getString("profile_image_url");
				//用户ID
				statuses_id[i] = jsonOb.getString("id");
				//用户名
				screen_name[i] = jsonOb.getJSONObject("user").getString("screen_name");
				//user = jsonOb.getJSONObject("user").getString("screen_name");
				//微博内容
				text = jsonOb.getString("text");
				//转发数
				reposts_count = jsonOb.getString("reposts_count");
				//评论数
				comments_count = jsonOb.getString("comments_count");
				reposts_comments[i] = "转发：" + reposts_count + "     " + "评论：" + comments_count  + "\n";
				
				//是否有转发
				jobj = jsonOb.optJSONObject("retweeted_status");
				
				//是否有配图
				pic_url1[i] = jsonOb.optString("thumbnail_pic");
				//配图大图
				bmiddle_pic[i] = jsonOb.optString("bmiddle_pic");
				
				if(jobj == null){
					//没有转发
					statuses_text[i] = text;
					pic_url2[i] = null;
					retweeted_statuses[i] = null;
					retweeted_status_bmiddle_pic[i] = null;
					
					}else{
						//有转发
						//转发图
						pic_url2[i] = jobj.optString("thumbnail_pic");
						//转发大图
						retweeted_status_bmiddle_pic[i] = jobj.optString("bmiddle_pic");
						
						//被转发微博用户
						retweeted_status_user = jobj.optJSONObject("user").optString("screen_name");
						
						//转发微博内容
						retweeted_status = jobj.getString("text");
						//微博内容
						statuses_text[i] = text;
						retweeted_statuses[i] = "∧" +"\n" + retweeted_status_user + "\n" + retweeted_status + "\n";
						}
				}
			
		    SQLiteDatabase db = null;
		    db = dboHelper.getReadableDatabase();
		    Cursor cursor = db.rawQuery("select * from result",null);
		    
		    if(cursor.getCount()==0){
		    	SQLiteDatabase sqlite = dboHelper.getWritableDatabase();
				ContentValues cValue = new ContentValues();
				for(int i = 0; i < 50; i++){
					cValue.put("id", i);
					cValue.put("statuses_id", statuses_id[i]);
					cValue.put("image_urls", image_urls[i]);
					cValue.put("screen_name", screen_name[i]);
					cValue.put("statuses_text", statuses_text[i]);
					cValue.put("retweeted_statuses", retweeted_statuses[i]);
					cValue.put("pic_url1", pic_url1[i]);
					cValue.put("pic_url2", pic_url2[i]);
					cValue.put("reposts_comments", reposts_comments[i]);
					sqlite.insert("result",null,cValue); 
					}
				sqlite.close();
		    }else{
		    	Log.i("result", "not null");
		    	SQLiteDatabase sqlite = dboHelper.getWritableDatabase();
		    	sqlite.delete("result", null, null);
				ContentValues cValue = new ContentValues();
				for(int i = 0; i < 50; i++){
					cValue.put("id", i);
					cValue.put("statuses_id", statuses_id[i]);
					cValue.put("image_urls", image_urls[i]);
					cValue.put("screen_name", screen_name[i]);
					cValue.put("statuses_text", statuses_text[i]);
					cValue.put("retweeted_statuses", retweeted_statuses[i]);
					cValue.put("pic_url1", pic_url1[i]);
					cValue.put("pic_url2", pic_url2[i]);
					cValue.put("reposts_comments", reposts_comments[i]);
					sqlite.insert("result",null,cValue); 
					}
				sqlite.close();
		    }
		    
		    bundle = new Bundle();
		    bundle.putStringArray("msg1", image_urls);
		    bundle.putStringArray("msg2", screen_name);
		    bundle.putStringArray("msg3", statuses_text);
		    bundle.putStringArray("msg4", retweeted_statuses);
		    bundle.putStringArray("msg5", pic_url1);
		    bundle.putStringArray("msg6", pic_url2);
		    bundle.putStringArray("msg7", reposts_comments);
		    
		    message = new Message();
		    message.setData(bundle);
		    message.what=0;
		    HomeFragment.handler.sendMessage(message);
		    
		    }catch(Exception e) {
		    	// TODO Auto-generated catch block
		    	e.printStackTrace();
		    	}
	}
	
	public boolean tabIsExist(String tabName){
		boolean result = false;
		if(tabName == null){
			return false;
			}
		SQLiteDatabase db = null;
		Cursor cursor = null;
		try {
			db = dboHelper.getReadableDatabase();
            String sql = "select count(*) as c from sqlite_master where type ='table' and name ='"+tabName.trim()+"' ";
            cursor = db.rawQuery(sql, null);
            if(cursor.moveToNext()){
            	int count = cursor.getInt(0);
            	if(count>0){
            		result = true;
            		}
            	}
            } catch (Exception e) {
            	// TODO: handle exception
            	}                
		return result;
		
	}
	
}
