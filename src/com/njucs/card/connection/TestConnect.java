package com.njucs.card.connection;

import java.io.FileNotFoundException;
import java.io.IOException;

import com.njucs.card.R;
import com.njucs.card.tools.BaseActivity;
import com.njucs.card.tools.FormatConversion;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


/*
 * 整个类仅用来测试网络，也提供了SendMessage的大概使用方式，以后整合时可参照~
 * @author zzzzwj
 */


public class TestConnect extends BaseActivity{
	
	private static final int ChosenPicture = 1;
	
	private EditText IPaddr;
	private Button Connect;
	private TextView ResContent;
	private String ip;
	//需要传送的图片（一般为logo，或者截下的文字）
	private Bitmap picchosen;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.testconnect);
		
		IPaddr=(EditText)findViewById(R.id.IPaddr);
		Connect=(Button)findViewById(R.id.Connect);
		ResContent=(TextView)findViewById(R.id.ResContent);
		
		IPaddr.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				ip = s.toString();
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub
				ip = s.toString();
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				ip = s.toString();
			}
		});
		
		Connect.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ip = IPaddr.getText().toString();
				
				//调出系统图库选择图片传送
				Intent intent = new Intent(Intent.ACTION_PICK);
				intent.setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
				startActivityForResult(intent, ChosenPicture);
				
			}
		});
	}
	
	private void send(byte[] img){
		Monitor m=new Monitor(new SendMessage(ip, Utils.Transformer(1,img)), 5000);
		new Thread(m).start();
		
		while(!m.isOver()){
			
		}
		
		//将结果传到面板
		if(m.GetErrorcode()==-1)
			ResContent.setText((String)m.GetResult());
		else
			ResContent.setText(m.GetErrorInfo());
	}
	
	@Override
	protected void onActivityResult(int requestCode,int resultCode,Intent data){
		if(resultCode!=RESULT_OK){
			return;
		}
		
		if(requestCode==ChosenPicture){
			if(data!=null){
				Uri Imguri=data.getData();
				try {
					picchosen=MediaStore.Images.Media.getBitmap(this.getContentResolver(), Imguri);
					send(FormatConversion.Bitmap2Bytes(picchosen));
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}
