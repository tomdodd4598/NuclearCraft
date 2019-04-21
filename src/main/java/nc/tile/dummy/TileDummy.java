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
import nc.tile.internal.inventory.InventoryConnection;
import nc.tile.internal.inventory.ItemSorption;
import nc.tile.inventory.ITileInventory;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class TileDummy<T extends IDummyMaster> extends TileEnergyFluidSidedInventory {
	
	public BlockPos masterPosition = null;
	protected final int updateRate;
	
	protected int checkCount;
	
	protected final Class<T> tClass;
	
	public TileDummy(Class<T> tClass, String name, int updateRate, List<String> allowedFluids) {
		this(tClass, name, ITileInventory.inventoryConnectionAll(ItemSorption.NON), ITileEnergy.energyConnectionAll(EnergyConnection.NON), updateRate, allowedFluids, ITileFluid.fluidConnectionAll(TankSorption.NON));
	}
	
	public TileDummy(Class<T> tClass, String name, @Nonnull EnergyConnection[] energyConnections, int updateRate, List<String> allowedFluids) {
		this(tClass, name, ITileInventory.inventoryConnectionAll(ItemSorption.NON), energyConnections, updateRate, allowedFluids, ITileFluid.fluidConnectionAll(TankSorption.NON));
	}
	
	public TileDummy(Class<T> tClass, String name, int updateRate, List<String> allowedFluids, @Nonnull FluidConnection[] fluidConnections) {
		this(tClass, name, ITileInventory.inventoryConnectionAll(ItemSorption.NON), ITileEnergy.energyConnectionAll(EnergyConnection.NON), updateRate, allowedFluids, fluidConnections);
	}
	
	public TileDummy(Class<T> tClass, String name, @Nonnull EnergyConnection[] energyConnections, int updateRate, List<String> allowedFluids, @Nonnull FluidConnection[] fluidConnections) {
		this(tClass, name, ITileInventory.inventoryConnectionAll(ItemSorption.NON), energyConnections, updateRate, allowedFluids, fluidConnections);
	}
	
	public TileDummy(Class<T> tClass, String name, @Nonnull InventoryConnection[] inventoryConnections, int updateRate, List<String> allowedFluids) {
		this(tClass, name, inventoryConnections, ITileEnergy.energyConnectionAll(EnergyConnection.NON), updateRate, allowedFluids, ITileFluid.fluidConnectionAll(TankSorption.NON));
	}
	
	public TileDummy(Class<T> tClass, String name, @Nonnull InventoryConnection[] inventoryConnections, @Nonnull EnergyConnection[] energyConnections, int updateRate, List<String> allowedFluids) {
		this(tClass, name, inventoryConnections, energyConnections, updateRate, allowedFluids, ITileFluid.fluidConnectionAll(TankSorption.NON));
	}
	
	public TileDummy(Class<T> tClass, String name, @Nonnull InventoryConnection[] inventoryConnections, int updateRate, List<String> allowedFluids, @Nonnull FluidConnection[] fluidConnections) {
		this(tClass, name, inventoryConnections, ITileEnergy.energyConnectionAll(EnergyConnection.NON), updateRate, allowedFluids, fluidConnections);
	}
	
	public TileDummy(Class<T> tClass, String name, @Nonnull InventoryConnection[] inventoryConnections, @Nonnull EnergyConnection[] energyConnections, int updateRate, List<String> allowedFluids, @Nonnull FluidConnection[] fluidConnections) {
		super(name, 1, inventoryConnections, 1, energyConnections, 1, allowedFluids, fluidConnections);
		this.updateRate = updateRate;
		this.tClass = tClass;
	}
	
	@Override
	public void onAdded() {
		super.onAdded();
		if (!world.isRemote) findMaster();
	}
	
	@Override
	public void update() {
		super.update();
		if(!world.isRemote) {
			if (checkCount == 0) findMaster();
			tickDummy();
		}
	}
	
	public void tickDummy() {
		checkCount++; checkCount %= updateRate;
	}
	
	@Override
	public void onBlockNeighborChanged(IBlockState state, World world, BlockPos pos, BlockPos fromPos) {
		super.onBlockNeighborChanged(state, world, pos, fromPos);
		if (hasMaster()) getMaster().onDummyNeighborChanged(state, world, pos, fromPos);
	}
	
	// Inventory
	
	@Override
	public @Nonnull NonNullList<ItemStack> getInventoryStacks() {
		if (getMaster() instanceof ITileInventory) return ((ITileInventory) getMaster()).getInventoryStacks();
		return super.getInventoryStacks();
	}
	
	@Override
	public boolean isEmpty() {
		if (getMaster() instanceof ITileInventory) ((ITileInventory) getMaster()).isEmpty();
		return super.isEmpty();
	}
	
	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		if (getMaster() instanceof ITileInventory) {
			((ITileInventory) getMaster()).setInventorySlotContents(index, stack);
			return;
		}
		else {
			super.setInventorySlotContents(index, stack);
		}
	}
	
	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		if (getMaster() instanceof ITileInventory) return ((ITileInventory) getMaster()).isItemValidForSlot(slot, stack);
		return false;
	}
	
	@Override
	public int getInventoryStackLimit() {
		if (getMaster() instanceof ITileInventory) return ((ITileInventory) getMaster()).getInventoryStackLimit();
		return 1;
	}
	
	@Override
	public boolean isUsableByPlayer(EntityPlayer player) {
		return false;
	}
	
	@Override
	public void openInventory(EntityPlayer player) {
		
	}
	
	@Override
	public void closeInventory(EntityPlayer player) {
		
	}
	
	@Override
	public int[] getSlotsForFace(EnumFacing side) {
		if (getMaster() instanceof ITileInventory) return ((ITileInventory) getMaster()).getSlotsForFace(side);
		return new int[] {0};
	}
	
	@Override
	public boolean canInsertItem(int slot, ItemStack stack, EnumFacing direction) {
		if (getMaster() instanceof ITileInventory) return ((ITileInventory) getMaster()).canInsertItem(slot, stack, direction);
		return false;
	}
	
	@Override
	public boolean canExtractItem(int slot, ItemStack stack, EnumFacing direction) {
		if (getMaster() instanceof ITileInventory) return ((ITileInventory) getMaster()).canExtractItem(slot, stack, direction);
		return false;
	}
	
	// ITileEnergy
	
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
		return 10;
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
	
	@Override
	public @Nonnull FluidTileWrapper[] getFluidSides() {
		if (getMaster() instanceof ITileFluid) return ((ITileFluid) getMaster()).getFluidSides();
		return super.getFluidSides();
	}
	
	@Override
	public @Nonnull GasTileWrapper getGasWrapper() {
		if (getMaster() instanceof ITileFluid) return ((ITileFluid) getMaster()).getGasWrapper();
		return super.getGasWrapper();
	}
	
	@Override
	public boolean getInputTanksSeparated() {
		if (getMaster() instanceof ITileFluid) return ((ITileFluid) getMaster()).getInputTanksSeparated();
		return super.getInputTanksSeparated();
	}
	
	@Override
	public void setInputTanksSeparated(boolean shared) {
		if (getMaster() instanceof ITileFluid) ((ITileFluid) getMaster()).setInputTanksSeparated(shared);
		else super.setInputTanksSeparated(shared);
	}
	
	@Override
	public boolean getVoidUnusableFluidInput(int tankNumber) {
		if (getMaster() instanceof ITileFluid) return ((ITileFluid) getMaster()).getVoidUnusableFluidInput(tankNumber);
		return super.getVoidUnusableFluidInput(tankNumber);
	}
	
	@Override
	public void setVoidUnusableFluidInput(int tankNumber, boolean voidUnusableFluidInput) {
		if (getMaster() instanceof ITileFluid) ((ITileFluid) getMaster()).setVoidUnusableFluidInput(tankNumber, voidUnusableFluidInput);
		else super.setVoidUnusableFluidInput(tankNumber, voidUnusableFluidInput);
	}
	
	@Override
	public boolean getVoidExcessFluidOutput(int tankNumber) {
		if (getMaster() instanceof ITileFluid) return ((ITileFluid) getMaster()).getVoidExcessFluidOutput(tankNumber);
		return super.getVoidExcessFluidOutput(tankNumber);
	}
	
	@Override
	public void setVoidExcessFluidOutput(int tankNumber, boolean voidExcessFluidOutput) {
		if (getMaster() instanceof ITileFluid) ((ITileFluid) getMaster()).setVoidExcessFluidOutput(tankNumber, voidExcessFluidOutput);
		else super.setVoidExcessFluidOutput(tankNumber, voidExcessFluidOutput);
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
