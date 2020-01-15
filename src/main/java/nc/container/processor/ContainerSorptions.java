package nc.container.processor;

import nc.container.ContainerTile;
import nc.tile.ITileGui;

public class ContainerSorptions<T extends ITileGui> extends ContainerTile<T> {
	
	public ContainerSorptions(T tile) {
		super(tile);
	}
}
