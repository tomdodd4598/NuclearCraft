package nc.handler;

import java.util.List;

import nc.Global;
import nc.NCInfo;
import nc.capability.radiation.IRadiationSource;
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
		
		addArmorRadiationTooltip(event.getToolTip(), stack);
		
		if (addRadiationTooltip(event.getToolTip(), stack)) return;
		
		if (stack.getItem().getRegistryName().getResourceDomain().equals(Global.MOD_ID)) return;
		int[] oreIDs = OreDictionary.getOreIDs(stack);
		if (oreIDs.length == 0) return;
		if (OreDictionary.getOreName(oreIDs[0]).equals("blockGraphite")) {
			InfoHelper.infoFull(event.getToolTip(), TextFormatting.AQUA, NCInfo.ingotBlockFixedInfo()[8], NCInfo.ingotBlockInfo()[8]);
			return;
		}
		else if (OreDictionary.getOreName(oreIDs[0]).equals("blockBeryllium")) {
			InfoHelper.infoFull(event.getToolTip(), TextFormatting.AQUA, NCInfo.ingotBlockFixedInfo()[9], NCInfo.ingotBlockInfo()[9]);
			return;
		}
	}
	
	private static final String RADIATION = Lang.localise("item.nuclearcraft.rads");
	private static final String RADIATION_RESISTANCE = Lang.localise("item.nuclearcraft.rad_resist");
	
	@SideOnly(Side.CLIENT)
	private static boolean addRadiationTooltip(List<String> tooltip, ItemStack stack) {
		if (!stack.hasCapability(IRadiationSource.CAPABILITY_RADIATION_SOURCE, null)) return false;
		IRadiationSource stackRadiation = stack.getCapability(IRadiationSource.CAPABILITY_RADIATION_SOURCE, null);
		tooltip.add(RadiationHelper.getRadiationTextColor(stackRadiation) + RADIATION + " " + UnitHelper.prefix(stackRadiation.getRadiationLevel()*stack.getCount(), 3, "Rads/t", 0, -8));
		return true;
	}
	
	@SideOnly(Side.CLIENT)
	private static void addArmorRadiationTooltip(List<String> tooltip, ItemStack stack) {
		if (stack.hasTagCompound()) if (stack.getTagCompound().hasKey("ncRadiationResistance")) {
			tooltip.add(TextFormatting.AQUA + RADIATION_RESISTANCE + " " + stack.getTagCompound().getDouble("ncRadiationResistance"));
		}
	}
}
