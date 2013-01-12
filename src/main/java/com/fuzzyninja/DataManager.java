package com.fuzzyninja;

import java.util.Hashtable;

import com.fuzzyninja.data.ClassData;

public class DataManager {
    private Hashtable<String, ClassData> dataEntries = new Hashtable<String, ClassData>();
    
    public DataManager() {
	
    }
    
    public void addData(ClassData data)
    {
	this.dataEntries.put(data.getClassName(), data);
    }
    
    public Hashtable<String, ClassData> getDataEntries()
    {
	return this.dataEntries;
    }
}
