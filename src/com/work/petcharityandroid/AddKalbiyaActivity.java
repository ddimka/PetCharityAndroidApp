package com.work.petcharityandroid;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.json.gson.GsonFactory;
import com.work.petcharity.kalbiyaendpoint.Kalbiyaendpoint;
import com.work.petcharity.kalbiyaendpoint.model.Kalbiya;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddKalbiyaActivity extends Activity implements Constants {

	EditText editKalbiyaName;
	EditText editKalbiyaPhone;
	EditText editKalbiyaResponsiblePerson;
	EditText editKalbiyaResponsiblePersonTel;
	EditText editKalbiyaPayPalAccount;
	int process;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_kalbiya);

		Intent intent = getIntent();
		final Long idFromIntent = intent.getLongExtra("id", 0);
		String nameFromIntent = intent.getStringExtra("name");
		String phoneFromIntent = intent.getStringExtra("phone");
		String personFromIntent = intent.getStringExtra("person");
		String personPhoneFromIntent = intent.getStringExtra("personphone");
		String paypalFromIntent = intent.getStringExtra("paypal");

		editKalbiyaName = (EditText) findViewById(R.id.editKalbiyaName);
		editKalbiyaPhone = (EditText) findViewById(R.id.editKalbiyaPhone);
		editKalbiyaResponsiblePerson = (EditText) findViewById(R.id.editRespPerson);
		editKalbiyaResponsiblePersonTel = (EditText) findViewById(R.id.editRespPersonPhone);
		editKalbiyaPayPalAccount = (EditText) findViewById(R.id.editPayPalAcc);

		editKalbiyaName.setText(nameFromIntent);
		editKalbiyaPhone.setText(phoneFromIntent);
		editKalbiyaResponsiblePerson.setText(personFromIntent);
		editKalbiyaResponsiblePersonTel.setText(personPhoneFromIntent);
		editKalbiyaPayPalAccount.setText(paypalFromIntent);
		
		if (idFromIntent == 0)
			process = ADD_NEW_KALBIYA;
		else
			process = UPDATE_KALBIYA;
		
		Button btnAddKalbiya = (Button) findViewById(R.id.btnAddKalbiya);
		btnAddKalbiya.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Long txtKalbiyaId = idFromIntent;
				String txtKalbiyaName = editKalbiyaName.getText().toString()
						.trim();
				String txtKalbiyaPhone = editKalbiyaPhone.getText().toString()
						.trim();
				String txtKalbiyaResponsiblePerson = editKalbiyaResponsiblePerson
						.getText().toString().trim();
				String txtKalbiyaResponsiblePersonTel = editKalbiyaResponsiblePersonTel
						.getText().toString().trim();
				String txtKalbiyaPayPalAccount = editKalbiyaPayPalAccount
						.getText().toString().trim();

				// Go ahead and perform the transaction
				Object[] params = { txtKalbiyaId, txtKalbiyaName,
						txtKalbiyaPhone, txtKalbiyaResponsiblePerson,
						txtKalbiyaResponsiblePersonTel,
						txtKalbiyaPayPalAccount, };
				if (process == ADD_NEW_KALBIYA)
					new AddKalbiyaAsyncTask(AddKalbiyaActivity.this).execute(params);
				else
					new UpdateKalbiyaAsyncTask(AddKalbiyaActivity.this).execute(params);

			}
		});

	}

	private class AddKalbiyaAsyncTask extends AsyncTask<Object, Void, Kalbiya> {
		Context context;
		private ProgressDialog pd;

		public AddKalbiyaAsyncTask(Context context) {
			this.context = context;
		}

		protected void onPreExecute() {
			super.onPreExecute();
			pd = new ProgressDialog(context);
			pd.setMessage("Adding kennel...");
			pd.show();
		}

		protected Kalbiya doInBackground(Object... params) {
			Kalbiya response = null;
			try {
				Kalbiyaendpoint.Builder builder = new Kalbiyaendpoint.Builder(
						AndroidHttp.newCompatibleTransport(),
						new GsonFactory(), null);
				Kalbiyaendpoint service = builder.build();
				Kalbiya kalbiya = new Kalbiya();
				kalbiya.setName((String) (params[1]));
				kalbiya.setPhone((String) (params[2]));
				kalbiya.setResponsiblePerson((String) (params[3]));
				kalbiya.setResponsiblePersonTel((String) (params[4]));
				kalbiya.setPayPalAccount((String) (params[5]));

				response = service.insertKalbiya(kalbiya).execute();
			} catch (Exception e) {
				Log.d("Could not add kennel", e.getMessage(), e);
			}
			return response;
		}

		protected void onPostExecute(Kalbiya kalbiya) {
			// Clear the progress dialog and the fields
			pd.dismiss();
			editKalbiyaName.setText("");
			editKalbiyaPhone.setText("");
			editKalbiyaResponsiblePerson.setText("");
			editKalbiyaResponsiblePersonTel.setText("");
			editKalbiyaPayPalAccount.setText("");

			// Display success message to user
/*			Toast.makeText(getBaseContext(), "Added succesfully",
					Toast.LENGTH_SHORT).show();*/
		}

	}
	
	private class UpdateKalbiyaAsyncTask extends AsyncTask<Object, Void, Kalbiya> {
		Context context;
		private ProgressDialog pd;

		public UpdateKalbiyaAsyncTask(Context context) {
			this.context = context;
		}

		protected void onPreExecute() {
			super.onPreExecute();
			pd = new ProgressDialog(context);
			pd.setMessage("Updating kennel...");
			pd.show();
		}

		protected Kalbiya doInBackground(Object... params) {
			Kalbiya response = null;
			try {
				Kalbiyaendpoint.Builder builder = new Kalbiyaendpoint.Builder(
						AndroidHttp.newCompatibleTransport(),
						new GsonFactory(), null);
				Kalbiyaendpoint service = builder.build();
				Kalbiya kalbiya = new Kalbiya();
				kalbiya.setId((Long) (params[0]));
				kalbiya.setName((String) (params[1]));
				kalbiya.setPhone((String) (params[2]));
				kalbiya.setResponsiblePerson((String) (params[3]));
				kalbiya.setResponsiblePersonTel((String) (params[4]));
				kalbiya.setPayPalAccount((String) (params[5]));

				response = service.updateKalbiya(kalbiya).execute();
			} catch (Exception e) {
				Log.d("Could not update kennel", e.getMessage(), e);
			}
			return response;
		}

		protected void onPostExecute(Kalbiya kalbiya) {
			// Clear the progress dialog and the fields
			pd.dismiss();


			// Display success message to user
			Toast.makeText(getBaseContext(), "Updated succesfully",
					Toast.LENGTH_SHORT).show();
			finish();
			return;
/*			Intent mainIntent = new Intent(AddKalbiyaActivity.this, MainActivity.class);
			startActivity(mainIntent);*/
		}

	}

}
