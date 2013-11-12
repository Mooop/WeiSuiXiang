package com.fragment;

import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuItem.OnMenuItemClickListener;
import com.example.weisuixiang.R;
import com.example.weisuixiang.SendStatusesActivity;
import com.example.weisuixiang.StatusesActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.util.AccessTokenKeeper;
import com.util.DBOpenHelper;
import com.util.Parse;
import com.weibo.sdk.android.Oauth2AccessToken;
import com.weibo.sdk.android.WeiboException;
import com.weibo.sdk.android.api.StatusesAPI;
import com.weibo.sdk.android.api.WeiboAPI.FEATURE;
import com.weibo.sdk.android.net.RequestListener;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

@SuppressLint("HandlerLeak")
public class HomeFragment extends SherlockFragment{
	
	public static Oauth2AccessToken accessToken;
	
	private ImageLoader imageLoader = ImageLoader.getInstance();
	private DisplayImageOptions options;
	
	public static Handler handler;
	private ListView list;
	
	private Parse parse;
	
	private MenuItem send, refresh;
	
	private Bundle bundle;
	private Message message;
	
	private String[] statuses_id, image_urls, screen_name, statuses_text, retweeted_statuses, pic_url1, pic_url2, reposts_comments;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		//inflater the layout 
	    View view = inflater.inflate(R.layout.home, null);
	    list = (ListView) view.findViewById(R.id.Home_List); 
		return view;	
	}
	
	@Override
	public void onStart(){
		super.onStart();
		 /*list.setOnItemClickListener(new OnItemClickListener(){

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
						long arg3) {
					// TODO Auto-generated method stub
					Intent mainIntent = new Intent(getActivity(), StatusesActivity.class);
					mainIntent.putExtra("statuses_id", statuses_id[arg2]);
					mainIntent.putExtra("profile_image", image_urls[arg2]);
					mainIntent.putExtra("screen_name", screen_name[arg2]);
					mainIntent.putExtra("statuses_text", statuses_text[arg2]);
					mainIntent.putExtra("retweeted_statuses", retweeted_statuses[arg2]);
					mainIntent.putExtra("pic_url1", pic_url1[arg2]);
					mainIntent.putExtra("pic_url2", pic_url2[arg2]);
					mainIntent.putExtra("reposts_comments", reposts_comments[arg2]);
					getActivity().startActivity(mainIntent);
				}
				
			});*/
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		initDisplayImageOptions();
		//getHomeTimeline();
		refreshUI();
		getStatuses();
	}

	private void getStatuses() {
		// TODO Auto-generated method stub
		DBOpenHelper dboHelper = new DBOpenHelper(this.getActivity());
		SQLiteDatabase db = dboHelper.getReadableDatabase();
		Cursor cursor = db.query("result", null, null, null, null, null, null);
		
		statuses_id = new String[50];
		image_urls = new String[50];
		screen_name = new String[50];
		statuses_text = new String[50];
		retweeted_statuses = new String[50];
		pic_url1 = new String[50];
		pic_url2 = new String[50];
		reposts_comments = new String[50];
		
		if(cursor.getCount() != 0){
			int i = 0;
		    while(cursor.moveToNext()){
		    	statuses_id[i] = cursor.getString(1);
		    	image_urls[i] = cursor.getString(2);
		    	screen_name[i] = cursor.getString(3);
		    	statuses_text[i] = cursor.getString(4);
		    	retweeted_statuses[i] = cursor.getString(5);
		    	pic_url1[i] = cursor.getString(6);
				pic_url2[i] = cursor.getString(7);
			    reposts_comments[i] = cursor.getString(8);
				Log.i("1", String.valueOf(i));
				i++;
				
		    }
		    
		    cursor.close();
		    bundle = new Bundle();
		    bundle.putStringArray("msg1",image_urls);
			bundle.putStringArray("msg2",screen_name);
			bundle.putStringArray("msg3",statuses_text);
			bundle.putStringArray("msg4",retweeted_statuses);
			bundle.putStringArray("msg5",pic_url1);
			bundle.putStringArray("msg6",pic_url2);
		    bundle.putStringArray("msg7",reposts_comments);
		    
			message = new Message();
			message.setData(bundle);
			message.what=0;
			handler.sendMessage(message);
			
		}else{
			getHomeTimeline();	
		}
		
	}

	private void refreshUI() {
		// TODO Auto-generated method stub
		//Fragment UI更新
		handler =new Handler(){
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
					list.setAdapter(new ItemAdapter(str1, str2, str3, str4, str5, str6, str7 ));
					break;
					
				default:
					break;
					}
				}
			};
			
	}

	private void getHomeTimeline() {
		// TODO Auto-generated method stub
		//微博SDK获取微博
		accessToken = AccessTokenKeeper.readAccessToken(getActivity());
	    new StatusesAPI(accessToken).homeTimeline(0, 0, 50, 1, false, FEATURE.ALL, false, new RequestListener(){
	    @Override
		public void onComplete(String arg0) {
	    	// TODO Auto-generated method stub
		    Log.i("sina", arg0);
		    parse = new Parse(this,arg0,getActivity());
			parse.start();
			
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

	private void initDisplayImageOptions() {
		// TODO Auto-generated method stub
		//imageloder 初始化
		options = new DisplayImageOptions.Builder()
		.showStubImage(R.drawable.ic_launcher)
	    //.showImageForEmptyUri(R.drawable.ic)
	    .showImageOnFail(R.drawable.ic_launcher)
	    .resetViewBeforeLoading(true)
	    .cacheInMemory(true)
		.cacheOnDisc(true)
		//.displayer(new RoundedBitmapDisplayer(10))
		.build();
		
	}
	
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //getSupportMenuInflater().inflate(R.menu.home, menu);
		super.onCreateOptionsMenu(menu, inflater);
		MenuItem send = menu.add("发微薄");
		MenuItem refresh = menu.add("刷新");
		
		send.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		refresh.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		
		send.setOnMenuItemClickListener(new OnMenuItemClickListener(){

			@Override
			public boolean onMenuItemClick(MenuItem item) {
				// TODO Auto-generated method stub
				/*FragmentTransaction fraTra = getFragmentManager().beginTransaction();
				fraTra.replace(R.id.main, new SendStatusesFragment());
				fraTra.commit();*/
				Intent mainIntent = new Intent(getActivity(),SendStatusesActivity.class);
				getActivity().startActivity(mainIntent);
				return false;
			}
			
		});
		
		refresh.setOnMenuItemClickListener(new OnMenuItemClickListener(){

			@Override
			public boolean onMenuItemClick(MenuItem item) {
				// TODO Auto-generated method stub
				getHomeTimeline();
				return false;
			}
			
		});
    }

	
	
	//异步加载图片 重写BaseAdapter自定义listview布局
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
				view = LayoutInflater.from(getActivity()).inflate(R.layout.home_image, parent, false);
				holder = new ViewHolder();
				holder.screen_name = (TextView) view.findViewById(R.id.screen_name);
				holder.text = (TextView) view.findViewById(R.id.text);
				holder.retweeted_status = (TextView) view.findViewById(R.id.retweeted_status);
				holder.reposts_comments = (TextView) view.findViewById(R.id.reposts_comments);
				holder.profile_image = (ImageView) view.findViewById(R.id.profile_image);
				holder.pic1 = (ImageView) view.findViewById(R.id.pic1);
				holder.pic2 = (ImageView) view.findViewById(R.id.pic2);
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
