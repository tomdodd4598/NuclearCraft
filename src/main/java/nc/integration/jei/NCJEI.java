package nc.integration.jei;

import mezz.jei.api.*;
import mezz.jei.api.recipe.VanillaRecipeCategoryUid;
import mezz.jei.api.recipe.transfer.IRecipeTransferRegistry;
import nc.ModCheck;
import nc.block.hx.*;
import nc.block.turbine.BlockTurbineController;
import nc.container.processor.ContainerNuclearFurnace;
import nc.enumm.MetaEnums;
import nc.gui.processor.GuiNuclearFurnace;
import nc.handler.TileInfoHandler;
import nc.init.*;
import nc.integration.jei.category.info.JEICategoryInfo;
import nc.multiblock.fission.FissionPlacement;
import nc.multiblock.turbine.TurbineRotorBladeUtil.IBlockRotorBlade;
import nc.recipe.*;
import nc.util.*;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.*;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.*;

import static nc.config.NCConfig.*;

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
		
		registry.addRecipeCatalyst(new ItemStack(NCBlocks.nuclear_furnace), VanillaRecipeCategoryUid.SMELTING);
		registry.addRecipeClickArea(GuiNuclearFurnace.class, 78, 32, 28, 23, VanillaRecipeCategoryUid.SMELTING);
		transferRegistry.addRecipeTransferHandler(ContainerNuclearFurnace.class, VanillaRecipeCategoryUid.SMELTING, 0, 1, 3, 36);
		
		for (int i = 0; i < MetaEnums.OreType.values().length; ++i) {
			if (!ore_gen[i] && ore_hide_disabled) {
				blacklist(jeiHelpers, new ItemStack(NCBlocks.ore, 1, i), new ItemStack(NCBlocks.ingot_block, 1, i), new ItemStack(NCItems.ingot, 1, i), new ItemStack(NCItems.dust, 1, i));
			}
		}
		
		if (!ModCheck.openComputersLoaded()) {
			blacklist(jeiHelpers, NCBlocks.machine_computer_port);
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
	
	private static List<Object> getRecipeListInputs(Stream<BasicRecipe> recipes) {
		return recipes.flatMap(x -> x.getItemIngredients().stream()).flatMap(x -> x.getInputStackList().stream()).collect(Collectors.toList());
	}
	
	private static List<Object> getRecipeListInputs(BasicRecipeHandler recipeHandler) {
		return getRecipeListInputs(recipeHandler.getRecipeList().stream());
	}
	
	private static List<Object> getBlockList(Predicate<? super Block> predicate) {
		return ForgeRegistries.BLOCKS.getValuesCollection().stream().filter(predicate).flatMap(x -> {
			Item item = Item.getItemFromBlock(x);
			return item instanceof ItemBlock ? LambdaHelper.also(NonNullList.<ItemStack>create(), y -> item.getSubItems(CreativeTabs.SEARCH, y)).stream() : Stream.empty();
		}).collect(Collectors.toList());
	}
	
	public static List<Object> getMachineDiaphragmCrafters() {
		return getRecipeListInputs(NCRecipes.machine_diaphragm);
	}
	
	public static List<Object> getMachineSieveAssemblyCrafters() {
		return getRecipeListInputs(NCRecipes.machine_sieve_assembly);
	}
	
	public static List<Object> getFissionModeratorCrafters() {
		List<Object> list = getRecipeListInputs(NCRecipes.fission_moderator);
		ItemStack heavyWaterModerator = new ItemStack(NCBlocks.heavy_water_moderator);
		list.sort(Comparator.comparingInt(x -> x instanceof ItemStack stack && stack.isItemEqual(heavyWaterModerator) ? 0 : 1));
		return list;
	}
	
	public static List<Object> getFissionReflectorCrafters() {
		List<Object> list = getRecipeListInputs(NCRecipes.fission_reflector);
		ItemStack berylliumCarbonReflector = new ItemStack(NCBlocks.fission_reflector);
		list.sort(Comparator.comparingInt(x -> x instanceof ItemStack stack && stack.isItemEqual(berylliumCarbonReflector) ? 0 : 1));
		return list;
	}
	
	public static List<Object> getCoolantHeaterCrafters() {
		return getRecipeListInputs(FissionPlacement.recipe_handler.getRecipeList().stream().filter(x -> x.getPlacementRuleID().endsWith("_heater")));
	}
	
	public static List<Object> getHeatExchangerCrafters() {
		return getBlockList(x -> x instanceof BlockHeatExchangerController || x instanceof BlockHeatExchangerTube);
	}
	
	public static List<Object> getTurbineCrafters() {
		return getBlockList(x -> x instanceof BlockTurbineController || x instanceof IBlockRotorBlade);
	}
}
