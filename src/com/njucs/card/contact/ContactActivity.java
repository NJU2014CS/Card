package com.njucs.card.contact;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.baidu.ocr.sdk.model.Location;
import com.njucs.card.R;
import com.njucs.card.main.MainActivity;
import com.njucs.card.main.Recent;
import com.njucs.card.recognition.BaiduOCR;
import com.njucs.card.tools.BaseActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

/*
 * 
 * 得到处理后照片并显示内容的活动
 * 需要上一个活动通过putExtra来进行数据传递
 * 可通过tools中的FormatConversion类来进行类型转换，将bitmap或者drawable以及Contacts类转换成byte数组
 *
 * 数据通过getData传入List<Map>中去，并由ListView显示
 * 
 */

public class ContactActivity extends BaseActivity {
	// 用来接收从上层活动传递过来的参数：识别出来基本信息
	private static String info;
	private static Activity activity;
	private static ListView listview;
	private static Contacts contact;
	private static List<Map<String,Object>> record;
	private ImageView iv;
		
	public static Handler handler=new Handler(){
	    @Override
	    public void handleMessage(Message msg) {
	           super.handleMessage(msg);
	   			// 设置列表
	           if(AipNlp.results.size()==AipNlp.n){
	        	   for(String s:AipNlp.results)
	        		   JsonUtil.parsing(s);
		   		   setListView();
	           }
	    }
	};
	
	public final class widget{
		public TextView label;
		public EditText info;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contact);
		activity=ContactActivity.this;
		listview=(ListView)findViewById(R.id.contactlist);
		iv=(ImageView)findViewById(R.id.handledpic);
		
		// 获取传递参数中识别出来的名片信息
		Intent intent=getIntent();
		info=intent.getStringExtra("Info");
		
		// 从主活动中直接取得图片相应的位图，并根据OCR识别结果圈出文字区域。
		showImage();
		
		// 自然语言处理，词法分析
		contact=new Contacts(info, false);
		contact.Transfer();
		//AipNlp.lexer(info);

		// 做进一步处理
		furtherProcessing();
	}
	
	private void furtherProcessing(){
		
		setListView();
	}
	
 	private void showImage(){
		Bitmap mutableBitmap=MainActivity.bitmap.copy(Bitmap.Config.ARGB_8888, true);
		Canvas canvas=new Canvas(mutableBitmap);
		Paint paint=new Paint();						// 画笔
		paint.setColor(Color.RED);						// 颜色
		paint.setStyle(Paint.Style.STROKE);			// 不填充
		paint.setStrokeWidth(1);  						// 线宽=1
		for(Location l:BaiduOCR.locations){
			canvas.drawRect(l.getLeft(), l.getTop(), l.getLeft()+l.getWidth(), l.getTop()+l.getHeight(), paint);
		}
		iv.setImageBitmap(mutableBitmap);
	}
	
	private static void setListView(){
		SimpleAdapter adapter = new SimpleAdapter(activity,getData(contact),R.layout.contact,
				new String[]{"label","info"},
				new int[]{R.id.label,R.id.web_info});
		listview.setAdapter(adapter);
	}
	
	/*private ArrayList<String> getData(Contacts c){
		ArrayList<String> record=new ArrayList<String>();
		if(c.getName()!=null)	record.add("姓名:"+c.getName());
		if(c.getMobilephone()!=null) record.add("电话:"+c.getMobilephone());
		if(c.getTelephone()!=null)	record.add("座机:"+c.getTelephone());
		if(c.getMail()!=null)	record.add("Email:"+c.getMail());
		if(c.getCompany()!=null)	record.add("公司:"+c.getCompany());
		if(c.getDuty()!=null)	record.add("职位:"+c.getDuty());
		if(c.getAddress()!=null)	record.add("地址:"+c.getAddress());
		if(c.getFax()!=null)	record.add("传真:"+c.getFax());
		if(c.getNote()!=null)	record.add("备注:"+c.getNote());
		Log.i("String", "GetData Over!");
		return record;
	}*/
	
	private static List<Map<String, Object>> getData(Contacts c) {
		record = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = null;
		
		if(c.getName()!=null){
			map = new HashMap<String, Object>();
			map.put("label", "姓名：");
			map.put("info", c.getName());
			record.add(map);
		}

		if(c.getMobilephone()!=null){
			map = new HashMap<String, Object>();
			map.put("label", "手机：");
			map.put("info", c.getMobilephone());
			record.add(map);
		}
		
		if(c.getTelephone()!=null){
			map = new HashMap<String, Object>();
			map.put("label", "电话：");
			map.put("info", c.getTelephone());
			record.add(map);
		}

		if(c.getMail()!=null){
			map = new HashMap<String, Object>();
			map.put("label", "邮箱：");
			map.put("info", c.getMail());
			record.add(map);
		}
		
		if(c.getCompany()!=null){
			map = new HashMap<String, Object>();
			map.put("label", "公司：");
			map.put("info", c.getCompany());
			record.add(map);
		}
		
		if(c.getDuty()!=null){
			map = new HashMap<String, Object>();
			map.put("label", "职位：");
			map.put("info", c.getDuty());
			record.add(map);
		}
		
		if(c.getAddress()!=null){
			map = new HashMap<String, Object>();
			map.put("label", "地址：");
			map.put("info", c.getAddress());
			record.add(map);
		}
		
		if(c.getFax()!=null){
			map = new HashMap<String, Object>();
			map.put("label", "传真：");
			map.put("info", c.getFax());
			record.add(map);
		}
		
		if(c.getNote()!=null){
			map = new HashMap<String, Object>();
			map.put("label", "备注：");
			map.put("info", c.getNote());
			record.add(map);
		}
		
		if(c.getPostcode()!=null){
			map = new HashMap<String, Object>();
			map.put("label", "邮编：");
			map.put("info", c.getPostcode());
			record.add(map);
		}
		
		if(c.getUrl()!=null){
			map = new HashMap<String, Object>();
			map.put("label", "网址：");
			map.put("info", c.getUrl());
			record.add(map);
		}
		
		return record;
	}

	public static Contacts getContact(){
		return contact;
	}
	
}