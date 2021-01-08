package nc.multiblock.fission.tile;

import static nc.util.PosHelper.DEFAULT_NON;

import java.util.Iterator;

import javax.annotation.Nullable;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import nc.enumm.MetaEnums;
import nc.multiblock.cuboidal.CuboidalPartPositionType;
import nc.multiblock.fission.*;
import nc.multiblock.fission.tile.IFissionFuelComponent.*;
import nc.multiblock.fission.tile.manager.*;
import nc.util.PosHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class TileFissionShield extends TileFissionPart implements IFissionHeatingComponent, IFissionManagerListener<TileFissionShieldManager, TileFissionShield> {
	
	public final double heatPerFlux, efficiency;
	public boolean isShielding = false, inCompleteModeratorLine = false, activeModerator = false;
	protected boolean[] validActiveModeratorPos = new boolean[] {false, false, false, false, false, false};
	
	protected FissionCluster cluster = null;
	protected long heat = 0L;
	
	protected int flux = 0;
	protected ModeratorLine[] activeModeratorLines = new ModeratorLine[] {null, null, null};
	
	protected BlockPos managerPos = DEFAULT_NON;
	protected TileFissionShieldManager manager = null;
	
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
	public boolean isValidHeatConductor(final Long2ObjectMap<IFissionComponent> componentFailCache, final Long2ObjectMap<IFissionComponent> assumedValidCache) {
		return inCompleteModeratorLine;
	}
	
	@Override
	public boolean isFunctional() {
		return inCompleteModeratorLine;
	}
	
	@Override
	public boolean isActiveModerator() {
		return activeModerator;
	}
	
	@Override
	public void resetStats() {
		inCompleteModeratorLine = activeModerator = false;
		for (EnumFacing dir : EnumFacing.VALUES) {
			validActiveModeratorPos[dir.getIndex()] = false;
		}
		flux = 0;
		for (Axis axis : PosHelper.AXES) {
			activeModeratorLines[PosHelper.getAxisIndex(axis)] = null;
		}
	}
	
	@Override
	public boolean isClusterRoot() {
		return true;
	}
	
	@Override
	public void clusterSearch(Integer id, final Object2IntMap<IFissionComponent> clusterSearchCache, final Long2ObjectMap<IFissionComponent> componentFailCache, final Long2ObjectMap<IFissionComponent> assumedValidCache) {
		IFissionHeatingComponent.super.clusterSearch(id, clusterSearchCache, componentFailCache, assumedValidCache);
	}
	
	@Override
	public void onClusterMeltdown(Iterator<IFissionComponent> componentIterator) {
		
	}
	
	@Override
	public boolean isNullifyingSources(EnumFacing side) {
		return isShielding;
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
		return isFunctional() ? (long) Math.min(Long.MAX_VALUE, Math.floor(flux * heatPerFlux)) : 0L;
	}
	
	@Override
	public double getEffectiveHeating() {
		return isFunctional() ? flux * heatPerFlux * efficiency : 0D;
	}
	
	// Moderator Line
	
	@Override
	public ModeratorBlockInfo getModeratorBlockInfo(EnumFacing dir, boolean validActiveModeratorPos) {
		this.validActiveModeratorPos[dir.getIndex()] = getMultiblock() == null ? false : getLogic().isShieldActiveModerator(this, validActiveModeratorPos);
		return getMultiblock() != null ? getLogic().getShieldModeratorBlockInfo(this, this.validActiveModeratorPos[dir.getIndex()]) : null;
	}
	
	@Override
	public void onAddedToModeratorCache(ModeratorBlockInfo thisInfo) {}
	
	@Override
	public void onModeratorLineComplete(ModeratorLine line, ModeratorBlockInfo thisInfo, EnumFacing dir) {
		inCompleteModeratorLine = true;
		if (validActiveModeratorPos[dir.getIndex()]) {
			activeModerator = true;
		}
		int index = PosHelper.getAxisIndex(dir.getAxis());
		if (activeModeratorLines[index] == null) {
			flux += getLineFluxContribution(line, thisInfo);
			activeModeratorLines[index] = line;
		}
	}
	
	protected int getLineFluxContribution(ModeratorLine line, ModeratorBlockInfo thisInfo) {
		int innerFlux = 0, outerFlux = 0;
		boolean inner = true;
		for (ModeratorBlockInfo info : line.info) {
			if (info == thisInfo) {
				inner = false;
			}
			if (inner) {
				innerFlux += info.fluxFactor;
			}
			else {
				outerFlux += info.fluxFactor;
			}
		}
		
		if (line.fluxSink != null) {
			if (line.fluxSink instanceof IFissionFuelComponent) {
				return innerFlux + outerFlux;
			}
			else {
				return innerFlux;
			}
		}
		else if (line.reflectorRecipe != null) {
			return (int) Math.floor((innerFlux + outerFlux) * (1D + line.reflectorRecipe.getFissionReflectorReflectivity()));
		}
		return innerFlux;
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
	public void clearManager() {
		manager = null;
		managerPos = DEFAULT_NON;
	}
	
	@Override
	public void refreshManager() {
		manager = getMultiblock() == null ? null : getMultiblock().getPartMap(TileFissionShieldManager.class).get(managerPos.toLong());
		if (manager == null) {
			managerPos = DEFAULT_NON;
		}
	}
	
	@Override
	public boolean onManagerRefresh(TileFissionShieldManager manager) {
		this.manager = manager;
		managerPos = manager.getPos();
		boolean wasShielding = isShielding;
		isShielding = manager.isShieldingActive();
		if (wasShielding != isShielding) {
			if (!world.isRemote) {
				setActivity(isShielding);
			}
			return true;
		}
		return false;
	}
	
	// Ticking
	
	/* @Override public void onLoad() { world.neighborChanged(pos, getBlockType(), pos); super.onLoad(); } */
	
	@Override
	public void onBlockNeighborChanged(IBlockState state, World world, BlockPos pos, BlockPos fromPos) {
		boolean wasShielding = isShielding;
		super.onBlockNeighborChanged(state, world, pos, fromPos);
		setActivity(isShielding);
		if (!world.isRemote && wasShielding != isShielding) {
			getLogic().onShieldUpdated(this);
		}
	}
	
	// NBT
	
	@Override
	public NBTTagCompound writeAll(NBTTagCompound nbt) {
		super.writeAll(nbt);
		nbt.setBoolean("isShielding", isShielding);
		nbt.setBoolean("inCompleteModeratorLine", inCompleteModeratorLine);
		nbt.setBoolean("activeModerator", activeModerator);
		nbt.setInteger("flux", flux);
		nbt.setLong("clusterHeat", heat);
		nbt.setLong("managerPos", managerPos.toLong());
		return nbt;
	}
	
	@Override
	public void readAll(NBTTagCompound nbt) {
		super.readAll(nbt);
		isShielding = nbt.getBoolean("isShielding");
		inCompleteModeratorLine = nbt.getBoolean("inCompleteModeratorLine");
		activeModerator = nbt.getBoolean("activeModerator");
		flux = nbt.getInteger("flux");
		heat = nbt.getLong("clusterHeat");
		managerPos = BlockPos.fromLong(nbt.getLong("managerPos"));
	}
}
