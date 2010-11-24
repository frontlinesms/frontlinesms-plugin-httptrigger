/**
 * 
 */
package net.frontlinesms.plugins.httptrigger;

/**
 * @author Alex
 */
public interface HttpTriggerListener {
	/** Non-blocking call to request that the listener stops. */
	void pleaseStop();

	/** Non-blocking call to make the listener start. */
	void start();
}
