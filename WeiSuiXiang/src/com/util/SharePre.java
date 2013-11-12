package com.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SharePre {
	static SharedPreferences pre;
	static SharedPreferences.Editor editor;
	
	public static void keepUID(Context context , long uid){
		pre = context.getSharedPreferences("keepUID", Context.MODE_APPEND);
		editor = pre.edit();
		editor.putLong("uid", uid);
		editor.commit();
		}
	
	public static void clear(Context context){
	    pre = context.getSharedPreferences("keepUID", Context.MODE_APPEND);
	    editor = pre.edit();
	    editor.clear();
	    editor.commit();
	    }
	
	public static long readUID(Context context){
		pre = context.getSharedPreferences("keepUID", Context.MODE_APPEND);
		return pre.getLong("uid", 0);
	}
}
