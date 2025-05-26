package nc.tile.machine;

import nc.handler.TileInfoHandler;
import nc.multiblock.cuboidal.CuboidalPartPositionType;
import nc.multiblock.machine.Machine;
import nc.tile.TileContainerInfo;
import nc.util.NCMath;
import net.minecraft.block.Block;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.*;

import static nc.block.property.BlockProperties.FACING_ALL;

public class TileDistillerController extends TileMachinePart implements IMachineController<TileDistillerController> {
	
	protected final TileContainerInfo<TileDistillerController> info = TileInfoHandler.getTileContainerInfo("distiller_controller");
	
	protected boolean isRenderer = false;
	
	protected final float[] brightnessArray = new float[] {1F, 1F, 1F, 1F, 1F, 1F, 1F, 1F};
	protected int brightnessIndex = 0;
	
	public TileDistillerController() {
		super(CuboidalPartPositionType.WALL);
	}
	
	@Override
	public String getLogicID() {
		return "distiller";
	}
	
	@Override
	public TileContainerInfo<TileDistillerController> getContainerInfo() {
		return info;
	}
	
	@Override
	public void onMachineAssembled(Machine multiblock) {
		doStandardNullControllerResponse(multiblock);
		super.onMachineAssembled(multiblock);
		if (!world.isRemote) {
			EnumFacing facing = getPartPosition().getFacing();
			if (facing != null) {
				world.setBlockState(pos, world.getBlockState(pos).withProperty(FACING_ALL, facing), 2);
			}
		}
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
	public boolean isRenderer() {
		return isRenderer;
	}
	
	@Override
	public void setIsRenderer(boolean isRenderer) {
		this.isRenderer = isRenderer;
	}
	
	@SideOnly(Side.CLIENT)
	public float nextRenderBrightness() {
		Machine machine = getMultiblock();
		if (machine == null) {
			return 1F;
		}
		
		brightnessArray[brightnessIndex] = world.getLightBrightness(machine.getExtremeInteriorCoord(NCMath.getBit(brightnessIndex, 0) == 1, NCMath.getBit(brightnessIndex, 1) == 1, NCMath.getBit(brightnessIndex, 2) == 1));
		brightnessIndex = (brightnessIndex + 1) % 8;
		
		return (brightnessArray[0] + brightnessArray[1] + brightnessArray[2] + brightnessArray[3] + brightnessArray[4] + brightnessArray[5] + brightnessArray[6] + brightnessArray[7]) / 8F;
	}
}
