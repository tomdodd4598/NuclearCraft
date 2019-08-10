package nc.block.tile;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;

public interface IActivatable {
	
	public Block getBlockType(boolean active);
	
	public void setState(boolean isActive, TileEntity tile);
}
