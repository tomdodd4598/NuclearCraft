package nc.container.processor;

import nc.container.ContainerInfoTile;
import nc.network.tile.TileUpdatePacket;
import nc.tile.*;
import net.minecraft.tileentity.TileEntity;

public class ContainerSorptions<TILE extends TileEntity & ITileGui<TILE, PACKET, INFO>, PACKET extends TileUpdatePacket, INFO extends TileContainerInfo<TILE>> extends ContainerInfoTile<TILE, PACKET, INFO> {
	
	public ContainerSorptions(TILE tile) {
		super(tile);
	}
}
