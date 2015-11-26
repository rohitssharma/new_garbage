package com.garbagebin.models;

/**
 * Created by sharanjeet on 30/10/15.
 */
public class MyObject {

    public String objectName;

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    // constructor for adding sample data
    public  MyObject(String objectName){

        this.objectName = objectName;
    }

    // constructor for adding sample data
    public  MyObject(){
    }
}
