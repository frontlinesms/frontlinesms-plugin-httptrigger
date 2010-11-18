package net.frontlinesms.plugins.httptrigger.httplistener;

import java.io.File;

import net.frontlinesms.FrontlineSMS;

/**
 * Class to Handle calls to the {@link GroovyScriptCaller} given a URI Request
 * 
 * @author Gonçalo Silva
 *
 */
public class GroovyUrlRequestHandler implements SimpleUrlRequestHandler {
	
//> INSTANCE PROPERTIES
	private FrontlineSMS frontlineController;
	private UrlMapper urlMapper;
	
//> CONSTRUCTORS
	public GroovyUrlRequestHandler(FrontlineSMS frontlineController, UrlMapper urlMapper){
		this.frontlineController = frontlineController;
		this.urlMapper = urlMapper;
	}
	
// > ISTANCE METHODS
	/** 
	 * @see net.frontlinesms.plugins.httptrigger.httplistener.SimpleUrlRequestHandler#shouldHandle(java.lang.String)
	 */
	public boolean shouldHandle(String requestUri) {
		String scriptPath = urlMapper.mapToPath(requestUri);
		return new ScriptFinder().mapToFile(scriptPath).isFile();
	}

	/**
	 * @see net.frontlinesms.plugins.httptrigger.httplistener.SimpleUrlRequestHandler#handle(java.lang.String)
	 */
	public boolean handle(String requestUri) {
		String scriptPath = urlMapper.mapToPath(requestUri);
		File groovyScript = new ScriptFinder().mapToFile(scriptPath);
		new GroovyScriptCaller(groovyScript, frontlineController).start();
		return true;
	}
	
//> ACCESSORS
	/**
	 * @param The {@link UrlMapper} to be used by this Request Handler
	 */
	public void setUrlMapper(UrlMapper urlMapper) {
		this.urlMapper = urlMapper;
	}
	
	/**
	 * @param see {@link FrontlineSMS}
	 */
	public void setFrontlineController(FrontlineSMS frontlineController) {
		this.frontlineController = frontlineController;
	}
}
