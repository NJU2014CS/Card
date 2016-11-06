package com.njucs.card.tools;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

/*
 * 先继承一次基类Activity，以后所有的活动均继承BaseActivity
 * 方便活动的管理，调试
 * 2016-11-02
 */
public class BaseActivity extends Activity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d("BaseActivity", getClass().getSimpleName());
		ActivityCollector.addActivity(this);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		ActivityCollector.removeActivity(this);
	}

}
