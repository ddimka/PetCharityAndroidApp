package com.work.petcharityandroid;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.os.Bundle;

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
import android.os.AsyncTask;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class ListKalbiyaActivity extends ListActivity {
	private TextView tv = null;
	private ArrayList<Map<String, String>> list = null;
	List<Kalbiya> allKalbiyaList;
	private SimpleAdapter adapter = null;
	private String[] from = { "Name", "Chief" };
	private int[] to = { android.R.id.text1, android.R.id.text2 };

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		tv = new TextView(this);
		tv.setText("List of all kalbiyas");
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
				Kalbiyaendpoint service = builder.build();
				kalbiya = service.listKalbiya().execute();
			} catch (Exception e) {
				Log.d("Test", "Could not retrieve kalbiya's list");
			}
			return kalbiya;
		}

		protected void onPostExecute(CollectionResponseKalbiya kalbiya) {
			pd.dismiss();

			// Do something with the result.
			// ArrayList<Map<String, String>>
			list = new ArrayList<Map<String, String>>();
			allKalbiyaList = kalbiya.getItems();

			for (Kalbiya office : allKalbiyaList) {
				HashMap<String, String> item = new HashMap<String, String>();

				item.put("Name", office.getName());
				item.put("Chief", office.getResponsiblePerson());
				list.add(item);

			}
			adapter = new SimpleAdapter(ListKalbiyaActivity.this, list,
					android.R.layout.simple_list_item_2, from, to);

			setListAdapter(adapter);

		}
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {

		Kalbiya currentOffice = allKalbiyaList.get(position - 1);

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
		builder.show();
	}

}