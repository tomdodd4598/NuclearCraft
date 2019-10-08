package nc.item;

import java.util.List;

import javax.annotation.Nullable;

import nc.Global;
import nc.util.InfoHelper;
import nc.util.Lang;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemRecord;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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
	public void addInformation(ItemStack itemStack, @Nullable World world, List<String> tooltip, ITooltipFlag flag) {
		//super.addInformation(itemStack, player, tooltip, advanced);
		if (info.length > 0) InfoHelper.infoFull(tooltip, TextFormatting.RED, InfoHelper.EMPTY_ARRAY, TextFormatting.AQUA, info);
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