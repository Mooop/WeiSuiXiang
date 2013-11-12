package com.example.weisuixiang;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuItem.OnMenuItemClickListener;
import com.util.AccessTokenKeeper;
import com.weibo.sdk.android.WeiboException;
import com.weibo.sdk.android.api.StatusesAPI;
import com.weibo.sdk.android.net.RequestListener;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;


@SuppressLint({ "NewApi", "HandlerLeak", "ShowToast" })
public class SendStatusesActivity extends SherlockActivity {
	
	private static int RESULT_LOAD_IMAGE = 1;
	private String file = null;
	private EditText editStatuses;
	private Button send_image;
	private Handler handler;
	private Message message;
	@SuppressWarnings("unused")
	private static Bundle bundle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle("����΢��");
		setContentView(R.layout.send_statuses);
		
		editStatuses = (EditText) findViewById(R.id.edit_statuses);
		send_image = (Button) findViewById(R.id.send_image);
		init();
		initActionBar();
		initInput();
		action();
		
	}
	
	//��ʼ��
	private void init() {
		// TODO Auto-generated method stub
		bundle = new Bundle();
	    message = new Message();
	    
		handler = new Handler(){
			public void handleMessage(Message msg){
				switch (msg.what) {
				case 0:
					Toast.makeText(SendStatusesActivity.this, "���ͳɹ�", Toast.LENGTH_SHORT).show();
					SendStatusesActivity.this.finish();
					break;
					
				default:
					break;
					
				}
				
			}
			
		};
	}
	
	//��������
	private void action() {
		// TODO Auto-generated method stub
		send_image.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent i = new Intent(
						Intent.ACTION_PICK , 
						android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				startActivityForResult(i, RESULT_LOAD_IMAGE);
			}
			
		});
	}
	
	//��ʼ������
	private void initInput() {
		// TODO Auto-generated method stub
		Timer timer = new Timer(); //���ö�ʱ��
		timer.schedule(new TimerTask() {
		
		@Override
		public void run() { //��������̵Ĵ���
				InputMethodManager imm = (InputMethodManager)getSystemService(SendStatusesActivity.this.INPUT_METHOD_SERVICE);
				imm.showSoftInput(editStatuses, InputMethodManager.RESULT_SHOWN);
				imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
			}
		}, 200); //����200�����ʱ��
	}

	//��ʼ��ActionBar
	private void initActionBar() {
		// TODO Auto-generated method stub
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setLogo(getResources().getDrawable(R.drawable.back));
		getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.ab_bg_green));
	}
	
	//��Ӻͼ����˵�
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuItem send = menu.add("����");
		send.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		send.setOnMenuItemClickListener(new OnMenuItemClickListener(){

			@Override
			public boolean onMenuItemClick(MenuItem item) {
				// TODO Auto-generated method stub
				
				if(file == null){
					sendText();
				}else{
					sendTextAndPicture();
				}
				
				return false;
			}
			
		});
		return true;
	}
	
	//΢�����ֺ�ͼƬд��
	protected void sendTextAndPicture() {
		// TODO Auto-generated method stub
		new StatusesAPI(AccessTokenKeeper.readAccessToken(SendStatusesActivity.this))
		.upload(editStatuses.getText().toString(), file, null, null, listener);	
		
	}

	//΢������д��
	protected void sendText() {
		// TODO Auto-generated method stub
		new StatusesAPI(AccessTokenKeeper.readAccessToken(SendStatusesActivity.this))
		.update(editStatuses.getText().toString(), null, null, listener);
	}

	//������Ͻ�ͼ�귵��������
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		 switch (item.getItemId()) {
		 case android.R.id.home:
			 InputMethodManager imm = (InputMethodManager)getSystemService(SendStatusesActivity.this.INPUT_METHOD_SERVICE); 
		     imm.hideSoftInputFromWindow(editStatuses.getWindowToken(),0);
			 SendStatusesActivity.this.finish();
			 return true;
			 
		 default:
			 return super.onOptionsItemSelected(item);
			 
		 }
		 
	}
	
	//�����
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
			editStatuses.setText("����ͼƬ");
			cursor.close();
			}
		}
	
	RequestListener listener = new RequestListener(){

		@Override
		public void onComplete(String arg0) {
			// TODO Auto-generated method stub
			Log.i("sina", arg0);
			message.what = 0;
			SendStatusesActivity.this.handler.sendMessage(message);
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
		
	};

}
