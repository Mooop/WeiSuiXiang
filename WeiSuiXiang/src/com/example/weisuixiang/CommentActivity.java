package com.example.weisuixiang;

import java.io.IOException;

import com.weibo.sdk.android.WeiboException;
import com.weibo.sdk.android.api.CommentsAPI;
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
import android.widget.MultiAutoCompleteTextView;
import android.widget.Toast;

public class CommentActivity extends Activity {

	Button comment, cancle;
	MultiAutoCompleteTextView comments;
	String id;
	CommentsAPI ca;
	static Handler hd;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.comment);
		
		Intent intent = getIntent();
		id = intent.getStringExtra("id");
		
		ca = new CommentsAPI(MainActivity.accessToken);
		initView();
		action();
		
		hd =new Handler(){
			public void handleMessage(Message msg){
				switch (msg.what) {
				case 0:
					comments.setText(null);
					Toast.makeText(CommentActivity.this, "评论发成功", Toast.LENGTH_SHORT).show();
					CommentActivity.this.finish();
					break;

				default:
					break;
				}
			}
		};
	}

	private void action() {
		// TODO Auto-generated method stub
		comment.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				ca.create(comments.getText().toString(), Long.parseLong(id), false, new RequestListener(){

					@Override
					public void onComplete(String arg0) {
						// TODO Auto-generated method stub
						Log.i("sina", arg0);
						Bundle bd = new Bundle();
						Message ms = new Message();
					    ms.setData(bd);
					    ms.what=0;
					    CommentActivity.hd.sendMessage(ms);
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
			}
			
		});
		
		cancle.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				CommentActivity.this.finish();
			}
			
		});
		
	}

	private void initView() {
		// TODO Auto-generated method stub
		comment = (Button) findViewById(R.id.comment);
		cancle = (Button) findViewById(R.id.cancle);
		comments = (MultiAutoCompleteTextView) findViewById(R.id.comments);
	}

}
