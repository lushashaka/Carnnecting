package com.carnnecting.account;


import android.os.Bundle;
import android.view.Menu;
import android.support.v4.app.FragmentActivity;
import com.carnnecting.ws.FBConnect;
import com.cmu.carnnecting.R;

public class Login extends FragmentActivity {

	private FBConnect mainFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		System.out.println("Login: Debug Message");
		
		if (savedInstanceState == null) {
	        // Add the fragment on initial activity setup
	        mainFragment = new FBConnect();
	        getSupportFragmentManager()
	        .beginTransaction()
	        .add(android.R.id.content, mainFragment)
	        .commit();
	    } else {
	        // Or set the fragment from restored state info
	        mainFragment = (FBConnect) getSupportFragmentManager()
	        .findFragmentById(android.R.id.content);
	    }
		setContentView(R.layout.activity_carnnecting_main);
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.carnnecting_main, menu);
		return true;
	}
	
}
