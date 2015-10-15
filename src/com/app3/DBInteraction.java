//========================================================================================================
//
//  Title:       DBInteractionCollection
//  Course:      CSC 5991  Mobile Application Development
//  Application: Student Subject Recommendation App
//  Date:        7/3/2015
//  Description:
//  	This method allows program to access collection of a database from application
//		
//===========================================================================================================
package com.app3;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

import java.net.UnknownHostException;

public class DBInteraction {

    private final DB db;

  //--------------------------------------------------
  //Class DBInteractioncollection
  //--------------------------------------------------
    public DBInteraction() {
        MongoClient client = null;
        try {
            client = new MongoClient(
                    new MongoClientURI("mongodb://Sunil:Android@ds047812.mongolab.com:47812/student"));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        if (client == null)
            db = null;
        else
            db = client.getDB("student");
    }

    public DB getDb() {
        return db;
    }

    public void insert(DBObject dbObject) {
        DBCollection collection = getDb().getCollection("user1");
        collection.insert(dbObject);
    }
}