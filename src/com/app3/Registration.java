//========================================================================================================
//
//  Title:       Registration
//  Course:      CSC 5991 Mobile Application Development
//  Application: Student Subject Recommendation App
//  Author:      Lakshmi Sompalli, Sunil Srinivas
//  Description:
//  	
//			This activity allows user to register for the app
//			Inputs Required: Username, password, Email, phone number
//		Application send registration confirmation mail to user's email-ID
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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.content.Intent;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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
import java.util.List;
import java.text.DecimalFormat;

import org.json.JSONException;
import org.json.JSONObject;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MapReduceCommand;
import com.mongodb.MapReduceOutput;


//--------------------------------------------------------------
//class ActMain
//--------------------------------------------------------------
public class Registration extends ActionBarActivity {

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

private Button Save, b3, b2;
private Button btnCancel;
private EditText etname,etemail,etpass,ettelephone;

private TextView textView;
final Context context = this;
private int selection;
private ArrayList<Integer> selections = new ArrayList<Integer>();

// ----------------------------------------------------------------
// Activity overrides
// ----------------------------------------------------------------

// ----------------------------------------------------------------
// onCreate event
// ----------------------------------------------------------------
@Override
protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.registration);
/********************************************************************************
	* Define all the controls
*********************************************************************************/	
	etname = (EditText) findViewById(R.id.reg_fullname);
	etemail = (EditText) findViewById(R.id.reg_email);
	etpass = (EditText) findViewById(R.id.reg_password);
	final EditText ettelephone = (EditText) findViewById(R.id.etPhone);
	
	b3 = (Button) findViewById(R.id.btnpersonal);
	b3.setOnClickListener(new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(context, Profile.class);
			startActivity(intent);
		}

	});
	/********************************************************************************
	* Button cancel to set all values to empty
*********************************************************************************/
	btnCancel = (Button) findViewById(R.id.btnCancel);
	btnCancel.setOnClickListener(new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			etname.setText("");
			etemail.setText("");
			etpass.setText("");
			ettelephone.setText("");
		}

	});
	
/********************************************************************************
	* Navigation to the student page when the button is pressed
*********************************************************************************/	

	b2 = (Button) findViewById(R.id.btnStudent);
	b2.setOnClickListener(new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(context, Subject.class);
			startActivity(intent);
		}

	});
	
/*******************************************************************************
	* Save button to store the details in database.
*********************************************************************************/	

	Save = (Button) findViewById(R.id.btnSave);
	Save.setOnClickListener(new View.OnClickListener() {
		@SuppressLint("NewApi")
		public void onClick(View v) {
		
hideSoftKeyboard();
try {
String etname1 = etname.getText().toString();
String etemail1 = etemail.getText().toString();
String etpass1 = etpass.getText().toString();
/********************************************************************************
* Validation to check for all fields
*********************************************************************************/

if (etname.getText().toString().isEmpty()
		|| etemail.getText().toString().isEmpty()
		|| etpass.getText().toString().isEmpty()
		|| ettelephone.getText().toString().isEmpty()) {
	
	Toast.makeText(getApplicationContext(),
			"Please Fill all Values", Toast.LENGTH_LONG)
			.show();
}


/********************************************************************************
* String builder to send message to students
*********************************************************************************/

StringBuilder sb = new StringBuilder();
sb.append("Name : " + etname1);
sb.append("\n");
sb.append("Email : " + etemail1);
sb.append("\n");
sb.append("Password : " + etpass1);
sb.append("\n");

Toast.makeText(getApplicationContext(),
		sb.toString(), Toast.LENGTH_LONG)
		.show();
/********************************************************************************
* Send message to the student
*********************************************************************************/

String phoneNo = ettelephone.getText().toString();
if(!phoneNo.isEmpty())
{
SmsManager smsManager = SmsManager.getDefault();
smsManager.sendTextMessage(phoneNo, null, sb.toString(),
		null, null);
Toast.makeText(getApplicationContext(), "SMS sent.",
		Toast.LENGTH_LONG).show();
}
else
{
	Toast.makeText(getApplicationContext(),
			"PLease enter telephone Number", Toast.LENGTH_LONG)
			.show();
}
/********************************************************************************
* Send email if email is not empty
*********************************************************************************/



if(!etemail1.isEmpty())
{
Intent i = new Intent(Intent.ACTION_SEND);
i.setType("message/rfc822");
i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"sunilsrinivaz@yahoo.co.in"});
i.putExtra(Intent.EXTRA_SUBJECT, "Registration details send through App");
i.putExtra(Intent.EXTRA_TEXT   , sb.toString());

try {
    startActivity(Intent.createChooser(i, "Send mail..."));
} catch (android.content.ActivityNotFoundException ex) {
    Toast.makeText(Registration.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
}
}

else
{
	Toast.makeText(getApplicationContext(),
			"Please provide email address", Toast.LENGTH_LONG)
			.show();
	
}

/********************************************************************************
* DB interaction can only take place in a new thread or in a new Asynctask
*********************************************************************************/	
new AsyncTask<Void, Void, Void>() {
	private DBInteraction dbInteraction;
	
	@Override
	protected Void doInBackground(Void... params) {
		// TODO Auto-generated method stub
		BasicDBObject db1 = new BasicDBObject();
		db1.put("Name", etname.getText().toString());
		db1.put("Email", etemail.getText().toString());
		db1.put("Password", etpass.getText().toString());
		dbInteraction = new DBInteraction();
		
/********************************************************************************
		* Insert data into database
	*********************************************************************************/			
		DBCollection collection = dbInteraction.getDb().getCollection("user1");
		collection.insert(db1);
		Log.i("message","Inserted");
		return null;
	}             
}.execute();


}catch (NumberFormatException e) {
		// Display a toast message if any value is null

Toast.makeText(getApplicationContext(),
		"Enter telephone Number", Toast.LENGTH_LONG).show();

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
		.setMessage("ENter essentail details to register to app")
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
