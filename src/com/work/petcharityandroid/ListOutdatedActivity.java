package com.work.petcharityandroid;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.json.gson.GsonFactory;
import com.work.petcharity.petendpoint.Petendpoint;
import com.work.petcharity.petendpoint.model.CollectionResponsePet;
import com.work.petcharity.petendpoint.model.Pet;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class ListOutdatedActivity extends Activity {

	private ArrayList<Map<String, String>> list = null;
	private List<Pet> allPetsList;
	private List<Pet> listPetsToDelete;
	private ListView listOutdated;
	private TextView txtInfo;
	private Button btnDelete;
	private SimpleAdapter adapter = null;
	private String[] from = { "Name", "Date" };
	private int[] to = { android.R.id.text1, android.R.id.text2 };
	private Petendpoint service;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_outdated);
		listPetsToDelete = new ArrayList<Pet>();
		txtInfo = (TextView)findViewById(R.id.txtInfo);
		listOutdated = (ListView) findViewById(R.id.lstOutdated2);
		btnDelete = (Button) findViewById(R.id.btnDelete);
		btnDelete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				AlertDialog.Builder builder = new AlertDialog.Builder(
						ListOutdatedActivity.this);
				builder.setTitle("Warning!");
				builder.setMessage("Are you sure?");
				builder.setPositiveButton(
						"Yes",
						new DialogInterface.OnClickListener() {

							public void onClick(
									DialogInterface dialog,
									int which) {
								new DeleteOutdatedAsyncTask(ListOutdatedActivity.this)
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

		new PetsListAsyncTask(this).execute();

	}
	private class PetsListAsyncTask extends
			AsyncTask<Void, Void, CollectionResponsePet> {
		Context context;
		private ProgressDialog pd;

		public PetsListAsyncTask(Context context) {
			this.context = context;
		}

		protected void onPreExecute() {
			super.onPreExecute();
			pd = new ProgressDialog(context);
			pd.setMessage("Retrieving pets...");
			pd.show();
		}

		protected CollectionResponsePet doInBackground(Void... unused) {
			CollectionResponsePet pets = null;
			try {
				Petendpoint.Builder builder = new Petendpoint.Builder(
						AndroidHttp.newCompatibleTransport(),
						new GsonFactory(), null);
				service = builder.build();
				pets = service.listPet().execute();
			} catch (Exception e) {
				Log.d("Test", "Could not retrieve pets");
			}
			return pets;
		}

		protected void onPostExecute(CollectionResponsePet pets) {
			pd.dismiss();

			// Do something with the result.
			// ArrayList<Map<String, String>>
			list = new ArrayList<Map<String, String>>();
			allPetsList = pets.getItems();

			if (allPetsList != null) {
				for (Pet pet : allPetsList) {
					HashMap<String, String> itemOfListView = new HashMap<String, String>();

					SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
					Date strDate;
					try {
						strDate = sdf.parse(pet.getDeathDate());
						if (new Date().after(strDate)) {
							itemOfListView.put(
									"Name", "Name: " 
											+ pet.getPetName() + "\nKennel: "
											+ pet.getKalbiya() + "\nMoney: "
											+ pet.getMoneyHave() + "/"
											+ pet.getMoneyHave());
							itemOfListView.put("Date",
									"Date: " + pet.getDeathDate());
							list.add(itemOfListView);
							listPetsToDelete.add(pet);
						}
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}
				adapter = new SimpleAdapter(ListOutdatedActivity.this, list,
						android.R.layout.simple_list_item_2, from, to);

				listOutdated.setAdapter(adapter);
				if (listPetsToDelete.size() == 0) {
					btnDelete.setEnabled(false);
					txtInfo.setText("Nothing to remove");
				}

			} else {
				
				Toast.makeText(ListOutdatedActivity.this, "Nothing to delete",
						Toast.LENGTH_LONG).show();
				finish();
			}

		}
	}

	private class DeleteOutdatedAsyncTask extends AsyncTask<Object, Void, Pet> {
		private ProgressDialog pd;
		Context context;

		public DeleteOutdatedAsyncTask(Context context) {
			this.context = context;
		}

		protected void onPreExecute() {
			super.onPreExecute();
			pd = new ProgressDialog(context);
			pd.setMessage("Deleting pets. Please be patients..");
			pd.show();
		}

		protected Pet doInBackground(Object... params) {
			for (Pet pet : listPetsToDelete) {
				try {
					service.removePet(pet.getId()).execute();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return null;

		}

		protected void onPostExecute(Pet pet) {
			// Clear the progress dialog and the fields
			pd.dismiss();
			Intent intent = new Intent(ListOutdatedActivity.this,
					MainActivity.class);
			startActivity(intent);
		}

	}
}
