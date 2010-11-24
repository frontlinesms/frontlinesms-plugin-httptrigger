/**
 * 
 */
package net.frontlinesms.plugins.httptrigger.httplistener;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Alex
 *
 */
public interface SimpleUrlRequestHandler {
	/**
	 * Check if this handler should handle the supplied URI.
	 * @param requestUri The request URI, without leading / character
	 * @return <code>true</code> if this should process the supplied URI, <code>false</code> otherwise
	 */
	public boolean shouldHandle(String requestUri);
	
	/**
	 * Process the supplied URI.
	 * @param requestUri
	 * @param response 
	 * @param request 
	 * @return {@link ResponseType#SUCCESS} if the request was processed successfully,
	 * 	{@link ResponseType#FAILURE} if there was a problem,
	 * 	or {@link ResponseType#HANDLED} if the response was handled internally 
	 */
	public ResponseType handle(String requestUri, HttpServletRequest request, HttpServletResponse response);
}
