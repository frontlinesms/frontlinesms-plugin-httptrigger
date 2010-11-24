package net.frontlinesms.plugins.httptrigger.httplistener;

import java.io.File;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import net.frontlinesms.FrontlineSMS;
import net.frontlinesms.FrontlineUtils;

/**
 * Class to Handle calls to the {@link GroovyScriptRunner} given a URI Request
 * 
 * @author Gonçalo Silva
 *
 */
public class GroovyUrlRequestHandler implements SimpleUrlRequestHandler {
	
//> INSTANCE PROPERTIES
	private final Logger log = FrontlineUtils.getLogger(this.getClass());
	private final FrontlineSMS frontlineController;
	private final UrlMapper urlMapper;
	private final ScriptFinder scriptFinder;
	
//> CONSTRUCTORS
	public GroovyUrlRequestHandler(FrontlineSMS frontlineController, UrlMapper urlMapper){
		this.frontlineController = frontlineController;
		this.urlMapper = urlMapper;
		this.scriptFinder = new ScriptFinder();
	}
	
// > ISTANCE METHODS
	/** 
	 * @see net.frontlinesms.plugins.httptrigger.httplistener.SimpleUrlRequestHandler#shouldHandle(java.lang.String)
	 */
	public boolean shouldHandle(String requestUri) {
		String scriptPath = urlMapper.mapToPath(requestUri);
		File script = scriptFinder.mapToFile(scriptPath);
		log.info("Checking for script at: " + script.getAbsolutePath());
		return script.isFile();
	}

	/**
	 * @see net.frontlinesms.plugins.httptrigger.httplistener.SimpleUrlRequestHandler#handle(String, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public ResponseType handle(String requestUri, HttpServletRequest request, HttpServletResponse response) {
		String scriptPath = urlMapper.mapToPath(requestUri);
		File groovyScript = scriptFinder.mapToFile(scriptPath);
		GroovyScriptRunner scriptRunner = new GroovyScriptRunner(groovyScript,
				new String[]{"boss", "request", "response"},
				new Object[]{frontlineController, request, response});
		return scriptRunner.run();
	}
	
//> ACCESSORS
}
