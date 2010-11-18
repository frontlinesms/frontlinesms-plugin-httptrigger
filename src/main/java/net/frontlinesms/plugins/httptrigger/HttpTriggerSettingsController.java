package net.frontlinesms.plugins.httptrigger;

import net.frontlinesms.plugins.PluginSettingsController;
import net.frontlinesms.plugins.httptrigger.ui.HttpTriggerSettingsRootSectionHandler;
import net.frontlinesms.settings.FrontlineValidationMessage;
import net.frontlinesms.ui.ThinletUiEventHandler;
import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.i18n.InternationalisationUtils;
import net.frontlinesms.ui.settings.UiSettingsSectionHandler;

public class HttpTriggerSettingsController implements ThinletUiEventHandler, PluginSettingsController {
	private UiGeneratorController uiController;
	private HttpTriggerPluginController pluginController;
	private String pluginIcon;

	public HttpTriggerSettingsController(HttpTriggerPluginController pluginController, UiGeneratorController uiController, String pluginIcon) {
		this.pluginController = pluginController;
		this.uiController = uiController;
		this.pluginIcon = pluginIcon;
	}

	public String getTitle() {
		return this.pluginController.getName(InternationalisationUtils.getCurrentLocale());
	}
	
	public void addSubSettingsNodes(Object rootSettingsNode) {
	}
	
	public FrontlineValidationMessage validateFields() {
		return null;
	}
	
	public UiSettingsSectionHandler getHandlerForSection(String section) {
			return null;
	}

	public UiSettingsSectionHandler getRootPanelHandler() {
		return new HttpTriggerSettingsRootSectionHandler(this.uiController, this.getTitle());
	}

	public Object getRootNode() {
		HttpTriggerSettingsRootSectionHandler rootHandler = new HttpTriggerSettingsRootSectionHandler(this.uiController, this.getTitle());
		return rootHandler.getSectionNode(pluginIcon);
	}

}
