package com.fuzzyninja.scripting;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.fuzzyninja.DataManager;
import com.fuzzyninja.FixManager;
import com.fuzzyninja.Main;
import com.fuzzyninja.remapper.RemapManager;

import groovy.lang.Binding;
import groovy.util.GroovyScriptEngine;
import groovy.util.ResourceException;
import groovy.util.ScriptException;

public class GroovyBinder {
    private final static Logger LOGGER = Logger.getLogger("fuzzyninja");

    private Binding binding;
    private String[] roots;

    public GroovyBinder(DataManager dataManager, FixManager fixManager, RemapManager remapManager) {
	this.roots = new String[] { Main.getScriptsFolder().getPath() };

	this.binding = new Binding();
	this.binding.setVariable("dataManager", new ScriptedDataManager(dataManager));
	this.binding.setVariable("fixManager", new ScriptedFixManager(fixManager));
	this.binding.setVariable("remapper", new ScriptedRemapManager(remapManager));
	this.binding.setVariable("scriptFolder", Main.getScriptsFolder());
	this.binding.setVariable("logger", LOGGER);
    }

    public void runScript(File script) {
	GroovyScriptEngine scriptEngine = null;

	try {
	    scriptEngine = new GroovyScriptEngine(this.roots);
	} catch (IOException e) {
	    LOGGER.log(Level.SEVERE, "Failed to start Groovy engine", e);
	    System.exit(Main.ExitCode.GroovyEngineFailed.code());
	}

	try {
	    scriptEngine.run(script.getPath(), this.binding);
	} catch (ResourceException e) {
	    LOGGER.log(Level.SEVERE, "Failed to access script(" + script.getName() + "): " + e.getMessage(), e);
	    System.exit(Main.ExitCode.GroovyEngineFailed.code());
	} catch (ScriptException e) {
	    LOGGER.log(Level.SEVERE, "Failed to execute script(" + script.getName() + "): " + e.getMessage(), e);
	    System.exit(Main.ExitCode.GroovyEngineFailed.code());
	}
    }
}
