package com.njucs.card.tools.web;

import com.njucs.card.R;
import com.njucs.card.tools.BaseActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class WebTest extends BaseActivity {
	private EditText content;
	private TextView web_info;
	private Button web_search;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.web_test);
		

		content=(EditText)findViewById(R.id.content);

		web_info=(TextView)findViewById(R.id.web_info);

		web_search=(Button)findViewById(R.id.web_search);
		
		
	}
}
