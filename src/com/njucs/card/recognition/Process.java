package com.njucs.card.recognition;

import java.util.ArrayList;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Rect;
import org.opencv.core.RotatedRect;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import com.njucs.card.main.MainActivity;

import android.graphics.Bitmap;

/*
 * 文字识别之前的图片处理：
 * 图片分块，切割
 */
public class Process {
	
	private Bitmap original_picture;
	
	public Process(){
		// 从MainActivity中获取共享位图
		original_picture=MainActivity.bitmap.copy(Bitmap.Config.ARGB_8888, true);
	}
	
	public ArrayList<Mat> Cut(){
		Mat img=new Mat();
		Utils.bitmapToMat(original_picture, img);
		
		Mat gray_x=new Mat(), binary=new Mat(), edge=new Mat();
		//Sobel算子，x方向求梯度，
		Imgproc.Sobel(img, gray_x, CvType.CV_16S, 1, 0, 3, 1, 1);
		//根据计算出的grad_x，求大小合适的梯度，这里似乎是求了平均情况
		Core.convertScaleAbs(gray_x, gray_x);
		
		binary.create(img.size(), img.type());
		//blur根据求出的梯度滤波
		Imgproc.blur(gray_x, edge, new Size(3, 3));
		//二值化
		Imgproc.threshold(edge, binary, 150, 255, Imgproc.THRESH_BINARY);

		Mat element1, element2;
		//膨胀和腐蚀操作的核函数
		element1=Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(18,5));
		element2=Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(28,7));
		
		Mat trans=new Mat();
		//膨胀操作
		Imgproc.dilate(binary, trans, element1);
		binary=trans;
		//腐蚀操作
		Imgproc.erode(binary, trans,element2);
		binary=trans;
		Imgproc.dilate(binary, trans, element1);
		
		ArrayList<MatOfPoint> contours = new ArrayList<MatOfPoint>();
        Mat hierarchy = new Mat();
        Mat tran=new Mat();
        // 格式转换
        Imgproc.cvtColor(trans, tran, Imgproc.COLOR_RGBA2GRAY);
        //寻找白色区域的轮廓，将轮廓存储
        Imgproc.findContours(tran, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_NONE);
        for(int i=0;i<contours.size();i++){
        	//计算每一个轮廓的大小，舍去太小的轮廓
        	double area=Imgproc.contourArea(contours.get(i));
        	if(area<100){
        		contours.remove(i);
        		i--;
        	}
        }
        
        ArrayList<Rect> list=new ArrayList<Rect>();
        for(int i=0;i<contours.size();i++){
        	//寻找可以包含轮廓的最小矩形
        	RotatedRect p=Imgproc.minAreaRect(new MatOfPoint2f(contours.get(i).toArray()));
        	//类型转换
        	Rect rect=p.boundingRect();
        	//横向切割，如果矩形的宽远大于长，认为不合理，舍去
        	if(rect.height>2*rect.width){
        		continue;
        	}
        	list.add(rect);
        }

        ArrayList<Mat> rst=new ArrayList<Mat>();
      //复制每个矩形框内的图片，实现切割效果。
        for(int i=list.size()-1;i>=0;i--){
        	Mat mat=new Mat(img, list.get(i));
        	rst.add(mat);
        }
        
		return rst;
	}

}
