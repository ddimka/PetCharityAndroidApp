package com.work.petcharityandroid;

import java.io.IOException;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.json.gson.GsonFactory;
import com.work.petcharity.petendpoint.Petendpoint;
import com.work.petcharity.petendpoint.model.Pet;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class PetInfo extends Activity {
	private Petendpoint service;
	Long id;
	String name;
    String description;
	String kalbiya;
    String needmoney;
	String havemoney;
    String deathdate;
    byte[] pictArray;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pet_info);

		TextView txtName = (TextView) findViewById(R.id.editPetName);
		TextView txtDescription = (TextView) findViewById(R.id.txtKalbiyaPhone);
		TextView txtKalbiya = (TextView) findViewById(R.id.txtKalbiyaRespPerson);
		TextView txtNeedmoney = (TextView) findViewById(R.id.txtRespPersonPhone);
		TextView txtHavemoney = (TextView) findViewById(R.id.txtPayPalAcc);
		TextView txtDeathdate = (TextView) findViewById(R.id.deathdate);
		ImageView picture = (ImageView) findViewById(R.id.picture);
		
		Intent intent = getIntent();
		id = intent.getLongExtra("id", 0);
		name = intent.getStringExtra("name");
	    description = intent.getStringExtra("description");
		kalbiya = intent.getStringExtra("kalbiya");
	    needmoney = intent.getStringExtra("needmoney");
		havemoney = intent.getStringExtra("havemoney");
	    deathdate = intent.getStringExtra("deathdate");
	    pictArray = intent.getByteArrayExtra("picture");
	    
	    

 	    Bitmap bitmap = BitmapFactory.decodeByteArray(pictArray , 0, pictArray.length);
	    
	    txtName.setText("Name: " + name);
	    txtDescription.setText("Description: " + description);
	    txtKalbiya.setText("Kennel: " + kalbiya);
	    txtNeedmoney.setText("Need Money: " + needmoney);
	    txtHavemoney.setText("Already have: " + havemoney);
	    txtDeathdate.setText("Death date: " + deathdate);
	    picture.setImageBitmap(bitmap);
	    picture.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {


				AlertDialog.Builder builder = new AlertDialog.Builder(
						PetInfo.this);
				builder.setTitle("Name: " + name);
				builder.setMessage("What do you want to do?");
				builder.setNeutralButton("Edit",
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int which) {
								Intent intent = new Intent(
										PetInfo.this,
										AddPetActivity.class);
								intent.putExtra("intentId", id);
								intent.putExtra("intentPetName", name);
								intent.putExtra("intentComment",description);
								intent.putExtra("intentKalbiya",kalbiya);
								intent.putExtra("intentNeedMoney",needmoney);
								intent.putExtra("intentHaveMoney",havemoney);
								intent.putExtra("intentDeathDate",havemoney);
								intent.putExtra("intentPicture", pictArray);
								startActivity(intent);

							}
						});

				builder.setNegativeButton("Delete",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {

								AlertDialog.Builder builder = new AlertDialog.Builder(
										PetInfo.this);
								builder.setTitle("Warning!");
								builder.setMessage("Are you sure?");
								builder.setPositiveButton(
										"Yes",
										new DialogInterface.OnClickListener() {

											public void onClick(
													DialogInterface dialog,
													int which) {
												new DeletePetAsyncTask(
														PetInfo.this)
														.execute();
											}
										});
								builder.setNegativeButton(
										"No",
										new DialogInterface.OnClickListener() {

											public void onClick(
													DialogInterface dialog,
													int which) {
												dialog.dismiss();
											}
										});
								builder.show();

								

							}
						});
				builder.setPositiveButton("OK",
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
							}
						});
				builder.show();
				return false;
			}
		});
	    
	}


	 private class DeletePetAsyncTask extends AsyncTask<Object, Void, Pet> {
			private ProgressDialog pd;
			Context context;

			public DeletePetAsyncTask(Context context) {
				this.context = context;
			}

			protected void onPreExecute() {
				super.onPreExecute();
				pd = new ProgressDialog(context);
				pd.setMessage("Deleting the pet...");
				pd.show();
			}

			protected Pet doInBackground(Object... params) {
				try {
					Log.d("Test", "Shoud delete id = " + id);
					Petendpoint.Builder builder = new Petendpoint.Builder(
							AndroidHttp.newCompatibleTransport(),
							new GsonFactory(), null);
					service = builder.build();
					service.removePet(id).execute();

				} catch (IOException e) {
					e.printStackTrace();
				}
				return null;

			}

			protected void onPostExecute(Pet pet) {
				// Clear the progress dialog and the fields
				pd.dismiss();
				Intent intent = new Intent(PetInfo.this,
						MainActivity.class);
				startActivity(intent);
			}

		}

}
