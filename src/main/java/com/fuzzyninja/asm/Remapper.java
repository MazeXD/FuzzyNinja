package com.fuzzyninja.asm;

import com.fuzzyninja.remapper.RemapManager;

public class Remapper extends org.objectweb.asm.commons.Remapper {
    private RemapManager remapManager;

    public Remapper(RemapManager remapManager) {
	this.remapManager = remapManager;
    }

    @Override
    public String mapMethodName(String owner, String name, String desc) {
	return remapManager.remapMethod(owner, name, desc);
    }

    @Override
    public String mapFieldName(String owner, String name, String desc) {
	return remapManager.remapField(owner, name);
    }

    @Override
    public String map(String typeName) {
	return remapManager.remapClass(typeName);
    }
}
