package com.fuzzyninja.asm;

import com.fuzzyninja.DataManager;
import com.fuzzyninja.data.ClassData;
import com.fuzzyninja.remapper.RemapManager;

public class Remapper extends org.objectweb.asm.commons.Remapper {
    private RemapManager remapManager;
    private DataManager dataManager;

    public Remapper(RemapManager remapManager, DataManager dataManager) {
	this.remapManager = remapManager;
	this.dataManager = dataManager;
    }

    @Override
    public String mapMethodName(String owner, String name, String desc) {
	String remapped = remapManager.remapMethod(owner, name, desc);
	
	if(remapped == name)
	{
	    ClassData data = dataManager.getData(owner);
	    
	    if(data != null){
		data = dataManager.getData(data.getSuperClass());
	    }
	    
	    while(data != null)
	    {
		remapped = remapManager.remapMethod(data.getClassName(), name, desc);
		
		if(remapped != name)
		{
		    break;
		}

		data = dataManager.getData(data.getSuperClass());
	    }
	}
	
	return remapped;
    }

    @Override
    public String mapFieldName(String owner, String name, String desc) {
	return remapManager.remapField(owner, name);
    }

    @Override
    public String map(String typeName) {
	return remapManager.remapClass(typeName);
    }
}
