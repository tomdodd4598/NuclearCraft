package nc.item;

import nc.Global;
import net.minecraft.block.Block;
import net.minecraft.item.ItemDoor;
import net.minecraft.util.ResourceLocation;

public class NCItemDoor extends ItemDoor {

	public NCItemDoor(String nameIn, Block block) {
		super(block);
		setUnlocalizedName(Global.MOD_ID + "." + nameIn);
		setRegistryName(new ResourceLocation(Global.MOD_ID, nameIn));
	}
}
