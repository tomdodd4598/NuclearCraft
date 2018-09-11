package nc.multiblock.turbine.low.block;

import nc.multiblock.turbine.block.BlockTurbineController;
import nc.multiblock.turbine.low.tile.TileLowTurbineController;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockLowTurbineController extends BlockTurbineController<TileLowTurbineController> {
	
	public BlockLowTurbineController() {
		super(TileLowTurbineController.class, "low_turbine_controller");
	}
	
	@Override
	public TileEntity createNewTileEntity(World world, int metadata) {
		return new TileLowTurbineController();
	}
}
