package nc.config;

import java.util.List;

import nc.util.Lang;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;

public interface IConfigCategory {
	
	public default GuiScreen buildChildScreen(String categoryName, GuiConfig owningScreen, IConfigElement configElement) {
		Configuration config = NCConfig.getConfig();
		ConfigElement newElement = new ConfigElement(config.getCategory(categoryName));
		List<IConfigElement> propertiesOnScreen = newElement.getChildElements();
		String windowTitle = Lang.localise("gui.config.category." + categoryName);
		return new GuiConfig(owningScreen, propertiesOnScreen, owningScreen.modID, configElement.requiresWorldRestart() || owningScreen.allRequireWorldRestart, configElement.requiresMcRestart() || owningScreen.allRequireMcRestart, windowTitle);
	}
}
