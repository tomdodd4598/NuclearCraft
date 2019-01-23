package nc.multiblock.saltFission;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

import com.google.common.collect.Lists;

import nc.Global;
import nc.config.NCConfig;
import nc.handler.SoundHandler;
import nc.multiblock.IMultiblockPart;
import nc.multiblock.MultiblockBase;
import nc.multiblock.TileBeefBase.SyncReason;
import nc.multiblock.container.ContainerSaltFissionController;
import nc.multiblock.cuboidal.CuboidalMultiblockBase;
import nc.multiblock.network.SaltFissionUpdatePacket;
import nc.multiblock.saltFission.tile.TileSaltFissionController;
import nc.multiblock.saltFission.tile.TileSaltFissionDistributor;
import nc.multiblock.saltFission.tile.TileSaltFissionHeater;
import nc.multiblock.saltFission.tile.TileSaltFissionModerator;
import nc.multiblock.saltFission.tile.TileSaltFissionRedstonePort;
import nc.multiblock.saltFission.tile.TileSaltFissionRetriever;
import nc.multiblock.saltFission.tile.TileSaltFissionVent;
import nc.multiblock.saltFission.tile.TileSaltFissionVessel;
import nc.multiblock.validation.IMultiblockValidator;
import nc.tile.internal.fluid.Tank;
import nc.tile.internal.heat.HeatBuffer;
import nc.util.RegistryHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class SaltFissionReactor extends CuboidalMultiblockBase<SaltFissionUpdatePacket> {
	
	private Set<TileSaltFissionController> controllers;
	private Set<TileSaltFissionVent> vents;
	private Set<TileSaltFissionVessel> vessels;
	private Set<TileSaltFissionHeater> heaters;
	private Set<TileSaltFissionModerator> moderators;
	private Set<TileSaltFissionDistributor> distributors;
	private Set<TileSaltFissionRetriever> retrievers;
	private Set<TileSaltFissionRedstonePort> redstonePorts;
	
	private TileSaltFissionController controller;
	
	private Random rand = new Random();
	
	public final HeatBuffer heatBuffer;
	private static final int BASE_MAX_HEAT = 25000;
	
	public int comparatorSignal = 0;
	private int updateCount = 0, distributeCount = 0;
	
	public boolean isReactorOn;
	public double cooling, heating, efficiency, heatMult, coolingRate;
	
	private short heaterCheckCount = 0;

	public SaltFissionReactor(World world) {
		super(world);
		
		controllers = new HashSet<TileSaltFissionController>();
		vents = new HashSet<TileSaltFissionVent>();
		vessels = new HashSet<TileSaltFissionVessel>();
		heaters = new HashSet<TileSaltFissionHeater>();
		moderators = new HashSet<TileSaltFissionModerator>();
		distributors = new HashSet<TileSaltFissionDistributor>();
		retrievers = new HashSet<TileSaltFissionRetriever>();
		redstonePorts = new HashSet<TileSaltFissionRedstonePort>();
		
		heatBuffer = new HeatBuffer(BASE_MAX_HEAT);
	}
	
	// Multiblock Part Getters
	
	public Set<TileSaltFissionController> getControllers() {
		return controllers;
	}
	
	public Set<TileSaltFissionVent> getVents() {
		return vents;
	}
	
	public Set<TileSaltFissionVessel> getVessels() {
		return vessels;
	}
	
	public Set<TileSaltFissionHeater> getHeaters() {
		return heaters;
	}
	
	public Set<TileSaltFissionModerator> getModerators() {
		return moderators;
	}
	
	public Set<TileSaltFissionDistributor> getDistributors() {
		return distributors;
	}
	
	public Set<TileSaltFissionRetriever> getRetrievers() {
		return retrievers;
	}
	
	public Set<TileSaltFissionRedstonePort> getRedstonePorts() {
		return redstonePorts;
	}
	
	// Multiblock Size Limits
	
	@Override
	protected int getMinimumInteriorLength() {
		return NCConfig.salt_fission_min_size;
	}
	
	@Override
	protected int getMaximumInteriorLength() {
		return NCConfig.salt_fission_max_size;
	}
	
	// Multiblock Methods
	
	@Override
	public void onAttachedPartWithMultiblockData(IMultiblockPart part, NBTTagCompound data) {
		syncDataFrom(data, SyncReason.FullSync);
	}
	
	@Override
	protected void onBlockAdded(IMultiblockPart newPart) {
		if (newPart instanceof TileSaltFissionController) controllers.add((TileSaltFissionController) newPart);
		else if (newPart instanceof TileSaltFissionVent) vents.add((TileSaltFissionVent) newPart);
		else if (newPart instanceof TileSaltFissionVessel) vessels.add((TileSaltFissionVessel) newPart);
		else if (newPart instanceof TileSaltFissionHeater) heaters.add((TileSaltFissionHeater) newPart);
		else if (newPart instanceof TileSaltFissionModerator) moderators.add((TileSaltFissionModerator) newPart);
		else if (newPart instanceof TileSaltFissionDistributor) distributors.add((TileSaltFissionDistributor) newPart);
		else if (newPart instanceof TileSaltFissionRetriever) retrievers.add((TileSaltFissionRetriever) newPart);
		else if (newPart instanceof TileSaltFissionRedstonePort) redstonePorts.add((TileSaltFissionRedstonePort) newPart);
	}
	
	@Override
	protected void onBlockRemoved(IMultiblockPart oldPart) {
		if (oldPart instanceof TileSaltFissionController) controllers.remove(oldPart);
		else if (oldPart instanceof TileSaltFissionVent) vents.remove(oldPart);
		else if (oldPart instanceof TileSaltFissionVessel) vessels.remove(oldPart);
		else if (oldPart instanceof TileSaltFissionHeater) heaters.remove(oldPart);
		else if (oldPart instanceof TileSaltFissionModerator) moderators.remove(oldPart);
		else if (oldPart instanceof TileSaltFissionDistributor) distributors.remove(oldPart);
		else if (oldPart instanceof TileSaltFissionRetriever) retrievers.remove(oldPart);
		else if (oldPart instanceof TileSaltFissionRedstonePort) redstonePorts.remove(oldPart);
	}
	
	@Override
	protected void onMachineAssembled() {
		for (TileSaltFissionController contr : controllers) controller = contr;
		onReactorFormed();
	}
	
	@Override
	protected void onMachineRestored() {
		onReactorFormed();
	}
	
	protected void onReactorFormed() {
		setIsReactorOn();
		
		heatBuffer.setHeatCapacity(BASE_MAX_HEAT*getNumConnectedBlocks());
		
		updateReactorStats();
	}
	
	@Override
	protected void onMachinePaused() {
		
	}
	
	@Override
	protected void onMachineDisassembled() {
		isReactorOn = false;
		if (controller != null) controller.updateBlock(false);
		cooling = heating = efficiency = heatMult = coolingRate = 0D;
	}
	
	@Override
	protected boolean isMachineWhole(IMultiblockValidator validatorCallback) {
		
		// Only one controller
		
		if (controllers.size() == 0) {
			validatorCallback.setLastError(Global.MOD_ID + ".multiblock_validation.no_controller", null);
			return false;
		}
		if (controllers.size() > 1) {
			validatorCallback.setLastError(Global.MOD_ID + ".multiblock_validation.too_many_controllers", null);
			return false;
		}
		
		return super.isMachineWhole(validatorCallback);
	}
	
	@Override
	protected void onAssimilate(MultiblockBase assimilated) {
		SaltFissionReactor newController = (SaltFissionReactor) assimilated;
		heatBuffer.mergeHeatBuffers(newController.heatBuffer);
	}
	
	@Override
	protected void onAssimilated(MultiblockBase assimilator) {
		
	}
	
	// Server
	
	@Override
	protected boolean updateServer() {
		//setIsReactorOn();
		if (shouldDistribute()) {
			distributeFuel();
		}
		if (shouldUpdate()) {
			updateReactorStats();
			updateRedstonePorts();
		}
		if (heatBuffer.isFull() && NCConfig.salt_fission_overheat) {
			heatBuffer.setHeatStored(0);
			doMeltdown();
			return true;
		}
		if (shouldUpdate()) sendUpdateToListeningPlayers();
		incrementUpdateCount();
		return true;
	}
	
	public void setIsReactorOn() {
		boolean oldIsReactorOn = isReactorOn;
		isReactorOn = isRedstonePowered() && isAssembled();
		if (isReactorOn != oldIsReactorOn) {
			if (controller != null) controller.updateBlock(isReactorOn);
			sendUpdateToAllPlayers();
		}
	}
	
	protected boolean isRedstonePowered() {
		if (controller != null && controller.checkIsRedstonePowered(WORLD, controller.getPos())) return true;
		for (TileSaltFissionRedstonePort redstonePort : redstonePorts) {
			if (redstonePort.checkIsRedstonePowered(WORLD, redstonePort.getPos())) return true;
		}
		return false;
	}
	
	protected void distributeFuel() {
		if (distributors.size() < 1 || vessels.size() < 1) return;
		
		for (TileSaltFissionVessel vessel : vessels) {
			vessel.distributedTo = false;
			vessel.retrievedFrom = false;
		}
		
		final int rate = Math.max(1, NCConfig.salt_fission_max_distribution_rate/vessels.size());
		
		distributorLoop: for (TileSaltFissionDistributor distributor : distributors) {
			int count = NCConfig.salt_fission_max_distribution_rate;
			Tank distributorTank = distributor.getTanks().get(0);
			
			for (TileSaltFissionVessel vessel : vessels) {
				if (count < 1 || distributorTank.isEmpty()) continue distributorLoop;
				if (vessel.distributedTo) continue;
				Tank vesselTank = vessel.getTanks().get(0);
				
				distributorTank.drainInternal(vesselTank.fillInternal(distributorTank.drainInternal(rate, false), true), true);
				vessel.distributedTo = true;
				count -= rate;
			}
		}
		
		retrieverLoop: for (TileSaltFissionRetriever retriever : retrievers) {
			int count = NCConfig.salt_fission_max_distribution_rate;
			Tank retrieverTank = retriever.getTanks().get(0);
			
			for (TileSaltFissionVessel vessel : vessels) {
				if (count < 1 || retrieverTank.isFull()) continue retrieverLoop;
				if (vessel.retrievedFrom) continue;
				Tank vesselTank = vessel.getTanks().get(1);
				
				vesselTank.drainInternal(retrieverTank.fillInternal(vesselTank.drainInternal(rate, false), true), true);
				vessel.retrievedFrom = true;
				count -= rate;
			}
		}
	}
	
	protected void updateReactorStats() {
		setVesselStats();
		doHeaterPlacementChecks();
		setCooling();
		heatBuffer.changeHeatStored((long) (updateTime()*getHeatChange(true)));
		setCoolingRate();
	}
	
	protected void updateRedstonePorts() {
		comparatorSignal = (int) MathHelper.clamp(1500D/(double)NCConfig.fission_comparator_max_heat*(double)heatBuffer.heatStored/(double)heatBuffer.heatCapacity, 0D, 15D);
		for (TileSaltFissionRedstonePort redstonePort : redstonePorts) {
			if (redstonePort.comparatorSignal != comparatorSignal) {
				redstonePort.comparatorSignal = comparatorSignal;
				WORLD.updateComparatorOutputLevel(redstonePort.getPos(), null);
			}
		}
	}
	
	protected void setVesselStats() {
		for (TileSaltFissionModerator moderator : moderators) {
			moderator.isInValidPosition = false;
			moderator.isInModerationLine = false;
		}
		
		if (vessels.size() < 1) {
			efficiency = 0;
			heatMult = 0;
			heating = 0;
		} else {
			double newEfficiency = 0;
			double newHeatMult = 0;
			double newHeating = 0;
			for (TileSaltFissionVessel vessel : vessels) {
				vessel.calculateEfficiency();
				newEfficiency += vessel.getEfficiency();
				newHeatMult += vessel.getHeatMultiplier();
				newHeating += vessel.getProcessHeat();
			}
			efficiency = newEfficiency/vessels.size();
			heatMult = newHeatMult/vessels.size();
			heating = newHeating;
			
			for (TileSaltFissionModerator moderator : moderators) {
				if (moderator.contributeExtraHeat()) heating += newHeating/vessels.size();
			}
		}
	}
	
	private static final ArrayList<String> STAGE_0_COOLANTS = Lists.newArrayList("nak", "redstone_nak", "quartz_nak", "glowstone_nak", "lapis_nak", "ender_nak", "cryotheum_nak", "emerald_nak", "magnesium_nak");
	private static final ArrayList<String> STAGE_1_COOLANTS = Lists.newArrayList("gold_nak", "diamond_nak", "liquidhelium_nak", "copper_nak", "tin_nak");
	private static final ArrayList<String> STAGE_2_COOLANTS = Lists.newArrayList("iron_nak");
	
	protected void doHeaterPlacementChecks() {
		for (TileSaltFissionHeater heater : heaters) heater.checked = false;
		
		for (short i = 0; i <= 2; i++) for (TileSaltFissionHeater heater : heaters) {
			heaterCheckCount = i;
			if (!heater.checked) heater.checkIsInValidPosition(heaterCheckCount);
		}
	}
	
	protected void setCooling() {
		double newCooling = 0;
		for (TileSaltFissionHeater heater : heaters) if (heater.isInValidPosition) newCooling += heater.getProcessCooling();
		cooling = newCooling;
	}
	
	public double getHeatChange(boolean checkIsReactorOn) {
		return isReactorOn || !checkIsReactorOn ? heating - cooling : -cooling;
	}
	
	protected void setCoolingRate() {
		if (vessels.size() <= 0 || cooling <= 0) coolingRate = 0;
		else {
			coolingRate = getHeatChange(false) >= 0 ? efficiency : efficiency*heating/cooling;
			double heaterRate = !isReactorOn && coolingRate*heatBuffer.heatStored < cooling ? coolingRate*heatBuffer.heatStored/cooling : coolingRate;
			for (TileSaltFissionHeater heater : heaters) heater.reactorCoolingRate = heaterRate;
		}
	}
	
	protected void doMeltdown() {
		Iterator<TileSaltFissionVessel> vesselIterator = vessels.iterator();
		while (vesselIterator.hasNext()) {
			TileSaltFissionVessel vessel = vesselIterator.next();
			vesselIterator.remove();
			vessel.doMeltdown();
		}
		
		Iterator<TileSaltFissionController> controllerIterator = controllers.iterator();
		while (controllerIterator.hasNext()) {
			TileSaltFissionController controller = controllerIterator.next();
			controllerIterator.remove();
			controller.doMeltdown();
		}
		
		IBlockState corium = RegistryHelper.getBlock(Global.MOD_ID + ":fluid_corium").getDefaultState();
		for (BlockPos blockPos : BlockPos.getAllInBoxMutable(getMinimumCoord(), getMaximumCoord())) {
			if (rand.nextDouble() < 0.18D) {
				if (WORLD.getTileEntity(blockPos) != null) WORLD.removeTileEntity(blockPos);
				WORLD.setBlockState(blockPos, corium);
			}
		}
		
		checkIfMachineIsWhole();
	}
	
	private void incrementUpdateCount() {
		updateCount++; updateCount %= updateTime();
		distributeCount++; distributeCount %= distributeTime();
	}
	
	private int updateTime() {
		return NCConfig.machine_update_rate / 2;
	}
	
	private boolean shouldUpdate() {
		return updateCount == 0;
	}
	
	private int distributeTime() {
		return 20;
	}
	
	private boolean shouldDistribute() {
		return distributeCount == 0;
	}
	
	// Client
	
	@Override
	protected void updateClient() {
		if (isReactorOn) {
			for (TileSaltFissionVessel vessel : vessels) playFissionSound(vessel.getPos());
		}
	}
	
	protected void playFissionSound(BlockPos pos) {
		if (vessels.size() <= 0) return;
		double soundRate = Math.min(efficiency/(14D*NCConfig.salt_fission_max_size*Math.sqrt(vessels.size())), 1D/vessels.size());
		if (rand.nextDouble() < soundRate) {
			WORLD.playSound(pos.getX(), pos.getY(), pos.getZ(), SoundHandler.geiger_tick, SoundCategory.BLOCKS, 1.6F, 1.0F + 0.12F*(rand.nextFloat() - 0.5F), false);
		}
	}
	
	// NBT
	
	@Override
	protected void syncDataTo(NBTTagCompound data, SyncReason syncReason) {
		heatBuffer.writeToNBT(data);
		data.setInteger("comparatorSignal", comparatorSignal);
		data.setBoolean("isReactorOn", isReactorOn);
		data.setDouble("cooling", cooling);
		data.setDouble("heating", heating);
		data.setDouble("efficiency", efficiency);
		data.setDouble("heatMult", heatMult);
		data.setDouble("coolingRate", coolingRate);
	}
	
	@Override
	protected void syncDataFrom(NBTTagCompound data, SyncReason syncReason) {
		heatBuffer.readFromNBT(data);
		comparatorSignal = data.getInteger("comparatorSignal");
		isReactorOn = data.getBoolean("isReactorOn");
		cooling = data.getDouble("cooling");
		heating = data.getDouble("heating");
		efficiency = data.getDouble("efficiency");
		heatMult = data.getDouble("heatMult");
		coolingRate = data.getDouble("coolingRate");
	}
	
	// Packets
	
	@Override
	protected SaltFissionUpdatePacket getUpdatePacket() {
		return new SaltFissionUpdatePacket(controller.getPos(), isReactorOn, cooling, heating, efficiency, heatMult, coolingRate, heatBuffer.getHeatCapacity(), heatBuffer.getHeatStored());
	}
	
	@Override
	public void onPacket(SaltFissionUpdatePacket message) {
		isReactorOn = message.isReactorOn;
		cooling = message.cooling;
		heating = message.heating;
		efficiency = message.efficiency;
		heatMult = message.heatMult;
		coolingRate = message.coolingRate;
		heatBuffer.setHeatCapacity(message.capacity);
		heatBuffer.setHeatStored(message.heat);
	}
	
	public Container getContainer(EntityPlayer player) {
		return new ContainerSaltFissionController(player, controller);
	}
	
	// Multiblock Validators
	
	@Override
	protected boolean isBlockGoodForInterior(World world, int x, int y, int z, IMultiblockValidator validatorCallback) {
		return true;
	}
}
