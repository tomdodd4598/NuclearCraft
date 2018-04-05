package nc.multiblock.fission.moltensalt.block;

import nc.multiblock.fission.moltensalt.tile.TileSaltFissionVessel;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockSaltFissionVessel extends BlockSaltFissionPartBase {

	public BlockSaltFissionVessel() {
		super("salt_fission_vessel");
	}

	@Override
	public TileEntity createNewTileEntity(World world, int metadata) {
		return new TileSaltFissionVessel();
	}
}
