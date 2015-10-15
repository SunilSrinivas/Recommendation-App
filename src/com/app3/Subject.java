//========================================================================================================
//
//  Title:       Subject
//  Course:      CSC 5991 Mobile Application Development
//  Application: Student Subject Recommendation App
//  Description:
//  	
//			This activity allows user to rate the subjects
//			Inputs Required: Name, Subject, Rating, semester, Professor name
//===========================================================================================================


package com.app3;

//Import Android packages
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.content.Intent;
import android.widget.Spinner;
import android.widget.Button;
import android.widget.EditText;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;






//Import Java packages
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.text.DecimalFormat;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;

//--------------------------------------------------------------
//class ActMain
//--------------------------------------------------------------
public class Subject extends ActionBarActivity {

	// ----------------------------------------------------------------
// Constants
// ----------------------------------------------------------------

// Declare application constants
private static final String APP_NAME = "Student Subject Recommender";
private static final String APP_VERSION = "1.0";



// ----------------------------------------------------------------
// Variables
// ----------------------------------------------------------------

// Declare variables

private Button b3;
private Button btnCancel;


private EditText etname,etsubjects,etrating,etsemester,etProfessor;

final Context context = this;

// ----------------------------------------------------------------
// Activity overrides
// ----------------------------------------------------------------

// ----------------------------------------------------------------
// onCreate event
// ----------------------------------------------------------------
@Override
protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.subject);
/********************************************************************************
	* Define all the controls
*********************************************************************************/	
	b3 = (Button) findViewById(R.id.btnSave);
	etname = (EditText) findViewById(R.id.reg_fullname);
	etsubjects = (EditText) findViewById(R.id.Subjects);
	etrating = (EditText) findViewById(R.id.Ratings);
	etsemester = (EditText) findViewById(R.id.Semester);		
	etProfessor= (EditText) findViewById(R.id.Professor);

	/********************************************************************************
	* Button cancel to set all values to empty
*********************************************************************************/	
	btnCancel = (Button) findViewById(R.id.btnCancel);
	btnCancel.setOnClickListener(new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			etname.setText("");
	        etsubjects.setText("");
	        etrating.setText("");
	        etsemester.setText("");
	        etProfessor.setText("");
	}

});

	/*******************************************************************************
	* Save button to store the details in database.
*********************************************************************************/
b3.setOnClickListener(new View.OnClickListener() {

	@SuppressLint("NewApi")
@Override
public void onClick(View v) {
	
	try {
		
		/********************************************************************************
		* Validation to check for all fields
		*********************************************************************************/	
	if (etname.getText().toString().isEmpty()
			|| etsubjects.getText().toString().isEmpty()
			|| etrating.getText().toString().isEmpty()
			|| etsemester.getText().toString().isEmpty() 
			|| etProfessor.getText().toString().isEmpty()) {
		
		Toast.makeText(getApplicationContext(),
				"Please Fill all Values", Toast.LENGTH_LONG)
				.show();
	}
	/********************************************************************************
	* DB interaction can only take place in a new thread or in a new Asynctask
	*********************************************************************************/	

	new AsyncTask<Void, Void, Void>() {
		private DBInteractioncollection dbInteraction1;
		
		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			BasicDBObject dbsubject = new BasicDBObject();
			dbsubject.put("name", etname.getText().toString());
			dbsubject.put("Subjects", etsubjects.getText().toString());
			dbsubject.put("Rating", etrating.getText().toString());
			dbsubject.put("semester", etsemester.getText().toString());
			dbsubject.put("Professor", etProfessor.getText().toString());
			dbInteraction1 = new DBInteractioncollection();
			DBCollection collection1 = dbInteraction1.getDb().getCollection("user4");
			collection1.insert(dbsubject);
			
			return null;
		}             
	}.execute();

	}catch (NumberFormatException e) {
			// Display a toast message if any value is null
				e.printStackTrace();
						}					     
			}
	});
}





// ----------------------------------------------------------------
// General utilities
// ----------------------------------------------------------------
/**
 * Hides the soft keyboard
 */
public void hideSoftKeyboard() {
	if (getCurrentFocus() != null) {
		InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
		inputMethodManager.hideSoftInputFromWindow(getCurrentFocus()
				.getWindowToken(), 0);
	}
}

/**
 * Shows the soft keyboard
 */
	public void showSoftKeyboard(View view) {
		InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
		view.requestFocus();
		inputMethodManager.showSoftInput(view, 0);
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
		.setMessage("Gathers all the subject details and enters into database")
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
