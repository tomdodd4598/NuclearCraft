package nc.tile.dummy;

import java.util.List;

import javax.annotation.*;

import nc.tile.energy.ITileEnergy;
import nc.tile.energyFluid.TileEnergyFluidSidedInventory;
import nc.tile.fluid.ITileFluid;
import nc.tile.internal.energy.*;
import nc.tile.internal.fluid.*;
import nc.tile.internal.inventory.*;
import nc.tile.inventory.ITileInventory;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.*;

public abstract class TileDummy<MASTER extends IDummyMaster> extends TileEnergyFluidSidedInventory implements ITickable {
	
	public BlockPos masterPosition = null;
	protected final int updateRate;
	
	protected int checkCount;
	
	protected final Class<MASTER> tClass;
	
	public TileDummy(Class<MASTER> tClass, String name, int updateRate, List<String> allowedFluids) {
		this(tClass, name, ITileInventory.inventoryConnectionAll(ItemSorption.NON), ITileEnergy.energyConnectionAll(EnergyConnection.NON), updateRate, allowedFluids, ITileFluid.fluidConnectionAll(TankSorption.NON));
	}
	
	public TileDummy(Class<MASTER> tClass, String name, @Nonnull EnergyConnection[] energyConnections, int updateRate, List<String> allowedFluids) {
		this(tClass, name, ITileInventory.inventoryConnectionAll(ItemSorption.NON), energyConnections, updateRate, allowedFluids, ITileFluid.fluidConnectionAll(TankSorption.NON));
	}
	
	public TileDummy(Class<MASTER> tClass, String name, int updateRate, List<String> allowedFluids, @Nonnull FluidConnection[] fluidConnections) {
		this(tClass, name, ITileInventory.inventoryConnectionAll(ItemSorption.NON), ITileEnergy.energyConnectionAll(EnergyConnection.NON), updateRate, allowedFluids, fluidConnections);
	}
	
	public TileDummy(Class<MASTER> tClass, String name, @Nonnull EnergyConnection[] energyConnections, int updateRate, List<String> allowedFluids, @Nonnull FluidConnection[] fluidConnections) {
		this(tClass, name, ITileInventory.inventoryConnectionAll(ItemSorption.NON), energyConnections, updateRate, allowedFluids, fluidConnections);
	}
	
	public TileDummy(Class<MASTER> tClass, String name, @Nonnull InventoryConnection[] inventoryConnections, int updateRate, List<String> allowedFluids) {
		this(tClass, name, inventoryConnections, ITileEnergy.energyConnectionAll(EnergyConnection.NON), updateRate, allowedFluids, ITileFluid.fluidConnectionAll(TankSorption.NON));
	}
	
	public TileDummy(Class<MASTER> tClass, String name, @Nonnull InventoryConnection[] inventoryConnections, @Nonnull EnergyConnection[] energyConnections, int updateRate, List<String> allowedFluids) {
		this(tClass, name, inventoryConnections, energyConnections, updateRate, allowedFluids, ITileFluid.fluidConnectionAll(TankSorption.NON));
	}
	
	public TileDummy(Class<MASTER> tClass, String name, @Nonnull InventoryConnection[] inventoryConnections, int updateRate, List<String> allowedFluids, @Nonnull FluidConnection[] fluidConnections) {
		this(tClass, name, inventoryConnections, ITileEnergy.energyConnectionAll(EnergyConnection.NON), updateRate, allowedFluids, fluidConnections);
	}
	
	public TileDummy(Class<MASTER> tClass, String name, @Nonnull InventoryConnection[] inventoryConnections, @Nonnull EnergyConnection[] energyConnections, int updateRate, List<String> allowedFluids, @Nonnull FluidConnection[] fluidConnections) {
		super(name, 1, inventoryConnections, 1, energyConnections, 1, allowedFluids, fluidConnections);
		this.updateRate = updateRate;
		this.tClass = tClass;
	}
	
	@Override
	public void onLoad() {
		super.onLoad();
		if (!world.isRemote) {
			findMaster();
		}
	}
	
	@Override
	public void update() {
		if (!world.isRemote) {
			if (checkCount == 0) {
				findMaster();
			}
			tickDummy();
		}
	}
	
	public void tickDummy() {
		++checkCount;
		checkCount %= updateRate;
	}
	
	@Override
	public void onBlockNeighborChanged(IBlockState state, World worldIn, BlockPos posIn, BlockPos fromPos) {
		super.onBlockNeighborChanged(state, worldIn, posIn, fromPos);
		if (hasMaster()) {
			getMaster().onDummyNeighborChanged(state, worldIn, posIn, fromPos);
		}
	}
	
	// Inventory
	
	@Override
	public @Nonnull NonNullList<ItemStack> getInventoryStacks() {
		if (getMaster() instanceof ITileInventory) {
			return ((ITileInventory) getMaster()).getInventoryStacks();
		}
		return super.getInventoryStacks();
	}
	
	@Override
	public int getInventoryStackLimit() {
		if (getMaster() instanceof ITileInventory) {
			return ((ITileInventory) getMaster()).getInventoryStackLimit();
		}
		return 1;
	}
	
	@Override
	public boolean isUsableByPlayer(EntityPlayer player) {
		return false;
	}
	
	@Override
	public @Nonnull InventoryConnection[] getInventoryConnections() {
		if (getMaster() instanceof ITileInventory) {
			return ((ITileInventory) getMaster()).getInventoryConnections();
		}
		return super.getInventoryConnections();
	}
	
	@Override
	public void setInventoryConnections(@Nonnull InventoryConnection[] connections) {
		if (getMaster() instanceof ITileInventory) {
			((ITileInventory) getMaster()).setInventoryConnections(connections);
		}
		super.setInventoryConnections(connections);
	}
	
	@Override
	public ItemOutputSetting getItemOutputSetting(int tankNumber) {
		if (getMaster() instanceof ITileInventory) {
			return ((ITileInventory) getMaster()).getItemOutputSetting(tankNumber);
		}
		return super.getItemOutputSetting(tankNumber);
	}
	
	@Override
	public void setItemOutputSetting(int tankNumber, ItemOutputSetting setting) {
		if (getMaster() instanceof ITileInventory) {
			((ITileInventory) getMaster()).setItemOutputSetting(tankNumber, setting);
		}
		else {
			super.setItemOutputSetting(tankNumber, setting);
		}
	}
	
	@Override
	public IItemHandler getItemHandler(@Nullable EnumFacing side) {
		ITileInventory tile = getMaster() instanceof ITileInventory ? (ITileInventory) getMaster() : this;
		return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(new ItemHandler<>(tile, side));
	}
	
	// ITileEnergy
	
	@Override
	public EnergyStorage getEnergyStorage() {
		if (getMaster() instanceof ITileEnergy) {
			return ((ITileEnergy) getMaster()).getEnergyStorage();
		}
		return super.getEnergyStorage();
	}
	
	@Override
	public EnergyConnection getEnergyConnection(@Nonnull EnumFacing side) {
		if (getMaster() instanceof ITileEnergy) {
			return ((ITileEnergy) getMaster()).getEnergyConnection(side);
		}
		return super.getEnergyConnection(side);
	}
	
	@Override
	public int getEnergyStored() {
		if (getMaster() instanceof ITileEnergy) {
			return ((ITileEnergy) getMaster()).getEnergyStored();
		}
		return super.getEnergyStored();
	}
	
	@Override
	public int getMaxEnergyStored() {
		if (getMaster() instanceof ITileEnergy) {
			return ((ITileEnergy) getMaster()).getMaxEnergyStored();
		}
		return super.getMaxEnergyStored();
	}
	
	@Override
	public boolean canReceiveEnergy(EnumFacing side) {
		if (getMaster() instanceof ITileEnergy) {
			return ((ITileEnergy) getMaster()).canReceiveEnergy(side);
		}
		return false;
	}
	
	@Override
	public boolean canExtractEnergy(EnumFacing side) {
		if (getMaster() instanceof ITileEnergy) {
			return ((ITileEnergy) getMaster()).canExtractEnergy(side);
		}
		return false;
	}
	
	@Override
	public int receiveEnergy(int maxReceive, EnumFacing side, boolean simulate) {
		if (getMaster() instanceof ITileEnergy) {
			return ((ITileEnergy) getMaster()).receiveEnergy(maxReceive, side, simulate);
		}
		return 0;
	}
	
	@Override
	public int extractEnergy(int maxExtract, EnumFacing side, boolean simulate) {
		if (getMaster() instanceof ITileEnergy) {
			return ((ITileEnergy) getMaster()).extractEnergy(maxExtract, side, simulate);
		}
		return 0;
	}
	
	// IC2 Energy
	
	@Override
	public int getSinkTier() {
		if (getMaster() instanceof ITileEnergy) {
			return ((ITileEnergy) getMaster()).getSinkTier();
		}
		return 10;
	}
	
	@Override
	public int getSourceTier() {
		if (getMaster() instanceof ITileEnergy) {
			return ((ITileEnergy) getMaster()).getSourceTier();
		}
		return 1;
	}
	
	// Energy Distribution
	
	@Override
	public void pushEnergy() {
		if (getMaster() == null) {
			return;
		}
		super.pushEnergy();
	}
	
	// Fluids
	
	// Tanks
	
	@Override
	public @Nonnull List<Tank> getTanks() {
		if (getMaster() instanceof ITileFluid) {
			return ((ITileFluid) getMaster()).getTanks();
		}
		return super.getTanks();
	}
	
	// Fluid Connections
	
	@Override
	public @Nonnull FluidConnection[] getFluidConnections() {
		if (getMaster() instanceof ITileFluid) {
			return ((ITileFluid) getMaster()).getFluidConnections();
		}
		return super.getFluidConnections();
	}
	
	@Override
	public @Nonnull FluidTileWrapper[] getFluidSides() {
		if (getMaster() instanceof ITileFluid) {
			return ((ITileFluid) getMaster()).getFluidSides();
		}
		return super.getFluidSides();
	}
	
	@Override
	public @Nonnull GasTileWrapper getGasWrapper() {
		if (getMaster() instanceof ITileFluid) {
			return ((ITileFluid) getMaster()).getGasWrapper();
		}
		return super.getGasWrapper();
	}
	
	@Override
	public boolean getInputTanksSeparated() {
		if (getMaster() instanceof ITileFluid) {
			return ((ITileFluid) getMaster()).getInputTanksSeparated();
		}
		return super.getInputTanksSeparated();
	}
	
	@Override
	public void setInputTanksSeparated(boolean shared) {
		if (getMaster() instanceof ITileFluid) {
			((ITileFluid) getMaster()).setInputTanksSeparated(shared);
		}
		else {
			super.setInputTanksSeparated(shared);
		}
	}
	
	@Override
	public boolean getVoidUnusableFluidInput(int tankNumber) {
		if (getMaster() instanceof ITileFluid) {
			return ((ITileFluid) getMaster()).getVoidUnusableFluidInput(tankNumber);
		}
		return super.getVoidUnusableFluidInput(tankNumber);
	}
	
	@Override
	public void setVoidUnusableFluidInput(int tankNumber, boolean voidUnusableFluidInput) {
		if (getMaster() instanceof ITileFluid) {
			((ITileFluid) getMaster()).setVoidUnusableFluidInput(tankNumber, voidUnusableFluidInput);
		}
		else {
			super.setVoidUnusableFluidInput(tankNumber, voidUnusableFluidInput);
		}
	}
	
	@Override
	public TankOutputSetting getTankOutputSetting(int tankNumber) {
		if (getMaster() instanceof ITileFluid) {
			return ((ITileFluid) getMaster()).getTankOutputSetting(tankNumber);
		}
		return super.getTankOutputSetting(tankNumber);
	}
	
	@Override
	public void setTankOutputSetting(int tankNumber, TankOutputSetting setting) {
		if (getMaster() instanceof ITileFluid) {
			((ITileFluid) getMaster()).setTankOutputSetting(tankNumber, setting);
		}
		else {
			super.setTankOutputSetting(tankNumber, setting);
		}
	}
	
	// Fluid Distribution
	
	@Override
	public void pushFluid() {
		if (getMaster() == null) {
			return;
		}
		super.pushFluid();
	}
	
	// Find Master
	
	/** Find the BlockPos of the master tile entity */
	public abstract void findMaster();
	
	public boolean hasMaster() {
		if (masterPosition == null) {
			return false;
		}
		return isMaster(masterPosition);
	}
	
	public boolean isMaster(BlockPos posIn) {
		return tClass.isInstance(world.getTileEntity(posIn));
	}
	
	public MASTER getMaster() {
		if (hasMaster()) {
			return tClass.cast(world.getTileEntity(masterPosition));
		}
		return null;
	}
	
	// NBT
	
	@Override
	public NBTTagCompound writeInventory(NBTTagCompound nbt) {
		ItemStackHelper.saveAllItems(nbt, super.getInventoryStacks());
		return nbt;
	}
	
	@Override
	public void readInventory(NBTTagCompound nbt) {
		ItemStackHelper.loadAllItems(nbt, super.getInventoryStacks());
	}
	
	@Override
	public NBTTagCompound writeInventoryConnections(NBTTagCompound nbt) {
		for (EnumFacing side : EnumFacing.VALUES) {
			super.getInventoryConnections()[side.getIndex()].writeToNBT(nbt, side);
		}
		return nbt;
	}
	
	@Override
	public void readInventoryConnections(NBTTagCompound nbt) {
		if (!super.hasConfigurableInventoryConnections()) {
			return;
		}
		for (EnumFacing side : EnumFacing.VALUES) {
			super.getInventoryConnections()[side.getIndex()].readFromNBT(nbt, side);
		}
	}
	
	@Override
	public NBTTagCompound writeSlotSettings(NBTTagCompound nbt) {
		for (int i = 0; i < super.getInventoryStacks().size(); ++i) {
			nbt.setInteger("itemOutputSetting" + i, super.getItemOutputSetting(i).ordinal());
		}
		return nbt;
	}
	
	@Override
	public void readSlotSettings(NBTTagCompound nbt) {
		for (int i = 0; i < super.getInventoryStacks().size(); ++i) {
			super.setItemOutputSetting(i, ItemOutputSetting.values()[nbt.getInteger("itemOutputSetting" + i)]);
		}
	}
	
	@Override
	public NBTTagCompound writeTanks(NBTTagCompound nbt) {
		for (int i = 0; i < super.getTanks().size(); ++i) {
			super.getTanks().get(i).writeToNBT(nbt, "tanks" + i);
		}
		return nbt;
	}
	
	@Override
	public void readTanks(NBTTagCompound nbt) {
		for (int i = 0; i < super.getTanks().size(); ++i) {
			super.getTanks().get(i).readFromNBT(nbt, "tanks" + i);
		}
	}
	
	@Override
	public NBTTagCompound writeFluidConnections(NBTTagCompound nbt) {
		for (EnumFacing side : EnumFacing.VALUES) {
			super.getFluidConnections()[side.getIndex()].writeToNBT(nbt, side);
		}
		return nbt;
	}
	
	@Override
	public void readFluidConnections(NBTTagCompound nbt) {
		if (!super.hasConfigurableFluidConnections()) {
			return;
		}
		for (EnumFacing side : EnumFacing.VALUES) {
			super.getFluidConnections()[side.getIndex()].readFromNBT(nbt, side);
		}
	}
	
	@Override
	public NBTTagCompound writeTankSettings(NBTTagCompound nbt) {
		nbt.setBoolean("inputTanksSeparated", super.getInputTanksSeparated());
		for (int i = 0; i < super.getTanks().size(); ++i) {
			nbt.setBoolean("voidUnusableFluidInput" + i, super.getVoidUnusableFluidInput(i));
			nbt.setInteger("tankOutputSetting" + i, super.getTankOutputSetting(i).ordinal());
		}
		return nbt;
	}
	
	@Override
	public void readTankSettings(NBTTagCompound nbt) {
		super.setInputTanksSeparated(nbt.getBoolean("inputTanksSeparated"));
		for (int i = 0; i < super.getTanks().size(); ++i) {
			super.setVoidUnusableFluidInput(i, nbt.getBoolean("voidUnusableFluidInput" + i));
			int ordinal = nbt.hasKey("voidExcessFluidOutput" + i) ? nbt.getBoolean("voidExcessFluidOutput" + i) ? 1 : 0 : nbt.getInteger("tankOutputSetting" + i);
			super.setTankOutputSetting(i, TankOutputSetting.values()[ordinal]);
		}
	}
	
	@Override
	public NBTTagCompound writeEnergy(NBTTagCompound nbt) {
		super.getEnergyStorage().writeToNBT(nbt, "energyStorage");
		return nbt;
	}
	
	@Override
	public void readEnergy(NBTTagCompound nbt) {
		super.getEnergyStorage().readFromNBT(nbt, "energyStorage");
	}
	
	@Override
	public NBTTagCompound writeEnergyConnections(NBTTagCompound nbt) {
		for (int i = 0; i < 6; ++i) {
			nbt.setInteger("energyConnections" + i, super.getEnergyConnections()[i].ordinal());
		}
		return nbt;
	}
	
	@Override
	public void readEnergyConnections(NBTTagCompound nbt) {
		if (super.hasConfigurableEnergyConnections()) {
			for (int i = 0; i < 6; ++i) {
				if (nbt.hasKey("energyConnections" + i)) {
					super.getEnergyConnections()[i] = EnergyConnection.values()[nbt.getInteger("energyConnections" + i)];
				}
			}
		}
	}
}
