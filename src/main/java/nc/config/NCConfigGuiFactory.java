package nc.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import nc.Global;
import nc.util.Lang;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.IModGuiFactory;
import net.minecraftforge.fml.client.config.DummyConfigElement.DummyCategoryElement;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.GuiConfigEntries;
import net.minecraftforge.fml.client.config.GuiConfigEntries.CategoryEntry;
import net.minecraftforge.fml.client.config.IConfigElement;

public class NCConfigGuiFactory implements IModGuiFactory {

	@Override
	public void initialize(Minecraft minecraftInstance) {}

	public Class<? extends GuiScreen> mainConfigGuiClass() {
		return NCConfigGui.class;
	}

	@Override
	public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() {
		return null;
	}

	/*public RuntimeOptionGuiHandler getHandlerFor(RuntimeOptionCategoryElement element) {
		return null;
	}*/
	
	public static class NCConfigGui extends GuiConfig {
		
		public NCConfigGui(GuiScreen parentScreen) {
			super(parentScreen, getConfigElements(), Global.MOD_ID, false, false, Lang.localise("gui.config.main_title"));
		}

		private static List<IConfigElement> getConfigElements() {
			List<IConfigElement> list = new ArrayList<IConfigElement>();
			list.add(new DummyCategoryElement(Lang.localise("gui.config.category.ores"), "gui.config.category.ores", CategoryEntryOres.class));
			list.add(new DummyCategoryElement(Lang.localise("gui.config.category.processors"), "gui.config.category.processors", CategoryEntryProcessors.class));
			list.add(new DummyCategoryElement(Lang.localise("gui.config.category.generators"), "gui.config.category.generators", CategoryEntryGenerators.class));
			list.add(new DummyCategoryElement(Lang.localise("gui.config.category.fission"), "gui.config.category.fission", CategoryEntryFission.class));
			list.add(new DummyCategoryElement(Lang.localise("gui.config.category.fusion"), "gui.config.category.fusion", CategoryEntryFusion.class));
			list.add(new DummyCategoryElement(Lang.localise("gui.config.category.salt_fission"), "gui.config.category.salt_fission", CategoryEntrySaltFission.class));
			list.add(new DummyCategoryElement(Lang.localise("gui.config.category.accelerator"), "gui.config.category.accelerator", CategoryEntryAccelerator.class));
			list.add(new DummyCategoryElement(Lang.localise("gui.config.category.energy_storage"), "gui.config.category.energy_storage", CategoryEntryEnergyStorage.class));
			list.add(new DummyCategoryElement(Lang.localise("gui.config.category.tools"), "gui.config.category.tools", CategoryEntryTools.class));
			list.add(new DummyCategoryElement(Lang.localise("gui.config.category.armor"), "gui.config.category.armor", CategoryEntryArmor.class));
			list.add(new DummyCategoryElement(Lang.localise("gui.config.category.other"), "gui.config.category.other", CategoryEntryOther.class));
			return list;
		}
		
		public static class CategoryEntryOres extends CategoryEntry {

			public CategoryEntryOres(GuiConfig owningScreen, GuiConfigEntries owningEntryList, IConfigElement configElement) {
				super(owningScreen, owningEntryList, configElement);
			}
			
			@Override
			protected GuiScreen buildChildScreen() {
				Configuration config = NCConfig.getConfig();
				ConfigElement categoryOres = new ConfigElement(config.getCategory(NCConfig.CATEGORY_ORES));
				List<IConfigElement> propertiesOnScreen = categoryOres.getChildElements();
				String windowTitle = Lang.localise("gui.config.category.ores");
				return new GuiConfig(owningScreen, propertiesOnScreen, owningScreen.modID, configElement.requiresWorldRestart() || owningScreen.allRequireWorldRestart, configElement.requiresMcRestart() || owningScreen.allRequireMcRestart, windowTitle);
			}
		}
		
		public static class CategoryEntryProcessors extends CategoryEntry {

			public CategoryEntryProcessors(GuiConfig owningScreen, GuiConfigEntries owningEntryList, IConfigElement configElement) {
				super(owningScreen, owningEntryList, configElement);
			}
			
			@Override
			protected GuiScreen buildChildScreen() {
				Configuration config = NCConfig.getConfig();
				ConfigElement categoryProcessors = new ConfigElement(config.getCategory(NCConfig.CATEGORY_PROCESSORS));
				List<IConfigElement> propertiesOnScreen = categoryProcessors.getChildElements();
				String windowTitle = Lang.localise("gui.config.category.processors");
				return new GuiConfig(owningScreen, propertiesOnScreen, owningScreen.modID, configElement.requiresWorldRestart() || owningScreen.allRequireWorldRestart, configElement.requiresMcRestart() || owningScreen.allRequireMcRestart, windowTitle);
			}
		}
		
		public static class CategoryEntryGenerators extends CategoryEntry {

			public CategoryEntryGenerators(GuiConfig owningScreen, GuiConfigEntries owningEntryList, IConfigElement configElement) {
				super(owningScreen, owningEntryList, configElement);
			}
			
			@Override
			protected GuiScreen buildChildScreen() {
				Configuration config = NCConfig.getConfig();
				ConfigElement categoryGenerators = new ConfigElement(config.getCategory(NCConfig.CATEGORY_GENERATORS));
				List<IConfigElement> propertiesOnScreen = categoryGenerators.getChildElements();
				String windowTitle = Lang.localise("gui.config.category.generators");
				return new GuiConfig(owningScreen, propertiesOnScreen, owningScreen.modID, configElement.requiresWorldRestart() || owningScreen.allRequireWorldRestart, configElement.requiresMcRestart() || owningScreen.allRequireMcRestart, windowTitle);
			}
		}
		
		public static class CategoryEntryFission extends CategoryEntry {

			public CategoryEntryFission(GuiConfig owningScreen, GuiConfigEntries owningEntryList, IConfigElement configElement) {
				super(owningScreen, owningEntryList, configElement);
			}
			
			@Override
			protected GuiScreen buildChildScreen() {
				Configuration config = NCConfig.getConfig();
				ConfigElement categoryFission = new ConfigElement(config.getCategory(NCConfig.CATEGORY_FISSION));
				List<IConfigElement> propertiesOnScreen = categoryFission.getChildElements();
				String windowTitle = Lang.localise("gui.config.category.fission");
				return new GuiConfig(owningScreen, propertiesOnScreen, owningScreen.modID, configElement.requiresWorldRestart() || owningScreen.allRequireWorldRestart, configElement.requiresMcRestart() || owningScreen.allRequireMcRestart, windowTitle);
			}
		}
		
		public static class CategoryEntryFusion extends CategoryEntry {

			public CategoryEntryFusion(GuiConfig owningScreen, GuiConfigEntries owningEntryList, IConfigElement configElement) {
				super(owningScreen, owningEntryList, configElement);
			}
			
			@Override
			protected GuiScreen buildChildScreen() {
				Configuration config = NCConfig.getConfig();
				ConfigElement categoryFission = new ConfigElement(config.getCategory(NCConfig.CATEGORY_FUSION));
				List<IConfigElement> propertiesOnScreen = categoryFission.getChildElements();
				String windowTitle = Lang.localise("gui.config.category.fusion");
				return new GuiConfig(owningScreen, propertiesOnScreen, owningScreen.modID, configElement.requiresWorldRestart() || owningScreen.allRequireWorldRestart, configElement.requiresMcRestart() || owningScreen.allRequireMcRestart, windowTitle);
			}
		}
		
		public static class CategoryEntrySaltFission extends CategoryEntry {

			public CategoryEntrySaltFission(GuiConfig owningScreen, GuiConfigEntries owningEntryList, IConfigElement configElement) {
				super(owningScreen, owningEntryList, configElement);
			}
			
			@Override
			protected GuiScreen buildChildScreen() {
				Configuration config = NCConfig.getConfig();
				ConfigElement categoryFission = new ConfigElement(config.getCategory(NCConfig.CATEGORY_SALT_FISSION));
				List<IConfigElement> propertiesOnScreen = categoryFission.getChildElements();
				String windowTitle = Lang.localise("gui.config.category.salt_fission");
				return new GuiConfig(owningScreen, propertiesOnScreen, owningScreen.modID, configElement.requiresWorldRestart() || owningScreen.allRequireWorldRestart, configElement.requiresMcRestart() || owningScreen.allRequireMcRestart, windowTitle);
			}
		}
		
		public static class CategoryEntryAccelerator extends CategoryEntry {

			public CategoryEntryAccelerator(GuiConfig owningScreen, GuiConfigEntries owningEntryList, IConfigElement configElement) {
				super(owningScreen, owningEntryList, configElement);
			}
			
			@Override
			protected GuiScreen buildChildScreen() {
				Configuration config = NCConfig.getConfig();
				ConfigElement categoryFission = new ConfigElement(config.getCategory(NCConfig.CATEGORY_ACCELERATOR));
				List<IConfigElement> propertiesOnScreen = categoryFission.getChildElements();
				String windowTitle = Lang.localise("gui.config.category.accelerator");
				return new GuiConfig(owningScreen, propertiesOnScreen, owningScreen.modID, configElement.requiresWorldRestart() || owningScreen.allRequireWorldRestart, configElement.requiresMcRestart() || owningScreen.allRequireMcRestart, windowTitle);
			}
		}
		
		public static class CategoryEntryEnergyStorage extends CategoryEntry {

			public CategoryEntryEnergyStorage(GuiConfig owningScreen, GuiConfigEntries owningEntryList, IConfigElement configElement) {
				super(owningScreen, owningEntryList, configElement);
			}
			
			@Override
			protected GuiScreen buildChildScreen() {
				Configuration config = NCConfig.getConfig();
				ConfigElement categoryEnergyStorage = new ConfigElement(config.getCategory(NCConfig.CATEGORY_ENERGY_STORAGE));
				List<IConfigElement> propertiesOnScreen = categoryEnergyStorage.getChildElements();
				String windowTitle = Lang.localise("gui.config.category.energy_storage");
				return new GuiConfig(owningScreen, propertiesOnScreen, owningScreen.modID, configElement.requiresWorldRestart() || owningScreen.allRequireWorldRestart, configElement.requiresMcRestart() || owningScreen.allRequireMcRestart, windowTitle);
			}
		}
		
		public static class CategoryEntryTools extends CategoryEntry {

			public CategoryEntryTools(GuiConfig owningScreen, GuiConfigEntries owningEntryList, IConfigElement configElement) {
				super(owningScreen, owningEntryList, configElement);
			}
			
			@Override
			protected GuiScreen buildChildScreen() {
				Configuration config = NCConfig.getConfig();
				ConfigElement categoryTools = new ConfigElement(config.getCategory(NCConfig.CATEGORY_TOOLS));
				List<IConfigElement> propertiesOnScreen = categoryTools.getChildElements();
				String windowTitle = Lang.localise("gui.config.category.tools");
				return new GuiConfig(owningScreen, propertiesOnScreen, owningScreen.modID, configElement.requiresWorldRestart() || owningScreen.allRequireWorldRestart, configElement.requiresMcRestart() || owningScreen.allRequireMcRestart, windowTitle);
			}
		}
		
		public static class CategoryEntryArmor extends CategoryEntry {

			public CategoryEntryArmor(GuiConfig owningScreen, GuiConfigEntries owningEntryList, IConfigElement configElement) {
				super(owningScreen, owningEntryList, configElement);
			}
			
			@Override
			protected GuiScreen buildChildScreen() {
				Configuration config = NCConfig.getConfig();
				ConfigElement categoryTools = new ConfigElement(config.getCategory(NCConfig.CATEGORY_ARMOR));
				List<IConfigElement> propertiesOnScreen = categoryTools.getChildElements();
				String windowTitle = Lang.localise("gui.config.category.armor");
				return new GuiConfig(owningScreen, propertiesOnScreen, owningScreen.modID, configElement.requiresWorldRestart() || owningScreen.allRequireWorldRestart, configElement.requiresMcRestart() || owningScreen.allRequireMcRestart, windowTitle);
			}
		}
		
		public static class CategoryEntryOther extends CategoryEntry {

			public CategoryEntryOther(GuiConfig owningScreen, GuiConfigEntries owningEntryList, IConfigElement configElement) {
				super(owningScreen, owningEntryList, configElement);
			}
			
			@Override
			protected GuiScreen buildChildScreen() {
				Configuration config = NCConfig.getConfig();
				ConfigElement categoryOther = new ConfigElement(config.getCategory(NCConfig.CATEGORY_OTHER));
				List<IConfigElement> propertiesOnScreen = categoryOther.getChildElements();
				String windowTitle = Lang.localise("gui.config.category.other");
				return new GuiConfig(owningScreen, propertiesOnScreen, owningScreen.modID, configElement.requiresWorldRestart() || owningScreen.allRequireWorldRestart, configElement.requiresMcRestart() || owningScreen.allRequireMcRestart, windowTitle);
			}
		}
	}

	@Override
	public boolean hasConfigGui() {
		return true;
	}

	@Override
	public GuiScreen createConfigGui(GuiScreen parentScreen) {
		return new NCConfigGui(parentScreen);
	}
}
