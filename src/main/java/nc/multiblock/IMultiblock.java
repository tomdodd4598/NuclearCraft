package nc.multiblock;

import nc.multiblock.tile.ITileMultiblockPart;
import net.minecraft.world.World;

public interface IMultiblock<MULTIBLOCK extends Multiblock<MULTIBLOCK, T>, T extends ITileMultiblockPart<MULTIBLOCK, T>> {
	
	public World getWorld();
}
