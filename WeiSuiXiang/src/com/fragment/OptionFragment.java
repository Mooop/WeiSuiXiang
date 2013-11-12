package com.fragment;

import com.example.weisuixiang.LoginActivity;
import com.example.weisuixiang.R;
import com.util.AccessTokenKeeper;
import com.util.DBOpenHelper;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.Button;
import android.widget.Toast;

public class OptionFragment extends Fragment{
	
	private Button logout;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {	 
		//inflater the layout 
	    View view = inflater.inflate(R.layout.option, null);
	    logout = (Button) view.findViewById(R.id.logout);
	    logout.setBackgroundColor(Color.RED);
	    logout.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//Çå³ýccesstoken
				AccessTokenKeeper.clear(getActivity());
				//Çå³ý»º´æ
				CookieSyncManager.createInstance(getActivity()); 
				CookieManager cookieManager = CookieManager.getInstance();
				cookieManager.removeAllCookie();
				
				DBOpenHelper dboHelper = new DBOpenHelper(getActivity());
				SQLiteDatabase sqlite = dboHelper.getWritableDatabase();
		    	sqlite.delete("result", null, null);
				
		    	Toast.makeText(getActivity(), "ÒÑ×¢ÏúÎ¢²©ÇëÖØÐÂµÇÂ¼", Toast.LENGTH_SHORT).show();
				
		    	Intent mainIntent = new Intent(getActivity(),LoginActivity.class);
				getActivity().startActivity(mainIntent);
				getActivity().finish();
			}
	    	
	    });
	    return view;
	}
}
