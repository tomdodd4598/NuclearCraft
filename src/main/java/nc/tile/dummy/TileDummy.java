package nc.tile.dummy;

import ic2.api.energy.tile.IEnergyAcceptor;
import ic2.api.energy.tile.IEnergyEmitter;
import ic2.api.energy.tile.IEnergySink;
import ic2.api.energy.tile.IEnergySource;
import nc.ModCheck;
import nc.config.NCConfig;
import nc.energy.EnumStorage.EnergyConnection;
import nc.energy.Storage;
import nc.fluid.EnumTank.FluidConnection;
import nc.fluid.Tank;
import nc.tile.ITileInventory;
import nc.tile.energy.ITileEnergy;
import nc.tile.energyFluid.TileEnergyFluidSidedInventory;
import nc.tile.fluid.ITileFluid;
import net.darkhax.tesla.capability.TeslaCapabilities;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.FluidTankProperties;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import net.minecraftforge.fluids.capability.templates.EmptyFluidHandler;

public abstract class TileDummy extends TileEnergyFluidSidedInventory {
	
	public BlockPos masterPosition = null;
	public int tickCount;
	public final int updateRate;
	
	public TileDummy(String name, int updateRate, String[]... allowedFluids) {
		this(name, EnergyConnection.BOTH, FluidConnection.BOTH, updateRate, allowedFluids);
	}
	
	public TileDummy(String name, EnergyConnection energyConnection, int updateRate, String[]... allowedFluids) {
		this(name, energyConnection, FluidConnection.BOTH, updateRate, allowedFluids);
	}
	
	public TileDummy(String name, FluidConnection fluidConnection, int updateRate, String[]... allowedFluids) {
		this(name, EnergyConnection.BOTH, fluidConnection, updateRate, allowedFluids);
	}
	
	public TileDummy(String name, EnergyConnection energyConnection, FluidConnection fluidConnection, int updateRate, String[]... allowedFluids) {
		super(name, 1, 1, energyConnection, new int[] {1}, new FluidConnection[] {fluidConnection}, allowedFluids);
		this.updateRate = updateRate;
	}
	
	public void update() {
		super.update();
		if(!world.isRemote) {
			if (shouldUpdate()) findMaster();
			tick();
		}
	}
	
	public void tick() {
		if (tickCount > updateRate) {
			tickCount = 0;
		} else {
			tickCount++;
		}
	}
	
	public boolean shouldUpdate() {
		return tickCount > updateRate;
	}
	
	public void onAdded() {
		super.onAdded();
		if (!world.isRemote) findMaster();
	}
	
	// Inventory
	
	public NonNullList<ItemStack> getInventoryStacks() {
		if (getMaster() != null) {
			if (getMaster() instanceof ITileInventory) return ((ITileInventory) getMaster()).getInventoryStacks();
		}
		return inventoryStacks;
	}
	
	public int getSizeInventory() {
		return getInventoryStacks().size();
	}

	public boolean isEmpty() {
		if (getMaster() != null) {
			if (getMaster() instanceof IInventory) ((IInventory) getMaster()).isEmpty();
		}
		for (ItemStack itemstack : inventoryStacks) {
			if (!itemstack.isEmpty()) {
				return false;
			}
		}
		return true;
	}

	public ItemStack getStackInSlot(int slot) {
		return getInventoryStacks().get(slot);
	}

	public ItemStack decrStackSize(int index, int count) {
		return ItemStackHelper.getAndSplit(getInventoryStacks(), index, count);
	}

	public ItemStack removeStackFromSlot(int index) {
		return ItemStackHelper.getAndRemove(getInventoryStacks(), index);
	}

	public void setInventorySlotContents(int index, ItemStack stack) {
		if (getMaster() != null) {
			if (getMaster() instanceof IInventory) {
				((IInventory) getMaster()).setInventorySlotContents(index, stack);
				return;
			}
		}
		ItemStack itemstack = inventoryStacks.get(index);
		boolean flag = !stack.isEmpty() && stack.isItemEqual(itemstack) && ItemStack.areItemStackTagsEqual(stack, itemstack);
		inventoryStacks.set(index, stack);

		if (stack.getCount() > getInventoryStackLimit()) {
			stack.setCount(getInventoryStackLimit());
		}

		if (index == 0 && !flag) {
			markDirty();
		}
	}
		
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		if (getMaster() != null) {
			if (getMaster() instanceof IInventory) return ((IInventory) getMaster()).isItemValidForSlot(slot, stack);
		}
		return false;
	}

	public int getInventoryStackLimit() {
		if (getMaster() != null) {
			if (getMaster() instanceof IInventory) return ((IInventory) getMaster()).getInventoryStackLimit();
		}
		return 1;
	}
		
	public void clear() {
		getInventoryStacks().clear();
	}

	public boolean isUsableByPlayer(EntityPlayer player) {
		return false;
	}

	public void openInventory(EntityPlayer player) {}

	public void closeInventory(EntityPlayer player) {}

	public int[] getSlotsForFace(EnumFacing side) {
		if (getMaster() != null) {
			if (getMaster() instanceof ISidedInventory) return ((ISidedInventory) getMaster()).getSlotsForFace(side);
		}
		return new int[] {0};
	}

	public boolean canInsertItem(int slot, ItemStack stack, EnumFacing direction) {
		if (getMaster() != null) {
			if (getMaster() instanceof ISidedInventory) return ((ISidedInventory) getMaster()).canInsertItem(slot, stack, direction);
		}
		return false;
	}

	public boolean canExtractItem(int slot, ItemStack stack, EnumFacing direction) {
		if (getMaster() != null) {
			if (getMaster() instanceof ISidedInventory) return ((ISidedInventory) getMaster()).canExtractItem(slot, stack, direction);
		}
		return false;
	}
	
	// Redstone Flux
	
	public Storage getStorage() {
		if (getMaster() != null) {
			if (getMaster() instanceof ITileEnergy) return ((ITileEnergy) getMaster()).getStorage();
		}
		return storage;
	}
	
	public EnergyConnection getEnergyConnection() {
		if (getMaster() != null) {
			if (getMaster() instanceof ITileEnergy) return ((ITileEnergy) getMaster()).getEnergyConnection();
		}
		return connection;
	}
	
	public int getEnergyStored() {
		if (getMaster() != null) {
			if (getMaster() instanceof IEnergyStorage) return ((IEnergyStorage) getMaster()).getEnergyStored();
		}
		return getStorage().getEnergyStored();
	}

	public int getMaxEnergyStored() {
		if (getMaster() != null) {
			if (getMaster() instanceof IEnergyStorage) return ((IEnergyStorage) getMaster()).getMaxEnergyStored();
		}
		return getStorage().getMaxEnergyStored();
	}

	public int receiveEnergy(int maxReceive, boolean simulate) {
		if (getMaster() != null) {
			if (getMaster() instanceof IEnergyStorage) return ((IEnergyStorage) getMaster()).receiveEnergy(maxReceive, simulate);
		}
		return 0;
	}
	
	public int extractEnergy(int maxExtract, boolean simulate) {
		if (getMaster() != null) {
			if (getMaster() instanceof IEnergyStorage) return ((IEnergyStorage) getMaster()).extractEnergy(maxExtract, simulate);
		}
		return 0;
	}
	
	public boolean canExtract() {
		if (getMaster() != null) {
			if (getMaster() instanceof IEnergyStorage) return ((IEnergyStorage) getMaster()).canExtract();
		}
		return false;
	}

	public boolean canReceive() {
		if (getMaster() != null) {
			if (getMaster() instanceof IEnergyStorage) return ((IEnergyStorage) getMaster()).canReceive();
		}
		return false;
	}
	
	// IC2 Energy
	
	public boolean acceptsEnergyFrom(IEnergyEmitter emitter, EnumFacing side) {
		return getEnergyConnection().canReceive();
	}

	public boolean emitsEnergyTo(IEnergyAcceptor receiver, EnumFacing side) {
		return getEnergyConnection().canExtract();
	}

	public double getOfferedEnergy() {
		return Math.min(Math.pow(2, 2*getSourceTier() + 3), getStorage().takePower(getStorage().maxExtract, true) / NCConfig.generator_rf_per_eu);
	}
	
	public double getDemandedEnergy() {
		return Math.min(Math.pow(2, 2*getSinkTier() + 3), getStorage().givePower(getStorage().maxReceive, true) / NCConfig.processor_rf_per_eu);
	}
	
	/** The normal conversion is 4 RF to 1 EU, but for RF generators, this is OP, so the ratio is instead 16:1 */
	public void drawEnergy(double amount) {
		getStorage().takePower((long) (NCConfig.generator_rf_per_eu * amount), false);
	}

	public double injectEnergy(EnumFacing directionFrom, double amount, double voltage) {
		int energyReceived = getStorage().receiveEnergy((int) (NCConfig.processor_rf_per_eu * amount), true);
		getStorage().givePower(energyReceived, false);
		return amount - (energyReceived / NCConfig.processor_rf_per_eu);
	}
	
	public int getSourceTier() {
		if (getMaster() != null) {
			if (getMaster() instanceof IEnergySource) return ((IEnergySource) getMaster()).getSourceTier();
		}
		return 1;
	}

	public int getSinkTier() {
		if (getMaster() != null) {
			if (getMaster() instanceof IEnergySink) return ((IEnergySink) getMaster()).getSinkTier();
		}
		return 4;
	}
	
	// Fluids
	
	public Tank[] getTanks() {
		if (getMaster() != null) {
			if (getMaster() instanceof ITileFluid) return ((ITileFluid) getMaster()).getTanks();
		}
		return tanks;
	}
	
	public FluidConnection[] getFluidConnections() {
		if (getMaster() != null) {
			if (getMaster() instanceof ITileFluid) return ((ITileFluid) getMaster()).getFluidConnections();
		}
		return fluidConnection;
	}
	
	public IFluidTankProperties[] getTankProperties() {
		if (getTanks().length == 0 || getTanks() == null) return EmptyFluidHandler.EMPTY_TANK_PROPERTIES_ARRAY;
		IFluidTankProperties[] properties = new IFluidTankProperties[getTanks().length];
		for (int i = 0; i < getTanks().length; i++) {
			properties[i] = new FluidTankProperties(getTanks()[i].getFluid(), getTanks()[i].getCapacity(), getFluidConnections()[i].canFill(), getFluidConnections()[i].canDrain());
		}
		return properties;
	}

	public int fill(FluidStack resource, boolean doFill) {
		if (getMaster() != null) {
			if (getMaster() instanceof ITileFluid) {
				if (getTanks().length == 0 || getTanks() == null) return 0;
				for (int i = 0; i < getTanks().length; i++) {
					if (getFluidConnections()[i].canFill() && getTanks()[i].isFluidValid(resource) && canFill(resource, i) && getTanks()[i].getFluidAmount() < getTanks()[i].getCapacity() && (getTanks()[i].getFluid() == null || getTanks()[i].getFluid().isFluidEqual(resource))) {
						return getTanks()[i].fill(resource, doFill);
					}
				}
			}
		}
		return 0;
	}

	public FluidStack drain(FluidStack resource, boolean doDrain) {
		if (getMaster() != null) {
			if (getMaster() instanceof ITileFluid) {
				if (getTanks().length == 0 || getTanks() == null) return null;
				for (int i = 0; i < getTanks().length; i++) {
					if (getFluidConnections()[i].canDrain() && getTanks()[i].getFluid() != null && getTanks()[i].getFluidAmount() > 0) {
						return getTanks()[i].drain(resource.amount, doDrain);
					}
				}
			}
		}
		return null;
	}

	public FluidStack drain(int maxDrain, boolean doDrain) {
		if (getMaster() != null) {
			if (getMaster() instanceof ITileFluid) {
				if (getTanks().length == 0 || getTanks() == null) return null;
				for (int i = 0; i < getTanks().length; i++) {
					if (getFluidConnections()[i].canDrain() && getTanks()[i].getFluid() != null && getTanks()[i].getFluidAmount() > 0) {
						return getTanks()[i].drain(maxDrain, doDrain);
					}
				}
			}
		}
		return null;
	}
	
	public boolean canFill(FluidStack resource, int tankNumber) {
		if (getMaster() != null) {
			if (getMaster() instanceof ITileFluid) return ((ITileFluid) getMaster()).canFill(resource, tankNumber);
		}
		return super.canFill(resource, tankNumber);
	}
	
	// Energy Connections
	
	public void setConnection(EnergyConnection connection) {
		this.connection = connection;
	}
	
	public void pushEnergy() {
		if (getMaster() == null) return;
		if (getStorage().getEnergyStored() <= 0 || !getEnergyConnection().canExtract()) return;
		for (EnumFacing side : EnumFacing.VALUES) {
			TileEntity tile = world.getTileEntity(getPos().offset(side));
			IEnergyStorage adjStorage = tile == null ? null : tile.getCapability(CapabilityEnergy.ENERGY, side.getOpposite());
			//TileEntity thisTile = world.getTileEntity(getPos());
			
			if (adjStorage != null && storage.canExtract()) {
				getStorage().extractEnergy(adjStorage.receiveEnergy(getStorage().extractEnergy(getStorage().getMaxEnergyStored(), true), false), false);
			}
			else if (tile instanceof IEnergySink /*&& tile != thisTile*/) {
				getStorage().extractEnergy((int) Math.round(((IEnergySink) tile).injectEnergy(side.getOpposite(), getStorage().extractEnergy(getStorage().getMaxEnergyStored(), true) / 24, getSourceTier())), false);
			}
		}
	}
	
	// Fluid Connections
	
	public void setConnection(FluidConnection[] connection) {
		if (tanks.length > 0 && tanks != null) fluidConnection = connection;
	}
	
	public void setConnection(FluidConnection connection, int tankNumber) {
		if (tanks.length > 0 && tanks != null) fluidConnection[tankNumber] = connection;
	}
	
	public void pushFluid() {
		if (getTanks().length > 0 && getTanks() != null) for (int i = 0; i < getTanks().length; i++) {
			if (getTanks()[i].getFluidAmount() <= 0 || !getFluidConnections()[i].canDrain()) return;
			for (EnumFacing side : EnumFacing.VALUES) {
				TileEntity tile = world.getTileEntity(getPos().offset(side));
				IFluidHandler adjStorage = tile == null ? null : tile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side.getOpposite());
				//TileEntity thisTile = world.getTileEntity(getPos());
				
				if (tile instanceof IFluidHandler /*&& tile != thisTile*/) {
					getTanks()[i].drain(((IFluidHandler) tile).fill(getTanks()[i].drain(getTanks()[i].getCapacity(), false), true), true);
				}
				if (adjStorage != null) {
					getTanks()[i].drain(adjStorage.fill(getTanks()[i].drain(getTanks()[i].getCapacity(), false), true), true);
				}
			}
		}
	}
	
	// Find Master
	
	/** Find the BlockPos of the master tile entity */
	protected abstract void findMaster();
	
	public abstract boolean isMaster(BlockPos pos);
	
	public TileEntity getMaster() {
		if (masterPosition == null) return null;
		if (world.getTileEntity(masterPosition) == null) return null;
		if (isMaster(masterPosition)) return world.getTileEntity(masterPosition);
		return null;
	}
	
	// Capability
	
	net.minecraftforge.items.IItemHandler handlerTop = new net.minecraftforge.items.wrapper.SidedInvWrapper(this, net.minecraft.util.EnumFacing.UP);
	net.minecraftforge.items.IItemHandler handlerBottom = new net.minecraftforge.items.wrapper.SidedInvWrapper(this, net.minecraft.util.EnumFacing.DOWN);
	net.minecraftforge.items.IItemHandler handlerSide = new net.minecraftforge.items.wrapper.SidedInvWrapper(this, net.minecraft.util.EnumFacing.WEST);
	
	@SuppressWarnings("unchecked")
	public <T> T getCapability(net.minecraftforge.common.capabilities.Capability<T> capability, @javax.annotation.Nullable net.minecraft.util.EnumFacing facing) {
		if (CapabilityEnergy.ENERGY == capability && connection.canConnect()) {
			return (T) getStorage();
		}
		if (connection != null && ModCheck.teslaLoaded && connection.canConnect()) {
			if ((capability == TeslaCapabilities.CAPABILITY_CONSUMER && connection.canReceive()) || (capability == TeslaCapabilities.CAPABILITY_PRODUCER && connection.canExtract()) || capability == TeslaCapabilities.CAPABILITY_HOLDER)
				return (T) getStorage();
		}
		if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
			return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(this);
		}
		if (facing != null && capability == net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			if (facing == EnumFacing.DOWN) {
				return (T) handlerBottom;
			} else if (facing == EnumFacing.UP) {
				return (T) handlerTop;
			} else {
				return (T) handlerSide;
			}
		}
		return null;
	}
}
