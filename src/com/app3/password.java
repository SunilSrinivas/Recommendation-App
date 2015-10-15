//========================================================================================================
//
//  Title:       Password 
//  Course:      CSC 5991 Mobile Application Development
//  Application: Student Subject Recommender App
//  Description:
//  	This activity displays a screen with a button to reset password.
//		If password is found in database then password is sent to user through E-mail
//===========================================================================================================
package com.app3;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MapReduceCommand;
import com.mongodb.MapReduceOutput;
import com.mongodb.Mongo;
import com.mongodb.MongoException;

public class password extends ActionBarActivity {
	/********************************************************************************
	 * Define all the controls
	 *********************************************************************************/
	Button b1, b2, b3;
	EditText ed1, ed2;

	TextView tx1;
	final Context context = this;

	int counter = 3;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.password);

		b1 = (Button) findViewById(R.id.btnforgotPassword);
		ed1 = (EditText) findViewById(R.id.etusername);

		b1.setOnClickListener(new View.OnClickListener() {
			/********************************************************************************
			 * DB interaction can only take place in a new thread or in a new
			 * Asynctask
			 *********************************************************************************/
			@Override
			public void onClick(View v) {
				Log.i("Message", "Inserted1");
				new AsyncTask<Void, Void, Void>() {
					private DBInteraction dbInteraction;

					@Override
					protected Void doInBackground(Void... params) {
						Log.i("Message", "Inserted2");
						String uname = ed1.getText().toString();
						dbInteraction = new DBInteraction();
						BasicDBObject query = new BasicDBObject();
						BasicDBObject fields = new BasicDBObject();
						query.put("Email", uname);
						/********************************************************************************
						 * Define the sub query for the main query
						 *********************************************************************************/
						fields.put("_id", 0);
						fields.put("Password", 1);
						/********************************************************************************
						 * Define DB Cursor to fetch multiple values
						 *********************************************************************************/
						DBCursor cursor = dbInteraction.getDb()
								.getCollection("user1").find(query, fields);
						while (cursor.hasNext()) {
							try {
								JSONObject returned = new JSONObject(cursor
										.next().toString());
								String password1 = returned
										.getString("Password");
								System.out.println(password1);

								/********************************************************************************
								 * Send email to the student
								 *********************************************************************************/
								Intent i = new Intent(Intent.ACTION_SEND);

								i.setType("message/rfc822");

								i.putExtra(Intent.EXTRA_EMAIL,
										new String[] { uname });

								i.putExtra(Intent.EXTRA_SUBJECT,
										"Your password Details");

								i.putExtra(Intent.EXTRA_TEXT, password1);

								try {

									startActivity(Intent.createChooser(i,
											"Send mail..."));

								} catch (android.content.ActivityNotFoundException ex) {

									Toast.makeText(
											password.this,
											"There are no email clients installed.",
											Toast.LENGTH_SHORT).show();

								}

							} catch (JSONException e) {
								e.printStackTrace();
							}

						}

						return null;
					}

				}.execute();
			}
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

	/********************************************************************************
	 * Define the options in the menu
	 *********************************************************************************/
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.

		super.onOptionsItemSelected(item);
		switch (item.getItemId()) {
		case R.id.About:
			AboutMenuItem();
			break;
		case R.id.Home:
			Intent intent = new Intent(this, Registration.class);
			this.startActivity(intent);
			// HomeMenuItem();
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
	private void HelpMenuItem() {
		new AlertDialog.Builder(this).setTitle("About")
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
	private void AboutMenuItem() {
		new AlertDialog.Builder(this).setTitle("About")
				.setMessage(" Sends Password to the associated email")
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
	private void SettingsMenuItem() {
		new AlertDialog.Builder(this).setTitle("Settings")
				.setMessage("This is a Settings menu option ")
				.setNeutralButton("OK", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub

					}
				}).show();

	}

}
