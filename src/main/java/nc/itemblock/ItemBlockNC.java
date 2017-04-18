package nc.itemblock;

import java.util.List;

import nc.util.InfoNC;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockNC extends ItemBlock {
	
	String[] info;

	public ItemBlockNC(Block block, String... lines) {
		super(block);
		String[] strings = new String[lines.length];
		for (int i = 0; i < lines.length; i++) {
			strings[i] = lines[i];
		}
		info = strings;
	}

	@SuppressWarnings({ "rawtypes" })
	public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean whatIsThis) {
        super.addInformation(itemStack, player, list, whatIsThis);
        if (info.length > 0) InfoNC.infoFull(list, info);
    }
}
