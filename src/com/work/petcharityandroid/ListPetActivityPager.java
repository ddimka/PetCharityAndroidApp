package com.work.petcharityandroid;

import java.io.IOException;
import java.util.List;

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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ListPetActivityPager extends Activity {

	List<Pet> allPetsList;
	private int pagesCount;
	// private SimpleAdapter adapter = null;
	private MyPagerAdapter adapter;
	private ViewPager pictPager;
	private CollectionResponsePet pets = null;
	private Petendpoint service;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_pet_activity_pager);
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
			pets = null;
			try {
				Petendpoint.Builder builder = new Petendpoint.Builder(
						AndroidHttp.newCompatibleTransport(),
						new GsonFactory(), null);
				service = builder.build();
				pets = service.listPet().execute();
			} catch (Exception e) {
				Log.d("Could not retrieve pets", e.getMessage(), e);
			}
			return pets;
		}

		protected void onPostExecute(CollectionResponsePet pets) {
			pd.dismiss();

			allPetsList = pets.getItems();
			pagesCount = allPetsList.size();

			adapter = new MyPagerAdapter();
			pictPager = (ViewPager) findViewById(R.id.pictPager);

			pictPager.setAdapter(adapter);
		}
	}

	private class MyPagerAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return pagesCount;
		}

		@Override
		public Object instantiateItem(View collection, final int position) {

			final Pet currentPet = allPetsList.get(position); // - 1
			byte[] bArray = currentPet.decodePicture(); 
			Bitmap bitmap = BitmapFactory.decodeByteArray(bArray, 0, bArray.length);
			LayoutInflater inflater = (LayoutInflater) collection.getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View layout = inflater.inflate(R.layout.pet_info_pager, null);

			ImageView petImage = (ImageView) layout.findViewById(R.id.petImage);
			petImage.setImageBitmap(bitmap);
			petImage.setOnLongClickListener(new OnLongClickListener() {
				
				@Override
				public boolean onLongClick(View v) {
//					Toast.makeText(ListPetActivityPager.this, "ID = " + currentPet.getId(), Toast.LENGTH_LONG).show();
					AlertDialog.Builder builder = new AlertDialog.Builder(ListPetActivityPager.this);                
                    builder.setTitle("ID = " + currentPet.getId());
                    builder.setMessage("Name: " + currentPet.getPetName() + "\n\nAre you sure you want to delete?");
                    builder.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                            
                            public void onClick(DialogInterface dialog, int which) {
                            	try {
									service.removePet(currentPet.getId()).execute();
									allPetsList.remove(position);
								} catch (IOException e) {
									Toast.makeText(ListPetActivityPager.this, "Can't delete pet from database", Toast.LENGTH_LONG).show();
									
								}
                            }
                    });
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        
                        public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                        }
                });
                    builder.show();
					// TODO Auto-generated method stub
					return false;
				}
			});
			
			TextView petName = (TextView) layout.findViewById(R.id.petName);
			TextView petDescription = (TextView) layout.findViewById(R.id.petDescription);
			TextView petKalbiya = (TextView) layout.findViewById(R.id.petKalbiya);
			TextView petNeedMoney = (TextView) layout.findViewById(R.id.petNeedMoney);
			TextView petHaveMoney = (TextView) layout.findViewById(R.id.petHaveMoney);
			TextView petDeathDate = (TextView) layout.findViewById(R.id.petDeathDate);
			
			petName.setText("Pet name:" + currentPet.getPetName());
			petDescription.setText("Description:" + currentPet.getDescription());
			petKalbiya.setText("Kalbiya:" + currentPet.getKalbiya());
			petNeedMoney.setText("Need money:" + currentPet.getMoneyNeeded());
			petHaveMoney.setText("Already have money:" + currentPet.getMoneyHave());
			petDeathDate.setText("Death date:" + currentPet.getDeathDate());
			
			((ViewPager) collection).addView(layout, 0);

			return layout;
		}

		@Override
		public void destroyItem(View collection, int position, Object view) {
			((ViewPager) collection).removeView((RelativeLayout) view);
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == ((RelativeLayout) object);
//			return view == ((ImageView) object);
		}

		@Override
		public void finishUpdate(View arg0) {
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {
		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View arg0) {
		}

	}

}
