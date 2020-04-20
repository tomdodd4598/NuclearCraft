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
			list.add(categoryElement(NCConfig.CATEGORY_WORLD_GEN, CategoryEntryWorldGen.class));
			list.add(categoryElement(NCConfig.CATEGORY_PROCESSOR, CategoryEntryProcessor.class));
			list.add(categoryElement(NCConfig.CATEGORY_GENERATOR, CategoryEntryGenerator.class));
			list.add(categoryElement(NCConfig.CATEGORY_ENERGY_STORAGE, CategoryEntryEnergyStorage.class));
			list.add(categoryElement(NCConfig.CATEGORY_FISSION, CategoryEntryFission.class));
			list.add(categoryElement(NCConfig.CATEGORY_FUSION, CategoryEntryFusion.class));
			list.add(categoryElement(NCConfig.CATEGORY_HEAT_EXCHANGER, CategoryEntryHeatExchanger.class));
			list.add(categoryElement(NCConfig.CATEGORY_TURBINE, CategoryEntryTurbine.class));
			list.add(categoryElement(NCConfig.CATEGORY_ACCELERATOR, CategoryEntryAccelerator.class));
			list.add(categoryElement(NCConfig.CATEGORY_QUANTUM, CategoryEntryQuantum.class));
			list.add(categoryElement(NCConfig.CATEGORY_TOOL, CategoryEntryTool.class));
			list.add(categoryElement(NCConfig.CATEGORY_ARMOR, CategoryEntryArmor.class));
			list.add(categoryElement(NCConfig.CATEGORY_ENTITY, CategoryEntryEntity.class));
			list.add(categoryElement(NCConfig.CATEGORY_RADIATION, CategoryEntryRadiation.class));
			list.add(categoryElement(NCConfig.CATEGORY_REGISTRATION, CategoryEntryRegistration.class));
			list.add(categoryElement(NCConfig.CATEGORY_MISC, CategoryEntryMisc.class));
			return list;
		}
		
		private static DummyCategoryElement categoryElement(String categoryName, Class<? extends IConfigEntry> categoryClass) {
			return new DummyCategoryElement(Lang.localise("gui.nc.config.category." + categoryName), "gui.nc.config.category." + categoryName, categoryClass);
		}
		
		public static class CategoryEntryWorldGen extends CategoryEntry implements IConfigCategory {

			public CategoryEntryWorldGen(GuiConfig owningScreen, GuiConfigEntries owningEntryList, IConfigElement configElement) {
				super(owningScreen, owningEntryList, configElement);
			}
			
			@Override
			protected GuiScreen buildChildScreen() {
				return buildChildScreen(NCConfig.CATEGORY_WORLD_GEN, owningScreen, configElement);
			}
		}
		
		public static class CategoryEntryProcessor extends CategoryEntry implements IConfigCategory {

			public CategoryEntryProcessor(GuiConfig owningScreen, GuiConfigEntries owningEntryList, IConfigElement configElement) {
				super(owningScreen, owningEntryList, configElement);
			}
			
			@Override
			protected GuiScreen buildChildScreen() {
				return buildChildScreen(NCConfig.CATEGORY_PROCESSOR, owningScreen, configElement);
			}
		}
		
		public static class CategoryEntryGenerator extends CategoryEntry implements IConfigCategory {

			public CategoryEntryGenerator(GuiConfig owningScreen, GuiConfigEntries owningEntryList, IConfigElement configElement) {
				super(owningScreen, owningEntryList, configElement);
			}
			
			@Override
			protected GuiScreen buildChildScreen() {
				return buildChildScreen(NCConfig.CATEGORY_GENERATOR, owningScreen, configElement);
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
		
		public static class CategoryEntryQuantum extends CategoryEntry implements IConfigCategory {

			public CategoryEntryQuantum(GuiConfig owningScreen, GuiConfigEntries owningEntryList, IConfigElement configElement) {
				super(owningScreen, owningEntryList, configElement);
			}
			
			@Override
			protected GuiScreen buildChildScreen() {
				return buildChildScreen(NCConfig.CATEGORY_QUANTUM, owningScreen, configElement);
			}
		}
		
		public static class CategoryEntryTool extends CategoryEntry implements IConfigCategory {

			public CategoryEntryTool(GuiConfig owningScreen, GuiConfigEntries owningEntryList, IConfigElement configElement) {
				super(owningScreen, owningEntryList, configElement);
			}
			
			@Override
			protected GuiScreen buildChildScreen() {
				return buildChildScreen(NCConfig.CATEGORY_TOOL, owningScreen, configElement);
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
		
		public static class CategoryEntryEntity extends CategoryEntry implements IConfigCategory {

			public CategoryEntryEntity(GuiConfig owningScreen, GuiConfigEntries owningEntryList, IConfigElement configElement) {
				super(owningScreen, owningEntryList, configElement);
			}
			
			@Override
			protected GuiScreen buildChildScreen() {
				return buildChildScreen(NCConfig.CATEGORY_ENTITY, owningScreen, configElement);
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
		
		public static class CategoryEntryRegistration extends CategoryEntry implements IConfigCategory {

			public CategoryEntryRegistration(GuiConfig owningScreen, GuiConfigEntries owningEntryList, IConfigElement configElement) {
				super(owningScreen, owningEntryList, configElement);
			}
			
			@Override
			protected GuiScreen buildChildScreen() {
				return buildChildScreen(NCConfig.CATEGORY_REGISTRATION, owningScreen, configElement);
			}
		}
		
		public static class CategoryEntryMisc extends CategoryEntry implements IConfigCategory {

			public CategoryEntryMisc(GuiConfig owningScreen, GuiConfigEntries owningEntryList, IConfigElement configElement) {
				super(owningScreen, owningEntryList, configElement);
			}
			
			@Override
			protected GuiScreen buildChildScreen() {
				return buildChildScreen(NCConfig.CATEGORY_MISC, owningScreen, configElement);
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
