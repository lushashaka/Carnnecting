package com.carnnecting.event;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import com.carnnecting.account.Logout;
import com.carnnecting.category.CategoryMenu;
import com.carnnecting.entities.Category;
import com.carnnecting.entities.CategoryDataSource;
import com.carnnecting.entities.EventDataSource;
import com.carnnecting.entities.ImageDataSource;
import com.carnnecting.ws.FBShare;
import com.carnnecting.home.Home;
import com.cmu.carnnecting.R;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class CreateEvent extends Activity implements LocationListener {
	private LocationManager locManager;
	Geocoder geoCoder;
	private Location myLocation = null;
	double latPoint = 0;
	double longPoint = 0;

	static int REQUEST_PICTURE = 1;
	static int REQUEST_PHOTO_ALBUM = 2;
	static String SAMPLEIMG = "thumbnail.png";

	private FBShare share = new FBShare();
	private String FBmessage;

	private EventDataSource eventDao;
	private CategoryDataSource categoryDao;
	private ImageDataSource imgDao;
	private HashMap<String, Integer> catName2Id = new HashMap<String, Integer>();

	private Bitmap bmp;
	private ImageView iv;

	private int cyear, cmonth, cday, chour, cmin, csec;
	private int catId = 1;
	private int userId;
	private String fmDate, fmStime, fmEtime, sec, cMsg;
	private String country, city, street;

	private Button takephotos, getphotos, upload, selCategory, getLoc;
	private Button date, sTime, eTime;
	private TextView showDate, showStime, showEtime, showCategory, jusoText;
	private EditText title, addr, org, dscr;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_event);

		ActionBar actionBar = getActionBar();
		Intent intent = getIntent();
		userId = -1;
		if (intent != null && intent.getExtras() != null) {
			userId = intent.getExtras().getInt("userId");
		}

		eventDao = new EventDataSource(this.getApplication());
		eventDao.open();
		categoryDao = new CategoryDataSource(this.getApplication());
		categoryDao.open();
		imgDao = new ImageDataSource(this.getApplication());
		imgDao.open();

		bmp = null;
		iv = (ImageView) findViewById(R.id.imgView);

		locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
		locManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, this);
		geoCoder = new Geocoder(this, Locale.ENGLISH);

		takephotos = (Button) findViewById(R.id.takePhotos);
		getphotos = (Button) findViewById(R.id.getPhotos);
		upload = (Button) findViewById(R.id.upload);
		selCategory = (Button) findViewById(R.id.selectCategory);
		date = (Button) findViewById(R.id.date);
		sTime = (Button) findViewById(R.id.selStime);
		eTime = (Button) findViewById(R.id.selEtime);
		getLoc = (Button) findViewById(R.id.addr);

		showDate = (TextView) findViewById(R.id.showDate);
		showStime = (TextView) findViewById(R.id.showStime);
		showEtime = (TextView) findViewById(R.id.showEtime);
		showCategory = (TextView) findViewById(R.id.showCategory);
		jusoText = (TextView) findViewById(R.id.getAddr);

		title = (EditText) findViewById(R.id.editText1);
		org = (EditText) findViewById(R.id.edit_host);
		dscr = (EditText) findViewById(R.id.editText5);

		// TODO Auto-generated method stub
		ArrayList<Category> categories = categoryDao.getAllCategories();
		final String[] items = new String[categories.size()];

		for (int i = 0; i < items.length; i++) {
			items[i] = categories.get(i).getName();
			catName2Id.put(categories.get(i).getName(), categories.get(i)
					.getId());
		}

		final AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Select Category");
		builder.setItems(items, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				catId = catName2Id.get(items[which]);
				cMsg = "Your event will be included in " + items[which];
				updateDisplayC();
				Toast.makeText(CreateEvent.this,
						"You selected " + items[which], Toast.LENGTH_SHORT)
						.show();
			}
		});
		builder.create();

		getLoc.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				GetLocations();
				Log.d("location", "button pressed");
			}
		});

		date.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				DialogDatePicker();
			}
		});

		sTime.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				DialogTimePicker1();
			}
		});

		eTime.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				DialogTimePicker2();
			}
		});

		selCategory.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				builder.show();
			}
		});

		takephotos.setOnClickListener(new OnClickListener() {
			public void onClick(View v1) {
				takePicture();
			}
		});

		getphotos.setOnClickListener(new OnClickListener() {
			public void onClick(View v2) {
				photoAlbum();
			}
		});

		upload.setOnClickListener(new OnClickListener() {
			public void onClick(View v3) {
				String subject = title.getText().toString();
				String startTime = fmDate + " " + fmStime;
				String endTime = fmDate + " " + fmEtime;
				String location = street + ", " + city + ", " + country;
				String host = org.getText().toString();
				String description = dscr.getText().toString();

				int categoryId = catId;
				int eventId = eventDao.createEvent(0, subject, startTime,
						endTime, location, host, description, categoryId);

				FBmessage = "Event: " + subject;
				FBmessage += "\nHost: " + host;
				FBmessage += "\nLocation: " + location;
				FBmessage += "\nWhen: " + startTime + "~" + endTime;
				FBmessage += "\nDescription: " + description;
				FBmessage += "\nRSVP to this event by downloading the 'Carnnecting' app!";

				DialogFBShare();

				// Log.e("INFO", subject + " " + startTime +" " + endTime + " "
				// + location + " " + host + " " + description + " " +
				// categoryId);

				if (bmp != null) {
					if (imgDao.createImage(eventId, bmp) == false)
						Log.e("ERROR", "Cannot insert image");
					else
						Log.e("INFO", "image inserted");
				}
			}
		});
	}

	private void takePicture() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		File file = new File(Environment.getExternalStorageDirectory(),
				SAMPLEIMG);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
		startActivityForResult(intent, REQUEST_PICTURE);
	}

	private void photoAlbum() {
		Intent intent = new Intent(Intent.ACTION_PICK);
		intent.setType(Images.Media.CONTENT_TYPE);
		intent.setData(Images.Media.EXTERNAL_CONTENT_URI);
		startActivityForResult(intent, REQUEST_PHOTO_ALBUM);
	}

	private Bitmap loadPicture() {
		File file = new File(Environment.getExternalStorageDirectory(),
				SAMPLEIMG);
		BitmapFactory.Options option = new BitmapFactory.Options();
		option.inSampleSize = 4;
		return BitmapFactory.decodeFile(file.getAbsolutePath(), option);
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode != RESULT_OK)
			return;

		if (requestCode == REQUEST_PICTURE) {
			bmp = loadPicture();
			iv.setImageBitmap(bmp);
		}

		if (requestCode == REQUEST_PHOTO_ALBUM) {
			iv.setImageURI(data.getData());
		}
	}

	private void DialogFBShare() {
		AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
		alt_bld.setMessage("Do you want to share your event on Facebook?")
				.setCancelable(true)
				.setPositiveButton("No", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						// TODO Auto-generated method stub
						// action for no
						Toast.makeText(CreateEvent.this,
								"Your event is created", Toast.LENGTH_SHORT)
								.show();
						finish();
					}
				})
				.setNegativeButton("Yes",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								// action for yes
								share.shareEvent(FBmessage);
								Toast.makeText(CreateEvent.this,
										"Your event is created and shared",
										Toast.LENGTH_SHORT).show();
								finish();
							}
						});
		AlertDialog alert = alt_bld.create();
		alert.show();
	}

	private void DialogDatePicker() {
		Calendar c = Calendar.getInstance();
		cyear = c.get(Calendar.YEAR);
		cmonth = c.get(Calendar.MONTH);
		cday = c.get(Calendar.DAY_OF_MONTH);

		DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear,
					int dayOfMonth) {
				// TODO Auto-generated method stub
				cyear = year;
				cmonth = monthOfYear + 1;
				cday = dayOfMonth;

				if (cmonth < 10 && cday < 10)
					fmDate = cyear + "-0" + cmonth + "-0" + cday;
				else if (cmonth < 10 && cday >= 10)
					fmDate = cyear + "-0" + cmonth + "-" + cday;
				else if (cmonth >= 10 && cday < 10)
					fmDate = cyear + "-" + cmonth + "-0" + cday;
				else
					fmDate = cyear + "-" + cmonth + "-" + cday;

				updateDisplay();

				Toast.makeText(CreateEvent.this, "Selected Date is " + fmDate,
						Toast.LENGTH_SHORT).show();
			}
		};

		DatePickerDialog alert = new DatePickerDialog(this, mDateSetListener,
				cyear, cmonth, cday);
		alert.show();
	}

	private void DialogTimePicker1() {
		Calendar c = Calendar.getInstance();
		chour = c.get(Calendar.HOUR_OF_DAY);
		cmin = c.get(Calendar.MINUTE);
		csec = c.get(Calendar.SECOND);

		if (csec < 10)
			sec = "0" + csec;
		else
			sec = String.valueOf(csec);

		TimePickerDialog.OnTimeSetListener mTimeSetListener1 = new TimePickerDialog.OnTimeSetListener() {
			@Override
			public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
				// TODO Auto-generated method stub
				chour = hourOfDay;
				cmin = minute;

				if (hourOfDay < 10 && minute < 10)
					fmStime = "0" + chour + ":0" + cmin + ":" + sec;
				else if (hourOfDay < 10 && minute >= 10)
					fmStime = "0" + chour + ":" + cmin + ":" + sec;
				else if (hourOfDay >= 10 && minute < 10)
					fmStime = chour + ":0" + cmin + ":" + sec;
				else
					fmStime = chour + ":" + cmin + ":" + sec;

				updateDisplayS();

				Toast.makeText(CreateEvent.this, "Start time is " + fmStime,
						Toast.LENGTH_SHORT).show();
			}
		};

		TimePickerDialog alert1 = new TimePickerDialog(this, mTimeSetListener1,
				chour, csec, false);
		alert1.show();
	}

	private void DialogTimePicker2() {
		Calendar c = Calendar.getInstance();
		chour = c.get(Calendar.HOUR_OF_DAY);
		cmin = c.get(Calendar.MINUTE);
		csec = c.get(Calendar.SECOND);

		if (csec < 10)
			sec = "0" + csec;
		else
			sec = String.valueOf(csec);

		TimePickerDialog.OnTimeSetListener mTimeSetListener2 = new TimePickerDialog.OnTimeSetListener() {
			@Override
			public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
				// TODO Auto-generated method stub
				chour = hourOfDay;
				cmin = minute;

				if (hourOfDay < 10 && minute < 10)
					fmEtime = "0" + chour + ":0" + cmin + ":" + sec;
				else if (hourOfDay < 10 && minute >= 10)
					fmEtime = "0" + chour + ":" + cmin + ":" + sec;
				else if (hourOfDay >= 10 && minute < 10)
					fmEtime = chour + ":0" + cmin + ":" + sec;
				else
					fmEtime = chour + ":" + cmin + ":" + sec;

				updateDisplayE();

				Toast.makeText(CreateEvent.this, "End time is " + fmEtime,
						Toast.LENGTH_SHORT).show();
			}
		};

		TimePickerDialog alert2 = new TimePickerDialog(this, mTimeSetListener2,
				chour, csec, false);
		alert2.show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.carnnecting_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent;
		switch (item.getItemId()) {
		case R.id.news_feed:
			// app icon in action bar clicked; go home
			intent = new Intent(this, Home.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			return true;
		case R.id.categories:
			intent = new Intent(this, CategoryMenu.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent.putExtra("userId", userId);
			startActivity(intent);
			return true;
		case R.id.my_events:
			intent = new Intent(this, MyEvents.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent.putExtra("userId", userId);
			startActivity(intent);
			return true;
		case R.id.favorites:
			intent = new Intent(this, Favorites.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent.putExtra("userId", userId);
			startActivity(intent);
			return true;
		case R.id.create_event:
			intent = new Intent(this, CreateEvent.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent.putExtra("userId", userId);
			startActivity(intent);
			return true;
		case R.id.logout:
			System.out.println("***LOGOUT***");
			Logout logout = new Logout();
			logout.FBLogout();
			finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void updateDisplay() {
		showDate.setText(new StringBuilder().append(
				"Your event will be held at ").append(fmDate));
	}

	private void updateDisplayS() {
		showStime.setText(new StringBuilder().append(fmStime));
	}

	private void updateDisplayE() {
		showEtime.setText(new StringBuilder().append(fmEtime));
	}

	private void updateDisplayC() {
		showCategory.setText(new StringBuilder().append(cMsg));
	}

	public void GetLocations() {
		StringBuffer juso = new StringBuffer();
		latPoint = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLatitude();
		longPoint = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLongitude(); 
		//latPoint = myLocation.getLatitude();
		//longPoint = myLocation.getLongitude();
		try {
			List<Address> addresses;
			addresses = geoCoder.getFromLocation(latPoint, longPoint, 1);
			for (Address addr : addresses) {
				int index = addr.getMaxAddressLineIndex();
				for (int i = 0; i <= index; i++) {
					//juso.append(addr.getAddressLine(i));
					//juso.append(" ");
				}
				//juso.append("\n");
				country = addr.getCountryCode();
				city = addr.getLocality();
				street = addr.getThoroughfare();
				/*
				Address address = addresses.get(0);
				juso.append(addr.getCountryName()).append("\n");
                juso.append(addr.getPostalCode()).append("\n");
                juso.append(addr.getLocality()).append("\n");
                juso.append(addr.getThoroughfare()).append("\n");
                juso.append(addr.getFeatureName()).append("\n");
                */				
			}
			jusoText.setText(street + ", " + city + ", " + country);
			Log.d("location", "set jusotext");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		myLocation = location;
		Log.d("location", "locations");
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}
}
