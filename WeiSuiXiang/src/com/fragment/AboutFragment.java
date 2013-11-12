package com.fragment;

import java.io.IOException;

import org.json.JSONObject;

import com.example.weisuixiang.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.util.AccessTokenKeeper;
import com.util.SharePre;
import com.weibo.sdk.android.Oauth2AccessToken;
import com.weibo.sdk.android.WeiboException;
import com.weibo.sdk.android.api.UsersAPI;
import com.weibo.sdk.android.net.RequestListener;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class AboutFragment extends Fragment{
	
	private TextView screen_name , location , description , statuses;
	private ImageView profile_image , thumbnail_pic;
	private Button statuses_count , friends_count , followers_count;
	
	public static Oauth2AccessToken accessToken;
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	private DisplayImageOptions options;
	
	private Handler handler;
	private String[] arr;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initDisplayImageOptions();
		getUserInfor();
		
		handler =new Handler(){
			public void handleMessage(Message msg){
				switch (msg.what) {
				case 0:
					Bundle b = msg.getData();
					String[] str = b.getStringArray("msg");
					screen_name.setText("êÇ³Æ£º" + str[0]);
					location.setText("ËùÔÚµØ£º" + str[1]);
					description.setText("¼ò½é£º" + str[2]);
					statuses_count.setText(str[3] + "\n" + "Î¢²©");
					friends_count.setText(str[4] + "\n" + "¹Ø×¢");
					followers_count.setText(str[5] + "\n" + "·ÛË¿");
					imageLoader.displayImage(arr[6], profile_image);
					statuses.setText("Î¢²©" + "(" + str[3] + ")" + "\n" + str[0] + "\n" + arr[7]);
					imageLoader.displayImage(arr[8], thumbnail_pic);
					break;

				default:
					break;
				}
				
			}
			
		};
		
	}
	
	private void getUserInfor() {
		// TODO Auto-generated method stub
		accessToken = AccessTokenKeeper.readAccessToken(getActivity());
		new UsersAPI(accessToken).show(SharePre.readUID(getActivity()), new RequestListener(){

			@Override
			public void onComplete(String arg0) {
				// TODO Auto-generated method stub
				Log.i("sina", arg0);
				new ParseJson(getActivity(), this, arg0).start();
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
		options = new DisplayImageOptions.Builder()
		.showStubImage(R.drawable.ic_launcher)
		.showImageForEmptyUri(R.drawable.ic_launcher)
		.showImageOnFail(R.drawable.ic_launcher)
		.cacheInMemory(true)
		.cacheOnDisc(true)
		//.displayer(new RoundedBitmapDisplayer(10))
		.build();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {	 
		//inflater the layout 
		View view = inflater.inflate(R.layout.about, null);
		profile_image = (ImageView) view.findViewById(R.id.profile_image);
		thumbnail_pic = (ImageView) view.findViewById(R.id.thumbnail_pic);
		screen_name = (TextView) view.findViewById(R.id.screen_name);
		location = (TextView) view.findViewById(R.id.location);
		description = (TextView) view.findViewById(R.id.description);
		statuses = (TextView) view.findViewById(R.id.statuses);
		statuses_count = (Button) view.findViewById(R.id.statuses_count);
		friends_count = (Button) view.findViewById(R.id.friends_count);
		followers_count = (Button) view.findViewById(R.id.followers_count);
		return view;
	}
	
	class ParseJson extends Thread{
		private FragmentActivity fragmentActivity;
		private RequestListener requestListener;
		private String arg0;
		
		public ParseJson(FragmentActivity fragmentActivity, RequestListener requestListener, String arg0){
			this.fragmentActivity = fragmentActivity;
			this.requestListener = requestListener;
			this.arg0 = arg0;
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
				
				Bundle bundle = new Bundle();
				bundle.putStringArray("msg",arr);
			    Message message = new Message();
			    message.setData(bundle);
			    message.what=0;
			    handler.sendMessage(message);
			    }catch(Exception e) {
			    	// TODO Auto-generated catch block
			    	e.printStackTrace();
			    	
			    }
			
		}
		
	}
}
