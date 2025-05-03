package nc.multiblock.fission;

import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import nc.Global;
import nc.network.multiblock.*;
import nc.recipe.*;
import nc.recipe.ingredient.IFluidIngredient;
import nc.tile.fission.*;
import nc.tile.fission.port.TileFissionCellPort;
import nc.tile.internal.fluid.Tank;
import nc.tile.multiblock.TilePartAbstract.SyncReason;
import nc.util.NCMath;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import java.util.*;

import static nc.config.NCConfig.*;

public class SolidFuelFissionLogic extends FissionReactorLogic {
	
	public List<Tank> tanks = Lists.newArrayList(new Tank(FissionReactor.BASE_TANK_CAPACITY, NCRecipes.fission_heating.validFluids.get(0)), new Tank(FissionReactor.BASE_TANK_CAPACITY, null));
	
	public RecipeInfo<BasicRecipe> heatingRecipeInfo;
	
	public int heatingOutputRate = 0;
	public double effectiveHeating = 0D, reservedEffectiveHeat = 0D, heatingRecipeRate = 0D, heatingOutputRateFP = 0D;
	
	public SolidFuelFissionLogic(FissionReactorLogic oldLogic) {
		super(oldLogic);
		if (oldLogic instanceof SolidFuelFissionLogic oldSolidFuelLogic) {
			heatingOutputRate = oldSolidFuelLogic.heatingOutputRate;
			effectiveHeating = oldSolidFuelLogic.effectiveHeating;
			reservedEffectiveHeat = oldSolidFuelLogic.reservedEffectiveHeat;
			heatingRecipeRate = oldSolidFuelLogic.heatingRecipeRate;
			heatingOutputRateFP = oldSolidFuelLogic.heatingOutputRateFP;
		}
	}
	
	@Override
	public String getID() {
		return "solid_fuel";
	}
	
	@Override
	public void onResetStats() {
		heatingOutputRate = 0;
		effectiveHeating = heatingRecipeRate = heatingOutputRateFP = 0D;
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
	
	public static final List<Pair<Class<? extends IFissionPart>, String>> SOLID_FUEL_PART_BLACKLIST = Lists.newArrayList(Pair.of(TileSaltFissionVessel.class, Global.MOD_ID + ".multiblock_validation.fission_reactor.prohibit_vessels"), Pair.of(TileSaltFissionHeater.class, Global.MOD_ID + ".multiblock_validation.fission_reactor.prohibit_heaters"));
	
	@Override
	public List<Pair<Class<? extends IFissionPart>, String>> getPartBlacklist() {
		return SOLID_FUEL_PART_BLACKLIST;
	}
	
	@Override
	public void refreshConnections() {
		super.refreshConnections();
		refreshFilteredPorts(TileFissionCellPort.class, TileSolidFissionCell.class);
	}
	
	@Override
	public void refreshAllFuelComponentModerators() {
		for (TileSolidFissionCell cell : getParts(TileSolidFissionCell.class)) {
			refreshFuelComponentModerators(cell, componentFailCache, assumedValidCache);
		}
	}
	
	@Override
	public void refreshReactorStats() {
		super.refreshReactorStats();
		
		for (FissionCluster cluster : multiblock.getClusterMap().values()) {
			if (cluster.connectedToWall) {
				multiblock.usefulPartCount += cluster.componentCount;
				multiblock.fuelComponentCount += cluster.fuelComponentCount;
				multiblock.cooling += cluster.cooling;
				multiblock.rawHeating += cluster.rawHeating;
				effectiveHeating += cluster.effectiveHeating;
				multiblock.totalHeatMult += cluster.totalHeatMult;
				multiblock.totalEfficiency += cluster.totalEfficiency;
			}
		}
		
		multiblock.usefulPartCount += multiblock.passiveModeratorCache.size() + multiblock.activeModeratorCache.size() + multiblock.activeReflectorCache.size();
		double usefulPartRatio = (double) multiblock.usefulPartCount / (double) multiblock.getInteriorVolume();
		multiblock.sparsityEfficiencyMult = usefulPartRatio >= fission_sparsity_penalty_params[1] ? 1D : (1D - fission_sparsity_penalty_params[0]) * Math.sin(usefulPartRatio * Math.PI / (2D * fission_sparsity_penalty_params[1])) + fission_sparsity_penalty_params[0];
		effectiveHeating *= multiblock.sparsityEfficiencyMult;
		multiblock.totalEfficiency *= multiblock.sparsityEfficiencyMult;
		multiblock.meanHeatMult = multiblock.fuelComponentCount == 0 ? 0D : (double) multiblock.totalHeatMult / (double) multiblock.fuelComponentCount;
		multiblock.meanEfficiency = multiblock.fuelComponentCount == 0 ? 0D : multiblock.totalEfficiency / multiblock.fuelComponentCount;
	}
	
	// Server
	
	@Override
	public boolean onUpdateServer() {
		heatBuffer.changeHeatStored(multiblock.rawHeating);
		
		if (heatBuffer.isFull() && fission_overheat) {
			heatBuffer.setHeatStored(0L);
			reservedEffectiveHeat = 0D;
			casingMeltdown();
			return true;
		}
		
		for (FissionCluster cluster : getClusterMap().values()) {
			cluster.heatBuffer.changeHeatStored(cluster.getNetHeating());
			if (cluster.heatBuffer.isFull() && fission_overheat) {
				cluster.heatBuffer.setHeatStored(0L);
				clusterMeltdown(cluster);
				return true;
			}
		}
		
		if (getEffectiveHeat() > 0D) {
			updateFluidHeating();
		}
		
		updateSounds();
		
		return super.onUpdateServer();
	}
	
	public void updateFluidHeating() {
		if (multiblock.isReactorOn) {
			refreshRecipe();
			if (canProcessInputs()) {
				produceProducts();
				return;
			}
		}
		heatingOutputRate = 0;
		heatingRecipeRate = heatingOutputRateFP = 0D;
	}
	
	public void updateSounds() {
		if (multiblock.isReactorOn) {
			playFuelComponentSounds(TileSolidFissionCell.class);
		}
	}
	
	public void refreshRecipe() {
		heatingRecipeInfo = NCRecipes.fission_heating.getRecipeInfoFromInputs(Collections.emptyList(), tanks.subList(0, 1));
	}
	
	public boolean canProcessInputs() {
		if (!setRecipeStats()) {
			return false;
		}
		return canProduceProducts();
	}
	
	public boolean setRecipeStats() {
		if (heatingRecipeInfo == null) {
			heatingOutputRate = 0;
			heatingRecipeRate = heatingOutputRateFP = 0D;
			return false;
		}
		return true;
	}
	
	public boolean canProduceProducts() {
		BasicRecipe recipe = heatingRecipeInfo.recipe;
		IFluidIngredient fluidProduct = recipe.getFluidProducts().get(0);
		int productSize = fluidProduct.getMaxStackSize(0);
		if (productSize <= 0 || fluidProduct.getStack() == null) {
			return false;
		}
		
		int heatPerMB = recipe.getFissionHeatingHeatPerInputMB();
		int inputSize = recipe.getFluidIngredients().get(0).getMaxStackSize(heatingRecipeInfo.getFluidIngredientNumbers().get(0));
		
		double usedInput = Math.min(tanks.get(0).getFluidAmount(), getEffectiveHeat() / heatPerMB);
		heatingRecipeRate = heatingOutputRateFP = NCMath.toInt(Math.min((double) (tanks.get(1).getCapacity() - tanks.get(1).getFluidAmount()) / productSize, usedInput / inputSize));
		reservedEffectiveHeat += (heatingRecipeRate - NCMath.toInt(heatingRecipeRate)) * inputSize * heatPerMB;
		
		int extraRecipeRate = NCMath.toInt(Math.min(Integer.MAX_VALUE - heatingRecipeRate, reservedEffectiveHeat / (heatPerMB * inputSize)));
		heatingRecipeRate += extraRecipeRate;
		reservedEffectiveHeat -= extraRecipeRate * inputSize * heatPerMB;
		
		return tanks.get(1).isEmpty() || tanks.get(1).getFluid().isFluidEqual(fluidProduct.getStack());
	}
	
	public void produceProducts() {
		BasicRecipe recipe = heatingRecipeInfo.recipe;
		int inputSize = recipe.getFluidIngredients().get(0).getMaxStackSize(heatingRecipeInfo.getFluidIngredientNumbers().get(0));
		int heatingRecipeRateInt = NCMath.toInt(heatingRecipeRate);
		
		if (heatingRecipeRateInt * inputSize > 0) {
			tanks.get(0).changeFluidAmount(-heatingRecipeRateInt * inputSize);
		}
		if (tanks.get(0).getFluidAmount() <= 0) {
			tanks.get(0).setFluidStored(null);
		}
		
		IFluidIngredient fluidProduct = recipe.getFluidProducts().get(0);
		if (fluidProduct.getMaxStackSize(0) > 0) {
			int stackSize = 0;
			if (tanks.get(1).isEmpty()) {
				tanks.get(1).setFluidStored(fluidProduct.getNextStack(0));
				stackSize = tanks.get(1).getFluidAmount();
				heatingOutputRate = heatingRecipeRateInt * stackSize;
				tanks.get(1).setFluidAmount(heatingOutputRate);
			}
			else if (tanks.get(1).getFluid().isFluidEqual(fluidProduct.getStack())) {
				stackSize = fluidProduct.getNextStackSize(0);
				heatingOutputRate = heatingRecipeRateInt * stackSize;
				tanks.get(1).changeFluidAmount(heatingOutputRate);
			}
			heatingOutputRateFP *= stackSize;
			if (heatingOutputRateFP > stackSize) {
				heatingOutputRateFP = Math.round(heatingOutputRateFP);
			}
		}
		
		long heatRemoval = (long) (multiblock.rawHeating / effectiveHeating * heatingRecipeRate * inputSize * recipe.getFissionHeatingHeatPerInputMB());
		heatBuffer.changeHeatStored(-heatRemoval);
	}
	
	public double getEffectiveHeat() {
		return multiblock.rawHeating == 0L ? 0D : effectiveHeating / multiblock.rawHeating * heatBuffer.getHeatStored();
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
	public void distributeFluxFromFuelComponent(IFissionFuelComponent fuelComponent, final ObjectSet<IFissionFuelComponent> fluxSearchCache, final Long2ObjectMap<IFissionComponent> lineFailCache, final Long2ObjectMap<IFissionComponent> currentAssumedValidCache) {
		fuelComponent.defaultDistributeFlux(fluxSearchCache, lineFailCache, assumedValidCache);
	}
	
	@Override
	public IFissionFuelComponent getNextFuelComponent(IFissionFuelComponent fuelComponent, BlockPos pos) {
		return getPartMap(TileSolidFissionCell.class).get(pos.toLong());
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
		writeTanks(tanks, logicTag, "tanks");
		logicTag.setInteger("heatingOutputRate", heatingOutputRate);
		logicTag.setDouble("effectiveHeating", effectiveHeating);
		logicTag.setDouble("reservedEffectiveHeat", reservedEffectiveHeat);
		logicTag.setDouble("heatingOutputRateFP", heatingOutputRateFP);
	}
	
	@Override
	public void readFromLogicTag(NBTTagCompound logicTag, SyncReason syncReason) {
		super.readFromLogicTag(logicTag, syncReason);
		readTanks(tanks, logicTag, "tanks");
		heatingOutputRate = logicTag.getInteger("heatingOutputRate");
		effectiveHeating = logicTag.getDouble("effectiveHeating");
		reservedEffectiveHeat = logicTag.getDouble("reservedEffectiveHeat");
		heatingOutputRateFP = logicTag.getDouble("heatingOutputRateFP");
	}
	
	// Packets
	
	@Override
	public SolidFissionUpdatePacket getMultiblockUpdatePacket() {
		return new SolidFissionUpdatePacket(multiblock.controller.getTilePos(), multiblock.isReactorOn, heatBuffer, multiblock.clusterCount, multiblock.cooling, multiblock.rawHeating, multiblock.totalHeatMult, multiblock.meanHeatMult, multiblock.fuelComponentCount, multiblock.usefulPartCount, multiblock.totalEfficiency, multiblock.meanEfficiency, multiblock.sparsityEfficiencyMult, effectiveHeating, heatingOutputRateFP, reservedEffectiveHeat);
	}
	
	@Override
	public void onMultiblockUpdatePacket(FissionUpdatePacket message) {
		super.onMultiblockUpdatePacket(message);
		if (message instanceof SolidFissionUpdatePacket packet) {
			effectiveHeating = packet.effectiveHeating;
			heatingOutputRateFP = packet.heatingOutputRateFP;
			reservedEffectiveHeat = packet.reservedEffectiveHeat;
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
