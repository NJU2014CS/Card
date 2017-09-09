package com.njucs.card.contact;

import java.util.ArrayList;

import com.njucs.card.main.MainActivity;
import android.util.Log;

public class AipNlp {
	
	public static ArrayList<String> results;
	public static int n;
	
	public static void lexer(String info) {
		results=new ArrayList<String>();
		n=info.split("\n").length;
		
		for(final String s:info.split("\n")){
			new Thread(){
				@Override
				public void run(){
					String lexer_Url="https://aip.baidubce.com/rpc/2.0/nlp/v1/lexer";
					String params="{\"text\": \""+s+"\"}";
					try {
						results.add(HttpUtil.post(lexer_Url, MainActivity.accessToken, params));
						ContactActivity.handler.sendEmptyMessage(0);
					} catch (Exception e) {
						Log.i("AipNlp", e.toString());
					}
				}
			}.start();
		}
		
	}
	
}
