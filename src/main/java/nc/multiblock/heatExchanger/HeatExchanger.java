package nc.multiblock.heatExchanger;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import nc.Global;
import nc.config.NCConfig;
import nc.multiblock.IMultiblockPart;
import nc.multiblock.MultiblockBase;
import nc.multiblock.TileBeefBase.SyncReason;
import nc.multiblock.cuboidal.CuboidalMultiblockBase;
import nc.multiblock.heatExchanger.tile.TileHeatExchangerController;
import nc.multiblock.network.HeatExchangerUpdatePacket;
import nc.multiblock.validation.IMultiblockValidator;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class HeatExchanger extends CuboidalMultiblockBase<HeatExchangerUpdatePacket> {
	
	private Set<TileHeatExchangerController> controllers;
	
	private TileHeatExchangerController controller;
	
	private Random rand = new Random();
	
	public int redstoneSignal = 0;
	private int updateCount = 0;
	
	public boolean isHeatExchangerOn;
	
	public HeatExchanger(World world) {
		super(world);
		
		controllers = new HashSet<TileHeatExchangerController>();
	}
	
	// Multiblock Part Getters
	
	public Set<TileHeatExchangerController> getControllers() {
		return controllers;
	}
	
	// Multiblock Size Limits
	
	
	
	// Multiblock Methods
	
	@Override
	public void onAttachedPartWithMultiblockData(IMultiblockPart part, NBTTagCompound data) {
		syncDataFrom(data, SyncReason.FullSync);
	}
	
	@Override
	protected void onBlockAdded(IMultiblockPart newPart) {
		if (newPart instanceof TileHeatExchangerController) controllers.add((TileHeatExchangerController) newPart);
		// TODO
	}
	
	@Override
	protected void onBlockRemoved(IMultiblockPart oldPart) {
		if (oldPart instanceof TileHeatExchangerController) controllers.remove(oldPart);
		// TODO
	}
	
	@Override
	protected void onMachineAssembled() {
		for (TileHeatExchangerController contr : controllers) controller = contr;
		calculateHeatExchangerStats();
	}
	
	@Override
	protected void onMachineRestored() {
		calculateHeatExchangerStats();
	}
	
	@Override
	protected void onMachinePaused() {
		
	}
	
	@Override
	protected void onMachineDisassembled() {
		isHeatExchangerOn = false;
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
	protected void onAssimilated(MultiblockBase assimilator) {
		
	}
	
	// Server
	
	@Override
	protected boolean updateServer() {
		setIsHeatExchangerOn();
		if (shouldUpdate()) calculateHeatExchangerStats();
		// TODO
		if (shouldUpdate()) sendUpdateToListeningPlayers();
		incrementUpdateCount();
		return true;
	}
	
	protected void setIsHeatExchangerOn() {
		boolean oldIsHeatExchangerOn = isHeatExchangerOn;
		isHeatExchangerOn = controller.isPowered() && isAssembled();
		if (isHeatExchangerOn != oldIsHeatExchangerOn) sendUpdateToAllPlayers();
	}
	
	protected void calculateHeatExchangerStats() {
		// TODO
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
		// TODO
	}
	
	// NBT
	
	@Override
	protected void syncDataTo(NBTTagCompound data, SyncReason syncReason) {
		// TODO
	}
	
	@Override
	protected void syncDataFrom(NBTTagCompound data, SyncReason syncReason) {
		// TODO
	}
	
	// Packets
	
	public void onPacket(boolean isHeatExchangerOn) {
		this.isHeatExchangerOn = isHeatExchangerOn;
		// TODO
	}
	
	// Multiblock Validators
	
	@Override
	protected boolean isBlockGoodForInterior(World world, int x, int y, int z, IMultiblockValidator validatorCallback) {
		return true;
	}
	
	// TODO
	// TODO
	// TODO
	// TODO
	// TODO

	@Override
	protected int getMinimumNumberOfBlocksForAssembledMachine() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected int getMaximumXSize() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected int getMaximumZSize() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected int getMaximumYSize() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected void onAssimilate(MultiblockBase assimilated) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected HeatExchangerUpdatePacket getUpdatePacket() {
		// TODO Auto-generated method stub
		return null;
	}
}
