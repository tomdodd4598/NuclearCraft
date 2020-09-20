package nc.multiblock;

import it.unimi.dsi.fastutil.longs.*;
import nc.multiblock.network.MultiblockUpdatePacket;
import nc.multiblock.tile.ITileMultiblockPart;

public class PartBundle<TYPE extends T, MULTIBLOCK extends Multiblock<T, PACKET>, T extends ITileMultiblockPart<MULTIBLOCK>, PACKET extends MultiblockUpdatePacket> {
	
	protected final MULTIBLOCK multiblock;
	protected int id;
	
	protected final Long2ObjectMap<TYPE> partMap = new Long2ObjectOpenHashMap<>();
	
	public PartBundle(MULTIBLOCK multiblock, int id) {
		this.multiblock = multiblock;
		this.id = id;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int newId) {
		id = newId;
	}
	
	public Long2ObjectMap<TYPE> getPartMap() {
		return partMap;
	}
}
