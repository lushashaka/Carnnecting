package com.carnnecting.event;

import java.io.File;

import com.cmu.carnnecting.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class CreateEvent extends Activity {
	static int REQUEST_PICTURE = 1;
	static int REQUEST_PHOTO_ALBUM = 2;
	static String SAMPLEIMG = "sample_img.png";
	
	Context mContext = this;
	ImageView iv;
	Dialog dialog;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.create_event);
		
	    // TODO Auto-generated method stub
	    iv = (ImageView) findViewById(R.id.imgView);
	    
	    Button getphotos = (Button) findViewById(R.id.getPhotos);
	    Button upload = (Button) findViewById(R.id.upload);
	 	    
	    getphotos.setOnClickListener(new OnClickListener() {
	    	public void onClick(View v) {
	    		photoAlbum();
	    			    	}
	    });

	}

	
	void takePicture() {
  		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
  		File file = new File(Environment.getExternalStorageDirectory(), SAMPLEIMG);
  		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
  		startActivityForResult(intent, REQUEST_PICTURE);
  	}
	
  	void photoAlbum() {
  		Intent intent = new Intent(Intent.ACTION_PICK);  		
  		intent.setType(Images.Media.CONTENT_TYPE);
  		intent.setData(Images.Media.EXTERNAL_CONTENT_URI);
  		startActivityForResult(intent, REQUEST_PHOTO_ALBUM);
  	}
  	
	Bitmap loadPicture() {
  		File file = new File(Environment.getExternalStorageDirectory(), SAMPLEIMG);
  		BitmapFactory.Options option = new BitmapFactory.Options();
  		option.inSampleSize = 4;
  		return BitmapFactory.decodeFile(file.getAbsolutePath(), option);
  	}
	
  	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
  		// TODO Auto-generated method stub
  		super.onActivityResult(requestCode, resultCode, data);
  		
  		if(resultCode != RESULT_OK)
  			return;
  		
  		if(requestCode == REQUEST_PICTURE) {
  			iv.setImageBitmap(loadPicture());
  		}
  		
  		if(requestCode == REQUEST_PHOTO_ALBUM) {
  			iv.setImageURI(data.getData());
  		}
  	}

}
