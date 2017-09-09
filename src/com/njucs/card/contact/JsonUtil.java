package com.njucs.card.contact;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class JsonUtil {

	public static void parsing(String raw){
		Log.i("JsonParsing", raw);
		try {
			JSONObject log=new JSONObject(raw);
			String text=log.getString("text");
			JSONArray items=log.getJSONArray("items");
			for(int i=0;i<items.length();i++){
				String ne=items.getJSONObject(i).getString("ne");
				if(ne.equals("PER"))
					ContactActivity.getContact().setName(text);
				else if(ne.equals("ORG"))
					ContactActivity.getContact().setCompany(text);
				else if(ne.equals("LOC"))
					ContactActivity.getContact().setAddress(text);
				else
					processing(text);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	public static void processing(String text){
		if(text.contains("电话"))
			ContactActivity.getContact().setTelephone(text);
		if(text.contains("传真"))
			ContactActivity.getContact().setFax(text);
		if(text.contains("网址"))
			ContactActivity.getContact().setUrl(text);
		if(text.contains("邮箱"))
			ContactActivity.getContact().setMail(text);
		if(text.contains("邮编")||text.contains("编"))
			ContactActivity.getContact().setPostcode(text);
		if(text.matches("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$")||text.contains("手机")){
			ContactActivity.getContact().setMobilephone(text);
		}
	}
}
