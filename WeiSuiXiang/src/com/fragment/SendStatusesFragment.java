package com.fragment;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuItem.OnMenuItemClickListener;
import com.example.weisuixiang.R;
import com.util.AccessTokenKeeper;
import com.weibo.sdk.android.Oauth2AccessToken;
import com.weibo.sdk.android.WeiboException;
import com.weibo.sdk.android.api.StatusesAPI;
import com.weibo.sdk.android.net.RequestListener;

@SuppressLint({ "NewApi", "HandlerLeak", "ShowToast" })
public class SendStatusesFragment extends SherlockFragment{
	
	private EditText editStatuses;
	
	private Message message;
	private Bundle bundle;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		//getSherlockActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
		
		bundle = new Bundle();
	    message = new Message();
		
		Timer timer = new Timer(); //设置定时器
		timer.schedule(new TimerTask() {
		
		@Override
		public void run() { //弹出软键盘的代码
				InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
				imm.showSoftInput(editStatuses, InputMethodManager.RESULT_SHOWN);
				imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
			}
		}, 200); //设置200毫秒的时长
	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		//inflater the layout 
	    View view = inflater.inflate(R.layout.send_statuses, null);
	    editStatuses = (EditText) view.findViewById(R.id.edit_statuses);
		return view;	
	}
	
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //getSupportMenuInflater().inflate(R.menu.home, menu);
		super.onCreateOptionsMenu(menu, inflater);
		
		MenuItem cancle = menu.add("取消");
		MenuItem send = menu.add("发布");
		
		send.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		cancle.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		
		send.setOnMenuItemClickListener(new OnMenuItemClickListener(){

			@Override
			public boolean onMenuItemClick(MenuItem item) {
				// TODO Auto-generated method stub
				Oauth2AccessToken accessToken = AccessTokenKeeper.readAccessToken(getActivity());
				new StatusesAPI(accessToken).update(editStatuses.getText().toString(), null, null, new RequestListener(){

					@Override
					public void onComplete(String arg0) {
						// TODO Auto-generated method stub
						Log.i("sina", arg0);
						InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE); 
				        imm.hideSoftInputFromWindow(editStatuses.getWindowToken(),0);
				        
						FragmentTransaction fraTra = getFragmentManager().beginTransaction();
						fraTra.replace(R.id.main, new HomeFragment());
						fraTra.commit();
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
				return false;
			}
			
		});
		
		cancle.setOnMenuItemClickListener(new OnMenuItemClickListener(){

			@Override
			public boolean onMenuItemClick(MenuItem item) {
				// TODO Auto-generated method stub
				
				InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE); 
		        imm.hideSoftInputFromWindow(editStatuses.getWindowToken(),0);
				
				FragmentTransaction fraTra = getFragmentManager().beginTransaction();
				fraTra.replace(R.id.main, new HomeFragment());
				fraTra.commit();
				return false;
			}
			
		});
		
	}
}
