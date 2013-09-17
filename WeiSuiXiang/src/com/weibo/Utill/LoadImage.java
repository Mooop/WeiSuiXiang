package com.weibo.Utill;

import java.io.InputStream;

import com.example.weisuixiang.AboutActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class LoadImage extends AsyncTask<String, Void, Bitmap> {  
	
	 ImageView imageView;
	 public LoadImage(ImageView imageView) { 
		 // TODO Auto-generated constructor stub 
		 this.imageView = imageView;
		 } 

	 @Override 
	 protected Bitmap doInBackground(String... urls) { 
		 // TODO Auto-generated method stub 
		 String url = urls[0]; 
		 Bitmap tmpBitmap = null; 
		 try {
			 InputStream is = new java.net.URL(url).openStream(); 
			 tmpBitmap = BitmapFactory.decodeStream(is); 
			 } catch (Exception e) { 
				 e.printStackTrace(); 
				 Log.i("test", e.getMessage()); 
				 } 
		 return tmpBitmap; 
		 } 
		
	 @Override 
	 protected void onPostExecute(Bitmap result) { 
		 // TODO Auto-generated method stub 
		 imageView.setImageBitmap(result); 
		 } 
	 }
