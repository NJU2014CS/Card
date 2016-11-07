package com.njucs.card.tools.web;

import com.njucs.card.R;
import com.njucs.card.tools.BaseActivity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
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
		
		web_search.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String s=content.getText().toString();
				web_info.setText(s.toCharArray(),0,s.length());
			}
		});
	}
}
