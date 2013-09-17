package com.example.weisuixiang;

import com.weibo.sdk.android.Oauth2AccessToken;
import com.weibo.sdk.android.keep.AccessTokenKeeper;

import android.os.Bundle;
import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Configuration;
import android.view.Menu;
import android.view.Window;
import android.widget.TabHost;

public class MainActivity extends TabActivity {

	public static Oauth2AccessToken accessToken;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		MainActivity.accessToken = AccessTokenKeeper.readAccessToken(this);
		TabHost tabHost = getTabHost();
		//ʹ��Intent��ӵ�һ��Tabҳ��
		tabHost.addTab(tabHost.newTabSpec("tab1")
				.setIndicator("��ҳ")
				.setContent(new Intent(this, HomeActivity.class)));
		
		//ʹ��Intent��ӵڶ���Tabҳ��
		tabHost.addTab(tabHost.newTabSpec("tab2")
				.setIndicator("��")
				.setContent(new Intent(this, AboutActivity.class)));
		
		//ʹ��Intent��ӵ�����Tabҳ��
		tabHost.addTab(tabHost.newTabSpec("tab2")
				.setIndicator("ѡ��")
				.setContent(new Intent(this, OptionActivity.class)));
		
	}

}
