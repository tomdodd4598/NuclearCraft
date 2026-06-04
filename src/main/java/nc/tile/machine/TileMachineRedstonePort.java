package nc.tile.machine;

import nc.multiblock.cuboidal.CuboidalPartPositionType;
import nc.multiblock.machine.Machine;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileMachineRedstonePort extends TileMachinePart {
	
	public TileMachineRedstonePort() {
		super(CuboidalPartPositionType.WALL);
	}
	
	@Override
	public void onMachineAssembled(Machine multiblock) {
		doStandardNullControllerResponse(multiblock);
		super.onMachineAssembled(multiblock);
	}
	
	@Override
	public void onBlockNeighborChanged(IBlockState state, World worldIn, BlockPos posIn, BlockPos fromPos) {
		super.onBlockNeighborChanged(state, worldIn, posIn, fromPos);
		setActivity(getIsRedstonePowered());
	}
}
