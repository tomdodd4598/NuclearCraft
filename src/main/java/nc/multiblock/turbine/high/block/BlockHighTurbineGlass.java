package nc.multiblock.turbine.high.block;

import nc.multiblock.turbine.block.BlockTurbineGlass;
import nc.multiblock.turbine.high.tile.TileHighTurbineGlass;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockHighTurbineGlass extends BlockTurbineGlass {

	public BlockHighTurbineGlass() {
		super("high_turbine_glass");
	}

	@Override
	public TileEntity createNewTileEntity(World world, int metadata) {
		return new TileHighTurbineGlass();
	}
}
