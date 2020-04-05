package nc.multiblock.fission.tile;

import static nc.util.BlockPosHelper.DEFAULT_NON;

import javax.annotation.Nullable;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import nc.enumm.MetaEnums;
import nc.multiblock.cuboidal.CuboidalPartPositionType;
import nc.multiblock.fission.FissionCluster;
import nc.multiblock.fission.FissionReactor;
import nc.multiblock.fission.block.BlockFissionShield;
import nc.multiblock.fission.tile.IFissionFuelComponent.ModeratorLineBlockInfo;
import nc.multiblock.fission.tile.manager.IFissionManagerListener;
import nc.multiblock.fission.tile.manager.TileFissionShieldManager;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class TileFissionShield extends TileFissionPart implements IFissionHeatingComponent, IFissionManagerListener<TileFissionShieldManager, TileFissionShield> {
	
	protected final double heatPerFlux, efficiency;
	protected boolean isShielding = false, inActiveModeratorLine = false;
	
	protected FissionCluster cluster = null;
	protected long heat = 0L;
	
	protected int flux = 0;
	
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
	public void resetStats() {
		inActiveModeratorLine = false;
		flux = 0;
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
	
	public boolean isShielding() {
		return isShielding;
	}
	
	public void setShielding(boolean shielding) {
		this.isShielding = shielding;
	}
	
	public boolean isInActiveModeratorLine() {
		return inActiveModeratorLine;
	}
	
	public void setInActiveModeratorLine(boolean inActiveModeratorLine) {
		this.inActiveModeratorLine = inActiveModeratorLine;
	}
	
	public boolean isShieldActiveModerator(boolean isActiveModeratorPos) {
		return getMultiblock() != null ? getLogic().isShieldActiveModerator(this, isActiveModeratorPos) : false;
	}
	
	@Override
	public ModeratorLineBlockInfo getModeratorComponentInfo(boolean activeModeratorPos) {
		return new ModeratorLineBlockInfo(pos, this, isShielding, false, 0, efficiency);
	}
	
	@Override
	public void onAddedToModeratorCache(ModeratorLineBlockInfo info) {
		setInActiveModeratorLine(true);
	}
	
	@Override
	public void onModeratorLineComplete(ModeratorLineBlockInfo info, int flux) {
		this.flux += flux;
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
		if (!world.isRemote && wasShielding != isShielding) {
			updateBlockState(isShielding);
		}
		return true;
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
		flux = nbt.getInteger("flux");
		heat = nbt.getLong("clusterHeat");
		masterManagerPos = BlockPos.fromLong(nbt.getLong("masterManagerPos"));
	}
}
