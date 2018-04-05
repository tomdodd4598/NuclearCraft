package nc.multiblock.fission.moltensalt.block;

import nc.multiblock.fission.moltensalt.tile.TileSaltFissionHeater;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockSaltFissionHeater extends BlockSaltFissionPartBase {

	public BlockSaltFissionHeater() {
		super("salt_fission_heater");
	}

	@Override
	public TileEntity createNewTileEntity(World world, int metadata) {
		return new TileSaltFissionHeater();
	}
}
