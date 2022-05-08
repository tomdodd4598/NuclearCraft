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
import nc.network.multiblock.SaltFissionHeaterUpdatePacket;
import nc.recipe.*;
import nc.recipe.ingredient.IFluidIngredient;
import nc.tile.ITileGui;
import nc.tile.fluid.*;
import nc.tile.generator.IFluidGenerator;
import nc.tile.internal.fluid.*;
import nc.util.CapabilityHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

public class TileSaltFissionHeater extends TileFissionPart implements ITileFilteredFluid, ITileGui<SaltFissionHeaterUpdatePacket>, IFluidGenerator, IFissionCoolingComponent, IFissionPortTarget<TileFissionHeaterPort, TileSaltFissionHeater> {
	
	protected final @Nonnull List<Tank> tanks = Lists.newArrayList(new Tank(INGOT_BLOCK_VOLUME, null), new Tank(INGOT_BLOCK_VOLUME, new ArrayList<>()));
	protected final @Nonnull List<Tank> filterTanks = Lists.newArrayList(new Tank(1000, null), new Tank(1000, new ArrayList<>()));
	protected final @Nonnull List<Tank> consumedTanks = Lists.newArrayList(new Tank(INGOT_BLOCK_VOLUME, new ArrayList<>()));
	
	protected @Nonnull FluidConnection[] fluidConnections = ITileFluid.fluidConnectionAll(Lists.newArrayList(TankSorption.NON, TankSorption.NON));
	
	protected @Nonnull FluidTileWrapper[] fluidSides;
	
	protected @Nonnull GasTileWrapper gasWrapper;
	
	protected final int fluidInputSize = 1, fluidOutputSize = 1;
	
	protected int baseProcessCooling;
	protected PlacementRule<FissionReactor, IFissionPart> placementRule = FissionPlacement.RULE_MAP.get("");
	
	public double heatingSpeedMultiplier; // Based on the cluster efficiency, but with heat/cooling taken into account
	
	public double time;
	public boolean isProcessing, hasConsumed, canProcessInputs;
	
	protected RecipeInfo<BasicRecipe> recipeInfo;
	
	protected final Set<EntityPlayer> updatePacketListeners;
	
	public String heaterType, coolantName;
	
	protected FissionCluster cluster = null;
	protected long heat = 0L;
	protected boolean isInValidPosition = false;
	
	public long clusterHeatStored, clusterHeatCapacity;
	
	protected BlockPos masterPortPos = DEFAULT_NON;
	protected TileFissionHeaterPort masterPort = null;
	
	/** Don't use this constructor! */
	public TileSaltFissionHeater() {
		super(CuboidalPartPositionType.INTERIOR);
		fluidSides = ITileFluid.getDefaultFluidSides(this);
		gasWrapper = new GasTileWrapper(this);
		
		updatePacketListeners = new ObjectOpenHashSet<>();
	}
	
	public TileSaltFissionHeater(String heaterType, String coolantName) {
		this();
		this.heaterType = heaterType;
		this.coolantName = coolantName;
		tanks.get(0).setAllowedFluids(Lists.newArrayList(coolantName));
		filterTanks.get(0).setAllowedFluids(Lists.newArrayList(coolantName));
	}
	
	protected static class Meta extends TileSaltFissionHeater {
		
		public Meta(String heaterType, String coolantName) {
			super(heaterType, coolantName);
		}
		
		protected Meta(int coolantID) {
			super(COOLANTS.get(coolantID), COOLANTS.get(coolantID) + "_nak");
		}
		
		@Override
		public boolean shouldRefresh(World worldIn, BlockPos posIn, IBlockState oldState, IBlockState newState) {
			return oldState != newState;
		}
	}
	
	public static class Standard extends Meta {
		
		public Standard() {
			super("standard", "nak");
		}
	}
	
	public static class Iron extends Meta {
		
		public Iron() {
			super(1);
		}
	}
	
	public static class Redstone extends Meta {
		
		public Redstone() {
			super(2);
		}
	}
	
	public static class Quartz extends Meta {
		
		public Quartz() {
			super(3);
		}
	}
	
	public static class Obsidian extends Meta {
		
		public Obsidian() {
			super(4);
		}
	}
	
	public static class NetherBrick extends Meta {
		
		public NetherBrick() {
			super(5);
		}
	}
	
	public static class Glowstone extends Meta {
		
		public Glowstone() {
			super(6);
		}
	}
	
	public static class Lapis extends Meta {
		
		public Lapis() {
			super(7);
		}
	}
	
	public static class Gold extends Meta {
		
		public Gold() {
			super(8);
		}
	}
	
	public static class Prismarine extends Meta {
		
		public Prismarine() {
			super(9);
		}
	}
	
	public static class Slime extends Meta {
		
		public Slime() {
			super(10);
		}
	}
	
	public static class EndStone extends Meta {
		
		public EndStone() {
			super(11);
		}
	}
	
	public static class Purpur extends Meta {
		
		public Purpur() {
			super(12);
		}
	}
	
	public static class Diamond extends Meta {
		
		public Diamond() {
			super(13);
		}
	}
	
	public static class Emerald extends Meta {
		
		public Emerald() {
			super(14);
		}
	}
	
	public static class Copper extends Meta {
		
		public Copper() {
			super(15);
		}
	}
	
	public static class Tin extends Meta {
		
		public Tin() {
			super(16);
		}
	}
	
	public static class Lead extends Meta {
		
		public Lead() {
			super(17);
		}
	}
	
	public static class Boron extends Meta {
		
		public Boron() {
			super(18);
		}
	}
	
	public static class Lithium extends Meta {
		
		public Lithium() {
			super(19);
		}
	}
	
	public static class Magnesium extends Meta {
		
		public Magnesium() {
			super(20);
		}
	}
	
	public static class Manganese extends Meta {
		
		public Manganese() {
			super(21);
		}
	}
	
	public static class Aluminum extends Meta {
		
		public Aluminum() {
			super(22);
		}
	}
	
	public static class Silver extends Meta {
		
		public Silver() {
			super(23);
		}
	}
	
	public static class Fluorite extends Meta {
		
		public Fluorite() {
			super(24);
		}
	}
	
	public static class Villiaumite extends Meta {
		
		public Villiaumite() {
			super(25);
		}
	}
	
	public static class Carobbiite extends Meta {
		
		public Carobbiite() {
			super(26);
		}
	}
	
	public static class Arsenic extends Meta {
		
		public Arsenic() {
			super(27);
		}
	}
	
	public static class LiquidNitrogen extends Meta {
		
		public LiquidNitrogen() {
			super(28);
		}
	}
	
	public static class LiquidHelium extends Meta {
		
		public LiquidHelium() {
			super(29);
		}
	}
	
	public static class Enderium extends Meta {
		
		public Enderium() {
			super(30);
		}
	}
	
	public static class Cryotheum extends Meta {
		
		public Cryotheum() {
			super(31);
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
		hasConsumed = hasConsumed();
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
	
	// Processing
	
	@Override
	public void onLoad() {
		super.onLoad();
		if (!world.isRemote) {
			refreshMasterPort();
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
			
			sendTileUpdatePacketToListeners();
			if (shouldUpdate) {
				markDirty();
			}
		}
	}
	
	@Override
	public void refreshRecipe() {
		recipeInfo = NCRecipes.coolant_heater.getRecipeInfoFromHeaterInputs(heaterType, getFluidInputs(hasConsumed));
		consumeInputs();
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
		BasicRecipe recipe = recipeInfo.getRecipe();
		baseProcessCooling = recipe.getCoolantHeaterCoolingRate();
		placementRule = FissionPlacement.RULE_MAP.get(recipe.getCoolantHeaterPlacementRule());
		return true;
	}
	
	// Processing
	
	public boolean isProcessing(boolean checkCluster, boolean checkValid) {
		return readyToProcess(checkCluster, checkValid);
	}
	
	public boolean readyToProcess(boolean checkCluster, boolean checkValid) {
		return canProcessInputs && hasConsumed && isMultiblockAssembled() && !(checkCluster && cluster == null) && !(checkValid && !isInValidPosition);
	}
	
	public boolean hasConsumed() {
		if (world.isRemote) {
			return hasConsumed;
		}
		for (int i = 0; i < fluidInputSize; ++i) {
			if (!consumedTanks.get(i).isEmpty()) {
				return true;
			}
		}
		return false;
	}
	
	public boolean canProcessInputs() {
		boolean validRecipe = setRecipeStats(), canProcess = validRecipe && canProduceProducts();
		if (hasConsumed && !validRecipe) {
			for (Tank tank : getFluidInputs(true)) {
				tank.setFluidStored(null);
			}
			hasConsumed = false;
		}
		if (!canProcess) {
			time = 0D;
		}
		return canProcess;
	}
	
	public boolean canProduceProducts() {
		for (int j = 0; j < fluidOutputSize; ++j) {
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
	
	public void consumeInputs() {
		if (hasConsumed || recipeInfo == null) {
			return;
		}
		IntList fluidInputOrder = recipeInfo.getFluidInputOrder();
		if (fluidInputOrder == AbstractRecipeHandler.INVALID) {
			return;
		}
		
		for (int i = 0; i < fluidInputSize; ++i) {
			if (!consumedTanks.get(i).isEmpty()) {
				consumedTanks.get(i).setFluidStored(null);
			}
		}
		for (int i = 0; i < fluidInputSize; ++i) {
			Tank tank = getTanks().get(i);
			int maxStackSize = getFluidIngredients().get(fluidInputOrder.get(i)).getMaxStackSize(recipeInfo.getFluidIngredientNumbers().get(i));
			if (maxStackSize > 0) {
				consumedTanks.get(i).setFluidStored(new FluidStack(tank.getFluid(), maxStackSize));
				tank.changeFluidAmount(-maxStackSize);
			}
			if (tank.isEmpty()) {
				tank.setFluidStored(null);
			}
		}
		hasConsumed = true;
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
					getMultiblock().addClusterToRefresh(cluster);
				}
			}
			else {
				getMultiblock().refreshFlag = true;
			}
		}
	}
	
	public void produceProducts() {
		for (int i = 0; i < fluidInputSize; ++i) {
			consumedTanks.get(i).setFluidStored(null);
		}
		
		if (!hasConsumed || recipeInfo == null) {
			return;
		}
		
		for (int j = 0; j < fluidOutputSize; ++j) {
			IFluidIngredient fluidProduct = getFluidProducts().get(j);
			if (fluidProduct.getNextStackSize(0) <= 0) {
				continue;
			}
			if (getTanks().get(j + fluidInputSize).isEmpty()) {
				getTanks().get(j + fluidInputSize).setFluidStored(fluidProduct.getNextStack(0));
			}
			else if (getTanks().get(j + fluidInputSize).getFluid().isFluidEqual(fluidProduct.getStack())) {
				getTanks().get(j + fluidInputSize).changeFluidAmount(fluidProduct.getNextStackSize(0));
			}
		}
		hasConsumed = false;
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
	public int getOtherSlotsSize() {
		return 0;
	}
	
	@Override
	public List<Tank> getFluidInputs(boolean consumed) {
		return consumed ? consumedTanks : getTanks().subList(0, fluidInputSize);
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
		return false;
	}
	
	@Override
	public void clearAllTanks() {
		IFluidGenerator.super.clearAllTanks();
		for (Tank tank : consumedTanks) {
			tank.setFluidStored(null);
		}
		
		hasConsumed = false;
		refreshRecipe();
		refreshActivity();
		refreshIsProcessing(true, true);
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
	public Object getFilterKey() {
		return heaterType;
	}
	
	// ITileGui
	
	@Override
	public int getGuiID() {
		return 203;
	}
	
	@Override
	public Set<EntityPlayer> getTileUpdatePacketListeners() {
		return updatePacketListeners;
	}
	
	@Override
	public SaltFissionHeaterUpdatePacket getTileUpdatePacket() {
		return new SaltFissionHeaterUpdatePacket(pos, masterPortPos, getTanks(), getFilterTanks(), cluster, isProcessing, time);
	}
	
	@Override
	public void onTileUpdatePacket(SaltFissionHeaterUpdatePacket message) {
		masterPortPos = message.masterPortPos;
		if (DEFAULT_NON.equals(masterPortPos) ^ masterPort == null) {
			refreshMasterPort();
		}
		for (int i = 0; i < getTanks().size(); ++i) {
			getTanks().get(i).readInfo(message.tanksInfo.get(i));
		}
		for (int i = 0; i < getFilterTanks().size(); ++i) {
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
		nbt.setString("heaterName", heaterType);
		nbt.setString("coolantName", coolantName);
		writeTanks(nbt);
		
		nbt.setInteger("baseProcessCooling", baseProcessCooling);
		nbt.setDouble("heatingSpeedMultiplier", heatingSpeedMultiplier);
		
		nbt.setDouble("time", time);
		nbt.setBoolean("isProcessing", isProcessing);
		nbt.setBoolean("hasConsumed", hasConsumed);
		nbt.setBoolean("canProcessInputs", canProcessInputs);
		
		nbt.setLong("clusterHeat", heat);
		nbt.setBoolean("isInValidPosition", isInValidPosition);
		return nbt;
	}
	
	@Override
	public void readAll(NBTTagCompound nbt) {
		super.readAll(nbt);
		if (nbt.hasKey("heaterName")) {
			heaterType = nbt.getString("heaterName");
		}
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
		hasConsumed = nbt.getBoolean("hasConsumed");
		canProcessInputs = nbt.getBoolean("canProcessInputs");
		
		heat = nbt.getLong("clusterHeat");
		isInValidPosition = nbt.getBoolean("isInValidPosition");
	}
	
	@Override
	public NBTTagCompound writeTanks(NBTTagCompound nbt) {
		for (int i = 0; i < tanks.size(); ++i) {
			tanks.get(i).writeToNBT(nbt, "tanks" + i);
		}
		for (int i = 0; i < filterTanks.size(); ++i) {
			filterTanks.get(i).writeToNBT(nbt, "filterTanks" + i);
		}
		for (int i = 0; i < consumedTanks.size(); ++i) {
			consumedTanks.get(i).writeToNBT(nbt, "consumedTanks" + i);
		}
		return nbt;
	}
	
	@Override
	public void readTanks(NBTTagCompound nbt) {
		for (int i = 0; i < tanks.size(); ++i) {
			tanks.get(i).readFromNBT(nbt, "tanks" + i);
		}
		for (int i = 0; i < filterTanks.size(); ++i) {
			filterTanks.get(i).readFromNBT(nbt, "filterTanks" + i);
		}
		for (int i = 0; i < consumedTanks.size(); ++i) {
			consumedTanks.get(i).readFromNBT(nbt, "consumedTanks" + i);
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
				return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(getFluidSide(nonNullSide(side)));
			}
			return null;
		}
		else if (ModCheck.mekanismLoaded() && capability == CapabilityHelper.GAS_HANDLER_CAPABILITY) {
			if (enable_mek_gas && !getTanks().isEmpty() && hasFluidSideCapability(side)) {
				return CapabilityHelper.GAS_HANDLER_CAPABILITY.cast(getGasWrapper());
			}
			return null;
		}
		return super.getCapability(capability, side);
	}
}
