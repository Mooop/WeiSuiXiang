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
		setTitle("Wei����");
		setContentView(R.layout.main);
		//���ò˵�����
		setBehindContentView(R.layout.menu);
		
		initView();
		initSlidingMenu();
		initFragment();
	}

	//��ʼ��������
	private void initFragment() {
		// TODO Auto-generated method stub
		FragmentTransaction fraTra = getSupportFragmentManager().beginTransaction();
		MenuFragment menuFragment = new MenuFragment();
		fraTra.replace(R.id.menu, menuFragment);
		fraTra.replace(R.id.main, new HomeFragment());
		fraTra.commit();
	}

	//��ʼ����ͼ
	private void initView() {
		// TODO Auto-generated method stub
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setIcon(getResources().getDrawable(R.drawable.settng));
		getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.ab_bg_green));
		
	}
	
	//��ʼ��Slidingmenu
	@SuppressWarnings("deprecation")
	private void initSlidingMenu() {
		// TODO Auto-generated method stub
		
		SlidingMenu menu = getSlidingMenu();
		
		// ���û�������
		menu.setMode(SlidingMenu.LEFT);
				
		// ���ü�����ʼ�����Ĵ�����Χ
		menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
				
		// ����menuȫ���򿪺�������ʣ�ಿ������Ļ�߽�ľ��룬ͨ��dimens��Դ�ļ�ID����
		menu.setBehindOffset(getWindowManager().getDefaultDisplay().getWidth() / 2);
				
		// �����Ƿ��뵭��
		menu.setFadeEnabled(true);
				
		// ���õ��뵭����ֵ��ֻ��setFadeEnabled����Ϊtrueʱ��Ч
		menu.setFadeDegree(0.35f);
				
		// ����menu�ı���
		menu.setBackgroundColor(Color.LTGRAY);
		
		//������Ӱ
		menu.setShadowDrawable(R.drawable.shadow);
		menu.setShadowWidthRes(R.dimen.shadow_width);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		 switch (item.getItemId()) {
		 case android.R.id.home:
			 //toggle���ǳ����Զ��ж��Ǵ򿪻��ǹر�
			 toggle();
			 //getSlidingMenu().showMenu();// show menu
			 //getSlidingMenu().showContent();//show content
			 return true;
			 
		 default:
			 return super.onOptionsItemSelected(item);
			 
		 }
		 
	 }

}
