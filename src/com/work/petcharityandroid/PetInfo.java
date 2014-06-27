package com.work.petcharityandroid;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.os.Build;

public class PetInfo extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pet_info);

		TextView txtName = (TextView) findViewById(R.id.pagerPetName);
		TextView txtDescription = (TextView) findViewById(R.id.description);
		TextView txtKalbiya = (TextView) findViewById(R.id.kalbiya);
		TextView txtNeedmoney = (TextView) findViewById(R.id.needmoney);
		TextView txtHavemoney = (TextView) findViewById(R.id.havemoney);
		TextView txtDeathdate = (TextView) findViewById(R.id.deathdate);
		ImageView picture = (ImageView) findViewById(R.id.picture);
		
		Intent intent = getIntent();
		String name = intent.getStringExtra("name");
	    String description = intent.getStringExtra("description");
		String kalbiya = intent.getStringExtra("kalbiya");
	    String needmoney = intent.getStringExtra("needmoney");
		String havemoney = intent.getStringExtra("havemoney");
	    String deathdate = intent.getStringExtra("deathdate");
	    byte[] pictArray = intent.getByteArrayExtra("picture");
	    
	    

 	    Bitmap bitmap = BitmapFactory.decodeByteArray(pictArray , 0, pictArray.length);
	    
	    txtName.setText(name);
	    txtDescription.setText(description);
	    txtKalbiya.setText(kalbiya);
	    txtNeedmoney.setText(needmoney);
	    txtHavemoney.setText(havemoney);
	    txtDeathdate.setText(deathdate);
	    picture.setImageBitmap(bitmap);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.pet_info, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}


}
