package nc.multiblock.fission.block.port;

import nc.multiblock.fission.tile.TileFissionIrradiator;
import nc.multiblock.fission.tile.port.TileFissionIrradiatorPort;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockFissionIrradiatorPort extends BlockFissionItemPort<TileFissionIrradiatorPort, TileFissionIrradiator> {

	public BlockFissionIrradiatorPort() {
		super(TileFissionIrradiatorPort.class, 300);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int metadata) {
		return new TileFissionIrradiatorPort();
	}
}
