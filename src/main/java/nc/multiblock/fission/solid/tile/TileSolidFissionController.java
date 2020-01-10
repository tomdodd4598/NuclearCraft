package nc.multiblock.fission.solid.tile;

import static nc.block.property.BlockProperties.FACING_ALL;

import nc.Global;
import nc.multiblock.cuboidal.CuboidalPartPositionType;
import nc.multiblock.fission.FissionReactor;
import nc.multiblock.fission.solid.block.BlockSolidFissionController;
import nc.multiblock.fission.tile.IFissionController;
import nc.multiblock.fission.tile.TileFissionPart;
import nc.util.RegistryHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileSolidFissionController extends TileFissionPart implements IFissionController {
	
	public TileSolidFissionController() {
		super(CuboidalPartPositionType.WALL);
	}
	
	@Override
	public String getLogicID() {
		return "solid_fuel";
	}
	
	@Override
	public void onMachineAssembled(FissionReactor controller) {
		doStandardNullControllerResponse(controller);
		super.onMachineAssembled(controller);
		if (!getWorld().isRemote && getPartPosition().getFacing() != null) {
			getWorld().setBlockState(getPos(), getWorld().getBlockState(getPos()).withProperty(FACING_ALL, getPartPosition().getFacing()), 2);
		}
	}
	
	@Override
	public void onMachineBroken() {
		super.onMachineBroken();
		//if (getWorld().isRemote) return;
		//getWorld().setBlockState(getPos(), getWorld().getBlockState(getPos()), 2);
	}
	
	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
		return oldState.getBlock() != newState.getBlock();
	}
	
	@Override
	public void onBlockNeighborChanged(IBlockState state, World world, BlockPos pos, BlockPos fromPos) {
		super.onBlockNeighborChanged(state, world, pos, fromPos);
		if (getMultiblock() != null) getMultiblock().updateActivity();
	}
	
	@Override
	public void updateBlockState(boolean isActive) {
		if (getBlockType() instanceof BlockSolidFissionController) {
			((BlockSolidFissionController)getBlockType()).setState(isActive, this);
			world.notifyNeighborsOfStateChange(pos, getBlockType(), true);
		}
	}
	
	@Override
	public void doMeltdown() {
		IBlockState corium = RegistryHelper.getBlock(Global.MOD_ID + ":fluid_corium").getDefaultState();
		world.removeTileEntity(pos);
		world.setBlockState(pos, corium);
	}
}
