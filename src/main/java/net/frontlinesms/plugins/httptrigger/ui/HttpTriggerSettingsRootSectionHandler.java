package net.frontlinesms.plugins.httptrigger.ui;

import java.util.ArrayList;
import java.util.List;

import net.frontlinesms.events.AppPropertiesEventNotification;
import net.frontlinesms.plugins.httptrigger.HttpTriggerProperties;
import net.frontlinesms.settings.BaseSectionHandler;
import net.frontlinesms.settings.FrontlineValidationMessage;
import net.frontlinesms.ui.ThinletUiEventHandler;
import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.settings.UiSettingsSectionHandler;

public class HttpTriggerSettingsRootSectionHandler extends BaseSectionHandler implements UiSettingsSectionHandler, ThinletUiEventHandler {
	private static final String UI_SECTION_ROOT = "/ui/plugins/httptrigger/settings/pnRootSettings.xml";
	private static final String UI_COMPONENT_TF_PORT = "tfPort";
	private static final String UI_COMPONENT_CB_AUTO_START = "cbAutoStart";
	private static final String I18N_SETTINGS_MESSAGE_INVALID_PORT = "plugins.httptrigger.settings.invalid.port.number";
	
	private static final String SECTION_ITEM_PORT_NUMBER = "PLUGINS_HTTPTRIGGER_PORT_NUMBER";
	private static final String SECTION_ITEM_AUTO_START = "PLUGINS_HTTPTRIGGER_AUTO_START";
	private String pluginName;
	private String pluginIcon;

	public HttpTriggerSettingsRootSectionHandler (UiGeneratorController ui, String pluginName, String pluginIcon) {
		super(ui);
		this.pluginName = pluginName;
		this.pluginIcon = pluginIcon;
	}
	
	protected void init() {
		this.panel = ui.loadComponentFromFile(UI_SECTION_ROOT, this);
		
		HttpTriggerProperties httpTriggerProperties = HttpTriggerProperties.getInstance();
		String portNumber = Integer.toString(httpTriggerProperties.getListenPort());
		boolean isAutoStart = httpTriggerProperties.isAutostart();
		ui.setText(find(UI_COMPONENT_TF_PORT), portNumber);
		ui.setSelected(find(UI_COMPONENT_CB_AUTO_START), isAutoStart);
		
		this.originalValues.put(SECTION_ITEM_PORT_NUMBER, portNumber);
		this.originalValues.put(SECTION_ITEM_AUTO_START, isAutoStart);
	}
	
	public void deinit() {}
	
	/**
	 * Called when the "Auto start" checkbox has changed state
	 * @param autoStart
	 */
	public void autoStartChanged(boolean autoStart) {
		super.settingChanged(SECTION_ITEM_AUTO_START, autoStart);
	}
	
	/**
	 * Called when the port number has changed
	 * @param autoStart
	 */
	public void portNumberChanged(String portNumber) {
		super.settingChanged(SECTION_ITEM_PORT_NUMBER, portNumber);
	}

	public void save() {
		// Save the port number
		HttpTriggerProperties properties = HttpTriggerProperties.getInstance();
		properties.setListenPort(Integer.parseInt(ui.getText(find(UI_COMPONENT_TF_PORT))));
		properties.setAutostart(ui.isSelected(find(UI_COMPONENT_CB_AUTO_START)));
		properties.saveToDisk();
		
		this.eventBus.notifyObservers(new AppPropertiesEventNotification(HttpTriggerProperties.class, HttpTriggerProperties.PROP_LISTEN_PORT));
		this.eventBus.notifyObservers(new AppPropertiesEventNotification(HttpTriggerProperties.class, HttpTriggerProperties.PROP_AUTOSTART));
	}

	public List<FrontlineValidationMessage> validateFields() {
		List<FrontlineValidationMessage> validationMessages = new ArrayList<FrontlineValidationMessage>();
		
		try {
			if (Integer.parseInt(ui.getText(find(UI_COMPONENT_TF_PORT))) <= 0) {
				validationMessages.add(new FrontlineValidationMessage(I18N_SETTINGS_MESSAGE_INVALID_PORT, null, this.pluginIcon));
			}
		} catch (NumberFormatException e) {
			validationMessages.add(new FrontlineValidationMessage(I18N_SETTINGS_MESSAGE_INVALID_PORT, null, this.pluginIcon));
		}
		
		return validationMessages;
	}

	public String getTitle() {
		return this.pluginName;
	}

	public Object getSectionNode() {
		return createSectionNode(getTitle(), this, this.pluginIcon);
	}
}
