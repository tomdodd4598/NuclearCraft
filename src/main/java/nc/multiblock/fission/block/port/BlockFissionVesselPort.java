package nc.multiblock.fission.block.port;

import nc.multiblock.fission.salt.tile.TileSaltFissionVessel;
import nc.multiblock.fission.tile.port.TileFissionVesselPort;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockFissionVesselPort extends BlockFissionFluidPort<TileFissionVesselPort, TileSaltFissionVessel> {

	public BlockFissionVesselPort() {
		super(TileFissionVesselPort.class, 302);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int metadata) {
		return new TileFissionVesselPort();
	}
}
