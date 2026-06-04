package nc.container;

import nc.network.NCPacket;
import nc.tile.*;
import net.minecraft.tileentity.TileEntity;

public abstract class ContainerInfoTile<TILE extends TileEntity & ITileGui<TILE, PACKET, INFO>, PACKET extends NCPacket, INFO extends TileContainerInfo<TILE>> extends NCContainer {
	
	protected final INFO info;
	
	protected ContainerInfoTile(TILE tile) {
		super(tile);
		info = tile.getContainerInfo();
	}
}
