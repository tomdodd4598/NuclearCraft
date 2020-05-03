package nc.multiblock.fission.tile;

import static nc.util.BlockPosHelper.DEFAULT_NON;

import javax.annotation.Nullable;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import nc.enumm.MetaEnums;
import nc.multiblock.cuboidal.CuboidalPartPositionType;
import nc.multiblock.fission.FissionCluster;
import nc.multiblock.fission.FissionReactor;
import nc.multiblock.fission.block.BlockFissionShield;
import nc.multiblock.fission.tile.IFissionFuelComponent.ModeratorBlockInfo;
import nc.multiblock.fission.tile.manager.IFissionManagerListener;
import nc.multiblock.fission.tile.manager.TileFissionShieldManager;
import nc.util.BlockPosHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class TileFissionShield extends TileFissionPart implements IFissionHeatingComponent, IFissionManagerListener<TileFissionShieldManager, TileFissionShield> {
	
	public final double heatPerFlux, efficiency;
	public boolean isShielding = false, inActiveModeratorLine = false, activeModerator = false;
	protected boolean[] activeModeratorPos = new boolean[] {false, false, false, false, false, false};
	
	protected FissionCluster cluster = null;
	protected long heat = 0L;
	
	protected int flux = 0;
	protected boolean[] moderatorLineAxes = new boolean[] {false, false, false};
	
	protected BlockPos masterManagerPos = DEFAULT_NON;
	protected TileFissionShieldManager masterManager = null;
	
	public TileFissionShield(double heatPerFlux, double efficiency) {
		super(CuboidalPartPositionType.INTERIOR);
		this.heatPerFlux = heatPerFlux;
		this.efficiency = efficiency;
	}
	
	public static class BoronSilver extends TileFissionShield {
		
		public BoronSilver() {
			super(MetaEnums.NeutronShieldType.BORON_SILVER);
		}
	}
	
	private TileFissionShield(MetaEnums.NeutronShieldType type) {
		this(type.getHeatPerFlux(), type.getEfficiency());
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
	
	// IFissionComponent
	
	@Override
	public @Nullable FissionCluster getCluster() {
		return cluster;
	}
	
	@Override
	public void setClusterInternal(@Nullable FissionCluster cluster) {
		this.cluster = cluster;
	}
	
	@Override
	public boolean isValidHeatConductor() {
		return inActiveModeratorLine;
	}
	
	@Override
	public boolean isFunctional() {
		return inActiveModeratorLine;
	}
	
	@Override
	public boolean isActiveModerator() {
		return isFunctional() && activeModerator;
	}
	
	@Override
	public void resetStats() {
		inActiveModeratorLine = activeModerator = false;
		for (EnumFacing dir : EnumFacing.VALUES) {
			activeModeratorPos[dir.getIndex()] = false;
		}
		flux = 0;
		for (Axis axis : BlockPosHelper.AXES) {
			moderatorLineAxes[BlockPosHelper.getAxisIndex(axis)] = false;
		}
	}
	
	@Override
	public boolean isClusterRoot() {
		return false;
	}
	
	@Override
	public void clusterSearch(Integer id, final Object2IntMap<IFissionComponent> clusterSearchCache) {
		IFissionHeatingComponent.super.clusterSearch(id, clusterSearchCache);
	}
	
	@Override
	public void onClusterMeltdown() {
		
	}
	
	@Override
	public long getHeatStored() {
		return heat;
	}
	
	@Override
	public void setHeatStored(long heat) {
		this.heat = heat;
	}
	
	@Override
	public long getRawHeating() {
		return isFunctional() ? (long) Math.min(Long.MAX_VALUE, Math.floor(flux*heatPerFlux)) : 0L;
	}
	
	@Override
	public double getEffectiveHeating() {
		return isFunctional() ? flux*heatPerFlux*efficiency : 0D;
	}
	
	// FissionShield
	
	@Override
	public ModeratorBlockInfo getModeratorBlockInfo(EnumFacing dir, boolean activeModeratorPos) {
		this.activeModeratorPos[dir.getIndex()] = getMultiblock() != null ? getLogic().isShieldActiveModerator(this, activeModeratorPos) : false;
		return getMultiblock() != null ? getLogic().getShieldModeratorBlockInfo(this, this.activeModeratorPos[dir.getIndex()]) : null;
	}
	
	@Override
	public void onModeratorLineComplete(ModeratorBlockInfo info, EnumFacing dir, int flux) {
		inActiveModeratorLine = true;
		if (activeModeratorPos[dir.getIndex()]) {
			activeModerator = true;
		}
		int index = BlockPosHelper.getAxisIndex(dir.getAxis());
		if (!moderatorLineAxes[index]) {
			this.flux += flux;
			moderatorLineAxes[index] = true;
		}
	}
	
	// IFissionManagerListener
	
	@Override
	public BlockPos getMasterManagerPos() {
		return masterManagerPos;
	}
	
	@Override
	public void setMasterManagerPos(BlockPos pos) {
		masterManagerPos = pos;
	}
	
	@Override
	public void clearMasterManager() {
		masterManager = null;
		masterManagerPos = DEFAULT_NON;
	}
	
	@Override
	public void refreshMasterManager() {
		masterManager = getMultiblock() == null ? null : getMultiblock().getPartMap(TileFissionShieldManager.class).get(masterManagerPos.toLong());
		if (masterManager == null) masterManagerPos = DEFAULT_NON;
	}
	
	@Override
	public boolean onManagerRefresh() {
		boolean wasShielding = isShielding;
		isShielding = masterManager.isShieldingActive();
		if (wasShielding != isShielding) {
			if (!world.isRemote) {
				updateBlockState(isShielding);
			}
			return true;
		}
		return false;
	}
	
	// Ticking
	
	@Override
	public void onAdded() {
		world.neighborChanged(pos, getBlockType(), pos);
		super.onAdded();
	}
	
	@Override
	public void onBlockNeighborChanged(IBlockState state, World world, BlockPos pos, BlockPos fromPos) {
		boolean wasShielding = isShielding;
		super.onBlockNeighborChanged(state, world, pos, fromPos);
		updateBlockState(isShielding);
		if (!world.isRemote && wasShielding != isShielding) {
			getLogic().onShieldUpdated(this);
		}
	}
	
	public void updateBlockState(boolean isActive) {
		if (getBlockType() instanceof BlockFissionShield) {
			((BlockFissionShield)getBlockType()).setState(isActive, this);
			//world.notifyNeighborsOfStateChange(pos, getBlockType(), true);
		}
	}
	
	// NBT
	
	@Override
	public NBTTagCompound writeAll(NBTTagCompound nbt) {
		super.writeAll(nbt);
		nbt.setBoolean("isShielding", isShielding);
		nbt.setBoolean("inActiveModeratorLine", inActiveModeratorLine);
		nbt.setBoolean("activeModerator", activeModerator);
		nbt.setInteger("flux", flux);
		nbt.setLong("clusterHeat", heat);
		nbt.setLong("masterManagerPos", masterManagerPos.toLong());
		return nbt;
	}
	
	@Override
	public void readAll(NBTTagCompound nbt) {
		super.readAll(nbt);
		isShielding = nbt.getBoolean("isShielding");
		inActiveModeratorLine = nbt.getBoolean("inActiveModeratorLine");
		activeModerator = nbt.getBoolean("activeModerator");
		flux = nbt.getInteger("flux");
		heat = nbt.getLong("clusterHeat");
		masterManagerPos = BlockPos.fromLong(nbt.getLong("masterManagerPos"));
	}
}
