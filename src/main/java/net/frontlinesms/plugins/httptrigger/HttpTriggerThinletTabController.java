/**
 * 
 */
package net.frontlinesms.plugins.httptrigger;

import net.frontlinesms.events.AppPropertiesEventNotification;
import net.frontlinesms.events.EventBus;
import net.frontlinesms.events.EventObserver;
import net.frontlinesms.events.FrontlineEventNotification;
import net.frontlinesms.plugins.BasePluginThinletTabController;
import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.i18n.InternationalisationUtils;

/**
 * @author Alex
 */
public class HttpTriggerThinletTabController extends BasePluginThinletTabController<HttpTriggerPluginController> implements EventObserver {

//> STATIC CONSTANTS

private static String I18N_INVALID_PORT_NUMBER = "plugins.httptrigger.invalid.port.number";
private EventBus eventBus;

//> CONSTRUCTORS
	/**
	 * @param httpTriggerController value for {@link #httpTriggerController}
	 * @param ui value for {@link #ui}
	 */
	public HttpTriggerThinletTabController(HttpTriggerPluginController httpTriggerController, UiGeneratorController ui) {
		super(httpTriggerController, ui);
		
		this.eventBus = ui.getFrontlineController().getEventBus();
		this.eventBus.registerObserver(this);
	}

	public void initFields() {
		// Set initial value for fields
		HttpTriggerProperties httpTriggerProperties = HttpTriggerProperties.getInstance();
		ui.setText(getPortTextfield(), Integer.toString(httpTriggerProperties.getListenPort()));
		ui.setSelected(getAutostartCheckbox(), httpTriggerProperties.isAutostart());
	}
	
//> PUBLIC UI METHODS
	/**
	 * The "start" button has been clicked on the UI.  Try to get the details of the listener
	 * setup, and start it on the suggested port.
	 * @param portNumberAsString The port number to connect to, as a String.  This will be ignored if {@link Integer#parseInt(String)} fails on it.
	 */
	public void startListener() {
		if(updateSettings()) {
			// Stop the old listener, if one is running
			this.getPluginController().stopListener();
			
			// Start the new listener
			this.getPluginController().startListener();
			
			// Enable and disable relevant fields
			enableFields(true);
		}
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
		
		ui.setText(getPortTextfield(), Integer.toString(HttpTriggerProperties.getInstance().getListenPort()));
	}
	
	/** Updates the setting for autostart of the listener */
	public void setAutostart(boolean value) {
		// We save the properties here AND THEN call updateSettings() so that
		// this checkbox is not left in an inconsistent state.  We still want to
		// call updateSettings() in case the port number has been changed.
		HttpTriggerProperties properties = HttpTriggerProperties.getInstance();
		properties.setAutostart(value);
		properties.saveToDisk();
		
		updateSettings();
	}
	
	/**
	 * Enable and disable UI components as appropriate for whether the listener is running or is stopped.
	 * @param running <code>true</code> if the listener has running; <code>false</code> otherwise.
	 */
	public void enableFields(boolean running) {
		ui.setEnabled(getStartButton(), !running);
		ui.setEnabled(getPortTextfield(), !running);
		ui.setEnabled(getStopButton(), running);
		
	}
	
	/**
	 * If this method returns false, it has probably displayed a popup, so you should stop
	 * doing what you're doing!
	 * @return <code>true</code> if the settings were successfully updated; <code>false</code>
	 * if there was a problem
	 */
	private boolean updateSettings() {
		String portNumberAsString = ui.getText(getPortTextfield());
		
		int portNumber;
		try {
			portNumber = Integer.parseInt(portNumberAsString.trim());
		} catch(NumberFormatException ex) {
			// Port number failed to parse.  Warn the user and do not change the state of the listener 
			this.ui.alert(InternationalisationUtils.getI18NString(I18N_INVALID_PORT_NUMBER));
			return false;
		}
		
		// Save the port number
		HttpTriggerProperties properties = HttpTriggerProperties.getInstance();
		properties.setListenPort(portNumber);
		properties.setAutostart(ui.isSelected(getAutostartCheckbox()));
		properties.saveToDisk();
		
		return true;
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
	/** @return Thinlet checkbox indicating autostart status */
	private Object getAutostartCheckbox() {
		return find("cbAutostart");
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

	public void notify(FrontlineEventNotification notification) {
		if (notification instanceof AppPropertiesEventNotification) {
			// An AppProperty has been changed
			AppPropertiesEventNotification appPropertiesNotification = (AppPropertiesEventNotification) notification;
			
			// If the changes concern the Http Trigger properties
			if (appPropertiesNotification.getAppClass().equals(HttpTriggerProperties.class)) {
				if (appPropertiesNotification.getProperty().equals(HttpTriggerProperties.PROP_AUTOSTART)) {
					ui.setSelected(getAutostartCheckbox(), HttpTriggerProperties.getInstance().isAutostart());
				} else if (appPropertiesNotification.getProperty().equals(HttpTriggerProperties.PROP_LISTEN_PORT)
						&& !this.getPluginController().isRunning()) {
					// We update the port if the server is not currently running
					// Otherwise, it will be done when it stops
					ui.setText(getPortTextfield(), Integer.toString(HttpTriggerProperties.getInstance().getListenPort()));
				}
			}
		}
	}

//> INSTANCE HELPER METHODS

//> STATIC FACTORIES

//> STATIC HELPER METHODS
}
