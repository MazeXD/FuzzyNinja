package com.fuzzyninja;

import java.util.Hashtable;

public class FixManager {
    private Hashtable<String, String> packageRemaps = new Hashtable<String, String>();
    
    public FixManager() 
    {
	
    }
    
    public void remapPackage(String from, String to)
    {
	packageRemaps.put(from, to);
    }
    
    
    
    public Hashtable<String, String> getPackageRemaps()
    {
	return this.packageRemaps;
    }
}
