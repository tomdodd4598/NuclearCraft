package nc.multiblock.highTurbine;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import nc.Global;
import nc.config.NCConfig;
import nc.multiblock.IMultiblockPart;
import nc.multiblock.MultiblockBase;
import nc.multiblock.TileBeefBase.SyncReason;
import nc.multiblock.cuboidal.CuboidalMultiblockBase;
import nc.multiblock.highTurbine.tile.TileHighTurbineController;
import nc.multiblock.network.HighTurbineUpdatePacket;
import nc.multiblock.validation.IMultiblockValidator;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class HighTurbine extends CuboidalMultiblockBase<HighTurbineUpdatePacket> {
	
	private Set<TileHighTurbineController> controllers;
	
	private TileHighTurbineController controller;
	
	private Random rand = new Random();
	
	public int redstoneSignal = 0;
	private int updateCount = 0;
	
	public boolean isHighTurbineOn;
	
	public HighTurbine(World world) {
		super(world);
		
		controllers = new HashSet<TileHighTurbineController>();
	}
	
	// Multiblock Part Getters
	
	public Set<TileHighTurbineController> getControllers() {
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
		if (newPart instanceof TileHighTurbineController) controllers.add((TileHighTurbineController) newPart);
		// TODO
	}
	
	@Override
	protected void onBlockRemoved(IMultiblockPart oldPart) {
		if (oldPart instanceof TileHighTurbineController) controllers.remove(oldPart);
		// TODO
	}
	
	@Override
	protected void onMachineAssembled() {
		for (TileHighTurbineController contr : controllers) controller = contr;
		calculateHighTurbineStats();
	}
	
	@Override
	protected void onMachineRestored() {
		calculateHighTurbineStats();
	}
	
	@Override
	protected void onMachinePaused() {
		
	}
	
	@Override
	protected void onMachineDisassembled() {
		isHighTurbineOn = false;
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
		setIsHighTurbineOn();
		if (shouldUpdate()) calculateHighTurbineStats();
		// TODO
		if (shouldUpdate()) sendUpdateToListeningPlayers();
		incrementUpdateCount();
		return true;
	}
	
	protected void setIsHighTurbineOn() {
		boolean oldIsHighTurbineOn = isHighTurbineOn;
		isHighTurbineOn = controller.isPowered() && isAssembled();
		if (isHighTurbineOn != oldIsHighTurbineOn) sendUpdateToAllPlayers();
	}
	
	protected void calculateHighTurbineStats() {
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
	
	public void onPacket(boolean isHighTurbineOn) {
		this.isHighTurbineOn = isHighTurbineOn;
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
	protected HighTurbineUpdatePacket getUpdatePacket() {
		// TODO Auto-generated method stub
		return null;
	}
}
