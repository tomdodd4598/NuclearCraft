package nc.multiblock.turbine.low.block;

import nc.multiblock.turbine.block.BlockTurbineFrame;
import nc.multiblock.turbine.low.tile.TileLowTurbineFrame;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockLowTurbineFrame extends BlockTurbineFrame {

	public BlockLowTurbineFrame() {
		super("low_turbine_frame");
	}

	@Override
	public TileEntity createNewTileEntity(World world, int metadata) {
		return new TileLowTurbineFrame();
	}
}
