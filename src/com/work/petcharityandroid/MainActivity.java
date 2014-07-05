package com.work.petcharityandroid;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
 
public class MainActivity extends Activity {
        
        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_main);

                
                Button btnListPetsPager = (Button)findViewById(R.id.btnListPetPager);
                btnListPetsPager.setOnClickListener(new OnClickListener() {
                        
                        @Override
                        public void onClick(View v) {
                                Intent myIntent = new Intent(MainActivity.this, ListPetActivityPager.class);
                                startActivity(myIntent);
                        }
                });
                
                  Button btnAddPet = (Button)findViewById(R.id.btnAddPet);
                btnAddPet.setOnClickListener(new OnClickListener() {
                        
                        @Override
                        public void onClick(View v) {
                                Intent myIntent = new Intent(MainActivity.this, AddPetActivity.class);
                                startActivity(myIntent);
                        }
                });
                
                Button btnAddKalbiya = (Button)findViewById(R.id.btnAddKalbiya);
                btnAddKalbiya.setOnClickListener(new OnClickListener() {
                        
                        @Override
                        public void onClick(View v) {
                                Intent myIntent = new Intent(MainActivity.this, AddKalbiyaActivity.class);
                                startActivity(myIntent);
                        }
                });
                
                Button btnListKalbiya = (Button)findViewById(R.id.btnListKalbiya);
                btnListKalbiya.setOnClickListener(new OnClickListener() {
                        
                        @Override
                        public void onClick(View v) {
                                Intent myIntent = new Intent(MainActivity.this, ListKalbiyaActivity.class);
                                startActivity(myIntent);
                        }
                });
        }
 
        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
                // Inflate the menu; this adds items to the action bar if it is present.
                //getMenuInflater().inflate(R.menu.main, menu);
                menu.add("Clear outdated");
                return true;
        }
        
        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
          Intent MyIntent = new Intent(MainActivity.this, ListOutdatedActivity.class);
          startActivity(MyIntent);
          return super.onOptionsItemSelected(item);
        }
        
        
}