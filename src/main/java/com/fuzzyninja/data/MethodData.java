package com.fuzzyninja.data;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class MethodData extends BaseData implements Serializable{
    private static final long serialVersionUID = 1L;
    
    private int access;
    private String desc;
    private String signature;
    private String name;
    
    public MethodData(int access, String desc, String signature, String name)
    {
	this.access = access;
	this.desc = desc;
	this.signature = signature;
	this.name = name;
    }
    
    public int getAccessModifier()
    {
	return this.access;
    }
    
    public String getDescriptor()
    {
	return this.desc;
    }
    
    public String getSignature()
    {
	return this.signature;
    }
    
    public String getName()
    {
	return this.name;
    }
    
    private void readObject(ObjectInputStream input) throws ClassNotFoundException, IOException {
	this.access = input.readInt();
	this.desc = input.readUTF();
	this.signature = input.readUTF();
	this.name = input.readUTF();
    }
    
    private void writeObject(ObjectOutputStream output) throws IOException {
	output.writeInt(this.access);
	output.writeUTF(this.desc == null ? "" : this.desc);
	output.writeUTF(this.signature == null ? "" : this.signature);
	output.writeUTF(this.name == null ? "" : this.name);
    }
}
