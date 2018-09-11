package nc.multiblock.turbine.high.block;

import nc.multiblock.turbine.block.BlockTurbineController;
import nc.multiblock.turbine.high.tile.TileHighTurbineController;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockHighTurbineController extends BlockTurbineController<TileHighTurbineController> {
	
	public BlockHighTurbineController() {
		super(TileHighTurbineController.class, "high_turbine_controller");
	}
	
	@Override
	public TileEntity createNewTileEntity(World world, int metadata) {
		return new TileHighTurbineController();
	}
}
