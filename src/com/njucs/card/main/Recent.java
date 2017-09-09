package com.njucs.card.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import android.os.Environment;

/*
 * 每次处理在我们自定义的路径下保存一条处理记录，
 * 记录内容可以是一个联系人的数据结构
 * 程序开始运行会首先到这个路径读取所有记录，导出姓名和公司（有价值）在主页面显示
 * 2016-11-02
 */
public class Recent {
	
	private static List<String> data;
	
	public static List<String> getData(){
		checkDirectory();
		readRecentFile();
		return data;
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
		data.add(str);
		MainActivity.adapter.notifyDataSetChanged();
	}
	
	private static void readRecentFile(){
		File sd=Environment.getExternalStorageDirectory();
		String path=sd.getPath()+"/com.njucs.card/recent.txt";
		File recent=new File(path);
		if(!recent.exists()){
			try {
				recent.createNewFile();
				FileWriter out=new FileWriter(recent);
				out.write("赵\n钱\n孙\n李\n");
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		data=new ArrayList<String>();
		try {
			Scanner in = new Scanner(recent);
			while(in.hasNextLine()){
				data.add(in.nextLine());
			}
			in.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
		
	private static void checkDirectory(){
		File sd=Environment.getExternalStorageDirectory();
		String path=sd.getPath()+"/com.njucs.card";
		File dir=new File(path);
		if(!dir.exists()){
			dir.mkdir();
		}
	}

}
