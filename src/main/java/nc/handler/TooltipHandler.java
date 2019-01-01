package nc.handler;

import java.util.List;

import nc.Global;
import nc.NCInfo;
import nc.capability.radiation.IDefaultRadiationResistance;
import nc.capability.radiation.IRadiationSource;
import nc.config.NCConfig;
import nc.util.ArmorHelper;
import nc.util.InfoHelper;
import nc.util.Lang;
import nc.util.OreDictHelper;
import nc.util.RadiationHelper;
import nc.util.UnitHelper;
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
		if (stack == null || stack.isEmpty()) return;
		
		if (!stack.getItem().getRegistryName().getResourceDomain().equals(Global.MOD_ID)) {
			List<String> oreNames = OreDictHelper.getOreNames(stack);
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
		}
	}
	
	private static final String RADIATION = Lang.localise("item.nuclearcraft.rads");
	private static final String RADIATION_RESISTANCE = Lang.localise("item.nuclearcraft.rad_resist");
	
	@SideOnly(Side.CLIENT)
	private static void addRadiationTooltip(List<String> tooltip, ItemStack stack) {
		if (!stack.hasCapability(IRadiationSource.CAPABILITY_RADIATION_SOURCE, null)) return;
		IRadiationSource stackRadiation = stack.getCapability(IRadiationSource.CAPABILITY_RADIATION_SOURCE, null);
		if (stackRadiation == null) return;
		tooltip.add(RadiationHelper.getRadiationTextColor(stackRadiation) + RADIATION + " " + UnitHelper.prefix(stackRadiation.getRadiationLevel()*stack.getCount(), 3, "Rads/t", 0, -8));
		return;
	}
	
	@SideOnly(Side.CLIENT)
	private static void addArmorRadiationTooltip(List<String> tooltip, ItemStack stack) {
		if (!ArmorHelper.isArmor(stack.getItem(), true)) return;
		boolean capability = stack.hasCapability(IDefaultRadiationResistance.CAPABILITY_DEFAULT_RADIATION_RESISTANCE, null);
		boolean nbt = stack.hasTagCompound() && stack.getTagCompound().hasKey("ncRadiationResistance");
		if (!capability && !nbt) return;
		
		double resistance = 0D;
		if (capability) {
			IDefaultRadiationResistance armorResistance = stack.getCapability(IDefaultRadiationResistance.CAPABILITY_DEFAULT_RADIATION_RESISTANCE, null);
			if (armorResistance != null) resistance += armorResistance.getRadiationResistance();
		}
		if (nbt) {
			resistance += stack.getTagCompound().getDouble("ncRadiationResistance");
		}
		
		if (resistance > 0D) tooltip.add(TextFormatting.AQUA + RADIATION_RESISTANCE + " " + resistance);
	}
}
