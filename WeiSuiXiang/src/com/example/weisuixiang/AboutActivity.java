package com.example.weisuixiang;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.weibo.Utill.LoadImage;
import com.weibo.sdk.android.Oauth2AccessToken;
import com.weibo.sdk.android.WeiboException;
import com.weibo.sdk.android.api.UsersAPI;
import com.weibo.sdk.android.keep.AccessTokenKeeper;
import com.weibo.sdk.android.keep.SharePre;
import com.weibo.sdk.android.net.RequestListener;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class AboutActivity extends Activity{
	
	public static Oauth2AccessToken accessToken;
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	DisplayImageOptions options;
	ProgressDialog progressDialog;
	ImageView profile_image , thumbnail_pic;
	TextView screen_name , location , description , statuses;
	Button statuses_count , friends_count , followers_count;
	String[] arr, msg;
	JSONObject jsonObject;
	JSONArray jsonArray;
	JSONObject jsonOb;
	Handler hd;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.about);
		
		initView();
		action();
		getUserInfor();
		refreshUI();
		//new UserIn(profile_image).execute(User.profileImageUrl);
	}

	private void refreshUI() {
		// TODO Auto-generated method stub
		hd =new Handler(){
			public void handleMessage(Message msg){
				switch (msg.what) {
				case 0:
					Bundle b = msg.getData();
					String[] str = b.getStringArray("msg");
					screen_name.setText("Í«≥∆£∫" + str[0]);
					location.setText("À˘‘⁄µÿ£∫" + str[1]);
					description.setText("ºÚΩÈ£∫" + str[2]);
					statuses_count.setText(str[3] + "\n" + "Œ¢≤©");
					friends_count.setText(str[4] + "\n" + "πÿ◊¢");
					followers_count.setText(str[5] + "\n" + "∑€Àø");
					imageLoader.displayImage(arr[6], profile_image);
					statuses.setText("Œ¢≤©" + "(" + str[3] + ")" + "\n" + str[0] + "\n" + arr[7]);
					imageLoader.displayImage(arr[8], thumbnail_pic);
					//new LoadImage(profile_image).execute(arr[6]);
					//statuses.setText("Œ¢≤©" + "(" + str[3] + ")" + "\n" + str[0] + "\n" + arr[7]);
					progressDialog.dismiss();
					break;

				default:
					break;
				}
			}
		};
	}

	private void getUserInfor() {
		// TODO Auto-generated method stub
		progressDialog = ProgressDialog.show(this,null, "º”‘ÿ÷–...",true, true);
		UsersAPI ua = new UsersAPI(MainActivity.accessToken);
		ua.show(SharePre.readUID(this), new RequestListener(){

			@Override
			public void onComplete(String arg0) {
				// TODO Auto-generated method stub
				Log.i("sina", arg0);
				new UpDateUI(AboutActivity.this, this, arg0).start();
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

	private void action() {
		// TODO Auto-generated method stub
		statuses_count.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent mainIntent = new Intent(AboutActivity.this , MyStatusesActivity.class);
				mainIntent.putExtra("id", arr[9]);
				AboutActivity.this.startActivity(mainIntent);
			}
			
		});
		
		friends_count.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent mainIntent = new Intent(AboutActivity.this , FriendsActivity.class);
				mainIntent.putExtra("id", arr[9]);
				AboutActivity.this.startActivity(mainIntent);
			}
			
		});
		
		followers_count.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent mainIntent = new Intent(AboutActivity.this , FollowersActivity.class);
				mainIntent.putExtra("id", arr[9]);
				AboutActivity.this.startActivity(mainIntent);
			}
			
		});
	}

	private void initView() {
		// TODO Auto-generated method stub
		
		options = new DisplayImageOptions.Builder()
		.showStubImage(R.drawable.ic_launcher)
		.showImageForEmptyUri(R.drawable.ic_launcher)
		.showImageOnFail(R.drawable.ic_launcher)
		.cacheInMemory(true)
		.cacheOnDisc(true)
		.displayer(new RoundedBitmapDisplayer(10))
		.build();
		
		profile_image = (ImageView) findViewById(R.id.profile_image);
		thumbnail_pic = (ImageView) findViewById(R.id.thumbnail_pic);
		screen_name = (TextView) findViewById(R.id.screen_name);
		location = (TextView) findViewById(R.id.location);
		description = (TextView) findViewById(R.id.description);
		statuses = (TextView) findViewById(R.id.statuses);
		
		statuses_count = (Button) findViewById(R.id.statuses_count);
		friends_count = (Button) findViewById(R.id.friends_count);
		followers_count = (Button) findViewById(R.id.followers_count);
		
	}
	
	class UpDateUI extends Thread{
		
		AboutActivity aboutActivity;
		RequestListener listener;
		String arg0;
		
		public UpDateUI(AboutActivity aboutActivity , RequestListener listener , String arg0){
			this.listener = listener;
			this.arg0 = arg0;
			this.aboutActivity = aboutActivity;
		}
		
		@Override
		public void run() {
			arr = new String[10];
			try{
				JSONObject jsonObject = new JSONObject(arg0);
			    arr[0] = jsonObject.getString("screen_name");
				arr[1] = jsonObject.getString("location");
				arr[2] = jsonObject.getString("description");
				arr[3] = String.valueOf(jsonObject.getInt("statuses_count"));
				arr[4] = String.valueOf(jsonObject.getInt("friends_count"));
				arr[5] = String.valueOf(jsonObject.getInt("followers_count"));
				arr[6] = jsonObject.getString("avatar_hd");
				arr[7] = jsonObject.getJSONObject("status").getString("text");
				arr[8] = jsonObject.getJSONObject("status").optString("thumbnail_pic");
				arr[9] = jsonObject.getString("id");
				
				Bundle bd = new Bundle();
			    msg = arr;
			    bd.putStringArray("msg",msg);
			    Message ms = new Message();
			    ms.setData(bd);
			    ms.what=0;
			    aboutActivity.hd.sendMessage(ms);
			    }catch(Exception e) {
			    	// TODO Auto-generated catch block
			    	e.printStackTrace();
			    	}
			}
		}
}
