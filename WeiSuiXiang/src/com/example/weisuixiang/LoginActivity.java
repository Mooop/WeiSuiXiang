package com.example.weisuixiang;

import com.weibo.sdk.android.Oauth2AccessToken;
import com.weibo.sdk.android.Weibo;
import com.weibo.sdk.android.WeiboAuthListener;
import com.weibo.sdk.android.WeiboDialogError;
import com.weibo.sdk.android.WeiboException;
import com.weibo.sdk.android.keep.AccessTokenKeeper;
import com.weibo.sdk.android.keep.SharePre;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity {
	
	private Weibo mWeibo;
	private static final String CONSUMER_KEY = "4192765690";
	private static final String REDIRECT_URL = "http://www.sina.com";
	public static Oauth2AccessToken accessToken;
	private Button login;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.login);
		
		mWeibo = Weibo.getInstance(CONSUMER_KEY, REDIRECT_URL);
		initView();
		action();
	}

	private void action() {
		// TODO Auto-generated method stub
		login.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				mWeibo.authorize(LoginActivity.this, new AuthDialogListener());
			}
			
		});
	}

	private void initView() {
		// TODO Auto-generated method stub
		login = (Button) findViewById(R.id.login);
	}
	
	class AuthDialogListener implements WeiboAuthListener{
		String token , expires_in , uidString;

		@Override
		public void onCancel() {
			// TODO Auto-generated method stub
			Toast.makeText(getApplicationContext(), "Auth cancle" , Toast.LENGTH_SHORT);
		}

		@Override
		public void onComplete(Bundle values) {
			// TODO Auto-generated method stub
			 token = values.getString("access_token");
	         expires_in = values.getString("expires_in");
	         uidString = values.getString("uid");
	         long uid = Long.parseLong(uidString);
	         
	         LoginActivity.accessToken = new Oauth2AccessToken(token, expires_in);
	         
	         if (LoginActivity.accessToken.isSessionValid()) {
	        	 if (LoginActivity.accessToken.isSessionValid()) {
	        		 AccessTokenKeeper.keepAccessToken(LoginActivity.this,accessToken);
	        		 SharePre.keepUID(LoginActivity.this, uid);
	        		 Toast.makeText(LoginActivity.this, "µÇÂ½³É¹¦", Toast.LENGTH_SHORT).show();
	        		 
	        		 Intent mainIntent = new Intent(LoginActivity.this,MainActivity.class);
	        		 LoginActivity.this.startActivity(mainIntent);
	        		 LoginActivity.this.finish();
	        	 }
	         }
		}

		@Override
		public void onError(WeiboDialogError e) {
			// TODO Auto-generated method stub
			Toast.makeText(getApplicationContext(), "Auth error:" + e.getMessage(), Toast.LENGTH_SHORT);
		}

		@Override
		public void onWeiboException(WeiboException e) {
			// TODO Auto-generated method stub
			Toast.makeText(getApplicationContext(), "Auth exception:" + e.getMessage(), Toast.LENGTH_SHORT);
		}
		}
}
