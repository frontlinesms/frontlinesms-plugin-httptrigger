/**
 * 
 */
package net.frontlinesms.plugins.httptrigger.httplistener;

import java.io.IOException;
import java.util.LinkedList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.frontlinesms.Utils;
import net.frontlinesms.plugins.httptrigger.HttpTriggerEventListener;

import org.mortbay.jetty.HttpConnection;
import org.mortbay.jetty.HttpURI;
import org.mortbay.jetty.Request;
import org.mortbay.jetty.handler.AbstractHandler;

/**
 * Handler for simple queries for FrontlineSMS to send messages.
 * @author Alex
 */
class SimpleFrontlineSmsHttpHandler extends AbstractHandler {
	
//> STATIC CONSTANTS

//> INSTANCE PROPERTIES
	/** The listener for events triggered on the HTTP listener. */
	private final HttpTriggerEventListener eventListener;
	/** Handlers for different URL mappings. */
	private final LinkedList<SimpleUrlRequestHandler> handlers = new LinkedList<SimpleUrlRequestHandler>();

//> CONSTRUCTORS
	/**
	 * Create a new {@link SimpleFrontlineSmsHttpHandler}.
	 * @param eventListener value for {@link #eventListener}.
	 */
	SimpleFrontlineSmsHttpHandler(HttpTriggerEventListener eventListener) {
		this.eventListener = eventListener;
		
		// Add handler for TEST command - just check if the server is running
		this.handlers.add(new AbstractSimpleUrlRequestHandler("test/") {
			@Override
			public boolean handle(String[] requestParts) {
				// URL triggered this handler successfully, so log it and return true
				SimpleFrontlineSmsHttpHandler.this.eventListener.log("The test trigger has been tripped."); // FIXME i18n
				return true;
			}
		});
		
		// Add handler for SEND SMS commands
		this.handlers.add(new AbstractSimpleUrlRequestHandler("send/sms/") {
			public boolean handle(String[] requestParts) {
				// N.B. the request parts will ignore message parts if there are non-URL-encoded / characters in the message.  This is intentional.
				if(requestParts.length < 2) {
					SimpleFrontlineSmsHttpHandler.this.eventListener.log("Not enough params in SEND SMS request."); // FIXME i18n
					return false;
				}
				String toPhoneNumber = requestParts[0];
				String message = requestParts[1];
				
				SimpleFrontlineSmsHttpHandler.this.eventListener.sendSms(Utils.urlDecode(toPhoneNumber), Utils.urlDecode(message));
				
				return true;
			}
		});
	}

//> ACCESSORS

//> INSTANCE METHODS
	/** @see org.mortbay.jetty.Handler#handle(java.lang.String, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, int) */
	public void handle(String target, HttpServletRequest request, HttpServletResponse response, int dispatch) throws IOException, ServletException {
		Request baseRequest = (request instanceof Request) ? (Request) request : HttpConnection.getCurrentConnection().getRequest();
		baseRequest.setHandled(true);
		
		response.setContentType("text/html");
		
		boolean success = processRequestFromUrl(baseRequest.getUri());

		final int httpStatusCode;
		final String responseContent;
		if(success) {
			httpStatusCode = HttpServletResponse.SC_OK;
			responseContent = "OK";
		} else {
			httpStatusCode = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
			responseContent = "ERROR";
		}
		response.setStatus(httpStatusCode);
		response.getWriter().println(responseContent);
	}
	
//> INSTANCE HELPER METHODS
	/**
	 * Process request from the URL.
	 * @param requestUri 
	 * @return <code>true</code> if the event was processed successfully; <code>false</code> if there was no processor available, or processing failed. 
	 */
	private boolean processRequestFromUrl(final HttpURI requestUri) {
		eventListener.log("Processing request: " + requestUri);

		// Get this URI string, stripping leading '/' character
		String requestUriString = requestUri.toString().substring(1);
		
		for(SimpleUrlRequestHandler handler : this.handlers) {
			if(handler.shouldHandle(requestUriString)) {
				return handler.handle(requestUriString);
			}
		}
		
		this.eventListener.log("No handler found for request: " + requestUriString); // FIXME i18n
		return false;
	}

//> STATIC FACTORIES

//> STATIC HELPER METHODS
}