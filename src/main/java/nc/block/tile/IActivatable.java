package nc.block.tile;

import net.minecraft.tileentity.TileEntity;

public interface IActivatable {
	
	public void setState(boolean isActive, TileEntity tile);
}
