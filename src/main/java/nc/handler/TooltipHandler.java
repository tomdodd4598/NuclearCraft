package nc.handler;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Lists;

import nc.NCInfo;
import nc.capability.radiation.resistance.IRadiationResistance;
import nc.capability.radiation.source.IRadiationSource;
import nc.config.NCConfig;
import nc.radiation.RadSources;
import nc.radiation.RadiationHelper;
import nc.recipe.NCRecipes;
import nc.recipe.ProcessorRecipe;
import nc.recipe.RecipeInfo;
import nc.util.ArmorHelper;
import nc.util.InfoHelper;
import nc.util.Lang;
import net.minecraft.client.util.RecipeItemHelper;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TooltipHandler {
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void addAdditionalTooltips(ItemTooltipEvent event) {
		ItemStack stack = event.getItemStack();
		RecipeInfo<ProcessorRecipe> recipeInfo = NCRecipes.pebble_fission.getRecipeInfoFromInputs(Lists.newArrayList(stack), new ArrayList<>());
		ProcessorRecipe recipe = recipeInfo == null ? null : recipeInfo.getRecipe();
		if (recipe != null) {
			InfoHelper.infoFull(event.getToolTip(), new TextFormatting[] {}, InfoHelper.EMPTY_ARRAY, new TextFormatting[] {TextFormatting.UNDERLINE, TextFormatting.GREEN, TextFormatting.YELLOW, TextFormatting.LIGHT_PURPLE, TextFormatting.RED, TextFormatting.DARK_AQUA}, NCInfo.fissionFuelInfo(recipe));
		}
		
		recipeInfo = NCRecipes.solid_fission.getRecipeInfoFromInputs(Lists.newArrayList(stack), new ArrayList<>());
		recipe = recipeInfo == null ? null : recipeInfo.getRecipe();
		if (recipe != null) {
			InfoHelper.infoFull(event.getToolTip(), new TextFormatting[] {}, InfoHelper.EMPTY_ARRAY, new TextFormatting[] {TextFormatting.UNDERLINE, TextFormatting.GREEN, TextFormatting.YELLOW, TextFormatting.LIGHT_PURPLE, TextFormatting.RED, TextFormatting.DARK_AQUA}, NCInfo.fissionFuelInfo(recipe));
		}
		
		recipeInfo = NCRecipes.fission_moderator.getRecipeInfoFromInputs(Lists.newArrayList(stack), new ArrayList<>());
		recipe = recipeInfo == null ? null : recipeInfo.getRecipe();
		if (recipe != null) {
			InfoHelper.infoFull(event.getToolTip(), new TextFormatting[] {TextFormatting.UNDERLINE, TextFormatting.GREEN, TextFormatting.LIGHT_PURPLE}, NCInfo.fissionModeratorFixedInfo(recipe), TextFormatting.AQUA, NCInfo.fissionModeratorInfo());
		}
		
		recipeInfo = NCRecipes.fission_reflector.getRecipeInfoFromInputs(Lists.newArrayList(stack), new ArrayList<>());
		recipe = recipeInfo == null ? null : recipeInfo.getRecipe();
		if (recipe != null) {
			InfoHelper.infoFull(event.getToolTip(), new TextFormatting[] {TextFormatting.UNDERLINE, TextFormatting.WHITE, TextFormatting.LIGHT_PURPLE}, NCInfo.fissionReflectorFixedInfo(recipe), TextFormatting.AQUA, NCInfo.fissionReflectorInfo());
		}
		
		if (NCConfig.radiation_enabled_public) {
			addArmorRadiationTooltip(event.getToolTip(), stack);
			addRadiationTooltip(event.getToolTip(), stack);
			addFoodRadiationTooltip(event.getToolTip(), stack);
		}
	}
	
	private static final String RADIATION = Lang.localise("item.nuclearcraft.rads");
	private static final String RADIATION_RESISTANCE = Lang.localise("item.nuclearcraft.rad_resist");
	private static final String FOOD_RADIATION = Lang.localise("item.nuclearcraft.food_rads");
	
	@SideOnly(Side.CLIENT)
	private static void addArmorRadiationTooltip(List<String> tooltip, ItemStack stack) {
		if (!ArmorHelper.isArmor(stack.getItem(), NCConfig.radiation_horse_armor_public)) return;
		IRadiationResistance armorResistance = RadiationHelper.getRadiationResistance(stack);
		boolean nbt = stack.hasTagCompound() && stack.getTagCompound().hasKey("ncRadiationResistance");
		if (armorResistance == null && !nbt) return;
		
		double resistance = 0D;
		if (armorResistance != null) {
			resistance += armorResistance.getRadiationResistance();
		}
		if (nbt) {
			resistance += stack.getTagCompound().getDouble("ncRadiationResistance");
		}
		
		if (resistance > 0D) tooltip.add(TextFormatting.AQUA + RADIATION_RESISTANCE + " " + RadiationHelper.resistanceSigFigs(resistance));
	}
	
	@SideOnly(Side.CLIENT)
	private static void addRadiationTooltip(List<String> tooltip, ItemStack stack) {
		IRadiationSource stackRadiation = RadiationHelper.getRadiationSource(stack);
		if (stackRadiation == null || stackRadiation.getRadiationLevel()*stack.getCount() <= NCConfig.radiation_lowest_rate) return;
		tooltip.add(RadiationHelper.getRadiationTextColor(stackRadiation.getRadiationLevel()*stack.getCount()) + RADIATION + " " + RadiationHelper.radsPrefix(stackRadiation.getRadiationLevel()*stack.getCount(), true));
		return;
	}
	
	@SideOnly(Side.CLIENT)
	private static void addFoodRadiationTooltip(List<String> tooltip, ItemStack stack) {
		if (!(stack.getItem() instanceof ItemFood)) return;
		
		int packed = RecipeItemHelper.pack(stack);
		if (!RadSources.FOOD_RAD_MAP.containsKey(packed)) return;
		
		double rads = RadSources.FOOD_RAD_MAP.get(packed);
		double resistance = RadSources.FOOD_RESISTANCE_MAP.get(packed);
		
		if (rads != 0D || resistance != 0D) {
			tooltip.add(TextFormatting.UNDERLINE + FOOD_RADIATION);
		}
		if (rads != 0D) tooltip.add(RadiationHelper.getFoodRadiationTextColor(rads) + RADIATION + " " + RadiationHelper.radsPrefix(rads, false));
		if (resistance != 0D) tooltip.add(RadiationHelper.getFoodResistanceTextColor(resistance) + RADIATION_RESISTANCE + " " + RadiationHelper.resistanceSigFigs(resistance));
		return;
	}
}
