package com.work.petcharityandroid;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.os.Bundle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.json.gson.GsonFactory;
import com.work.petcharity.kalbiyaendpoint.Kalbiyaendpoint;
import com.work.petcharity.kalbiyaendpoint.model.CollectionResponseKalbiya;
import com.work.petcharity.kalbiyaendpoint.model.Kalbiya;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class ListKalbiyaActivity extends ListActivity {
	private TextView tv = null;
	private ArrayList<Map<String, String>> list = null;
	List<Kalbiya> allKalbiyaList;
	private SimpleAdapter adapter = null;
	private Kalbiyaendpoint service;
	private Kalbiya currentOffice;
	private String[] from = { "Name", "Chief" };
	private int[] to = { android.R.id.text1, android.R.id.text2 };

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		tv = new TextView(this);
		tv.setText("List of all kennels");
		tv.setGravity(Gravity.CENTER);
		getListView().addHeaderView(tv);
		new KalbiyaListAsyncTask(this).execute();
	}

	public class KalbiyaListAsyncTask extends
			AsyncTask<Void, Void, CollectionResponseKalbiya> {
		Context context;
		private ProgressDialog pd;

		public KalbiyaListAsyncTask(Context context) {
			this.context = context;
		}

		protected void onPreExecute() {
			super.onPreExecute();
			pd = new ProgressDialog(context);
			pd.setMessage("Retrieving list...");
			pd.show();
		}

		protected CollectionResponseKalbiya doInBackground(Void... unused) {
			CollectionResponseKalbiya kalbiya = null;
			try {
				Kalbiyaendpoint.Builder builder = new Kalbiyaendpoint.Builder(
						AndroidHttp.newCompatibleTransport(),
						new GsonFactory(), null);
				service = builder.build();
				kalbiya = service.listKalbiya().execute();
			} catch (Exception e) {
				Log.d("Test", "Could not retrieve kalbiya's list");
			}
			return kalbiya;
		}

		protected void onPostExecute(CollectionResponseKalbiya kalbiya) {
			pd.dismiss();

			list = new ArrayList<Map<String, String>>();
			allKalbiyaList = kalbiya.getItems();
			
			if (allKalbiyaList == null) {
				Toast.makeText(ListKalbiyaActivity.this, "Empty list. Please add kennels", Toast.LENGTH_LONG).show();
				finish();
				return;
			}

			for (Kalbiya office : allKalbiyaList) {
				HashMap<String, String> item = new HashMap<String, String>();

				item.put("Name", office.getName());
				item.put("Chief", "Resp. person: " + office.getResponsiblePerson());
				list.add(item);

			}
			adapter = new SimpleAdapter(ListKalbiyaActivity.this, list,
					android.R.layout.simple_list_item_2, from, to);

			
			setListAdapter(adapter);

		}
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {

		currentOffice = allKalbiyaList.get(position - 1);

		AlertDialog.Builder builder = new AlertDialog.Builder(
				ListKalbiyaActivity.this);
		builder.setTitle("Kalbiya: " + currentOffice.getName());
		builder.setMessage("\nPhone: " + currentOffice.getPhone() + "\nChief: "
				+ currentOffice.getResponsiblePerson() + "\nChief phone: "
				+ currentOffice.getResponsiblePersonTel() + "\nPayPal: "
				+ currentOffice.getPayPalAccount());
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.setNegativeButton("Delete", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						ListKalbiyaActivity.this);
				builder.setTitle("Warning!");
				builder.setMessage("Are you sure?");
				builder.setPositiveButton(
						"Yes",
						new DialogInterface.OnClickListener() {

							public void onClick(
									DialogInterface dialog,
									int which) {
								new DeleteKalbiyaAsyncTask(
										ListKalbiyaActivity.this)
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
		builder.setNeutralButton("Edit", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				Intent intent = new Intent(ListKalbiyaActivity.this, AddKalbiyaActivity.class);
		    	intent.putExtra("id", currentOffice.getId());
		        intent.putExtra("name", currentOffice.getName());
		    	intent.putExtra("phone", currentOffice.getPhone());
		        intent.putExtra("person", currentOffice.getResponsiblePerson());
		    	intent.putExtra("personphone", currentOffice.getResponsiblePersonTel());
		    	intent.putExtra("paypal", currentOffice.getPayPalAccount());

				startActivity(intent);
			}
		});
		builder.show();
	}
    
	private class DeleteKalbiyaAsyncTask extends AsyncTask<Object, Void, Kalbiya> {
		private ProgressDialog pd;
		Context context;

		public DeleteKalbiyaAsyncTask(Context context) {
			this.context = context;
		}

		protected void onPreExecute() {
			super.onPreExecute();
			pd = new ProgressDialog(context);
			pd.setMessage("Deleting the kennel...");
			pd.show();
		}

		protected Kalbiya doInBackground(Object... params) {
			try {

				service.removeKalbiya(currentOffice.getId()).execute();

			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;

		}

		protected void onPostExecute(Kalbiya kalbiya) {
			// Clear the progress dialog and the fields
			pd.dismiss();
			Intent intent = new Intent(ListKalbiyaActivity.this,
					MainActivity.class);
			startActivity(intent);
		}

	}
}	

