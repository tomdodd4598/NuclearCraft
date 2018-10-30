package nc.tile.dummy;

import java.util.List;

import javax.annotation.Nonnull;

import nc.tile.energy.ITileEnergy;
import nc.tile.energyFluid.TileEnergyFluidSidedInventory;
import nc.tile.fluid.ITileFluid;
import nc.tile.internal.energy.EnergyConnection;
import nc.tile.internal.energy.EnergyStorage;
import nc.tile.internal.fluid.FluidConnection;
import nc.tile.internal.fluid.FluidTileWrapper;
import nc.tile.internal.fluid.GasTileWrapper;
import nc.tile.internal.fluid.Tank;
import nc.tile.internal.fluid.TankSorption;
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

public abstract class TileDummy<T extends TileEntity> extends TileEnergyFluidSidedInventory {
	
	public BlockPos masterPosition = null;
	public final int updateRate;
	
	protected final Class<T> tClass;
	
	public TileDummy(Class<T> tClass, String name, int updateRate, List<String> allowedFluids) {
		this(tClass, name, ITileEnergy.energyConnectionAll(EnergyConnection.BOTH), TankSorption.BOTH, updateRate, allowedFluids, ITileFluid.fluidConnectionAll(FluidConnection.BOTH));
	}
	
	public TileDummy(Class<T> tClass, String name, EnergyConnection[] energyConnections, int updateRate, List<String> allowedFluids) {
		this(tClass, name, energyConnections, TankSorption.BOTH, updateRate, allowedFluids, ITileFluid.fluidConnectionAll(FluidConnection.BOTH));
	}
	
	public TileDummy(Class<T> tClass, String name, TankSorption fluidConnection, int updateRate, List<String> allowedFluids, @Nonnull FluidConnection[] fluidConnections) {
		this(tClass, name, ITileEnergy.energyConnectionAll(EnergyConnection.BOTH), fluidConnection, updateRate, allowedFluids, fluidConnections);
	}
	
	public TileDummy(Class<T> tClass, String name, EnergyConnection[] energyConnections, TankSorption fluidConnection, int updateRate, List<String> allowedFluids, @Nonnull FluidConnection[] fluidConnections) {
		super(name, 1, 1, energyConnections, 1, fluidConnection, allowedFluids, fluidConnections);
		this.updateRate = updateRate;
		this.tClass = tClass;
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
		if (getMaster() instanceof ITileInventory) return ((ITileInventory) getMaster()).getInventoryStacks();
		return inventoryStacks;
	}
	
	@Override
	public int getSizeInventory() {
		return getInventoryStacks().size();
	}

	@Override
	public boolean isEmpty() {
		if (getMaster() instanceof IInventory) ((IInventory) getMaster()).isEmpty();
		for (ItemStack itemstack : inventoryStacks) {
			if (!itemstack.isEmpty()) return false;
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
		if (getMaster() instanceof IInventory) {
			((IInventory) getMaster()).setInventorySlotContents(index, stack);
			return;
		}
		ItemStack itemstack = inventoryStacks.get(index);
		boolean flag = !stack.isEmpty() && stack.isItemEqual(itemstack) && nc.util.ItemStackHelper.areItemStackTagsEqual(stack, itemstack);
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
		if (getMaster() instanceof IInventory) return ((IInventory) getMaster()).isItemValidForSlot(slot, stack);
		return false;
	}

	@Override
	public int getInventoryStackLimit() {
		if (getMaster() instanceof IInventory) return ((IInventory) getMaster()).getInventoryStackLimit();
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
		if (getMaster() instanceof ISidedInventory) return ((ISidedInventory) getMaster()).getSlotsForFace(side);
		return new int[] {0};
	}

	@Override
	public boolean canInsertItem(int slot, ItemStack stack, EnumFacing direction) {
		if (getMaster() instanceof ISidedInventory) return ((ISidedInventory) getMaster()).canInsertItem(slot, stack, direction);
		return false;
	}

	@Override
	public boolean canExtractItem(int slot, ItemStack stack, EnumFacing direction) {
		if (getMaster() instanceof ISidedInventory) return ((ISidedInventory) getMaster()).canExtractItem(slot, stack, direction);
		return false;
	}
	
	// Redstone Flux
	
	@Override
	public EnergyStorage getEnergyStorage() {
		if (getMaster() instanceof ITileEnergy) return ((ITileEnergy) getMaster()).getEnergyStorage();
		return super.getEnergyStorage();
	}
	
	@Override
	public EnergyConnection getEnergyConnection(@Nonnull EnumFacing side) {
		if (getMaster() instanceof ITileEnergy) return ((ITileEnergy) getMaster()).getEnergyConnection(side);
		return super.getEnergyConnection(side);
	}
	
	@Override
	public int getEnergyStored() {
		if (getMaster() instanceof ITileEnergy) return ((ITileEnergy) getMaster()).getEnergyStored();
		return super.getEnergyStored();
	}

	@Override
	public int getMaxEnergyStored() {
		if (getMaster() instanceof ITileEnergy) return ((ITileEnergy) getMaster()).getMaxEnergyStored();
		return super.getMaxEnergyStored();
	}

	@Override
	public boolean canReceiveEnergy(EnumFacing side) {
		if (getMaster() instanceof ITileEnergy) return ((ITileEnergy) getMaster()).canReceiveEnergy(side);
		return false;
	}
	
	@Override
	public boolean canExtractEnergy(EnumFacing side) {
		if (getMaster() instanceof ITileEnergy) return ((ITileEnergy) getMaster()).canExtractEnergy(side);
		return false;
	}
	
	@Override
	public int receiveEnergy(int maxReceive, EnumFacing side, boolean simulate) {
		if (getMaster() instanceof ITileEnergy) return ((ITileEnergy) getMaster()).receiveEnergy(maxReceive, side, simulate);
		return 0;
	}
	
	@Override
	public int extractEnergy(int maxExtract, EnumFacing side, boolean simulate) {
		if (getMaster() instanceof ITileEnergy) return ((ITileEnergy) getMaster()).extractEnergy(maxExtract, side, simulate);
		return 0;
	}
	
	// IC2 Energy
	
	@Override
	public int getEUSourceTier() {
		if (getMaster() instanceof ITileEnergy) return ((ITileEnergy) getMaster()).getEUSourceTier();
		return 1;
	}

	@Override
	public int getEUSinkTier() {
		if (getMaster() instanceof ITileEnergy) return ((ITileEnergy) getMaster()).getEUSinkTier();
		return 4;
	}
	
	// Energy Distribution
	
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
	
	// Fluids
	
	// Tanks
	
	@Override
	public @Nonnull List<Tank> getTanks() {
		if (getMaster() instanceof ITileFluid) return ((ITileFluid) getMaster()).getTanks();
		return super.getTanks();
	}
	
	// Fluid Connections
	
	@Override
	public @Nonnull FluidConnection[] getFluidConnections() {
		if (getMaster() instanceof ITileFluid) return ((ITileFluid) getMaster()).getFluidConnections();
		return super.getFluidConnections();
	}
	
	@Override
	public void setFluidConnections(@Nonnull FluidConnection[] connections) {
		if (getMaster() instanceof ITileFluid) ((ITileFluid) getMaster()).setFluidConnections(connections);
		super.setFluidConnections(connections);
	}
	
	// Fluid Wrappers
	
	@Override
	public @Nonnull FluidTileWrapper[] getFluidSides() {
		if (getMaster() instanceof ITileFluid) return ((ITileFluid) getMaster()).getFluidSides();
		return super.getFluidSides();
	}
	
	// Gas Wrapper
	
	@Override
	public @Nonnull GasTileWrapper getGasWrapper() {
		if (getMaster() instanceof ITileFluid) return ((ITileFluid) getMaster()).getGasWrapper();
		return super.getGasWrapper();
	}
	
	// Fluid Distribution
	
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
	
	public boolean hasMaster() {
		if (masterPosition == null) return false;
		return isMaster(masterPosition);
	}
	
	public boolean isMaster(BlockPos pos) {
		return tClass.isInstance(world.getTileEntity(pos));
	}
	
	public T getMaster() {
		if (hasMaster()) return (T) world.getTileEntity(masterPosition);
		return null;
	}
}
