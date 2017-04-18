package nc.integ.jei;

import javax.annotation.Nonnull;

import mezz.jei.api.BlankModPlugin;
import mezz.jei.api.IJeiRuntime;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.ISubtypeRegistry;
import mezz.jei.api.JEIPlugin;

@JEIPlugin
public class JEIPeripheral extends BlankModPlugin {
	
	private static IJeiRuntime jeiRuntime = null;
	
	public void registerItemSubtypes(ISubtypeRegistry subtypeRegistry) {
		
	}

	public void register(@Nonnull IModRegistry registry) {
		//IJeiHelpers jeiHelpers = registry.getJeiHelpers();
		//IGuiHelper guiHelper = jeiHelpers.getGuiHelper();
		
		/*ManufactoryRecipeCategory.register(registry, guiHelper);
		IsotopeSeparatorRecipeCategory.register(registry, guiHelper);
		DecayHastenerRecipeCategory.register(registry, guiHelper);
		FuelReprocessorRecipeCategory.register(registry, guiHelper);
		AlloyFurnaceRecipeCategory.register(registry, guiHelper);*/
		
		registry.addAdvancedGuiHandlers(new AdvancedGuiHandlerNC());
	}
	
	public void onRuntimeAvailable(@Nonnull IJeiRuntime jeiRuntime) {
		JEIPeripheral.jeiRuntime = jeiRuntime;
		JEIRetriever.jeiRuntimeAvailable = true;
	}
	
	public static void setFilterText(@Nonnull String filterText) {
		jeiRuntime.getItemListOverlay().setFilterText(filterText);
	}
	
	public static @Nonnull String getFilterText() {
		return jeiRuntime.getItemListOverlay().getFilterText();
	}
}
