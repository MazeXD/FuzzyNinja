package com.fuzzyninja.scripting.data;

import java.util.Map;

import com.fuzzyninja.data.AnnotationData;

public class ScriptedAnnotationData {
    private AnnotationData annotationData;

    public ScriptedAnnotationData(AnnotationData annotationData) {
	this.annotationData = annotationData;
    }

    public String getDescriptor() {
	return this.annotationData.getDescriptor();
    }

    public Map<String, Object> getData() {
	return this.annotationData.getData();
    }
}
