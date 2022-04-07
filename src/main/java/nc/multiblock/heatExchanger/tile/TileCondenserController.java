package nc.multiblock.heatExchanger.tile;

import static nc.block.property.BlockProperties.FACING_ALL;

import nc.multiblock.container.ContainerCondenserController;
import nc.multiblock.cuboidal.CuboidalPartPositionType;
import nc.multiblock.heatExchanger.HeatExchanger;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileCondenserController extends TileHeatExchangerPart implements IHeatExchangerController<TileCondenserController> {
	
	public TileCondenserController() {
		super(CuboidalPartPositionType.WALL);
	}
	
	@Override
	public String getLogicID() {
		return "condenser";
	}
	
	@Override
	public void onMachineAssembled(HeatExchanger controller) {
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
	public ContainerCondenserController getContainer(EntityPlayer player) {
		return new ContainerCondenserController(player, this);
	}
	
	@Override
	public void onBlockNeighborChanged(IBlockState state, World worldIn, BlockPos posIn, BlockPos fromPos) {
		super.onBlockNeighborChanged(state, worldIn, posIn, fromPos);
		if (getMultiblock() != null) {
			getMultiblock().setIsHeatExchangerOn();
		}
	}
	
	@Override
	public int[] weakSidesToCheck(World worldIn, BlockPos posIn) {
		return new int[] {2, 3, 4, 5};
	}
}
