package nc.item;

import java.util.List;

import javax.annotation.Nullable;

import nc.Global;
import nc.NuclearCraft;
import nc.util.InfoHelper;
import nc.util.Lang;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemRecord;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class NCItemRecord extends ItemRecord {
	
	public final String[] info;
	private final String name;
	
	public NCItemRecord(String nameIn, SoundEvent sound, String... tooltip) {
		super("record_" + nameIn, sound);
		name = nameIn;
		setTranslationKey(Global.MOD_ID + "." + "record_" + nameIn);
		if (NuclearCraft.regName) setRegistryName(new ResourceLocation(Global.MOD_ID, "record_" + nameIn));
		info = InfoHelper.buildInfo(getTranslationKey(), tooltip);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack itemStack, @Nullable World world, List<String> tooltip, ITooltipFlag flag) {
		//super.addInformation(itemStack, player, tooltip, advanced);
		if (info.length > 0) InfoHelper.infoFull(tooltip, info);
	}

	@Override
	public EnumRarity getRarity(ItemStack stack) {
		return EnumRarity.EPIC;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public String getRecordNameLocal() {
		return Lang.localise("item." + Global.MOD_ID + ".record_" + name + ".des0");
	}
}