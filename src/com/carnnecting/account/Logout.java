package com.carnnecting.account;

import android.support.v4.app.Fragment;

import com.facebook.Session;

/**
 * This class is for handling Logout from Facebook
 *
 */

public class Logout extends Fragment {
	public void FBLogout() {
		Session session = Session.getActiveSession();
	
		if (session != null){
			session.closeAndClearTokenInformation();
		}
	}
}