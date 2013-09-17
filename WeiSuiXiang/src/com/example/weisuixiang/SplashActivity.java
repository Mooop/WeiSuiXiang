package com.example.weisuixiang;

import com.weibo.sdk.android.Oauth2AccessToken;
import com.weibo.sdk.android.keep.AccessTokenKeeper;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.view.Menu;
import android.view.Window;

public class SplashActivity extends Activity {

	private final int SPLASH_DISPLAY_LENGHT = 3000;
	public static Oauth2AccessToken accessToken;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.splash);
		
		//读取保存再sharepreference的accesstoken
		SplashActivity.accessToken = AccessTokenKeeper.readAccessToken(this);
		
		new Handler().postDelayed(new Runnable(){
			@Override
			public void run() {
				//若accesstoken有效
				if (SplashActivity.accessToken.isSessionValid()) {
					Intent mainIntent = new Intent(SplashActivity.this,MainActivity.class);
					SplashActivity.this.startActivity(mainIntent);
					SplashActivity.this.finish();
				}else{//否则
					Intent mainIntent = new Intent(SplashActivity.this,LoginActivity.class);
					SplashActivity.this.startActivity(mainIntent);
					SplashActivity.this.finish();
				}
				
				}
			}, SPLASH_DISPLAY_LENGHT);
		}
	}
