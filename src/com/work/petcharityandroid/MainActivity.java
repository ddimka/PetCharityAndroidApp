package com.work.petcharityandroid;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
 
public class MainActivity extends Activity {
        
        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_main);
                
                //Event Listener for About App button
                Button btnAboutApp = (Button)findViewById(R.id.btnAboutApp);
                btnAboutApp.setOnClickListener(new OnClickListener() {
                        
                        @Override
                        public void onClick(View v) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);                
                                builder.setTitle("About Application");
                                builder.setMessage("Pet Charity\nVersion 1.0");
                                builder.setPositiveButton("OK",new DialogInterface.OnClickListener() {
                                        
                                        public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                        }
                                });
                                builder.show();
                        }
                });
                
                
                //Event Listener for List Quotes button
                Button btnListPets = (Button)findViewById(R.id.btnListPet);
                btnListPets.setOnClickListener(new OnClickListener() {
                        
                        @Override
                        public void onClick(View v) {
                                Intent myIntent = new Intent(MainActivity.this, ListPetActivity.class);
                                startActivity(myIntent);
                        }
                });
                
                Button btnListPetsPager = (Button)findViewById(R.id.btnListPetPager);
                btnListPetsPager.setOnClickListener(new OnClickListener() {
                        
                        @Override
                        public void onClick(View v) {
                                Intent myIntent = new Intent(MainActivity.this, ListPetActivityPager.class);
                                startActivity(myIntent);
                        }
                });
                
                //Event Listener for Add Quote button
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
                getMenuInflater().inflate(R.menu.main, menu);
                return true;
        }
 
}