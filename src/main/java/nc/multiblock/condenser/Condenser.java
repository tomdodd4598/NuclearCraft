package nc.multiblock.condenser;

import java.util.HashSet;
import java.util.Set;

import nc.Global;
import nc.config.NCConfig;
import nc.multiblock.IMultiblockFluid;
import nc.multiblock.IMultiblockPart;
import nc.multiblock.MultiblockBase;
import nc.multiblock.TileBeefBase.SyncReason;
import nc.multiblock.condenser.tile.TileCondenserController;
import nc.multiblock.container.ContainerCondenserController;
import nc.multiblock.cuboidal.CuboidalMultiblockBase;
import nc.multiblock.network.CondenserUpdatePacket;
import nc.multiblock.validation.IMultiblockValidator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class Condenser extends CuboidalMultiblockBase<CondenserUpdatePacket> implements IMultiblockFluid {
	
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
		onCondenserFormed();
	}
	
	@Override
	protected void onMachineRestored() {
		onCondenserFormed();
	}
	
	protected void onCondenserFormed() {
		setIsCondenserOn();
		calculateCondenserStats();
	}
	
	@Override
	protected void onMachinePaused() {
		
	}
	
	@Override
	protected void onMachineDisassembled() {
		isCondenserOn = false;
		if (controller != null) controller.updateBlock(false);
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
	protected void onAssimilate(MultiblockBase assimilated) {
		
	}
	
	@Override
	protected void onAssimilated(MultiblockBase assimilator) {
		
	}
	
	// Server
	
	@Override
	protected boolean updateServer() {
		//setIsCondenserOn();
		if (shouldUpdate()) calculateCondenserStats();
		// TODO
		if (shouldUpdate()) sendUpdateToListeningPlayers();
		incrementUpdateCount();
		return true;
	}
	
	public void setIsCondenserOn() {
		boolean oldIsCondenserOn = isCondenserOn;
		isCondenserOn = isRedstonePowered() && isAssembled();
		if (isCondenserOn != oldIsCondenserOn) {
			if (controller != null) controller.updateBlock(isCondenserOn);
			sendUpdateToAllPlayers();
		}
	}
	
	protected boolean isRedstonePowered() {
		if (controller != null && controller.checkIsRedstonePowered(WORLD, controller.getPos())) return true;
		return false;
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
	
	@Override
	protected CondenserUpdatePacket getUpdatePacket() {
		return new CondenserUpdatePacket(controller.getPos(), isCondenserOn);
	}
	
	@Override
	public void onPacket(CondenserUpdatePacket message) {
		isCondenserOn = message.isCondenserOn;
		// TODO
	}
	
	public Container getContainer(EntityPlayer player) {
		return new ContainerCondenserController(player, controller);
	}
	
	@Override
	public void clearAllFluids() {
		// TODO Auto-generated method stub
		
	}
	
	// Multiblock Validators
	
	@Override
	protected boolean isBlockGoodForInterior(World world, int x, int y, int z, IMultiblockValidator validatorCallback) {
		return true;
	}
}
