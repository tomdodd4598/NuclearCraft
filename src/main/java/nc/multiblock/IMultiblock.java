package nc.multiblock;

import nc.tile.multiblock.ITileMultiblockPart;
import net.minecraft.world.World;

public interface IMultiblock<MULTIBLOCK extends Multiblock<MULTIBLOCK, T>, T extends ITileMultiblockPart<MULTIBLOCK, T>> {
	
	public World getWorld();
}
