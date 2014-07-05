package com.work.petcharityandroid;

import android.app.ListActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.json.gson.GsonFactory;
import com.work.petcharity.petendpoint.Petendpoint;
import com.work.petcharity.petendpoint.model.CollectionResponsePet;
import com.work.petcharity.petendpoint.model.Pet;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class ListPetActivity extends ListActivity{
	private TextView tv = null;
	private ArrayList<Map<String,String>> list = null;
	List<Pet> allPetsList;

	private SimpleAdapter adapter = null;
	private String[] from = { "Name", "Kennel" };
	private int[] to = { android.R.id.text1, android.R.id.text2 };

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		tv = new TextView(this);
		tv.setText("List of all pets");
		tv.setGravity(Gravity.CENTER);
		getListView().addHeaderView(tv);
		new PetsListAsyncTask(this).execute();
	}

	private class PetsListAsyncTask extends AsyncTask<Void, Void, CollectionResponsePet>{
		  Context context;
		  private ProgressDialog pd;

		  public PetsListAsyncTask(Context context) {
		    this.context = context;
		  }

		  protected void onPreExecute(){ 
		     super.onPreExecute();
		          pd = new ProgressDialog(context);
		          pd.setMessage("Prepearing...");
		          pd.show();    
		  }

		  protected CollectionResponsePet doInBackground(Void... unused) {
			  CollectionResponsePet pets = null;
		    try {
		    	Petendpoint.Builder builder = new Petendpoint.Builder(AndroidHttp.newCompatibleTransport(), new GsonFactory(), null);
		    	Petendpoint service =  builder.build();
				pets = service.listPet().execute();
		    } catch (Exception e) {
		      Log.d("Test", "Could not retrieve pets");
		    }
		    return pets;
		  }

		  protected void onPostExecute(CollectionResponsePet pets) {
			  pd.dismiss();

		    // Do something with the result.
// 			   ArrayList<Map<String, String>> 
 			   list = new ArrayList<Map<String, String>>();
			   allPetsList = pets.getItems();
			   
				for (Pet pet : allPetsList) {
					HashMap<String, String> item = new HashMap<String, String>();

					item.put("Name", "Name: " + pet.getPetName());
					item.put("Kennel", pet.getKalbiya());
			 	    list.add(item);
			 	    

				}
				adapter = new SimpleAdapter(ListPetActivity.this, list,android.R.layout.simple_list_item_2, from, to);

				setListAdapter(adapter);
				
		  }
		}
	    @Override
	    protected void onListItemClick(ListView l, View v, int position, long id) {

	    	Pet currentPet = allPetsList.get(position - 1);
	    	

	 	    byte[] bArray = currentPet.decodePicture();

	    	
	    	Intent intent = new Intent(this, PetInfo.class); 
	    	intent.putExtra("id", currentPet.getId());
	    	intent.putExtra("name", currentPet.getPetName());
	        intent.putExtra("description", currentPet.getDescription());
	    	intent.putExtra("kalbiya", currentPet.getKalbiya());
	        intent.putExtra("needmoney", currentPet.getMoneyNeeded());
	    	intent.putExtra("havemoney", currentPet.getMoneyHave());
	        intent.putExtra("picture", bArray);
	    	intent.putExtra("deathdate", currentPet.getDeathDate());

	        startActivity(intent);


	    }
		@Override
		public boolean onCreateOptionsMenu(Menu menu) {

			// Inflate the menu; this adds items to the action bar if it is present.
			//getMenuInflater().inflate(R.menu.pet_info, menu);
			menu.add("Picture view");
			return true;
		}

		@Override
		public boolean onOptionsItemSelected(MenuItem item) {

			Intent intent = new Intent(ListPetActivity.this, ListPetActivityPager.class);
			startActivity(intent);
			return super.onOptionsItemSelected(item);
		}
	   
		
}