package com.work.petcharityandroid;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.json.gson.GsonFactory;
import com.work.petcharity.kalbiyaendpoint.Kalbiyaendpoint;
import com.work.petcharity.kalbiyaendpoint.model.CollectionResponseKalbiya;
import com.work.petcharity.kalbiyaendpoint.model.Kalbiya;
import com.work.petcharity.petendpoint.Petendpoint;
import com.work.petcharity.petendpoint.model.Pet;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class AddPetActivity extends Activity {

	private static final int LOAD_IMAGE_FROM_CAMERA = 1;
	private static final int LOAD_IMAGE_FROM_GALERY = 2;
	private static final int ADD_NEW_PET = 3;
	private static final int UPDATE_PET = 4;
	EditText editPetName;
	EditText editComment;
	Spinner editKalbiya;
	EditText editNeedMoney;
	EditText editHaveMoney;
	TextView editDeathDate;
	TextView selectKalbiya;
	ImageView pictFrame;
	Button btnAddUpdate;
	Bitmap captureBmp;
	Bitmap smallBmp;
	byte[] bArray;
	ByteArrayOutputStream bos;
	Calendar calendar;
	int process;
	int mYear;
	int mMonth;
	int mDay;
	List<Kalbiya> allKalbiyaList;
	ArrayAdapter<String> adapter;
	String fromIntentKalbiya;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_pet);

		calendar = Calendar.getInstance();
		final int mYear = calendar.get(Calendar.YEAR);
		final int mMonth = calendar.get(Calendar.MONTH);
		final int mDay = calendar.get(Calendar.DAY_OF_MONTH);

		new KalbiyaListAsyncTask().execute();

		Intent intent = getIntent();

		final Long fromIntentId = intent.getLongExtra("intentId", 0);
		String fromIntentPetName = intent.getStringExtra("intentPetName");
		String fromIntentComment = intent.getStringExtra("intentComment");
		fromIntentKalbiya = intent.getStringExtra("intentKalbiya");
		String fromIntentNeedMoney = intent.getStringExtra("intentNeedMoney");
		String fromIntentHaveMoney = intent.getStringExtra("intentHaveMoney");
		String fromIntentDeathDate = intent.getStringExtra("intentDeathDate");
		byte[] fromIntentPicture = intent.getByteArrayExtra("intentPicture");
		


		Log.d("Test", "Id = " + fromIntentId);

		editPetName = (EditText) findViewById(R.id.editPetName);
		editComment = (EditText) findViewById(R.id.editComment);
		editKalbiya = (Spinner) findViewById(R.id.editKalbiya);
		editNeedMoney = (EditText) findViewById(R.id.editNeedMoney);
		editHaveMoney = (EditText) findViewById(R.id.editHaveMoney);
		editDeathDate = (TextView) findViewById(R.id.editDeathDate);
		pictFrame = (ImageView) findViewById(R.id.imgFrame);
		btnAddUpdate = (Button) findViewById(R.id.btnAddPet);

		Log.d("Test", "Update = " + fromIntentPetName);
		if (fromIntentId == 0) {
			editPetName.setText("");
			process = ADD_NEW_PET;
			btnAddUpdate.setText("Add");
		} else {
			editPetName.setText(fromIntentPetName);
			process = UPDATE_PET;
			btnAddUpdate.setText("Update");
		}
		if (fromIntentComment == "")
			editComment.setText("");
		else
			editComment.setText(fromIntentComment);
		if (fromIntentNeedMoney == "")
			editNeedMoney.setText("");
		else
			editNeedMoney.setText(fromIntentNeedMoney);
		if (fromIntentHaveMoney == "")
			editHaveMoney.setText("");
		else
			editHaveMoney.setText(fromIntentHaveMoney);
		
		if (fromIntentPicture == null)
			bArray = null;
		else {
			bArray = fromIntentPicture.clone();
			smallBmp = BitmapFactory.decodeByteArray(bArray, 0, bArray.length);
			pictFrame.setImageBitmap(smallBmp);
		}

		if (fromIntentDeathDate == null)
			editDeathDate.setText(mDay + "/" + mMonth + "/" + mYear);
		else
			editDeathDate.setText(fromIntentDeathDate);

		editDeathDate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				DatePickerDialog dpd = new DatePickerDialog(
						AddPetActivity.this,
						new DatePickerDialog.OnDateSetListener() {

							@Override
							public void onDateSet(DatePicker view, int year,
									int monthOfYear, int dayOfMonth) {
								editDeathDate.setText(dayOfMonth + "/"
										+ (monthOfYear + 1) + "/" + year);

							}

						}, mYear, mMonth, mDay);
				dpd.show();

			}
		});

		pictFrame.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				AlertDialog.Builder builder = new AlertDialog.Builder(
						AddPetActivity.this);
				builder.setTitle("Select image source");
				// builder.setMessage("Pet Charity\nVersion 1.0");
				builder.setPositiveButton("Camera",
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int which) {
								final Intent intent = new Intent(
										MediaStore.ACTION_IMAGE_CAPTURE);
								intent.putExtra(
										MediaStore.EXTRA_OUTPUT,
										Uri.fromFile(getTempFile(AddPetActivity.this)));
								startActivityForResult(intent,
										LOAD_IMAGE_FROM_CAMERA);
								dialog.dismiss();
							}
						});
				builder.setNegativeButton("Galery",
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int which) {
								Intent i = new Intent(
										Intent.ACTION_PICK,
										android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

								startActivityForResult(i,
										LOAD_IMAGE_FROM_GALERY);
								dialog.dismiss();
							}
						});
				builder.show();

			}
		});

		btnAddUpdate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Long txtPetId = fromIntentId;
				Log.d("Test", "txtPetId = " + txtPetId);
				String txtPetName = editPetName.getText().toString().trim();
				String txtComment = editComment.getText().toString().trim();
				String txtKalbiya = editKalbiya.getSelectedItem()
						.toString().trim();
				String txtNeedMoney = editNeedMoney.getText().toString()
						.trim();
				String txtHaveMoney = editHaveMoney.getText().toString()
						.trim();
				// String txtPicture = "";
				// //editPictureLink.getText().toString().trim();
				String txtDeathDate = editDeathDate.getText().toString()
						.trim();

				if (smallBmp == null) {
					smallBmp = BitmapFactory.decodeResource(getResources(),
							R.drawable.ic_launcher);
				}
				bos = new ByteArrayOutputStream();
				smallBmp.compress(Bitmap.CompressFormat.PNG, 100, bos);
				bArray = bos.toByteArray();

				// Go ahead and perform the transaction
				Object[] params = { txtPetId, txtPetName, txtComment, txtKalbiya,
						txtNeedMoney, txtHaveMoney, bArray, txtDeathDate };
				
				Log.d("Test", "process = " + process);
				if (process == ADD_NEW_PET) {
					Log.d("Test", "Adding new pet");
					new AddPetAsyncTask(AddPetActivity.this).execute(params);
				}
				if (process == UPDATE_PET) {
					Log.d("Test", "Updating pet");
					new UpdatePetAsyncTask(AddPetActivity.this).execute(params);
				}
				
			}
		});

	}

	private File getTempFile(Context context) {
		// it will return /sdcard/image.tmp
		final File path = new File(Environment.getExternalStorageDirectory(),
				context.getPackageName());
		if (!path.exists()) {
			path.mkdir();
		}
		return new File(path, "image.tmp");
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case LOAD_IMAGE_FROM_CAMERA:
				final File fileFromCamera = getTempFile(this);
				try {
					captureBmp = Media.getBitmap(getContentResolver(),
							Uri.fromFile(fileFromCamera));
					smallBmp = ShrinkBitmap(fileFromCamera.toString(), 300, 300);
					pictFrame.setImageBitmap(captureBmp);

				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;
			case LOAD_IMAGE_FROM_GALERY:
				if (resultCode == RESULT_OK) {
					Uri selectedImage = data.getData();
					String[] filePathColumn = { MediaStore.Images.Media.DATA };

					Cursor cursor = getContentResolver().query(selectedImage,
							filePathColumn, null, null, null);
					cursor.moveToFirst();

					int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
					String picturePath = cursor.getString(columnIndex);
					cursor.close();

					// String picturePath contains the path of selected Image
					final File fileFromGalery = new File(picturePath);
					try {
						captureBmp = Media.getBitmap(getContentResolver(),
								Uri.fromFile(fileFromGalery));
						smallBmp = ShrinkBitmap(picturePath.toString(), 300,
								300);
						pictFrame.setImageBitmap(captureBmp);

					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
					break;
				}
			}
		}
	}

	Bitmap ShrinkBitmap(String file, int width, int height) {

		BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
		bmpFactoryOptions.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeFile(file, bmpFactoryOptions);

		int heightRatio = (int) Math.ceil(bmpFactoryOptions.outHeight
				/ (float) height);
		int widthRatio = (int) Math.ceil(bmpFactoryOptions.outWidth
				/ (float) width);

		if (heightRatio > 1 || widthRatio > 1) {
			if (heightRatio > widthRatio) {
				bmpFactoryOptions.inSampleSize = heightRatio;
			} else {
				bmpFactoryOptions.inSampleSize = widthRatio;
			}
		}

		bmpFactoryOptions.inJustDecodeBounds = false;
		bitmap = BitmapFactory.decodeFile(file, bmpFactoryOptions);
		return bitmap;
	}

	private class AddPetAsyncTask extends AsyncTask<Object, Void, Pet> {
		Context context;
		private ProgressDialog pd;

		public AddPetAsyncTask(Context context) {
			this.context = context;
		}

		protected void onPreExecute() {
			super.onPreExecute();
			pd = new ProgressDialog(context);
			pd.setMessage("Adding the pet...");
			pd.show();
		}

		protected Pet doInBackground(Object... params) {
			Pet response = null;
			try {
				Petendpoint.Builder builder = new Petendpoint.Builder(
						AndroidHttp.newCompatibleTransport(),
						new GsonFactory(), null);
				Petendpoint service = builder.build();
				Pet pet = new Pet();
				//pet.setId((Long) (params[0]));
				pet.setPetName((String) (params[1]));
				pet.setDescription((String) (params[2]));
				pet.setKalbiya((String) (params[3]));
				pet.setMoneyNeeded((String) (params[4]));
				pet.setMoneyHave((String) (params[5]));
				pet.encodePicture((byte[]) params[6]);
				pet.setDeathDate((String) (params[7]));

				response = service.insertPet(pet).execute();
			} catch (Exception e) {
				Log.d("Could not add pet", e.getMessage(), e);
			}
			return response;
		}

		protected void onPostExecute(Pet pet) {
			// Clear the progress dialog and the fields
			pd.dismiss();
			Intent intent = new Intent(AddPetActivity.this, MainActivity.class);
			startActivity(intent);
		}

	}

	private class UpdatePetAsyncTask extends AsyncTask<Object, Void, Pet> {
		Context context;
		private ProgressDialog pd;

		public UpdatePetAsyncTask(Context context) {
			this.context = context;
		}

		protected void onPreExecute() {
			super.onPreExecute();
			pd = new ProgressDialog(context);
			pd.setMessage("Updating the pet...");
			pd.show();
		}

		protected Pet doInBackground(Object... params) {
			Pet response = null;
			try {
				Petendpoint.Builder builder = new Petendpoint.Builder(
						AndroidHttp.newCompatibleTransport(),
						new GsonFactory(), null);
				Petendpoint service = builder.build();
				Pet pet = new Pet();
				pet.setId((Long) (params[0]));
				pet.setPetName((String) (params[1]));
				pet.setDescription((String) (params[2]));
				pet.setKalbiya((String) (params[3]));
				pet.setMoneyNeeded((String) (params[4]));
				pet.setMoneyHave((String) (params[5]));
				pet.encodePicture((byte[]) params[6]);
				pet.setDeathDate((String) (params[7]));

				response = service.updatePet(pet).execute();
			} catch (Exception e) {
				Log.d("Could not update pet", e.getMessage(), e);
			}
			return response;
		}

		protected void onPostExecute(Pet pet) {
			// Clear the progress dialog and the fields
			pd.dismiss();
			Intent intent = new Intent(AddPetActivity.this, MainActivity.class);
			startActivity(intent);
			Toast.makeText(getBaseContext(), "Pet updated succesfully",
					Toast.LENGTH_SHORT).show();
		}

	}
	
	private class KalbiyaListAsyncTask extends
			AsyncTask<Void, Void, CollectionResponseKalbiya> {
		// Context context;

		private ProgressDialog pd;

		protected void onPreExecute() {
			super.onPreExecute();
			pd = new ProgressDialog(AddPetActivity.this);
			pd.setMessage("Retrieving kalbya's list...");
			pd.show();
		}

		protected CollectionResponseKalbiya doInBackground(Void... unused) {
			CollectionResponseKalbiya kalbiya = null;
			try {
				Kalbiyaendpoint.Builder builder = new Kalbiyaendpoint.Builder(
						AndroidHttp.newCompatibleTransport(),
						new GsonFactory(), null);
				Kalbiyaendpoint service = builder.build();
				kalbiya = service.listKalbiya().execute();
				allKalbiyaList = kalbiya.getItems();

			} catch (Exception e) {
				Log.d("Test", "Could not retrieve list");
			}
			return kalbiya;
		}

		protected void onPostExecute(CollectionResponseKalbiya kalbiya) {
		    pd.dismiss();
			List<String> data = new ArrayList<String>();
			
			if (allKalbiyaList != null) {
				for (int i = 0; i < allKalbiyaList.size(); i++)
					data.add(String.valueOf((allKalbiyaList.get(i).getName())));

				adapter = new ArrayAdapter<String>(
						AddPetActivity.this,
						android.R.layout.simple_spinner_item, data);
				adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				editKalbiya.setAdapter(adapter);
				for (int i = 0; i < allKalbiyaList.size(); i++)
					if (allKalbiyaList.get(i).getName().equals(fromIntentKalbiya))
						editKalbiya.setSelection(i);
			} else {
				Toast.makeText(AddPetActivity.this, "Internet problem?",
						Toast.LENGTH_LONG).show();
				finish();
			}

		}
	}
}
