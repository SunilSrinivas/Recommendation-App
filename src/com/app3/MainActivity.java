//========================================================================================================
//
//  Title:       MainActivity
//  Course:      CSC 5991  Mobile Application Development
//  Application: Student Subject Recommendation App 
//  Author:      Lakshmi Sompalli, Sunil Srinivas, Mehrina
//  Date:        7/3/2015
//  Description:
//  	This application allows students to view top rated courses and professors in CS Department 
//		at Wayne State University.
//      * Access to app is provided using valid credentials if already registered user.
//			-User has to enter valid username and password.
//
//		* New students can register and optionally provide their personal information, and 
//			feedback on already taken courses. Provided information will be stored in Mongodb database.
//			-New user has to register, by giving name, password, mobile number, email as inputs.
//			-Provides personal information inputs as Address, Gender, DOB, Contact information.
//
//		* If a login is successful, user is provided with options to select to view top courses 
//			or top professors, which returns the list of top courses/professors. 
//		 
//		* IF a user forgets passwords, a reset password will be sent through email/phonenumber 
//			provided in registration form.
//		
//		* A list of Top courses/ professors will be displayed in the app.
//		* User can view location of campus on Google maps.
//  Permissions:
//		A list of permissions used by app to perform as intended.
//			-Internet (To connect to database in cloud storage, and to access google maps)
//			-ACCESS FINE LOCATION 
//			-ACCESS COARSE LOCATION
//			-Read GSERVICES
//			-Send-SMS
//			-Read/Write External storage
//===========================================================================================================
package com.app3;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;

public class MainActivity extends ActionBarActivity {

	Button b1, b2, b3,b4;
	EditText ed1, ed2;

	TextView tx1;
	final Context context = this;

	int counter = 3;

	@SuppressWarnings({ "unchecked", "rawtypes" })
@Override
protected void onCreate(Bundle savedInstanceState) {

/********************************************************************************
		* Define all the controls
*********************************************************************************/		
		
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_main);

	b1 = (Button) findViewById(R.id.button);
	ed1 = (EditText) findViewById(R.id.editText);
	ed2 = (EditText) findViewById(R.id.editText2);

	b2 = (Button) findViewById(R.id.button2);
	tx1 = (TextView) findViewById(R.id.textView3);
	tx1.setVisibility(View.GONE);

	b3 = (Button) findViewById(R.id.button1);
	b4 = (Button) findViewById(R.id.buttonmap);
	
	
/********************************************************************************
	* Button Reset to set all values to empty
*********************************************************************************/

	b2.setOnClickListener(new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			ed1.setText("");
			ed2.setText("");
		}

	});
	
/********************************************************************************
	* Map button navigates to the map class
*********************************************************************************/	
	
	b4.setOnClickListener(new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(context, Map.class);
			startActivity(intent);
		}

	});
/********************************************************************************
	* Register button navigates to register class to store the data to database
*********************************************************************************/	

	b3.setOnClickListener(new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(context, Registration.class);
			startActivity(intent);
		}

	});
	
/********************************************************************************
	* Login button navigates to different classes based on the logic
*********************************************************************************/		
	

	b1.setOnClickListener(new View.OnClickListener() {

		@SuppressLint("NewApi")
		@Override
		public void onClick(View v) {
			Log.i("Message", "Inserted1");
			
/********************************************************************************
			* Validation to check for empty username and password
*********************************************************************************/			
			
			if(ed1.getText().toString().isEmpty() || ed2.getText().toString().isEmpty())
			{
				Toast.makeText(getApplicationContext(),
						"User name and password cannot be null", Toast.LENGTH_LONG)
						.show();
			}
			
/********************************************************************************
	* DB interaction can only take place in a new thread or in a new Asynctask
*********************************************************************************/			
			
			else
			{
			
	new AsyncTask<Void, Void, Void>() {
		private DBInteraction dbInteraction;

		@Override
		protected Void doInBackground(Void... params) {
			Log.i("Message", "Inserted2");
			String uname = ed1.getText().toString();
			String password = ed2.getText().toString();
			
/********************************************************************************
			* Define the sub query for the main query
*********************************************************************************/			
			
			dbInteraction = new DBInteraction();
			   BasicDBObject query = new BasicDBObject();
                BasicDBObject fields = new BasicDBObject();
                query.put("Name", uname);

                fields.put("_id", 0);
                fields.put("Password", 1);
                
/********************************************************************************
            	* Define DB Cursor to fetch multiple values
*********************************************************************************/
            DBCursor cursor = dbInteraction.getDb().getCollection("user1").find(query,fields);
            while (cursor.hasNext()) {
            	try
            	{
            	JSONObject returned = new JSONObject(cursor.next().toString());
            	String password1 = returned.getString("Password");
            	System.out.println(password1);
            	
 /********************************************************************************
   * If validation passes, go the query class
 *********************************************************************************/           	
            	if(password.equals(password1))
            	{
            		Log.i("Message", "Inserted3");
            		Intent intent = new Intent(context, ActOption1.class);
        			startActivity(intent);
            	}
            	else
            	{
            		Log.i("Message", "Inserted4");
      /********************************************************************************
            * If validation does not pass, send email to the student
      *********************************************************************************/       		
            		System.out.println("Please check your password");
            		Intent intent = new Intent(context, password.class);
        			startActivity(intent);
            	}
            	}catch (JSONException e) {
                    e.printStackTrace();
                }
            	
		                	}
		
						return null;
				}

	}.execute();
					}}
		});
	
	}
/********************************************************************************
	* Definning the menu for the App
*********************************************************************************/	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.fmenu1, menu);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		
/********************************************************************************
		* Define the options in the menu
*********************************************************************************/		
		
		super.onOptionsItemSelected(item);
		switch(item.getItemId()){
		case R.id.About:
			AboutMenuItem();
			break;
		case R.id.Home:
			Intent intent = new Intent(this, Registration.class);
	        this.startActivity(intent);
			//HomeMenuItem();
			break;
		case R.id.Settings:
			SettingsMenuItem();
			break;
		case R.id.Help:
			HelpMenuItem();
			break;
		}
		return true;
		
	}
/********************************************************************************
	* help option settings in the menu
*********************************************************************************/		
	private void HelpMenuItem(){
		new AlertDialog.Builder(this)
		.setTitle("About")
		.setMessage(" Sends Password to the associated email")
		.setNeutralButton("OK", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
			}
		}).show();
		
	}

/********************************************************************************
	* About option in the menu
*********************************************************************************/
	
	private void AboutMenuItem(){
		new AlertDialog.Builder(this)
		.setTitle("About")
		.setMessage("This is a Student Recommender Application.The main aim is to gain practical experience with the Map reduce based query functionality provided by Mongo db No SQL document database and integrate it with the android application while trying to recommend subjects for students")
		.setNeutralButton("OK", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
			}
		}).show();
		
	}
	
/********************************************************************************
	* Settings option in the menu
*********************************************************************************/	
	private void SettingsMenuItem(){
		new AlertDialog.Builder(this)
		.setTitle("Settings")
		.setMessage("This is a Settings menu option ")
		.setNeutralButton("OK", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
			}
		}).show();
		
	}
	
}
