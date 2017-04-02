package com.njucs.card.user;

import com.njucs.card.R;
import com.njucs.card.connection.TestConnect;
import com.njucs.card.tools.BaseActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

/*
 * 用户界面，由主界面跳转至此
 * 目前布局中只有“设置”和“关于”，还没添加响应函数，以后可以用更多功能丰富此界面
 * 2016-11-02
 */
public class User extends BaseActivity {
	
	private ImageView TestConnectView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		setContentView(R.layout.user);
		
		TestConnectView=(ImageView)findViewById(R.id.TestConnectView);
		
		TestConnectView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(User.this,TestConnect.class);
				startActivity(intent);
			}
		});
		
		
	}
}
