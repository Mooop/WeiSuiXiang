package com.example.weisuixiang;

import com.weibo.sdk.android.keep.AccessTokenKeeper;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.Button;
import android.widget.Toast;

public class OptionActivity extends Activity {

	Button exit;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.option);
		
		initView();
		action();
	}

	private void action() {
		// TODO Auto-generated method stub
		exit.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//Çå³ýccesstoken
				AccessTokenKeeper.clear(OptionActivity.this);
				//Çå³ý»º´æ
				CookieSyncManager.createInstance(OptionActivity.this); 
				CookieManager cookieManager = CookieManager.getInstance();
				cookieManager.removeAllCookie();
				Toast.makeText(OptionActivity.this, "ÒÑ×¢ÏúÎ¢²©ÇëÖØÐÂµÇÂ¼", Toast.LENGTH_SHORT).show();
				Intent mainIntent = new Intent(OptionActivity.this,LoginActivity.class);
				OptionActivity.this.startActivity(mainIntent);
				OptionActivity.this.finish();
			}
			
		});
	}

	private void initView() {
		// TODO Auto-generated method stub
		exit = (Button) findViewById(R.id.exit);
	}

}
