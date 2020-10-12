package nc.multiblock;

import it.unimi.dsi.fastutil.longs.*;
import nc.multiblock.network.MultiblockUpdatePacket;
import nc.multiblock.tile.ITileMultiblockPart;

public abstract class PartBunch<TYPE extends T, MULTIBLOCK extends Multiblock<T, PACKET>, T extends ITileMultiblockPart<MULTIBLOCK>, PACKET extends MultiblockUpdatePacket> {
	
	protected boolean initialized = false;
	
	protected final MULTIBLOCK multiblock;
	
	protected final Long2ObjectMap<TYPE> partMap = new Long2ObjectOpenHashMap<>();
	
	public PartBunch(MULTIBLOCK multiblock) {
		this.multiblock = multiblock;
	}
	
	public Long2ObjectMap<TYPE> getPartMap() {
		return partMap;
	}
	
	protected abstract void init();
}
