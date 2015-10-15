
//========================================================================================================
//
//  Title:       Profile
//  Course:      CSC 5991 Mobile Application Development
//  Application: Student Subject Recommendor App
//  Description:
//  	This activity allows user to provide personal information
//   	Inputs required: Address, City, State, Zipcode, Email, PhoneNUmber, Gender, DOB, Language
//		Entered data will be saved to database
//===========================================================================================================






package com.app3;

//Import Android packages
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
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




//--------------------------------------------------------------
//class ActMain
//--------------------------------------------------------------
public class Profile extends Activity
{

//----------------------------------------------------------------
// Constants
//----------------------------------------------------------------

	// Declare application constants
	private static final String APP_NAME = "Student Subject Recommender";
	private static final String APP_VERSION = "1.0";

//----------------------------------------------------------------
// Variables
//----------------------------------------------------------------

// Declare variables


private Button Save, b2;
private Button Cancel;

private RadioGroup radioGroup1,radioGroup2;

EditText etaddress, etcity,etzip,etemail,etphone;
private Spinner spinnermonth,spinnerday,spinneryear;
/*final RadioButton radiomale = (RadioButton) findViewById(R.id.male);
final RadioButton radiofemale = (RadioButton) findViewById(R.id.female);
final RadioButton radioenglush = (RadioButton) findViewById(R.id.english);
final RadioButton radiononenglish = (RadioButton) findViewById(R.id.nonenglish);*/

private Button btnCancel;

private TextView textView;
final Context context = this;

private int selection;
private ArrayList<Integer> selections = new ArrayList<Integer>();


//----------------------------------------------------------------
// Activity overrides
//----------------------------------------------------------------

//----------------------------------------------------------------
// onCreate  event 
//----------------------------------------------------------------
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.profile);
		
		
		/********************************************************************************
			* Save button to insert data into database 
		*********************************************************************************/	
		
		Save = (Button) findViewById(R.id.Save);
		Save.setOnClickListener(new View.OnClickListener() {
			@SuppressLint("NewApi")
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(v
						.getContext());
				builder.setTitle(APP_NAME + " Message");
				hideSoftKeyboard();
				try{
			
					
					//address event 
					etaddress = (EditText)findViewById(R.id.addressedit);	
					String addressedit1 = etaddress.getText().toString();
					
					//City event 
					etcity = (EditText)findViewById(R.id.cityedit);	
					String cityedit1 = etcity.getText().toString();
					
					
					//define  spinner State event
					Spinner spstate = (Spinner) findViewById(R.id.spinner1);
					String txtspinnerstate = spstate.getSelectedItem().toString();
					
					//Zipcode event 
					etzip = (EditText)findViewById(R.id.zipcodeedit);	
					String zipcode1 = etzip.getText().toString();
					
					
					//Email event 
					etemail = (EditText)findViewById(R.id.emailedit);	
					String email1 = etemail.getText().toString();
					
					//Phone number event 
					etphone = (EditText)findViewById(R.id.phoneedit);	
					String phone1 = etphone.getText().toString();
					
	
					

					//define  DOB spinner month event
					spinnermonth = (Spinner) findViewById(R.id.spinner2);
					String txtspinnermonth = spinnermonth.getSelectedItem().toString();
					
					//define  DOB spinner day event
					spinnerday = (Spinner) findViewById(R.id.spinner3);
					String txtspinnerday = spinnerday.getSelectedItem().toString();
					
					//define  DOB spinner year event
					spinneryear = (Spinner) findViewById(R.id.spinner4);
					String txtspinneryear = spinneryear.getSelectedItem().toString();
					
					
					/********************************************************************************
						* Set the messages in string builder for toast
					*********************************************************************************/
					
					
					StringBuilder sb = new StringBuilder();
					sb.append("Address : "
							+ etaddress.getText().toString());
					sb.append("\n");
					sb.append("City : "
							+ etcity.getText().toString());
					sb.append("\n");
					sb.append("Zip : "
							+ etzip.getText().toString());
					sb.append("\n");
					sb.append("email : "
							+ etemail.getText().toString());
					sb.append("\n");
					sb.append("Phone : "
							+ etphone.getText().toString());
					sb.append("\n");
					sb.append("Spinner 1 :"
					+ String.valueOf(spinneryear
					.getSelectedItem()) + String.valueOf(spinnermonth
					.getSelectedItem()) +String.valueOf(spinnerday
					.getSelectedItem()));
					sb.append("\n");
					
					
				/********************************************************************************
					* Validating all the empty strings 
				*********************************************************************************/
					
					
					if (etaddress.getText().toString().isEmpty()
							|| etcity.getText().toString().isEmpty()
							|| etzip.getText().toString().isEmpty()
							|| etemail.getText().toString().isEmpty() ||
						etphone.getText().toString().isEmpty() ||
						spinneryear.getSelectedItem().toString().isEmpty() ||
						spinnermonth.getSelectedItem().toString().isEmpty() ||
						spinnerday.getSelectedItem().toString().isEmpty())
						
						{
						Toast.makeText(getApplicationContext(),
								"Please fill all values", Toast.LENGTH_LONG)
								.show();
					     }
					
					
			/********************************************************************************
					* Send email with the string buffer contents
			*********************************************************************************/			
					
					else
					{
							
					Toast.makeText(getApplicationContext(),
							sb.toString(), Toast.LENGTH_LONG)
							.show();
					Intent i = new Intent(Intent.ACTION_SEND);
					i.setType("message/rfc822");
					i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"sunilsrinivaz@yahoo.co.in"});
					i.putExtra(Intent.EXTRA_SUBJECT, "Personal details send through App");
					i.putExtra(Intent.EXTRA_TEXT   , sb.toString());
					
					try {
					    startActivity(Intent.createChooser(i, "Send mail..."));
					} catch (android.content.ActivityNotFoundException ex) {
					    Toast.makeText(Profile.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
					}}
		
				}
				catch (NumberFormatException e){
					// Display a toast message if any value is  null
					
					Toast.makeText(getBaseContext(),
							"All fileds must be entered", Toast.LENGTH_LONG)
							.show();
				}
			}
		});
		
	/********************************************************************************
		* Navigate to the student page to fill  in the student details
	*********************************************************************************/
		
		
		b2 = (Button) findViewById(R.id.btnStudent);	
		b2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, Subject.class);
				startActivity(intent);
			}

		});
		
		
		
		
  }

//----------------------------------------------------------------
// General utilities
//----------------------------------------------------------------


//----------------------------------------------------------------
// getSelection
//----------------------------------------------------------------
private int getSelection()
{
	return selection;
}

//----------------------------------------------------------------
// setSelection
//----------------------------------------------------------------
private void setSelection(int selection)
{
	this.selection = selection;
}
/**
 * Hides the soft keyboard
 */
public void hideSoftKeyboard() {
    if(getCurrentFocus()!=null) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
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

}
