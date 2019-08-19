package nc.multiblock.heatExchanger;

import java.util.Set;

import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import nc.Global;
import nc.config.NCConfig;
import nc.multiblock.IMultiblockFluid;
import nc.multiblock.IMultiblockPart;
import nc.multiblock.MultiblockBase;
import nc.multiblock.TileBeefBase.SyncReason;
import nc.multiblock.container.ContainerHeatExchangerController;
import nc.multiblock.cuboidal.CuboidalMultiblockBase;
import nc.multiblock.heatExchanger.tile.TileHeatExchangerCondenserTube;
import nc.multiblock.heatExchanger.tile.TileHeatExchangerController;
import nc.multiblock.heatExchanger.tile.TileHeatExchangerTube;
import nc.multiblock.heatExchanger.tile.TileHeatExchangerVent;
import nc.multiblock.network.HeatExchangerUpdatePacket;
import nc.multiblock.validation.IMultiblockValidator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class HeatExchanger extends CuboidalMultiblockBase<HeatExchangerUpdatePacket> implements IMultiblockFluid {
	
	protected Set<TileHeatExchangerController> controllers;
	protected Set<TileHeatExchangerVent> vents;
	protected Set<TileHeatExchangerTube> tubes;
	protected Set<TileHeatExchangerCondenserTube> condenserTubes;
	
	protected TileHeatExchangerController controller;
	
	protected int updateCount = 0;
	
	public boolean isHeatExchangerOn, computerActivated;
	public double fractionOfTubesActive, efficiency, maxEfficiency;
	
	public HeatExchanger(World world) {
		super(world);
		
		controllers = new ObjectOpenHashSet<TileHeatExchangerController>();
		vents = new ObjectOpenHashSet<TileHeatExchangerVent>();
		tubes = new ObjectOpenHashSet<TileHeatExchangerTube>();
		condenserTubes = new ObjectOpenHashSet<TileHeatExchangerCondenserTube>();
	}
	
	// Multiblock Part Getters
	
	public Set<TileHeatExchangerController> getControllers() {
		return controllers;
	}
	
	public Set<TileHeatExchangerVent> getVents() {
		return vents;
	}
	
	public Set<TileHeatExchangerTube> getTubes() {
		return tubes;
	}
	
	public Set<TileHeatExchangerCondenserTube> getCondenserTubes() {
		return condenserTubes;
	}
	
	// Multiblock Size Limits
	
	@Override
	protected int getMinimumInteriorLength() {
		return NCConfig.heat_exchanger_min_size;
	}
	
	@Override
	protected int getMaximumInteriorLength() {
		return NCConfig.heat_exchanger_max_size;
	}
	
	// Multiblock Methods
	
	@Override
	public void onAttachedPartWithMultiblockData(IMultiblockPart part, NBTTagCompound data) {
		syncDataFrom(data, SyncReason.FullSync);
	}
	
	@Override
	protected void onBlockAdded(IMultiblockPart newPart) {
		if (newPart instanceof TileHeatExchangerController) controllers.add((TileHeatExchangerController) newPart);
		if (newPart instanceof TileHeatExchangerVent) vents.add((TileHeatExchangerVent) newPart);
		if (newPart instanceof TileHeatExchangerTube) tubes.add((TileHeatExchangerTube) newPart);
		if (newPart instanceof TileHeatExchangerCondenserTube) condenserTubes.add((TileHeatExchangerCondenserTube) newPart);
	}
	
	@Override
	protected void onBlockRemoved(IMultiblockPart oldPart) {
		if (oldPart instanceof TileHeatExchangerController) controllers.remove(oldPart);
		if (oldPart instanceof TileHeatExchangerVent) vents.remove(oldPart);
		if (oldPart instanceof TileHeatExchangerTube) tubes.remove(oldPart);
		if (oldPart instanceof TileHeatExchangerCondenserTube) condenserTubes.remove(oldPart);
	}
	
	@Override
	protected void onMachineAssembled() {
		for (TileHeatExchangerController contr : controllers) controller = contr;
		onHeatExchangerFormed();
	}
	
	@Override
	protected void onMachineRestored() {
		onHeatExchangerFormed();
	}
	
	protected void onHeatExchangerFormed() {
		setIsHeatExchangerOn();
		for (TileHeatExchangerTube tube : tubes) tube.updateFlowDir();
		for (TileHeatExchangerCondenserTube condenserTube : condenserTubes) condenserTube.updateAdjacentTemperatures();
		updateHeatExchangerStats();
	}
	
	@Override
	protected void onMachinePaused() {
		
	}
	
	@Override
	protected void onMachineDisassembled() {
		isHeatExchangerOn = false;
		if (controller != null) controller.updateBlockState(false);
		fractionOfTubesActive = efficiency = maxEfficiency = 0D;
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
		
	}
	
	@Override
	protected void onAssimilated(MultiblockBase assimilator) {
		
	}
	
	// Server
	
	@Override
	protected boolean updateServer() {
		//setIsHeatExchangerOn();
		if (shouldUpdate()) updateHeatExchangerStats();
		if (shouldUpdate()) sendUpdateToListeningPlayers();
		incrementUpdateCount();
		return true;
	}
	
	public void setIsHeatExchangerOn() {
		boolean oldIsHeatExchangerOn = isHeatExchangerOn;
		isHeatExchangerOn = (isRedstonePowered() || computerActivated) && isAssembled();
		if (isHeatExchangerOn != oldIsHeatExchangerOn) {
			if (controller != null) controller.updateBlockState(isHeatExchangerOn);
			sendUpdateToAllPlayers();
		}
	}
	
	protected boolean isRedstonePowered() {
		if (controller != null && controller.checkIsRedstonePowered(WORLD, controller.getPos())) return true;
		return false;
	}
	
	protected void updateHeatExchangerStats() {
		int numberOfTubes = tubes.size() + condenserTubes.size();
		if (numberOfTubes < 1) {
			fractionOfTubesActive = efficiency = maxEfficiency = 0D;
			return;
		}
		int activeCount = 0, efficiencyCount = 0, maxEfficiencyCount = 0;
		
		for (TileHeatExchangerTube tube : tubes) {
			int[] eff = tube.checkPosition();
			if (eff[0] > 0) activeCount++;
			efficiencyCount += eff[0];
			maxEfficiencyCount += eff[1];
		}
		
		for (TileHeatExchangerCondenserTube condenserTube : condenserTubes) {
			int eff = condenserTube.checkPosition();
			if (eff > 0) activeCount++;
			efficiencyCount += eff;
			maxEfficiencyCount += eff;
		}
		
		fractionOfTubesActive = (double)activeCount/numberOfTubes;
		efficiency = activeCount == 0 ? 0D : (double)efficiencyCount/activeCount;
		maxEfficiency = (double)maxEfficiencyCount/numberOfTubes;
	}
	
	protected void incrementUpdateCount() {
		updateCount++; updateCount %= updateTime();
	}
	
	protected static int updateTime() {
		return NCConfig.machine_update_rate;
	}
	
	protected boolean shouldUpdate() {
		return updateCount == 0;
	}
	
	// Client
	
	@Override
	protected void updateClient() {
		
	}
	
	// NBT
	
	@Override
	protected void syncDataTo(NBTTagCompound data, SyncReason syncReason) {
		data.setBoolean("isHeatExchangerOn", isHeatExchangerOn);
		data.setBoolean("computerActivated", computerActivated);
		data.setDouble("fractionOfTubesActive", fractionOfTubesActive);
		data.setDouble("efficiency", efficiency);
		data.setDouble("maxEfficiency", maxEfficiency);
	}
	
	@Override
	protected void syncDataFrom(NBTTagCompound data, SyncReason syncReason) {
		isHeatExchangerOn = data.getBoolean("isHeatExchangerOn");
		computerActivated = data.getBoolean("computerActivated");
		fractionOfTubesActive = data.getDouble("fractionOfTubesActive");
		efficiency = data.getDouble("efficiency");
		maxEfficiency = data.getDouble("maxEfficiency");
	}
	
	// Packets
	
	@Override
	protected HeatExchangerUpdatePacket getUpdatePacket() {
		return new HeatExchangerUpdatePacket(controller.getPos(), isHeatExchangerOn, fractionOfTubesActive, efficiency, maxEfficiency);
	}
	
	@Override
	public void onPacket(HeatExchangerUpdatePacket message) {
		isHeatExchangerOn = message.isHeatExchangerOn;
		fractionOfTubesActive = message.fractionOfTubesActive;
		efficiency = message.efficiency;
		maxEfficiency = message.maxEfficiency;
	}
	
	public Container getContainer(EntityPlayer player) {
		return new ContainerHeatExchangerController(player, controller);
	}
	
	@Override
	public void clearAllFluids() {
		for (TileHeatExchangerVent vent : vents) vent.clearAllTanks();
		for (TileHeatExchangerTube tube : tubes) tube.clearAllTanks();
		for (TileHeatExchangerCondenserTube condenserTube : condenserTubes) condenserTube.clearAllTanks();
	}
	
	// Multiblock Validators
	
	@Override
	protected boolean isBlockGoodForInterior(World world, int x, int y, int z, IMultiblockValidator validatorCallback) {
		return true;
	}
}
