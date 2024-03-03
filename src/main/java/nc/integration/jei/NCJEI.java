package nc.integration.jei;

import static nc.config.NCConfig.*;

import java.util.*;

import mezz.jei.api.*;
import mezz.jei.api.recipe.transfer.IRecipeTransferRegistry;
import nc.ModCheck;
import nc.enumm.MetaEnums;
import nc.handler.TileInfoHandler;
import nc.init.*;
import nc.integration.jei.category.info.JEICategoryInfo;
import nc.multiblock.fission.FissionPlacement;
import nc.recipe.BasicRecipe;
import nc.recipe.ingredient.IItemIngredient;
import nc.util.*;
import net.minecraft.item.*;

@JEIPlugin
public class NCJEI implements IModPlugin {
	
	@Override
	public void register(IModRegistry registry) {
		IJeiHelpers jeiHelpers = registry.getJeiHelpers();
		IGuiHelper guiHelper = jeiHelpers.getGuiHelper();
		IRecipeTransferRegistry transferRegistry = registry.getRecipeTransferRegistry();
		
		for (JEICategoryInfo<?, ?, ?> categoryInfo : TileInfoHandler.JEI_CATEGORY_INFO_MAP.values()) {
			categoryInfo.registerJEICategory(registry, jeiHelpers, guiHelper, transferRegistry);
		}
		
		for (int i = 0; i < MetaEnums.OreType.values().length; ++i) {
			if (!ore_gen[i] && ore_hide_disabled) {
				blacklist(jeiHelpers, new ItemStack(NCBlocks.ore, 1, i), new ItemStack(NCBlocks.ingot_block, 1, i), new ItemStack(NCItems.ingot, 1, i), new ItemStack(NCItems.dust, 1, i));
			}
		}
		
		if (!ModCheck.openComputersLoaded()) {
			blacklist(jeiHelpers, NCBlocks.fission_computer_port);
			blacklist(jeiHelpers, NCBlocks.heat_exchanger_computer_port);
			blacklist(jeiHelpers, NCBlocks.turbine_computer_port);
		}
		
		if (!radiation_enabled_public) {
			blacklist(jeiHelpers, NCBlocks.radiation_scrubber);
			blacklist(jeiHelpers, NCBlocks.geiger_block, NCItems.geiger_counter);
			blacklistAll(jeiHelpers, MetaEnums.RadShieldingType.class, NCItems.rad_shielding);
			blacklist(jeiHelpers, NCItems.radiation_badge);
			blacklist(jeiHelpers, NCItems.radaway, NCItems.radaway_slow);
			blacklist(jeiHelpers, NCItems.rad_x);
			if (!ModCheck.ic2Loaded()) {
				blacklist(jeiHelpers, NCArmor.helm_hazmat, NCArmor.chest_hazmat, NCArmor.legs_hazmat, NCArmor.boots_hazmat);
			}
		}
		
		if (!ModCheck.ic2Loaded()) {
			blacklistAll(jeiHelpers, MetaEnums.IC2DepletedFuelType.class, NCItems.depleted_fuel_ic2);
		}
		
		blacklist(jeiHelpers, NCItems.foursmore);
		
		NCUtil.getLogger().info("JEI integration complete!");
	}
	
	private static void blacklist(IJeiHelpers jeiHelpers, Object... items) {
		for (Object item : items) {
			if (item == null) {
				return;
			}
			jeiHelpers.getIngredientBlacklist().addIngredientToBlacklist(StackHelper.fixItemStack(item));
		}
	}
	
	private static <T extends Enum<T>> void blacklistAll(IJeiHelpers jeiHelpers, Class<T> enumm, Item item) {
		if (item == null) {
			return;
		}
		for (int i = 0; i < enumm.getEnumConstants().length; ++i) {
			blacklist(jeiHelpers, new ItemStack(item, 1, i));
		}
	}
	
	public static List<Object> registeredCollectors() {
		List<Object> list = new ArrayList<>();
		if (register_passive[0]) {
			list.add(NCBlocks.cobblestone_generator);
			list.add(NCBlocks.cobblestone_generator_compact);
			list.add(NCBlocks.cobblestone_generator_dense);
		}
		if (register_passive[1]) {
			list.add(NCBlocks.water_source);
			list.add(NCBlocks.water_source_compact);
			list.add(NCBlocks.water_source_dense);
		}
		if (register_passive[2]) {
			list.add(NCBlocks.nitrogen_collector);
			list.add(NCBlocks.nitrogen_collector_compact);
			list.add(NCBlocks.nitrogen_collector_dense);
		}
		return list;
	}
	
	public static List<Object> getCoolantHeaters() {
		List<Object> list = new ArrayList<>();
		for (BasicRecipe recipe : FissionPlacement.recipe_handler.getRecipeList()) {
			if (recipe.getPlacementRuleID().endsWith("_heater"))
				for (IItemIngredient ingredient : recipe.getItemIngredients()) {
                    list.addAll(ingredient.getInputStackList());
				}
		}
		return list;
	}
}
