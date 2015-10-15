//========================================================================================================
//
//  Title:       ActOption2 
//  Course:      CSC 5991  Mobile Application Development
//  Application: Student Subject Recommendation App
//  Description:
//  	This activity displays a screen with a listview of  
//		Most Opted subjects(Top3 subjects)
//		No Input is required from user
//      	Program uses Mapreduce Queries to fetch results from database.
//===========================================================================================================
package com.app3;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MapReduceCommand;
import com.mongodb.MapReduceOutput;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.json.JSONException;
import org.json.JSONObject;

public class ActOption2 extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listfile);

		final ListView list1 = (ListView) findViewById(R.id.listview_indexing_results);
		
		
/********************************************************************************
    * All background data should start in asynctask
*********************************************************************************/ 		
		new AsyncTask<Void, Void, List>() {
			private DBInteraction dbInteraction;

			@Override
			protected List doInBackground(Void... params) {
				dbInteraction = new DBInteraction();
				DBCollection collection = dbInteraction.getDb().getCollection("user4");
				DBCollection collectiondata1 = dbInteraction.getDb().getCollection("user3");
				collectiondata1.drop();
/********************************************************************************
            	* Map Function
  *********************************************************************************/ 
					String map = "function () {" +
							"emit( {subject: this.Subjects}, {recs: 1});}" ;
/********************************************************************************
			    	* Reduce function
*********************************************************************************/ 
					
					String reduce = "function (keys, vals) {" +
							"var ret = {recs : 0};"+
							"for (var i=0;i<vals.length; i++) {"+
							"ret.recs += vals[i].recs;"+
							"}" +
							"return ret;}";
/********************************************************************************
			    	* Execute the map reduce
*********************************************************************************/					

					MapReduceCommand cmd = new MapReduceCommand(collection, map, reduce,
							null, MapReduceCommand.OutputType.INLINE, null);

					MapReduceOutput out = collection.mapReduce(cmd);
/********************************************************************************
			    	* Parse the results and store in arraylist
*********************************************************************************/ 	
					List<String> results = new ArrayList<String>();
					for (DBObject dbObject : out.results()) {
                    JSONObject jsonObject;
						try {
							System.out.println(dbObject.toString());
							jsonObject = new JSONObject(dbObject.get("_id").toString());
							String subject = jsonObject.getString("subject");
							JSONObject object = new JSONObject(dbObject.get("value").toString());
					         String recs = Integer.toString(object.getInt("recs"));
							results.add("subjects:" + subject + "\t "+ "counts:" + recs );
/********************************************************************************
		* Create a new collection and store the above results in collection
*********************************************************************************/ 	                         			
							BasicDBObject db1 = new BasicDBObject();
							db1.put("Subjects", subject);
							db1.put("recs", Integer.parseInt(recs));
							DBCollection collectiondata = dbInteraction.getDb().getCollection("user3");
							
							collectiondata.insert(db1);							
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
					ArrayAdapter<String> indexingResultsAdapter = new ArrayAdapter<String>(ActOption2.this,
	                        android.R.layout.simple_list_item_1, results);
	                list1.setAdapter(indexingResultsAdapter);
	            }

		}.execute();
		
		/********************************************************************************
    	* All background data should start in asynctask
*********************************************************************************/ 
		final ListView lista = (ListView) findViewById(R.id.listview_indexing_results);
        new AsyncTask<Void, Void, List>() {
            private DBInteraction dbInteraction;

            @Override
            protected List doInBackground(Void... params) {
                dbInteraction = new DBInteraction();
                DBCursor cursor = dbInteraction.getDb().getCollection("user3").find();
                cursor.sort(new BasicDBObject("recs", -1)).limit(3);
                List<String> results = new ArrayList<String>();
               
                while (cursor.hasNext()) {
                	String[] a=  cursor.next().toString().split(",");
                	a[2] = a[2].replace("}","");
                	results.add(a[1]+ a[2]);
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
				ArrayAdapter<String> indexingResultsAdapter = new ArrayAdapter<String>(ActOption2.this,
                        android.R.layout.simple_list_item_1, results);
                lista.setAdapter(indexingResultsAdapter);
            }
        }.execute();
 
		
	}
}