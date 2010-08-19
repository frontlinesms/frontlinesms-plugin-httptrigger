/**
 * 
 */
package net.frontlinesms.plugins.httptrigger;

import net.frontlinesms.resources.UserHomeFilePropertySet;

/**
 * Persistent properties for the HttpTrigger plugin.
 * @author Alex Anderson <alex@frontlinesms.com>
 */
public class HttpTriggerProperties extends UserHomeFilePropertySet {
	
//> STATIC CONSTANTS
	private static final int DEFAULT_LISTEN_PORT = 8011;

//> PROPERTY KEYS
	private static final String PROP_AUTOSTART = "listener.autostart";
	private static final String PROP_LISTEN_PORT = "listener.port";
	
	/** Singleton instance of this class */
	private static HttpTriggerProperties INSTANCE;
	
	private HttpTriggerProperties() {
		super("plugins.httptrigger");
	}

	int getListenPort() {
		return super.getPropertyAsInt(PROP_LISTEN_PORT, DEFAULT_LISTEN_PORT);
	}
	
	void setListenPort(int port) {
		super.setPropertyAsInteger(PROP_LISTEN_PORT, port);
	}
	
	boolean isAutostart() {
		return super.getPropertyAsBoolean(PROP_AUTOSTART, false);
	}
	
	/** @param value new value for #PROP_AU */
	void setAutostart(boolean value) {
		super.setPropertyAsBoolean(PROP_AUTOSTART, value);
	}

//> STATIC FACTORIES
	/**
	 * Lazy getter for {@link #instance}
	 * @return The singleton instance of this class
	 */
	public static synchronized HttpTriggerProperties getInstance() {
		if(INSTANCE == null) {
			INSTANCE = new HttpTriggerProperties();
		}
		return INSTANCE;
	}
}
