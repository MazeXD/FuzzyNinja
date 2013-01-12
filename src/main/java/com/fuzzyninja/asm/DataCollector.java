package com.fuzzyninja.asm;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import com.fuzzyninja.data.AnnotationData;
import com.fuzzyninja.data.ClassData;
import com.fuzzyninja.data.FieldData;
import com.fuzzyninja.data.MethodData;
import com.fuzzyninja.data.OuterClassData;

public class DataCollector extends ClassVisitor {
    private ClassData data = null;

    public DataCollector() {
	super(Opcodes.ASM4, null);
    }

    @Override
    public void visit(int version, int access, String name, String signature,
	    String superName, String[] interfaces) {
	this.data = new ClassData(access, name, superName);

	this.data.addInterfaces(interfaces);

	super.visit(version, access, name, signature, superName, interfaces);
    }

    @Override
    public FieldVisitor visitField(int access, String name, String desc,
	    String signature, Object value) {
	this.data.addField(new FieldData(access, desc, name));

	return super.visitField(access, name, desc, signature, value);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc,
	    String signature, String[] exceptions) {
	MethodData method = new MethodData(access, desc, signature, name);

	this.data.addMethod(method);

	return new MethodDataCollector(super.visitMethod(access, name, desc, signature, exceptions), method);
    }

    @Override
    public void visitOuterClass(String owner, String name, String desc) {
	this.data.setOuterClass(new OuterClassData(owner, name, desc));
	
	super.visitOuterClass(owner, name, desc);
    }

    @Override
    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
	AnnotationData annotation = new AnnotationData(desc);
	
	data.addAnnotation(annotation);

	return new AnnotationDataCollector(super.visitAnnotation(desc, visible), annotation);
    }   
    
    public boolean hasClassData() {
	return this.data != null;
    }

    public ClassData getClassData() {
	return this.data;
    }
}
