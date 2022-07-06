package nc.multiblock.fission.tile;

import static nc.config.NCConfig.fission_max_size;

import javax.annotation.Nonnull;

import nc.enumm.MetaEnums;
import nc.multiblock.cuboidal.*;
import nc.multiblock.fission.*;
import nc.recipe.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileFissionSource extends TileFissionPart {
	
	protected double efficiency;
	
	public EnumFacing facing = EnumFacing.DOWN;
	
	/** Don't use this constructor! */
	public TileFissionSource() {
		super(CuboidalPartPositionType.WALL);
	}
	
	public TileFissionSource(double efficiency) {
		this();
		this.efficiency = efficiency;
	}
	
	protected static class Meta extends TileFissionSource {
		
		protected Meta(MetaEnums.NeutronSourceType type) {
			super(type.getEfficiency());
		}
		
		@Override
		public boolean shouldRefresh(World worldIn, BlockPos posIn, IBlockState oldState, IBlockState newState) {
			return oldState.getBlock() != newState.getBlock() || oldState.getBlock().getMetaFromState(oldState) != newState.getBlock().getMetaFromState(newState);
		}
	}
	
	public static class RadiumBeryllium extends Meta {
		
		public RadiumBeryllium() {
			super(MetaEnums.NeutronSourceType.RADIUM_BERYLLIUM);
		}
	}
	
	public static class PoloniumBeryllium extends Meta {
		
		public PoloniumBeryllium() {
			super(MetaEnums.NeutronSourceType.POLONIUM_BERYLLIUM);
		}
	}
	
	public static class Californium extends Meta {
		
		public Californium() {
			super(MetaEnums.NeutronSourceType.CALIFORNIUM);
		}
	}
	
	@Override
	public void onMachineAssembled(FissionReactor controller) {
		doStandardNullControllerResponse(controller);
		super.onMachineAssembled(controller);
	}
	
	@Override
	public void onMachineBroken() {
		super.onMachineBroken();
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
	public int[] weakSidesToCheck(World worldIn, BlockPos posIn) {
		return new int[] {2, 3, 4, 5};
	}
	
	/*@Override
	public void onLoad() {
		world.neighborChanged(pos, getBlockType(), pos);
		super.onLoad();
	}*/
	
	@Override
	public void onBlockNeighborChanged(IBlockState state, World worldIn, BlockPos posIn, BlockPos fromPos) {
		boolean wasRedstonePowered = getIsRedstonePowered();
		super.onBlockNeighborChanged(state, worldIn, posIn, fromPos);
		setActivity(getIsRedstonePowered());
		if (!worldIn.isRemote && wasRedstonePowered != getIsRedstonePowered()) {
			FissionReactorLogic logic = getLogic();
			if (logic != null) {
				logic.onSourceUpdated(this);
			}
		}
	}
	
	public PrimingTargetInfo getPrimingTarget(boolean simulate) {
		EnumFacing posFacing = getPartPosition().getFacing();
		if (posFacing == null) {
			posFacing = facing;
			if (posFacing == null) {
				return null;
			}
		}
		EnumFacing dir = posFacing.getOpposite();
		for (int i = 1; i <= fission_max_size; ++i) {
			BlockPos offPos = pos.offset(dir, i);
			BasicRecipe blockRecipe = RecipeHelper.blockRecipe(NCRecipes.fission_reflector, world, offPos);
			if (blockRecipe != null && blockRecipe.getFissionReflectorReflectivity() >= 1D) {
				return null;
			}
			IFissionComponent component = getMultiblock().getPartMap(IFissionComponent.class).get(offPos.toLong());
			// First check if source is blocked by a flux sink
			if (component != null && component.isNullifyingSources(posFacing)) {
				return null;
			}
			if (component instanceof IFissionFuelComponent) {
				IFissionFuelComponent fuelComponent = (IFissionFuelComponent) component;
				if (simulate) {
					return new PrimingTargetInfo(fuelComponent, false);
				}
				else if (fuelComponent.isAcceptingFlux(posFacing)) {
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
		nbt.setDouble("efficiency", efficiency);
		return nbt;
	}
	
	@Override
	public void readAll(NBTTagCompound nbt) {
		super.readAll(nbt);
		facing = EnumFacing.byIndex(nbt.getInteger("facing"));
		if (nbt.hasKey("efficiency")) {
			efficiency = nbt.getDouble("efficiency");
		}
	}
}
