package nc.multiblock.fission.tile;

import javax.annotation.Nonnull;

import nc.config.NCConfig;
import nc.enumm.MetaEnums;
import nc.multiblock.cuboidal.CuboidalPartPositionType;
import nc.multiblock.cuboidal.PartPosition;
import nc.multiblock.fission.FissionReactor;
import nc.multiblock.fission.block.BlockFissionSource;
import nc.recipe.NCRecipes;
import nc.recipe.ProcessorRecipe;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class TileFissionSource extends TileFissionPart {
	
	protected final double efficiency;
	
	public EnumFacing facing = EnumFacing.DOWN;
	
	public TileFissionSource(double efficiency) {
		super(CuboidalPartPositionType.WALL);
		this.efficiency = efficiency;
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
	
	private TileFissionSource(MetaEnums.NeutronSourceType type) {
		this(type.getEfficiency());
	}
	
	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
		return oldState.getBlock() != newState.getBlock() || oldState.getBlock().getMetaFromState(oldState) != newState.getBlock().getMetaFromState(newState);
	}
	
	@Override
	public void onMachineAssembled(FissionReactor controller) {
		doStandardNullControllerResponse(controller);
		super.onMachineAssembled(controller);
		//if (getWorld().isRemote) return;
	}
	
	@Override
	public void onMachineBroken() {
		super.onMachineBroken();
		//if (getWorld().isRemote) return;
		//getWorld().setBlockState(getPos(), getWorld().getBlockState(getPos()), 2);
	}
	
	@Override
	public @Nonnull PartPosition getPartPosition() {
		PartPosition partPos = super.getPartPosition();
		if (partPos.getFacing() != null) {
			facing = partPos.getFacing();
		}
		return partPos;
	}
	
	@Override
	public int[] weakSidesToCheck(World world, BlockPos pos) {
		return new int[] {2, 3, 4, 5};
	}
	
	@Override
	public void onAdded() {
		world.neighborChanged(pos, getBlockType(), pos);
		super.onAdded();
	}
	
	@Override
	public void onBlockNeighborChanged(IBlockState state, World world, BlockPos pos, BlockPos fromPos) {
		boolean wasRedstonePowered = getIsRedstonePowered();
		super.onBlockNeighborChanged(state, world, pos, fromPos);
		updateBlockState(getIsRedstonePowered());
		if (!world.isRemote && wasRedstonePowered != getIsRedstonePowered()) {
			getLogic().onSourceUpdated(this);
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
		EnumFacing facing = getPartPosition().getFacing(), dir = facing.getOpposite();
		for (int i = NCConfig.fission_min_size; i <= NCConfig.fission_max_size; i++) {
			BlockPos offPos = pos.offset(dir, i);
			ProcessorRecipe blockRecipe = blockRecipe(NCRecipes.fission_reflector, offPos);
			if (blockRecipe != null && blockRecipe.getFissionReflectorReflectivity() >= 1D) {
				return null;
			}
			IFissionComponent component = getMultiblock().getPartMap(IFissionComponent.class).get(offPos.toLong());
			// First check if source is blocked by a flux sink
			if (component instanceof IFissionFluxSink && ((IFissionFluxSink)component).isNullifyingSources(facing)) {
				return null;
			}
			if (component instanceof IFissionFuelComponent) {
				IFissionFuelComponent fuelComponent = (IFissionFuelComponent) component;
				if (fuelComponent.isAcceptingFlux(facing)) {
					double oldSourceEfficiency = fuelComponent.getSourceEfficiency();
					fuelComponent.setSourceEfficiency(efficiency, true);
					return new PrimingTargetInfo(fuelComponent, oldSourceEfficiency != fuelComponent.getSourceEfficiency());
				}
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
	
	@Override
	public NBTTagCompound writeAll(NBTTagCompound nbt) {
		super.writeAll(nbt);
		nbt.setInteger("facing", facing.getIndex());
		return nbt;
	}
	
	@Override
	public void readAll(NBTTagCompound nbt) {
		super.readAll(nbt);
		facing = EnumFacing.byIndex(nbt.getInteger("facing"));
	}
}
