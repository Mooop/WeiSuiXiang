package com.example.weisuixiang;

import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;




import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.weibo.sdk.android.WeiboException;
import com.weibo.sdk.android.api.FriendshipsAPI;
import com.weibo.sdk.android.net.RequestListener;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class FollowersActivity extends Activity {

	protected ImageLoader imageLoader = ImageLoader.getInstance();
	DisplayImageOptions options;
	ListView list;
	FriendshipsAPI fa;
	String id;
	String[] screen_name, profile_image_url, msg2, msg3;
	int[] user_id, msg1;
	Handler hd;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.followers);
		
		fa = new FriendshipsAPI(MainActivity.accessToken);
		initView();
		action();
		getfollowers();
		refreshUI();
	}
	
	//更新UI
	private void refreshUI() {
		// TODO Auto-generated method stub
		hd =new Handler(){
			public void handleMessage(Message msg){
				switch (msg.what) {
				case 0:
					Bundle b = msg.getData();
					String[] str2 = b.getStringArray("msg2");
					String[] str3 = b.getStringArray("msg3");
					list.setAdapter(new ItemAdapter(str2, str3));
					break;
					
				default:
					break;
					}
				}
		};
	}

	private void getfollowers() {
		// TODO Auto-generated method stub
		fa.followers(Long.parseLong(id), 100, 0, false, new RequestListener(){

			@Override
			public void onComplete(String arg0) {
				// TODO Auto-generated method stub
				Log.i("sina", arg0);
				new UpDateUI(FollowersActivity.this, this, arg0).start();
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
		
	}

	private void initView() {
		// TODO Auto-generated method stub
		list = (ListView) findViewById(R.id.followers);
		
		user_id = new int[50];
		screen_name = new String[50];
		profile_image_url = new String[50];
		
		Intent intent = getIntent();
		id = intent.getStringExtra("id");
		
		options = new DisplayImageOptions.Builder()
		//.showStubImage(R.drawable.ic_launcher)
		.showImageForEmptyUri(R.drawable.ic_launcher)
		.showImageOnFail(R.drawable.ic_launcher)
		.resetViewBeforeLoading(true)
		.cacheInMemory(true)
		.cacheOnDisc(true)
		.displayer(new RoundedBitmapDisplayer(10))
		.build();
	}
	
	//网络耗时操作部分
	class UpDateUI extends Thread{
		
		FollowersActivity followersActivity;
		RequestListener listener;
		String arg0;
		
		@Override
		public void run() {
			try{
				JSONObject jsonObject = new JSONObject(arg0);
				JSONArray jsonArray = jsonObject.getJSONArray("users");
				for(int i = 0; i < 50 ; i++ ){
					JSONObject jsonOb = jsonArray.getJSONObject(i);
					user_id[i] = jsonOb.getInt("id"); 
					screen_name[i] = jsonOb.getString("screen_name");
					profile_image_url[i] = jsonOb.getString("profile_image_url");
					}
				
				Bundle bd = new Bundle();
				
			    msg1 = user_id;
			    msg2 = screen_name;
			    msg3 = profile_image_url;
			    
			    bd.putIntArray("msg1",msg1);
			    bd.putStringArray("msg2", msg2);
			    bd.putStringArray("msg3", msg3);
			    
			    Message ms = new Message();
			    ms.setData(bd);
			    ms.what=0;
			    followersActivity.hd.sendMessage(ms);
			    }catch(Exception e) {
			    	// TODO Auto-generated catch block
			    	e.printStackTrace();
			    	}
			}
		
		public UpDateUI(FollowersActivity followersActivity, RequestListener listener , String arg0){
			this.listener = listener;
			this.arg0 = arg0;
			this.followersActivity = followersActivity;
			}
		
		}
	
	//异步加载图片
	class ItemAdapter extends BaseAdapter {

		String[] msg2, msg3;
		private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
		
		private class ViewHolder {
			public TextView screen_name;
			public ImageView profile_image;
		}

		public ItemAdapter(String[] msg2 ,String[] msg3) {
			// TODO Auto-generated constructor stub
			this.msg2 = msg2;
			this.msg3 = msg3;
		}

		@Override
		public int getCount() {
			return msg2.length;
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			View view = convertView;
			final ViewHolder holder;
			if (convertView == null) {
				view = getLayoutInflater().inflate(R.layout.followers_list, parent, false);
				holder = new ViewHolder();
				holder.screen_name = (TextView) view.findViewById(R.id.followers_screen_name);
				holder.profile_image = (ImageView) view.findViewById(R.id.followers_image);
				view.setTag(holder);
			} else {
				holder = (ViewHolder) view.getTag();
			}
			
			holder.screen_name.setText(msg2[position]);
			imageLoader.displayImage(msg3[position], holder.profile_image, options, animateFirstListener);
			return view;
		}
	}
	
	private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {

		static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

		@Override
		public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
			if (loadedImage != null) {
				ImageView imageView = (ImageView) view;
				boolean firstDisplay = !displayedImages.contains(imageUri);
				if (firstDisplay) {
					FadeInBitmapDisplayer.animate(imageView, 500);
					displayedImages.add(imageUri);
				}
			}
		}
	}
	

}
