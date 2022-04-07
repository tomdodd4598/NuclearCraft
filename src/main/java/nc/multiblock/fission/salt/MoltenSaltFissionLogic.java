package nc.multiblock.fission.salt;

import static nc.config.NCConfig.*;

import java.util.*;

import javax.annotation.Nonnull;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.Lists;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.objects.*;
import nc.Global;
import nc.multiblock.fission.*;
import nc.multiblock.fission.salt.tile.*;
import nc.multiblock.fission.solid.tile.*;
import nc.multiblock.fission.tile.*;
import nc.multiblock.fission.tile.port.*;
import nc.multiblock.tile.TileBeefAbstract.SyncReason;
import nc.network.multiblock.*;
import nc.recipe.*;
import nc.recipe.ingredient.IFluidIngredient;
import nc.tile.internal.fluid.Tank;
import nc.util.NCMath;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public class MoltenSaltFissionLogic extends FissionReactorLogic {
	
	public final ObjectSet<SaltFissionVesselBunch> vesselBunches = new ObjectOpenHashSet<>();
	
	public final List<Tank> tanks = Lists.newArrayList(new Tank(FissionReactor.BASE_TANK_CAPACITY, NCRecipes.fission_emergency_cooling_valid_fluids.get(0)), new Tank(FissionReactor.BASE_TANK_CAPACITY, null));
	
	public RecipeInfo<BasicRecipe> emergencyCoolingRecipeInfo;
	
	public int heaterCount = 0;
	public double meanHeatingSpeedMultiplier = 0D, totalHeatingSpeedMultiplier = 0D;
	
	public MoltenSaltFissionLogic(FissionReactorLogic oldLogic) {
		super(oldLogic);
		if (oldLogic instanceof MoltenSaltFissionLogic) {
			MoltenSaltFissionLogic oldMoltenSaltLogic = (MoltenSaltFissionLogic) oldLogic;
			heaterCount = oldMoltenSaltLogic.heaterCount;
			meanHeatingSpeedMultiplier = oldMoltenSaltLogic.meanHeatingSpeedMultiplier;
			totalHeatingSpeedMultiplier = oldMoltenSaltLogic.totalHeatingSpeedMultiplier;
		}
	}
	
	@Override
	public String getID() {
		return "molten_salt";
	}
	
	@Override
	public void onResetStats() {
		heaterCount = 0;
		meanHeatingSpeedMultiplier = totalHeatingSpeedMultiplier = 0D;
	}
	
	@Override
	public void onReactorFormed() {
		tanks.get(0).setCapacity(FissionReactor.BASE_TANK_CAPACITY * getCapacityMultiplier());
		tanks.get(1).setCapacity(FissionReactor.BASE_TANK_CAPACITY * getCapacityMultiplier());
		
		super.onReactorFormed();
	}
	
	@Override
	public boolean isMachineWhole() {
		return !containsBlacklistedPart();
	}
	
	public static final List<Pair<Class<? extends IFissionPart>, String>> MOLTEN_SALT_PART_BLACKLIST = Lists.newArrayList(Pair.of(TileSolidFissionCell.class, Global.MOD_ID + ".multiblock_validation.fission_reactor.prohibit_cells"), Pair.of(TileSolidFissionSink.class, Global.MOD_ID + ".multiblock_validation.fission_reactor.prohibit_sinks"));
	
	@Override
	public List<Pair<Class<? extends IFissionPart>, String>> getPartBlacklist() {
		return MOLTEN_SALT_PART_BLACKLIST;
	}
	
	@Override
	public void refreshConnections() {
		super.refreshConnections();
		refreshFilteredPorts(TileFissionVesselPort.class, TileSaltFissionVessel.class);
		refreshFilteredPorts(TileFissionHeaterPort.class, TileSaltFissionHeater.class);
		formVesselBunches();
	}
	
	public void formVesselBunches() {
		vesselBunches.clear();
		
		for (TileSaltFissionVessel vessel : getParts(TileSaltFissionVessel.class)) {
			vessel.setVesselBunch(null);
		}
		
		for (TileSaltFissionVessel vessel : getParts(TileSaltFissionVessel.class)) {
			boolean setBunch = false;
			SaltFissionVesselBunch bunch = null;
			for (EnumFacing dir : EnumFacing.VALUES) {
				TileSaltFissionVessel other = getPartMap(TileSaltFissionVessel.class).get(vessel.getPos().offset(dir).toLong());
				if (other != null && vessel.getFilterKey().equals(other.getFilterKey())) {
					SaltFissionVesselBunch otherBunch = other.getVesselBunch();
					if (bunch == null) {
						bunch = otherBunch;
					}
					else if (bunch != otherBunch) {
						if (otherBunch != null) {
							vesselBunches.remove(otherBunch);
							for (TileSaltFissionVessel otherBunchVessel : otherBunch.getPartMap().values()) {
								otherBunchVessel.setVesselBunch(bunch);
							}
							otherBunch.getPartMap().clear();
						}
						else {
							other.setVesselBunch(bunch);
						}
					}
					
					if (!setBunch && bunch != null) {
						vessel.setVesselBunch(bunch);
						setBunch = true;
					}
				}
			}
			
			if (bunch == null) {
				bunch = new SaltFissionVesselBunch(getReactor());
				vessel.setVesselBunch(bunch);
				vesselBunches.add(bunch);
			}
		}
		
		for (SaltFissionVesselBunch bunch : vesselBunches) {
			bunch.init();
		}
	}
	
	@Override
	public void refreshAllFuelComponentModerators() {
		for (TileSaltFissionVessel vessel : getParts(TileSaltFissionVessel.class)) {
			refreshFuelComponentModerators(vessel, componentFailCache, assumedValidCache);
		}
	}
	
	@Override
	public void incrementClusterStatsFromComponents(FissionCluster cluster) {
		for (SaltFissionVesselBunch bunch : vesselBunches) {
			bunch.statsRetrieved = false;
		}
		
		for (IFissionComponent component : cluster.getComponentMap().values()) {
			if (component.isFunctional()) {
				++cluster.componentCount;
				if (component instanceof IFissionHeatingComponent) {
					if (component instanceof TileSaltFissionVessel) {
						SaltFissionVesselBunch bunch = ((TileSaltFissionVessel) component).getVesselBunch();
						++cluster.fuelComponentCount;
						if (bunch != null && !bunch.statsRetrieved) {
							bunch.statsRetrieved = true;
							cluster.rawHeating += bunch.getRawHeating();
							cluster.rawHeatingIgnoreCoolingPenalty += bunch.getRawHeatingIgnoreCoolingPenalty();
							cluster.effectiveHeating += bunch.getEffectiveHeating();
							cluster.effectiveHeatingIgnoreCoolingPenalty += bunch.getEffectiveHeatingIgnoreCoolingPenalty();
							cluster.totalHeatMult += bunch.getHeatMultiplier();
							cluster.totalEfficiency += bunch.getEfficiency();
							cluster.totalEfficiencyIgnoreCoolingPenalty += bunch.getEfficiencyIgnoreCoolingPenalty();
						}
					}
					else {
						cluster.rawHeating += ((IFissionHeatingComponent) component).getRawHeating();
						cluster.effectiveHeating += ((IFissionHeatingComponent) component).getEffectiveHeating();
					}
				}
				if (component instanceof IFissionCoolingComponent) {
					cluster.cooling += ((IFissionCoolingComponent) component).getCooling();
				}
			}
		}
	}
	
	@Override
	public void refreshReactorStats() {
		super.refreshReactorStats();
		
		for (FissionCluster cluster : getReactor().getClusterMap().values()) {
			getReactor().usefulPartCount += cluster.componentCount;
			getReactor().fuelComponentCount += cluster.fuelComponentCount;
			getReactor().cooling += cluster.cooling;
			getReactor().rawHeating += cluster.rawHeating;
			// effectiveHeating += cluster.effectiveHeating;
			getReactor().totalHeatMult += cluster.totalHeatMult;
			getReactor().totalEfficiency += cluster.totalEfficiency;
		}
		
		getReactor().usefulPartCount += getReactor().passiveModeratorCache.size() + getReactor().activeModeratorCache.size() + getReactor().activeReflectorCache.size();
		double usefulPartRatio = (double) getReactor().usefulPartCount / (double) getReactor().getInteriorVolume();
		getReactor().sparsityEfficiencyMult = usefulPartRatio >= fission_sparsity_penalty_params[1] ? 1D : (1D - fission_sparsity_penalty_params[0]) * Math.sin(usefulPartRatio * Math.PI / (2D * fission_sparsity_penalty_params[1])) + fission_sparsity_penalty_params[0];
		// effectiveHeating *= sparsityEfficiencyMult;
		getReactor().totalEfficiency *= getReactor().sparsityEfficiencyMult;
		getReactor().meanHeatMult = getReactor().fuelComponentCount == 0 ? 0D : (double) getReactor().totalHeatMult / (double) getReactor().fuelComponentCount;
		getReactor().meanEfficiency = getReactor().fuelComponentCount == 0 ? 0D : getReactor().totalEfficiency / getReactor().fuelComponentCount;
		
		for (FissionCluster cluster : getReactor().getClusterMap().values()) {
			cluster.meanHeatingSpeedMultiplier = cluster.totalHeatingSpeedMultiplier = 0D;
			int clusterHeaters = 0;
			for (IFissionComponent component : cluster.getComponentMap().values()) {
				if (component instanceof TileSaltFissionHeater) {
					TileSaltFissionHeater heater = (TileSaltFissionHeater) component;
					heater.heatingSpeedMultiplier = cluster.meanEfficiency * getReactor().sparsityEfficiencyMult * (cluster.rawHeating >= cluster.cooling ? 1D : (double) cluster.rawHeating / (double) cluster.cooling);
					cluster.totalHeatingSpeedMultiplier += heater.heatingSpeedMultiplier;
					++clusterHeaters;
				}
			}
			cluster.meanHeatingSpeedMultiplier = clusterHeaters == 0 ? 0D : cluster.totalHeatingSpeedMultiplier / clusterHeaters;
			totalHeatingSpeedMultiplier += cluster.meanHeatingSpeedMultiplier;
		}
		meanHeatingSpeedMultiplier = getReactor().getClusterMap().isEmpty() ? 0D : totalHeatingSpeedMultiplier / getReactor().getClusterMap().size();
	}
	
	// Server
	
	@Override
	public boolean onUpdateServer() {
		if (heatBuffer.isFull() && fission_overheat) {
			heatBuffer.setHeatStored(0L);
			casingMeltdown();
			return true;
		}
		
		for (FissionCluster cluster : getClusterMap().values()) {
			long netHeating = cluster.getNetHeating();
			if (netHeating > 0 && cluster.connectedToWall) {
				heatBuffer.changeHeatStored(netHeating);
			}
			else {
				cluster.heatBuffer.changeHeatStored(netHeating);
			}
			
			if (cluster.heatBuffer.isFull() && fission_overheat) {
				cluster.heatBuffer.setHeatStored(0L);
				clusterMeltdown(cluster);
				return true;
			}
		}
		
		if (heatBuffer.getHeatStored() > 0L) {
			updateEmergencyCooling();
		}
		
		updateSounds();
		
		return super.onUpdateServer();
	}
	
	public void updateEmergencyCooling() {
		if (!getReactor().isReactorOn) {
			refreshRecipe();
			if (canProcessInputs()) {
				produceProducts();
				return;
			}
		}
	}
	
	public void updateSounds() {
		if (getReactor().isReactorOn) {
			playFuelComponentSounds(TileSaltFissionVessel.class);
		}
	}
	
	public void refreshRecipe() {
		emergencyCoolingRecipeInfo = NCRecipes.fission_emergency_cooling.getRecipeInfoFromInputs(new ArrayList<>(), tanks.subList(0, 1));
	}
	
	public boolean canProcessInputs() {
		if (!setRecipeStats()) {
			return false;
		}
		return canProduceProducts();
	}
	
	public boolean setRecipeStats() {
		if (emergencyCoolingRecipeInfo == null) {
			return false;
		}
		return true;
	}
	
	public boolean canProduceProducts() {
		BasicRecipe recipe = emergencyCoolingRecipeInfo.getRecipe();
		IFluidIngredient fluidProduct = recipe.getFluidProducts().get(0);
		int productSize = fluidProduct.getMaxStackSize(0);
		if (productSize <= 0 || fluidProduct.getStack() == null) {
			return false;
		}
		
		return tanks.get(1).isEmpty() || tanks.get(1).getFluid().isFluidEqual(fluidProduct.getStack());
	}
	
	public void produceProducts() {
		BasicRecipe recipe = emergencyCoolingRecipeInfo.getRecipe();
		int usedInput = NCMath.toInt(Math.min(tanks.get(0).getFluidAmount() / recipe.getEmergencyCoolingHeatPerInputMB(), Math.min(heatBuffer.getHeatStored(), FissionReactor.BASE_TANK_CAPACITY * getPartCount(TileFissionVent.class))));
		
		tanks.get(0).changeFluidAmount(-usedInput);
		if (tanks.get(0).getFluidAmount() <= 0) {
			tanks.get(0).setFluidStored(null);
		}
		
		IFluidIngredient fluidProduct = recipe.getFluidProducts().get(0);
		if (fluidProduct.getMaxStackSize(0) > 0) {
			if (tanks.get(1).isEmpty()) {
				tanks.get(1).setFluidStored(fluidProduct.getNextStack(0));
				tanks.get(1).setFluidAmount(usedInput);
			}
			else if (tanks.get(1).getFluid().isFluidEqual(fluidProduct.getStack())) {
				tanks.get(1).changeFluidAmount(usedInput);
			}
		}
		
		heatBuffer.changeHeatStored((long) (-usedInput * recipe.getEmergencyCoolingHeatPerInputMB()));
	}
	
	public long getNetClusterHeating() {
		return getReactor().rawHeating - getReactor().cooling;
	}
	
	@Override
	public void clusterMeltdown(FissionCluster cluster) {
		final Iterator<IFissionComponent> componentIterator = cluster.getComponentMap().values().iterator();
		while (componentIterator.hasNext()) {
			IFissionComponent component = componentIterator.next();
			component.onClusterMeltdown(componentIterator);
		}
		super.clusterMeltdown(cluster);
	}
	
	// Component Logic
	
	@Override
	public void distributeFluxFromFuelComponent(IFissionFuelComponent fuelComponent, final ObjectSet<IFissionFuelComponent> fluxSearchCache, final Long2ObjectMap<IFissionComponent> lineFailCache, final Long2ObjectMap<IFissionComponent> currentAssumedValidCache) {
		fuelComponent.defaultDistributeFlux(fluxSearchCache, lineFailCache, assumedValidCache);
	}
	
	@Override
	public IFissionFuelComponent getNextFuelComponent(IFissionFuelComponent fuelComponent, BlockPos pos) {
		return getPartMap(TileSaltFissionVessel.class).get(pos.toLong());
	}
	
	@Override
	public void refreshFuelComponentLocal(IFissionFuelComponent fuelComponent) {
		fuelComponent.defaultRefreshLocal();
	}
	
	@Override
	public void refreshFuelComponentModerators(IFissionFuelComponent fuelComponent, final Long2ObjectMap<IFissionComponent> currentComponentFailCache, final Long2ObjectMap<IFissionComponent> currentAssumedValidCache) {
		fuelComponent.defaultRefreshModerators(componentFailCache, assumedValidCache);
	}
	
	@Override
	public boolean isShieldActiveModerator(TileFissionShield shield, boolean activeModeratorPos) {
		// return activeModeratorPos;
		return super.isShieldActiveModerator(shield, activeModeratorPos);
	}
	
	@Override
	public @Nonnull List<Tank> getVentTanks(List<Tank> backupTanks) {
		return getReactor().isAssembled() ? tanks : backupTanks;
	}
	
	// Client
	
	@Override
	public void onUpdateClient() {
		super.onUpdateClient();
	}
	
	// NBT
	
	@Override
	public void writeToLogicTag(NBTTagCompound logicTag, SyncReason syncReason) {
		super.writeToLogicTag(logicTag, syncReason);
		writeTanks(tanks, logicTag, "tanks");
		logicTag.setInteger("heaterCount", heaterCount);
		logicTag.setDouble("meanHeatingSpeedMultiplier", meanHeatingSpeedMultiplier);
		logicTag.setDouble("totalHeatingSpeedMultiplier", totalHeatingSpeedMultiplier);
	}
	
	@Override
	public void readFromLogicTag(NBTTagCompound logicTag, SyncReason syncReason) {
		super.readFromLogicTag(logicTag, syncReason);
		readTanks(tanks, logicTag, "tanks");
		heaterCount = logicTag.getInteger("heaterCount");
		meanHeatingSpeedMultiplier = logicTag.getDouble("meanHeatingSpeedMultiplier");
		totalHeatingSpeedMultiplier = logicTag.getDouble("totalHeatingSpeedMultiplier");
	}
	
	// Packets
	
	@Override
	public SaltFissionUpdatePacket getMultiblockUpdatePacket() {
		return new SaltFissionUpdatePacket(getReactor().controller.getTilePos(), getReactor().isReactorOn, heatBuffer, getReactor().clusterCount, getReactor().cooling, getReactor().rawHeating, getReactor().totalHeatMult, getReactor().meanHeatMult, getReactor().fuelComponentCount, getReactor().usefulPartCount, getReactor().totalEfficiency, getReactor().meanEfficiency, getReactor().sparsityEfficiencyMult, meanHeatingSpeedMultiplier, totalHeatingSpeedMultiplier);
	}
	
	@Override
	public void onMultiblockUpdatePacket(FissionUpdatePacket message) {
		super.onMultiblockUpdatePacket(message);
		if (message instanceof SaltFissionUpdatePacket) {
			SaltFissionUpdatePacket packet = (SaltFissionUpdatePacket) message;
			meanHeatingSpeedMultiplier = packet.meanHeatingSpeedMultiplier;
			totalHeatingSpeedMultiplier = packet.totalHeatingSpeedMultiplier;
		}
	}
	
	// Clear Material
	
	@Override
	public void clearAllMaterial() {
		super.clearAllMaterial();
		for (Tank tank : tanks) {
			tank.setFluidStored(null);
		}
	}
}
