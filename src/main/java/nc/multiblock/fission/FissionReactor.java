package nc.multiblock.fission;

import static nc.block.property.BlockProperties.ACTIVE;
import static nc.block.property.BlockProperties.FACING_ALL;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.common.collect.Lists;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.longs.LongSet;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import nc.Global;
import nc.config.NCConfig;
import nc.init.NCSounds;
import nc.multiblock.IMultiblockPart;
import nc.multiblock.MultiblockBase;
import nc.multiblock.TileBeefBase.SyncReason;
import nc.multiblock.container.ContainerSaltFissionController;
import nc.multiblock.container.ContainerSolidFissionController;
import nc.multiblock.cuboidal.CuboidalMultiblockBase;
import nc.multiblock.fission.salt.tile.TileSaltFissionController;
import nc.multiblock.fission.salt.tile.TileSaltFissionHeater;
import nc.multiblock.fission.salt.tile.TileSaltFissionVessel;
import nc.multiblock.fission.solid.tile.TileSolidFissionCell;
import nc.multiblock.fission.solid.tile.TileSolidFissionController;
import nc.multiblock.fission.solid.tile.TileSolidFissionSink;
import nc.multiblock.fission.tile.IFissionComponent;
import nc.multiblock.fission.tile.IFissionController;
import nc.multiblock.fission.tile.IFissionFuelComponent;
import nc.multiblock.fission.tile.IFissionSpecialComponent;
import nc.multiblock.fission.tile.TileFissionConductor;
import nc.multiblock.fission.tile.TileFissionPort;
import nc.multiblock.fission.tile.TileFissionSource;
import nc.multiblock.fission.tile.TileFissionSource.PrimingTargetInfo;
import nc.multiblock.fission.tile.TileFissionVent;
import nc.multiblock.network.FissionUpdatePacket;
import nc.multiblock.network.SaltFissionUpdatePacket;
import nc.multiblock.network.SolidFissionUpdatePacket;
import nc.multiblock.validation.IMultiblockValidator;
import nc.recipe.NCRecipes;
import nc.recipe.ProcessorRecipe;
import nc.recipe.ProcessorRecipeHandler;
import nc.recipe.RecipeInfo;
import nc.recipe.ingredient.IFluidIngredient;
import nc.tile.internal.fluid.Tank;
import nc.tile.internal.heat.HeatBuffer;
import nc.util.NCMath;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidRegistry;

public class FissionReactor extends CuboidalMultiblockBase<FissionUpdatePacket> {
	
	public FissionReactorType type = FissionReactorType.SOLID_FUEL;
	
	protected final Int2ObjectMap<FissionCluster> clusterMap = new Int2ObjectOpenHashMap<>();
	public int clusterCount = 0;
	
	protected final Long2ObjectMap<IFissionComponent> componentMap = new Long2ObjectOpenHashMap<>();
	protected final Long2ObjectMap<IFissionSpecialComponent> specialComponentMap = new Long2ObjectOpenHashMap<>();
	
	protected final Long2ObjectMap<TileFissionConductor> conductorMap = new Long2ObjectOpenHashMap<>();
	protected final Long2ObjectMap<TileFissionPort> portMap = new Long2ObjectOpenHashMap<>();
	protected final Long2ObjectMap<TileFissionVent> ventMap = new Long2ObjectOpenHashMap<>();
	protected final Long2ObjectMap<TileFissionSource> sourceMap = new Long2ObjectOpenHashMap<>();
	
	protected final Long2ObjectMap<TileSolidFissionCell> cellMap = new Long2ObjectOpenHashMap<>();
	protected final Long2ObjectMap<TileSolidFissionSink> sinkMap = new Long2ObjectOpenHashMap<>();
	
	protected final Long2ObjectMap<TileSaltFissionVessel> vesselMap = new Long2ObjectOpenHashMap<>();
	protected final Long2ObjectMap<TileSaltFissionHeater> heaterMap = new Long2ObjectOpenHashMap<>();
	
	protected final Long2ObjectMap<IFissionController> controllerMap = new Long2ObjectOpenHashMap<>();
	protected IFissionController controller;
	
	public final LongSet passiveModeratorCache = new LongOpenHashSet();
	public final LongSet activeModeratorCache = new LongOpenHashSet();
	public final LongSet activeReflectorCache = new LongOpenHashSet();
	
	public static final int BASE_MAX_HEAT = 25000, MAX_TEMP = 2400, BASE_TANK_CAPACITY = 4000;
	public final HeatBuffer heatBuffer = new HeatBuffer(BASE_MAX_HEAT);
	public int ambientTemp = 290, comparatorSignal = 0;
	
	public final List<Tank> tanks = Lists.newArrayList(new Tank(BASE_TANK_CAPACITY, NCRecipes.fission_heating_valid_fluids.get(0)), new Tank(BASE_TANK_CAPACITY, null));
	public static final ProcessorRecipeHandler HEATING_RECIPE_HANDLER = NCRecipes.fission_heating;
	protected RecipeInfo<ProcessorRecipe> recipeInfo;
	
	public boolean refreshFlag = true;
	
	public boolean isReactorOn;
	public int fuelComponentCount = 0, recipeRate = 0, heatRemovalRecipeRate = 0;
	public long cooling = 0L, rawHeating = 0L, totalHeatMult = 0L, usefulPartCount = 0L, netHeating = 0L;
	public double effectiveHeating = 0D, meanHeatMult = 0D, totalEfficiency = 0D, meanEfficiency = 0D, sparsityEfficiencyMult = 0D, heatPerInputSize = 128D, roundedOutputRate = 0D;
	
	public FissionReactor(World world) {
		super(world);
	}
	
	// Multiblock Part Getters
	
	public Int2ObjectMap<FissionCluster> getClusterMap() {
		return clusterMap;
	}
	
	public Long2ObjectMap<IFissionComponent> getComponentMap() {
		return componentMap;
	}
	
	public Long2ObjectMap<IFissionSpecialComponent> getSpecialComponentMap() {
		return specialComponentMap;
	}
	
	public Long2ObjectMap<TileFissionConductor> getConductorMap() {
		return conductorMap;
	}
	
	public Long2ObjectMap<TileFissionPort> getPortMap() {
		return portMap;
	}
	
	public Long2ObjectMap<TileFissionVent> getVentMap() {
		return ventMap;
	}
	
	public Long2ObjectMap<TileFissionSource> getSourceMap() {
		return sourceMap;
	}
	
	public Long2ObjectMap<TileSolidFissionCell> getCellMap() {
		return cellMap;
	}
	
	public Long2ObjectMap<TileSolidFissionSink> getSinkMap() {
		return sinkMap;
	}
	
	public Long2ObjectMap<TileSaltFissionVessel> getVesselMap() {
		return vesselMap;
	}
	
	public Long2ObjectMap<TileSaltFissionHeater> getHeaterMap() {
		return heaterMap;
	}
	
	// Multiblock Size Limits
	
	@Override
	protected int getMinimumInteriorLength() {
		return NCConfig.fission_min_size;
	}
	
	@Override
	protected int getMaximumInteriorLength() {
		return NCConfig.fission_max_size;
	}
	
	// Multiblock Methods
	
	@Override
	public void onAttachedPartWithMultiblockData(IMultiblockPart part, NBTTagCompound data) {
		syncDataFrom(data, SyncReason.FullSync);
	}
	
	@Override
	protected void onBlockAdded(IMultiblockPart newPart) {
		if (newPart instanceof IFissionComponent) {
			componentMap.put(newPart.getTilePos().toLong(), (IFissionComponent) newPart);
			if (newPart instanceof IFissionSpecialComponent) specialComponentMap.put(newPart.getTilePos().toLong(), (IFissionSpecialComponent) newPart);
		}
		
		if (newPart instanceof TileFissionConductor) conductorMap.put(newPart.getTilePos().toLong(), (TileFissionConductor) newPart);
		else if (newPart instanceof TileFissionPort) portMap.put(newPart.getTilePos().toLong(), (TileFissionPort) newPart);
		else if (newPart instanceof TileFissionVent) ventMap.put(newPart.getTilePos().toLong(), (TileFissionVent) newPart);
		else if (newPart instanceof TileFissionSource) sourceMap.put(newPart.getTilePos().toLong(), (TileFissionSource) newPart);
		
		else if (newPart instanceof TileSolidFissionCell) cellMap.put(newPart.getTilePos().toLong(), (TileSolidFissionCell) newPart);
		else if (newPart instanceof TileSolidFissionSink) sinkMap.put(newPart.getTilePos().toLong(), (TileSolidFissionSink) newPart);
		
		else if (newPart instanceof TileSaltFissionVessel) vesselMap.put(newPart.getTilePos().toLong(), (TileSaltFissionVessel) newPart);
		else if (newPart instanceof TileSaltFissionHeater) heaterMap.put(newPart.getTilePos().toLong(), (TileSaltFissionHeater) newPart);
		
		else if (newPart instanceof IFissionController) controllerMap.put(newPart.getTilePos().toLong(), (IFissionController) newPart);
	}
	
	@Override
	protected void onBlockRemoved(IMultiblockPart oldPart) {
		if (oldPart instanceof IFissionComponent) {
			componentMap.remove(oldPart.getTilePos().toLong());
			if (oldPart instanceof IFissionSpecialComponent) specialComponentMap.remove(oldPart.getTilePos().toLong());
		}
		
		if (oldPart instanceof TileFissionConductor) conductorMap.remove(oldPart.getTilePos().toLong());
		else if (oldPart instanceof TileFissionPort) portMap.remove(oldPart.getTilePos().toLong());
		else if (oldPart instanceof TileFissionVent) ventMap.remove(oldPart.getTilePos().toLong());
		else if (oldPart instanceof TileFissionSource) sourceMap.remove(oldPart.getTilePos().toLong());
		
		else if (oldPart instanceof TileSolidFissionCell) cellMap.remove(oldPart.getTilePos().toLong());
		else if (oldPart instanceof TileSolidFissionSink) sinkMap.remove(oldPart.getTilePos().toLong());
		
		else if (oldPart instanceof TileSaltFissionVessel) vesselMap.remove(oldPart.getTilePos().toLong());
		else if (oldPart instanceof TileSaltFissionHeater) heaterMap.remove(oldPart.getTilePos().toLong());
		
		else if (oldPart instanceof IFissionController) controllerMap.remove(oldPart.getTilePos().toLong());
	}
	
	@Override
	protected void onMachineAssembled() {
		onReactorFormed();
	}
	
	@Override
	protected void onMachineRestored() {
		onReactorFormed();
	}
	
	protected void onReactorFormed() {
		for (IFissionController contr : controllerMap.values()) controller = contr;
		
		heatBuffer.setHeatCapacity(BASE_MAX_HEAT*getCapacityMultiplier());
		tanks.get(0).setCapacity(BASE_TANK_CAPACITY*getCapacityMultiplier());
		tanks.get(1).setCapacity(BASE_TANK_CAPACITY*getCapacityMultiplier());
		ambientTemp = 273 + (int) (WORLD.getBiome(getMiddleCoord()).getTemperature(getMiddleCoord())*20F);
		
		if (!WORLD.isRemote) {
			linkPorts();
			refreshReactor();
			updateActivity();
		}
	}
	
	//TODO - temporary ports
	protected void linkPorts() {
		if (portMap.isEmpty()) {
			for (TileSolidFissionCell cell : cellMap.values()) {
				cell.clearPort();
			}
			return;
		}
		
		for (TileFissionPort port : portMap.values()) {
			port.clearMasterPort();
		}
		
		TileFissionPort masterPort = null;
		for (TileFissionPort port : portMap.values()) {
			masterPort = port;
			break;
		}
		
		for (TileFissionPort port : portMap.values()) {
			if (port != masterPort) {
				port.shiftStacks(masterPort);
				port.setMasterPortPos(masterPort.getPos());
				port.refreshMasterPort();
			}
			
			if (!cellMap.isEmpty()) {
				port.inventoryStackLimit = Math.max(64, 2*cellMap.size());
				port.recipe_handler = NCRecipes.solid_fission;
			}
			else {
				port.inventoryStackLimit = 64;
				port.recipe_handler = null;
			}
		}
		
		for (TileSolidFissionCell cell : cellMap.values()) {
			cell.setPortPos(masterPort.getPos());
			cell.refreshPort();
		}
	}
	
	protected int getCapacityMultiplier() {
		return Math.max(getExteriorSurfaceArea(), getInteriorVolume());
	}
	
	protected void refreshReactor() {
		refreshFlag = false;
		if (!isAssembled()) return;
		
		final ObjectSet<IFissionFuelComponent> primedCache = new ObjectOpenHashSet<>();
		for (IFissionComponent component : componentMap.values()) {
			if (component instanceof IFissionFuelComponent) {
				IFissionFuelComponent fuelComponent = (IFissionFuelComponent) component;
				fuelComponent.refreshIsProcessing(false);
				if (fuelComponent.isFunctional()) {
					fuelComponent.tryPriming(this);
					if (fuelComponent.isPrimed()) {
						primedCache.add(fuelComponent);
					}
				}
			}
			component.setCluster(null);
			component.resetStats();
		}
		clusterMap.clear();
		clusterCount = 0;
		passiveModeratorCache.clear();
		activeModeratorCache.clear();
		activeReflectorCache.clear();
		
		if (type == FissionReactorType.PEBBLE_BED) {
			
		}
		else {
			for (TileFissionSource source : sourceMap.values()) {
				IBlockState state = WORLD.getBlockState(source.getPos());
				EnumFacing facing = source.getPartPosition().getFacing();
				source.refreshIsRedstonePowered(WORLD, source.getPos());
				WORLD.setBlockState(source.getPos(), state.withProperty(FACING_ALL, facing != null ? facing : state.getValue(FACING_ALL)).withProperty(ACTIVE, source.getIsRedstonePowered()), 3);
				
				if (!source.getIsRedstonePowered()) continue;
				PrimingTargetInfo targetInfo = source.getPrimingTarget();
				if (targetInfo == null || targetInfo.fuelComponent == null) continue;
				
				targetInfo.fuelComponent.tryPriming(this);
				if (targetInfo.fuelComponent.isPrimed()) {
					primedCache.add(targetInfo.fuelComponent);
				}
			}
			
			for (IFissionFuelComponent primedComponent : primedCache) {
				primedComponent.fluxSearch();
			}
			
			for (IFissionFuelComponent primedComponent : primedCache) {
				primedComponent.refreshIsProcessing(false);
				primedComponent.refreshLocal();
				primedComponent.unprime();
			}
			
			if (type == FissionReactorType.SOLID_FUEL) {
				for (TileSolidFissionCell cell : cellMap.values()) {
					cell.refreshModerators();
				}
			}
			else {
				for (TileSaltFissionVessel vessel : vesselMap.values()) {
					vessel.refreshModerators();
				}
			}
			
			passiveModeratorCache.removeAll(activeModeratorCache);
			
			if (type == FissionReactorType.SOLID_FUEL) {
				for (TileSolidFissionCell cell : cellMap.values()) {
					cell.clusterSearch(null);
				}
			}
			else {
				for (TileSaltFissionVessel vessel : vesselMap.values()) {
					vessel.clusterSearch(null);
				}
			}
			
			for (long posLong : activeModeratorCache) {
				for (EnumFacing dir : EnumFacing.VALUES) {
					IFissionComponent component = componentMap.get(BlockPos.fromLong(posLong).offset(dir).toLong());
					if (component != null) component.clusterSearch(null);
				}
			}
			
			for (long posLong : activeReflectorCache) {
				for (EnumFacing dir : EnumFacing.VALUES) {
					IFissionComponent component = componentMap.get(BlockPos.fromLong(posLong).offset(dir).toLong());
					if (component != null) component.clusterSearch(null);
				}
			}
		}
		
		for (IFissionSpecialComponent specialComponent : specialComponentMap.values()) {
			specialComponent.postClusterSearch();
		}
		
		for (FissionCluster cluster : clusterMap.values()) {
			cluster.refreshClusterStats();
		}
		sortClusters();
		
		refreshReactorStats();
	}
	
	public void onSourceUpdated(TileFissionSource source) {
		if (source.getIsRedstonePowered()) {
			PrimingTargetInfo targetInfo = source.getPrimingTarget();
			if (targetInfo != null) {
				if (!targetInfo.fuelComponent.isFunctional()) {
					refreshFlag = true;
				}
				else if (targetInfo.newSourceEfficiency) {
					refreshCluster(targetInfo.fuelComponent.getCluster());
				}
			}
		}
	}
	
	/** Only use when the cluster geometry isn't changed and there is no effect on other clusters! */
	public void refreshCluster(FissionCluster cluster) {
		if (cluster != null && clusterMap.containsKey(cluster.getId())) {
			cluster.refreshClusterStats();
		}
		refreshReactorStats();
	}
	
	protected void refreshReactorStats() {
		resetStats();
		for (FissionCluster cluster : clusterMap.values()) {
			if (cluster.connectedToWall) {
				usefulPartCount += cluster.componentCount;
				fuelComponentCount += cluster.fuelComponentCount;
				cooling += cluster.cooling;
				rawHeating += cluster.rawHeating;
				effectiveHeating += cluster.effectiveHeating;
				totalHeatMult += cluster.totalHeatMult;
				totalEfficiency += cluster.totalEfficiency;
			}
		}
		
		usefulPartCount += passiveModeratorCache.size() + activeModeratorCache.size() + activeReflectorCache.size();
		double usefulPartRatio = (double)usefulPartCount/(double)getInteriorVolume();
		sparsityEfficiencyMult = usefulPartRatio >= NCConfig.fission_sparsity_penalty_params[1] ? 1D : (1D - NCConfig.fission_sparsity_penalty_params[0])*Math.sin(usefulPartRatio*Math.PI/(2D*NCConfig.fission_sparsity_penalty_params[1])) + NCConfig.fission_sparsity_penalty_params[0];
		effectiveHeating *= sparsityEfficiencyMult;
		totalEfficiency *= sparsityEfficiencyMult;
		meanHeatMult = fuelComponentCount == 0 ? 0D : (double)totalHeatMult/(double)fuelComponentCount;
		meanEfficiency = fuelComponentCount == 0 ? 0D : totalEfficiency/fuelComponentCount;
	}
	
	protected void sortClusters() {
		final ObjectSet<FissionCluster> uniqueClusterCache = new ObjectOpenHashSet<>();
		for (FissionCluster cluster : clusterMap.values()) {
			uniqueClusterCache.add(cluster);
		}
		clusterMap.clear();
		int i = 0;
		for (FissionCluster cluster : uniqueClusterCache) {
			cluster.setId(i);
			clusterMap.put(i, cluster);
			i++;
		}
		clusterCount = clusterMap.size();
	}
	
	public void mergeClusters(int assimilatorId, FissionCluster targetCluster) {
		if (assimilatorId == targetCluster.getId()) return;
		FissionCluster assimilatorCluster = clusterMap.get(assimilatorId);
		for (IFissionComponent component : targetCluster.getComponentMap().values()) {
			component.setCluster(assimilatorCluster);
		}
		assimilatorCluster.heatBuffer.mergeHeatBuffers(targetCluster.heatBuffer);
		targetCluster.getComponentMap().clear();
		clusterMap.remove(targetCluster.getId());
	}
	
	@Override
	protected void onMachinePaused() {
		
	}
	
	@Override
	protected void onMachineDisassembled() {
		resetStats();
		if (controller != null) controller.updateBlockState(false);
		isReactorOn = false;
	}
	
	protected void resetStats() {
		//isReactorOn = false;
		fuelComponentCount = recipeRate = heatRemovalRecipeRate = 0;
		cooling = rawHeating = totalHeatMult = usefulPartCount = netHeating = 0L;
		effectiveHeating = meanHeatMult = totalEfficiency = meanEfficiency = sparsityEfficiencyMult = roundedOutputRate = 0D;
	}
	
	@Override
	protected boolean isMachineWhole(IMultiblockValidator validatorCallback) {
		
		// Only one controller
		
		if (controllerMap.size() == 0) {
			validatorCallback.setLastError(Global.MOD_ID + ".multiblock_validation.no_controller", null);
			return false;
		}
		if (controllerMap.size() > 1) {
			validatorCallback.setLastError(Global.MOD_ID + ".multiblock_validation.too_many_controllers", null);
			return false;
		}
		
		for (IFissionController contr : controllerMap.values()) {
			controller = contr;
		}
		
		if (controller instanceof TileSolidFissionController) {
			type = FissionReactorType.SOLID_FUEL;
		}
		else type = FissionReactorType.MOLTEN_SALT;
		
		
		if (type != FissionReactorType.PEBBLE_BED) {
			
		}
		else if (type != FissionReactorType.SOLID_FUEL) {
			if (cellMap.size() != 0) {
				validatorCallback.setLastError(Global.MOD_ID + ".multiblock_validation.fission_reactor.prohibit_cells", null);
				return false;
			}
			if (sinkMap.size() != 0) {
				validatorCallback.setLastError(Global.MOD_ID + ".multiblock_validation.fission_reactor.prohibit_sinks", null);
				return false;
			}
		}
		else if (type != FissionReactorType.MOLTEN_SALT) {
			if (vesselMap.size() != 0) {
				validatorCallback.setLastError(Global.MOD_ID + ".multiblock_validation.fission_reactor.prohibit_vessels", null);
				return false;
			}
			if (heaterMap.size() != 0) {
				validatorCallback.setLastError(Global.MOD_ID + ".multiblock_validation.fission_reactor.prohibit_heaters", null);
				return false;
			}
		}
		
		return super.isMachineWhole(validatorCallback);
	}
	
	@Override
	protected void onAssimilate(MultiblockBase assimilated) {
		if (!(assimilated instanceof FissionReactor)) return;
		FissionReactor assimilatedReactor = (FissionReactor) assimilated;
		heatBuffer.mergeHeatBuffers(assimilatedReactor.heatBuffer);
	}
	
	@Override
	protected void onAssimilated(MultiblockBase assimilator) {
		
	}
	
	// Server
	
	@Override
	protected boolean updateServer() {
		if (refreshFlag) {
			refreshReactor();
		}
		updateActivity();
		
		if (type == FissionReactorType.PEBBLE_BED) {
			
		}
		else if (type == FissionReactorType.SOLID_FUEL) {
			long oldHeatStored = heatBuffer.getHeatStored();
			heatBuffer.changeHeatStored(rawHeating);
			updateFluidHeating();
			netHeating = netHeating == Long.MIN_VALUE ? getRawNetHeating() : heatBuffer.getHeatStored() - oldHeatStored;
		}
		else {
			heatBuffer.changeHeatStored(getRawNetHeating());
		}
		
		if (heatBuffer.isFull() && NCConfig.fission_overheat) {
			heatBuffer.setHeatStored(0);
			doMeltdown();
			return true;
		}
		if (!isReactorOn) {
			heatBuffer.changeHeatStored(-getHeatDissipation());
		}
		
		for (FissionCluster cluster : clusterMap.values()) {
			cluster.heatBuffer.changeHeatStored(cluster.getNetHeating());
			if (cluster.heatBuffer.isFull() && NCConfig.fission_overheat) {
				cluster.heatBuffer.setHeatStored(0);
				cluster.doMeltdown();
				return true;
			}
		}
		
		//updateRedstonePorts();
		sendUpdateToListeningPlayers();
		
		return true;
	}
	
	public void updateActivity() {
		boolean wasReactorOn = isReactorOn;
		isReactorOn = rawHeating > 0L && isAssembled();
		if (isReactorOn != wasReactorOn) {
			if (controller != null) controller.updateBlockState(isReactorOn);
			sendUpdateToAllPlayers();
		}
	}
	
	/*protected void updateRedstonePorts() {
		comparatorSignal = (int) MathHelper.clamp(1500D/NCConfig.fission_comparator_max_heat*heatBuffer.heatStored/heatBuffer.heatCapacity, 0D, 15D);
		for (TileSaltFissionRedstonePort redstonePort : redstonePorts) {
			if (redstonePort.comparatorSignal != comparatorSignal) {
				redstonePort.comparatorSignal = comparatorSignal;
				WORLD.updateComparatorOutputLevel(redstonePort.getPos(), null);
			}
		}
	}*/
	
	public long getRawNetHeating() {
		return rawHeating - cooling;
	}
	
	public int getTemperature() {
		return Math.round(ambientTemp + (MAX_TEMP - ambientTemp)*(float)heatBuffer.getHeatStored()/heatBuffer.getHeatCapacity());
	}
	
	public float getBurnDamage() {
		return getTemperature() < 373 ? 0F : 1F + (getTemperature() - 373)/200F;
	}
	
	//TODO - config
	public long getHeatDissipation() {
		return Math.max(1L, heatBuffer.getHeatStored()*getExteriorSurfaceArea()/(NCMath.cube(6)*672000L));
	}
	
	public void updateFluidHeating() {
		if (isReactorOn) {
			refreshRecipe();
			if (canProcessInputs()) {
				produceProducts();
				return;
			}
		}
		recipeRate = heatRemovalRecipeRate = 0;
		roundedOutputRate = 0D;
	}
	
	protected void refreshRecipe() {
		recipeInfo = HEATING_RECIPE_HANDLER.getRecipeInfoFromInputs(new ArrayList<ItemStack>(), tanks.subList(0, 1));
	}
	
	protected boolean canProcessInputs() {
		if (!isReactorOn || !setRecipeStats()) return false;
		return canProduceProducts();
	}
	
	protected boolean setRecipeStats() {
		if (recipeInfo == null) {
			recipeRate = heatRemovalRecipeRate = 0;
			heatPerInputSize = 128D;
			return false;
		}
		ProcessorRecipe recipe = recipeInfo.getRecipe();
		heatPerInputSize = (double)recipe.getFissionHeatingHeatPerInputMB()/(double)recipe.fluidIngredients().get(0).getMaxStackSize(recipeInfo.getFluidIngredientNumbers().get(0));
		return true;
	}
	
	protected boolean canProduceProducts() {
		IFluidIngredient fluidProduct = recipeInfo.getRecipe().fluidProducts().get(0);
		if (fluidProduct.getMaxStackSize(0) <= 0 || fluidProduct.getStack() == null) return false;
		
		recipeRate = Math.min(tanks.get(0).getFluidAmount(), (int) getMaxRecipeRate(false, true));
		heatRemovalRecipeRate = Math.min(tanks.get(0).getFluidAmount(), (int) getMaxRecipeRate(true, true));
		roundedOutputRate = Math.min(tanks.get(0).getFluidAmount(), getMaxRecipeRate(false, false));
		
		if (!tanks.get(1).isEmpty()) {
			if (!tanks.get(1).getFluid().isFluidEqual(fluidProduct.getStack())) {
				return false;
			} else if (tanks.get(1).getFluidAmount() + fluidProduct.getMaxStackSize(0)*recipeRate > tanks.get(1).getCapacity()) {
				return false;
			}
		}
		return true;
	}
	
	public void produceProducts() {
		boolean isMaxRecipeRate = roundedOutputRate == getMaxRecipeRate(false, false);
		
		int fluidIngredientStackSize = recipeInfo.getRecipe().fluidIngredients().get(0).getMaxStackSize(recipeInfo.getFluidIngredientNumbers().get(0))*recipeRate;
		if (fluidIngredientStackSize > 0) tanks.get(0).changeFluidAmount(-fluidIngredientStackSize);
		if (tanks.get(0).getFluidAmount() <= 0) tanks.get(0).setFluidStored(null);
		
		IFluidIngredient fluidProduct = recipeInfo.getRecipe().fluidProducts().get(0);
		if (fluidProduct.getMaxStackSize(0) > 0) {
			if (tanks.get(1).isEmpty()) {
				tanks.get(1).setFluidStored(fluidProduct.getNextStack(0));
				int amount = tanks.get(1).getFluidAmount();
				tanks.get(1).setFluidAmount(amount*recipeRate);
				roundedOutputRate *= amount;
			} else if (tanks.get(1).getFluid().isFluidEqual(fluidProduct.getStack())) {
				int amount = fluidProduct.getNextStackSize(0);
				tanks.get(1).changeFluidAmount(amount*recipeRate);
				roundedOutputRate *= amount;
			}
		}
		
		long heatRemoval = (isMaxRecipeRate && getRawNetHeating() == 0) ? cooling : Math.min(Integer.MAX_VALUE, Math.round(heatRemovalRecipeRate/getRecipeRateMult()));
		if (isMaxRecipeRate) {
			if (heatRemoval <= heatBuffer.getHeatStored()) {
				heatRemoval = cooling;
			}
			if (heatRemoval > heatBuffer.getHeatStored()) {
				netHeating = Long.MIN_VALUE;
			}
		}
		heatBuffer.changeHeatStored(-heatRemoval);
	}
	
	protected double getMaxRecipeRate(boolean heatRemoval, boolean ceil) {
		double rate = Math.min(heatBuffer.getHeatStored(), (heatRemoval ? cooling : rawHeating)*getRecipeRateMult());
		return Math.min(Integer.MAX_VALUE, (ceil && heatBuffer.getHeatStored() > rawHeating && cooling >= rawHeating) ? Math.ceil(rate) : rate);
	}
	
	protected double getRecipeRateMult() {
		return rawHeating == 0 ? 0D : effectiveHeating/(rawHeating*heatPerInputSize);
	}
	
	protected void doMeltdown() {
		Iterator<IFissionController> controllerIterator = controllerMap.values().iterator();
		while (controllerIterator.hasNext()) {
			IFissionController controller = controllerIterator.next();
			controllerIterator.remove();
			controller.doMeltdown();
		}
		
		//TODO - graphite fires
		
		//TODO - explosions if vents are present, melt casing if not
		if (ventMap.isEmpty()) {
			
		}
		else {
			
		}
		
		/*IBlockState corium = RegistryHelper.getBlock(Global.MOD_ID + ":fluid_corium").getDefaultState();
		for (BlockPos blockPos : BlockPos.getAllInBoxMutable(getMinimumCoord(), getMaximumCoord())) {
			if (rand.nextDouble() < 0.18D) {
				WORLD.removeTileEntity(blockPos);
				WORLD.setBlockState(blockPos, corium);
			}
		}*/
		
		checkIfMachineIsWhole();
	}
	
	// Client
	
	@Override
	protected void updateClient() {
		if (isReactorOn) {
			int i;
			if (type == FissionReactorType.PEBBLE_BED) {
				
			}
			else if (type == FissionReactorType.SOLID_FUEL) {
				i = cellMap.size();
				for (TileSolidFissionCell cell : cellMap.values()) {
					if (rand.nextDouble() < 1D/i && playFissionSound(cell)) return;
					if (cell.isFunctional()) i--;
				}
			}
			else {
				i = vesselMap.size();
				for (TileSaltFissionVessel vessel : vesselMap.values()) {
					if (rand.nextDouble() < 1D/i && playFissionSound(vessel)) return;
					if (vessel.isFunctional()) i--;
				}
			}
		}
	}
	
	protected boolean playFissionSound(IFissionFuelComponent fuelComponent) {
		if (fuelComponentCount <= 0) return true;
		double soundRate = Math.min(1D, meanEfficiency/(14D*NCConfig.fission_max_size));
		if (rand.nextDouble() < soundRate) {
			WORLD.playSound(fuelComponent.getTilePos().getX(), fuelComponent.getTilePos().getY(), fuelComponent.getTilePos().getZ(), NCSounds.geiger_tick, SoundCategory.BLOCKS, (float) (1.6D*Math.log1p(Math.sqrt(fuelComponentCount))), 1F + 0.12F*(rand.nextFloat() - 0.5F), false);
			return true;
		}
		return false;
	}
	
	// NBT
	
	@Override
	protected void syncDataTo(NBTTagCompound data, SyncReason syncReason) {
		heatBuffer.writeToNBT(data);
		writeTanks(data);
		data.setInteger("comparatorSignal", comparatorSignal);
		data.setBoolean("isReactorOn", isReactorOn);
		data.setInteger("clusterCount", clusterCount);
		data.setLong("cooling", cooling);
		data.setLong("rawHeating", rawHeating);
		data.setDouble("effectiveHeating", effectiveHeating);
		data.setLong("totalHeatMult", totalHeatMult);
		data.setDouble("meanHeatMult", meanHeatMult);
		data.setInteger("fuelComponentCount", fuelComponentCount);
		data.setLong("usefulPartCount", usefulPartCount);
		data.setDouble("totalEfficiency", totalEfficiency);
		data.setDouble("meanEfficiency", meanEfficiency);
		data.setDouble("sparsityEfficiencyMult", sparsityEfficiencyMult);
		data.setLong("netHeating", netHeating);
	}
	
	@Override
	protected void syncDataFrom(NBTTagCompound data, SyncReason syncReason) {
		heatBuffer.readFromNBT(data);
		readTanks(data);
		comparatorSignal = data.getInteger("comparatorSignal");
		isReactorOn = data.getBoolean("isReactorOn");
		clusterCount = data.getInteger("clusterCount");
		cooling = data.getLong("cooling");
		rawHeating = data.getLong("rawHeating");
		effectiveHeating = data.getDouble("effectiveHeating");
		totalHeatMult = data.getLong("totalHeatMult");
		meanHeatMult = data.getDouble("meanHeatMult");
		fuelComponentCount = data.getInteger("fuelComponentCount");
		usefulPartCount = data.getLong("usefulPartCount");
		totalEfficiency = data.getDouble("totalEfficiency");
		meanEfficiency = data.getDouble("meanEfficiency");
		sparsityEfficiencyMult = data.getDouble("sparsityEfficiencyMult");
		netHeating = data.getLong("netHeating");
	}
	
	protected NBTTagCompound writeTanks(NBTTagCompound nbt) {
		if (!tanks.isEmpty()) for (int i = 0; i < tanks.size(); i++) {
			nbt.setInteger("capacity" + i, tanks.get(i).getCapacity());
			nbt.setInteger("fluidAmount" + i, tanks.get(i).getFluidAmount());
			nbt.setString("fluidName" + i, tanks.get(i).getFluidName());
		}
		return nbt;
	}
	
	protected void readTanks(NBTTagCompound nbt) {
		if (!tanks.isEmpty()) for (int i = 0; i < tanks.size(); i++) {
			tanks.get(i).setCapacity(nbt.getInteger("capacity" + i));
			if (nbt.getString("fluidName" + i).equals("nullFluid") || nbt.getInteger("fluidAmount" + i) == 0) tanks.get(i).setFluidStored(null);
			else tanks.get(i).setFluidStored(FluidRegistry.getFluid(nbt.getString("fluidName" + i)), nbt.getInteger("fluidAmount" + i));
		}
	}
	
	// Packets
	
	@Override
	protected FissionUpdatePacket getUpdatePacket() {
		/*if (type == FissionReactorType.PEBBLE_BED) {
			
		}
		else*/ if (type == FissionReactorType.SOLID_FUEL) {
			return new SolidFissionUpdatePacket(controller.getTilePos(), isReactorOn, clusterCount, cooling, rawHeating, effectiveHeating, totalHeatMult, meanHeatMult, fuelComponentCount, usefulPartCount, totalEfficiency, meanEfficiency, sparsityEfficiencyMult, heatBuffer.getHeatCapacity(), heatBuffer.getHeatStored(), roundedOutputRate, netHeating);
		}
		else {
			return new SaltFissionUpdatePacket(controller.getTilePos(), isReactorOn, clusterCount, cooling, rawHeating, effectiveHeating, totalHeatMult, meanHeatMult, fuelComponentCount, usefulPartCount, totalEfficiency, meanEfficiency, sparsityEfficiencyMult, heatBuffer.getHeatCapacity(), heatBuffer.getHeatStored(), roundedOutputRate, netHeating);
		}
	}
	
	@Override
	public void onPacket(FissionUpdatePacket message) {
		isReactorOn = message.isReactorOn;
		clusterCount = message.clusterCount;
		cooling = message.cooling;
		rawHeating = message.rawHeating;
		effectiveHeating = message.effectiveHeating;
		totalHeatMult = message.totalHeatMult;
		meanHeatMult = message.meanHeatMult;
		fuelComponentCount = message.fuelComponentCount;
		usefulPartCount = message.usefulPartCount;
		totalEfficiency = message.totalEfficiency;
		meanEfficiency = message.meanEfficiency;
		sparsityEfficiencyMult = message.sparsityEfficiencyMult;
		heatBuffer.setHeatCapacity(message.capacity);
		heatBuffer.setHeatStored(message.heat);
		roundedOutputRate = message.roundedOutputRate;
		netHeating = message.netHeating;
	}
	
	public Container getContainer(EntityPlayer player) {
		if (controller instanceof TileSolidFissionController) {
			return new ContainerSolidFissionController(player, (TileSolidFissionController) controller);
		}
		else {
			return new ContainerSaltFissionController(player, (TileSaltFissionController) controller);
		}
	}
	
	@Override
	public void clearAll() {
		for (Tank tank : tanks) {
			tank.setFluidStored(null);
		}
		
		for (TileFissionPort port : portMap.values()) {
			port.clearAllSlots();
			port.clearAllTanks();
		}
		for (TileFissionVent vent : ventMap.values()) vent.clearAllTanks();
		
		for (TileSolidFissionCell cell : cellMap.values()) cell.clearAllSlots();
		
		for (TileSaltFissionVessel vessel : vesselMap.values()) vessel.clearAllTanks();
		for (TileSaltFissionHeater heater : heaterMap.values()) heater.clearAllTanks();
	}
	
	// Multiblock Validators
	
	@Override
	protected boolean isBlockGoodForInterior(World world, int x, int y, int z, IMultiblockValidator validatorCallback) {
		return true;
	}
}
