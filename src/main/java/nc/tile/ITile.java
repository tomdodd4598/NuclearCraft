package nc.tile;

import nc.capability.radiation.IRadiationSource;
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
	
	public IRadiationSource getRadiationSource();
	
	public default boolean shouldSaveRadiation() {
		return true;
	}
	
	public void setState(boolean isActive);
}
