package nc.block.fission.port;

import nc.tile.fission.TileFissionIrradiator;
import nc.tile.fission.port.TileFissionIrradiatorPort;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockFissionIrradiatorPort extends BlockFissionItemPort<TileFissionIrradiatorPort, TileFissionIrradiator> {
	
	public BlockFissionIrradiatorPort() {
		super(TileFissionIrradiatorPort.class);
	}
	
	@Override
	public TileEntity createNewTileEntity(World world, int metadata) {
		return new TileFissionIrradiatorPort();
	}
}
