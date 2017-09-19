package nc.item;

import java.util.List;

import nc.Global;
import nc.util.NCInfo;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemRecord;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class NCItemRecord extends ItemRecord {
	
	public final String[] info;
	private final String name;
	
	public NCItemRecord(String unlocalizedName, String registryName, SoundEvent sound, Object... tooltip) {
		super("record_" + unlocalizedName, sound);
		name = unlocalizedName;
		setUnlocalizedName("record_" + unlocalizedName);
		setRegistryName(new ResourceLocation(Global.MOD_ID, "record_" + registryName));
		
		if (tooltip.length == 0) {
			String[] strings = {};
			info = strings;
		} else if (tooltip[0] instanceof String) {
			String[] strings = new String[tooltip.length];
			for (int i = 0; i < tooltip.length; i++) {
				strings[i] = (String) tooltip[i];
			}
			info = strings;
		} else if (tooltip[0] instanceof Integer) {
			String[] strings = new String[(int) tooltip[0]];
			for (int i = 0; i < (int) tooltip[0]; i++) {
				strings[i] = I18n.translateToLocalFormatted("item." + "record_" + unlocalizedName + ".des" + i);
			}
			info = strings;
		} else {
			String[] strings = {};
			info = strings;
		}
	}
	
	public void addInformation(ItemStack itemStack, EntityPlayer player, List<String> tooltip, boolean advanced) {
		//super.addInformation(itemStack, player, tooltip, advanced);
		if (info.length > 0) NCInfo.infoFull(tooltip, info);
	}

	public EnumRarity getRarity(ItemStack stack) {
		return EnumRarity.EPIC;
	}
	
	@SideOnly(Side.CLIENT)
	public String getRecordNameLocal() {
		return I18n.translateToLocal("item.record_" + name + ".des0");
	}
}