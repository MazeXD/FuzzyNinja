package com.fuzzyninja.scripting.data;

import com.fuzzyninja.data.MethodData;

public class ScriptedMethodData extends ScriptedBaseData {
    private MethodData methodData;

    public ScriptedMethodData(MethodData methodData) {
	super(methodData);

	this.methodData = methodData;
    }
    
    public int getAccessModifier()
    {
	return this.methodData.getAccessModifier();
    }
    
    public String getDescriptor()
    {
	return this.methodData.getDescriptor();
    }
    
    public String getSignature()
    {
	return this.methodData.getSignature();
    }
    
    public String getName()
    {
	return this.methodData.getName();
    }
}
