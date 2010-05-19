/**
 * 
 */
package net.frontlinesms.plugins.httptrigger;

import net.frontlinesms.plugins.BasePluginThinletTabController;
import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.i18n.InternationalisationUtils;

/**
 * @author Alex
 */
public class HttpTriggerThinletTabController extends BasePluginThinletTabController<HttpTriggerPluginController> {

//> STATIC CONSTANTS

private static String I18N_INVALID_PORT_NUMBER = "plugins.httptrigger.invalid.port.number";

	//> CONSTRUCTORS
	/**
	 * @param httpTriggerController value for {@link #httpTriggerController}
	 * @param ui value for {@link #ui}
	 */
	public HttpTriggerThinletTabController(HttpTriggerPluginController httpTriggerController, UiGeneratorController ui) {
		super(httpTriggerController, ui);
	}
	
//> PUBLIC UI METHODS
	/**
	 * The "start" button has been clicked on the UI.  Try to get the details of the listener
	 * setup, and start it on the suggested port.
	 * @param portNumberAsString The port number to connect to, as a String.  This will be ignored if {@link Integer#parseInt(String)} fails on it.
	 */
	public void startListener() {
		String portNumberAsString = ui.getText(getPortTextfield());
		
		int portNumber;
		try {
			portNumber = Integer.parseInt(portNumberAsString.trim());
		} catch(NumberFormatException ex) {
			// Port number failed to parse.  Warn the user and do not change the state of the listener 
			this.ui.alert(InternationalisationUtils.getI18NString(I18N_INVALID_PORT_NUMBER));
			return;
		}

		// Stop the old listener, if one is running
		this.getPluginController().stopListener();
		
		// Start the new listener
		this.getPluginController().startListener(portNumber);
		
		// Enable and disable relevant fields
		enableFields(true);
	}
	
	/**
	 * Tells the {@link #httpTriggerController} to stop the HTTP listener, and changes the
	 * available UI components as is suitable. 
	 */
	public void stopListener() {
		// Stop the listener
		this.getPluginController().stopListener();

		// Enable and disable relevant fields
		enableFields(false);
	}
	
	/**
	 * Enable and disable UI components as appropriate for whether the listener is running or is stopped.
	 * @param running <code>true</code> if the listener has running; <code>false</code> otherwise.
	 */
	private void enableFields(boolean running) {
		ui.setEnabled(getStartButton(), !running);
		ui.setEnabled(getPortTextfield(), !running);
		ui.setEnabled(getStopButton(), running);
		
	}
	
//> ACCESSORS
	/** @return Thinlet button for starting the listener */
	private Object getStartButton() {
		return find("btStart");
	}
	/** @return Thinlet button for stopping */
	private Object getStopButton() {
		return find("btStop");
	}
	/** @return Thinlet textfield for setting the port */
	private Object getPortTextfield() {
		return find("tfPort");
	}
	/** @return Thinlet list containing log entries */
	private Object getLogList() {
		return find("lsHttpTriggerLog");
	}
	
//> EVENT LISTENER METHODS
	/**
	 * Adds a new item to the log list.  The item is added at the top of the list. 
	 * @param message Message to log
	 * @see HttpTriggerEventListener#log(String)
	 */
	public void log(String message) {
		// TODO add timestamp to this list item
		ui.add(getLogList(), ui.createListItem(message, null), 0);
	}

//> INSTANCE HELPER METHODS

//> STATIC FACTORIES

//> STATIC HELPER METHODS
}
