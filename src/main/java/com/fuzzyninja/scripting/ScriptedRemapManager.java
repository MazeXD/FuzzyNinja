package com.fuzzyninja.scripting;

import com.fuzzyninja.remapper.RemapManager;

public class ScriptedRemapManager {
    private RemapManager remapManager;

    public ScriptedRemapManager(RemapManager remapManager) {
	this.remapManager = remapManager;
    }

    public void remapPackage(String from, String to) {
	remapManager.addPackage(from, to);
    }

    public void remapClass(String nms, String from, String to) {
	remapManager.addClass(nms, from, to);
    }

    public void remapMethod(String name, String desc, String from, String to) {
	remapManager.addMethod(name, desc, from, to);
    }

    public void remapField(String name, String from, String to) {
	remapManager.addField(name, from, to);
    }
}
