package nc.block.item;

import java.util.List;

import nc.util.NCInfo;
import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

public class NCItemBlock extends ItemBlock {
	
	public final String[] info;

	public NCItemBlock(Block block, Object... tooltip) {
		super(block);
		
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
				strings[i] = I18n.translateToLocalFormatted(block.getUnlocalizedName() + ".des" + i);
			}
			info = strings;
		} else {
			String[] strings = {};
			info = strings;
		}
	}
	
	public void addInformation(ItemStack itemStack, World world, List<String> tooltip, ITooltipFlag flag) {
        super.addInformation(itemStack, world, tooltip, flag);
        if (info.length > 0) NCInfo.infoFull(tooltip, info);
    }
}
