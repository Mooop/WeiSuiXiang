package com.example.weisuixiang;

import com.actionbarsherlock.view.MenuItem;
import com.example.weisuixiang.R;
import com.fragment.HomeFragment;
import com.fragment.MenuFragment;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.annotation.SuppressLint;
import android.graphics.Color;


@SuppressLint("NewApi")
public class HomeActivity extends SlidingFragmentActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle("Wei随享");
		setContentView(R.layout.main);
		//设置菜单布局
		setBehindContentView(R.layout.menu);
		
		initView();
		initSlidingMenu();
		initFragment();
	}

	//初始化主界面
	private void initFragment() {
		// TODO Auto-generated method stub
		FragmentTransaction fraTra = getSupportFragmentManager().beginTransaction();
		MenuFragment menuFragment = new MenuFragment();
		fraTra.replace(R.id.menu, menuFragment);
		fraTra.replace(R.id.main, new HomeFragment());
		fraTra.commit();
	}

	//初始化视图
	private void initView() {
		// TODO Auto-generated method stub
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setIcon(getResources().getDrawable(R.drawable.settng));
		getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.ab_bg_green));
		
	}
	
	//初始化Slidingmenu
	@SuppressWarnings("deprecation")
	private void initSlidingMenu() {
		// TODO Auto-generated method stub
		
		SlidingMenu menu = getSlidingMenu();
		
		// 设置滑动方向
		menu.setMode(SlidingMenu.LEFT);
				
		// 设置监听开始滑动的触碰范围
		menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
				
		// 设置menu全部打开后，主界面剩余部分与屏幕边界的距离，通过dimens资源文件ID设置
		menu.setBehindOffset(getWindowManager().getDefaultDisplay().getWidth() / 2);
				
		// 设置是否淡入淡出
		menu.setFadeEnabled(true);
				
		// 设置淡入淡出的值，只在setFadeEnabled设置为true时有效
		menu.setFadeDegree(0.35f);
				
		// 设置menu的背景
		menu.setBackgroundColor(Color.LTGRAY);
		
		//设置阴影
		menu.setShadowDrawable(R.drawable.shadow);
		menu.setShadowWidthRes(R.dimen.shadow_width);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		 switch (item.getItemId()) {
		 case android.R.id.home:
			 //toggle就是程序自动判断是打开还是关闭
			 toggle();
			 //getSlidingMenu().showMenu();// show menu
			 //getSlidingMenu().showContent();//show content
			 return true;
			 
		 default:
			 return super.onOptionsItemSelected(item);
			 
		 }
		 
	 }

}
