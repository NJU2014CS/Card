package com.njucs.card.main;

import com.baidu.ocr.sdk.OCR;
import com.baidu.ocr.sdk.OnResultListener;
import com.baidu.ocr.sdk.exception.OCRError;
import com.baidu.ocr.sdk.model.AccessToken;
import com.njucs.card.R;
import com.njucs.card.initializtion.GetRecentCard;
import com.njucs.card.recognition.BaiduOCR;
import com.njucs.card.tools.BaseActivity;
import com.njucs.card.tools.web.WebTest;
import com.njucs.card.user.User;

import java.io.*;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;

/*
 * 主界面：包括最近扫描结果组成的一个列表ListView::recent和下方四个ImageButton
 * 分别是：名片夹（没什么用），相册，照相，用户
 * 相册和照相的响应函数已完成，会在onActivityResult()里得到一个图片的Uri进而处理得到Bitmap传入识别模块
 * 
 * 对于最近处理列表还需要对每一项添加响应函数，点击后跳转到我们自定义的一个联系人界面（未设计）进行浏览
 * 2016-11-02
 */
public class MainActivity extends BaseActivity{
	static final int ALBUM=1, CAMERA=2, CROP=3;
	// 操作按钮
	private ImageButton album, camera, user;
	// 最近处理的联系人列表
	private ListView recent;
	// 从相册得到的或者拍照得到的照片的URI保存在imageUri里。
	private Uri imageUri;
	// 共享位图
	public static Bitmap bitmap;
	// 图片切割工具
	private CutPictureUtils cutPictureUtils=new CutPictureUtils(MainActivity.this);
	// 百度OCR
	private BaiduOCR baiduOCR=new BaiduOCR(MainActivity.this);
	
	@Override
 	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);	
		
		// 获取版本号
		int version=android.os.Build.VERSION.SDK_INT;
		Log.i("MainActivity", "SystemVersion:"+version+"");
		
		// 几个控件的点击响应函数
		onAlbum();		
		onCamera();
		onUser();
		onRecent();
		
		initAccessTokenWithAkSk();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// 没有选取或者拍摄照片，resultCode=0，直接返回。
		if(resultCode!=RESULT_OK)
			return ;
		
		if(requestCode==ALBUM){
			if(data!=null){
				Uri uri=data.getData();
				imageUri=cutPictureUtils.crop(uri);
			}
		}
		else if(requestCode==CAMERA){
			Uri uri=imageUri;
			imageUri=null;
			imageUri=cutPictureUtils.crop(uri);
		}
		else if(requestCode==cutPictureUtils.CROP){
			if(imageUri!=null){
				bitmap=cutPictureUtils.decodeUriAsBitmap(imageUri);

				baiduOCR.recognize(imageUri.getPath());
				
				File file=new File(imageUri.getPath());
				if(file.exists()){
					boolean success=file.delete();
					Log.i("MainActivity", "Delete the temp picture "+(success==true?"success":"failed"));
				}
			}
			else{
				Log.e("MainActivity", "Crop imageUri is null");
			}
		}
		else{
			;
		}
	}
		
	private void onAlbum(){
		album=(ImageButton)findViewById(R.id.btn_album);
		
		album.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.i("MainActivity", "Click Album");
				Intent intent = new Intent(Intent.ACTION_PICK);
				intent.setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
				startActivityForResult(intent, ALBUM);     
			}
		});
		
		album.setOnTouchListener(new OnTouchListener() {
			@SuppressLint("ClickableViewAccessibility")
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(event.getAction() == MotionEvent.ACTION_DOWN)
					album.setImageDrawable(getResources().getDrawable(R.drawable.album_press));		
				if(event.getAction() == MotionEvent.ACTION_UP)
					album.setImageDrawable(getResources().getDrawable(R.drawable.album));		
				return false;
			}
		});
	}
	
	private void onCamera(){
		camera=(ImageButton)findViewById(R.id.btn_camera);
		
		camera.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.i("MainActivity", "Click Camrea");
				// 拍照得到的照片存储在根目录下的temp.jpg
				File outputImage = new File(Environment.getExternalStorageDirectory(), "temp.jpg");
				try {
					if (outputImage.exists()) {
						outputImage.delete();
					}
					outputImage.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
				// 获取照片的URI
				imageUri = Uri.fromFile(outputImage);
				Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
				intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
				startActivityForResult(intent, CAMERA);
			}
		});
		
		camera.setOnTouchListener(new OnTouchListener() {		
			@SuppressLint("ClickableViewAccessibility")
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(event.getAction() == MotionEvent.ACTION_DOWN)
					camera.setImageDrawable(getResources().getDrawable(R.drawable.camera_press));		
				if(event.getAction() == MotionEvent.ACTION_UP)
					camera.setImageDrawable(getResources().getDrawable(R.drawable.camera));	
				return false;
			}
		});
	}
	
	private void onUser(){
		user=(ImageButton)findViewById(R.id.btn_user);
		
		user.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.i("MainActivity", "Click User");
				Intent intent = new Intent(MainActivity.this, User.class);
				startActivity(intent);
			}
		});
		
		user.setOnTouchListener(new OnTouchListener() {	
			@SuppressLint("ClickableViewAccessibility")
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(event.getAction() == MotionEvent.ACTION_DOWN)
					user.setImageDrawable(getResources().getDrawable(R.drawable.user_press));		
				if(event.getAction() == MotionEvent.ACTION_UP)
					user.setImageDrawable(getResources().getDrawable(R.drawable.user));	
				return false;
			}
		});
	}
	
	private void onRecent(){
		recent = (ListView) findViewById(R.id.list_recent);
		
		// 获取最近识别的名片信息
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, GetRecentCard.getData());
		recent.setAdapter(adapter);
		
		recent.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
				if(position==3){
					Log.i("MainActivity", "Click Test");
					Intent intent = new Intent(MainActivity.this, WebTest.class);
					startActivity(intent);
				}
			}
		});
	}

    private void initAccessTokenWithAkSk() {
    	OCR.getInstance().initAccessTokenWithAkSk(new OnResultListener<AccessToken>() {
			@Override
			public void onResult(AccessToken result) {
				String token = result.getAccessToken();
				Log.i("InitAccessToken", token);
			}
			
			@Override
			public void onError(OCRError error) {
				Log.e("InitAccessToken", error.getMessage());
			}
		}, getApplicationContext(), "ImSCx0VgCO5tahbz8DBCwoW9", "530h3uiDYuZB1oXC5Zp0ZYlHT1zvbUN6");
    }
    
}
