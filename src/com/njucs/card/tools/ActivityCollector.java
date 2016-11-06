package com.njucs.card.tools;

import java.util.ArrayList;
import android.app.Activity;
/*
 * 活动收集器，添加活动删除活动均会改变列表activities
 * 提供函数finishAll()，可以在任意需要的地方销毁所有活动，结束程序
 * 2016-11-02
 */
public class ActivityCollector {
	
	public static ArrayList<Activity> activities = new ArrayList<Activity>();

	public static void addActivity(Activity activity) {
		activities.add(activity);
	}

	public static void removeActivity(Activity activity) {
		activities.remove(activity);
	}

	public static void finishAll() {
		for (Activity activity : activities) {
			if (!activity.isFinishing()) {
				activity.finish();
			}
		}
	}
}
