package com.example.weisuixiang;

import com.example.weisuixiang.R;
import com.util.AccessTokenKeeper;
import com.weibo.sdk.android.Oauth2AccessToken;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Intent;
import android.view.Window;

public class SplashActivity extends Activity {

	private final int SPLASH_DISPLAY_LENGHT = 1000;
	public static Oauth2AccessToken accessToken;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.splash);
		
		//读取保存再sharepreference的accesstoken
	    accessToken = AccessTokenKeeper.readAccessToken(this);
	
		new Handler().postDelayed(new Runnable(){
			@Override
			public void run() {
				//accessToken有效则跳转
				if(accessToken.isSessionValid()){
					Intent mainIntent = new Intent(SplashActivity.this,HomeActivity.class);
					SplashActivity.this.startActivity(mainIntent);
					SplashActivity.this.finish();
				}else{
					Intent mainIntent = new Intent(SplashActivity.this,LoginActivity.class);
					SplashActivity.this.startActivity(mainIntent);
					SplashActivity.this.finish();
				}
				
				}
			}, SPLASH_DISPLAY_LENGHT);
	}
}
