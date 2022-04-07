package nc.multiblock.fission.salt.tile;

import static nc.block.property.BlockProperties.FACING_ALL;

import java.util.Iterator;

import nc.multiblock.container.ContainerSaltFissionController;
import nc.multiblock.cuboidal.CuboidalPartPositionType;
import nc.multiblock.fission.FissionReactor;
import nc.multiblock.fission.tile.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidRegistry;

public class TileSaltFissionController extends TileFissionPart implements IFissionController<TileSaltFissionController> {
	
	public TileSaltFissionController() {
		super(CuboidalPartPositionType.WALL);
	}
	
	@Override
	public String getLogicID() {
		return "molten_salt";
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
	}
	
	@Override
	public ContainerSaltFissionController getContainer(EntityPlayer player) {
		return new ContainerSaltFissionController(player, this);
	}
	
	@Override
	public void onBlockNeighborChanged(IBlockState state, World worldIn, BlockPos posIn, BlockPos fromPos) {
		super.onBlockNeighborChanged(state, worldIn, posIn, fromPos);
		if (getMultiblock() != null) {
			getMultiblock().updateActivity();
		}
	}
	
	@Override
	public void doMeltdown(Iterator<IFissionController<?>> controllerIterator) {
		controllerIterator.remove();
		world.removeTileEntity(pos);
		
		IBlockState corium = FluidRegistry.getFluid("corium").getBlock().getDefaultState();
		world.setBlockState(pos, corium);
	}
}
