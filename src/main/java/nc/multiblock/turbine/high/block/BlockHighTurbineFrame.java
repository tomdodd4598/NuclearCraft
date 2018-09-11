package nc.multiblock.turbine.high.block;

import nc.multiblock.turbine.block.BlockTurbineFrame;
import nc.multiblock.turbine.high.tile.TileHighTurbineFrame;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockHighTurbineFrame extends BlockTurbineFrame {

	public BlockHighTurbineFrame() {
		super("high_turbine_frame");
	}

	@Override
	public TileEntity createNewTileEntity(World world, int metadata) {
		return new TileHighTurbineFrame();
	}
}
