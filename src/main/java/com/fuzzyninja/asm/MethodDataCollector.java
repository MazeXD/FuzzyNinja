package com.fuzzyninja.asm;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import com.fuzzyninja.data.AnnotationData;
import com.fuzzyninja.data.MethodData;

public class MethodDataCollector extends MethodVisitor {
    private MethodData method;
    
    public MethodDataCollector(MethodVisitor visitor, MethodData method) {
	super(Opcodes.ASM4, visitor);
	
	this.method = method;
    }

    @Override
    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
	AnnotationData annotation = new AnnotationData(desc);
	
	method.addAnnotation(annotation);
	
	return new AnnotationDataCollector(super.visitAnnotation(desc, visible), annotation);
    }
}
