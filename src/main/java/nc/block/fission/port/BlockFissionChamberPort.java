package nc.block.fission.port;

import nc.tile.fission.TilePebbleFissionChamber;
import nc.tile.fission.port.TileFissionChamberPort;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockFissionChamberPort extends BlockFissionItemPort<TileFissionChamberPort, TilePebbleFissionChamber> {
	
	public BlockFissionChamberPort() {
		super(TileFissionChamberPort.class);
	}
	
	@Override
	public TileEntity createNewTileEntity(World world, int metadata) {
		return new TileFissionChamberPort();
	}
}
