package net.frontlinesms.plugins.httptrigger;

import net.frontlinesms.plugins.PluginSettingsController;
import net.frontlinesms.plugins.httptrigger.ui.HttpTriggerSettingsAppearanceSectionHandler;
import net.frontlinesms.plugins.httptrigger.ui.HttpTriggerSettingsRootSectionHandler;
import net.frontlinesms.settings.FrontlineValidationMessage;
import net.frontlinesms.ui.ThinletUiEventHandler;
import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.UiSettingsSectionHandler;
import net.frontlinesms.ui.i18n.InternationalisationUtils;

public class HttpTriggerSettingsController implements ThinletUiEventHandler, PluginSettingsController {
	private static final Object SECTION_APPEARANCE = "APPEARANCE";

	private UiGeneratorController uiController;
	private HttpTriggerPluginController pluginController;

	public HttpTriggerSettingsController(HttpTriggerPluginController pluginController, UiGeneratorController uiController) {
		this.pluginController = pluginController;
		this.uiController = uiController;
	}

	public String getTitle() {
		return this.pluginController.getName(InternationalisationUtils.getCurrentLocale());
	}
	
	public void addSubSettingsNodes(Object rootSettingsNode) {
		this.uiController.add(rootSettingsNode, this.uiController.createNode("Appearance", SECTION_APPEARANCE));
		this.uiController.add(rootSettingsNode, this.uiController.createNode("Test", "TEST"));
	}
	
	public FrontlineValidationMessage validateFields() {
		return null;
	}
	
	enum HttpTriggerSettingsSections {
		APPEARANCE,
		TEST
	}

	public UiSettingsSectionHandler getHandlerForSection(String section) {
		switch (HttpTriggerSettingsSections.valueOf(section)) {
			case APPEARANCE:
				return new HttpTriggerSettingsAppearanceSectionHandler(this.uiController);
			default:
				return null;
		}
	}

	public UiSettingsSectionHandler getRootPanelHandler() {
		return new HttpTriggerSettingsRootSectionHandler(this.uiController);
	}

}
