package com.fuzzyninja.scripting.data;

import java.util.ArrayList;
import java.util.Collection;

import com.fuzzyninja.data.ClassData;
import com.fuzzyninja.data.FieldData;
import com.fuzzyninja.data.MethodData;

public class ScriptedClassData extends ScriptedBaseData{
    private ClassData classData;
    
    private ScriptedOuterClassData outerClass = null;
    private Collection<ScriptedFieldData> fields = new ArrayList<ScriptedFieldData>();
    private Collection<ScriptedMethodData> methods = new ArrayList<ScriptedMethodData>();
    
    public ScriptedClassData(ClassData classData)
    {
	super(classData);
	
	if(classData.isEnclosed())
	{
	    this.outerClass = new ScriptedOuterClassData(classData.getOuterClass());
	}
	
	for(FieldData field : classData.getFields())
	{
	    fields.add(new ScriptedFieldData(field));
	}
	
	for(MethodData method : classData.getMethods())
	{
	    methods.add(new ScriptedMethodData(method));
	}
	
	this.classData = classData;
    }
    
    public boolean isEnclosed()
    {
	return this.classData.isEnclosed();
    }
    
    public int getAccessModifier() {
	return this.classData.getAccessModifier();
    }

    public String getSuperClass() {
	return this.classData.getSuperClass();
    }

    public String getClassName() {
	return this.classData.getClassName();
    }
    
    public Collection<String> getInterfaces() {
	return this.classData.getInterfaces();
    }
    
    public ScriptedOuterClassData getOuterClass() {
	return this.outerClass;
    }
    
    public Collection<ScriptedFieldData> getFields() {
	return this.fields;
    }

    public Collection<ScriptedMethodData> getMethods() {
	return this.methods;
    }
}
