package com.fragment;

import com.example.weisuixiang.R;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

@SuppressLint("ValidFragment")
public class Content extends Fragment{
	
	private String str;
	
	public Content(){}
	
	public Content(String str){
		this.str = str;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {	 
		//inflater the layout 
	    View view = inflater.inflate(R.layout.content, null);
	    TextView t = (TextView) view.findViewById(R.id.text);
	    t.setText(str);
	    return view;
	    }
	
}
