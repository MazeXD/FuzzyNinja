package com.fuzzyninja.scripting.data;

import com.fuzzyninja.data.FieldData;

public class ScriptedFieldData extends ScriptedBaseData {
    private FieldData fieldData;

    public ScriptedFieldData(FieldData fieldData) {
	super(fieldData);

	this.fieldData = fieldData;
    }
    
    public int getAccessModifier()
    {
	return this.fieldData.getAccessModifier();
    }
    
    public String getDescriptor()
    {
	return this.fieldData.getDescriptor();
    }
    
    public String getName()
    {
	return this.fieldData.getName();
    }
}
