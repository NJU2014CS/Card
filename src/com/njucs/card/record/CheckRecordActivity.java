package com.njucs.card.record;

import java.util.Map;

import com.njucs.card.R;
import com.njucs.card.tools.BaseActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.widget.EditText;
import android.widget.Toast;

public class CheckRecordActivity extends BaseActivity{
	
	private EditText name;
	private EditText company;
	private EditText duty;
	private EditText phone;
	private EditText mail;
	private EditText address;
	private EditText net;
	private EditText note;
	
	private Map<String,String> m;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.check);
		Intent intent=getIntent();
		String buffer=intent.getStringExtra("data");
		m=Recent.GenerateMap(buffer);
		
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
	}
	
	private EditText initEditText(EditText et, final String etname){
		et=SetListener(et, etname);
		if(et!=null){
			et.setText(m.get(etname)==null?"":m.get(etname));
		}
		return et;
	}
	
	private EditText SetListener(final EditText et, final String etname){
		if(et==null)
			return et;
		else{
			et.setCursorVisible(false);
			et.setOnTouchListener(new OnTouchListener() {
				
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
	
	public Map<String,String> getData(){
		return m;
	}
}
