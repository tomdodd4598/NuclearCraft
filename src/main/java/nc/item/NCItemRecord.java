package nc.item;

import java.util.List;

import javax.annotation.Nullable;

import nc.Global;
import nc.util.*;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.*;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.*;

public class NCItemRecord extends ItemRecord implements IInfoItem {
	
	private final String[] tooltip;
	public String[] info;
	private final String name;
	
	public NCItemRecord(String nameIn, SoundEvent sound, String... tooltip) {
		super(nameIn, sound);
		this.tooltip = tooltip;
		name = nameIn;
	}
	
	@Override
	public void setInfo() {
		info = InfoHelper.buildInfo(getTranslationKey(), tooltip);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack itemStack, @Nullable World world, List<String> currentTooltip, ITooltipFlag flag) {
		// super.addInformation(itemStack, player, currentTooltip, advanced);
		if (info.length > 0) {
			InfoHelper.infoFull(currentTooltip, TextFormatting.RED, InfoHelper.EMPTY_ARRAY, TextFormatting.AQUA, info);
		}
	}
	
	@Override
	public EnumRarity getRarity(ItemStack stack) {
		return EnumRarity.EPIC;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public String getRecordNameLocal() {
		return Lang.localise("item." + Global.MOD_ID + "." + name + ".des0");
	}
}
