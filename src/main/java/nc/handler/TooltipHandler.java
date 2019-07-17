package nc.handler;

import java.util.List;
import java.util.Set;

import nc.Global;
import nc.NCInfo;
import nc.capability.radiation.resistance.IRadiationResistance;
import nc.capability.radiation.source.IRadiationSource;
import nc.config.NCConfig;
import nc.radiation.RadSources;
import nc.radiation.RadiationHelper;
import nc.util.ArmorHelper;
import nc.util.InfoHelper;
import nc.util.Lang;
import nc.util.OreDictHelper;
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
		final ItemStack stack = event.getItemStack();
		if (stack.isEmpty()) return;
		
		if (!stack.getItem().getRegistryName().getNamespace().equals(Global.MOD_ID)) {
			Set<String> oreNames = OreDictHelper.getOreNames(stack);
			if (!oreNames.isEmpty()) {
				if (oreNames.contains("blockGraphite")) {
					InfoHelper.infoFull(event.getToolTip(), TextFormatting.AQUA, NCInfo.ingotBlockFixedInfo()[8], NCInfo.ingotBlockInfo()[8]);
				}
				else if (oreNames.contains("blockBeryllium")) {
					InfoHelper.infoFull(event.getToolTip(), TextFormatting.AQUA, NCInfo.ingotBlockFixedInfo()[9], NCInfo.ingotBlockInfo()[9]);
				}
			}
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
		if (stackRadiation == null || stackRadiation.getRadiationLevel() <= 0D) return;
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
