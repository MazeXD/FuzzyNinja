package com.fuzzyninja.data;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

public class BaseData implements Serializable{
    private static final long serialVersionUID = 1L;
    
    protected Collection<AnnotationData> annotations = new ArrayList<AnnotationData>();

    public Collection<AnnotationData> getAnnotations() {
	return this.annotations;
    }
    
    public void addAnnotation(AnnotationData annotation)
    {
	this.annotations.add(annotation);
    }
    
    @SuppressWarnings("unchecked")
    private void readObject(ObjectInputStream input) throws ClassNotFoundException, IOException {
	this.annotations = (Collection<AnnotationData>)input.readObject();
    }
    
    private void writeObject(ObjectOutputStream output) throws IOException {
	output.writeObject(this.annotations);
    }
}
