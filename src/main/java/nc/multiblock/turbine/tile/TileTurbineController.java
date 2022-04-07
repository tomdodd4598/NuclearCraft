package nc.multiblock.turbine.tile;

import static nc.block.property.BlockProperties.FACING_ALL;

import nc.multiblock.container.ContainerTurbineController;
import nc.multiblock.cuboidal.CuboidalPartPositionType;
import nc.multiblock.turbine.Turbine;
import nc.util.NCMath;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.*;

public class TileTurbineController extends TileTurbinePart implements ITurbineController<TileTurbineController> {
	
	private boolean isRenderer = false;
	
	public TileTurbineController() {
		super(CuboidalPartPositionType.WALL);
	}
	
	@Override
	public String getLogicID() {
		return "turbine";
	}
	
	@Override
	public void onMachineAssembled(Turbine controller) {
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
	public ContainerTurbineController getContainer(EntityPlayer player) {
		return new ContainerTurbineController(player, this);
	}
	
	@Override
	public int[] weakSidesToCheck(World worldIn, BlockPos posIn) {
		return new int[] {2, 3, 4, 5};
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public AxisAlignedBB getRenderBoundingBox() {
		if (!isRenderer || !isMultiblockAssembled()) {
			return Block.FULL_BLOCK_AABB.offset(pos);
		}
		return new AxisAlignedBB(getMultiblock().getMinimumCoord(), getMultiblock().getMaximumCoord());
	}
	
	@Override
	public double getDistanceSq(double x, double y, double z) {
		double dX, dY, dZ;
		if (!isRenderer || !isMultiblockAssembled()) {
			dX = pos.getX() + 0.5D - x;
			dY = pos.getY() + 0.5D - y;
			dZ = pos.getZ() + 0.5D - z;
		}
		else {
			dX = getMultiblock().getMiddleX() + 0.5D - x;
			dY = getMultiblock().getMiddleY() + 0.5D - y;
			dZ = getMultiblock().getMiddleZ() + 0.5D - z;
		}
		return dX * dX + dY * dY + dZ * dZ;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public double getMaxRenderDistanceSquared() {
		double defaultDistSq = super.getMaxRenderDistanceSquared();
		if (!isRenderer || !isMultiblockAssembled()) {
			return defaultDistSq;
		}
		return defaultDistSq + (NCMath.sq(getMultiblock().getExteriorLengthX()) + NCMath.sq(getMultiblock().getExteriorLengthY()) + NCMath.sq(getMultiblock().getExteriorLengthZ())) / 4D;
	}
	
	@Override
	public void onBlockNeighborChanged(IBlockState state, World worldIn, BlockPos posIn, BlockPos fromPos) {
		super.onBlockNeighborChanged(state, worldIn, posIn, fromPos);
		if (getMultiblock() != null) {
			getLogic().setIsTurbineOn();
		}
	}
	
	@Override
	public boolean isRenderer() {
		return isRenderer;
	}
	
	@Override
	public void setIsRenderer(boolean isRenderer) {
		this.isRenderer = isRenderer;
	}
}
