package com.njucs.card.user;

import com.njucs.card.R;
import com.njucs.card.tools.BaseActivity;

import android.os.Bundle;

/*
 * 用户界面，由主界面跳转至此
 * 目前布局中只有“设置”和“关于”，还没添加响应函数，以后可以用更多功能丰富此界面
 * 2016-11-02
 */
public class User extends BaseActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		setContentView(R.layout.user);
	}
}
