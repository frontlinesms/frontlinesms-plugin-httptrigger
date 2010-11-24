package net.frontlinesms.plugins.httptrigger.httplistener;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;

import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import net.frontlinesms.ErrorUtils;
import net.frontlinesms.FrontlineSMS;
import net.frontlinesms.FrontlineUtils;

/**
 * Class which will handle the script calls to the Groovy shell
 * 
 * @author Gonçalo Silva
 *
 */
public class GroovyScriptRunner {
	
//> INSTANCE PROPERTIES
	private final Logger log = FrontlineUtils.getLogger(this.getClass());
	private final File groovyScript;
	private final String[] bindingNames;
	private final Object[] boundObjects;
	
//> CONSTRUCTORS
	/**
	 * @param The Groovy script file
	 * @param A controller for {@link FrontlineSMS} 
	 */
	public GroovyScriptRunner(File mapToFile, String[] bindingNames, Object[] boundObjects) {
		assert(bindingNames.length == boundObjects.length);
		this.groovyScript = mapToFile;
		this.bindingNames = bindingNames;
		this.boundObjects = boundObjects;
	}

//> ISTANCE METHODS	
	public ResponseType run() {
		Binding binding = getBinding();
		GroovyShell shell = new GroovyShell(binding);
		try {
			Object returnedObject = shell.evaluate(groovyScript);
			
			log.trace("returnedObject: " + 
					(returnedObject == null ? null : returnedObject.toString()));
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return ResponseType.HANDLED;
	}
	
	private void handleThrowable(Throwable t) {
		log.trace("Groovy Script " + groovyScript.getName() + " failed to run", t);
		HttpServletResponse response = (HttpServletResponse) getBinding().getVariable("response");
		response.setStatus(500);
		try {
			response.getWriter().write("Error processing script '" + this.groovyScript.getAbsolutePath() + "':\r\n" + ErrorUtils.getStackTraceAsString(t));
		} catch (IOException ex) {
			log.error("Could not output script error due to exception.", ex);
		}
	}

	private Binding getBinding() {
		Binding binding = new Binding();
		for (int i = 0; i < bindingNames.length; i++) {
			binding.setVariable(bindingNames[i], boundObjects[i]);
		}
		return binding;
	}
	
	private String getThreadName() {
		return GroovyScriptRunner.class.getSimpleName() + "::'" + groovyScript.getAbsolutePath() + "'";
	}
}
