package nc.multiblock.turbine.low.block;

import nc.multiblock.turbine.block.BlockTurbineWall;
import nc.multiblock.turbine.low.tile.TileLowTurbineWall;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockLowTurbineWall extends BlockTurbineWall {

	public BlockLowTurbineWall() {
		super("low_turbine_wall");
	}

	@Override
	public TileEntity createNewTileEntity(World world, int metadata) {
		return new TileLowTurbineWall();
	}
}
