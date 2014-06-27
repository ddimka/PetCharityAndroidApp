package com.work.petcharityandroid;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.json.gson.GsonFactory;
import com.work.petcharity.kalbiyaendpoint.Kalbiyaendpoint;
import com.work.petcharity.kalbiyaendpoint.model.CollectionResponseKalbiya;
import com.work.petcharity.kalbiyaendpoint.model.Kalbiya;
import com.work.petcharity.petendpoint.Petendpoint;
import com.work.petcharity.petendpoint.model.Pet;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

public class AddPetActivity extends Activity {

	EditText editPetName;
	EditText editComment;
	Spinner editKalbiya;
	EditText editNeedMoney;
	EditText editHaveMoney;
	EditText editDeathDate;
	ImageView pictFrame;
	Bitmap captureBmp;
	Bitmap smallBmp;
	byte[] bArray;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_pet);

		
		
		editPetName = (EditText) findViewById(R.id.editPetName);
		editComment = (EditText) findViewById(R.id.editComment);
		editKalbiya = (Spinner) findViewById(R.id.editKalbiya);
		editNeedMoney = (EditText) findViewById(R.id.editNeedMoney);
		editHaveMoney = (EditText) findViewById(R.id.editHaveMoney);
		editDeathDate = (EditText) findViewById(R.id.editDeathDate);

		new KalbiyaListAsyncTask().execute();
		
		pictFrame = (ImageView) findViewById(R.id.imgFrame);
		pictFrame.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				final Intent intent = new Intent(
						MediaStore.ACTION_IMAGE_CAPTURE);
				intent.putExtra(MediaStore.EXTRA_OUTPUT,
						Uri.fromFile(getTempFile(AddPetActivity.this)));
				startActivityForResult(intent, 1);
			}
		});

		// Event Listener for About App button
		Button btnAddQuote = (Button) findViewById(R.id.btnAddQuote);
		btnAddQuote.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				// Check if values are provided
				String txtPetName = editPetName.getText().toString().trim();
				String txtComment = editComment.getText().toString().trim();
				String txtKalbiya = editKalbiya.getSelectedItem().toString().trim();
				String txtNeedMoney = editNeedMoney.getText().toString().trim();
				String txtHaveMoney = editHaveMoney.getText().toString().trim();
				// String txtPicture = "";
				// //editPictureLink.getText().toString().trim();
				String txtDeathDate = editDeathDate.getText().toString().trim();

				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				Log.d("Test", "Compress");
				smallBmp.compress(Bitmap.CompressFormat.PNG, 100, bos);
				Log.d("Test", "Create bArray");
				byte[] bArray = bos.toByteArray();

				// Go ahead and perform the transaction
				Object[] params = { txtPetName, txtComment, txtKalbiya,
						txtNeedMoney, txtHaveMoney, bArray, txtDeathDate };
				new AddPetAsyncTask(AddPetActivity.this).execute(params);

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
			case 1:
				final File file = getTempFile(this);
				try {
					captureBmp = Media.getBitmap(getContentResolver(),
							Uri.fromFile(file));
					// Log.d("Test", file.toString());
					// do whatever you want with the bitmap (Resize, Rename, Add
					// To Gallery, etc)
					smallBmp = ShrinkBitmap(file.toString(), 300, 300);
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
				pet.setPetName((String) (params[0]));
				pet.setDescription((String) (params[1]));
				pet.setKalbiya((String) (params[2]));
				pet.setMoneyNeeded((String) (params[3]));
				pet.setMoneyHave((String) (params[4]));
				pet.encodePicture((byte[]) params[5]);
				pet.setDeathDate((String) (params[6]));

				response = service.insertPet(pet).execute();
				Log.d("Test", response.toString());
			} catch (Exception e) {
				Log.d("Could not add pet", e.getMessage(), e);
			}
			return response;
		}

		protected void onPostExecute(Pet pet) {
			// Clear the progress dialog and the fields
			pd.dismiss();
			editPetName.setText("");
			editComment.setText("");
//			editKalbiya.setSelection();
			editNeedMoney.setText("");
			editHaveMoney.setText("");
			pictFrame.setImageDrawable(getResources().getDrawable(
					R.drawable.ic_launcher));
			editDeathDate.setText("");

			// Display success message to user
			Toast.makeText(getBaseContext(), "Pet added succesfully",
					Toast.LENGTH_SHORT).show();
		}

	}

	private class KalbiyaListAsyncTask extends
			AsyncTask<Void, Void, CollectionResponseKalbiya> {
//		Context context;
		List<Kalbiya> allKalbiyaList;

/*		public KalbiyaListAsyncTask(Context context) {
			this.context = context;
		}*/

		protected void onPreExecute() {
			super.onPreExecute();
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
			//pd.dismiss();
			List<String> data = new ArrayList<String>();

			for (int i = 0; i < allKalbiyaList.size(); i++)
				data.add(String.valueOf((allKalbiyaList.get(i).getName())));

			
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(AddPetActivity.this, android.R.layout.simple_spinner_item, data);
	        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	        editKalbiya.setAdapter(adapter);
	        


		}
	}
}
