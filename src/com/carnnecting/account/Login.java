package com.carnnecting.account;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;

import com.carnnecting.ws.FBConnect;
import com.cmu.carnnecting.R;

public class Login extends FragmentActivity {

	private FBConnect mainFragment;
	private static final String TAG = "Login";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i(TAG, "Login Screen");
		/*try {
		       PackageInfo info = getPackageManager().getPackageInfo(
		               "com.cmu.carnnecting", 
		               PackageManager.GET_SIGNATURES);
		       for (Signature signature : info.signatures) {
		           MessageDigest md = MessageDigest.getInstance("SHA");
		           md.update(signature.toByteArray());
		           Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
		           }
		   } catch (NameNotFoundException e) {

		   } catch (NoSuchAlgorithmException e) {

		   }*/
		
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
