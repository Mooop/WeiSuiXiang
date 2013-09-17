package com.example.weisuixiang;

import java.io.IOException;

import com.example.weisuixiang.HomeActivity.ItemAdapter;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.weibo.sdk.android.WeiboException;
import com.weibo.sdk.android.api.CommentsAPI;
import com.weibo.sdk.android.api.StatusesAPI;
import com.weibo.sdk.android.api.WeiboAPI;
import com.weibo.sdk.android.api.WeiboAPI.COMMENTS_TYPE;
import com.weibo.sdk.android.net.RequestListener;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class StatusesActivity extends Activity {
	
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	DisplayImageOptions options;
	StatusesAPI sa;
	CommentsAPI ca;
	Button repost, create;
	ListView list;
	ImageView profile_image, pic1, pic2;
	TextView screen_name, text, retweeted_status, reposts_comments;
	String pi, sn, id, tx, rs, rc, bp, rsbp;
	static Handler hd;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.sta);
		
		sa = new StatusesAPI(MainActivity.accessToken);
		initView();
		refreshUI();
		action();
		
		hd =new Handler(){
			public void handleMessage(Message msg){
				switch (msg.what) {
				case 0:
					Toast.makeText(StatusesActivity.this, "转发成功", Toast.LENGTH_SHORT).show();
					break;

				default:
					break;
				}
			}
		};
	}
	
	private void action() {
		// TODO Auto-generated method stub
		repost.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				sa.repost(Long.parseLong(id), "转发微博", COMMENTS_TYPE.NONE, new RequestListener(){

					@Override
					public void onComplete(String arg0) {
						// TODO Auto-generated method stub
						Log.i("sina", arg0);
						Bundle bd = new Bundle();
						Message ms = new Message();
					    ms.setData(bd);
					    ms.what=0;
					    StatusesActivity.hd.sendMessage(ms);
					}

					@Override
					public void onError(WeiboException arg0) {
						// TODO Auto-generated method stub
						Log.i("sina", "WeiboException:"+arg0.getMessage());
					}

					@Override
					public void onIOException(IOException arg0) {
						// TODO Auto-generated method stub
						Log.i("sina", "IOException:"+arg0.getMessage());
					}
					
				});
			}});
		
		create.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent mainIntent = new Intent(StatusesActivity.this , CommentActivity.class);
				mainIntent.putExtra("id", id);
				StatusesActivity.this.startActivity(mainIntent);
			}
			
		});
	}

	private void refreshUI() {
		// TODO Auto-generated method stub
		imageLoader.displayImage(pi, profile_image, options);
		imageLoader.displayImage(bp, pic1, options);
		imageLoader.displayImage(rsbp, pic2, options);
		screen_name.setText(sn);
		text.setText(tx);
		retweeted_status.setText(rs);
		reposts_comments.setText(rc);
	}

	private void initView() {
		// TODO Auto-generated method stub
		list = (ListView) findViewById(R.id.statuses_comments);
		
		repost = (Button) findViewById(R.id.repost);
		create = (Button) findViewById(R.id.create);
		
		profile_image = (ImageView) findViewById(R.id.profile_image2);
		pic1 = (ImageView) findViewById(R.id.pic11);
		pic2 = (ImageView) findViewById(R.id.pic22);
		
		screen_name = (TextView) findViewById(R.id.screen_name2);
		text = (TextView) findViewById(R.id.text2);
		retweeted_status = (TextView) findViewById(R.id.retweeted_status2);
		reposts_comments = (TextView) findViewById(R.id.reposts_comments2);
		
		Intent intent = getIntent();
		pi = intent.getStringExtra("profile_image");
		bp = intent.getStringExtra("bmiddle_pic");
		rsbp = intent.getStringExtra("retweeted_status_bmiddle_pic");
		sn = intent.getStringExtra("screen_name");
		tx = intent.getStringExtra("text");
		id = intent.getStringExtra("id");
		rs = intent.getStringExtra("retweeted_status");
		rc = intent.getStringExtra("reposts_comments");
		
		options = new DisplayImageOptions.Builder()
		//.showStubImage(R.drawable.ic_launcher)
		//.showImageForEmptyUri(R.drawable.ic_launcher)
		.showImageOnFail(R.drawable.ic_launcher)
		.resetViewBeforeLoading(true)
		.cacheInMemory(true)
		.cacheOnDisc(true)
		.displayer(new RoundedBitmapDisplayer(10))
		.build();
	}
	
	
}
