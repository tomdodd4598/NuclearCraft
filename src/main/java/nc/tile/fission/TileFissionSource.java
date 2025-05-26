package nc.tile.fission;

import it.unimi.dsi.fastutil.objects.*;
import nc.enumm.MetaEnums;
import nc.multiblock.cuboidal.*;
import nc.multiblock.fission.*;
import nc.recipe.*;
import nc.render.BlockHighlightTracker;
import nc.tile.fission.manager.*;
import nc.util.Lang;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

import static nc.config.NCConfig.fission_max_size;
import static nc.util.PosHelper.DEFAULT_NON;

public class TileFissionSource extends TileFissionPart implements IFissionManagerListener<TileFissionSourceManager, TileFissionSource> {
	
	public static final Object2DoubleMap<String> DYN_EFFICIENCY_MAP = new Object2DoubleOpenHashMap<>();
	
	protected String sourceType;
	
	protected double efficiency;
	
	public boolean isActive = false;
	public EnumFacing facing = EnumFacing.DOWN;
	
	protected BlockPos managerPos = DEFAULT_NON;
	protected TileFissionSourceManager manager = null;
	
	/**
	 * Don't use this constructor!
	 */
	public TileFissionSource() {
		super(CuboidalPartPositionType.WALL);
	}
	
	public TileFissionSource(String sourceType, double efficiency) {
		this();
		this.efficiency = efficiency;
	}
	
	public static class Meta extends TileFissionSource {
		
		protected Meta(MetaEnums.NeutronSourceType type) {
			super(type.getName(), type.getEfficiency());
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
	public void onMachineAssembled(FissionReactor multiblock) {
		doStandardNullControllerResponse(multiblock);
		super.onMachineAssembled(multiblock);
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
		boolean wasActive = isActive;
		super.onBlockNeighborChanged(state, worldIn, posIn, fromPos);
		isActive = isSourceActive();
		setActivity(isActive);
		if (!worldIn.isRemote && wasActive != isActive) {
			FissionReactorLogic logic = getLogic();
			if (logic != null) {
				logic.onSourceUpdated(this);
			}
		}
	}
	
	public boolean isSourceActive() {
		return (manager != null && manager.isManagerActive()) || getIsRedstonePowered();
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
			if (component instanceof IFissionFuelComponent fuelComponent) {
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
	
	// IFissionManagerListener
	
	@Override
	public BlockPos getManagerPos() {
		return managerPos;
	}
	
	@Override
	public void setManagerPos(BlockPos pos) {
		managerPos = pos;
	}
	
	@Override
	public TileFissionSourceManager getManager() {
		return manager;
	}
	
	@Override
	public void setManager(TileFissionSourceManager manager) {
		this.manager = manager;
	}
	
	@Override
	public boolean onManagerRefresh(TileFissionSourceManager manager) {
		this.manager = manager;
		if (manager != null) {
			managerPos = manager.getPos();
			boolean wasActive = isActive;
			isActive = isSourceActive();
			if (wasActive != isActive) {
				setActivity(isActive);
				return true;
			}
		}
		else {
			managerPos = DEFAULT_NON;
		}
		return false;
	}
	
	@Override
	public String getManagerType() {
		return "fissionSourceManager";
	}
	
	@Override
	public Class<TileFissionSourceManager> getManagerClass() {
		return TileFissionSourceManager.class;
	}
	
	// IMultitoolLogic
	
	@Override
	public boolean onUseMultitool(ItemStack multitool, EntityPlayerMP player, World world, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (IFissionManagerListener.super.onUseMultitool(multitool, player, world, facing, hitX, hitY, hitZ)) {
			return true;
		}
		if (!player.isSneaking()) {
			PrimingTargetInfo targetInfo = getPrimingTarget(true);
			if (targetInfo == null) {
				player.sendMessage(new TextComponentString(Lang.localize("nuclearcraft.multiblock.fission_reactor_source.no_target")));
			}
			else {
				IFissionFuelComponent fuelComponent = targetInfo.fuelComponent;
				BlockPos pos = fuelComponent.getTilePos();
				BlockHighlightTracker.sendPacket(player, pos, 5000);
				player.sendMessage(new TextComponentString(Lang.localize("nuclearcraft.multiblock.fission_reactor_source.target", pos.getX(), pos.getY(), pos.getZ(), fuelComponent.getTileBlockDisplayName())));
			}
			return true;
		}
		return super.onUseMultitool(multitool, player, world, facing, hitX, hitY, hitZ);
	}
	
	// NBT
	
	@Override
	public NBTTagCompound writeAll(NBTTagCompound nbt) {
		super.writeAll(nbt);
		nbt.setInteger("facing", facing.getIndex());
		
		if (sourceType == null) {
			nbt.setDouble("efficiency", efficiency);
		}
		else {
			nbt.setString("sourceType", sourceType);
		}
		
		nbt.setBoolean("isSourceActive", isActive);
		nbt.setLong("managerPos", managerPos.toLong());
		return nbt;
	}
	
	@Override
	public void readAll(NBTTagCompound nbt) {
		super.readAll(nbt);
		facing = EnumFacing.byIndex(nbt.getInteger("facing"));
		
		if (nbt.hasKey("efficiency")) {
			efficiency = nbt.getDouble("efficiency");
		}
		else if (nbt.hasKey("sourceType")) {
			sourceType = nbt.getString("sourceType");
			
			if (DYN_EFFICIENCY_MAP.containsKey(sourceType)) {
				efficiency = DYN_EFFICIENCY_MAP.getDouble(sourceType);
			}
		}
		
		isActive = nbt.getBoolean("isSourceActive");
		managerPos = BlockPos.fromLong(nbt.getLong("managerPos"));
	}
}
