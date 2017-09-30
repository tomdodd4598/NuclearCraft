package nc.item.tool;

import java.util.List;

import nc.Global;
import nc.util.NCInfo;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class NCShovel extends ItemSpade {
	
	String[] info;

	public NCShovel(String unlocalizedName, ToolMaterial material, String... tooltip) {
		super(material);
		setUnlocalizedName(unlocalizedName);
		setRegistryName(new ResourceLocation(Global.MOD_ID, unlocalizedName));
		
		String[] strings = new String[tooltip.length];
		for (int i = 0; i < tooltip.length; i++) {
			strings[i] = tooltip[i];
		}
		info = strings;
	}
	
	public void addInformation(ItemStack itemStack, World world, List<String> tooltip, ITooltipFlag flag) {
        super.addInformation(itemStack, world, tooltip, flag);
        if (info.length > 0) NCInfo.infoFull(tooltip, info);
    }
}
