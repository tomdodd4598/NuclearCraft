package nc.block.fission.port;

import nc.tile.fission.TileSaltFissionVessel;
import nc.tile.fission.port.TileFissionVesselPort;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockFissionVesselPort extends BlockFissionFluidPort<TileFissionVesselPort, TileSaltFissionVessel> {
	
	public BlockFissionVesselPort() {
		super(TileFissionVesselPort.class);
	}
	
	@Override
	public TileEntity createNewTileEntity(World world, int metadata) {
		return new TileFissionVesselPort();
	}
}
