package com.fuzzyninja.asm;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Opcodes;

import com.fuzzyninja.data.AnnotationData;
import com.fuzzyninja.data.FieldData;

public class FieldDataCollector extends FieldVisitor {
    private FieldData field;
    
    public FieldDataCollector(FieldVisitor visitor, FieldData field) {
	super(Opcodes.ASM4, visitor);
	
	this.field = field;
    }

    @Override
    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
	AnnotationData annotation = new AnnotationData(desc);
	
	field.addAnnotation(annotation);
	
	return new AnnotationDataCollector(super.visitAnnotation(desc, visible), annotation);
    }
}
