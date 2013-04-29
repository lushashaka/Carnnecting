package com.carnnecting.event;

import java.io.File;

import com.cmu.carnnecting.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.Toast;

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
	    setContentView(R.layout.activity_create_event);
	    
	    final String[] items = {"Category1", "Category2", "Category3", "Category4", "Category5", "Category6", "Category7", "Category8", "Category9", "Category10"};
	    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Select Category");
		builder.setItems(items, new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				Toast.makeText(CreateEvent.this, "You selected " + items[which], Toast.LENGTH_SHORT).show();
			}
		});
		
		builder.create();
		
		    // TODO Auto-generated method stub
		    iv = (ImageView) findViewById(R.id.imgView);
		    
		    Button takephotos = (Button) findViewById(R.id.takePhotos);
		    Button getphotos = (Button) findViewById(R.id.getPhotos);
		    Button upload = (Button) findViewById(R.id.upload);
		    Button selCategory = (Button) findViewById(R.id.selectCategory);
		    
		    selCategory.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					builder.show();
				}
			});
		    
		    //registerForContextMenu(selCategory);
		    
		    takephotos.setOnClickListener(new OnClickListener() {
		    	public void onClick(View v) {
		    		//takePicture();
		      		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		      		File file = new File(Environment.getExternalStorageDirectory(), SAMPLEIMG);
		      		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
		      		startActivityForResult(intent, REQUEST_PICTURE);
		    	}
		    });
		    
		    getphotos.setOnClickListener(new OnClickListener() {
		    	public void onClick(View v) {
		    		//photoAlbum();
		      		Intent intent = new Intent(Intent.ACTION_PICK);  		
		      		intent.setType(Images.Media.CONTENT_TYPE);
		      		intent.setData(Images.Media.EXTERNAL_CONTENT_URI);
		      		startActivityForResult(intent, REQUEST_PHOTO_ALBUM);	    		
		    	}
		    });
	}
	/*
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		if(v.getId() == R.id.selectCategory) {
		menu.setHeaderTitle("Select category");
        menu.add(0,1,0, "Category1");
        menu.add(0,2,0, "Category2");
        menu.add(0,3,0, "Category3");
        menu.add(0,4,0, "Category4");
        menu.add(0,5,0, "Category5");
        menu.add(0,6,0, "Category6");
        menu.add(0,7,0, "Category7");
        menu.add(0,8,0, "Category8");
        menu.add(0,9,0, "Category9");
        menu.add(0,10,0, "Category10");
		}
        super.onCreateContextMenu(menu, v, menuInfo);
		
    } 
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if ("Category1" == item.getTitle())
            Toast.makeText(this, "Select Category1", Toast.LENGTH_SHORT).show();
        else if ("Category2" == item.getTitle())
            Toast.makeText(this, "Select Category2", Toast.LENGTH_SHORT).show();
        else
        	Toast.makeText(this, "Select Category?", Toast.LENGTH_SHORT).show();
        return true;
    }
*/
		
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
