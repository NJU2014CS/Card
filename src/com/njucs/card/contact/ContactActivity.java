package com.njucs.card.contact;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.njucs.card.R;
import com.njucs.card.main.MainActivity;
import com.njucs.card.tools.BaseActivity;
import com.njucs.card.tools.Contacts;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
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
	//	private Uri imageUri;
	private String info;
	
	private ListView listview=null;
	private ImageView iv;
	private List<Map<String,Object>> record;
		
	public final class widget{
		public TextView label;
		public EditText info;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contact);
		
		// 获取传递参数中识别出来的名片信息
		Intent intent=getIntent();
		info=intent.getStringExtra("Info");
		
		// 从主活动中直接取得图片相应的位图
		Bitmap bm=MainActivity.bitmap;
		
		iv=(ImageView)findViewById(R.id.handledpic);
		if(bm!=null){
			iv.setImageBitmap(bm);
		}
		
		Log.i("InContactActivity", "Show the Picture");
		
		listview=(ListView)findViewById(R.id.contactlist);
		
		SimpleAdapter adapter = new SimpleAdapter(this,getData(new Contacts(info)),R.layout.contact,
				new String[]{"label","info"},
				new int[]{R.id.label,R.id.web_info});
		listview.setAdapter(adapter);
		Log.i("InContactActivity", "Adapter");
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
	
	private List<Map<String, Object>> getData(Contacts c) {
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
			map.put("label", "电话：");
			map.put("info", c.getMobilephone());
			record.add(map);
		}
		
		if(c.getTelephone()!=null){
			map = new HashMap<String, Object>();
			map.put("label", "座机：");
			map.put("info", c.getTelephone());
			record.add(map);
		}

		if(c.getMail()!=null){
			map = new HashMap<String, Object>();
			map.put("label", "Email：");
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
		
		return record;
	}

}