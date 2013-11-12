package com.example.weisuixiang;

import com.example.weisuixiang.R;
import com.util.AccessTokenKeeper;
import com.util.SharePre;
import com.weibo.sdk.android.Oauth2AccessToken;
import com.weibo.sdk.android.Weibo;
import com.weibo.sdk.android.WeiboAuthListener;
import com.weibo.sdk.android.WeiboDialogError;
import com.weibo.sdk.android.WeiboException;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

@SuppressLint("ShowToast")
public class LoginActivity extends Activity {
	
	private Weibo mWeibo;
	private static final String CONSUMER_KEY = "4192765690";
	private static final String REDIRECT_URL = "http://www.sina.com";
	public static Oauth2AccessToken accessToken;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.login);
		
		//��Ȩ��½
		mWeibo = Weibo.getInstance(CONSUMER_KEY, REDIRECT_URL);
		mWeibo.authorize(LoginActivity.this, new AuthDialogListener());
		
		Button login = (Button) findViewById(R.id.login);
		login.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				mWeibo.authorize(LoginActivity.this, new AuthDialogListener());
			}
			
		});
	}
	
	/**
     * ΢����֤��Ȩ�ص��ࡣ
     * 1. SSO��½ʱ����Ҫ��{@link #onActivityResult}�е���mSsoHandler.authorizeCallBack��
     *    �ûص��Żᱻִ�С�
     * 2. ��SSO��½ʱ������Ȩ�󣬾ͻᱻִ�С�
     * ����Ȩ�ɹ����뱣���access_token��expires_in����Ϣ��SharedPreferences�С�
     */
    class AuthDialogListener implements WeiboAuthListener {
        
        @Override
        public void onComplete(Bundle values) {
            
            String token = values.getString("access_token");
            String expires_in = values.getString("expires_in");
            String uidString = values.getString("uid");
	        long uid = Long.parseLong(uidString);
            accessToken = new Oauth2AccessToken(token, expires_in);
            if (accessToken.isSessionValid()) {
            	
                AccessTokenKeeper.keepAccessToken(LoginActivity.this,accessToken);
                SharePre.keepUID(LoginActivity.this, uid);
                Toast.makeText(LoginActivity.this, "��֤�ɹ�", Toast.LENGTH_SHORT).show();
                
                Intent mainIntent = new Intent(LoginActivity.this,HomeActivity.class);
       		    LoginActivity.this.startActivity(mainIntent);
       		    LoginActivity.this.finish();
            }
        }

        @Override
        public void onError(WeiboDialogError e) {
            Toast.makeText(getApplicationContext(), 
                    "Auth error : " + e.getMessage(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCancel() {
            Toast.makeText(getApplicationContext(), "Auth cancel", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onWeiboException(WeiboException e) {
            Toast.makeText(getApplicationContext(), 
                    "Auth exception : " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
