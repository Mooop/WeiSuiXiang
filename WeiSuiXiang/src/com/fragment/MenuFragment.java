package com.fragment;



import com.example.weisuixiang.HomeActivity;
import com.example.weisuixiang.R;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MenuFragment extends Fragment{
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {	 
		//inflater the layout 
	    View view = inflater.inflate(R.layout.menu, null);
	    ListView list = (ListView)view.findViewById(R.id.menu_list);
	    String[] data = new String[]{"首页", "我", "选项"};
	    ArrayAdapter<String> adapter = new ArrayAdapter<String>(
	    		getActivity(),android.R.layout.simple_list_item_1, data);
	    list.setAdapter(adapter);
	    list.setOnItemClickListener(new OnItemClickListener(){
			@TargetApi(Build.VERSION_CODES.HONEYCOMB)
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				
				((HomeActivity)getActivity()).getSlidingMenu().toggle();
				
				if(arg2 == 0){
					getActivity().setTitle("Wei随享");
					FragmentTransaction fraTra = getFragmentManager().beginTransaction();
					fraTra.replace(R.id.main, new HomeFragment());
					fraTra.commit();
					}
				
				if(arg2 == 1){
					getActivity().setTitle("我");
					FragmentTransaction fraTra = getFragmentManager().beginTransaction();
					fraTra.replace(R.id.main, new AboutFragment());
					fraTra.commit();
					}
				
				if(arg2 == 2){
					getActivity().setTitle("选项");
					FragmentTransaction fraTra = getFragmentManager().beginTransaction();
					fraTra.replace(R.id.main, new OptionFragment());
					fraTra.commit();
					}
				}
			});
	    return view;
	    }
	
}
