package com.carnnecting.account;


import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.widget.Button;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.carnnecting.home.Home;
import com.cmu.carnnecting.R;


public class Login extends Activity {

	private Button demoButton;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_carnnecting_main);
		
		demoButton = (Button) findViewById(R.id.demoOnlyButton);
		
		demoButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(v.getContext(), Home.class);
				startActivity(intent);
			}
		});
		
		//test comment
		
	}
}
