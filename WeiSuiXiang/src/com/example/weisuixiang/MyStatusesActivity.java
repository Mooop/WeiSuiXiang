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
import com.weibo.sdk.android.WeiboException;
import com.weibo.sdk.android.api.StatusesAPI;
import com.weibo.sdk.android.api.WeiboAPI;
import com.weibo.sdk.android.api.WeiboAPI.FEATURE;
import com.weibo.sdk.android.net.RequestListener;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class MyStatusesActivity extends Activity {

	protected ImageLoader imageLoader = ImageLoader.getInstance();
	DisplayImageOptions options;
	JSONObject jsonObject;
	JSONArray jsonArray;
	JSONObject jsonOb;
	ListView list;
	Button send,refresh;
	String id;
	String[] image_urls, screen_name, statusesarr, statuses_id, retweeted_statuses, reposts_comments, 
	pic_url1, pic_url2, bmiddle_pic, retweeted_status_bmiddle_pic,msg1, msg2, msg3, msg4, 
	msg5, msg6, msg7;
	Handler hd;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.my_statuses);
		
		initView();
		action();
		getStatuses();
		refreshUI();
	}

	private void refreshUI() {
		// TODO Auto-generated method stub
		hd =new Handler(){
			public void handleMessage(Message msg){
				switch (msg.what) {
				case 0:
					Bundle b = msg.getData();
					String[] str1 = b.getStringArray("msg1");
					String[] str2 = b.getStringArray("msg2");
					String[] str3 = b.getStringArray("msg3");
					String[] str4 = b.getStringArray("msg4");
					String[] str5 = b.getStringArray("msg5");
					String[] str6 = b.getStringArray("msg6");
					String[] str7 = b.getStringArray("msg7");
					//ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(HomeActivity.this, android.R.layout.simple_list_item_1, str);
					//list.setAdapter(arrayAdapter);
					list.setAdapter(new ItemAdapter(str1, str2, str3, str4, str5, str6, str7 ));
					//progressDialog.dismiss();
					break;

				default:
					break;
				}
			}
		};
	}

	private void getStatuses() {
		// TODO Auto-generated method stub
		StatusesAPI sa = new StatusesAPI(MainActivity.accessToken);
		sa.userTimeline(Long.parseLong(id), 0, 0, 50, 1, false, FEATURE.ALL, false, new RequestListener(){

			@Override
			public void onComplete(String arg0) {
				// TODO Auto-generated method stub
				Log.i("sina", arg0);
				new UpDateUI(MyStatusesActivity.this, this, arg0).start();
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
		options = new DisplayImageOptions.Builder()
		.showStubImage(R.drawable.ic)
		//.showImageForEmptyUri(R.drawable.ic)
		.showImageOnFail(R.drawable.ic)
		.resetViewBeforeLoading(true)
		.cacheInMemory(true)
		.cacheOnDisc(true)
		//.displayer(new RoundedBitmapDisplayer(10))
		.build();
		
		Intent intent = getIntent();
		id = intent.getStringExtra("id");
		
		image_urls = new String[50];
		screen_name = new String[50];
		statusesarr = new String[50];
		statuses_id = new String[50];
		retweeted_statuses = new String[50];
		pic_url1 = new String[50];
		pic_url2 = new String[50];
		bmiddle_pic = new String[50];
		retweeted_status_bmiddle_pic = new String[50];
		reposts_comments = new String[50];
		
		list = (ListView) findViewById(R.id.myStatuses);
	}
	
	//网络耗时操作部分
	class UpDateUI extends Thread{
		
		MyStatusesActivity myStatusesActivity;
		RequestListener listener;
		String arg0;
		String user ,text , reposts_count , comments_count , retweeted_status , retweeted_status_user;
		
		@Override
		public void run() {
			try{
				jsonObject = new JSONObject(arg0);
				jsonArray = jsonObject.getJSONArray("statuses");
				JSONObject jobj;
				
				
				for(int i = 0; i < 50 ; i++ ){
					jsonOb = jsonArray.getJSONObject(i);
					image_urls[i] = jsonOb.getJSONObject("user").getString("profile_image_url");
					statuses_id[i] = jsonOb.getString("id");
					screen_name[i] = jsonOb.getJSONObject("user").getString("screen_name");
					user = jsonOb.getJSONObject("user").getString("screen_name");
					text = jsonOb.getString("text");
					reposts_count = jsonOb.getString("reposts_count");
					comments_count = jsonOb.getString("comments_count");
					reposts_comments[i] = "转发：" + reposts_count + "     " + "评论：" + comments_count  + "\n";
					
					jobj = jsonOb.optJSONObject("retweeted_status");
					
					pic_url1[i] = jsonOb.optString("thumbnail_pic");
					bmiddle_pic[i] = jsonOb.optString("bmiddle_pic");
					
					if(jobj == null){
						statusesarr[i] = text;
						pic_url2[i] = null;
						retweeted_statuses[i] = null;
						retweeted_status_bmiddle_pic[i] = null;
						}else{
							pic_url2[i] = jobj.optString("thumbnail_pic");
							retweeted_status_bmiddle_pic[i] = jobj.optString("bmiddle_pic");
							//retweeted_status_user = jsonOb.optJSONObject("retweeted_status").optJSONObject("user").getString("screen_name");
							//retweeted_status = jsonOb.optJSONObject("retweeted_status").getString("text");
							
							retweeted_status_user = jobj.optJSONObject("user").getString("screen_name");
							retweeted_status = jobj.getString("text");
							statusesarr[i] = text;
							retweeted_statuses[i] = "∧" +"\n" +  retweeted_status_user + "\n" + retweeted_status + "\n";
							}
					}
				
				Bundle bd = new Bundle();
				
			    msg1 = image_urls;
			    msg2 = screen_name;
			    msg3 = statusesarr;
			    msg4 = retweeted_statuses;
			    msg5 = pic_url1;
			    msg6 = pic_url2;
			    msg7 = reposts_comments;
			    
			    bd.putStringArray("msg1",msg1);
			    bd.putStringArray("msg2",msg2);
			    bd.putStringArray("msg3",msg3);
			    bd.putStringArray("msg4",msg4);
			    bd.putStringArray("msg5",msg5);
			    bd.putStringArray("msg6",msg6);
			    bd.putStringArray("msg7",msg7);
			    
			    Message ms = new Message();
			    ms.setData(bd);
			    ms.what=0;
			    myStatusesActivity.hd.sendMessage(ms);
			    }catch(Exception e) {
			    	// TODO Auto-generated catch block
			    	e.printStackTrace();
			    	}
			}
		
		public UpDateUI(MyStatusesActivity myStatusesActivity , RequestListener listener , String arg0){
			this.listener = listener;
			this.arg0 = arg0;
			this.myStatusesActivity = myStatusesActivity;
			}
		}
	
	//异步加载图片
	class ItemAdapter extends BaseAdapter {

		String[] msg1, msg2, msg3, msg4, msg5, msg6, msg7;
		private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
		
		private class ViewHolder {
			public TextView screen_name, text, retweeted_status, reposts_comments;
			public ImageView profile_image, pic1, pic2;
		}

		public ItemAdapter(String[] msg1, String[] msg2 ,String[] msg3, String[] msg4, String[] msg5, String[] msg6, String[] msg7) {
			// TODO Auto-generated constructor stub
			this.msg1 = msg1;
			this.msg2 = msg2;
			this.msg3 = msg3;
			this.msg4 = msg4;
			this.msg5 = msg5;
			this.msg6 = msg6;
			this.msg7 = msg7;
		}

		@Override
		public int getCount() {
			return msg1.length;
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
				view = getLayoutInflater().inflate(R.layout.my_statuses_image, parent, false);
				holder = new ViewHolder();
				holder.screen_name = (TextView) view.findViewById(R.id.my_screen_name);
				holder.text = (TextView) view.findViewById(R.id.my_text);
				holder.retweeted_status = (TextView) view.findViewById(R.id.my_retweeted_status);
				holder.reposts_comments = (TextView) view.findViewById(R.id.my_reposts_comments);
				holder.profile_image = (ImageView) view.findViewById(R.id.my_profile_image);
				holder.pic1 = (ImageView) view.findViewById(R.id.my_pic1);
				holder.pic2 = (ImageView) view.findViewById(R.id.my_pic2);
				view.setTag(holder);
			} else {
				holder = (ViewHolder) view.getTag();
			}
			
			holder.screen_name.setText(msg2[position]);
			holder.text.setText(msg3[position]/*"Item " + (position + 1)*/);
			holder.retweeted_status.setText(msg4[position]);
			holder.reposts_comments.setText(msg7[position]);
			
			imageLoader.displayImage(msg1[position], holder.profile_image, options, animateFirstListener);
			imageLoader.displayImage(msg5[position], holder.pic1, options, animateFirstListener);
			imageLoader.displayImage(msg6[position], holder.pic2, options, animateFirstListener);
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
