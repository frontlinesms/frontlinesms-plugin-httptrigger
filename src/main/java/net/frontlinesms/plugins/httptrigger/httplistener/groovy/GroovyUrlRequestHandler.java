package net.frontlinesms.plugins.httptrigger.httplistener.groovy;

import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import net.frontlinesms.FrontlineSMS;
import net.frontlinesms.plugins.httptrigger.HttpTriggerEventListener;
import net.frontlinesms.plugins.httptrigger.httplistener.AbstractSimpleUrlRequestHandler;
import net.frontlinesms.plugins.httptrigger.httplistener.ResponseType;
import net.frontlinesms.plugins.httptrigger.httplistener.SimpleUrlRequestHandler;
import net.frontlinesms.ui.i18n.InternationalisationUtils;

/**
 * Class to Handle calls to the {@link GroovyScriptRunner} given a URI Request
 * 
 * @author Gonçalo Silva
 *
 */
public class GroovyUrlRequestHandler implements SimpleUrlRequestHandler {
	
	private static final String I18N_SCRIPT_URL_MAPPED = "plugins.httptrigger.script.url.mapped";
	private static final String I18N_SCRIPT_NOT_FOUND = "plugins.httptrigger.script.not.found";
	private static final String I18N_SCRIPT_EXECUTION_COMPLETE = "plugins.httptrigger.script.execution.complete";
	
	//> INSTANCE PROPERTIES
	private final HttpTriggerEventListener listener;
	private final FrontlineSMS frontlineController;
	private final UrlMapper urlMapper;
	private final ScriptFinder scriptFinder;
	
//> CONSTRUCTORS
	public GroovyUrlRequestHandler(HttpTriggerEventListener listener, FrontlineSMS frontlineController, UrlMapper urlMapper){
		this.listener = listener;
		this.frontlineController = frontlineController;
		this.urlMapper = urlMapper;
		this.scriptFinder = new ScriptFinder();
	}
	
// > ISTANCE METHODS
	/** 
	 * @see net.frontlinesms.plugins.httptrigger.httplistener.SimpleUrlRequestHandler#shouldHandle(java.lang.String)
	 */
	public boolean shouldHandle(String requestUri) {
		this.listener.log(InternationalisationUtils.getI18nString(AbstractSimpleUrlRequestHandler.I18N_PROCESSING_REQUEST, requestUri.toString()));
		return true;
	}

	/**
	 * @see net.frontlinesms.plugins.httptrigger.httplistener.SimpleUrlRequestHandler#handle(String, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public ResponseType handle(String requestUri, HttpServletRequest request, HttpServletResponse response) {
		String scriptPath = urlMapper.mapToPath(requestUri);
		File groovyScript = scriptFinder.mapToFile(scriptPath);
		
		if(!groovyScript.isFile()) {
			listener.log(InternationalisationUtils.getI18nString(I18N_SCRIPT_NOT_FOUND, groovyScript.getAbsolutePath()));
			return ResponseType.FAILURE;
		} else {
			listener.log(InternationalisationUtils.getI18nString(I18N_SCRIPT_URL_MAPPED, groovyScript.getAbsolutePath()));
		
			GroovyScriptRunner scriptRunner = new GroovyScriptRunner(groovyScript,
					new String[]{"boss", "request", "response", "log", "out"},
					new Object[]{frontlineController, request, response, listener, getPrinter(response)});
			ResponseType run = scriptRunner.run();
			listener.log(InternationalisationUtils.getI18nString(I18N_SCRIPT_EXECUTION_COMPLETE));;
			return run;
		}
	}

//> ACCESSORS
	private Object getPrinter(HttpServletResponse response) {
		Object out = System.out;
		try {
			out = response.getWriter();
		} catch(IOException ex) { /* damnit */ }
		return out;
	}
}