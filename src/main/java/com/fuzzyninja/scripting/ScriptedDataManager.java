package com.fuzzyninja.scripting;

import java.util.Collection;
import java.util.Hashtable;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.fuzzyninja.DataManager;
import com.fuzzyninja.data.ClassData;
import com.fuzzyninja.scripting.data.ScriptedClassData;

public class ScriptedDataManager {
    private Hashtable<String, ScriptedClassData> dataEntries = new Hashtable<String, ScriptedClassData>();
    
    public ScriptedDataManager(DataManager dataManager)
    {
	for(Entry<String, ClassData> entry : dataManager.getDataEntries().entrySet())
	{
	    dataEntries.put(entry.getKey(), new ScriptedClassData(entry.getValue()));
	}
    }
    
    public boolean hasDataFor(String className)
    {
	if(className.endsWith(";"))
	{
	    className = className.substring(1, className.length() - 1);
	}
	
	return this.dataEntries.containsKey(className);
    }
    
    public ScriptedClassData getDataFor(String className)
    {
	if(className.endsWith(";"))
	{
	    className = className.substring(1, className.length() - 1);
	}
	
	return hasDataFor(className) ? this.dataEntries.get(className) : null;
    }
    
    public Collection<String> getClassNames()
    {
	return this.dataEntries.keySet();
    }
    
    public Collection<ScriptedClassData> getDataValues()
    {
	return this.dataEntries.values();
    }
    
    public Set<Entry<String, ScriptedClassData>> getTable()
    {
	return this.dataEntries.entrySet();
    }
}
