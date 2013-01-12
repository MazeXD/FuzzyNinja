package com.fuzzyninja.scripting;

import com.fuzzyninja.FixManager;

public class ScriptedFixManager {
    private FixManager fixManager;
    
    public ScriptedFixManager(FixManager fixManager)
    {
	this.fixManager = fixManager;
    }
    
    public void remapPackage(String from, String to)
    {
	this.fixManager.remapPackage(from, to);
    }
}
