package com.fuzzyninja.io;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.commons.RemappingClassAdapter;

import com.fuzzyninja.DataManager;
import com.fuzzyninja.FixManager;
import com.fuzzyninja.Main;
import com.fuzzyninja.asm.DataCollector;
import com.fuzzyninja.asm.Remapper;
import com.fuzzyninja.data.ClassData;
import com.fuzzyninja.remapper.RemapManager;
import com.fuzzyninja.scripting.GroovyBinder;

public class JarProcessor {
    private final static Logger LOGGER = Logger.getLogger("fuzzyninja");

    private File script;
    private File libsFolder;
    private File cacheFolder;

    private DataManager dataManager = new DataManager();
    private FixManager fixManager = new FixManager();
    private RemapManager remapManager = new RemapManager();

    public JarProcessor(File script, File libsFolder, File cacheFolder) {
	this.script = script;
	this.libsFolder = libsFolder;
	this.cacheFolder = cacheFolder;
    }

    public void processScript() {
	if (this.script == null) {
	    return;
	}

	LOGGER.info("Executing script(" + this.script.getName() + ")");

	GroovyBinder binder = new GroovyBinder(this.dataManager,
		this.fixManager, this.remapManager);
	binder.runScript(this.script);
    }

    public boolean process(File input, File output) throws IOException {
	for(File file : libsFolder.listFiles())
	{
	    if(file.getName().endsWith(".jar") || file.getName().endsWith(".zip"))
	    {
		collectData(file);
	    }
	}
	
	collectData(input);

	processScript();

	ZipFile inJar = new ZipFile(input);

	Enumeration<? extends ZipEntry> entries = inJar.entries();
	ZipOutputStream outJar = null;

	try {
	    Remapper remapper = null;

	    outJar = new ZipOutputStream(new BufferedOutputStream(
		    new FileOutputStream(output)));

	    while (entries.hasMoreElements()) {
		ZipEntry inEntry = entries.nextElement();

		if (inEntry.isDirectory()) {
		    continue;
		}

		DataInputStream inStream = null;
		try {
		    inStream = new DataInputStream(new BufferedInputStream(
			    inJar.getInputStream(inEntry)));

		    if (!inEntry.getName().endsWith(".class")) {
			ZipEntry outEntry = (ZipEntry) inEntry.clone();
			outJar.putNextEntry(outEntry);

			byte[] buf = new byte[1024];
			int len = 0;

			do {
			    len = inStream.read(buf);
			    if (len > 0) {
				outJar.write(buf, 0, len);
			    }
			} while (len > 0);

			outJar.closeEntry();
			continue;
		    }

		    ClassReader reader = new ClassReader(inStream);
		    ClassWriter writer = new ClassWriter(
			    ClassWriter.COMPUTE_MAXS);
		    String className = reader.getClassName();

		    // TODO: Some kind of class fixing

		    // Remap class
		    if (this.remapManager.hasRemaps()) {
			if (remapper == null) {
			    remapper = new Remapper(this.remapManager, this.dataManager);
			}

			className = remapManager.remapClass(className);

			reader.accept(new RemappingClassAdapter(writer,
				remapper), ClassReader.EXPAND_FRAMES);
		    }

		    ZipEntry outEntry = new ZipEntry(className + ".class");
		    outJar.putNextEntry(outEntry);
		    outJar.write(writer.toByteArray());
		    outJar.closeEntry();

		    LOGGER.info("Processed " + reader.getClassName());
		} finally {
		    if (inStream != null) {
			inStream.close();
		    }
		}
	    }

	    outJar.closeEntry();
	} finally {
	    if (outJar != null) {
		outJar.close();
	    }

	    if (inJar != null) {
		inJar.close();
	    }
	}

	return false;
    }

    @SuppressWarnings("unchecked")
    private void collectData(File file) throws IOException {
	File cacheFile = new File(this.cacheFolder, file.getName() + ".cache");
	
	Collection<ClassData> classInformations = new ArrayList<ClassData>();
	
	if (cacheFile.exists()) {
	    FileInputStream fileIn = new FileInputStream(cacheFile);
	    ObjectInputStream in = new ObjectInputStream(fileIn);
	    
	    try {
		classInformations = (Collection<ClassData>)in.readObject();
	    } catch (ClassNotFoundException e) {
	    }
	    finally {
		if(in != null)
		{
		    in.close();
		}
	    
		if(fileIn != null)
		{
		    fileIn.close();
		}
	    }
	}
	
	if(classInformations.size() <= 0)
	{
	    ZipFile inJar = new ZipFile(file);

	    Enumeration<? extends ZipEntry> entries = inJar.entries();

	    LOGGER.info("Collecting data about " + inJar.getName());

	    try {
		while (entries.hasMoreElements()) {
		    ZipEntry inEntry = entries.nextElement();

		    if (inEntry.isDirectory()) {
			continue;
		    }

		    DataInputStream inStream = null;
		    try {
			inStream = new DataInputStream(new BufferedInputStream(
				inJar.getInputStream(inEntry)));
			String name = inEntry.getName();

			if (!name.endsWith(".class")) {
			    continue;
			}

			ClassReader reader = new ClassReader(inStream);
			DataCollector collector = new DataCollector();

			reader.accept(collector, 0);

			if (collector.hasClassData()) {
			    classInformations.add(collector.getClassData());
			}
		    } finally {
			if (inStream != null) {
			    inStream.close();
			}
		    }
		}
	    } catch (IOException e) {
		LOGGER.log(
			Level.SEVERE,
			"Failed to collect data about a class in "
				+ inJar.getName(), e);
		System.exit(Main.ExitCode.DataCollectingFailed.code());
	    }
	    
	    FileOutputStream fileOut = new FileOutputStream(cacheFile);
	    ObjectOutputStream out = new ObjectOutputStream(fileOut);
	    
	    out.writeObject(classInformations);
	    
	    out.flush();
	    out.close();
	    fileOut.close();
	}
	
	for (ClassData data : classInformations) {
	    this.dataManager.addData(data);
	}
    }
}
