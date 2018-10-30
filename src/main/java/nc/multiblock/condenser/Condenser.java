package nc.multiblock.condenser;

import java.util.HashSet;
import java.util.Set;

import nc.Global;
import nc.config.NCConfig;
import nc.multiblock.IMultiblockPart;
import nc.multiblock.MultiblockBase;
import nc.multiblock.TileBeefBase.SyncReason;
import nc.multiblock.condenser.tile.TileCondenserController;
import nc.multiblock.cuboidal.CuboidalMultiblockBase;
import nc.multiblock.network.CondenserUpdatePacket;
import nc.multiblock.validation.IMultiblockValidator;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class Condenser extends CuboidalMultiblockBase<CondenserUpdatePacket> {
	
	private Set<TileCondenserController> controllers;
	
	private TileCondenserController controller;
	
	public int redstoneSignal = 0;
	private int updateCount = 0;
	
	public boolean isCondenserOn;
	
	public Condenser(World world) {
		super(world);
		
		controllers = new HashSet<TileCondenserController>();
	}
	
	// Multiblock Part Getters
	
	public Set<TileCondenserController> getControllers() {
		return controllers;
	}
	
	// Multiblock Size Limits
	
	@Override
	protected int getMinimumInteriorLength() {
		return NCConfig.condenser_min_size;
	}
	
	@Override
	protected int getMaximumInteriorLength() {
		return NCConfig.condenser_max_size;
	}
	
	// Multiblock Methods
	
	@Override
	public void onAttachedPartWithMultiblockData(IMultiblockPart part, NBTTagCompound data) {
		syncDataFrom(data, SyncReason.FullSync);
	}
	
	@Override
	protected void onBlockAdded(IMultiblockPart newPart) {
		if (newPart instanceof TileCondenserController) controllers.add((TileCondenserController) newPart);
		// TODO
	}
	
	@Override
	protected void onBlockRemoved(IMultiblockPart oldPart) {
		if (oldPart instanceof TileCondenserController) controllers.remove(oldPart);
		// TODO
	}
	
	@Override
	protected void onMachineAssembled() {
		for (TileCondenserController contr : controllers) controller = contr;
		calculateCondenserStats();
	}
	
	@Override
	protected void onMachineRestored() {
		calculateCondenserStats();
	}
	
	@Override
	protected void onMachinePaused() {
		
	}
	
	@Override
	protected void onMachineDisassembled() {
		isCondenserOn = false;
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
		setIsCondenserOn();
		if (shouldUpdate()) calculateCondenserStats();
		// TODO
		if (shouldUpdate()) sendUpdateToListeningPlayers();
		incrementUpdateCount();
		return true;
	}
	
	protected void setIsCondenserOn() {
		boolean oldIsCondenserOn = isCondenserOn;
		isCondenserOn = controller.isRedstonePowered() && isAssembled();
		if (isCondenserOn != oldIsCondenserOn) sendUpdateToAllPlayers();
	}
	
	protected void calculateCondenserStats() {
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
	
	public void onPacket(boolean isCondenserOn) {
		this.isCondenserOn = isCondenserOn;
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
	protected CondenserUpdatePacket getUpdatePacket() {
		// TODO Auto-generated method stub
		return null;
	}
}
