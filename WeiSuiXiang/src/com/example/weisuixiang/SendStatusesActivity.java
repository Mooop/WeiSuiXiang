package com.example.weisuixiang;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import com.weibo.sdk.android.WeiboException;
import com.weibo.sdk.android.api.StatusesAPI;
import com.weibo.sdk.android.net.RequestListener;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SendStatusesActivity extends Activity {
	
	Button back , send , pictures;
	EditText statuese;
	ProgressDialog progressDialog;
	String msg , file = null;
	StatusesAPI sa;
	Message ms;
	Bundle bd;
	private static int RESULT_LOAD_IMAGE = 1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.send_statuses);
		
	    sa = new StatusesAPI(MainActivity.accessToken);
	    bd = new Bundle();
	    ms = new Message();
		initView();
		action();
	}

	private void action() {
		// TODO Auto-generated method stub
		
		back.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//返回时隐藏键盘
				 InputMethodManager imm = (InputMethodManager)getSystemService(SendStatusesActivity.INPUT_METHOD_SERVICE); 
		         imm.hideSoftInputFromWindow(statuese.getWindowToken(),0);
		         SendStatusesActivity.this.finish();
			}
			
		});
		
		send.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				progressDialog = ProgressDialog.show(SendStatusesActivity.this,null, "正在发送中...",true, true);
				if(file == null){
					if("".equals(statuese.getText().toString().trim())){
						progressDialog.dismiss();
						Toast.makeText(SendStatusesActivity.this, "不能发送空内容", Toast.LENGTH_SHORT).show();
						}else{
							sa.update(statuese.getText().toString(), null, null, listener);
						}
					}else{
						sa.upload(statuese.getText().toString(), file, null, null, listener);
						}
				}
			}
		);
		
		pictures.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//打开图库
				Intent i = new Intent(
						Intent.ACTION_PICK , 
						android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				startActivityForResult(i, RESULT_LOAD_IMAGE);
			}
			
		});
	}

	private void initView() {
		// TODO Auto-generated method stub
		back = (Button) findViewById(R.id.back);
		send = (Button) findViewById(R.id.send);
		pictures = (Button) findViewById(R.id.pictures);
		statuese = (EditText) findViewById(R.id.statuses);
		
		Timer timer = new Timer(); //设置定时器
		timer.schedule(new TimerTask() {
		
		@Override
		public void run() { //弹出软键盘的代码
				InputMethodManager imm = (InputMethodManager)getSystemService(SendStatusesActivity.INPUT_METHOD_SERVICE);
				imm.showSoftInput(statuese, InputMethodManager.RESULT_SHOWN);
				imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
			}
		}, 200); //设置300毫秒的时长
	} 
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);	
		if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
			Uri selectedImage = data.getData();
			String[] filePathColumn = { MediaStore.Images.Media.DATA };
			Cursor cursor = getContentResolver().query(selectedImage,
					filePathColumn, null, null, null);
			cursor.moveToFirst();
			
			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			String picturePath = cursor.getString(columnIndex);
			Toast.makeText(SendStatusesActivity.this, picturePath, Toast.LENGTH_SHORT).show();
			file = picturePath;
			statuese.setText("分享图片");
			cursor.close();
			}
		}
	
	RequestListener listener = new RequestListener(){

		@Override
		public void onComplete(String arg0) {
			// TODO Auto-generated method stub
			Log.i("sina", arg0);
			ms.what = 0;
			SendStatusesActivity.this.hd.sendMessage(ms);
		}

		@Override
		public void onError(WeiboException arg0) {
			// TODO Auto-generated method stub
			Log.i("sina", "WeiboException:"+arg0);
			ms.what = 1;
			SendStatusesActivity.this.hd.sendMessage(ms);
		}

		@Override
		public void onIOException(IOException arg0) {
			// TODO Auto-generated method stub
			Log.i("sina", "IOException:"+arg0);
			ms.what = 2;
			SendStatusesActivity.this.hd.sendMessage(ms);
			}
		};
		
		Handler hd =new Handler(){
			public void handleMessage(Message msg){
				switch (msg.what) {
				case 0:
					statuese.setText("");
					file = null;
					progressDialog.dismiss();
					SendStatusesActivity.this.finish();
					break;
					
				case 1:
					progressDialog.dismiss();
					Toast.makeText(SendStatusesActivity.this, "不要发送空白内容或两分钟内发布同一内容", Toast.LENGTH_SHORT);
					break;
					
				case 2:
					progressDialog.dismiss();
					Bundle b = msg.getData();
					String arg0 = b.getString("msg");
					Toast.makeText(getApplicationContext(), "不好意思出问题了( >﹏<。)～", Toast.LENGTH_SHORT);
				default:
					break;
				}
			}
		};
}