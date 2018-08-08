package nc.tile;

import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface ITile {
	
	public void tickTile();
	
	public boolean shouldTileCheck();
	
	public World getTileWorld();
	
	public BlockPos getTilePos();
	
	public default boolean isRedstonePowered() {
		return getTileWorld().isBlockPowered(getTilePos());
	}
	
	public void markTileDirty();
	
	public Block getTileBlockType();
	
	public void setState(boolean isActive);
}
