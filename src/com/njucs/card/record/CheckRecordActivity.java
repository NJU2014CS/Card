package com.njucs.card.record;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.njucs.card.R;
import com.njucs.card.tools.BaseActivity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class CheckRecordActivity extends BaseActivity{
	
	private EditText name;
	private EditText company;
	private EditText duty;
	private EditText phone;
	private EditText mail;
	private EditText address;
	private EditText net;
	private EditText note;
	
	private ImageView phonecall;
	private ImageView email;
	
	private Button cancel;
	private Button save;
	
	private int index;
	private boolean add=false;
	private Map<String,String> m;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.check);
		Intent intent=getIntent();
		String buffer=intent.getStringExtra("data");
		m=Recent.GenerateMap(buffer);
		index=intent.getIntExtra("index", 0);
		add=intent.getBooleanExtra("add", false);
		
		name=(EditText) findViewById(R.id.personname);
		name=initEditText(name, "name");
		
		company=(EditText) findViewById(R.id.personcompany);
		company=initEditText(company, "company");
		
		duty=(EditText) findViewById(R.id.personduty);
		duty=initEditText(duty, "duty");
		
		phone=(EditText) findViewById(R.id.personphone);
		phone=initEditText(phone, "mobilephone");
		
		mail=(EditText) findViewById(R.id.personemail);
		mail=initEditText(mail, "mail");
		
		address=(EditText) findViewById(R.id.personaddress);
		address=initEditText(address, "address");
		
		net=(EditText) findViewById(R.id.personnet);
		net=initEditText(net, "website");
		
		note=(EditText) findViewById(R.id.personnote);
		note=initEditText(note, "note");
		
		phonecall=(ImageView) findViewById(R.id.image_call);
		phonecall=initImageView(phonecall, "phonecall");
		
		email=(ImageView) findViewById(R.id.image_mail);
		email=initImageView(email, "email");
		
		initCancelButton();
		initSaveButton();
	}
	
	private EditText initEditText(EditText et, final String etname){
		et=SetListener(et, etname);
		if(et!=null){
			et.setText(m.get(etname)==null?"":m.get(etname));
		}
		return et;
	}
	
	private ImageView initImageView(ImageView iv, final String ivname){
		if(iv==null)
			return iv;
		else{
			iv.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if(ivname.equals("phonecall")){
						Intent intent=new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+phone.getText()));
						intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						startActivity(intent);
					}
					else if(ivname.equals("email")){
						Uri uri=Uri.parse("mailto:"+mail.getText());
						List<String> list=new ArrayList<String>();
						list.add(mail.getText().toString());
						Intent intent=new Intent(Intent.ACTION_SENDTO, uri);
						intent.putExtra(Intent.EXTRA_CC, list.toArray());
						startActivity(Intent.createChooser(intent, "请选择应用"));
					}
				}
			});
			return iv;
		}
	}
	
	private EditText SetListener(final EditText et, final String etname){
		if(et==null)
			return et;
		else{
			et.setCursorVisible(false);
			et.setOnTouchListener(new OnTouchListener() {
				
				@SuppressLint("ClickableViewAccessibility")
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					// TODO Auto-generated method stub
					//Toast.makeText(CheckRecordActivity.this, et.getText().toString(), Toast.LENGTH_SHORT).show();
					return false;
				}
			});
			et.addTextChangedListener(new TextWatcher() {
				
				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {
					// TODO Auto-generated method stub
				}
				
				@Override
				public void beforeTextChanged(CharSequence s, int start, int count, int after) {
					// TODO Auto-generated method stub
				}
				
				@Override
				public void afterTextChanged(Editable s) {
					// TODO Auto-generated method stub
					m.put(etname,s.toString());
				}
			});
			et.setOnFocusChangeListener(new OnFocusChangeListener() {
				
				@Override
				public void onFocusChange(View v, boolean hasFocus) {
					// TODO Auto-generated method stub
					if(hasFocus)
						et.setCursorVisible(true);
					else
						et.setCursorVisible(false);
				}
			});
			return et;
		}
	}
	
	private void initSaveButton(){
		save=(Button) findViewById(R.id.check_save);
		save.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(!add)
					Recent.SetMetaData(index, m);
				else
					Recent.AddMetaData(m);
				CheckRecordActivity.this.finish();
			}
		});
	}
	
	private void initCancelButton(){
		cancel=(Button) findViewById(R.id.check_cancel);
		cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				CheckRecordActivity.this.finish();
			}
		});
	}
	
	public Map<String,String> getData(){
		return m;
	}
}
