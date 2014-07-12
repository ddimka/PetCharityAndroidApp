package com.work.petcharityandroid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.json.gson.GsonFactory;
import com.work.petcharity.paymentsendpoint.Paymentsendpoint;
import com.work.petcharity.paymentsendpoint.model.CollectionResponsePayments;
import com.work.petcharity.paymentsendpoint.model.Payments;
import com.work.petcharity.petendpoint.model.Pet;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class PaymentsList extends ListActivity {

	private ArrayList<Map<String, String>> list = null;
	List<Payments> allPaymentsList;
	List<Payments> paymentsByKalbiya;
	List<String> kalbiyaList;
	List<String> paymentsList;
	private TextView tv = null;
	List<String> paymentsIdList;

	private SimpleAdapter adapter = null;
	private String[] from = { "Kennel", "Payments" };
	private int[] to = { android.R.id.text1, android.R.id.text2 };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.activity_payments_list);
		tv = new TextView(this);
		tv.setText("List of all payments");
		tv.setGravity(Gravity.CENTER);
		getListView().addHeaderView(tv);
		paymentsByKalbiya = new ArrayList<Payments>();
		kalbiyaList = new ArrayList<String>();
		paymentsList = new ArrayList<String>();
		paymentsIdList = new ArrayList<String>();
		new PaymentsListAsyncTask(this).execute();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.payments_list, menu);
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

	private class PaymentsListAsyncTask extends
			AsyncTask<Void, Void, CollectionResponsePayments> {
		Context context;
		private ProgressDialog pd;

		public PaymentsListAsyncTask(Context context) {
			this.context = context;
		}

		protected void onPreExecute() {
			super.onPreExecute();
			pd = new ProgressDialog(context);
			pd.setMessage("Get all payments...");
			pd.show();
		}

		protected CollectionResponsePayments doInBackground(Void... unused) {
			CollectionResponsePayments payments = null;
			try {
				Paymentsendpoint.Builder builder = new Paymentsendpoint.Builder(
						AndroidHttp.newCompatibleTransport(),
						new GsonFactory(), null);
				Paymentsendpoint service = builder.build();
				payments = service.listPayments().execute();
			} catch (Exception e) {
				Log.d("Test", "Could not retrieve payments");
			}
			return payments;
		}

		protected void onPostExecute(CollectionResponsePayments payments) {
			pd.dismiss();

			// Do something with the result.
			// ArrayList<Map<String, String>>
			list = new ArrayList<Map<String, String>>();
			allPaymentsList = payments.getItems();

			prepairPaymentsByKalbiya();
			for (Payments payment : paymentsByKalbiya) {
				HashMap<String, String> item = new HashMap<String, String>();

				item.put("Kennel", "Kennel: " + payment.getKalbiya());
				item.put("Payments", payment.getMoney());
				list.add(item);

			}
			adapter = new SimpleAdapter(PaymentsList.this, list,
					android.R.layout.simple_list_item_2, from, to);

			setListAdapter(adapter);

		}

	}

	private void prepairPaymentsByKalbiya() {
		Payments cPay;
		for (int i = 0; i < allPaymentsList.size(); i++) {
			if (!kalbiyaList.contains(allPaymentsList.get(i).getKalbiya())) {
				kalbiyaList.add(allPaymentsList.get(i).getKalbiya());
				paymentsIdList.add("");
				cPay = new Payments();
				cPay.setKalbiya(allPaymentsList.get(i).getKalbiya());
				cPay.setMoney("0");
				cPay.setPetName("");
				paymentsByKalbiya.add(cPay);
			}
		}

		for (int i = 0; i < allPaymentsList.size(); i++) {

			int index = kalbiyaList
					.indexOf(allPaymentsList.get(i).getKalbiya());
			int price = Integer.parseInt(paymentsByKalbiya.get(index)
					.getMoney())
					+ Integer.parseInt(allPaymentsList.get(i).getMoney());
			cPay = new Payments();
			cPay.setMoney(String.valueOf(price));
			cPay.setKalbiya(allPaymentsList.get(i).getKalbiya());
			cPay.setPetName(paymentsByKalbiya.get(index).getPetName() + "/"
					+ allPaymentsList.get(i).getPetName());
			String s = paymentsIdList.get(index) + "/" +
			 allPaymentsList.get(i).getPaymentId();

//TODO			cPay.setPaymentId(allPaymentsList.get(i).getPaymentId());
			paymentsIdList.add(s);
			paymentsIdList.remove(index);
			kalbiyaList.add(cPay.getKalbiya());
			kalbiyaList.remove(index);
			paymentsByKalbiya.add(cPay);
			paymentsByKalbiya.remove(index);
		}
			for (int i = 0; i < paymentsByKalbiya.size(); i++)
			Log.d("Test",
					"Kennel: " + paymentsByKalbiya.get(i).getKalbiya() + "   Money: "
							+ paymentsByKalbiya.get(i).getMoney() + "   Name: " 
							+ paymentsByKalbiya.get(i).getPetName() + "   IDs: " + paymentsIdList.get(i));
	}
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {

    	AlertDialog.Builder builder = new AlertDialog.Builder(PaymentsList.this);
    	builder.setTitle("Message")
    			.setMessage("Paying for:"
    					+ "\nKennel: " + paymentsByKalbiya.get(position-1).getKalbiya()
    					+ "\nNames: " + paymentsByKalbiya.get(position-1).getPetName()
    					+ "\nIDs: " + paymentsIdList.get(position-1))
    			.setIcon(R.drawable.ic_launcher)
    			.setCancelable(false)
    			.setNegativeButton("ÎÊ",
    					new DialogInterface.OnClickListener() {
    						public void onClick(DialogInterface dialog, int id) {
    							dialog.cancel();
    						}
    					})
    					.setPositiveButton("PayPal",
    					new DialogInterface.OnClickListener() {
    						public void onClick(DialogInterface dialog, int id) {
    							//TODO Paypal transaction
    							dialog.cancel();
    						}
    					});
    	
    	AlertDialog alert = builder.create();
    	alert.show();
    }
}
