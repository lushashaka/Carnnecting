package com.carnnecting.ws;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.carnnecting.entities.UserDataSource;
import com.carnnecting.home.Home;
import com.cmu.carnnecting.R;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;

public class FBConnect extends Fragment {
	private static final String TAG = "FBConnect";
	static Context context;
	private UiLifecycleHelper uiHelper;
	private FBShare share = new FBShare();
	
	private Button shareButton;
	
	public View onCreateView(LayoutInflater inflater, 
	        ViewGroup container, 
	        Bundle savedInstanceState) {
	    View view = inflater.inflate(R.layout.activity_carnnecting_main, container, false);
	    
	    LoginButton authButton = (LoginButton) view.findViewById(R.id.authButton);
	    authButton.setFragment(this);
	    
	    if (savedInstanceState != null) {
	    	share.pendingPublishReauthorization = 
	    			savedInstanceState.getBoolean(share.PENDING_PUBLISH_KEY, false);
	    }
	    
	    shareButton = (Button) view.findViewById(R.id.shareButton);
	    shareButton.setOnClickListener(new View.OnClickListener() {
	        @Override
	        public void onClick(View v) {
	            share.shareEvent();        
	        }
	    });

	    return view;
	}
	
	private void onSessionStateChange(Session session, SessionState state, Exception exception) {
	    if (state.isOpened()) {
	        Log.i(TAG, "Success: Logged in...");
	        // Request user data and show the results
	        
	        shareButton.setVisibility(View.VISIBLE);
	        
	        if (share.pendingPublishReauthorization && 
	                state.equals(SessionState.OPENED_TOKEN_UPDATED)) {
	            share.pendingPublishReauthorization = false;
	            share.shareEvent();
	        }
	        
	        Request.executeMeRequestAsync(session, new Request.GraphUserCallback() {

				@Override
				public void onCompleted(GraphUser user, Response response) {
					Log.i(TAG, String.format("Name: %s\n\n", 
					        user.getName()));
					Log.i(TAG, String.format("Username: %s\n\n", 
					        user.getUsername()));
					
					// Insert username in DB
					UserDataSource userDAO = new UserDataSource(getActivity().getApplication());
					userDAO.open();
					
					String userName = user.getUsername();
					userDAO.createUser(userName);
					
					int userId = userDAO.getUserIdByFbName(userName);;
					
					Log.i(TAG, "User ID: " + userId);
					
					// Launch "News Feed"
					Intent intent = new Intent(getActivity().getApplicationContext(), Home.class);
			        intent.putExtra("USERID", userId);
			        
					startActivity(intent);					
				}
	        });	        
	    } else if (state.isClosed()) {
	        Log.i(TAG, "Success: Logged out...");
	        shareButton.setVisibility(View.INVISIBLE);
	    }
	}
	
	private Session.StatusCallback callback = new Session.StatusCallback() {
	    @Override
	    public void call(Session session, SessionState state, Exception exception) {
	        onSessionStateChange(session, state, exception);
	    }
	};
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    context = this.getActivity().getApplicationContext();
	    uiHelper = new UiLifecycleHelper(getActivity(), callback);
	    uiHelper.onCreate(savedInstanceState);
	}
	
	@Override
	public void onResume() {
	    super.onResume();

	    // For scenarios where the main activity is launched and user
	    // session is not null, the session state change notification
	    // may not be triggered. Trigger it if it's open/closed.
//	    Session session = Session.getActiveSession();
//	    if (session != null &&
//	           (session.isOpened() || session.isClosed()) ) {
//	        onSessionStateChange(session, session.getState(), null);
//	    }

	    uiHelper.onResume();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	    super.onActivityResult(requestCode, resultCode, data);
	    uiHelper.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onPause() {
	    super.onPause();
	    uiHelper.onPause();
	}

	@Override
	public void onDestroy() {
	    super.onDestroy();
	    uiHelper.onDestroy();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
	    super.onSaveInstanceState(outState);
	    outState.putBoolean(share.PENDING_PUBLISH_KEY, share.pendingPublishReauthorization);
	    uiHelper.onSaveInstanceState(outState);
	}	
}