package nc.multiblock.turbine.high.block;

import nc.multiblock.turbine.block.BlockTurbineWall;
import nc.multiblock.turbine.high.tile.TileHighTurbineWall;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockHighTurbineWall extends BlockTurbineWall {

	public BlockHighTurbineWall() {
		super("high_turbine_wall");
	}

	@Override
	public TileEntity createNewTileEntity(World world, int metadata) {
		return new TileHighTurbineWall();
	}
}
