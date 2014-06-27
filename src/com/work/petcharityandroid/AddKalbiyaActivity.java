package com.work.petcharityandroid;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.json.gson.GsonFactory;
import com.work.petcharity.kalbiyaendpoint.Kalbiyaendpoint;
import com.work.petcharity.kalbiyaendpoint.model.Kalbiya;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddKalbiyaActivity extends Activity {

	EditText editKalbiyaName;
	EditText editKalbiyaPhone;
	EditText editKalbiyaResponsiblePerson;
	EditText editKalbiyaResponsiblePersonTel;
	EditText editKalbiyaPayPalAccount;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_kalbiya);

		editKalbiyaName = (EditText) findViewById(R.id.editKalbiyaName);
		editKalbiyaPhone = (EditText) findViewById(R.id.editKalbiyaPhone);
		editKalbiyaResponsiblePerson = (EditText) findViewById(R.id.editRespPerson);
		editKalbiyaResponsiblePersonTel = (EditText) findViewById(R.id.editRespPersonPhone);
		editKalbiyaPayPalAccount = (EditText) findViewById(R.id.editPayPalAcc);

		Button btnAddKalbiya = (Button) findViewById(R.id.btnAddKalbiya);
		btnAddKalbiya.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				// Check if values are provided
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
				Object[] params = { txtKalbiyaName, txtKalbiyaPhone,
						txtKalbiyaResponsiblePerson,
						txtKalbiyaResponsiblePersonTel,
						txtKalbiyaPayPalAccount, };
				new AddPetAsyncTask(AddKalbiyaActivity.this).execute(params);

			}
		});

	}

	private class AddPetAsyncTask extends AsyncTask<Object, Void, Kalbiya> {
		Context context;
		private ProgressDialog pd;

		public AddPetAsyncTask(Context context) {
			this.context = context;
		}

		protected void onPreExecute() {
			super.onPreExecute();
			pd = new ProgressDialog(context);
			pd.setMessage("Adding kalbiya...");
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
				kalbiya.setName((String) (params[0]));
				kalbiya.setPhone((String) (params[1]));
				kalbiya.setResponsiblePerson((String) (params[2]));
				kalbiya.setResponsiblePersonTel((String) (params[3]));
				kalbiya.setPayPalAccount((String) (params[4]));

				response = service.insertKalbiya(kalbiya).execute();
			} catch (Exception e) {
				Log.d("Could not add kalbiya", e.getMessage(), e);
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
			Toast.makeText(getBaseContext(), "Added succesfully",
					Toast.LENGTH_SHORT).show();
		}

	}

}
