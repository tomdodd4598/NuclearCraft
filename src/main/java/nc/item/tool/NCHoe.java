package nc.item.tool;

import java.util.List;

import nc.Global;
import nc.util.InfoHelper;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class NCHoe extends ItemHoe {
	
	String[] info;

	public NCHoe(String unlocalizedName, ToolMaterial material, String... tooltip) {
		super(material);
		setUnlocalizedName(Global.MOD_ID + "." + unlocalizedName);
		setRegistryName(new ResourceLocation(Global.MOD_ID, unlocalizedName));
		info = InfoHelper.buildInfo(getUnlocalizedName(), tooltip);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack itemStack, World world, List<String> tooltip, ITooltipFlag flag) {
        super.addInformation(itemStack, world, tooltip, flag);
        if (info.length > 0) InfoHelper.infoFull(tooltip, info);
    }
}
