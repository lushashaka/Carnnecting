package com.carnnecting.ws;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.Toast;

import com.facebook.FacebookRequestError;
import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.RequestAsyncTask;
import com.facebook.Response;
import com.facebook.Session;


public class FBShare extends Fragment {
	private static final String TAG = "FBShare";
	private static final List<String> PERMISSIONS = Arrays.asList("publish_actions"); 
	public final String PENDING_PUBLISH_KEY = "pendingPublishReauthorization";
	public boolean pendingPublishReauthorization = false;
	
	public void shareEvent(String event) {	
	    Session session = Session.getActiveSession();

	    if (session != null){
	    	try {
		        // Check for publish permissions    
		        List<String> permissions = session.getPermissions();
		        if (!isSubsetOf(PERMISSIONS, permissions)) {
		            pendingPublishReauthorization = true;
		            Session.NewPermissionsRequest newPermissionsRequest = new Session
		                    .NewPermissionsRequest(this, PERMISSIONS);
		        session.requestNewPublishPermissions(newPermissionsRequest);
		            return;
		        }
	
		        Bundle postParams = new Bundle();
		        postParams.putString("message", event);
	
		        Request.Callback callback= new Request.Callback() {
		            public void onCompleted(Response response) {
		                JSONObject graphResponse; 
		                @SuppressWarnings("unused")
						String postId = null;
		                try {
		                	graphResponse = response
	                                .getGraphObject()
	                                .getInnerJSONObject();
		                    postId = graphResponse.getString("id");
		                } catch (Exception e) {
		                    Log.i(TAG,
		                        "JSON error "+ e.getMessage());
		                }
		                FacebookRequestError error = response.getError();
		                if (error != null) {
		                    Toast.makeText(FBConnect.context,
		                         error.getErrorMessage(),
		                         Toast.LENGTH_SHORT).show();
		                    } else {
		                        Toast.makeText(FBConnect.context, 
		                             "Event shared",
		                             Toast.LENGTH_LONG).show();
		                        
		                        Log.i(TAG, "Event shared");
		                }
		            }
		        };
	
		        Request request = new Request(session, "me/feed", postParams, 
		                              HttpMethod.POST, callback);
	
		        RequestAsyncTask task = new RequestAsyncTask(request);
		        task.execute();
	    	} catch(Exception e) {
	    		Log.i(TAG, "Exception: " + e.getMessage());
	    		Toast.makeText(FBConnect.context, 
                        "Facebook exception: " + e.getMessage(),
                        Toast.LENGTH_SHORT).show();
	    	}
	    }

	}
	
	private boolean isSubsetOf(Collection<String> subset, Collection<String> superset) {
	    for (String string : subset) {
	        if (!superset.contains(string)) {
	            return false;
	        }
	    }
	    return true;
	}
}