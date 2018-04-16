package nc.item;

import java.util.List;

import nc.Global;
import nc.util.InfoHelper;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class NCItem extends Item {
	
	public final TextFormatting fixedColor;
	public final String[] fixedInfo;
	public final String[] info;
	
	public NCItem(String nameIn, TextFormatting fixedColor, String[] fixedTooltip, String... tooltip) {
		setUnlocalizedName(Global.MOD_ID + "." + nameIn);
		setRegistryName(new ResourceLocation(Global.MOD_ID, nameIn));
		this.fixedColor = fixedColor;
		fixedInfo = InfoHelper.buildFixedInfo(getUnlocalizedName(), fixedTooltip);
		info = InfoHelper.buildInfo(getUnlocalizedName(), tooltip);
	}
	
	public NCItem(String nameIn, TextFormatting fixedColor, String... tooltip) {
		this(nameIn, fixedColor, InfoHelper.EMPTY_ARRAY, tooltip);
	}
	
	public NCItem(String nameIn, String[] fixedTooltip, String... tooltip) {
		this(nameIn, TextFormatting.AQUA, fixedTooltip, tooltip);
	}
	
	public NCItem(String nameIn, String... tooltip) {
		this(nameIn, InfoHelper.EMPTY_ARRAY, tooltip);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack itemStack, World world, List<String> tooltip, ITooltipFlag flag) {
        super.addInformation(itemStack, world, tooltip, flag);
        if (info.length + fixedInfo.length > 0) InfoHelper.infoFull(tooltip, fixedColor, fixedInfo, info);
    }
}
