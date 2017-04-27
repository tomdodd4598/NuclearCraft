package nc.tile.generator;

import cofh.api.energy.IEnergyReceiver;
import ic2.api.energy.tile.IEnergyAcceptor;
import ic2.api.energy.tile.IEnergyEmitter;
import ic2.api.energy.tile.IEnergySink;
import nc.block.tile.energy.generator.BlockFissionController;
import nc.block.tile.energy.generator.BlockFissionPort;
import nc.config.NCConfig;
import nc.energy.EnumStorage.EnergyConnection;
import nc.init.NCBlocks;
import nc.tile.energy.TileEnergySidedInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public class TileFissionPort extends TileEnergySidedInventory {
	
	public int tickCount;
	
	public BlockPos controllerPosition = null;

	public TileFissionPort() {
		super("fission_port", 1, 1, EnergyConnection.OUT);
	}
	
	public void update() {
		super.update();
		if(!worldObj.isRemote) {
			tick();
			findController();
			pushEnergy();
		}
	}
	
	public void onAdded() {
		super.onAdded();
		if (!worldObj.isRemote) findController();
	}
	
	public void tick() {
		if (tickCount > NCConfig.fission_update_rate) {
			tickCount = 0;
		} else {
			tickCount++;
		}
	}
	
	public boolean shouldUpdate() {
		return tickCount > NCConfig.fission_update_rate;
	}
	
	// Inventory
	
	public int getSizeInventory() {
		return getController() != null ? getController().inventoryStacks.length : inventoryStacks.length;
	}

	public boolean isEmpty() {
		if (getController() != null) return getController().isEmpty();
		for (ItemStack itemstack : inventoryStacks) {
			if (itemstack != null) {
				return false;
			}
		}
		return true;
	}

	public ItemStack getStackInSlot(int slot) {
		return getController() != null ? getController().inventoryStacks[slot] : inventoryStacks[slot];
	}

	public ItemStack decrStackSize(int index, int count) {
		return ItemStackHelper.getAndSplit(getController() != null ? getController().inventoryStacks : inventoryStacks, index, count);
	}

	public ItemStack removeStackFromSlot(int index) {
		return ItemStackHelper.getAndRemove(getController() != null ? getController().inventoryStacks : inventoryStacks, index);
	}

	public void setInventorySlotContents(int index, ItemStack stack) {
		if (getController() != null) {
			getController().setInventorySlotContents(index, stack);
			return;
		}
		ItemStack itemstack = inventoryStacks[index];
		boolean flag = stack != null && stack.isItemEqual(itemstack) && ItemStack.areItemStackTagsEqual(stack, itemstack);
		inventoryStacks[index] = stack;

		if (stack.stackSize > getInventoryStackLimit()) {
			stack.stackSize = getInventoryStackLimit();
		}

		if (index == 0 && !flag) {
			markDirty();
		}
	}
		
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		return getController() != null ? getController().isItemValidForSlot(slot, stack) : false;
	}

	public int getInventoryStackLimit() {
		return 64;
	}
	
	public void clear() {
		for (int i = 0; i < (getController() != null ? getController().inventoryStacks : inventoryStacks).length; ++i) {
			(getController() != null ? getController().inventoryStacks : inventoryStacks)[i] = null;
		}
	}

	public boolean isUseableByPlayer(EntityPlayer player) {
		return false;
	}

	public void openInventory(EntityPlayer player) {}

	public void closeInventory(EntityPlayer player) {}

	public int[] getSlotsForFace(EnumFacing side) {
		return getController() != null ? getController().getSlotsForFace(side) : new int[] {0};
	}

	public boolean canInsertItem(int slot, ItemStack stack, EnumFacing direction) {
		return getController() != null ? getController().canInsertItem(slot, stack, direction) : false;
	}

	public boolean canExtractItem(int slot, ItemStack stack, EnumFacing direction) {
		return getController() != null ? getController().canExtractItem(slot, stack, direction) : false;
	}
	
	// Redstone Flux
	
	public int getEnergyStored(EnumFacing from) {
		return getController() != null ? getController().storage.getEnergyStored() : storage.getEnergyStored();
	}

	public int getMaxEnergyStored(EnumFacing from) {
		return getController() != null ? getController().storage.getMaxEnergyStored() : storage.getMaxEnergyStored();
	}
	
	public int getEnergyStored() {
		return getController() != null ? getController().storage.getEnergyStored() : storage.getEnergyStored();
	}

	public int getMaxEnergyStored() {
		return getController() != null ? getController().storage.getMaxEnergyStored() : storage.getMaxEnergyStored();
	}

	public boolean canConnectEnergy(EnumFacing from) {
		return getController() != null ? getController().connection.canConnect() : connection.canConnect();
	}

	public int receiveEnergy(EnumFacing from, int maxReceive, boolean simulate) {
		return getController() != null ? getController().storage.receiveEnergy(maxReceive, simulate) : 0;
	}
	
	public int extractEnergy(EnumFacing from, int maxExtract, boolean simulate) {
		return getController() != null ? getController().storage.extractEnergy(maxExtract, simulate) : 0;
	}
	
	public int receiveEnergy(int maxReceive, boolean simulate) {
		return getController() != null ? getController().storage.receiveEnergy(maxReceive, simulate) : 0;
	}

	public int extractEnergy(int maxExtract, boolean simulate) {
		return getController() != null ? getController().storage.extractEnergy(maxExtract, simulate) : 0;
	}
	
	// IC2 Energy
	
	public boolean acceptsEnergyFrom(IEnergyEmitter emitter, EnumFacing side) {
		return getController() != null ? getController().connection.canReceive() : connection.canReceive();
	}

	public boolean emitsEnergyTo(IEnergyAcceptor receiver, EnumFacing side) {
		return getController() != null ? getController().connection.canExtract() : connection.canExtract();
	}

	public double getOfferedEnergy() {
		return Math.min(Math.pow(2, 2*getSourceTier() + 3), (getController() != null ? getController().storage : storage).takePower((getController() != null ? getController().storage : storage).maxExtract, true) / NCConfig.generator_rf_per_eu);
	}
	
	public double getDemandedEnergy() {
		return Math.min(Math.pow(2, 2*getSinkTier() + 3), (getController() != null ? getController().storage : storage).givePower((getController() != null ? getController().storage : storage).maxReceive, true) / NCConfig.processor_rf_per_eu);
	}
	
	/** The normal conversion is 4 RF to 1 EU, but for RF generators, this is OP, so the ratio is instead 16:1 */
	public void drawEnergy(double amount) {
		(getController() != null ? getController().storage : storage).takePower((long) (NCConfig.generator_rf_per_eu * amount), false);
	}

	public double injectEnergy(EnumFacing directionFrom, double amount, double voltage) {
		int energyReceived = (getController() != null ? getController().storage : storage).receiveEnergy((int) (NCConfig.processor_rf_per_eu * amount), true);
		(getController() != null ? getController().storage : storage).givePower(energyReceived, false);
		return amount - (energyReceived / NCConfig.processor_rf_per_eu);
	}
	
	public int getSourceTier() {
		return getController() != null ? getController().getSourceTier() : 4;
	}

	public int getSinkTier() {
		return getController() != null ? getController().getSinkTier() : 4;
	}
	
	// Energy Connections
	
	public void setConnection(EnergyConnection connection) {
		if (getController() != null) getController().setConnection(connection); this.connection = connection;
	}
	
	public void pushEnergy() {
		if (getController() == null) return;
		if (getController().storage.getEnergyStored() <= 0 || !getController().connection.canExtract()) return;
		for (EnumFacing side : EnumFacing.VALUES) {
			TileEntity tile = worldObj.getTileEntity(getPos().offset(side));
			//TileEntity thisTile = world.getTileEntity(getPos());
			
			if (tile instanceof IEnergyReceiver /*&& tile != thisTile*/) {
				getController().storage.extractEnergy(((IEnergyReceiver) tile).receiveEnergy(side.getOpposite(), getController().storage.extractEnergy(getController().storage.getMaxEnergyStored(), true), false), false);
			}
			else if (tile instanceof IEnergySink /*&& tile != thisTile*/) {
				getController().storage.extractEnergy((int) Math.round(((IEnergySink) tile).injectEnergy(side.getOpposite(), getController().storage.extractEnergy(getController().storage.getMaxEnergyStored(), true) / 24, getSourceTier())), false);
			}
		}
	}
	
	// Finding Blocks
	
	private BlockPos position(int x, int y, int z) {
		int xCheck = getPos().getX();
		int yCheck = getPos().getY() + y;
		int zCheck = getPos().getZ();
		
		if (getBlockMetadata() == 4) {
			return new BlockPos(xCheck + x, yCheck, zCheck + z);
		}
		if (getBlockMetadata() == 2) {
			return new BlockPos(xCheck - z, yCheck, zCheck + x);
		}
		if (getBlockMetadata() == 5) {
			return new BlockPos(xCheck - x, yCheck, zCheck - z);
		}
		if (getBlockMetadata() == 3) {
			return new BlockPos(xCheck + z, yCheck, zCheck - x);
		}
		else return new BlockPos(xCheck + x, yCheck, zCheck + z);
	}
	
	private boolean findCasingPort(int x, int y, int z) {
		if (worldObj.getBlockState(position(x, y, z)) == NCBlocks.fission_block.getStateFromMeta(0)) return true;
		if (worldObj.getBlockState(position(x, y, z)).getBlock() instanceof BlockFissionPort) return true;
		return false;
	}
	
	private boolean findController(int x, int y, int z) {
		return worldObj.getBlockState(position(x, y, z)).getBlock() instanceof BlockFissionController;
	}
	
	private boolean findCasingControllerPort(int x, int y, int z) {
		if (worldObj.getBlockState(position(x, y, z)) == NCBlocks.fission_block.getStateFromMeta(0)) return true;
		if (worldObj.getBlockState(position(x, y, z)).getBlock() instanceof BlockFissionController) return true;
		if (worldObj.getBlockState(position(x, y, z)).getBlock() instanceof BlockFissionPort) return true;
		return false;
	}
	
	// Finding Structure
	
	private void findController() {
		if (shouldUpdate()) {
			int l = NCConfig.fission_max_size + 2;
			boolean f = false;
			int rz = 0;
			int z0 = 0;
			int x0 = 0;
			int y0 = 0;
			int z1 = 0;
			int x1 = 0;
			int y1 = 0;
			for (int z = 0; z <= l; z++) {
				if ((findCasingPort(0, 1, 0) || findCasingPort(0, -1, 0)) || ((findCasingPort(1, 1, 0) || findCasingPort(1, -1, 0)) && findCasingPort(1, 0, 0)) || ((findCasingPort(1, 1, 0) && !findCasingPort(1, -1, 0)) && !findCasingPort(1, 0, 0)) || ((!findCasingPort(1, 1, 0) && findCasingPort(1, -1, 0)) && !findCasingPort(1, 0, 0))) {
					if (/*!find(b, 0, 0, -z) &&*/ !findCasingPort(0, 1, -z) && !findCasingPort(0, -1, -z) && (findCasingControllerPort(0, 0, -z + 1) || findCasingControllerPort(0, 1, -z + 1) || findCasingControllerPort(0, -1, -z + 1))) {
						rz = l - z;
						z0 = -z;
						f = true;
						break;
					}
				} else if (!findCasingPort(0, 0, -z) && !findCasingPort(1, 1, -z) && !findCasingPort(1, -1, -z) && findCasingControllerPort(0, 0, -z + 1) && findCasingPort(1, 0, -z) && findCasingPort(1, 1, -z + 1) && findCasingPort(1, -1, -z + 1)) {
					rz = l - z;
					z0 = -z;
					f = true;
					break;
				}
			}
			if (!f) {
				controllerPosition = null;
				return;
			}
			f = false;
			for (int y = 0; y <= l; y++) {
				if (/*!find(b, x0, -y, z0) && */!findCasingPort(x0, -y + 1, z0) && !findCasingPort(x0 + 1, -y, z0) && !findCasingPort(x0, -y, z0 + 1) && findCasingControllerPort(x0 + 1, -y, z0 + 1) && findCasingControllerPort(x0, -y + 1, z0 + 1) && findCasingControllerPort(x0 + 1, -y + 1, z0)) {
					y0 = -y;
					f = true;
					break;
				}
			}
			if (!f) {
				controllerPosition = null;
				return;
			}
			f = false;
			for (int z = 0; z <= rz; z++) {
				if (/*!find(b, x0, y0, z) &&*/ !findCasingPort(x0, y0 + 1, z) && !findCasingPort(x0 + 1, y0, z) && !findCasingPort(x0, y0, z - 1) && findCasingControllerPort(x0 + 1, y0, z - 1) && findCasingControllerPort(x0, y0 + 1, z - 1) && findCasingControllerPort(x0 + 1, y0 + 1, z)) {
					z1 = z;
					f = true;
					break;
				}
			}
			if (!f) {
				controllerPosition = null;
				return;
			}
			f = false;
			for (int x = 0; x <= l; x++) {
				if (/*!find(b, x0 + x, y0, z0) &&*/ !findCasingPort(x0 + x, y0 + 1, z0) && !findCasingPort(x0 + x - 1, y0, z0) && !findCasingPort(x0 + x, y0, z0 + 1) && findCasingControllerPort(x0 + x - 1, y0, z0 + 1) && findCasingControllerPort(x0 + x, y0 + 1, z0 + 1) && findCasingControllerPort(x0 + x - 1, y0 + 1, z0)) {
					x1 = x0 + x;
					f = true;
					break;
				}
			}
			if (!f) {
				controllerPosition = null;
				return;
			}
			f = false;
			for (int y = 0; y <= l; y++) {
				if (/*!find(b, x0, y0 + y, z0) &&*/ !findCasingPort(x0, y0 + y - 1, z0) && !findCasingPort(x0 + 1, y0 + y, z0) && !findCasingPort(x0, y0 + y, z0 + 1) && findCasingControllerPort(x0 + 1, y0 + y, z0 + 1) && findCasingControllerPort(x0, y0 + y - 1, z0 + 1) && findCasingControllerPort(x0 + 1, y0 + y - 1, z0)) {
					y1 = y0 + y;
					f = true;
					break;
				}
			}
			if (!f) {
				controllerPosition = null;
				return;
			}
			f = false;
			if ((x0 > 0 || x1 < 0) || (y0 > 0 || y1 < 0) || (z0 > 0 || z1 < 0) || x1 - x0 < 1 || y1 - y0 < 1 || z1 - z0 < 1) {
				controllerPosition = null;
				return;
			}
			for (int z = z0; z <= z1; z++) {
				for (int x = x0; x <= x1; x++) {
					for (int y = y0; y <= y1; y++) {
						if(worldObj.getTileEntity(position(x, y, z)) != null) {
							if(worldObj.getTileEntity(position(x, y, z)) instanceof TileFissionController) {
								controllerPosition = position(x, y, z);
								return;
							}
						}
					}
				}
			}
			controllerPosition = null;
		}
	}
	
	public TileFissionController getController() {
		if (controllerPosition == null) return null;
		if (worldObj.getTileEntity(controllerPosition) instanceof TileFissionController) return (TileFissionController) worldObj.getTileEntity(controllerPosition);
		return null;
	}
}
