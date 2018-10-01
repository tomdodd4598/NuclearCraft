package nc.multiblock.turbine;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import nc.Global;
import nc.config.NCConfig;
import nc.multiblock.IMultiblockPart;
import nc.multiblock.MultiblockBase;
import nc.multiblock.TileBeefBase.SyncReason;
import nc.multiblock.cuboidal.CuboidalMultiblockBase;
import nc.multiblock.network.TurbineUpdatePacket;
import nc.multiblock.turbine.tile.TileTurbineController;
import nc.multiblock.validation.IMultiblockValidator;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public abstract class Turbine<CONTROLLER extends TileTurbineController> extends CuboidalMultiblockBase<TurbineUpdatePacket> {
	
	protected final Class<CONTROLLER> tileControllerClass;
	
	protected Set<CONTROLLER> controllers;
	
	protected CONTROLLER controller;
	
	protected Random rand = new Random();
	
	public int redstoneSignal = 0;
	protected int updateCount = 0;
	
	public boolean isTurbineOn;
	
	public Turbine(World world, Class<CONTROLLER> tileControllerClass) {
		super(world);
		this.tileControllerClass = tileControllerClass;
		
		controllers = new HashSet<CONTROLLER>();
	}
	
	// Multiblock Part Getters
	
	public Set<CONTROLLER> getControllers() {
		return controllers;
	}
	
	// Multiblock Size Limits
	
	@Override
	protected int getMinimumInteriorLength() {
		return NCConfig.turbine_min_size;
	}
	
	@Override
	protected int getMaximumInteriorLength() {
		return NCConfig.turbine_max_size;
	}
	
	// Multiblock Methods
	
	@Override
	public void onAttachedPartWithMultiblockData(IMultiblockPart part, NBTTagCompound data) {
		syncDataFrom(data, SyncReason.FullSync);
	}
	
	@Override
	protected void onBlockAdded(IMultiblockPart newPart) {
		if (tileControllerClass.isInstance(newPart)) controllers.add((CONTROLLER) newPart);
		// TODO
	}
	
	@Override
	protected void onBlockRemoved(IMultiblockPart oldPart) {
		if (tileControllerClass.isInstance(oldPart)) controllers.remove(oldPart);
		// TODO
	}
	
	@Override
	protected void onMachineAssembled() {
		for (CONTROLLER contr : controllers) controller = contr;
		calculateTurbineStats();
	}
	
	@Override
	protected void onMachineRestored() {
		calculateTurbineStats();
	}
	
	@Override
	protected void onMachinePaused() {
		
	}
	
	@Override
	protected void onMachineDisassembled() {
		isTurbineOn = false;
	}
	
	@Override
	protected boolean isMachineWhole(IMultiblockValidator validatorCallback) {
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
	protected void onAssimilated(MultiblockBase assimilator) {
		
	}
	
	// Server
	
	@Override
	protected boolean updateServer() {
		setIsTurbineOn();
		if (shouldUpdate()) calculateTurbineStats();
		// TODO
		if (shouldUpdate()) sendUpdateToListeningPlayers();
		incrementUpdateCount();
		return true;
	}
	
	protected void setIsTurbineOn() {
		boolean oldIsTurbineOn = isTurbineOn;
		isTurbineOn = controller.isRedstonePowered() && isAssembled();
		if (isTurbineOn != oldIsTurbineOn) sendUpdateToAllPlayers();
	}
	
	protected void calculateTurbineStats() {
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
	
	public void onPacket(boolean isTurbineOn) {
		this.isTurbineOn = isTurbineOn;
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
	protected TurbineUpdatePacket getUpdatePacket() {
		// TODO Auto-generated method stub
		return null;
	}
}
