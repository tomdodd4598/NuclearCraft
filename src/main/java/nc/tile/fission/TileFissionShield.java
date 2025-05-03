package nc.tile.fission;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.objects.*;
import nc.enumm.MetaEnums;
import nc.multiblock.cuboidal.CuboidalPartPositionType;
import nc.multiblock.fission.*;
import nc.tile.fission.IFissionFuelComponent.*;
import nc.tile.fission.manager.*;
import nc.util.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Iterator;

import static nc.util.PosHelper.DEFAULT_NON;

public class TileFissionShield extends TileFissionPart implements IFissionHeatingComponent, IFissionManagerListener<TileFissionShieldManager, TileFissionShield> {
	
	public double heatPerFlux, efficiency;
	public boolean isShielding = false, inCompleteModeratorLine = false, activeModerator = false;
	protected boolean[] validActiveModeratorPos = new boolean[] {false, false, false, false, false, false};
	
	protected FissionCluster cluster = null;
	protected long heat = 0L;
	
	protected int flux = 0;
	protected ModeratorLine[] activeModeratorLines = new ModeratorLine[] {null, null, null};
	
	protected BlockPos managerPos = DEFAULT_NON;
	protected TileFissionShieldManager manager = null;
	
	/**
	 * Don't use this constructor!
	 */
	public TileFissionShield() {
		super(CuboidalPartPositionType.INTERIOR);
	}
	
	public TileFissionShield(double heatPerFlux, double efficiency) {
		this();
		this.heatPerFlux = heatPerFlux;
		this.efficiency = efficiency;
	}
	
	public static class Meta extends TileFissionShield {
		
		protected Meta(MetaEnums.NeutronShieldType type) {
			super(type.getHeatPerFlux(), type.getEfficiency());
		}
		
		@Override
		public boolean shouldRefresh(World worldIn, BlockPos posIn, IBlockState oldState, IBlockState newState) {
			return oldState.getBlock() != newState.getBlock() || oldState.getBlock().getMetaFromState(oldState) != newState.getBlock().getMetaFromState(newState);
		}
	}
	
	public static class BoronSilver extends Meta {
		
		public BoronSilver() {
			super(MetaEnums.NeutronShieldType.BORON_SILVER);
		}
	}
	
	@Override
	public void onMachineAssembled(FissionReactor multiblock) {
		doStandardNullControllerResponse(multiblock);
		super.onMachineAssembled(multiblock);
	}
	
	public boolean isShieldActive() {
		return manager != null && manager.isManagerActive();
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
	public long getRawHeatingIgnoreCoolingPenalty() {
		return 0L;
	}
	
	@Override
	public double getEffectiveHeating() {
		return isFunctional() ? flux * heatPerFlux * efficiency : 0D;
	}
	
	@Override
	public double getEffectiveHeatingIgnoreCoolingPenalty() {
		return 0D;
	}
	
	// Moderator Line
	
	@Override
	public ModeratorBlockInfo getModeratorBlockInfo(EnumFacing dir, boolean validActiveModeratorPosIn) {
		FissionReactorLogic logic = getLogic();
		this.validActiveModeratorPos[dir.getIndex()] = logic != null && logic.isShieldActiveModerator(this, validActiveModeratorPosIn);
		return logic != null ? logic.getShieldModeratorBlockInfo(this, this.validActiveModeratorPos[dir.getIndex()]) : null;
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
		long innerFlux = 0, outerFlux = 0;
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
				return NCMath.toInt(innerFlux + outerFlux);
			}
			else {
				return NCMath.toInt(innerFlux);
			}
		}
		else if (line.reflectorRecipe != null) {
			return NCMath.toInt(Math.floor((innerFlux + outerFlux) * (1D + line.reflectorRecipe.getFissionReflectorReflectivity())));
		}
		return NCMath.toInt(innerFlux);
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
	public TileFissionShieldManager getManager() {
		return manager;
	}
	
	@Override
	public void setManager(TileFissionShieldManager manager) {
		this.manager = manager;
	}
	
	@Override
	public boolean onManagerRefresh(TileFissionShieldManager manager) {
		this.manager = manager;
		if (manager != null) {
			managerPos = manager.getPos();
			boolean wasShielding = isShielding;
			isShielding = isShieldActive();
			if (wasShielding != isShielding) {
				setActivity(isShielding);
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
		return "fissionShieldManager";
	}
	
	@Override
	public Class<TileFissionShieldManager> getManagerClass() {
		return TileFissionShieldManager.class;
	}
	
	// IMultitoolLogic
	
	@Override
	public boolean onUseMultitool(ItemStack multitool, EntityPlayerMP player, World world, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (IFissionManagerListener.super.onUseMultitool(multitool, player, world, facing, hitX, hitY, hitZ)) {
			return true;
		}
		return IFissionHeatingComponent.super.onUseMultitool(multitool, player, world, facing, hitX, hitY, hitZ);
	}
	
	// NBT
	
	@Override
	public NBTTagCompound writeAll(NBTTagCompound nbt) {
		super.writeAll(nbt);
		nbt.setDouble("heatPerFlux", heatPerFlux);
		nbt.setDouble("efficiency", efficiency);
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
		if (nbt.hasKey("heatPerFlux")) {
			heatPerFlux = nbt.getDouble("heatPerFlux");
		}
		if (nbt.hasKey("efficiency")) {
			efficiency = nbt.getDouble("efficiency");
		}
		isShielding = nbt.getBoolean("isShielding");
		inCompleteModeratorLine = nbt.getBoolean("inCompleteModeratorLine");
		activeModerator = nbt.getBoolean("activeModerator");
		flux = nbt.getInteger("flux");
		heat = nbt.getLong("clusterHeat");
		managerPos = BlockPos.fromLong(nbt.getLong("managerPos"));
	}
	
	// OpenComputers
	
	@Override
	public String getOCKey() {
		return "shield";
	}
	
	@Override
	public Object getOCInfo() {
		Object2ObjectMap<String, Object> entry = new Object2ObjectLinkedOpenHashMap<>();
		entry.put("effective_heating", getEffectiveHeating());
		entry.put("is_shielding", isShielding);
		entry.put("flux", flux);
		return entry;
	}
}
