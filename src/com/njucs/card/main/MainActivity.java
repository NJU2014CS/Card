package com.njucs.card.main;

import com.njucs.card.R;
import com.njucs.card.contact.ContactActivity;
import com.njucs.card.initializtion.GetRecentCard;
import com.njucs.card.recognition.Recognition;
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
	
	private ImageButton album, camera, user;
	// 最近处理的联系人列表
	private ListView recent;
	// 从相册得到的或者拍照得到的照片的URI保存在imageUri里。
	private Uri imageUri;
	// 共享位图
	public static Bitmap bitmap;
	
	@Override
 	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);	
		
		// 获取版本号
		int version=android.os.Build.VERSION.SDK_INT;
		Log.i("Version", version+"");
		
		// 几个控件的点击响应函数
		onAlbum();		
		onCamera();
		onUser();
		onRecent();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// 没有选取或者拍摄照片，resultCode=0，直接返回。
		if(resultCode!=RESULT_OK)
			return ;
		
		if(requestCode==ALBUM){
			if(data!=null){
				imageUri=data.getData();
				Crop(ALBUM);
			}
		}
		else if(requestCode==CAMERA){
			Crop(CAMERA);
		}
		else if(requestCode==CROP){
			// 裁剪后直接取得数据。
			bitmap=data.getParcelableExtra("data");
			// 识别过程。
			String info=Recognition.BitmapToText();
			Log.i("content", info);
			// 调用其他活动。
			callContactActivity(info);
		}
		else{
			Log.e("Error", "In MainActivity, onActivityResult");
		}
	}
	
	private void callContactActivity(String info){
		// 传递识别出来的信息Info
		// 不再传递URI，其他活动直接使用本活动中的共享位图数据。
		Intent intent=new Intent(MainActivity.this, ContactActivity.class);
		// intent.putExtra("Uri", imageUri.toString());
		intent.putExtra("Info", info);
		startActivity(intent);
	}
	
	private void onAlbum(){
		album=(ImageButton)findViewById(R.id.btn_album);
		
		album.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.i("InMainActicity", "Click Album");
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
				Log.i("InMainActicity", "Click Camrea");
				// 拍照得到的照片存储在output_image.jpg
				File outputImage = new File(Environment.getExternalStorageDirectory(), "output_image.jpg");
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
				Log.i("InMainActicity", "Click User");
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
					Log.i("InMainActicity", "Click Test");
					Intent intent = new Intent(MainActivity.this, WebTest.class);
					startActivity(intent);
				}
			}
		});
	}

	// 裁剪，返回位图数据
	private void Crop(int order){
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(imageUri, "image/*");
		intent.putExtra("crop", true);
		intent.putExtra("scale", true);
		intent.putExtra("return-data", true); 
		if(order==CAMERA){
			intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
		}
		startActivityForResult(intent, CROP);
	}

}
