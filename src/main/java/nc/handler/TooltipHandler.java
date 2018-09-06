package nc.handler;

import java.util.List;

import nc.Global;
import nc.NCInfo;
import nc.capability.radiation.IRadiationSource;
import nc.config.NCConfig;
import nc.util.InfoHelper;
import nc.util.Lang;
import nc.util.RadiationHelper;
import nc.util.UnitHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

public class TooltipHandler {
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void addAdditionalTooltips(ItemTooltipEvent event) {
		ItemStack stack = event.getItemStack();
		if (event.getEntityPlayer() == null || event.getEntityPlayer().world == null || stack == null || stack.isEmpty()) return;
		
		if (!stack.getItem().getRegistryName().getResourceDomain().equals(Global.MOD_ID)) {
			int[] oreIDs = OreDictionary.getOreIDs(stack);
			if (oreIDs.length > 0) {
				if (OreDictionary.getOreName(oreIDs[0]).equals("blockGraphite")) {
					InfoHelper.infoFull(event.getToolTip(), TextFormatting.AQUA, NCInfo.ingotBlockFixedInfo()[8], NCInfo.ingotBlockInfo()[8]);
				}
				else if (OreDictionary.getOreName(oreIDs[0]).equals("blockBeryllium")) {
					InfoHelper.infoFull(event.getToolTip(), TextFormatting.AQUA, NCInfo.ingotBlockFixedInfo()[9], NCInfo.ingotBlockInfo()[9]);
				}
			}
		}
		
		if (NCConfig.radiation_enabled) {
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
		if (stack.hasTagCompound()) if (stack.getTagCompound().hasKey("ncRadiationResistance")) {
			tooltip.add(TextFormatting.AQUA + RADIATION_RESISTANCE + " " + stack.getTagCompound().getDouble("ncRadiationResistance"));
		}
	}
}
