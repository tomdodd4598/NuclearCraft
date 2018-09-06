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
import nc.multiblock.saltFission.tile.TileSaltFissionHeater;
import nc.multiblock.saltFission.tile.TileSaltFissionModerator;
import nc.multiblock.saltFission.tile.TileSaltFissionVent;
import nc.multiblock.saltFission.tile.TileSaltFissionVessel;
import nc.multiblock.validation.IMultiblockValidator;
import nc.tile.internal.HeatBuffer;
import nc.util.RegistryHelper;
import net.minecraft.block.Block;
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
	
	private TileSaltFissionController controller;
	
	private Random rand = new Random();
	
	public int redstoneSignal = 0;
	private int updateCount = 0;
	
	public boolean isReactorOn;
	public double cooling, heating, efficiency, heatMult, coolingRate;
	public final HeatBuffer heatBuffer;
	private static final int BASE_MAX_HEAT = 25000;

	public SaltFissionReactor(World world) {
		super(world);
		
		controllers = new HashSet<TileSaltFissionController>();
		vents = new HashSet<TileSaltFissionVent>();
		vessels = new HashSet<TileSaltFissionVessel>();
		heaters = new HashSet<TileSaltFissionHeater>();
		moderators = new HashSet<TileSaltFissionModerator>();
		
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
	
	// Multiblock Size Limits
	
	@Override
	protected int getMinimumNumberOfBlocksForAssembledMachine() {
		return getHollowCubeNumberOfBlocks(NCConfig.salt_fission_min_size + 2);
	}
	
	@Override
	protected int getMinimumXSize() {
		return NCConfig.salt_fission_min_size + 2;
	}
	
	@Override
	protected int getMinimumYSize() {
		return NCConfig.salt_fission_min_size + 2;
	}
	
	@Override
	protected int getMinimumZSize() {
		return NCConfig.salt_fission_min_size + 2;
	}
	
	@Override
	protected int getMaximumXSize() {
		return NCConfig.salt_fission_max_size + 2;
	}
	
	@Override
	protected int getMaximumYSize() {
		return NCConfig.salt_fission_max_size + 2;
	}
	
	@Override
	protected int getMaximumZSize() {
		return NCConfig.salt_fission_max_size + 2;
	}
	
	// Multiblock Methods
	
	@Override
	public void onAttachedPartWithMultiblockData(IMultiblockPart part, NBTTagCompound data) {
		syncDataFrom(data, SyncReason.FullSync);
	}
	
	@Override
	protected void onBlockAdded(IMultiblockPart newPart) {
		if (newPart instanceof TileSaltFissionController) controllers.add((TileSaltFissionController) newPart);
		if (newPart instanceof TileSaltFissionVent) vents.add((TileSaltFissionVent) newPart);
		if (newPart instanceof TileSaltFissionVessel) vessels.add((TileSaltFissionVessel) newPart);
		if (newPart instanceof TileSaltFissionHeater) heaters.add((TileSaltFissionHeater) newPart);
		if (newPart instanceof TileSaltFissionModerator) moderators.add((TileSaltFissionModerator) newPart);
	}
	
	@Override
	protected void onBlockRemoved(IMultiblockPart oldPart) {
		if (oldPart instanceof TileSaltFissionController) controllers.remove(oldPart);
		if (oldPart instanceof TileSaltFissionVent) vents.remove(oldPart);
		if (oldPart instanceof TileSaltFissionVessel) vessels.remove(oldPart);
		if (oldPart instanceof TileSaltFissionHeater) heaters.remove(oldPart);
		if (oldPart instanceof TileSaltFissionModerator) moderators.remove(oldPart);
	}
	
	@Override
	protected void onMachineAssembled() {
		for (TileSaltFissionController contr : controllers) controller = contr;
		calculateReactorStats();
	}
	
	@Override
	protected void onMachineRestored() {
		calculateReactorStats();
	}
	
	@Override
	protected void onMachinePaused() {
		
	}
	
	@Override
	protected void onMachineDisassembled() {
		isReactorOn = false;
	}
	
	@Override
	protected boolean isMachineWhole(IMultiblockValidator validatorCallback) {
		if (controllers.size() == 0) {
			validatorCallback.setLastError(Global.MOD_ID + ".multiblock_validation.no_controller");
			return false;
		}
		if (controllers.size() > 1) {
			validatorCallback.setLastError(Global.MOD_ID + ".multiblock_validation.too_many_controllers");
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
		setIsReactorOn();
		if (shouldUpdate()) calculateReactorStats();
		if (heatBuffer.isFull() && NCConfig.salt_fission_overheat) {
			doMeltdown();
			return true;
		}
		if (shouldUpdate()) sendUpdateToListeningPlayers();
		incrementUpdateCount();
		return true;
	}
	
	protected void setIsReactorOn() {
		boolean oldIsReactorOn = isReactorOn;
		isReactorOn = controller.isPowered() && isAssembled();
		if (isReactorOn != oldIsReactorOn) sendUpdateToAllPlayers();
	}
	
	protected void calculateReactorStats() {
		heatBuffer.setHeatCapacity(BASE_MAX_HEAT*getNumConnectedBlocks());
		setVesselStats();
		doHeaterPlacementChecks();
		setCooling();
		heatBuffer.changeHeatStored((long) (updateTime()*getHeatChange(true)));
		setCoolingRate();
		redstoneSignal = (int) (15D*MathHelper.clamp(2*(double)heatBuffer.heatStored / (double)heatBuffer.heatCapacity, 0D, 1D));
	}
	
	protected void setVesselStats() {
		for (TileSaltFissionModerator moderator : moderators) moderator.isInValidPosition = false;
		
		if (vessels.size() <= 0) {
			efficiency = 0;
			heatMult = 0;
			heating = 0;
		} else {
			double newEfficiency = 0;
			double newHeatMult = 0;
			double newHeating = 0;
			for (TileSaltFissionVessel vessel : vessels) {
				vessel.calculateEfficiency();
				newEfficiency += vessel.vesselEfficiency;
				newHeatMult += vessel.vesselEfficiency*(vessel.vesselEfficiency + 1D)*0.5D;
				newHeating += vessel.getProcessHeat();
			}
			efficiency = newEfficiency/vessels.size();
			heatMult = newHeatMult/vessels.size();
			heating = newHeating;
		}
	}
	
	private static final ArrayList<String> STAGE_0_COOLANTS = Lists.newArrayList("nak", "redstone_nak", "quartz_nak", "glowstone_nak", "lapis_nak", "ender_nak", "cryotheum_nak", "emerald_nak", "magnesium_nak");
	private static final ArrayList<String> STAGE_1_COOLANTS = Lists.newArrayList("gold_nak", "diamond_nak", "liquidhelium_nak", "copper_nak", "tin_nak");
	private static final ArrayList<String> STAGE_2_COOLANTS = Lists.newArrayList("iron_nak");
	
	protected void doHeaterPlacementChecks() {
		for (TileSaltFissionHeater heater : heaters) {
			heater.checked = false;
		}
		for (int i = 0; i <= 2; i++) for (TileSaltFissionHeater heater : heaters) {
			if (!heater.checked) heater.checkIsInValidPosition();
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
			for (TileSaltFissionHeater heater : heaters) heater.reactorCoolingRate = !isReactorOn && coolingRate*heatBuffer.heatStored < cooling ? coolingRate*heatBuffer.heatStored/cooling : coolingRate;
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
		for (BlockPos blockPos : BlockPos.getAllInBoxMutable(getMinimumCoord(), getMaximumCoord())) {
			if (rand.nextDouble() < 0.18D) {
				if (WORLD.getTileEntity(blockPos) != null) WORLD.removeTileEntity(blockPos);
				Block corium = RegistryHelper.getBlock(Global.MOD_ID + ":fluid_corium");
				if (corium != null) WORLD.setBlockState(blockPos, corium.getDefaultState());
			}
		}
		checkIfMachineIsWhole();
	}
	
	private void incrementUpdateCount() {
		updateCount++; updateCount %= updateTime();
	}
	
	private int updateTime() {
		return NCConfig.machine_update_rate / 4;
	}
	
	private boolean shouldUpdate() {
		return updateCount == 0;
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
		data.setInteger("redstoneSignal", redstoneSignal);
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
		redstoneSignal = data.getInteger("redstoneSignal");
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
	
	public void onPacket(boolean isReactorOn, double cooling, double heating, double efficiency, double heatMult, double coolingRate, long capacity, long heat) {
		this.isReactorOn = isReactorOn;
		this.cooling = cooling;
		this.heating = heating;
		this.efficiency = efficiency;
		this.heatMult = heatMult;
		this.coolingRate = coolingRate;
		heatBuffer.setHeatCapacity(capacity);
		heatBuffer.setHeatStored(heat);
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
