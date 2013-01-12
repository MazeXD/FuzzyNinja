package com.fuzzyninja.data;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class ClassData extends BaseData implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private int access = 0;
    private String superClass = "";
    private String name = "";

    private OuterClassData outerClass = null;
    private Collection<String> interfaces = new ArrayList<String>();
    private Collection<FieldData> fields = new ArrayList<FieldData>();
    private Collection<MethodData> methods = new ArrayList<MethodData>();

    public ClassData(int access, String name, String superClass) {
	this.access = access;
	this.name = name;
	this.superClass = superClass;
    }

    public boolean isEnclosed() {
	return this.outerClass != null;
    }

    public int getAccessModifier() {
	return this.access;
    }

    public String getSuperClass() {
	return this.superClass;
    }

    public String getClassName() {
	return this.name;
    }

    public Collection<String> getInterfaces() {
	return this.interfaces;
    }

    public OuterClassData getOuterClass() {
	return this.outerClass;
    }

    public Collection<FieldData> getFields() {
	return this.fields;
    }

    public Collection<MethodData> getMethods() {
	return this.methods;
    }

    public void setOuterClass(OuterClassData outerClass) {
	this.outerClass = outerClass;
    }

    public void addInterfaces(String[] interfaces) {
	this.interfaces.addAll(Arrays.asList(interfaces));
    }

    public void addField(FieldData field) {
	this.fields.add(field);
    }

    public void addMethod(MethodData method) {
	this.methods.add(method);
    }

    @SuppressWarnings("unchecked")
    private void readObject(ObjectInputStream input) throws ClassNotFoundException, IOException {
	this.access = input.readInt();
	this.superClass = input.readUTF();
	this.name = input.readUTF();

	this.outerClass = (OuterClassData)input.readObject();
	this.interfaces = (Collection<String>)input.readObject();
	this.fields = (Collection<FieldData>)input.readObject();
	this.methods = (Collection<MethodData>)input.readObject();
    }
    
    private void writeObject(ObjectOutputStream output) throws IOException {
	output.writeInt(this.access);
	output.writeUTF(this.superClass == null ? "" : this.superClass);
	output.writeUTF(this.name == null ? "" : this.name);

	output.writeObject(this.outerClass);
	output.writeObject(this.interfaces);
	output.writeObject(this.fields);
	output.writeObject(this.methods);
    }
}
