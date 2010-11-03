package net.frontlinesms.plugins.httptrigger.httplistener;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;

import java.io.File;

import org.apache.log4j.Logger;

import net.frontlinesms.FrontlineSMS;
import net.frontlinesms.FrontlineUtils;

/**
 * Class which will handle the script calls to the Groovy shell
 * 
 * @author Gonçalo Silva
 *
 */
public class GroovyScriptCaller extends Thread {
	
//> INSTANCE PROPERTIES
	private File groovyScript;
	private FrontlineSMS frontlineController;
	private final Logger log = FrontlineUtils.getLogger(this.getClass());
	
//> CONSTRUCTORS
	/**
	 * @param The Groovy script file
	 * @param A controller for {@link FrontlineSMS} 
	 */
	public GroovyScriptCaller(File mapToFile, FrontlineSMS frontlineController) {
		super(GroovyScriptCaller.class.getSimpleName() + "'" + mapToFile.getAbsolutePath() + "'");
		this.groovyScript = mapToFile;
		this.frontlineController = frontlineController;
	}

//> ISTANCE METHODS	
	/**
	 * 	Invoke the Groovy script
	 */
	public void run() {
		Binding binding = new Binding();
		binding.setVariable("boss", frontlineController);
		GroovyShell shell = new GroovyShell(binding);
		try {
			Object returnedObject = shell.evaluate(groovyScript);
			log.trace(returnedObject.toString());
		} catch (Throwable t) {
			log.trace("Groovy Script " + groovyScript.getName() + " failed to run", t);
		}
	}
}
