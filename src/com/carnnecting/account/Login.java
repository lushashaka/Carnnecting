package com.carnnecting.account;

import android.os.Bundle;

import android.support.v4.app.FragmentActivity;

import android.util.Log;
import android.view.Menu;

import com.carnnecting.entities.PopulateDB;
import com.carnnecting.ws.FBConnect;
import com.cmu.carnnecting.R;

/**
 * This class is for the Login Screen and populating initial local database contents
 *
 */

public class Login extends FragmentActivity {

	private FBConnect mainFragment;
	private static final String TAG = "Login";
	private int userId;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i(TAG, "Login Screen");
		/* Uncomment the below code to generate the hash value
		 * Register this value in the FB account
		  try {
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
		
		// Populate DB data
		PopulateDB db = new PopulateDB();
		db.populateDBData(this.getApplication());
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
		userId = mainFragment.getUserId();
		Log.i(TAG, "User ID in login screen: " + userId);
		setContentView(R.layout.activity_carnnecting_main);			}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}
}