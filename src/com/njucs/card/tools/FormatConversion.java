package com.njucs.card.tools;

import java.io.ByteArrayOutputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.util.Log;


/*
 * 
 * 因为Intent无法传递Bitmap类型的数据，因而需要转换成Byte数组
 * 这个类就是用于Bitmap和Byte数组的互相转换
 * Contacts到byte数组的转换暂时还没写
 * 
 */



public class FormatConversion {
	public static byte[] Bitmap2Bytes(Bitmap bm){
		ByteArrayOutputStream baos=new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
		Log.i("String", "Byte2Bitmap");
		return baos.toByteArray();
	}
	
	public static byte[] Drawable2Bytes(Drawable da){
		if(da==null){
			return null;
		}
		Bitmap bm=Drawable2Bitmap(da);
		
		return Bitmap2Bytes(bm);
	}
	
	public static Bitmap Drawable2Bitmap(Drawable da){
		Bitmap bm=Bitmap.createBitmap(
				da.getIntrinsicWidth(),
				da.getIntrinsicHeight(),
				da.getOpacity()!=PixelFormat.OPAQUE?Bitmap.Config.ARGB_8888:Bitmap.Config.RGB_565);
		Canvas canvas=new Canvas(bm);
		da.setBounds(0,0,da.getIntrinsicWidth(),da.getIntrinsicHeight());
		da.draw(canvas);
		return bm;
	}
	
	public static Bitmap Bytes2Bitmap(byte[] b){
		return BitmapFactory.decodeByteArray(b, 0, b.length);
	}
}
