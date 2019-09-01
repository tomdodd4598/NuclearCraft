package nc.item;

import net.minecraft.block.Block;
import net.minecraft.item.ItemDoor;

public class NCItemDoor extends ItemDoor implements IInfoItem {

	public NCItemDoor(Block block) {
		super(block);
	}
	
	@Override
	public void setInfo() {}
}
