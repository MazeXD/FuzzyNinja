package com.fuzzyninja;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URISyntaxException;
import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import com.fuzzyninja.io.JarProcessor;

import joptsimple.OptionParser;
import joptsimple.OptionSet;

public class Main {
    private final static Logger LOGGER = Logger.getLogger("fuzzyninja");
    private final static String VERSION = "0.0.1";

    private static File inputFile;
    private static File outputFile;
    private static File scriptFile;

    private static File scriptsFolder;
    private static File libsFolder;
    private static File cacheFolder;

    public static void main(String[] args) {
	setupLogger();

	setupDirectories();

	parseOptions(args);

	LOGGER.info("Processing " + inputFile.getName());

	try {
	    JarProcessor processor = new JarProcessor(scriptFile, libsFolder, cacheFolder);

	    processor.process(inputFile, outputFile);
	} catch (IOException e) {
	    LOGGER.log(Level.SEVERE, "Failed to process jar", e);
	    System.exit(ExitCode.ProcessingFailed.code());
	}

	LOGGER.info("Processed " + inputFile.getName() + " successfully");
    }

    private static void setupDirectories() {
	File temp = null;
	
	try {
	    temp = new File(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI());
	} 
	catch (URISyntaxException e) {}

	if (temp == null) {
	    temp = new File(".");
	}

	scriptsFolder = new File(temp, "scripts");

	if (!scriptsFolder.exists()) {
	    scriptsFolder.mkdirs();
	}

	libsFolder = new File(temp, "libs");

	if (!libsFolder.exists()) {
	    libsFolder.mkdirs();
	}

	cacheFolder = new File(temp, "cache");

	if (!cacheFolder.exists()) {
	    cacheFolder.mkdirs();
	}
    }

    private static void parseOptions(String[] args) {
	OptionParser parser = new OptionParser() {
	    {
		accepts("help", "Display help");

		accepts("in", "Input file").withRequiredArg().ofType(File.class);
		accepts("out", "Output file").withRequiredArg().ofType(File.class);
		accepts("script", "Script file").withRequiredArg().ofType(File.class);
		accepts("clearcache", "Clears the cache");
	    }
	};

	OptionSet options = null;

	try {
	    options = parser.parse(args);
	} catch (joptsimple.OptionException ex) {
	    LOGGER.log(Level.SEVERE, ex.getLocalizedMessage());
	}

	if (options == null || options.has("?") || ((!options.has("in") || !options.has("script")) && !options.has("clearcache"))) {
	    try {
		System.out.println("FuzzyNinja v" + VERSION);
		parser.printHelpOn(System.out);
	    } catch (IOException ex) {
		LOGGER.log(Level.SEVERE, null, ex);
	    }

	    System.exit(ExitCode.Normal.code());
	}

	if (options.has("clearcache")) {
	    cacheFolder.delete();
	    System.exit(ExitCode.ClearedCache.code());
	}

	inputFile = (File) options.valueOf("in");
	scriptFile = (File) options.valueOf("script");

	if (scriptFile.isAbsolute()) {
	    LOGGER.severe("Only relative paths are allowed (base = scripts folder)");
	    System.exit(ExitCode.WrongScriptPath.code());
	}

	File temp = new File(scriptsFolder, scriptFile.getPath());

	if (!temp.exists()) {
	    LOGGER.severe("Script file doesn't exist");
	    System.exit(ExitCode.ScriptDoesntExist.code());
	} else if (!scriptFile.getName().endsWith(".groovy")) {
	    LOGGER.severe("Script must be made in groovy and it must end with \".groovy\"");
	    System.exit(ExitCode.NoGroovyScript.code());
	}

	if (options.has("out")) {
	    outputFile = (File) options.valueOf("out");
	} else {
	    String extension = inputFile.getName();
	    extension = extension.substring(extension.lastIndexOf('.'));

	    outputFile = new File(inputFile.getParentFile(), inputFile.getName().replace(extension, ".trans" + extension));
	}

	if (!inputFile.exists()) {
	    LOGGER.log(Level.SEVERE, "Inputfile doesn't exist");

	    System.out.println(inputFile.getAbsolutePath());
	    System.exit(ExitCode.MissingInput.code());
	}
    }

    private static void setupLogger() {
	LOGGER.setUseParentHandlers(false);
	LOGGER.addHandler(new ConsoleHandler() {
	    {
		// Formatter by FML (FMLLogFormatter)
		// hhttps://github.com/cpw/FML/blob/master/common/cpw/mods/fml/relauncher/FMLLogFormatter.java
		setFormatter(new Formatter() {
		    final String LINE_SEPARATOR = System.getProperty("line.separator");

		    @Override
		    public String format(LogRecord record) {
			StringBuilder msg = new StringBuilder();
			msg.append("[" + record.getLevel().getLocalizedName() + "] ");

			msg.append(record.getMessage());
			msg.append(LINE_SEPARATOR);
			Throwable thr = record.getThrown();

			if (thr != null) {
			    StringWriter thrDump = new StringWriter();
			    thr.printStackTrace(new PrintWriter(thrDump));
			    msg.append(thrDump.toString());
			}

			return msg.toString();
		    }
		});
	    }
	});
    }

    public static File getScriptsFolder() {
	return scriptsFolder;
    }

    public static enum ExitCode {
	Normal(0), 
	MissingInput(1), 
	ProcessingFailed(2), 
	DataCollectingFailed(3), 
	ScriptDoesntExist(4), 
	WrongScriptPath(5), 
	NoGroovyScript(6), 
	GroovyEngineFailed(7), 
	ClearedCache(8);

	private int exitCode = 0;

	ExitCode(int exitCode) {
	    this.exitCode = exitCode;
	}

	public int code() {
	    return this.exitCode;
	}
    }
}
