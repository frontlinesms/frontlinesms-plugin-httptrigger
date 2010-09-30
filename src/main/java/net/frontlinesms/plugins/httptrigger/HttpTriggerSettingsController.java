package net.frontlinesms.plugins.httptrigger;

import net.frontlinesms.plugins.PluginSettingsController;
import net.frontlinesms.ui.ThinletUiEventHandler;
import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.i18n.InternationalisationUtils;

public class HttpTriggerSettingsController implements ThinletUiEventHandler, PluginSettingsController {
	private static final String UI_SECTION_APPEARANCE = "/ui/plugins/httptrigger/settings/pnAppearance.xml";

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
	
	public Object getPanelForSection(String section) {
		switch (HttpTriggerSettingsSections.valueOf(section)) {
			case APPEARANCE:
				return getAppearancePanel();
			case TEST:
				return getTestPanel();
			default:
				return null;
		}
	}

	public Object getRootPanel() {
		return null;
	}


	private Object getTestPanel() {
		// TODO Auto-generated method stub
		return null;
	}
	
	private Object getAppearancePanel() {
		return uiController.loadComponentFromFile(UI_SECTION_APPEARANCE, this);
	}
	
	enum HttpTriggerSettingsSections {
		APPEARANCE,
		TEST
	}

}
