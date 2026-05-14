package nc.handler;

import com.google.common.collect.Lists;
import nc.NCInfo;
import nc.capability.radiation.resistance.IRadiationResistance;
import nc.capability.radiation.source.IRadiationSource;
import nc.multiblock.fission.FissionPlacement;
import nc.multiblock.turbine.TurbinePlacement;
import nc.radiation.*;
import nc.recipe.*;
import nc.util.*;
import net.minecraft.client.util.RecipeItemHelper;
import net.minecraft.item.*;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.*;
import net.minecraftforge.fml.relauncher.*;

import java.util.*;
import java.util.function.Function;

import static nc.config.NCConfig.*;

public class TooltipHandler {
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent(priority = EventPriority.HIGH)
	public void addAdditionalTooltips(ItemTooltipEvent event) {
		List<String> tooltip = event.getToolTip();
		ItemStack stack = event.getItemStack();
		
		addPlacementRuleTooltip(tooltip, stack);
		
		addRecipeTooltip(tooltip, stack);
		
		if (radiation_enabled_public) {
			addArmorRadiationTooltip(tooltip, stack);
			addRadiationTooltip(tooltip, stack);
			addFoodRadiationTooltip(tooltip, stack);
		}
	}
	
	// Placement Rule Tooltips
	
	@SideOnly(Side.CLIENT)
	private static void addPlacementRuleTooltip(List<String> tooltip, ItemStack stack) {
		RecipeInfo<BasicRecipe> recipeInfo = FissionPlacement.recipe_handler.getRecipeInfoFromInputs(Lists.newArrayList(stack), Collections.emptyList());
		BasicRecipe recipe = recipeInfo == null ? null : recipeInfo.recipe;
		if (recipe != null) {
			String rule = FissionPlacement.TOOLTIP_MAP.get(recipe.getPlacementRuleID());
			if (rule != null) {
				InfoHelper.infoFull(tooltip, TextFormatting.AQUA, FontRenderHelper.wrapString(rule, InfoHelper.MAXIMUM_TEXT_WIDTH));
			}
		}
		
		recipeInfo = TurbinePlacement.recipe_handler.getRecipeInfoFromInputs(Lists.newArrayList(stack), Collections.emptyList());
		recipe = recipeInfo == null ? null : recipeInfo.recipe;
		if (recipe != null) {
			String rule = TurbinePlacement.TOOLTIP_MAP.get(recipe.getPlacementRuleID());
			if (rule != null) {
				InfoHelper.infoFull(tooltip, TextFormatting.AQUA, FontRenderHelper.wrapString(rule, InfoHelper.MAXIMUM_TEXT_WIDTH));
			}
		}
	}
	
	// Recipe Tooltips
	
	@SideOnly(Side.CLIENT)
	private static void addRecipeTooltip(List<String> tooltip, ItemStack stack) {
		BasicRecipe recipe;
		
		Function<BasicRecipeHandler, BasicRecipe> itemRecipe = x -> {
			RecipeInfo<BasicRecipe> recipeInfo = x.getRecipeInfoFromInputs(Collections.singletonList(stack), Collections.emptyList());
			return recipeInfo == null ? null : recipeInfo.recipe;
		};
		
		recipe = itemRecipe.apply(NCRecipes.machine_diaphragm);
		if (recipe != null) {
			InfoHelper.infoFull(tooltip, new TextFormatting[] {TextFormatting.UNDERLINE, TextFormatting.LIGHT_PURPLE, TextFormatting.RED}, NCInfo.machineDiaphragmFixedInfo(recipe), TextFormatting.AQUA, NCInfo.machineDiaphragmInfo());
		}
		
		recipe = itemRecipe.apply(NCRecipes.machine_sieve_assembly);
		if (recipe != null) {
			InfoHelper.infoFull(tooltip, new TextFormatting[] {TextFormatting.UNDERLINE, TextFormatting.LIGHT_PURPLE}, NCInfo.machineSieveAssemblyFixedInfo(recipe), TextFormatting.AQUA, NCInfo.machineSieveAssemblyInfo());
		}
		
		BasicRecipe cathodeRecipe = itemRecipe.apply(NCRecipes.electrolyzer_cathode), anodeRecipe = itemRecipe.apply(NCRecipes.electrolyzer_anode);
		if (cathodeRecipe != null || anodeRecipe != null) {
			List<TextFormatting> fixedColors = Lists.newArrayList(TextFormatting.UNDERLINE);
			if (cathodeRecipe != null) {
				fixedColors.add(TextFormatting.LIGHT_PURPLE);
			}
			if (anodeRecipe != null) {
				fixedColors.add(TextFormatting.BLUE);
			}
			InfoHelper.infoFull(tooltip, fixedColors.toArray(new TextFormatting[0]), NCInfo.electrodeFixedInfo(cathodeRecipe, anodeRecipe), TextFormatting.AQUA, NCInfo.electrodeInfo());
		}
		
		recipe = itemRecipe.apply(NCRecipes.fission_moderator);
		if (recipe != null) {
			InfoHelper.infoFull(tooltip, new TextFormatting[] {TextFormatting.UNDERLINE, TextFormatting.GREEN, TextFormatting.LIGHT_PURPLE}, NCInfo.fissionModeratorFixedInfo(recipe), TextFormatting.AQUA, NCInfo.fissionModeratorInfo());
		}
		
		recipe = itemRecipe.apply(NCRecipes.fission_reflector);
		if (recipe != null) {
			InfoHelper.infoFull(tooltip, new TextFormatting[] {TextFormatting.UNDERLINE, TextFormatting.WHITE, TextFormatting.LIGHT_PURPLE}, NCInfo.fissionReflectorFixedInfo(recipe), TextFormatting.AQUA, NCInfo.fissionReflectorInfo());
		}
		
		recipe = itemRecipe.apply(NCRecipes.pebble_fission);
		if (recipe != null) {
			InfoHelper.infoFull(tooltip, new TextFormatting[] {TextFormatting.UNDERLINE, TextFormatting.GREEN, TextFormatting.YELLOW, TextFormatting.LIGHT_PURPLE, TextFormatting.RED, TextFormatting.AQUA, TextFormatting.GRAY, TextFormatting.DARK_AQUA}, NCInfo.fissionFuelInfo(recipe));
		}
		
		recipe = itemRecipe.apply(NCRecipes.solid_fission);
		if (recipe != null) {
			InfoHelper.infoFull(tooltip, new TextFormatting[] {TextFormatting.UNDERLINE, TextFormatting.GREEN, TextFormatting.YELLOW, TextFormatting.LIGHT_PURPLE, TextFormatting.RED, TextFormatting.AQUA, TextFormatting.GRAY, TextFormatting.DARK_AQUA}, NCInfo.fissionFuelInfo(recipe));
		}
	}
	
	// Radiation Tooltips
	
	private static final String RADIATION = Lang.localize("item.nuclearcraft.rads");
	private static final String RADIATION_RESISTANCE = Lang.localize("item.nuclearcraft.rad_resist");
	private static final String FOOD_RADIATION = Lang.localize("item.nuclearcraft.food_rads");
	
	@SideOnly(Side.CLIENT)
	private static void addArmorRadiationTooltip(List<String> tooltip, ItemStack stack) {
		if (stack.isEmpty() || !ArmorHelper.isArmor(stack.getItem(), radiation_horse_armor_public)) {
			return;
		}
		IRadiationResistance armorResistance = RadiationHelper.getRadiationResistance(stack);
		NBTTagCompound nbt = NBTHelper.getStackNBT(stack);
		boolean hasRadResistanceNBT = stack.hasTagCompound() && nbt.hasKey("ncRadiationResistance");
		if (armorResistance == null && !hasRadResistanceNBT) {
			return;
		}
		
		double resistance = 0D;
		if (armorResistance != null) {
			resistance += armorResistance.getTotalRadResistance();
		}
		if (hasRadResistanceNBT) {
			resistance += nbt.getDouble("ncRadiationResistance");
		}
		
		if (resistance > 0D) {
			tooltip.add(TextFormatting.AQUA + RADIATION_RESISTANCE + " " + RadiationHelper.resistanceSigFigs(resistance));
		}
	}
	
	@SideOnly(Side.CLIENT)
	private static void addRadiationTooltip(List<String> tooltip, ItemStack stack) {
		IRadiationSource stackRadiation = RadiationHelper.getRadiationSource(stack);
		if (stackRadiation == null || stackRadiation.getRadiationLevel() * stack.getCount() <= radiation_lowest_rate) {
			return;
		}
		tooltip.add(RadiationHelper.getRadiationTextColor(stackRadiation.getRadiationLevel() * stack.getCount()) + RADIATION + " " + RadiationHelper.radsPrefix(stackRadiation.getRadiationLevel() * stack.getCount(), true));
	}
	
	@SideOnly(Side.CLIENT)
	private static void addFoodRadiationTooltip(List<String> tooltip, ItemStack stack) {
		if (!(stack.getItem() instanceof ItemFood)) {
			return;
		}
		
		int packed = RecipeItemHelper.pack(stack);
		if (!RadSources.FOOD_RAD_MAP.containsKey(packed)) {
			return;
		}
		
		double rads = RadSources.FOOD_RAD_MAP.get(packed);
		double resistance = RadSources.FOOD_RESISTANCE_MAP.get(packed);
		
		if (rads != 0D || resistance != 0D) {
			tooltip.add(TextFormatting.UNDERLINE + FOOD_RADIATION);
		}
		if (rads != 0D) {
			tooltip.add(RadiationHelper.getFoodRadiationTextColor(rads) + RADIATION + " " + RadiationHelper.radsPrefix(rads, false));
		}
		if (resistance != 0D) {
			tooltip.add(RadiationHelper.getFoodResistanceTextColor(resistance) + RADIATION_RESISTANCE + " " + RadiationHelper.resistanceSigFigs(resistance));
		}
	}
}
