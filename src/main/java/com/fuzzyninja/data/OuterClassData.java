package com.fuzzyninja.data;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class OuterClassData implements Serializable{
    private static final long serialVersionUID = 1L;
    
    private String parent;
    private String method = null;
    private String desc = null;
    
    public OuterClassData(String parent, String method, String desc) {
	this.parent = parent;
	this.method = method;
	this.desc = desc;
    }
    
    public boolean isEnclosed()
    {
	return this.method != null && this.method != "";
    }
    
    public String getName()
    {
	return this.parent;
    }
    
    public String getMethod()
    {
	return this.method;
    }
    
    public String getMethodDescriptor()
    {
	return this.desc;
    }
    
    private void readObject(ObjectInputStream input) throws ClassNotFoundException, IOException {
	this.parent = input.readUTF();
	this.method = input.readUTF();
	this.desc = input.readUTF();
    }
    
    private void writeObject(ObjectOutputStream output) throws IOException {
	output.writeUTF(this.parent == null ? "" : this.parent);
	output.writeUTF(this.method == null ? "" : this.method);
	output.writeUTF(this.desc == null ? "" : this.desc);
    }
}
