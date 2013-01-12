package com.fuzzyninja.scripting.data;

import com.fuzzyninja.data.OuterClassData;

public class ScriptedOuterClassData {
    private OuterClassData outerClassData;
    
    public ScriptedOuterClassData(OuterClassData outerClassData)
    {
	this.outerClassData = outerClassData;
    }
    
    public boolean isEnclosed()
    {
	return this.outerClassData.isEnclosed();
    }
    
    public String getName()
    {
	return this.outerClassData.getName();
    }
    
    public String getMethod()
    {
	return this.outerClassData.getMethod();
    }
    
    public String getMethodDescriptor()
    {
	return this.outerClassData.getMethodDescriptor();
    }
}
