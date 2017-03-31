package com.njucs.card.recognition;

import java.util.ArrayList;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import com.njucs.card.main.MainActivity;

import android.graphics.Bitmap;

/*
 * 文字识别之前的图片处理：
 * 
 */

public class Process {
	
	private Bitmap original_picture;
	
	public Process(){
		original_picture=MainActivity.bitmap.copy(Bitmap.Config.ARGB_8888, true);
	}
	
	public ArrayList<Mat> Cut(){
		Mat img=new Mat();
		Utils.bitmapToMat(original_picture, img);
/*		
		Mat gray_x=new Mat();
		Imgproc.Sobel(img, gray_x, CvType.CV_16S, 1, 0, 3, 1, 1);
		Core.convertScaleAbs(gray_x, gray_x);
		
		Mat binary=new Mat();
		binary.create(img.size(), img.type());
		Mat edge=new Mat();
		Imgproc.blur(gray_x, edge, new Size(3, 3));
		Imgproc.threshold(edge, binary, 150, 255, Imgproc.THRESH_BINARY);
*/
		return null;
	}

}
