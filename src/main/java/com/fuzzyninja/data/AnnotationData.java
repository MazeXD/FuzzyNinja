package com.fuzzyninja.data;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Hashtable;
import java.util.Map;

public class AnnotationData implements Serializable{
    private static final long serialVersionUID = 1L;
    
    private String desc;
    private Map<String, Object> data = new Hashtable<String, Object>();
    
    public AnnotationData(String desc)
    {
	this.desc = desc;
    }
    
    public String getDescriptor()
    {
	return this.desc;
    }
    
    public Map<String, Object> getData()
    {
	return this.data;
    }
    
    public void addData(String name, Object value)
    {
	if(name == null)
	{
	    name = "enum";
	}
	
	data.put(name, value);
    }
    
    @SuppressWarnings("unchecked")
    private void readObject(ObjectInputStream input) throws ClassNotFoundException, IOException {
	this.desc = input.readUTF();
	this.data = (Map<String, Object>)input.readObject();
    }
    
    private void writeObject(ObjectOutputStream output) throws IOException {
	output.writeUTF(this.desc == null ? "" : this.desc);
	output.writeObject(this.data);
    }
}
