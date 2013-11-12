package com.example.weisuixiang;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.annotation.SuppressLint;
import android.content.Intent;

@SuppressLint("NewApi")
public class StatusesActivity extends SherlockActivity {
	
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	private DisplayImageOptions options;
	private String statuses_id, profile_image, screen_name, statuses_text, retweeted_statuses, pic_url1, pic_url2, reposts_comments;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle("微博正文");
		setContentView(R.layout.statuses_activity);
		
		initActionBar();
		initData();
		initView();
	}
	
	private void initView() {
		// TODO Auto-generated method stub
		
		options = new DisplayImageOptions.Builder()
		//.showStubImage(R.drawable.ic_launcher)
		//.showImageForEmptyUri(R.drawable.ic_launcher)
		.showImageOnFail(R.drawable.ic_launcher)
		.resetViewBeforeLoading(true)
		.cacheInMemory(true)
		.cacheOnDisc(true)
		//.displayer(new RoundedBitmapDisplayer(10))
		.build();
		
		 ImageView profileimage= (ImageView)findViewById(R.id.profile_image2);
		 imageLoader.displayImage(profile_image, profileimage, options);
		 
		 ImageView pic11 = (ImageView)findViewById(R.id.pic11);
		 imageLoader.displayImage(pic_url1, pic11, options);
		 
		 ImageView pic22 = (ImageView)findViewById(R.id.pic22);
		 imageLoader.displayImage(pic_url2, pic22, options);
		 
		 TextView name = (TextView)findViewById(R.id.screen_name2);
		 name.setText(screen_name);
		 
		 TextView textview = (TextView)findViewById(R.id.text2);
		 textview.setText(statuses_text);
		 
		 TextView retweeted = (TextView)findViewById(R.id.retweeted_status2);
		 retweeted.setText(retweeted_statuses);
		 
		 TextView reposts = (TextView)findViewById(R.id.reposts_comments2);
		 reposts.setText(reposts_comments);
	}

	private void initData() {
		// TODO Auto-generated method stub
		Intent intent = getIntent();
		statuses_id = intent.getStringExtra("statuses_id");
		profile_image = intent.getStringExtra("profile_image");
		screen_name = intent.getStringExtra("screen_name");
		statuses_text = intent.getStringExtra("statuses_text");
		retweeted_statuses = intent.getStringExtra("retweeted_statuses");
		pic_url1 = intent.getStringExtra("pic_url1");
		pic_url2 = intent.getStringExtra("pic_url2");
		reposts_comments = intent.getStringExtra("reposts_comments");
		
	}

	//初始化ActionBar
	private void initActionBar() {
		// TODO Auto-generated method stub
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setLogo(getResources().getDrawable(R.drawable.back));
		getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.ab_bg_green));
	}

	//添加和监听菜单
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuItem repost = menu.add("转发");
		MenuItem comments = menu.add("评论");
		
		comments.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		repost.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		return true;
	}
	
	//点击左上角图标返回主界面
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		 switch (item.getItemId()) {
		 case android.R.id.home:
		     StatusesActivity.this.finish();
			 return true;
			 
		 default:
			 return super.onOptionsItemSelected(item);
			 
		 }
		 
	}

}
