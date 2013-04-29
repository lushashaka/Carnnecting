package com.carnnecting.event;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import com.carnnecting.entities.Category;
import com.carnnecting.entities.CategoryDataSource;
import com.carnnecting.entities.EventDataSource;
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
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class CreateEvent extends Activity {
	static int REQUEST_PICTURE = 1;
	static int REQUEST_PHOTO_ALBUM = 2;
	static String SAMPLEIMG = "sample_img.png";
	EventDataSource eventDao;
	CategoryDataSource categoryDao;
	HashMap<String, Integer> catName2Id = new HashMap<String, Integer>();
    int catId = 1;
	
	Context mContext = this;
	ImageView iv;
	Dialog dialog;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_create_event);
	    
	    eventDao  = new EventDataSource(this.getApplication());
	    eventDao.open();
	    categoryDao = new CategoryDataSource(this.getApplication());
	    categoryDao.open();
	    
	    ArrayList<Category> categories= categoryDao.getAllCategories();
	    final String[] items = new String[categories.size()];
	    
	    for (int i = 0; i < items.length; i++) {
	    	items[i] = categories.get(i).getName();
	    	catName2Id.put(categories.get(i).getName(), categories.get(i).getId());
	    	
	    }
	    
	    
	    // final String[] items = {"Category1", "Category2", "Category3", "Category4", "Category5", "Category6", "Category7", "Category8", "Category9", "Category10"};
	    
	    
	    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Select Category");
		builder.setItems(items, new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				catId = catName2Id.get(items[which]);
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
		         	    
		    final EditText title = (EditText) findViewById(R.id.editText1);
		    final EditText startD = (EditText) findViewById(R.id.editText3_1);
		    final EditText startT = (EditText) findViewById(R.id.editText6_1);
		    final EditText endD = (EditText) findViewById(R.id.editText3_2);
		    final EditText endT = (EditText) findViewById(R.id.editText6_2);
		    final EditText addr = (EditText) findViewById(R.id.editText4);
		    final EditText org = (EditText) findViewById(R.id.edit_host);
		    final EditText dscr = (EditText) findViewById(R.id.editText5);
		    		    
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
		    
		    upload.setOnClickListener(new OnClickListener() {
		    	public void onClick(View v) {
		    		String subject = title.getText().toString();
				    String startTime = startD.getText().toString() + " " + startT.getText().toString();
				    String endTime = endD.getText().toString() + " " + endT.getText().toString();
				    String location = addr.getText().toString();
				    String host = org.getText().toString();
				    String description = dscr.getText().toString();
		    	    int categoryId = catId;
		    	    
		    	    Log.e("INFO", subject + " " + startTime +" " + endTime + " " + location + " " + host + " " + description + " " + categoryId);
		    	    
		    	    eventDao.createEvent(0/*not used*/, subject, startTime, endTime, location, host, description, categoryId);
		    	    Toast.makeText(CreateEvent.this, "Your event is uploaded", Toast.LENGTH_SHORT).show();
		    	    finish();
		    	}
		    });
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
