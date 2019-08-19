package nc.tile.generator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import javax.annotation.Nullable;

import org.cyclops.commoncapabilities.api.capability.temperature.ITemperature;
import org.cyclops.commoncapabilities.api.capability.work.IWorker;

import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;
import li.cil.oc.api.network.SimpleComponent;
import nc.Global;
import nc.ModCheck;
import nc.capability.radiation.source.IRadiationSource;
import nc.config.NCConfig;
import nc.enumm.MetaEnums.CoolerType;
import nc.handler.SoundHandler;
import nc.handler.SoundHandler.SoundInfo;
import nc.init.NCBlocks;
import nc.init.NCSounds;
import nc.network.tile.FusionUpdatePacket;
import nc.radiation.RadiationHelper;
import nc.recipe.NCRecipes;
import nc.recipe.ProcessorRecipe;
import nc.tile.IGui;
import nc.tile.fluid.TileActiveCooler;
import nc.tile.internal.energy.EnergyConnection;
import nc.tile.internal.fluid.Tank;
import nc.util.BlockFinder;
import nc.util.BlockPosHelper;
import nc.util.CommonCapsHelper;
import nc.util.EnergyHelper;
import nc.util.Lang;
import nc.util.MaterialHelper;
import nc.util.SoundHelper;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Optional.InterfaceList({@Optional.Interface(iface = "li.cil.oc.api.network.SimpleComponent", modid = "opencomputers"), @Optional.Interface(iface = "org.cyclops.commoncapabilities.api.capability.temperature.ITemperature", modid = "commoncapabilities"), @Optional.Interface(iface = "org.cyclops.commoncapabilities.api.capability.work.IWorker", modid = "commoncapabilities")})
public class TileFusionCore extends TileFluidGenerator implements IGui<FusionUpdatePacket>, SimpleComponent, ITemperature, IWorker {
	
	public static final double ROOM_TEMP = 0.298D;
	
	public double processHeatVariable = 0;
	public double heat = ROOM_TEMP, efficiency, cooling, heatChange; // cooling and heatChange are in K, not kK
	public int currentEnergyStored = 0, energyChange = 0;
	
	public int size = 1;
	public int complete;
	public String problem = RING_INCOMPLETE;
	
	public static final String RING_INCOMPLETE = Lang.localise("gui.container.fusion_core.ring_incomplete");
	public static final String RING_BLOCKED = Lang.localise("gui.container.fusion_core.ring_blocked");
	public static final String POWER_ISSUE = Lang.localise("gui.container.fusion_core.power_issue");
	public static final String INCORRECT_STRUCTURE = Lang.localise("gui.container.fusion_core.incorrect_structure");
	public static final String NO_PROBLEM = Lang.localise("gui.container.fusion_core.no_problem");
	
	public static final String NO_FUEL = Lang.localise("gui.container.fusion_core.empty");
	
	public boolean computerActivated = true;
	public int structureCount = 0;
	
	private BlockFinder finder;
	
	@SideOnly(Side.CLIENT)
	private List<SoundInfo> activeSounds;
	private int soundCount = 0;
	private boolean updateSoundInfo = true;
	private final Random rand = new Random();
	
	public TileFusionCore() {
		super("fusion_core", 2, 4, 0, defaultItemSorptions(), defaultTankCapacities(32000, 2, 4), defaultTankSorptions(2, 4), NCRecipes.fusion_valid_fluids, maxPower(), NCRecipes.fusion);
		setInputTanksSeparated(false);
	}
	
	private static int maxPower() {
		double max = 0D;
		List<ProcessorRecipe> recipes = NCRecipes.fusion.getRecipeList();
		for (ProcessorRecipe recipe : recipes) {
			if (recipe == null) continue;
			max = Math.max(max, recipe.getFusionComboPower());
		}
		return (int) Math.min(100*max*NCConfig.fusion_base_power*NCConfig.fusion_max_size, Integer.MAX_VALUE);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public AxisAlignedBB getRenderBoundingBox() {
		return new AxisAlignedBB(pos.add(-1, 0, -1), pos.add(2, 3, 2));
	}
	
	// Ticking
	
	@Override
	public void onAdded() {
		finder = new BlockFinder(pos, world);
		super.onAdded();
	}
	
	@Override
	public void updateGenerator() {
		if (!world.isRemote) {
			boolean wasProcessing = isProcessing;
			isProcessing = isProcessing();
			if (structureCount == 0) refreshMultiblock();
			tickStructureCheck();
			energyChange = getEnergyStored() - currentEnergyStored;
			double previousHeat = heat;
			run();
			if (isHotEnough()) doCooling();
			else plasma(false);
			doHeating();
			heatChange = 1000*(heat - previousHeat);
			if (overheat()) return;
			if (isProcessing) process();
			else getRadiationSource().setRadiationLevel(0D);
			if (wasProcessing != isProcessing) {
				if (isProcessing || recipeInfo == null) {
					plasma(isProcessing);
				}
				updateBlockType();
				sendUpdateToAllPlayers();
			}
			if (isHotEnough()) pushEnergy();
			sendUpdateToListeningPlayers();
		} else {
			updateSounds();
		}
	}
	
	public int getComparatorStrength() {
		double strength = getAlternateComparator() ? heat/getMaxHeat() : efficiency/NCConfig.fusion_comparator_max_efficiency;
		return (int) MathHelper.clamp(15D*strength, 0, 15);
	}
	
	public void tickStructureCheck() {
		structureCount++; structureCount %= 4*NCConfig.machine_update_rate;
	}
	
	public void refreshMultiblock() {
		setSize();
		if (NCConfig.fusion_active_cooling) setCooling();
		refreshActivity();
	}
	
	@Override
	public void updateBlockType() {
		super.updateBlockType();
		refreshMultiblock();
	}
	
	// Dummies can't be checked directly as they are transparent
	@Override
	public boolean checkIsRedstonePowered(World world, BlockPos dummyPos) {
		if (world.isBlockPowered(pos)) return true;
		for (BlockPos ringPos : new BlockPosHelper(pos).squareRing(1, 0)) if (world.isBlockPowered(ringPos)) return true;
		return false;
	}
	
	public void onFusionPacket(boolean isProcessing, double efficiency, boolean computerActivated) {
		this.isProcessing = isProcessing;
		this.efficiency = efficiency;
		this.computerActivated = computerActivated;
	}
	
	private boolean overheat() {
		if (heat >= getMaxHeat() && NCConfig.fusion_overheat) {
			meltdown();
			return true;
		}
		return false;
	}
	
	public void meltdown() {
		IRadiationSource chunkSource = RadiationHelper.getRadiationSource(world.getChunk(pos));
		if (chunkSource != null) {
			RadiationHelper.addToSourceRadiation(chunkSource, 8D*baseProcessRadiation*size*NCConfig.fusion_fuel_use*NCConfig.fusion_meltdown_radiation_multiplier);
		}
		
		world.removeTileEntity(pos);
		world.destroyBlock(pos, false);
		world.setBlockState(pos, Blocks.LAVA.getDefaultState());
		
		BlockPosHelper helper = new BlockPosHelper(pos);
		for (BlockPos pos : helper.squareRing(ringRadius(), 1)) world.setBlockState(pos, Blocks.LAVA.getStateFromMeta(1));
		for (BlockPos pos : helper.squareTube(ringRadius(), 1)) {
			world.removeTileEntity(pos);
			world.setBlockToAir(pos);
		}
	}
	
	@SideOnly(Side.CLIENT)
	private void updateSounds() {
		if (!NCConfig.fusion_enable_sound) {
			return;
		}
		
		if (activeSounds == null) {
			activeSounds = new ArrayList<>();
		}
		
		if (isProcessing && !isInvalid()) {
			if (--soundCount > 0) {
				return;
			}
			
			// Generate sound info if necessary
			if (updateSoundInfo) {
				stopSounds();
				activeSounds.clear();
				if (size < 2) {
					addSoundInfo(0, 1, 0);
				}
				else {
					if (size > 4) {
						addSoundInfo(0, 1, 0);
					}
					addSoundInfo(ringRadius(), 1, ringRadius());
					addSoundInfo(ringRadius(), 1, -ringRadius());
					addSoundInfo(-ringRadius(), 1, ringRadius());
					addSoundInfo(-ringRadius(), 1, -ringRadius());
					if (size > 8) {
						addSoundInfo(ringRadius(), 1, 0);
						addSoundInfo(-ringRadius(), 1, 0);
						addSoundInfo(0, 1, ringRadius());
						addSoundInfo(0, 1, -ringRadius());
					}
				}
				updateSoundInfo = false;
			}
			
			// If this machine isn't playing sounds, go ahead and play them
			for (SoundInfo activeSound : activeSounds) {
				if (activeSound != null && (activeSound.sound == null || !Minecraft.getMinecraft().getSoundHandler().isSoundPlaying(activeSound.sound))) {
					activeSound.sound = SoundHandler.startTileSound(NCSounds.fusion_run, activeSound.pos, 0.35F, SoundHelper.getPitch(1F));
				}
			}
			
			// Always reset the count
			soundCount = 116;
		}
		else {
			stopSounds();
		}
	}
	
	@SideOnly(Side.CLIENT)
	private void stopSounds() {
		for (SoundInfo activeSound : activeSounds) {
			if (activeSound != null) {
				SoundHandler.stopTileSound(activeSound.pos);
				activeSound.sound = null;
			}
		}
		soundCount = 0;
	}
	
	@SideOnly(Side.CLIENT)
	private void addSoundInfo(int x, int y, int z) {
		activeSounds.add(new SoundInfo(null, new BlockPos(pos.getX() + x, pos.getY() + y, pos.getZ() + z)));
	}
	
	@Override
	public void invalidate() {
		super.invalidate();
		if (world.isRemote) {
			updateSounds();
		}
	}
	
	// Processor Stats
	
	@Override
	public boolean setRecipeStats() {
		if (recipeInfo == null) {
			baseProcessTime = defaultProcessTime;
			baseProcessPower = defaultProcessPower;
			processHeatVariable = 1000D;
			baseProcessRadiation = 0D;
			return false;
		}
		baseProcessTime = recipeInfo.getRecipe().getFusionComboTime();
		baseProcessPower = recipeInfo.getRecipe().getFusionComboPower();
		processHeatVariable = recipeInfo.getRecipe().getFusionComboHeatVariable();
		baseProcessRadiation = recipeInfo.getRecipe().getFusionComboRadiation();
		return true;
	}
	
	// Processing
	
	@Override
	public boolean isProcessing() {
		return readyToProcess() && !isDeactivated();
	}
	
	private boolean isDeactivated() { // Because reactor is on by default
		return getIsRedstonePowered() || !computerActivated;
	}
	
	@Override
	public boolean readyToProcess() {
		return canProcessInputs && hasConsumed && isHotEnough() && complete == 1;
	}
	
	// Energy Connections
	
	@Override
	public boolean canExtractEnergy(EnumFacing side) {
		return isHotEnough();
	}

	@Override
	public boolean canReceiveEnergy(EnumFacing side) {
		return !isHotEnough();
	}
	
	public boolean isHotEnough() {
		return heat > 8000;
	}
	
	// IC2 Tiers

	@Override
	public int getEUSourceTier() {
		return EnergyHelper.getEUTier(processPower);
	}
	
	@Override
	public int getEUSinkTier() {
		return 10;
	}
	
	// Reactor Stats
	
	public static double getMaxHeat() {
		return 20000000D;
	}
	
	public int ringRadius() {
		return size + 2;
	}
	
	// Setting Blocks
	
	private static IBlockState plasmaState() {
		return FluidRegistry.getFluid("plasma").getBlock().getDefaultState();
	}
	
	public void plasma(boolean createPlasma) {
		setSize();
		if (complete == 0) return;
		BlockPosHelper helper = new BlockPosHelper(pos);
		if (createPlasma) {
			for (BlockPos pos : helper.squareRing(ringRadius(), 1)) world.setBlockState(pos, plasmaState());
		}
		else {
			for (BlockPos pos : helper.squareRing(ringRadius(), 1)) world.setBlockToAir(pos);
		}
	}
	
	// Finding Blocks
	
	private boolean findIdleElectromagnet(BlockPos pos) {
		return finder.find(pos, NCBlocks.fusion_electromagnet_idle, NCBlocks.fusion_electromagnet_transparent_idle);
	}
	
	private boolean findActiveElectromagnet(BlockPos pos) {
		return finder.find(pos, NCBlocks.fusion_electromagnet_active, NCBlocks.fusion_electromagnet_transparent_active);
	}
	
	private boolean findElectromagnet(BlockPos pos) {
		return finder.find(pos, NCBlocks.fusion_electromagnet_active, NCBlocks.fusion_electromagnet_transparent_active, NCBlocks.fusion_electromagnet_idle, NCBlocks.fusion_electromagnet_transparent_idle);
	}
	
	private boolean findAir(BlockPos pos) {
		Material mat = finder.getBlockState(pos).getMaterial();
		return MaterialHelper.isReplaceable(mat) || findPlasma(pos);
	}
	
	private boolean findPlasma(BlockPos pos) {
		return finder.find(pos, plasmaState());
	}
	
	private TileActiveCooler findActiveCooler(BlockPos pos) {
		TileEntity tile = world.getTileEntity(pos);
		if (tile instanceof TileActiveCooler) return (TileActiveCooler) tile;
		return null;
	}
	
	// Finding Ring
	
	public void setSize() {
		int runningSize = 1;
		for (int r = 0; r <= NCConfig.fusion_max_size; r++) {
			if (finder.horizontalY(pos.offset(EnumFacing.UP), r + 2, NCBlocks.fusion_connector)) {
				runningSize ++;
			} else break;
		}
		size = runningSize;
		BlockPosHelper helper = new BlockPosHelper(pos);
		for (BlockPos pos : helper.squareTube(ringRadius(), 1)) {
			if (!(findElectromagnet(pos))) {
				complete = 0;
				problem = RING_INCOMPLETE;
				return;
			}
		}
		for (BlockPos pos : helper.squareRing(ringRadius(), 1)) {
			if (!(findAir(pos))) {
				complete = 0;
				problem = RING_BLOCKED;
				return;
			}
		}
		for (BlockPos pos : helper.squareTube(ringRadius(), 1)) {
			if (!(findActiveElectromagnet(pos))) {
				complete = 0;
				problem = POWER_ISSUE;
				return;
			}
		}
		complete = 1;
		problem = NO_PROBLEM;
	}
	
	// Set Fuel and Power and Modify Heat
	
	public void run() {
		efficiency = efficiency();
		double heatChange = 0;
		if (isProcessing) {
			heatChange = NCConfig.fusion_heat_generation*(100D - (0.9*efficiency))/2D;
			processPower = MathHelper.clamp(efficiency, 0D, 100D)*NCConfig.fusion_base_power*size*baseProcessPower;
			speedMultiplier = size*NCConfig.fusion_fuel_use;
		} else {
			heatChange = 0;
			processPower = 0;
			speedMultiplier = 0;
			
			if (heat >= 1.005D*ROOM_TEMP) {
				heat = heat-((heat/100000D)*Math.log10(1000D*(heat-ROOM_TEMP)));
			}
		}
		
		if (heat + heatChange >= ROOM_TEMP) {
			heat += heatChange;
		} else {
			heat = ROOM_TEMP;
		}
	}
	
	public double efficiency() {
		if (!readyToProcess()) return 0;
		else if (isHotEnough()) {
			double heatMK = heat/1000D;
			double z = 7.415D*(Math.exp(-heatMK/processHeatVariable)+Math.tanh(heatMK/processHeatVariable)-1);
			return 100*Math.pow(z, 2);
	   	} else return 0;
	}
	
	public void doHeating() {
		if (!readyToProcess()) {
			double r = 0.0001D*getEnergyStorage().getEnergyStored()/(ringRadius());
			getEnergyStorage().setEnergyStored(0);
			heat = heat + r*NCConfig.fusion_heating_multiplier;
			setEnergyConnectionAll(EnergyConnection.IN);
			if (heat < ROOM_TEMP) heat = ROOM_TEMP;
		}
		else setEnergyConnectionAll(EnergyConnection.OUT);
	}
	
	public BlockPos getOpposite(BlockPos pos) {
		BlockPos relativePos = new BlockPos(pos.getX() - this.pos.getX(), pos.getY() - this.pos.getY(), pos.getZ() - this.pos.getZ());
		return finder.position(-relativePos.getX(), -relativePos.getY() + 2, -relativePos.getZ());
	}
	
	public void setCooling() {
		if (complete == 1) {
			List<BlockPos> posList = new ArrayList<BlockPos>();
			BlockPosHelper helper = new BlockPosHelper(pos);
			for (BlockPos pos : helper.squareTubeDiagonals(ringRadius(), 1)) {
				TileActiveCooler cooler = findActiveCooler(pos);
				if (cooler != null) if (cooler.getTanks().get(0).getFluidAmount() > 0) posList.add(pos);
			}
			if (posList.isEmpty()) {
				cooling = 0D;
				return;
			}
			double cooled = 0; // in kK
			double currentHeat = heat;
			for (BlockPos pos : posList) {
				TileActiveCooler cooler = findActiveCooler(pos);
				if (cooler == null) continue;
				Tank tank = cooler.getTanks().get(0);
				int fluidAmount = Math.min(tank.getFluidAmount(), (4*NCConfig.machine_update_rate*NCConfig.active_cooler_max_rate)/20);
				if (currentHeat > ROOM_TEMP) {
					double cool_mult = posList.contains(getOpposite(pos)) ? NCConfig.fusion_heat_generation*4 : NCConfig.fusion_heat_generation;
					for (int i = 1; i < CoolerType.values().length; i++) if (tank.getFluidName().equals(CoolerType.values()[i].getFluidName())) {
						cooled += (NCConfig.fusion_active_cooling_rate[i - 1]*fluidAmount*cool_mult)/(size*1000D);
						break;
					}
					currentHeat -= cooled;
					if (currentHeat > ROOM_TEMP) tank.drain(fluidAmount, true);
				}
			}
			cooling = Math.round(1000D*cooled/(4*NCConfig.machine_update_rate));
		}
	}
	
	public void doCooling() {
		double coolingkK = cooling/1000D;
		if (heat - coolingkK < ROOM_TEMP) heat = ROOM_TEMP;
		else heat -= coolingkK;
	}
	
	// NBT
	
	@Override
	public NBTTagCompound writeAll(NBTTagCompound nbt) {
		super.writeAll(nbt);
		nbt.setDouble("baseProcessTime", baseProcessTime);
		nbt.setDouble("processPower", processPower);
		nbt.setDouble("speedMultiplier", speedMultiplier);
		nbt.setDouble("heat", heat);
		nbt.setDouble("cooling", cooling);
		nbt.setDouble("heatChange", heatChange);
		nbt.setDouble("efficiency", efficiency);
		nbt.setInteger("size", size);
		nbt.setInteger("complete", complete);
		nbt.setString("problem", problem);
		nbt.setBoolean("computerActivated", computerActivated);
		nbt.setInteger("currentEnergyStored", currentEnergyStored);
		return nbt;
	}
			
	@Override
	public void readAll(NBTTagCompound nbt) {
		super.readAll(nbt);
		baseProcessTime = nbt.getDouble("baseProcessTime");
		processPower = nbt.getDouble("processPower");
		speedMultiplier = nbt.getDouble("speedMultiplier");
		heat = nbt.getDouble("heat");
		cooling = nbt.getDouble("cooling");
		heatChange = nbt.getDouble("heatChange");
		efficiency = nbt.getDouble("efficiency");
		size = nbt.getInteger("size");
		complete = nbt.getInteger("complete");
		problem = nbt.getString("problem");
		computerActivated = nbt.getBoolean("computerActivated");
		currentEnergyStored = nbt.getInteger("currentEnergyStored");
	}
	
	// IGui
	
	@Override
	public int getGuiID() {
		return 101;
	}
	
	@Override
	public Set<EntityPlayer> getPlayersToUpdate() {
		return playersToUpdate;
	}
	
	@Override
	public FusionUpdatePacket getGuiUpdatePacket() {
		return new FusionUpdatePacket(pos, time, getEnergyStored(), baseProcessTime, baseProcessPower, processPower, isProcessing, heat, efficiency, speedMultiplier, size, complete, cooling, heatChange, hasConsumed, computerActivated, problem, getTanks());
	}
	
	@Override
	public void onGuiPacket(FusionUpdatePacket message) {
		time = message.time;
		getEnergyStorage().setEnergyStored(message.energyStored);
		baseProcessTime = message.baseProcessTime;
		baseProcessPower = message.baseProcessPower;
		processPower = message.processPower;
		boolean wasProcessing = isProcessing;
		isProcessing = message.isProcessing;
		updateSoundInfo = updateSoundInfo || (wasProcessing != isProcessing);
		heat = message.heat;
		efficiency = message.efficiency;
		speedMultiplier = message.speedMultiplier;
		size = message.size;
		complete = message.complete;
		cooling = message.cooling;
		heatChange = message.heatChange;
		hasConsumed = message.hasConsumed;
		computerActivated = message.computerActivated;
		problem = message.problem;
		for (int i = 0; i < getTanks().size(); i++) getTanks().get(i).readInfo(message.tanksInfo.get(i));
	}
	
	// Capability
	
	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing side) {
		if (ModCheck.commonCapabilitiesLoaded() && (capability == CommonCapsHelper.CAPABILITY_TEMPERATURE || capability == CommonCapsHelper.CAPABILITY_WORKER)) {
			return true;
		}
		return super.hasCapability(capability, side);
	}
	
	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing side) {
		if (ModCheck.commonCapabilitiesLoaded()) {
			if (capability == CommonCapsHelper.CAPABILITY_TEMPERATURE) {
				return CommonCapsHelper.CAPABILITY_TEMPERATURE.cast(this);
			}
			if (capability == CommonCapsHelper.CAPABILITY_WORKER) {
				return CommonCapsHelper.CAPABILITY_WORKER.cast(this);
			}
		}
		return super.getCapability(capability, side);
	}
	
	// OpenComputers
	
	@Override
	@Optional.Method(modid = "opencomputers")
	public String getComponentName() {
		return Global.MOD_SHORT_ID + "_fusion_reactor";
	}
	
	@Callback
	@Optional.Method(modid = "opencomputers")
	public Object[] isComplete(Context context, Arguments args) {
		return new Object[] {complete == 1};
	}
	
	@Callback
	@Optional.Method(modid = "opencomputers")
	public Object[] isProcessing(Context context, Arguments args) {
		return new Object[] {isProcessing};
	}
	
	@Callback
	@Optional.Method(modid = "opencomputers")
	public Object[] isHotEnough(Context context, Arguments args) {
		return new Object[] {isHotEnough()};
	}
	
	@Callback
	@Optional.Method(modid = "opencomputers")
	public Object[] getProblem(Context context, Arguments args) {
		return new Object[] {problem};
	}
	
	@Callback
	@Optional.Method(modid = "opencomputers")
	public Object[] getToroidSize(Context context, Arguments args) {
		return new Object[] {size};
	}
	
	@Callback
	@Optional.Method(modid = "opencomputers")
	public Object[] getEnergyStored(Context context, Arguments args) {
		return new Object[] {getEnergyStored()};
	}
	
	@Callback
	@Optional.Method(modid = "opencomputers")
	public Object[] getMaxEnergyStored(Context context, Arguments args) {
		return new Object[] {getMaxEnergyStored()};
	}
	
	@Callback
	@Optional.Method(modid = "opencomputers")
	public Object[] getEnergyChange(Context context, Arguments args) {
		return new Object[] {energyChange};
	}
	
	@Callback
	@Optional.Method(modid = "opencomputers")
	public Object[] getCurrentProcessTime(Context context, Arguments args) {
		return new Object[] {time};
	}
	
	@Callback
	@Optional.Method(modid = "opencomputers")
	public Object[] getTemperature(Context context, Arguments args) {
		return new Object[] {1000D*heat};
	}
	
	@Callback
	@Optional.Method(modid = "opencomputers")
	public Object[] getMaxTemperature(Context context, Arguments args) {
		return new Object[] {1000D*getMaxHeat()};
	}
	
	@Callback
	@Optional.Method(modid = "opencomputers")
	public Object[] getEfficiency(Context context, Arguments args) {
		return new Object[] {efficiency};
	}
	
	@Callback
	@Optional.Method(modid = "opencomputers")
	public Object[] getFusionComboTime(Context context, Arguments args) {
		return new Object[] {recipeInfo != null ? baseProcessTime : 0D};
	}
	
	@Callback
	@Optional.Method(modid = "opencomputers")
	public Object[] getFusionComboPower(Context context, Arguments args) {
		return new Object[] {baseProcessPower};
	}
	
	@Callback
	@Optional.Method(modid = "opencomputers")
	public Object[] getFusionComboHeatVariable(Context context, Arguments args) {
		return new Object[] {recipeInfo != null ? processHeatVariable : 0D};
	}
	
	@Callback
	@Optional.Method(modid = "opencomputers")
	public Object[] getFirstFusionFuel(Context context, Arguments args) {
		return new Object[] {recipeInfo != null ? getFluidIngredients().get(0).getIngredientName() : NO_FUEL};
	}
	
	@Callback
	@Optional.Method(modid = "opencomputers")
	public Object[] getSecondFusionFuel(Context context, Arguments args) {
		return new Object[] {recipeInfo != null ? getFluidIngredients().get(1).getIngredientName() : NO_FUEL};
	}
	
	@Callback
	@Optional.Method(modid = "opencomputers")
	public Object[] getReactorProcessTime(Context context, Arguments args) {
		return new Object[] {recipeInfo != null ? (size == 0 ? baseProcessTime : baseProcessTime/size) : 0D};
	}
	
	@Callback
	@Optional.Method(modid = "opencomputers")
	public Object[] getReactorProcessPower(Context context, Arguments args) {
		return new Object[] {processPower};
	}
	
	@Callback
	@Optional.Method(modid = "opencomputers")
	public Object[] getReactorProcessHeat(Context context, Arguments args) {
		return new Object[] {heatChange};
	}
	
	@Callback
	@Optional.Method(modid = "opencomputers")
	public Object[] getReactorCoolingRate(Context context, Arguments args) {
		return new Object[] {cooling};
	}
	
	@Callback
	@Optional.Method(modid = "opencomputers")
	public Object[] activate(Context context, Arguments args) {
		computerActivated = true;
		return new Object[] {};
	}
	
	@Callback
	@Optional.Method(modid = "opencomputers")
	public Object[] deactivate(Context context, Arguments args) {
		computerActivated = false;
		return new Object[] {};
	}
	
	// CommonCapabilities
	
	@Override
	@Optional.Method(modid = "commoncapabilities")
	public double getTemperature() {
		return 1000D*heat;
	}
	
	@Override
	@Optional.Method(modid = "commoncapabilities")
	public double getMaximumTemperature() {
		return 1000D*getMaxHeat();
	}
	
	@Override
	@Optional.Method(modid = "commoncapabilities")
	public double getMinimumTemperature() {
		return 1000D*ROOM_TEMP;
	}
	
	@Override
	@Optional.Method(modid = "commoncapabilities")
	public double getDefaultTemperature() {
		return 1000D*ROOM_TEMP;
	}
	
	@Override
	@Optional.Method(modid = "commoncapabilities")
	public boolean hasWork() {
		return isProcessing;
	}
	
	@Override
	@Optional.Method(modid = "commoncapabilities")
	public boolean canWork() {
		return readyToProcess();
	}
}
