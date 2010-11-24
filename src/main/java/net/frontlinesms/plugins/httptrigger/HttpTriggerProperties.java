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
	public static final String PROP_AUTOSTART = "listener.autostart"; // FIXME this should be private
	public static final String PROP_LISTEN_PORT = "listener.port"; // FIXME this should be private
	private static final String PROP_SCRIPT_PATHS = "script.path";
	
	/** Singleton instance of this class */
	private static HttpTriggerProperties INSTANCE;
	
	private HttpTriggerProperties() {
		super("plugins.httptrigger");
	}

	public int getListenPort() {
		return super.getPropertyAsInt(PROP_LISTEN_PORT, DEFAULT_LISTEN_PORT);
	}
	
	public void setListenPort(int port) {
		super.setPropertyAsInteger(PROP_LISTEN_PORT, port);
	}
	
	public boolean isAutostart() {
		return super.getPropertyAsBoolean(PROP_AUTOSTART, false);
	}
	
	/** @param value new value for #PROP_AU */
	public void setAutostart(boolean value) {
		super.setPropertyAsBoolean(PROP_AUTOSTART, value);
	}

	public String[] getScriptPaths() {
		return super.getPropertyValues(PROP_SCRIPT_PATHS);
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
