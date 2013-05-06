package com.carnnecting.util;

import com.cmu.carnnecting.R;
import android.app.Activity;
import android.media.MediaPlayer;

public class SoundEffect{
	private MediaPlayer mediaPlayer;
	private Activity activity;
	
	public SoundEffect(Activity activity){
		this.activity = activity;
	}
	
	public void playSubscribeSound() throws Exception 
	{
		mediaPlayer = MediaPlayer.create(this.activity, R.raw.subscribe_tone);
		mediaPlayer.start();
	}
}