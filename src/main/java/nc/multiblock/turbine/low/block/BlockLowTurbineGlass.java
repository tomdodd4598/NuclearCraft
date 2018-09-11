package nc.multiblock.turbine.low.block;

import nc.multiblock.turbine.block.BlockTurbineGlass;
import nc.multiblock.turbine.low.tile.TileLowTurbineGlass;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockLowTurbineGlass extends BlockTurbineGlass {

	public BlockLowTurbineGlass() {
		super("low_turbine_glass");
	}

	@Override
	public TileEntity createNewTileEntity(World world, int metadata) {
		return new TileLowTurbineGlass();
	}
}
