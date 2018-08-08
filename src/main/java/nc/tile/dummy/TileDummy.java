package nc.tile.dummy;

import java.util.List;

import ic2.api.energy.tile.IEnergySink;
import ic2.api.energy.tile.IEnergySource;
import nc.tile.energy.ITileEnergy;
import nc.tile.energyFluid.TileEnergyFluidSidedInventory;
import nc.tile.fluid.ITileFluid;
import nc.tile.internal.energy.EnergyConnection;
import nc.tile.internal.energy.EnergyStorage;
import nc.tile.internal.fluid.FluidConnection;
import nc.tile.internal.fluid.Tank;
import nc.tile.inventory.ITileInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.FluidTankProperties;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import net.minecraftforge.fluids.capability.templates.EmptyFluidHandler;
import net.minecraftforge.fml.common.Optional;

public abstract class TileDummy extends TileEnergyFluidSidedInventory {
	
	public BlockPos masterPosition = null;
	public int tickCount;
	public final int updateRate;
	
	public TileDummy(String name, int updateRate, List<String> allowedFluids) {
		this(name, energyConnectionAll(EnergyConnection.BOTH), FluidConnection.BOTH, updateRate, allowedFluids);
	}
	
	public TileDummy(String name, EnergyConnection[] energyConnections, int updateRate, List<String> allowedFluids) {
		this(name, energyConnections, FluidConnection.BOTH, updateRate, allowedFluids);
	}
	
	public TileDummy(String name, FluidConnection fluidConnection, int updateRate, List<String> allowedFluids) {
		this(name, energyConnectionAll(EnergyConnection.BOTH), fluidConnection, updateRate, allowedFluids);
	}
	
	public TileDummy(String name, EnergyConnection[] energyConnections, FluidConnection fluidConnection, int updateRate, List<String> allowedFluids) {
		super(name, 1, 1, energyConnections, 1, fluidConnection, allowedFluids);
		this.updateRate = updateRate;
	}
	
	@Override
	public void update() {
		super.update();
		if(!world.isRemote) {
			if (shouldTileCheck()) findMaster();
			tickTile();
		}
	}
	
	@Override
	public void onAdded() {
		super.onAdded();
		if (!world.isRemote) findMaster();
	}
	
	// Inventory
	
	@Override
	public NonNullList<ItemStack> getInventoryStacks() {
		if (getMaster() != null) {
			if (getMaster() instanceof ITileInventory) return ((ITileInventory) getMaster()).getInventoryStacks();
		}
		return inventoryStacks;
	}
	
	@Override
	public int getSizeInventory() {
		return getInventoryStacks().size();
	}

	@Override
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

	@Override
	public ItemStack getStackInSlot(int slot) {
		return getInventoryStacks().get(slot);
	}

	@Override
	public ItemStack decrStackSize(int index, int count) {
		return ItemStackHelper.getAndSplit(getInventoryStacks(), index, count);
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {
		return ItemStackHelper.getAndRemove(getInventoryStacks(), index);
	}

	@Override
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
		
	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		if (getMaster() != null) {
			if (getMaster() instanceof IInventory) return ((IInventory) getMaster()).isItemValidForSlot(slot, stack);
		}
		return false;
	}

	@Override
	public int getInventoryStackLimit() {
		if (getMaster() != null) {
			if (getMaster() instanceof IInventory) return ((IInventory) getMaster()).getInventoryStackLimit();
		}
		return 1;
	}
		
	@Override
	public void clear() {
		getInventoryStacks().clear();
	}

	@Override
	public boolean isUsableByPlayer(EntityPlayer player) {
		return false;
	}

	@Override
	public void openInventory(EntityPlayer player) {}

	@Override
	public void closeInventory(EntityPlayer player) {}

	@Override
	public int[] getSlotsForFace(EnumFacing side) {
		if (getMaster() != null) {
			if (getMaster() instanceof ISidedInventory) return ((ISidedInventory) getMaster()).getSlotsForFace(side);
		}
		return new int[] {0};
	}

	@Override
	public boolean canInsertItem(int slot, ItemStack stack, EnumFacing direction) {
		if (getMaster() != null) {
			if (getMaster() instanceof ISidedInventory) return ((ISidedInventory) getMaster()).canInsertItem(slot, stack, direction);
		}
		return false;
	}

	@Override
	public boolean canExtractItem(int slot, ItemStack stack, EnumFacing direction) {
		if (getMaster() != null) {
			if (getMaster() instanceof ISidedInventory) return ((ISidedInventory) getMaster()).canExtractItem(slot, stack, direction);
		}
		return false;
	}
	
	// Redstone Flux
	
	@Override
	public EnergyStorage getEnergyStorage() {
		if (getMaster() != null) {
			if (getMaster() instanceof ITileEnergy) return ((ITileEnergy) getMaster()).getEnergyStorage();
		}
		return super.getEnergyStorage();
	}
	
	@Override
	public EnergyConnection getEnergyConnection(EnumFacing side) {
		if (getMaster() != null) {
			if (getMaster() instanceof ITileEnergy) return ((ITileEnergy) getMaster()).getEnergyConnection(side);
		}
		return super.getEnergyConnection(side);
	}
	
	@Override
	public int getEnergyStored() {
		if (getMaster() != null) {
			if (getMaster() instanceof ITileEnergy) return ((ITileEnergy) getMaster()).getEnergyStored();
		}
		return super.getEnergyStored();
	}

	@Override
	public int getMaxEnergyStored() {
		if (getMaster() != null) {
			if (getMaster() instanceof ITileEnergy) return ((ITileEnergy) getMaster()).getMaxEnergyStored();
		}
		return super.getMaxEnergyStored();
	}

	@Override
	public boolean canReceiveEnergy(EnumFacing side) {
		if (getMaster() != null) {
			if (getMaster() instanceof ITileEnergy) return ((ITileEnergy) getMaster()).canReceiveEnergy(side);
		}
		return false;
	}
	
	@Override
	public boolean canExtractEnergy(EnumFacing side) {
		if (getMaster() != null) {
			if (getMaster() instanceof ITileEnergy) return ((ITileEnergy) getMaster()).canExtractEnergy(side);
		}
		return false;
	}
	
	@Override
	public int receiveEnergy(int maxReceive, EnumFacing side, boolean simulate) {
		if (getMaster() != null) {
			if (getMaster() instanceof ITileEnergy) return ((ITileEnergy) getMaster()).receiveEnergy(maxReceive, side, simulate);
		}
		return 0;
	}
	
	@Override
	public int extractEnergy(int maxExtract, EnumFacing side, boolean simulate) {
		if (getMaster() != null) {
			if (getMaster() instanceof ITileEnergy) return ((ITileEnergy) getMaster()).extractEnergy(maxExtract, side, simulate);
		}
		return 0;
	}
	
	// IC2 Energy
	
	@Override
	@Optional.Method(modid = "ic2")
	public int getSourceTier() {
		if (getMaster() != null) {
			if (getMaster() instanceof IEnergySource) return ((IEnergySource) getMaster()).getSourceTier();
		}
		return 1;
	}

	@Override
	@Optional.Method(modid = "ic2")
	public int getSinkTier() {
		if (getMaster() != null) {
			if (getMaster() instanceof IEnergySink) return ((IEnergySink) getMaster()).getSinkTier();
		}
		return 4;
	}
	
	// Fluids
	
	@Override
	public List<Tank> getTanks() {
		if (getMaster() != null) {
			if (getMaster() instanceof ITileFluid) return ((ITileFluid) getMaster()).getTanks();
		}
		return tanks;
	}
	
	@Override
	public List<FluidConnection> getFluidConnections() {
		if (getMaster() != null) {
			if (getMaster() instanceof ITileFluid) return ((ITileFluid) getMaster()).getFluidConnections();
		}
		return fluidConnections;
	}
	
	@Override
	public IFluidTankProperties[] getTankProperties() {
		if (getTanks() == null || getTanks().isEmpty()) return EmptyFluidHandler.EMPTY_TANK_PROPERTIES_ARRAY;
		IFluidTankProperties[] properties = new IFluidTankProperties[getTanks().size()];
		for (int i = 0; i < getTanks().size(); i++) {
			properties[i] = new FluidTankProperties(getTanks().get(i).getFluid(), getTanks().get(i).getCapacity(), getFluidConnections().get(i).canFill(), getFluidConnections().get(i).canDrain());
		}
		return properties;
	}

	@Override
	public int fill(FluidStack resource, boolean doFill) {
		if (getMaster() != null) {
			if (getMaster() instanceof ITileFluid) {
				if (getTanks() == null || getTanks().isEmpty()) return 0;
				for (int i = 0; i < getTanks().size(); i++) {
					if (getFluidConnections().get(i).canFill() && getTanks().get(i).isFluidValid(resource) && canFill(resource, i) && getTanks().get(i).getFluidAmount() < getTanks().get(i).getCapacity() && (getTanks().get(i).getFluid() == null || getTanks().get(i).getFluid().isFluidEqual(resource))) {
						return getTanks().get(i).fill(resource, doFill);
					}
				}
			}
		}
		return 0;
	}

	@Override
	public FluidStack drain(FluidStack resource, boolean doDrain) {
		if (getMaster() != null) {
			if (getMaster() instanceof ITileFluid) {
				if (getTanks() == null || getTanks().isEmpty()) return null;
				for (int i = 0; i < getTanks().size(); i++) {
					if (getFluidConnections().get(i).canDrain() && getTanks().get(i).getFluid() != null && getTanks().get(i).getFluidAmount() > 0) {
						if (resource.isFluidEqual(getTanks().get(i).getFluid()) && getTanks().get(i).drain(resource, false) != null) return getTanks().get(i).drain(resource, doDrain);
					}
				}
			}
		}
		return null;
	}

	@Override
	public FluidStack drain(int maxDrain, boolean doDrain) {
		if (getMaster() != null) {
			if (getMaster() instanceof ITileFluid) {
				if (getTanks() == null || getTanks().isEmpty()) return null;
				for (int i = 0; i < getTanks().size(); i++) {
					if (getFluidConnections().get(i).canDrain() && getTanks().get(i).getFluid() != null && getTanks().get(i).getFluidAmount() > 0) {
						if (getTanks().get(i).drain(maxDrain, false) != null) return getTanks().get(i).drain(maxDrain, doDrain);
					}
				}
			}
		}
		return null;
	}
	
	@Override
	public boolean canFill(FluidStack resource, int tankNumber) {
		if (getMaster() != null) {
			if (getMaster() instanceof ITileFluid) return ((ITileFluid) getMaster()).canFill(resource, tankNumber);
		}
		return super.canFill(resource, tankNumber);
	}
	
	// Mekanism Gas
	
	/*public int receiveGas(EnumFacing side, GasStack stack, boolean doTransfer) {
		String gasName = stack.getGas().getName();
		Fluid fluid = FluidRegistry.getFluid(gasName);
		if (fluid == null) return 0;
		FluidStack fluidStack = new FluidStack(fluid, 1000);
		
		if (getMaster() != null) {
			if (getMaster() instanceof ITileFluid) {
				if (getTanks().length == 0 || getTanks() == null) return 0;
				for (int i = 0; i < getTanks().length; i++) {
					if (getFluidConnections().get(i).canFill() && getTanks().get(i).isFluidValid(fluidStack) && canFill(fluidStack, i) && getTanks().get(i).getFluidAmount() < getTanks().get(i).getCapacity() && (getTanks().get(i).getFluid() == null || getTanks().get(i).getFluid().isFluidEqual(fluidStack))) {
						return tanks.get(i).fill(fluidStack, doTransfer);
					}
				}
			}
		}
		return 0;
	}

	public GasStack drawGas(EnumFacing side, int amount, boolean doTransfer) {
		return null;
	}

	public boolean canReceiveGas(EnumFacing side, Gas gas) {
		Fluid fluid = FluidRegistry.getFluid(gas.getName());
		if (fluid == null) return false;

		if (getMaster() != null) {
			if (getMaster() instanceof ITileFluid) return ((ITileFluid) getMaster()).canReceiveGas(side, gas);
		}
		return super.canReceiveGas(side, gas);
	}

	public boolean canDrawGas(EnumFacing side, Gas type) {
		return false;
	}
	
	public boolean canTubeConnect(EnumFacing side) {
		if (getMaster() != null) {
			for (FluidConnection con : getFluidConnections()) {
				if (con.canFill()) return true;
			}
		}
		return false;
	}*/
	
	// Energy Connections
	
	@Override
	public void pushEnergy() {
		if (getMaster() == null) return;
		super.pushEnergy();
	}
	
	@Override
	public void spreadEnergy() {
		if (getMaster() == null) return;
		super.spreadEnergy();
	}
	
	// Fluid Connections
	
	@Override
	public void setConnection(List<FluidConnection> connections) {
		if (tanks != null && !tanks.isEmpty()) fluidConnections = connections;
	}
	
	@Override
	public void setConnection(FluidConnection connections, int tankNumber) {
		if (tanks != null && !tanks.isEmpty()) fluidConnections.set(tankNumber, connections);
	}
	
	@Override
	public void pushFluid() {
		if (getMaster() == null) return;
		super.pushFluid();
	}
	
	@Override
	public void spreadFluid() {
		if (getMaster() == null) return;
		super.spreadFluid();
	}
	
	// Find Master
	
	/** Find the BlockPos of the master tile entity */
	public abstract void findMaster();
	
	public abstract boolean isMaster(BlockPos pos);
	
	public TileEntity getMaster() {
		if (masterPosition == null) return null;
		if (world.getTileEntity(masterPosition) == null) return null;
		if (isMaster(masterPosition)) return world.getTileEntity(masterPosition);
		return null;
	}
}
