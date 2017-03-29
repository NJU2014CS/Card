package com.njucs.card.recognition;

import com.googlecode.leptonica.android.MorphApp;
import com.googlecode.leptonica.android.Pix;
import com.googlecode.leptonica.android.ReadFile;
import com.googlecode.leptonica.android.WriteFile;
import com.njucs.card.main.MainActivity;

import android.graphics.Bitmap;

public class Process {

	static Bitmap bmp;
	
	static Pix pixs;
	
	public static void Test(){
		bmp=MainActivity.bitmap;
		
		pixs=ReadFile.readBitmap(bmp);
		
		pixs=MorphApp.pixFastTophatBlack(pixs);
//		pixs=Convert.convertTo8(pixs);

//		pixs=Binarize.sauvolaBinarizeTiled(pixs);
//		pixs=GrayQuant.pixThresholdToBinary(pixs, 110);
		
		bmp=WriteFile.writeBitmap(pixs);
		
		MainActivity.bitmap=bmp;	
	}
}
