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
		//使用Intent添加第一个Tab页面
		tabHost.addTab(tabHost.newTabSpec("tab1")
				.setIndicator("主页")
				.setContent(new Intent(this, HomeActivity.class)));
		
		//使用Intent添加第二个Tab页面
		tabHost.addTab(tabHost.newTabSpec("tab2")
				.setIndicator("我")
				.setContent(new Intent(this, AboutActivity.class)));
		
		//使用Intent添加第三个Tab页面
		tabHost.addTab(tabHost.newTabSpec("tab2")
				.setIndicator("选项")
				.setContent(new Intent(this, OptionActivity.class)));
		
	}

}
