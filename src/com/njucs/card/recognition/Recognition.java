package com.njucs.card.recognition;

import com.googlecode.tesseract.android.TessBaseAPI;
import com.njucs.card.main.MainActivity;
import android.graphics.Bitmap;

/*
 * 识别模块，传入Uri，传出联系人的一个对象
 * 2016-11-02
 */
public class Recognition {
	
	static boolean english=true;
	
	public static String BitmapToText(){
//		Process.Test();
		
		Bitmap bm=MainActivity.bitmap;
		
		TessBaseAPI baseApi=new TessBaseAPI();
		
		if(english){
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
