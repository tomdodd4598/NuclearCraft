package nc.multiblock.fission.block.port;

import nc.multiblock.fission.solid.tile.TileSolidFissionCell;
import nc.multiblock.fission.tile.port.TileFissionCellPort;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockFissionCellPort extends BlockFissionItemPort<TileFissionCellPort, TileSolidFissionCell> {

	public BlockFissionCellPort() {
		super(TileFissionCellPort.class, 301);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int metadata) {
		return new TileFissionCellPort();
	}
}
