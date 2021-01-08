package nc.multiblock.fission.salt.tile;

import static nc.config.NCConfig.enable_mek_gas;
import static nc.init.NCCoolantFluids.COOLANTS;
import static nc.util.FluidStackHelper.INGOT_BLOCK_VOLUME;
import static nc.util.PosHelper.DEFAULT_NON;

import java.util.*;

import javax.annotation.*;

import com.google.common.collect.Lists;

import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.objects.*;
import nc.ModCheck;
import nc.multiblock.PlacementRule;
import nc.multiblock.cuboidal.CuboidalPartPositionType;
import nc.multiblock.fission.*;
import nc.multiblock.fission.tile.*;
import nc.multiblock.fission.tile.IFissionFuelComponent.ModeratorBlockInfo;
import nc.multiblock.fission.tile.port.*;
import nc.multiblock.network.SaltFissionHeaterUpdatePacket;
import nc.recipe.*;
import nc.recipe.ingredient.IFluidIngredient;
import nc.tile.ITileGui;
import nc.tile.fluid.*;
import nc.tile.internal.fluid.*;
import nc.tile.processor.IFluidProcessor;
import nc.util.CapabilityHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

public class TileSaltFissionHeater extends TileFissionPart implements ITileFilteredFluid, ITileGui<SaltFissionHeaterUpdatePacket>, IFluidProcessor, IFissionCoolingComponent, IFissionPortTarget<TileFissionHeaterPort, TileSaltFissionHeater> {
	
	protected final @Nonnull List<Tank> tanks;
	protected final @Nonnull List<Tank> filterTanks;
	
	protected @Nonnull FluidConnection[] fluidConnections = ITileFluid.fluidConnectionAll(Lists.newArrayList(TankSorption.NON, TankSorption.NON));
	
	protected @Nonnull FluidTileWrapper[] fluidSides;
	
	protected @Nonnull GasTileWrapper gasWrapper;
	
	protected final int fluidInputSize = 1, fluidOutputSize = 1;
	
	protected int baseProcessCooling;
	protected PlacementRule<IFissionPart> placementRule = FissionPlacement.RULE_MAP.get("");
	
	public double heatingSpeedMultiplier; // Based on the cluster efficiency, but with heat/cooling taken into account
	
	public double time;
	public boolean isProcessing, canProcessInputs;
	
	protected RecipeInfo<BasicRecipe> recipeInfo;
	
	protected final Set<EntityPlayer> playersToUpdate;
	
	public String heaterName, coolantName;
	
	protected FissionCluster cluster = null;
	protected long heat = 0L;
	protected boolean isInValidPosition = false;
	
	public long clusterHeatStored, clusterHeatCapacity;
	
	protected BlockPos masterPortPos = DEFAULT_NON;
	protected TileFissionHeaterPort masterPort = null;
	
	/** Don't use this constructor! */
	public TileSaltFissionHeater() {
		super(CuboidalPartPositionType.INTERIOR);
		tanks = Lists.newArrayList(new Tank(INGOT_BLOCK_VOLUME, null), new Tank(INGOT_BLOCK_VOLUME, new ArrayList<>()));
		filterTanks = Lists.newArrayList(new Tank(1000, null), new Tank(1000, new ArrayList<>()));
		fluidSides = ITileFluid.getDefaultFluidSides(this);
		gasWrapper = new GasTileWrapper(this);
		
		playersToUpdate = new ObjectOpenHashSet<>();
	}
	
	public TileSaltFissionHeater(String heaterName, String coolantName) {
		this();
		this.heaterName = heaterName;
		this.coolantName = coolantName;
		tanks.get(0).setAllowedFluids(Lists.newArrayList(coolantName));
		filterTanks.get(0).setAllowedFluids(Lists.newArrayList(coolantName));
	}
	
	protected TileSaltFissionHeater(String heaterName, int coolantID) {
		this(heaterName, COOLANTS.get(coolantID) + "nak");
	}
	
	public static class Standard extends TileSaltFissionHeater {
		
		public Standard() {
			super("standard", 0);
		}
	}
	
	public static class Iron extends TileSaltFissionHeater {
		
		public Iron() {
			super("iron", 1);
		}
	}
	
	public static class Redstone extends TileSaltFissionHeater {
		
		public Redstone() {
			super("redstone", 2);
		}
	}
	
	public static class Quartz extends TileSaltFissionHeater {
		
		public Quartz() {
			super("quartz", 3);
		}
	}
	
	public static class Obsidian extends TileSaltFissionHeater {
		
		public Obsidian() {
			super("obsidian", 4);
		}
	}
	
	public static class NetherBrick extends TileSaltFissionHeater {
		
		public NetherBrick() {
			super("nether_brick", 5);
		}
	}
	
	public static class Glowstone extends TileSaltFissionHeater {
		
		public Glowstone() {
			super("glowstone", 6);
		}
	}
	
	public static class Lapis extends TileSaltFissionHeater {
		
		public Lapis() {
			super("lapis", 7);
		}
	}
	
	public static class Gold extends TileSaltFissionHeater {
		
		public Gold() {
			super("gold", 8);
		}
	}
	
	public static class Prismarine extends TileSaltFissionHeater {
		
		public Prismarine() {
			super("prismarine", 9);
		}
	}
	
	public static class Slime extends TileSaltFissionHeater {
		
		public Slime() {
			super("slime", 10);
		}
	}
	
	public static class EndStone extends TileSaltFissionHeater {
		
		public EndStone() {
			super("end_stone", 11);
		}
	}
	
	public static class Purpur extends TileSaltFissionHeater {
		
		public Purpur() {
			super("purpur", 12);
		}
	}
	
	public static class Diamond extends TileSaltFissionHeater {
		
		public Diamond() {
			super("diamond", 13);
		}
	}
	
	public static class Emerald extends TileSaltFissionHeater {
		
		public Emerald() {
			super("emerald", 14);
		}
	}
	
	public static class Copper extends TileSaltFissionHeater {
		
		public Copper() {
			super("copper", 15);
		}
	}
	
	public static class Tin extends TileSaltFissionHeater {
		
		public Tin() {
			super("tin", 16);
		}
	}
	
	public static class Lead extends TileSaltFissionHeater {
		
		public Lead() {
			super("lead", 17);
		}
	}
	
	public static class Boron extends TileSaltFissionHeater {
		
		public Boron() {
			super("boron", 18);
		}
	}
	
	public static class Lithium extends TileSaltFissionHeater {
		
		public Lithium() {
			super("lithium", 19);
		}
	}
	
	public static class Magnesium extends TileSaltFissionHeater {
		
		public Magnesium() {
			super("magnesium", 20);
		}
	}
	
	public static class Manganese extends TileSaltFissionHeater {
		
		public Manganese() {
			super("manganese", 21);
		}
	}
	
	public static class Aluminum extends TileSaltFissionHeater {
		
		public Aluminum() {
			super("aluminum", 22);
		}
	}
	
	public static class Silver extends TileSaltFissionHeater {
		
		public Silver() {
			super("silver", 23);
		}
	}
	
	public static class Fluorite extends TileSaltFissionHeater {
		
		public Fluorite() {
			super("fluorite", 24);
		}
	}
	
	public static class Villiaumite extends TileSaltFissionHeater {
		
		public Villiaumite() {
			super("villiaumite", 25);
		}
	}
	
	public static class Carobbiite extends TileSaltFissionHeater {
		
		public Carobbiite() {
			super("carobbiite", 26);
		}
	}
	
	public static class Arsenic extends TileSaltFissionHeater {
		
		public Arsenic() {
			super("arsenic", 27);
		}
	}
	
	public static class LiquidNitrogen extends TileSaltFissionHeater {
		
		public LiquidNitrogen() {
			super("liquid_nitrogen", 28);
		}
	}
	
	public static class LiquidHelium extends TileSaltFissionHeater {
		
		public LiquidHelium() {
			super("liquid_helium", 29);
		}
	}
	
	public static class Enderium extends TileSaltFissionHeater {
		
		public Enderium() {
			super("enderium", 30);
		}
	}
	
	public static class Cryotheum extends TileSaltFissionHeater {
		
		public Cryotheum() {
			super("cryotheum", 31);
		}
	}
	
	@Override
	public void onMachineAssembled(FissionReactor controller) {
		doStandardNullControllerResponse(controller);
		super.onMachineAssembled(controller);
	}
	
	@Override
	public void onMachineBroken() {
		super.onMachineBroken();
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
		if (!isProcessing(false, false) || componentFailCache.containsKey(pos.toLong())) {
			return isInValidPosition = false;
		}
		else if (placementRule.requiresRecheck()) {
			isInValidPosition = placementRule.satisfied(this);
			if (isInValidPosition) {
				assumedValidCache.put(pos.toLong(), this);
			}
			return isInValidPosition;
		}
		else if (isInValidPosition) {
			return true;
		}
		return isInValidPosition = placementRule.satisfied(this);
	}
	
	@Override
	public boolean isFunctional() {
		return isProcessing;
	}
	
	@Override
	public void resetStats() {
		isInValidPosition = false;
		heatingSpeedMultiplier = 0;
		
		refreshRecipe();
		refreshActivity();
		refreshIsProcessing(true, true);
	}
	
	@Override
	public boolean isClusterRoot() {
		return false;
	}
	
	@Override
	public void clusterSearch(Integer id, final Object2IntMap<IFissionComponent> clusterSearchCache, final Long2ObjectMap<IFissionComponent> componentFailCache, final Long2ObjectMap<IFissionComponent> assumedValidCache) {
		refreshRecipe();
		refreshActivity();
		
		IFissionCoolingComponent.super.clusterSearch(id, clusterSearchCache, componentFailCache, assumedValidCache);
		
		refreshIsProcessing(true, true);
	}
	
	public void refreshIsProcessing(boolean checkCluster, boolean checkValid) {
		isProcessing = isProcessing(checkCluster, checkValid);
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
	public void onClusterMeltdown(Iterator<IFissionComponent> componentIterator) {
		
	}
	
	@Override
	public boolean isNullifyingSources(EnumFacing side) {
		return false;
	}
	
	@Override
	public long getCooling() {
		return baseProcessCooling;
	}
	
	// Moderator Line
	
	@Override
	public ModeratorBlockInfo getModeratorBlockInfo(EnumFacing dir, boolean activeModeratorPos) {
		return new ModeratorBlockInfo(pos, this, false, false, 0, 0D);
	}
	
	@Override
	public void onAddedToModeratorCache(ModeratorBlockInfo thisInfo) {}
	
	// IFissionPortTarget
	
	@Override
	public BlockPos getMasterPortPos() {
		return masterPortPos;
	}
	
	@Override
	public void setMasterPortPos(BlockPos pos) {
		masterPortPos = pos;
	}
	
	@Override
	public void clearMasterPort() {
		masterPort = null;
		masterPortPos = DEFAULT_NON;
	}
	
	@Override
	public void refreshMasterPort() {
		masterPort = getMultiblock() == null ? null : getMultiblock().getPartMap(TileFissionHeaterPort.class).get(masterPortPos.toLong());
		if (masterPort == null) {
			masterPortPos = DEFAULT_NON;
		}
	}
	
	@Override
	public boolean onPortRefresh() {
		refreshRecipe();
		refreshActivity();
		refreshIsProcessing(true, true);
		
		return isMultiblockAssembled() && getMultiblock().isReactorOn && !isProcessing && isProcessing(false, true);
	}
	
	// Ticking
	
	@Override
	public void onLoad() {
		super.onLoad();
		if (!world.isRemote) {
			refreshRecipe();
			refreshActivity();
			refreshIsProcessing(true, true);
		}
	}
	
	@Override
	public void update() {
		if (!world.isRemote) {
			boolean wasProcessing = isProcessing;
			isProcessing = isProcessing(true, true);
			boolean shouldRefresh = isMultiblockAssembled() && getMultiblock().isReactorOn && !isProcessing && isProcessing(false, true);
			boolean shouldUpdate = wasProcessing != isProcessing;
			
			if (isProcessing) {
				process();
			}
			else {
				getRadiationSource().setRadiationLevel(0D);
			}
			
			if (shouldRefresh) {
				getMultiblock().refreshFlag = true;
			}
			
			sendUpdateToListeningPlayers();
			if (shouldUpdate) {
				markDirty();
			}
		}
	}
	
	@Override
	public void refreshRecipe() {
		recipeInfo = NCRecipes.coolant_heater.getRecipeInfoFromInputs(new ArrayList<>(), getFluidInputs());
	}
	
	@Override
	public void refreshActivity() {
		canProcessInputs = canProcessInputs();
	}
	
	@Override
	public void refreshActivityOnProduction() {
		canProcessInputs = canProcessInputs();
	}
	
	// Processor Stats
	
	public double getSpeedMultiplier() {
		return heatingSpeedMultiplier;
	}
	
	public boolean setRecipeStats() {
		if (recipeInfo == null) {
			baseProcessCooling = 0;
			placementRule = FissionPlacement.RULE_MAP.get("");
			return false;
		}
		baseProcessCooling = recipeInfo.getRecipe().getCoolantHeaterCoolingRate();
		placementRule = FissionPlacement.RULE_MAP.get(recipeInfo.getRecipe().getCoolantHeaterPlacementRule());
		return true;
	}
	
	// Processing
	
	public boolean isProcessing(boolean checkCluster, boolean checkValid) {
		return readyToProcess(checkCluster, checkValid);
	}
	
	public boolean readyToProcess(boolean checkCluster, boolean checkValid) {
		return canProcessInputs && isMultiblockAssembled() && !(checkCluster && cluster == null) && !(checkValid && !isInValidPosition);
	}
	
	public boolean canProcessInputs() {
		boolean validRecipe = setRecipeStats(), canProcess = validRecipe && canProduceProducts();
		if (!canProcess) {
			time = 0D;
		}
		return canProcess;
	}
	
	public boolean canProduceProducts() {
		for (int j = 0; j < fluidOutputSize; j++) {
			IFluidIngredient fluidProduct = getFluidProducts().get(j);
			if (fluidProduct.getMaxStackSize(0) <= 0) {
				continue;
			}
			if (fluidProduct.getStack() == null) {
				return false;
			}
			else if (!getTanks().get(j + fluidInputSize).isEmpty()) {
				if (!getTanks().get(j + fluidInputSize).getFluid().isFluidEqual(fluidProduct.getStack())) {
					return false;
				}
				else if (getTanks().get(j + fluidInputSize).getFluidAmount() + fluidProduct.getMaxStackSize(0) > getTanks().get(j + fluidInputSize).getCapacity()) {
					return false;
				}
			}
		}
		return true;
	}
	
	public void process() {
		time += getSpeedMultiplier();
		while (time >= 1D) {
			finishProcess();
		}
	}
	
	public void finishProcess() {
		int oldProcessCooling = baseProcessCooling;
		produceProducts();
		refreshRecipe();
		time = Math.max(0D, time - 1D);
		refreshActivityOnProduction();
		if (!canProcessInputs) {
			time = 0;
		}
		
		if (getMultiblock() != null) {
			if (canProcessInputs) {
				if (oldProcessCooling != baseProcessCooling) {
					getMultiblock().refreshCluster(cluster);
				}
			}
			else {
				getMultiblock().refreshFlag = true;
			}
		}
	}
	
	public void produceProducts() {
		if (recipeInfo == null) {
			return;
		}
		IntList fluidInputOrder = recipeInfo.getFluidInputOrder();
		if (fluidInputOrder == AbstractRecipeHandler.INVALID) {
			return;
		}
		
		for (int i = 0; i < fluidInputSize; i++) {
			int fluidIngredientStackSize = getFluidIngredients().get(fluidInputOrder.get(i)).getMaxStackSize(recipeInfo.getFluidIngredientNumbers().get(i));
			if (fluidIngredientStackSize > 0) {
				getTanks().get(i).changeFluidAmount(-fluidIngredientStackSize);
			}
			if (getTanks().get(i).getFluidAmount() <= 0) {
				getTanks().get(i).setFluidStored(null);
			}
		}
		for (int j = 0; j < fluidOutputSize; j++) {
			IFluidIngredient fluidProduct = getFluidProducts().get(j);
			if (fluidProduct.getMaxStackSize(0) <= 0) {
				continue;
			}
			if (getTanks().get(j + fluidInputSize).isEmpty()) {
				getTanks().get(j + fluidInputSize).setFluidStored(fluidProduct.getNextStack(0));
			}
			else if (getTanks().get(j + fluidInputSize).getFluid().isFluidEqual(fluidProduct.getStack())) {
				getTanks().get(j + fluidInputSize).changeFluidAmount(fluidProduct.getNextStackSize(0));
			}
		}
	}
	
	// IProcessor
	
	@Override
	public int getFluidInputSize() {
		return fluidInputSize;
	}
	
	@Override
	public int getFluidOutputputSize() {
		return fluidOutputSize;
	}
	
	@Override
	public List<Tank> getFluidInputs() {
		return getTanks().subList(0, fluidInputSize);
	}
	
	@Override
	public List<IFluidIngredient> getFluidIngredients() {
		return recipeInfo.getRecipe().getFluidIngredients();
	}
	
	@Override
	public List<IFluidIngredient> getFluidProducts() {
		return recipeInfo.getRecipe().getFluidProducts();
	}
	
	// Fluids
	
	@Override
	public @Nonnull List<Tank> getTanks() {
		return !DEFAULT_NON.equals(masterPortPos) ? masterPort.getTanks() : tanks;
	}
	
	@Override
	public @Nonnull FluidConnection[] getFluidConnections() {
		return fluidConnections;
	}
	
	@Override
	public void setFluidConnections(@Nonnull FluidConnection[] connections) {
		fluidConnections = connections;
	}
	
	@Override
	public @Nonnull FluidTileWrapper[] getFluidSides() {
		return !DEFAULT_NON.equals(masterPortPos) ? masterPort.getFluidSides() : fluidSides;
	}
	
	@Override
	public @Nonnull GasTileWrapper getGasWrapper() {
		return !DEFAULT_NON.equals(masterPortPos) ? masterPort.getGasWrapper() : gasWrapper;
	}
	
	public void pushCoolant(TileSaltFissionHeater other) {
		int diff = getTanks().get(0).getFluidAmount() - other.getTanks().get(0).getFluidAmount();
		if (diff > 1) {
			getTanks().get(0).drain(other.getTanks().get(0).fillInternal(getTanks().get(0).drain(diff / 2, false), true), true);
		}
	}
	
	public void pushHotCoolant(TileSaltFissionHeater other) {
		getTanks().get(1).drain(other.getTanks().get(1).fillInternal(getTanks().get(1).drain(getTanks().get(1).getCapacity(), false), true), true);
	}
	
	@Override
	public boolean getInputTanksSeparated() {
		return false;
	}
	
	@Override
	public void setInputTanksSeparated(boolean separated) {}
	
	@Override
	public boolean getVoidUnusableFluidInput(int tankNumber) {
		return false;
	}
	
	@Override
	public void setVoidUnusableFluidInput(int tankNumber, boolean voidUnusableFluidInput) {}
	
	@Override
	public TankOutputSetting getTankOutputSetting(int tankNumber) {
		return TankOutputSetting.DEFAULT;
	}
	
	@Override
	public void setTankOutputSetting(int tankNumber, TankOutputSetting setting) {}
	
	@Override
	public boolean hasConfigurableFluidConnections() {
		return true;
	}
	
	// ITileFilteredFluid
	
	@Override
	public @Nonnull List<Tank> getTanksInternal() {
		return tanks;
	}
	
	@Override
	public @Nonnull List<Tank> getFilterTanks() {
		return !DEFAULT_NON.equals(masterPortPos) ? masterPort.getFilterTanks() : filterTanks;
	}
	
	@Override
	public boolean canModifyFilter(int tank) {
		return getMultiblock() != null ? !getMultiblock().isAssembled() : true;
	}
	
	@Override
	public void onFilterChanged(int slot) {
		markDirty();
	}
	
	@Override
	public int getFilterID() {
		return coolantName.hashCode();
	}
	
	// ITileGui
	
	@Override
	public int getGuiID() {
		return 203;
	}
	
	@Override
	public Set<EntityPlayer> getPlayersToUpdate() {
		return playersToUpdate;
	}
	
	@Override
	public SaltFissionHeaterUpdatePacket getGuiUpdatePacket() {
		return new SaltFissionHeaterUpdatePacket(pos, masterPortPos, getTanks(), getFilterTanks(), cluster, isProcessing, time);
	}
	
	@Override
	public void onGuiPacket(SaltFissionHeaterUpdatePacket message) {
		masterPortPos = message.masterPortPos;
		if (DEFAULT_NON.equals(masterPortPos) ^ masterPort == null) {
			refreshMasterPort();
		}
		for (int i = 0; i < getTanks().size(); i++) {
			getTanks().get(i).readInfo(message.tanksInfo.get(i));
		}
		for (int i = 0; i < getFilterTanks().size(); i++) {
			getFilterTanks().get(i).readInfo(message.filterTanksInfo.get(i));
		}
		clusterHeatStored = message.clusterHeatStored;
		clusterHeatCapacity = message.clusterHeatCapacity;
		isProcessing = message.isProcessing;
		time = message.time;
	}
	
	// NBT
	
	@Override
	public NBTTagCompound writeAll(NBTTagCompound nbt) {
		super.writeAll(nbt);
		nbt.setString("heaterName", heaterName);
		nbt.setString("coolantName", coolantName);
		writeTanks(nbt);
		
		nbt.setInteger("baseProcessCooling", baseProcessCooling);
		nbt.setDouble("heatingSpeedMultiplier", heatingSpeedMultiplier);
		
		nbt.setDouble("time", time);
		nbt.setBoolean("isProcessing", isProcessing);
		nbt.setBoolean("canProcessInputs", canProcessInputs);
		
		nbt.setLong("clusterHeat", heat);
		nbt.setBoolean("isInValidPosition", isInValidPosition);
		return nbt;
	}
	
	@Override
	public void readAll(NBTTagCompound nbt) {
		super.readAll(nbt);
		if (nbt.hasKey("heaterName"))
			heaterName = nbt.getString("heaterName");
		if (nbt.hasKey("coolantName")) {
			coolantName = nbt.getString("coolantName");
			tanks.get(0).setAllowedFluids(Lists.newArrayList(coolantName));
			filterTanks.get(0).setAllowedFluids(Lists.newArrayList(coolantName));
		}
		readTanks(nbt);
		
		baseProcessCooling = nbt.getInteger("baseProcessCooling");
		heatingSpeedMultiplier = nbt.getDouble("heatingSpeedMultiplier");
		
		time = nbt.getDouble("time");
		isProcessing = nbt.getBoolean("isProcessing");
		canProcessInputs = nbt.getBoolean("canProcessInputs");
		
		heat = nbt.getLong("clusterHeat");
		isInValidPosition = nbt.getBoolean("isInValidPosition");
	}
	
	@Override
	public NBTTagCompound writeTanks(NBTTagCompound nbt) {
		for (int i = 0; i < tanks.size(); i++) {
			tanks.get(i).writeToNBT(nbt, "tanks" + i);
		}
		for (int i = 0; i < filterTanks.size(); i++) {
			filterTanks.get(i).writeToNBT(nbt, "filterTanks" + i);
		}
		return nbt;
	}
	
	@Override
	public void readTanks(NBTTagCompound nbt) {
		for (int i = 0; i < tanks.size(); i++) {
			tanks.get(i).readFromNBT(nbt, "tanks" + i);
		}
		for (int i = 0; i < filterTanks.size(); i++) {
			filterTanks.get(i).readFromNBT(nbt, "filterTanks" + i);
		}
	}
	
	// Capability
	
	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing side) {
		if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY || ModCheck.mekanismLoaded() && enable_mek_gas && capability == CapabilityHelper.GAS_HANDLER_CAPABILITY) {
			return !getTanks().isEmpty() && hasFluidSideCapability(side);
		}
		return super.hasCapability(capability, side);
	}
	
	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing side) {
		if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
			if (!getTanks().isEmpty() && hasFluidSideCapability(side)) {
				return (T) getFluidSide(nonNullSide(side));
			}
			return null;
		}
		else if (ModCheck.mekanismLoaded() && capability == CapabilityHelper.GAS_HANDLER_CAPABILITY) {
			if (enable_mek_gas && !getTanks().isEmpty() && hasFluidSideCapability(side)) {
				return (T) getGasWrapper();
			}
			return null;
		}
		return super.getCapability(capability, side);
	}
}
