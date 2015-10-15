package com.app3;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

import java.net.UnknownHostException;

public class DBInteractioncollection {

    private final DB db;
  //--------------------------------------------------
  //Class DBInteractioncollection
  //--------------------------------------------------
    public DBInteractioncollection() {
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
        DBCollection collection = getDb().getCollection("user4");
        collection.insert(dbObject);
    }
}