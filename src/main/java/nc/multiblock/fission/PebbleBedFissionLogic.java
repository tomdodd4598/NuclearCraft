package nc.multiblock.fission;

import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import nc.Global;
import nc.init.NCBlocks;
import nc.network.multiblock.*;
import nc.recipe.*;
import nc.recipe.ingredient.IFluidIngredient;
import nc.tile.fission.*;
import nc.tile.fission.port.*;
import nc.tile.internal.fluid.Tank;
import nc.tile.multiblock.TilePartAbstract.SyncReason;
import nc.util.NCMath;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import java.util.*;

import static nc.config.NCConfig.*;

public class PebbleBedFissionLogic extends FissionReactorLogic {
	
	public final List<Tank> tanks = Lists.newArrayList(new Tank(FissionReactor.BASE_TANK_CAPACITY, NCRecipes.fission_emergency_cooling.validFluids.get(0)), new Tank(FissionReactor.BASE_TANK_CAPACITY, null));
	
	public RecipeInfo<BasicRecipe> emergencyCoolingRecipeInfo;
	
	public double meanHeatingSpeedMultiplier = 0D, totalHeatingSpeedMultiplier = 0D;
	
	public PebbleBedFissionLogic(FissionReactorLogic oldLogic) {
		super(oldLogic);
		if (oldLogic instanceof PebbleBedFissionLogic oldPebbleBedLogic) {
			meanHeatingSpeedMultiplier = oldPebbleBedLogic.meanHeatingSpeedMultiplier;
			totalHeatingSpeedMultiplier = oldPebbleBedLogic.totalHeatingSpeedMultiplier;
		}
	}
	
	@Override
	public String getID() {
		return "pebble_bed";
	}
	
	@Override
	public void onResetStats() {
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
		return !containsBlacklistedPart() && !isMissingSorption();
	}
	
	public static final List<Pair<Class<? extends IFissionPart>, String>> PEBBLE_BED_PART_BLACKLIST = Lists.newArrayList(Pair.of(TileSolidFissionCell.class, Global.MOD_ID + ".multiblock_validation.fission_reactor.prohibit_cells"), Pair.of(TileSolidFissionSink.class, Global.MOD_ID + ".multiblock_validation.fission_reactor.prohibit_sinks"), Pair.of(TileSaltFissionVessel.class, Global.MOD_ID + ".multiblock_validation.fission_reactor.prohibit_vessels"), Pair.of(TileSaltFissionHeater.class, Global.MOD_ID + ".multiblock_validation.fission_reactor.prohibit_heaters"));
	
	@Override
	public List<Pair<Class<? extends IFissionPart>, String>> getPartBlacklist() {
		return PEBBLE_BED_PART_BLACKLIST;
	}
	
	public boolean isMissingSorption() {
		return super.isMissingSorption() || isMissingSorption(TileFissionChamberPort.class, TilePebbleFissionChamber.class, NCBlocks.fission_chamber_port.getLocalizedName());
	}
	
	@Override
	public void refreshConnections() {
		super.refreshConnections();
		refreshFilteredPorts(TileFissionChamberPort.class, TilePebbleFissionChamber.class);
		refreshFilteredPorts(TileFissionCoolerPort.class, TilePebbleFissionCooler.class);
		formFuelBunches(TilePebbleFissionChamber.class, (x, y) -> x.getFilterKey().equals(y.getFilterKey()));
	}
	
	@Override
	public void refreshAllFuelComponentModerators(boolean simulate) {
		for (TilePebbleFissionChamber chamber : getParts(TilePebbleFissionChamber.class)) {
			refreshFuelComponentModerators(chamber, componentFailCache, assumedValidCache, simulate);
		}
	}
	
	@Override
	public void incrementClusterStatsFromComponents(FissionCluster cluster, boolean simulate) {
		for (FissionFuelBunch fuelBunch : fuelBunches) {
			fuelBunch.statsRetrieved = false;
		}
		
		for (IFissionComponent component : cluster.getComponentMap().values()) {
			if (component.isFunctional(simulate)) {
				++cluster.componentCount;
				if (component instanceof IFissionHeatingComponent heatingComponent) {
					if (component instanceof TilePebbleFissionChamber chamber) {
						FissionFuelBunch fuelBunch = chamber.getFuelBunch();
						++cluster.fuelComponentCount;
						if (fuelBunch != null && !fuelBunch.statsRetrieved) {
							fuelBunch.statsRetrieved = true;
							cluster.rawHeating += fuelBunch.getRawHeating(simulate);
							cluster.rawHeatingIgnoreCoolingPenalty += fuelBunch.getRawHeatingIgnoreCoolingPenalty(simulate);
							cluster.effectiveHeating += fuelBunch.getEffectiveHeating(simulate);
							cluster.effectiveHeatingIgnoreCoolingPenalty += fuelBunch.getEffectiveHeatingIgnoreCoolingPenalty(simulate);
							cluster.totalHeatMult += fuelBunch.getHeatMultiplier(simulate);
							cluster.totalEfficiency += fuelBunch.getEfficiency(simulate);
							cluster.totalEfficiencyIgnoreCoolingPenalty += fuelBunch.getEfficiencyIgnoreCoolingPenalty(simulate);
						}
					}
					else {
						cluster.rawHeating += heatingComponent.getRawHeating(simulate);
						cluster.rawHeatingIgnoreCoolingPenalty += heatingComponent.getRawHeatingIgnoreCoolingPenalty(simulate);
						cluster.effectiveHeating += heatingComponent.getEffectiveHeating(simulate);
						cluster.effectiveHeatingIgnoreCoolingPenalty += heatingComponent.getEffectiveHeatingIgnoreCoolingPenalty(simulate);
					}
				}
				if (component instanceof IFissionCoolingComponent coolingComponent) {
					cluster.cooling += coolingComponent.getCooling(simulate);
				}
			}
		}
	}
	
	@Override
	public void refreshReactorStats(boolean simulate) {
		super.refreshReactorStats(simulate);
		
		for (FissionCluster cluster : multiblock.getClusterMap().values()) {
			multiblock.usefulPartCount += cluster.componentCount;
			multiblock.fuelComponentCount += cluster.fuelComponentCount;
			multiblock.cooling += cluster.cooling;
			multiblock.rawHeating += cluster.rawHeating;
			// effectiveHeating += cluster.effectiveHeating;
			multiblock.totalHeatMult += cluster.totalHeatMult;
			multiblock.totalEfficiency += cluster.totalEfficiency;
		}
		
		multiblock.usefulPartCount += multiblock.passiveModeratorCache.size() + multiblock.activeModeratorCache.size() + multiblock.activeReflectorCache.size();
		double usefulPartRatio = (double) multiblock.usefulPartCount / (double) multiblock.getInteriorVolume();
		multiblock.sparsityEfficiencyMult = usefulPartRatio >= fission_sparsity_penalty_params[1] ? 1D : (1D - fission_sparsity_penalty_params[0]) * Math.sin(usefulPartRatio * Math.PI / (2D * fission_sparsity_penalty_params[1])) + fission_sparsity_penalty_params[0];
		// effectiveHeating *= multiblock.sparsityEfficiencyMult;
		multiblock.totalEfficiency *= multiblock.sparsityEfficiencyMult;
		multiblock.meanHeatMult = multiblock.fuelComponentCount == 0 ? 0D : (double) multiblock.totalHeatMult / (double) multiblock.fuelComponentCount;
		multiblock.meanEfficiency = multiblock.fuelComponentCount == 0 ? 0D : multiblock.totalEfficiency / multiblock.fuelComponentCount;
		
		for (FissionCluster cluster : multiblock.getClusterMap().values()) {
			cluster.meanHeatingSpeedMultiplier = cluster.totalHeatingSpeedMultiplier = 0D;
			int clusterCoolers = 0;
			for (IFissionComponent component : cluster.getComponentMap().values()) {
				if (component instanceof TilePebbleFissionCooler cooler) {
					cooler.heatingSpeedMultiplier = cluster.meanEfficiency * multiblock.sparsityEfficiencyMult * (cluster.rawHeating >= cluster.cooling ? 1D : (double) cluster.rawHeating / (double) cluster.cooling);
					cluster.totalHeatingSpeedMultiplier += cooler.heatingSpeedMultiplier;
					++clusterCoolers;
				}
			}
			cluster.meanHeatingSpeedMultiplier = clusterCoolers == 0 ? 0D : cluster.totalHeatingSpeedMultiplier / clusterCoolers;
			totalHeatingSpeedMultiplier += cluster.meanHeatingSpeedMultiplier;
		}
		meanHeatingSpeedMultiplier = multiblock.getClusterMap().isEmpty() ? 0D : totalHeatingSpeedMultiplier / multiblock.getClusterMap().size();
	}
	
	// Server
	
	@Override
	public boolean onUpdateServer() {
		if (!multiblock.isSimulation) {
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
		}
		
		updateEmergencyCooling();
		
		updateSounds();
		
		return super.onUpdateServer();
	}
	
	public void updateEmergencyCooling() {
		if (!multiblock.isReactorOn && !heatBuffer.isEmpty()) {
			refreshRecipe();
			if (canProcessInputs()) {
				produceProducts();
			}
		}
	}
	
	public void updateSounds() {
		if (multiblock.isReactorOn) {
			playFuelComponentSounds(TilePebbleFissionChamber.class);
		}
	}
	
	public void refreshRecipe() {
		emergencyCoolingRecipeInfo = NCRecipes.fission_emergency_cooling.getRecipeInfoFromInputs(Collections.emptyList(), tanks.subList(0, 1));
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
		BasicRecipe recipe = emergencyCoolingRecipeInfo.recipe;
		int inputSize = recipe.getFluidIngredients().get(0).getMaxStackSize(emergencyCoolingRecipeInfo.getFluidIngredientNumbers().get(0));
		if (inputSize <= 0 || recipe.getEmergencyCoolingHeatPerInputMB() <= 0D) {
			return false;
		}
		
		IFluidIngredient fluidProduct = recipe.getFluidProducts().get(0);
		int productSize = fluidProduct.getMaxStackSize(0);
		if (productSize <= 0 || fluidProduct.getStack() == null) {
			return false;
		}
		
		Tank outputTank = tanks.get(1);
		return outputTank.isEmpty() ? outputTank.getCapacity() >= productSize : outputTank.getFluid().isFluidEqual(fluidProduct.getStack()) && outputTank.getCapacity() - outputTank.getFluidAmount() >= productSize;
	}
	
	public void produceProducts() {
		Tank inputTank = tanks.get(0), outputTank = tanks.get(1);
		
		BasicRecipe recipe = emergencyCoolingRecipeInfo.recipe;
		int inputSize = recipe.getFluidIngredients().get(0).getMaxStackSize(emergencyCoolingRecipeInfo.getFluidIngredientNumbers().get(0));
		IFluidIngredient fluidProduct = recipe.getFluidProducts().get(0);
		int productSize = fluidProduct.getMaxStackSize(0);
		double heatPerRecipe = recipe.getEmergencyCoolingHeatPerInputMB() * inputSize;
		if (inputSize <= 0 || productSize <= 0 || heatPerRecipe <= 0D) {
			return;
		}
		
		int outputSpace = outputTank.isEmpty() ? outputTank.getCapacity() : outputTank.getCapacity() - outputTank.getFluidAmount();
		int recipeRate = NCMath.toInt(Math.min((double) inputTank.getFluidAmount() / inputSize, Math.min((double) heatBuffer.getHeatStored() / heatPerRecipe, Math.min((double) FissionReactor.BASE_TANK_CAPACITY * getPartCount(TileFissionVent.class) / inputSize, (double) outputSpace / productSize))));
		if (recipeRate <= 0) {
			return;
		}
		int inputAmount = NCMath.toInt((long) recipeRate * inputSize);
		int productAmount = NCMath.toInt((long) recipeRate * productSize);
		
		inputTank.changeFluidAmount(-inputAmount);
		if (inputTank.getFluidAmount() <= 0) {
			inputTank.setFluidStored(null);
		}
		
		if (outputTank.isEmpty()) {
			outputTank.setFluidStored(fluidProduct.getNextStack(0));
			outputTank.setFluidAmount(productAmount);
		}
		else if (outputTank.getFluid().isFluidEqual(fluidProduct.getStack())) {
			outputTank.changeFluidAmount(productAmount);
		}
		
		heatBuffer.changeHeatStored((long) (-recipeRate * heatPerRecipe));
	}
	
	public long getNetClusterHeating() {
		return multiblock.rawHeating - multiblock.cooling;
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
	public void distributeFluxFromFuelComponent(IFissionFuelComponent fuelComponent, final ObjectSet<IFissionFuelComponent> fluxSearchCache, final Long2ObjectMap<IFissionComponent> lineFailCache, final Long2ObjectMap<IFissionComponent> currentAssumedValidCache, boolean simulate) {
		fuelComponent.defaultDistributeFlux(fluxSearchCache, lineFailCache, assumedValidCache, simulate);
	}
	
	@Override
	public IFissionFuelComponent getNextFuelComponent(IFissionFuelComponent fuelComponent, BlockPos pos) {
		return getPartMap(TilePebbleFissionChamber.class).get(pos.toLong());
	}
	
	@Override
	public void refreshFuelComponentLocal(IFissionFuelComponent fuelComponent, boolean simulate) {
		fuelComponent.defaultRefreshLocal(simulate);
	}
	
	@Override
	public void refreshFuelComponentModerators(IFissionFuelComponent fuelComponent, final Long2ObjectMap<IFissionComponent> currentComponentFailCache, final Long2ObjectMap<IFissionComponent> currentAssumedValidCache, boolean simulate) {
		fuelComponent.defaultRefreshModerators(componentFailCache, assumedValidCache, simulate);
	}
	
	@Override
	public boolean isShieldActiveModerator(TileFissionShield shield, boolean activeModeratorPos) {
		return super.isShieldActiveModerator(shield, activeModeratorPos);
	}
	
	@Override
	public @Nonnull List<Tank> getVentTanks(List<Tank> backupTanks) {
		return multiblock.isAssembled() ? tanks : backupTanks;
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
		logicTag.setDouble("meanHeatingSpeedMultiplier", meanHeatingSpeedMultiplier);
		logicTag.setDouble("totalHeatingSpeedMultiplier", totalHeatingSpeedMultiplier);
	}
	
	@Override
	public void readFromLogicTag(NBTTagCompound logicTag, SyncReason syncReason) {
		super.readFromLogicTag(logicTag, syncReason);
		meanHeatingSpeedMultiplier = logicTag.getDouble("meanHeatingSpeedMultiplier");
		totalHeatingSpeedMultiplier = logicTag.getDouble("totalHeatingSpeedMultiplier");
	}
	
	// Packets
	
	@Override
	public PebbleFissionUpdatePacket getMultiblockUpdatePacket() {
		return new PebbleFissionUpdatePacket(multiblock.controller.getTilePos(), multiblock.isReactorOn, heatBuffer, multiblock.clusterCount, multiblock.cooling, multiblock.rawHeating, multiblock.totalHeatMult, multiblock.meanHeatMult, multiblock.fuelComponentCount, multiblock.usefulPartCount, multiblock.totalEfficiency, multiblock.meanEfficiency, multiblock.sparsityEfficiencyMult, meanHeatingSpeedMultiplier, totalHeatingSpeedMultiplier);
	}
	
	@Override
	public void onMultiblockUpdatePacket(FissionUpdatePacket message) {
		super.onMultiblockUpdatePacket(message);
		if (message instanceof PebbleFissionUpdatePacket packet) {
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
