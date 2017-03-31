package com.njucs.card.recognition;

import java.util.ArrayList;
import java.util.Date;

import org.opencv.android.Utils;
import org.opencv.core.Mat;
import com.googlecode.tesseract.android.TessBaseAPI;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.util.Log;

/*
 * 识别模块：
 * 识别之前先做一些图片处理工作。
 * 文字识别时使用图片处理模块返回的ArrayList<Mat>数据结构
 * 并以此转换为Bitmap进行识别
 * 2017-03-31
 */
public class Recognition {
	
	private String opencv_lib="opencv_java3";
	private boolean chinese=true;					// 中文支持
	private boolean multithreading=true;		// 多线程
	private ArrayList<Mat> slices;
	public ArrayList<String> texts;
	public TessBaseAPI baseApi;
	
	public Recognition(){
		texts=new ArrayList<String>();
		baseApi=new TessBaseAPI();
		if(chinese==true){
			baseApi.init("/storage/emulated/0/1/", "chi");
		}
		else{
			baseApi.init("/storage/emulated/0/1/", "eng");
		}	
		System.loadLibrary(opencv_lib);
	}
	
	public String getText(){
		Date start=new Date();
		
		// 对初始图片切割，分块进行识别，识别是更有针对性，正确率较高
		Process process=new Process();
		slices=process.Cut();
		
		for(int i=0;i<slices.size();i++){
			Mat mat=slices.get(i);
			if(multithreading==true){
				Thread task=new Thread(new HandleOneSlice(mat));
				task.run();
			}
			else{
				Bitmap bmp=Bitmap.createBitmap(mat.width(), mat.height(), Config.ARGB_8888);
				Utils.matToBitmap(mat, bmp);
				baseApi.setImage(bmp);
				String text=baseApi.getUTF8Text();
				texts.add(text);
				Log.i("Text", text);
				baseApi.clear();
			}
		}

		baseApi.end();
		Date end=new Date();
		Log.i("TimeConsuming", (end.getTime()-start.getTime())+"ms");
		
		StringBuffer text=new StringBuffer();
		for(String s:texts){
			text.append(s).append('\n');
		}
		return text.toString();
	}
	
	class HandleOneSlice implements Runnable{

		private Bitmap bmp;
		
		public HandleOneSlice(Mat mat) {
			bmp=Bitmap.createBitmap(mat.width(), mat.height(), Config.ARGB_8888);
			Utils.matToBitmap(mat, bmp);
		}
		
		@Override
		public void run() {	
			baseApi.setImage(bmp);
			String text=baseApi.getUTF8Text();
			texts.add(text);
			Log.i("Text", text);
			baseApi.clear();
		}
	}
	
}


