package com.njucs.card.record;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import com.njucs.card.main.MainActivity;
import android.os.Environment;
import android.util.Log;

/*
 * 每次处理在我们自定义的路径下保存一条处理记录，
 * 记录内容可以是一个联系人的数据结构
 * 程序开始运行会首先到这个路径读取所有记录，导出姓名和公司（有价值）在主页面显示
 * 2016-11-02
 */
public class Recent {
	
	private static List<String> data;
	private static List<Map<String, String>> info;
	
	/*public static void init(){
		for(int i=0;i<)
	}*/
	
	public static List<String> getData(){
		checkDirectory();
		readRecentFile();
		return data;
	}
	
	public static Map<String,String> getMetaData(int position){
		if(position<0||position>=info.size())
			return null;
		else
			return info.get(position);
	}
	
	public static void SetMetaData(int position, Map<String,String> m){
		if(position<0||position>=info.size())
			return;
		else
			info.set(position, m);
	}
	
	public static void write(String str){
		File recent=new File(Environment.getExternalStorageDirectory().getPath()+"/com.njucs.card/recent.txt");
		try {
			FileWriter out = new FileWriter(recent,true);
			out.write(str+"\n");
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Map<String,String> m=GenerateMap(str);
		data.add(m.get("name"));
		info.add(m);
		MainActivity.adapter.notifyDataSetChanged();
	}
	
	public static void save(){
		StringBuilder sb=new StringBuilder();
		for(Map<String,String> buf:info){
			sb.append(GetString(buf)+"\n");
		}
		File recent=new File(Environment.getExternalStorageDirectory().getPath()+"/com.njucs.card/recent.txt");
		try {
			FileWriter out = new FileWriter(recent);
			out.write(sb.toString());
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static void readRecentFile(){
		File sd=Environment.getExternalStorageDirectory();
		String path=sd.getPath()+"/com.njucs.card/recent.txt";
		File recent=new File(path);
		data=new ArrayList<String>();
		info=new ArrayList<Map<String,String>>();
		if(!recent.exists()){
			return;
		}
		else{
			try {
				Scanner in = new Scanner(recent);
				while(in.hasNextLine()){
					String s=in.nextLine();
					if(s.contains("\uFEFF"))
						s=s.replaceAll("\uFEFF", "");
					Map<String,String> m=GenerateMap(s);
					data.add(m.get("name"));
					info.add(m);
					//data.add(in.nextLine());
				}
				in.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void remove(int position){
		data.remove(position);
		info.remove(position);
	}
		
	private static void checkDirectory(){
		File sd=Environment.getExternalStorageDirectory();
		String path=sd.getPath()+"/com.njucs.card";
		File dir=new File(path);
		if(!dir.exists()){
			dir.mkdir();
		}
	}
	
	public static Map<String,String> GenerateMap(String str){
		Map<String,String> buf=new HashMap<String,String>();
		String[] buffer=str.split("\\s");
		for(String s:buffer){
			String[] b=s.split(":");
			if(b.length<2){
				Log.d("String", s);
				continue;
			}
			buf.put(b[0], b[1]);
		}
		return buf;
	}
	
	public static String GetString(Map<String,String> map){
		Set<String> set=map.keySet();
		StringBuilder sb=new StringBuilder();
		for(String k:set){
			sb.append(k+":"+map.get(k)+" ");
		}
		return sb.toString();
	}
	
	public static List<Integer> SearchContent(String key){
		List<Integer> res=new ArrayList<Integer>();
		for(int i=0;i<data.size();i++){
			if(data.get(i).startsWith(key))
				res.add(i);
		}
		return res;
	}
	
	public static List<String> GetResult(List<Integer> list){
		List<String> res=new ArrayList<String>();
		for(int i:list){
			res.add(data.get(i));
		}
		return res;
	}
	
	public static List<Integer> GetIndex(){
		List<Integer> res=new ArrayList<Integer>();
		for(int i=0;i<data.size();i++){
			res.add(i);
		}
		return res;
	}

}
