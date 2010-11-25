/**
 * 
 */
package net.frontlinesms.plugins.httptrigger.httplistener;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.frontlinesms.plugins.httptrigger.HttpTriggerEventListener;
import net.frontlinesms.ui.i18n.InternationalisationUtils;

/**
 * @author Alex
 */
public abstract class AbstractSimpleUrlRequestHandler implements SimpleUrlRequestHandler {
//> STATIC CONSTANTS

//> INSTANCE PROPERTIES
	/** The start of URIs that this {@link SimpleUrlRequestHandler} should handle. */
	private final String requestStart;
	private HttpTriggerEventListener eventListener;
	
	public static final String I18N_PROCESSING_REQUEST = "plugins.httptrigger.processing.request";

//> CONSTRUCTORS
	/** @param requestStart value for {@link #requestStart} */
	public AbstractSimpleUrlRequestHandler(String requestStart, HttpTriggerEventListener eventListener) {
		this.requestStart = requestStart;
		this.eventListener = eventListener;
	}

//> ACCESSORS

//> INSTANCE METHODS
	/** @see net.frontlinesms.plugins.httptrigger.httplistener.SimpleUrlRequestHandler#shouldHandle(java.lang.String) */
	public boolean shouldHandle(String requestUri) {
		if (requestUri.startsWith(this.requestStart)) {
			this.eventListener.log(InternationalisationUtils.getI18NString(I18N_PROCESSING_REQUEST, requestUri.toString()));
			return true;
		}
		
		return false;
	}
	
	/**
	 * Handle a request for a given URI.  This method splits the URI using {@link #getRequestParts(String)}, and passes
	 * these parts to {@link #handle(String[])}.
	 * @see SimpleUrlRequestHandler#handle(String, HttpServletRequest, HttpServletResponse)
	 */
	public ResponseType handle(String requestUri, HttpServletRequest request, HttpServletResponse response) {
		assert shouldHandle(requestUri) : "This URI should not be handled here.";
		boolean success = this.handle(this.getRequestParts(requestUri));
		return success ? ResponseType.SUCCESS : ResponseType.FAILURE;
	}
	
	/**
	 * Perform processing of a request.
	 * @param requestParts The parts of the request URI, separated using {@link #getRequestParts(String)}.
	 * @return <code>true</code> if the request was processed without problems, <code>false</code> otherwise
	 * @see SimpleUrlRequestHandler#handle(String)
	 */
	public abstract boolean handle(String[] requestParts);
	
	/**
	 * Splits a request into it's separate "directories", removing {@link #requestStart}.
	 * @param requestUri
	 * @return A string array containing the supplied URI split around / characters
	 */
	private String[] getRequestParts(String requestUri) {
		return requestUri.substring(this.requestStart.length()).split("\\/");
	}

//> STATIC FACTORIES

//> STATIC HELPER METHODS
}
