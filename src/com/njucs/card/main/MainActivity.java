package com.njucs.card.main;

import com.njucs.card.R;
import com.njucs.card.contact.ContactActivity;
import com.njucs.card.initializtion.GetRecentCard;
import com.njucs.card.tools.BaseActivity;
import com.njucs.card.tools.FormatConversion;
import com.njucs.card.user.User;

import java.io.*;
import android.widget.*;
import android.graphics.*;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;

/*
 * 主界面：包括最近扫描结果组成的一个列表ListView::recent和下方四个ImageButton
 * 分别是：名片夹（没什么用），相册，照相，用户
 * 相册和照相的响应函数以完成，会在onActivityResult()里得到一个图片的Uri进而处理得到Bitmap传入识别模块
 * 
 * 对于最近处理列表还需要对每一项添加响应函数，点击后跳转到我们自定义的一个联系人界面（未设计）进行浏览
 * 2016-11-02
 */
public class MainActivity extends BaseActivity implements OnTouchListener{
	static final int ALBUM=1, CAMERA=2;
	
	private ImageButton album, camera, user;
	private ImageView test;
	private ListView recent;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		test=(ImageView)findViewById(R.id.test);
		
		album=(ImageButton)findViewById(R.id.btn_album);
		camera=(ImageButton)findViewById(R.id.btn_camera);
		user=(ImageButton)findViewById(R.id.btn_user);
		album.setOnTouchListener(this);
		camera.setOnTouchListener(this);
		user.setOnTouchListener(this);
		
		album.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent=new Intent();  
				intent.setAction(Intent.ACTION_GET_CONTENT); 
				intent.setType("image/*");
				startActivityForResult(intent, ALBUM);     
			}
		});
		
		camera.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				startActivityForResult(intent, CAMERA);
			}
		});

		user.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.i("Main", "Click User");
				Intent intent = new Intent(MainActivity.this, User.class);
				startActivity(intent);
			}
		});
		
		recent = (ListView) findViewById(R.id.list_recent);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, GetRecentCard.getData());
		recent.setAdapter(adapter);

	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode!=RESULT_OK)
			return ;
		
		Bitmap bitmap=null;
		switch (requestCode) {
		case ALBUM:
			if(data!=null){
				Uri imageUri=data.getData();
				try {
					bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
					Log.i("ALBUM", bitmap.getWidth()+"*"+bitmap.getHeight());
					//获取照片成功，设置背景（测试），下一步将照片作为数据传递
					test.setImageBitmap(bitmap);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}
			break;
		case CAMERA:
			bitmap=data.getParcelableExtra("data");
			if(bitmap!=null){
				Log.i("Camera", bitmap.getWidth()+"*"+bitmap.getHeight());
				//获取照片成功，设置背景（测试），下一步将照片作为数据传递
				//目前似乎存在拍照获得的Bitmap失真的问题
				//----------------------------
				test.setImageBitmap(bitmap);
			}
			break;
		default:
			break;
		}
		
		//未选取照片时不进行任何动作
		if(data!=null&&bitmap!=null){
			//Log.i("String", "OutSide!");
			Intent intent=new Intent(MainActivity.this, ContactActivity.class);
			byte[] pic=FormatConversion.Bitmap2Bytes(bitmap);
			intent.putExtra("Picture", pic);
			//Log.i("String", "Convert Success");
			startActivity(intent);
		}
	}

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch(v.getId()){
		case R.id.btn_album:
			if(event.getAction() == MotionEvent.ACTION_DOWN)
				album.setImageDrawable(getResources().getDrawable(R.drawable.album_press));		
			if(event.getAction() == MotionEvent.ACTION_UP)
				album.setImageDrawable(getResources().getDrawable(R.drawable.album));		
			break;
		case R.id.btn_camera:
			if(event.getAction() == MotionEvent.ACTION_DOWN)
				camera.setImageDrawable(getResources().getDrawable(R.drawable.camera_press));		
			if(event.getAction() == MotionEvent.ACTION_UP)
				camera.setImageDrawable(getResources().getDrawable(R.drawable.camera));	
			break;
		case R.id.btn_user:
			if(event.getAction() == MotionEvent.ACTION_DOWN)
				user.setImageDrawable(getResources().getDrawable(R.drawable.user_press));		
			if(event.getAction() == MotionEvent.ACTION_UP)
				user.setImageDrawable(getResources().getDrawable(R.drawable.user));	
			break;
		default:
			break;
		}
		return false;
	}

}
