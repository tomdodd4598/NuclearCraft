package nc.multiblock.fission.tile;

import static nc.block.property.BlockProperties.AXIS_ALL;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.collect.Lists;

import nc.Global;
import nc.ModCheck;
import nc.config.NCConfig;
import nc.multiblock.cuboidal.CuboidalPartPositionType;
import nc.multiblock.fission.FissionReactor;
import nc.multiblock.fission.salt.SaltFissionHeaterSetting;
import nc.multiblock.fission.salt.SaltFissionVesselSetting;
import nc.multiblock.fission.salt.tile.TileSaltFissionHeater;
import nc.multiblock.fission.salt.tile.TileSaltFissionVessel;
import nc.multiblock.fission.solid.tile.TileSolidFissionCell;
import nc.recipe.ProcessorRecipeHandler;
import nc.tile.fluid.ITileFluid;
import nc.tile.internal.fluid.FluidConnection;
import nc.tile.internal.fluid.FluidTileWrapper;
import nc.tile.internal.fluid.GasTileWrapper;
import nc.tile.internal.fluid.Tank;
import nc.tile.internal.fluid.TankOutputSetting;
import nc.tile.internal.fluid.TankSorption;
import nc.tile.internal.inventory.InventoryConnection;
import nc.tile.internal.inventory.InventoryTileWrapper;
import nc.tile.internal.inventory.ItemOutputSetting;
import nc.tile.internal.inventory.ItemSorption;
import nc.tile.inventory.ITileInventory;
import nc.util.BlockPosHelper;
import nc.util.FluidStackHelper;
import nc.util.GasHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;

public class TileFissionPort extends TileFissionPart implements ITileInventory, ITileFluid {
	
	private final @Nonnull String inventoryName = Global.MOD_ID + ".container.fission_port";
	
	private final @Nonnull NonNullList<ItemStack> inventoryStacks = NonNullList.<ItemStack>withSize(2, ItemStack.EMPTY);
	
	private @Nonnull InventoryConnection[] inventoryConnections = ITileInventory.inventoryConnectionAll(Arrays.asList(ItemSorption.IN, ItemSorption.OUT));
	
	private @Nonnull InventoryTileWrapper invWrapper;
	public int inventoryStackLimit = 64;
	public ProcessorRecipeHandler recipe_handler = null;
	
	private final @Nonnull List<Tank> tanks = Lists.newArrayList(new Tank(FluidStackHelper.INGOT_BLOCK_VOLUME, null), new Tank(FluidStackHelper.INGOT_BLOCK_VOLUME, null));
	
	private @Nonnull FluidConnection[] fluidConnections = ITileFluid.fluidConnectionAll(Arrays.asList(TankSorption.IN, TankSorption.OUT));
	
	private @Nonnull FluidTileWrapper[] fluidSides;
	
	private @Nonnull GasTileWrapper gasWrapper;
	
	protected BlockPos masterPortPos = BlockPosHelper.DEFAULT_NON;
	protected TileFissionPort masterPort = null;
	public boolean refreshCellsFlag = false;
	
	//protected int portCount;
	
	public TileFissionPort() {
		super(CuboidalPartPositionType.WALL);
		invWrapper = new InventoryTileWrapper(this);
		fluidSides = ITileFluid.getDefaultFluidSides(this);
		gasWrapper = new GasTileWrapper(this);
	}
	
	@Override
	public void onMachineAssembled(FissionReactor controller) {
		doStandardNullControllerResponse(controller);
		super.onMachineAssembled(controller);
		if (!getWorld().isRemote && getPartPosition().getFacing() != null) {
			getWorld().setBlockState(getPos(), getWorld().getBlockState(getPos()).withProperty(AXIS_ALL, getPartPosition().getFacing().getAxis()), 2);
		}
	}
	
	@Override
	public void onMachineBroken() {
		super.onMachineBroken();
		
		//TODO - temporary ports
		if (!getWorld().isRemote && masterPort != null) {
			TileFissionPort master = masterPort;
			clearMasterPort();
			master.shiftStacks(this);
		}
	}
	
	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
		return oldState.getBlock() != newState.getBlock();
	}
	
	public BlockPos getMasterPortPos() {
		return masterPortPos;
	}
	
	public void setMasterPortPos(BlockPos pos) {
		masterPortPos = pos;
	}
	
	public TileFissionPort getMasterPort() {
		return masterPort;
	}
	
	public void clearMasterPort() {
		masterPort = null;
		masterPortPos = BlockPosHelper.DEFAULT_NON;
	}
	
	public void refreshMasterPort() {
		if (getMultiblock() != null) {
			masterPort = getMultiblock().getPartMap(TileFissionPort.class).get(masterPortPos.toLong());
			if (masterPort == null) masterPortPos = BlockPosHelper.DEFAULT_NON;
		}
	}
	
	public void shiftStacks(TileFissionPort port) {
		NonNullList<ItemStack> portStacks = port.getInventoryStacks();
		if (getInventoryStacks() != portStacks) {
			for (int i = 0; i < getInventoryStacks().size(); i++) {
				if (portStacks.get(i).isEmpty()) {
					if (!getInventoryStacks().get(i).isEmpty()) {
						portStacks.set(i, getInventoryStacks().get(i).copy());
					}
					getInventoryStacks().set(i, ItemStack.EMPTY);
				}
				else if (portStacks.get(i).isItemEqual(getInventoryStacks().get(i))) {
					portStacks.get(i).grow(getInventoryStacks().get(i).getCount());
					getInventoryStacks().set(i, ItemStack.EMPTY);
				}
			}
		}
	}
	
	//TODO - temporary ports
	protected void refreshConnectedCells(/*boolean refreshRecipe, boolean refreshIsProcessing*/) {
		refreshCellsFlag = false;
		if (isMultiblockAssembled()) {
			boolean refresh = false;
			for (TileSolidFissionCell cell : getMultiblock().getPartMap(TileSolidFissionCell.class).values()) {
				if (cell.getPortPos() != null && (cell.getPortPos().equals(pos) || cell.getPortPos().equals(masterPortPos))) {
					/*if (refreshRecipe)*/ cell.refreshRecipe();
					cell.refreshActivity();
					/*if (refreshIsProcessing)*/ cell.refreshIsProcessing(cell.isFunctional());
					
					if (!refresh && (cell.isFunctional() ^ cell.readyToProcess(false))) {
						refresh = true;
					}
				}
			}
			if (refresh) getMultiblock().refreshFlag = true;
		}
	}
	
	@Override
	public void update() {
		super.update();
		if (!world.isRemote) {
			if (refreshCellsFlag) {
				refreshConnectedCells();
			}
			/*if (portCount == 0) {
				pushStacks();
				pushFluid();
			}
			tickPort();*/
		}
	}
	
	/*public void tickPort() {
		portCount++; portCount %= NCConfig.machine_update_rate / 2;
	}*/
	
	public ProcessorRecipeHandler getRecipeHandler() {
		return masterPort != null ? masterPort.getRecipeHandler() : recipe_handler;
	}
	
	// Inventory
	
	@Override
	public String getName() {
		return inventoryName;
	}
	
	@Override
	public @Nonnull NonNullList<ItemStack> getInventoryStacks() {
		return masterPort != null ? masterPort.getInventoryStacks() : inventoryStacks;
	}
	
	@Override
	public ItemStack decrStackSize(int slot, int amount) {
		ItemStack stack = ITileInventory.super.decrStackSize(slot, amount);
		if (!world.isRemote && getRecipeHandler() != null) {
			if (slot < getRecipeHandler().itemInputSize) {
				//refreshConnectedCells(true, false);
				refreshCellsFlag = true;
			}
			else if (slot < getRecipeHandler().itemInputSize + getRecipeHandler().itemOutputSize) {
				//refreshConnectedCells(false, false);
				refreshCellsFlag = true;
			}
		}
		return stack;
	}
	
	@Override
	public void setInventorySlotContents(int slot, ItemStack stack) {
		ITileInventory.super.setInventorySlotContents(slot, stack);
		if (!world.isRemote && getRecipeHandler() != null) {
			if (slot < getRecipeHandler().itemInputSize) {
				//refreshConnectedCells(true, false);
				refreshCellsFlag = true;
			}
			else if (slot < getRecipeHandler().itemInputSize + getRecipeHandler().itemOutputSize) {
				//refreshConnectedCells(false, false);
				refreshCellsFlag = true;
			}
		}
	}
	
	@Override
	public void markDirty() {
		//refreshConnectedCells(true, false);
		refreshCellsFlag = true;
		super.markDirty();
	}
	
	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		if (getRecipeHandler() == null) {
			return ITileInventory.super.isItemValidForSlot(slot, stack);
		}
		if (stack == ItemStack.EMPTY || slot >= getRecipeHandler().itemInputSize) return false;
		return NCConfig.smart_processor_input ? getRecipeHandler().isValidItemInput(stack, getInventoryStacks().get(slot), inputItemStacksExcludingSlot(slot)) : getRecipeHandler().isValidItemInput(stack);
	}
	
	public List<ItemStack> inputItemStacksExcludingSlot(int slot) {
		List<ItemStack> inputItemsExcludingSlot = new ArrayList<ItemStack>(getInventoryStacks().subList(0, getRecipeHandler().itemInputSize));
		inputItemsExcludingSlot.remove(slot);
		return inputItemsExcludingSlot;
	}
	
	@Override
	public boolean canInsertItem(int slot, ItemStack stack, EnumFacing side) {
		return ITileInventory.super.canInsertItem(slot, stack, side) && (getRecipeHandler() == null || isItemValidForSlot(slot, stack));
	}
	
	@Override
	public void clearAllSlots() {
		ITileInventory.super.clearAllSlots();
		//refreshConnectedCells(true, true);
		refreshCellsFlag = true;
	}
	
	@Override
	public @Nonnull InventoryConnection[] getInventoryConnections() {
		return inventoryConnections;
	}
	
	@Override
	public void setInventoryConnections(@Nonnull InventoryConnection[] connections) {
		inventoryConnections = connections;
	}
	
	@Override
	public @Nonnull InventoryTileWrapper getInventory() {
		return invWrapper;
	}
	
	@Override
	public int getInventoryStackLimit() {
		return masterPort != null ? masterPort.getInventoryStackLimit() : inventoryStackLimit;
	}
	
	@Override
	public ItemOutputSetting getItemOutputSetting(int slot) {
		return ItemOutputSetting.DEFAULT;
	}
	
	@Override
	public void setItemOutputSetting(int slot, ItemOutputSetting setting) {}
	
	// Fluids
	
	@Override
	@Nonnull
	public List<Tank> getTanks() {
		return tanks;
	}

	@Override
	@Nonnull
	public FluidConnection[] getFluidConnections() {
		return fluidConnections;
	}
	
	@Override
	public void setFluidConnections(@Nonnull FluidConnection[] connections) {
		fluidConnections = connections;
	}

	@Override
	@Nonnull
	public FluidTileWrapper[] getFluidSides() {
		return fluidSides;
	}
	
	@Override
	public @Nonnull GasTileWrapper getGasWrapper() {
		return gasWrapper;
	}
	
	@Override
	public void pushFluidToSide(@Nonnull EnumFacing side) {
		if (!getTankSorption(side, 0).canDrain()) return;
		
		TileEntity tile = getTileWorld().getTileEntity(getTilePos().offset(side));
		if (tile instanceof TileSaltFissionVessel) {
			TileSaltFissionVessel vessel = (TileSaltFissionVessel) tile;
			
			if (vessel.getVesselSetting(side.getOpposite()) == SaltFissionVesselSetting.DEFAULT) {
				getTanks().get(0).drain(vessel.getTanks().get(0).fill(getTanks().get(0).drain(getTanks().get(0).getCapacity(), false), true), true);
			}
		}
		else if (tile instanceof TileSaltFissionHeater) {
			TileSaltFissionHeater heater = (TileSaltFissionHeater) tile;
			
			if (heater.getHeaterSetting(side.getOpposite()) == SaltFissionHeaterSetting.DEFAULT) {
				getTanks().get(0).drain(heater.getTanks().get(0).fill(getTanks().get(0).drain(getTanks().get(0).getCapacity(), false), true), true);
			}
		}
	}

	@Override
	public boolean getInputTanksSeparated() {
		return false;
	}

	@Override
	public void setInputTanksSeparated(boolean separated) {}

	@Override
	public boolean getVoidUnusableFluidInput(int tankNumber) {
		return false;
	}

	@Override
	public void setVoidUnusableFluidInput(int tankNumber, boolean voidUnusableFluidInput) {}

	@Override
	public TankOutputSetting getTankOutputSetting(int tankNumber) {
		return TankOutputSetting.DEFAULT;
	}
	
	@Override
	public void setTankOutputSetting(int tankNumber, TankOutputSetting setting) {}
	
	// NBT
	
	@Override
	public NBTTagCompound writeAll(NBTTagCompound nbt) {
		super.writeAll(nbt);
		writeInventory(nbt);
		writeInventoryConnections(nbt);
		writeTanks(nbt);
		writeFluidConnections(nbt);
		
		nbt.setInteger("inventoryStackLimit", inventoryStackLimit);
		nbt.setLong("masterPortPos", masterPortPos.toLong());
		return nbt;
	}
	
	@Override
	public void readAll(NBTTagCompound nbt) {
		super.readAll(nbt);
		readInventory(nbt);
		readInventoryConnections(nbt);
		readTanks(nbt);
		readFluidConnections(nbt);
		
		inventoryStackLimit = nbt.getInteger("inventoryStackLimit");
		masterPortPos = BlockPos.fromLong(nbt.getLong("masterPortPos"));
	}
	
	@Override
	public NBTTagCompound writeInventory(NBTTagCompound nbt) {
		ItemStackHelper.saveAllItems(nbt, inventoryStacks);
		return nbt;
	}
	
	@Override
	public void readInventory(NBTTagCompound nbt) {
		ItemStackHelper.loadAllItems(nbt, inventoryStacks);
	}
	
	// Capability
	
	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing side) {
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return !getInventoryStacks().isEmpty() && hasInventorySideCapability(side);
		}
		else if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY || (ModCheck.mekanismLoaded() && NCConfig.enable_mek_gas && capability == GasHelper.GAS_HANDLER_CAPABILITY)) {
			return !getTanks().isEmpty() && hasFluidSideCapability(side);
		}
		return super.hasCapability(capability, side);
	}

	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing side) {
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			if (!getInventoryStacks().isEmpty() && hasInventorySideCapability(side)) {
				return (T) getItemHandlerCapability(side);
			}
			return null;
		}
		else if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
			if (!getTanks().isEmpty() && hasFluidSideCapability(side)) {
				return (T) getFluidSide(nonNullSide(side));
			}
			return null;
		}
		else if (ModCheck.mekanismLoaded() && capability == GasHelper.GAS_HANDLER_CAPABILITY) {
			if (NCConfig.enable_mek_gas && !getTanks().isEmpty() && hasFluidSideCapability(side)) {
				return (T) getGasWrapper();
			}
			return null;
		}
		return super.getCapability(capability, side);
	}
}
