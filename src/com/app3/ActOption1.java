//========================================================================================================
//
//  Title:       ActOption1
//  Course:      CSC 5991  Mobile Application Development
//  Application: Student Subject Recommendation
//  Description:
//  	This activity displays a screen with 3 buttons. 
//		1.Popular per semester: Displays popular subjects for each semester
//		2.Most Opted subjects: Displays Top3 subjects
//		3.Popular Professors: Displays popular professors
//      	Program uses Mapreduce Queries to fetch results from dataset.
//===========================================================================================================
package com.app3;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.AliasActivity;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
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

public class ActOption1 extends ActionBarActivity {

	
	
	
/********************************************************************************
	* Define the controls
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
	setContentView(R.layout.queries);
/********************************************************************************
	* Define the variables
*********************************************************************************/ 	

	b1 = (Button) findViewById(R.id.btnprofstats);
	b2 = (Button) findViewById(R.id.btntopthree);
	b3 = (Button) findViewById(R.id.btnpoprof);

/********************************************************************************
	* Show the top5 professor status
*********************************************************************************/ 

	b1.setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			final ListView list1 = (ListView) findViewById(R.id.Query1);
/********************************************************************************
        	* All background data should start in asynctask
 *********************************************************************************/ 			

			new AsyncTask<Void, Void, List>() {
				private DBInteraction dbInteraction;

				@Override
				protected List doInBackground(Void... params) {
					dbInteraction = new DBInteraction();
					DBCollection collection = dbInteraction.getDb()
							.getCollection("user4");
		/********************************************************************************
	            	* Map Function
	       *********************************************************************************/ 

		String map1 = "function () {"
				+ "emit({subject: this.Subjects,semester: this.semester}, {recs: 1});} ";
/********************************************************************************
    	* Reduce function
*********************************************************************************/ 

		String reduce = "function (keys, vals) {"
				+ "var ret = {recs : 0};"
				+ "for (var i=0;i<vals.length; i++) {"
				+ "ret.recs += vals[i].recs;" + "}"
				+ "return ret;}";

/********************************************************************************
    	* Execute the map reduce
*********************************************************************************/ 		
		MapReduceCommand cmd = new MapReduceCommand(collection,
				map1, reduce, null,
				MapReduceCommand.OutputType.INLINE, null);

		MapReduceOutput out = collection.mapReduce(cmd);
/********************************************************************************
    	* Parse the results and store in arraylist
*********************************************************************************/ 		
		
		List<String> results = new ArrayList<String>();
		for (DBObject dbObject : out.results()) {
        JSONObject jsonObject;
			try {

				JSONObject object = new JSONObject(dbObject
						.get("_id").toString());
				String Subject = object.getString("subject");
				String semester= object.getString("semester");
				//JSONObject object1= object.getJSONObject("value");
				//String recs= Double.toString(object.getDouble("recs"));
				results.add(Subject +"\t"+ semester );
				} catch (JSONException e) {
				// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
			return results;
					}
				
/********************************************************************************
   * Post execute method runs after the execution in background
 *********************************************************************************/ 				
				@Override
	            protected void onPostExecute(List results) {
	                super.onPostExecute(results);
	                @SuppressWarnings("unchecked")
					ArrayAdapter<String> indexingResultsAdapter = new ArrayAdapter<String>(ActOption1.this,
	                        android.R.layout.simple_list_item_1, results);
	                list1.setAdapter(indexingResultsAdapter);
	            }

				}.execute();
			}
		});
	
//-----------------------------------------------------------------------------------//
	/********************************************************************************
	*  Executing the second QUery and navigate to the class for results
*********************************************************************************/ 	
	
	b2.setOnClickListener(new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(context, ActOption2.class);
			startActivity(intent);
		}

	});
/********************************************************************************
	* If validation passes, go the query class
*********************************************************************************/ 	
	
	b3.setOnClickListener(new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(context, ActOption3.class);
			startActivity(intent);
		}

	});
}
	
	
/********************************************************************************
	* Define the Menu
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
		.setMessage("Runs all the basic Queries using mongo db and java connections")
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


