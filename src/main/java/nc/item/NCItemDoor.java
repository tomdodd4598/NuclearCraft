package nc.item;

import nc.Global;
import net.minecraft.block.Block;
import net.minecraft.item.ItemDoor;
import net.minecraft.util.ResourceLocation;

public class NCItemDoor extends ItemDoor {

	public NCItemDoor(String unlocalizedName, String registryName, Block block) {
		super(block);
		setUnlocalizedName(unlocalizedName);
		setRegistryName(new ResourceLocation(Global.MOD_ID, registryName));
	}
}
