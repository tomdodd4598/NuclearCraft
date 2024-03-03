package nc.block.fission.port;

import nc.tile.fission.TileSolidFissionCell;
import nc.tile.fission.port.TileFissionCellPort;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockFissionCellPort extends BlockFissionItemPort<TileFissionCellPort, TileSolidFissionCell> {
	
	public BlockFissionCellPort() {
		super(TileFissionCellPort.class);
	}
	
	@Override
	public TileEntity createNewTileEntity(World world, int metadata) {
		return new TileFissionCellPort();
	}
}
