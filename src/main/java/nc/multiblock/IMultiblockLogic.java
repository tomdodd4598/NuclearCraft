package nc.multiblock;

import nc.multiblock.tile.ITileLogicMultiblockPart;
import net.minecraft.world.World;

public interface IMultiblockLogic<MULTIBLOCK extends Multiblock<MULTIBLOCK, T> & ILogicMultiblock<MULTIBLOCK, LOGIC, T>, LOGIC extends MultiblockLogic<MULTIBLOCK, LOGIC, T>, T extends ITileLogicMultiblockPart<MULTIBLOCK, LOGIC, T>> {
	
	public abstract String getID();
	
	public World getWorld();
}
