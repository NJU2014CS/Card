package com.njucs.card.recognition;

import java.io.File;
import java.util.ArrayList;

import com.baidu.ocr.sdk.OCR;
import com.baidu.ocr.sdk.OnResultListener;
import com.baidu.ocr.sdk.exception.OCRError;
import com.baidu.ocr.sdk.model.GeneralParams;
import com.baidu.ocr.sdk.model.GeneralResult;
import com.baidu.ocr.sdk.model.Location;
import com.baidu.ocr.sdk.model.Word;
import com.baidu.ocr.sdk.model.WordSimple;
import com.njucs.card.contact.ContactActivity;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;

public class BaiduOCR {
	private String info;
	private Activity activity;
	public static ArrayList<Location> locations;
	
	public BaiduOCR(Activity activity) {
		this.activity=activity;
	}
	
	public void recognize(String path){
		info=new String();
		locations=new ArrayList<Location>();
		// 通用文字识别参数设置
		GeneralParams param = new GeneralParams();
		param.setDetectDirection(true);
		param.setImageFile(new File(path));

		// 调用通用文字识别服务
		OCR.getInstance().recognizeGeneral(param, new OnResultListener<GeneralResult>() {
		    @Override
		    public void onResult(GeneralResult result) {
		        // 调用成功，返回GeneralResult对象
		        for (WordSimple wordSimple : result.getWordList()) {
		            // Word类包含位置信息
		            Word word = (Word) wordSimple;
		            info+=word.getWords()+"\n";
		            locations.add(word.getLocation());
		        }
		        Log.i("BaiduOCR", info);
		        callContactActivity(info);
		    }
		    @Override
		    public void onError(OCRError error) {
		       Log.e("BaiduOCR", error.getMessage());
		    }
		});
	}
	
	private void callContactActivity(String info){
		Intent intent=new Intent(activity, ContactActivity.class);
		intent.putExtra("Info", info);
		activity.startActivity(intent);
	}
}
