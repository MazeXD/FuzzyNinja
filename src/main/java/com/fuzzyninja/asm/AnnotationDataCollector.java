package com.fuzzyninja.asm;

import java.util.ArrayList;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Opcodes;

import com.fuzzyninja.data.AnnotationData;

public class AnnotationDataCollector extends AnnotationVisitor {
    private AnnotationData annotation;
    private ArrayList<String> array;

    public AnnotationDataCollector(AnnotationVisitor visitor,
	    AnnotationData annotation) {
	this(visitor, annotation, null);
    }

    public AnnotationDataCollector(AnnotationVisitor visitor,
	    AnnotationData annotation, ArrayList<String> array) {
	super(Opcodes.ASM4, visitor);

	this.annotation = annotation;
	this.array = array;
    }

    @Override
    public void visit(String name, Object value) {
	if (this.array == null) {
	    annotation.addData(name, value.toString());
	} else {
	    this.array.add(value.toString());
	}

	super.visit(name, value);
    }

    @Override
    public AnnotationVisitor visitArray(String name) {
	ArrayList<String> array = new ArrayList<String>();
	annotation.addData(name, array);

	return new AnnotationDataCollector(super.visitArray(name), annotation, array);
    }

    @Override
    public void visitEnum(String name, String desc, String value) {
	this.annotation.addData(name, desc.substring(0, desc.length() - 1) + "." + value + ";");
	
	super.visitEnum(name, desc, value);
    }
}
