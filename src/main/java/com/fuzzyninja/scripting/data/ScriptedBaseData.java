package com.fuzzyninja.scripting.data;

import java.util.ArrayList;
import java.util.Collection;

import com.fuzzyninja.data.AnnotationData;
import com.fuzzyninja.data.BaseData;

public class ScriptedBaseData {
    private Collection<ScriptedAnnotationData> annotations = new ArrayList<ScriptedAnnotationData>();

    public ScriptedBaseData(BaseData baseData) {
	for(AnnotationData annotation : baseData.getAnnotations())
	    this.annotations.add(new ScriptedAnnotationData(annotation));
    }
    
    public Collection<ScriptedAnnotationData> getAnnotations() {
	return this.annotations;
    }
}
