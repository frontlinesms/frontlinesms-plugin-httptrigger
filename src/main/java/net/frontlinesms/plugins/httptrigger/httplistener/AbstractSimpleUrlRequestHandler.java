/**
 * 
 */
package net.frontlinesms.plugins.httptrigger.httplistener;

/**
 * @author Alex
 */
public abstract class AbstractSimpleUrlRequestHandler implements SimpleUrlRequestHandler {
//> STATIC CONSTANTS

//> INSTANCE PROPERTIES
	/** The start of URIs that this {@link SimpleUrlRequestHandler} should handle. */
	private final String requestStart;

//> CONSTRUCTORS
	/** @param requestStart value for {@link #requestStart} */
	public AbstractSimpleUrlRequestHandler(String requestStart) {
		this.requestStart = requestStart;
	}

//> ACCESSORS

//> INSTANCE METHODS
	/** @see net.frontlinesms.plugins.httptrigger.httplistener.SimpleUrlRequestHandler#shouldHandle(java.lang.String) */
	public boolean shouldHandle(String requestUri) {
		return requestUri.startsWith(this.requestStart);
	}
	
	/**
	 * Handle a request for a given URI.  This method splits the URI using {@link #getRequestParts(String)}, and passes
	 * these parts to {@link #handle(String[])}.
	 * @see SimpleUrlRequestHandler#handle(String)
	 */
	public boolean handle(String requestUri) {
		assert shouldHandle(requestUri) : "This URI should not be handled here.";
		return this.handle(this.getRequestParts(requestUri));
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
