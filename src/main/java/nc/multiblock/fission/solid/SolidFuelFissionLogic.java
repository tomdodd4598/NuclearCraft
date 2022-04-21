package nc.multiblock.fission.solid;

import static nc.config.NCConfig.*;

import java.util.*;

import javax.annotation.Nonnull;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.Lists;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import nc.Global;
import nc.multiblock.fission.*;
import nc.multiblock.fission.salt.tile.*;
import nc.multiblock.fission.solid.tile.TileSolidFissionCell;
import nc.multiblock.fission.tile.*;
import nc.multiblock.fission.tile.port.TileFissionCellPort;
import nc.multiblock.tile.TileBeefAbstract.SyncReason;
import nc.network.multiblock.*;
import nc.recipe.*;
import nc.recipe.ingredient.IFluidIngredient;
import nc.tile.internal.fluid.Tank;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;

public class SolidFuelFissionLogic extends FissionReactorLogic {
	
	public List<Tank> tanks = Lists.newArrayList(new Tank(FissionReactor.BASE_TANK_CAPACITY, NCRecipes.fission_heating_valid_fluids.get(0)), new Tank(FissionReactor.BASE_TANK_CAPACITY, null));
	
	public RecipeInfo<BasicRecipe> heatingRecipeInfo;
	
	public int heatingOutputRate = 0;
	public double effectiveHeating = 0D, reservedEffectiveHeat = 0D, heatingRecipeRate = 0D, heatingOutputRateFP = 0D;
	
	public SolidFuelFissionLogic(FissionReactorLogic oldLogic) {
		super(oldLogic);
		if (oldLogic instanceof SolidFuelFissionLogic) {
			SolidFuelFissionLogic oldSolidFuelLogic = (SolidFuelFissionLogic) oldLogic;
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
		
		for (FissionCluster cluster : getReactor().getClusterMap().values()) {
			if (cluster.connectedToWall) {
				getReactor().usefulPartCount += cluster.componentCount;
				getReactor().fuelComponentCount += cluster.fuelComponentCount;
				getReactor().cooling += cluster.cooling;
				getReactor().rawHeating += cluster.rawHeating;
				effectiveHeating += cluster.effectiveHeating;
				getReactor().totalHeatMult += cluster.totalHeatMult;
				getReactor().totalEfficiency += cluster.totalEfficiency;
			}
		}
		
		getReactor().usefulPartCount += getReactor().passiveModeratorCache.size() + getReactor().activeModeratorCache.size() + getReactor().activeReflectorCache.size();
		double usefulPartRatio = (double) getReactor().usefulPartCount / (double) getReactor().getInteriorVolume();
		getReactor().sparsityEfficiencyMult = usefulPartRatio >= fission_sparsity_penalty_params[1] ? 1D : (1D - fission_sparsity_penalty_params[0]) * Math.sin(usefulPartRatio * Math.PI / (2D * fission_sparsity_penalty_params[1])) + fission_sparsity_penalty_params[0];
		effectiveHeating *= getReactor().sparsityEfficiencyMult;
		getReactor().totalEfficiency *= getReactor().sparsityEfficiencyMult;
		getReactor().meanHeatMult = getReactor().fuelComponentCount == 0 ? 0D : (double) getReactor().totalHeatMult / (double) getReactor().fuelComponentCount;
		getReactor().meanEfficiency = getReactor().fuelComponentCount == 0 ? 0D : getReactor().totalEfficiency / getReactor().fuelComponentCount;
	}
	
	// Server
	
	@Override
	public boolean onUpdateServer() {
		heatBuffer.changeHeatStored(getReactor().rawHeating);
		
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
		if (getReactor().isReactorOn) {
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
		if (getReactor().isReactorOn) {
			playFuelComponentSounds(TileSolidFissionCell.class);
		}
	}
	
	public void refreshRecipe() {
		heatingRecipeInfo = NCRecipes.fission_heating.getRecipeInfoFromInputs(new ArrayList<>(), tanks.subList(0, 1));
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
		BasicRecipe recipe = heatingRecipeInfo.getRecipe();
		IFluidIngredient fluidProduct = recipe.getFluidProducts().get(0);
		int productSize = fluidProduct.getMaxStackSize(0);
		if (productSize <= 0 || fluidProduct.getStack() == null) {
			return false;
		}
		
		int heatPerMB = recipe.getFissionHeatingHeatPerInputMB();
		int inputSize = recipe.getFluidIngredients().get(0).getMaxStackSize(heatingRecipeInfo.getFluidIngredientNumbers().get(0));
		
		double usedInput = Math.min(tanks.get(0).getFluidAmount(), getEffectiveHeat() / heatPerMB);
		heatingRecipeRate = heatingOutputRateFP = Math.min(Integer.MAX_VALUE, Math.min((tanks.get(1).getCapacity() - tanks.get(1).getFluidAmount()) / productSize, usedInput / inputSize));
		reservedEffectiveHeat += (heatingRecipeRate - (int) heatingRecipeRate) * inputSize * heatPerMB;
		
		int extraRecipeRate = (int) Math.min(Integer.MAX_VALUE - heatingRecipeRate, reservedEffectiveHeat / (heatPerMB * inputSize));
		heatingRecipeRate += extraRecipeRate;
		reservedEffectiveHeat -= extraRecipeRate * inputSize * heatPerMB;
		
		return tanks.get(1).isEmpty() || tanks.get(1).getFluid().isFluidEqual(fluidProduct.getStack());
	}
	
	public void produceProducts() {
		BasicRecipe recipe = heatingRecipeInfo.getRecipe();
		int inputSize = recipe.getFluidIngredients().get(0).getMaxStackSize(heatingRecipeInfo.getFluidIngredientNumbers().get(0));
		int heatingRecipeRateInt = (int) heatingRecipeRate;
		
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
		
		long heatRemoval = (long) (getReactor().rawHeating / effectiveHeating * heatingRecipeRate * inputSize * recipe.getFissionHeatingHeatPerInputMB());
		heatBuffer.changeHeatStored(-heatRemoval);
	}
	
	public double getEffectiveHeat() {
		return getReactor().rawHeating == 0L ? 0D : effectiveHeating / getReactor().rawHeating * heatBuffer.getHeatStored();
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
		return new SolidFissionUpdatePacket(getReactor().controller.getTilePos(), getReactor().isReactorOn, heatBuffer, getReactor().clusterCount, getReactor().cooling, getReactor().rawHeating, getReactor().totalHeatMult, getReactor().meanHeatMult, getReactor().fuelComponentCount, getReactor().usefulPartCount, getReactor().totalEfficiency, getReactor().meanEfficiency, getReactor().sparsityEfficiencyMult, effectiveHeating, heatingOutputRateFP, reservedEffectiveHeat);
	}
	
	@Override
	public void onMultiblockUpdatePacket(FissionUpdatePacket message) {
		super.onMultiblockUpdatePacket(message);
		if (message instanceof SolidFissionUpdatePacket) {
			SolidFissionUpdatePacket packet = (SolidFissionUpdatePacket) message;
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
