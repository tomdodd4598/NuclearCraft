package nc.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import nc.Global;
import nc.util.Lang;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.IModGuiFactory;
import net.minecraftforge.fml.client.config.DummyConfigElement.DummyCategoryElement;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.GuiConfigEntries;
import net.minecraftforge.fml.client.config.GuiConfigEntries.CategoryEntry;
import net.minecraftforge.fml.client.config.GuiConfigEntries.IConfigEntry;
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
			super(parentScreen, getConfigElements(), Global.MOD_ID, false, false, Lang.localise("gui.nc.config.main_title"));
		}

		private static List<IConfigElement> getConfigElements() {
			List<IConfigElement> list = new ArrayList<>();
			list.add(categoryElement(NCConfig.CATEGORY_ORES, CategoryEntryOres.class));
			list.add(categoryElement(NCConfig.CATEGORY_PROCESSORS, CategoryEntryProcessors.class));
			list.add(categoryElement(NCConfig.CATEGORY_GENERATORS, CategoryEntryGenerators.class));
			list.add(categoryElement(NCConfig.CATEGORY_FISSION, CategoryEntryFission.class));
			list.add(categoryElement(NCConfig.CATEGORY_FUSION, CategoryEntryFusion.class));
			list.add(categoryElement(NCConfig.CATEGORY_HEAT_EXCHANGER, CategoryEntryHeatExchanger.class));
			list.add(categoryElement(NCConfig.CATEGORY_TURBINE, CategoryEntryTurbine.class));
			list.add(categoryElement(NCConfig.CATEGORY_ACCELERATOR, CategoryEntryAccelerator.class));
			list.add(categoryElement(NCConfig.CATEGORY_ENERGY_STORAGE, CategoryEntryEnergyStorage.class));
			list.add(categoryElement(NCConfig.CATEGORY_TOOLS, CategoryEntryTools.class));
			list.add(categoryElement(NCConfig.CATEGORY_ARMOR, CategoryEntryArmor.class));
			list.add(categoryElement(NCConfig.CATEGORY_RADIATION, CategoryEntryRadiation.class));
			list.add(categoryElement(NCConfig.CATEGORY_ENTITIES, CategoryEntryEntities.class));
			list.add(categoryElement(NCConfig.CATEGORY_OTHER, CategoryEntryOther.class));
			return list;
		}
		
		private static DummyCategoryElement categoryElement(String categoryName, Class<? extends IConfigEntry> categoryClass) {
			return new DummyCategoryElement(Lang.localise("gui.nc.config.category." + categoryName), "gui.nc.config.category." + categoryName, categoryClass);
		}
		
		public static class CategoryEntryOres extends CategoryEntry implements IConfigCategory {

			public CategoryEntryOres(GuiConfig owningScreen, GuiConfigEntries owningEntryList, IConfigElement configElement) {
				super(owningScreen, owningEntryList, configElement);
			}
			
			@Override
			protected GuiScreen buildChildScreen() {
				return buildChildScreen(NCConfig.CATEGORY_ORES, owningScreen, configElement);
			}
		}
		
		public static class CategoryEntryProcessors extends CategoryEntry implements IConfigCategory {

			public CategoryEntryProcessors(GuiConfig owningScreen, GuiConfigEntries owningEntryList, IConfigElement configElement) {
				super(owningScreen, owningEntryList, configElement);
			}
			
			@Override
			protected GuiScreen buildChildScreen() {
				return buildChildScreen(NCConfig.CATEGORY_PROCESSORS, owningScreen, configElement);
			}
		}
		
		public static class CategoryEntryGenerators extends CategoryEntry implements IConfigCategory {

			public CategoryEntryGenerators(GuiConfig owningScreen, GuiConfigEntries owningEntryList, IConfigElement configElement) {
				super(owningScreen, owningEntryList, configElement);
			}
			
			@Override
			protected GuiScreen buildChildScreen() {
				return buildChildScreen(NCConfig.CATEGORY_GENERATORS, owningScreen, configElement);
			}
		}
		
		public static class CategoryEntryFission extends CategoryEntry implements IConfigCategory {

			public CategoryEntryFission(GuiConfig owningScreen, GuiConfigEntries owningEntryList, IConfigElement configElement) {
				super(owningScreen, owningEntryList, configElement);
			}
			
			@Override
			protected GuiScreen buildChildScreen() {
				return buildChildScreen(NCConfig.CATEGORY_FISSION, owningScreen, configElement);
			}
		}
		
		public static class CategoryEntryFusion extends CategoryEntry implements IConfigCategory {

			public CategoryEntryFusion(GuiConfig owningScreen, GuiConfigEntries owningEntryList, IConfigElement configElement) {
				super(owningScreen, owningEntryList, configElement);
			}
			
			@Override
			protected GuiScreen buildChildScreen() {
				return buildChildScreen(NCConfig.CATEGORY_FUSION, owningScreen, configElement);
			}
		}
		
		public static class CategoryEntryHeatExchanger extends CategoryEntry implements IConfigCategory {

			public CategoryEntryHeatExchanger(GuiConfig owningScreen, GuiConfigEntries owningEntryList, IConfigElement configElement) {
				super(owningScreen, owningEntryList, configElement);
			}
			
			@Override
			protected GuiScreen buildChildScreen() {
				return buildChildScreen(NCConfig.CATEGORY_HEAT_EXCHANGER, owningScreen, configElement);
			}
		}
		
		public static class CategoryEntryTurbine extends CategoryEntry implements IConfigCategory {

			public CategoryEntryTurbine(GuiConfig owningScreen, GuiConfigEntries owningEntryList, IConfigElement configElement) {
				super(owningScreen, owningEntryList, configElement);
			}
			
			@Override
			protected GuiScreen buildChildScreen() {
				return buildChildScreen(NCConfig.CATEGORY_TURBINE, owningScreen, configElement);
			}
		}
		
		public static class CategoryEntryAccelerator extends CategoryEntry implements IConfigCategory {

			public CategoryEntryAccelerator(GuiConfig owningScreen, GuiConfigEntries owningEntryList, IConfigElement configElement) {
				super(owningScreen, owningEntryList, configElement);
			}
			
			@Override
			protected GuiScreen buildChildScreen() {
				return buildChildScreen(NCConfig.CATEGORY_ACCELERATOR, owningScreen, configElement);
			}
		}
		
		public static class CategoryEntryEnergyStorage extends CategoryEntry implements IConfigCategory {

			public CategoryEntryEnergyStorage(GuiConfig owningScreen, GuiConfigEntries owningEntryList, IConfigElement configElement) {
				super(owningScreen, owningEntryList, configElement);
			}
			
			@Override
			protected GuiScreen buildChildScreen() {
				return buildChildScreen(NCConfig.CATEGORY_ENERGY_STORAGE, owningScreen, configElement);
			}
		}
		
		public static class CategoryEntryTools extends CategoryEntry implements IConfigCategory {

			public CategoryEntryTools(GuiConfig owningScreen, GuiConfigEntries owningEntryList, IConfigElement configElement) {
				super(owningScreen, owningEntryList, configElement);
			}
			
			@Override
			protected GuiScreen buildChildScreen() {
				return buildChildScreen(NCConfig.CATEGORY_TOOLS, owningScreen, configElement);
			}
		}
		
		public static class CategoryEntryArmor extends CategoryEntry implements IConfigCategory {

			public CategoryEntryArmor(GuiConfig owningScreen, GuiConfigEntries owningEntryList, IConfigElement configElement) {
				super(owningScreen, owningEntryList, configElement);
			}
			
			@Override
			protected GuiScreen buildChildScreen() {
				return buildChildScreen(NCConfig.CATEGORY_ARMOR, owningScreen, configElement);
			}
		}
		
		public static class CategoryEntryRadiation extends CategoryEntry implements IConfigCategory {

			public CategoryEntryRadiation(GuiConfig owningScreen, GuiConfigEntries owningEntryList, IConfigElement configElement) {
				super(owningScreen, owningEntryList, configElement);
			}
			
			@Override
			protected GuiScreen buildChildScreen() {
				return buildChildScreen(NCConfig.CATEGORY_RADIATION, owningScreen, configElement);
			}
		}
		
		public static class CategoryEntryEntities extends CategoryEntry implements IConfigCategory {

			public CategoryEntryEntities(GuiConfig owningScreen, GuiConfigEntries owningEntryList, IConfigElement configElement) {
				super(owningScreen, owningEntryList, configElement);
			}
			
			@Override
			protected GuiScreen buildChildScreen() {
				return buildChildScreen(NCConfig.CATEGORY_ENTITIES, owningScreen, configElement);
			}
		}
		
		public static class CategoryEntryOther extends CategoryEntry implements IConfigCategory {

			public CategoryEntryOther(GuiConfig owningScreen, GuiConfigEntries owningEntryList, IConfigElement configElement) {
				super(owningScreen, owningEntryList, configElement);
			}
			
			@Override
			protected GuiScreen buildChildScreen() {
				return buildChildScreen(NCConfig.CATEGORY_OTHER, owningScreen, configElement);
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
