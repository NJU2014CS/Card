package com.njucs.card.recognition;

import java.util.ArrayList;
import org.opencv.core.Mat;
import com.googlecode.tesseract.android.TessBaseAPI;
import com.njucs.card.main.MainActivity;
import android.graphics.Bitmap;

/*
 * 识别模块，传入Uri，传出联系人的一个对象
 * 识别之前先做一些图片处理工作。
 * 2016-11-02
 */
public class Recognition {
	
	static boolean english=true;
	static boolean test=true;
	
	public static String BitmapToText(){
		
		if(test==true){
			System.loadLibrary("opencv_java3");
			Process process=new Process();
			ArrayList<Mat> slices=process.Cut();
			return "test";
		}
		
		Bitmap bm=MainActivity.bitmap.copy(Bitmap.Config.ARGB_8888, true);
		
		TessBaseAPI baseApi=new TessBaseAPI();
		
		if(english==true){
			baseApi.init("/storage/emulated/0/1/", "eng");
		}
		else{
			baseApi.init("/storage/emulated/0/1/", "chi");
		}
		
		baseApi.setImage(bm);
		
		String text= baseApi.getUTF8Text();
		
		baseApi.clear();

		baseApi.end();
		
		return text;
	}
	
}
