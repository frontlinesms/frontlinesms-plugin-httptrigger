/**
 * 
 */
package net.frontlinesms.plugins.httptrigger.httplistener;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author aga
 */
public class RequestIgnoreHandler implements SimpleUrlRequestHandler {
	private final String[] ignoreList;

	public RequestIgnoreHandler(String... ignoreList) {
		this.ignoreList = ignoreList;
	}
	public ResponseType handle(String requestUri, HttpServletRequest request, HttpServletResponse response) {
		response.setStatus(HttpServletResponse.SC_FORBIDDEN);
		return ResponseType.HANDLED;
	}

	public boolean shouldHandle(String requestUri) {
		for (String ignoredRequest : ignoreList) {
			if (requestUri.matches(ignoredRequest)) {
				return true;
			}
		}
		return false;
	}
}
