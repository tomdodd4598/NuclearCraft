package nc.multiblock.fission.tile;

import nc.config.NCConfig;
import nc.enumm.MetaEnums;
import nc.multiblock.cuboidal.CuboidalPartPositionType;
import nc.multiblock.fission.FissionReactor;
import nc.multiblock.fission.block.BlockFissionSource;
import nc.recipe.NCRecipes;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class TileFissionSource extends TileFissionPartBase {
	
	protected final MetaEnums.NeutronSourceType type;
	
	public TileFissionSource(MetaEnums.NeutronSourceType type) {
		super(CuboidalPartPositionType.WALL);
		this.type = type;
	}
	
	public static class RadiumBeryllium extends TileFissionSource {
		
		public RadiumBeryllium() {
			super(MetaEnums.NeutronSourceType.RADIUM_BERYLLIUM);
		}
	}
	
	public static class PoloniumBeryllium extends TileFissionSource {
		
		public PoloniumBeryllium() {
			super(MetaEnums.NeutronSourceType.POLONIUM_BERYLLIUM);
		}
	}
	
	public static class Californium extends TileFissionSource {
		
		public Californium() {
			super(MetaEnums.NeutronSourceType.CALIFORNIUM);
		}
	}
	
	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
		return oldState.getBlock() != newState.getBlock() || oldState.getBlock().getMetaFromState(oldState) != newState.getBlock().getMetaFromState(newState);
	}
	
	@Override
	public void onMachineAssembled(FissionReactor controller) {
		doStandardNullControllerResponse(controller);
		super.onMachineAssembled(controller);
		/*if (!getWorld().isRemote && getMultiblock() != null) {
			EnumFacing facing = pos.getX() == getMultiblock().getMaxX() ? EnumFacing.EAST : pos.getX() == getMultiblock().getMinX() ? EnumFacing.WEST : pos.getY() == getMultiblock().getMaxY() ? EnumFacing.UP : pos.getY() == getMultiblock().getMinY() ? EnumFacing.DOWN : pos.getZ() == getMultiblock().getMaxZ() ? EnumFacing.SOUTH : pos.getZ() == getMultiblock().getMinZ() ? EnumFacing.NORTH : null;
			if (facing != null) {
				getWorld().setBlockState(pos, world.getBlockState(pos).withProperty(FACING_ALL, facing), 2);
			}
		}*/
	}
	
	@Override
	public void onMachineBroken() {
		super.onMachineBroken();
		//if (getWorld().isRemote) return;
		//getWorld().setBlockState(getPos(), getWorld().getBlockState(getPos()), 2);
	}
	
	@Override
	public void onAdded() {
		if (!world.isRemote) {
			onBlockNeighborChanged(world.getBlockState(pos), world, pos, pos);
		}
		super.onAdded();
	}
	
	@Override
	public void onBlockNeighborChanged(IBlockState state, World world, BlockPos pos, BlockPos fromPos) {
		boolean wasRedstonePowered = getIsRedstonePowered();
		super.onBlockNeighborChanged(state, world, pos, fromPos);
		updateBlockState(getIsRedstonePowered());
		if (wasRedstonePowered != getIsRedstonePowered()) {
			getMultiblock().onSourceUpdated(this);
		}
	}
	
	public void updateBlockState(boolean isActive) {
		if (getBlockType() instanceof BlockFissionSource) {
			((BlockFissionSource)getBlockType()).setState(isActive, this);
			//world.notifyNeighborsOfStateChange(pos, getBlockType(), true);
		}
	}
	
	public PrimingTargetInfo getPrimingTarget() {
		if (getPartPosition().getFacing() == null) return null;
		EnumFacing dir = getPartPosition().getFacing().getOpposite();
		for (int i = NCConfig.fission_min_size; i <= NCConfig.fission_max_size; i++) {
			BlockPos offPos = pos.offset(dir, i);
			if (blockRecipe(NCRecipes.fission_reflector, offPos) != null) return null;
			TileEntity tile = world.getTileEntity(offPos);
			if (tile instanceof IFissionFuelComponent) {
				IFissionFuelComponent fuelComponent = (IFissionFuelComponent) tile;
				double oldSourceEfficiency = fuelComponent.getSourceEfficiency();
				fuelComponent.setSourceEfficiency(type.getEfficiency(), true);
				return new PrimingTargetInfo(fuelComponent, oldSourceEfficiency != fuelComponent.getSourceEfficiency());
			}
		}
		return null;
	}
	
	public static class PrimingTargetInfo {
		
		public final IFissionFuelComponent fuelComponent;
		public final boolean newSourceEfficiency;
		
		PrimingTargetInfo(IFissionFuelComponent fuelComponent, boolean newSourceEfficiency) {
			this.fuelComponent = fuelComponent;
			this.newSourceEfficiency = newSourceEfficiency;
		}
	}
}
