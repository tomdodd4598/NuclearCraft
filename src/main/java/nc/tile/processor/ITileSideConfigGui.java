package nc.tile.processor;

import nc.network.tile.TileUpdatePacket;
import nc.tile.ITileGui;

public interface ITileSideConfigGui<PACKET extends TileUpdatePacket> extends ITileGui<PACKET> {
	
	public int getSideConfigXOffset();
	
	public int getSideConfigYOffset();
}
