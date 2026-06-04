package nc.tile.machine;

import nc.*;
import nc.multiblock.cuboidal.CuboidalPartPositionType;
import nc.multiblock.machine.*;
import nc.tile.fluid.ITileFluid;
import nc.tile.internal.fluid.*;
import nc.tile.internal.inventory.*;
import nc.tile.inventory.ITileInventory;
import nc.tile.passive.ITilePassive;
import nc.util.*;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.text.*;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.*;
import net.minecraftforge.items.*;

import javax.annotation.*;
import java.util.*;

import static nc.block.property.BlockProperties.FACING_ALL;
import static nc.config.NCConfig.enable_mek_gas;

public class TileMachineReservoirPort extends TileMachinePart implements ITickable, ITileInventory, ITileFluid {
	
	private final @Nonnull String inventoryName;
	
	public final @Nonnull NonNullList<ItemStack> backupStacks = InventoryStackList.EMPTY_LIST;
	public final @Nonnull List<Tank> backupTanks = Collections.emptyList();
	
	public @Nonnull InventoryConnection[] backupInventoryConnections = ITileInventory.inventoryConnectionAll(ItemSorption.IN);
	public @Nonnull FluidConnection[] backupFluidConnections = ITileFluid.fluidConnectionAll(TankSorption.IN);
	
	private final @Nonnull FluidTileWrapper[] fluidSides;
	private final @Nonnull GasTileWrapper gasWrapper;
	
	public TileMachineReservoirPort() {
		super(CuboidalPartPositionType.WALL);
		inventoryName = Global.MOD_ID + ".container.machine_reservoir_port";
		fluidSides = ITileFluid.getDefaultFluidSides(this);
		gasWrapper = new GasTileWrapper(this);
	}
	
	@Override
	public void onMachineAssembled(Machine multiblock) {
		doStandardNullControllerResponse(multiblock);
		super.onMachineAssembled(multiblock);
		if (!world.isRemote) {
			EnumFacing facing = getPartPosition().getFacing();
			if (facing != null) {
				world.setBlockState(pos, world.getBlockState(pos).withProperty(FACING_ALL, facing), 2);
			}
		}
	}
	
	@Override
	public void update() {
		if (!world.isRemote) {
			MachineLogic logic = getLogic();
			EnumFacing facing = getPartPosition().getFacing();
			if (logic != null && facing != null) {
				logic.pushReservoirPortItemToSide(this, facing);
				logic.pushReservoirPortFluidToSide(this, facing);
			}
		}
	}
	
	// Inventory
	
	@Override
	public String getName() {
		return inventoryName;
	}
	
	@Override
	public ItemStack getStackInSlot(int slot) {
		MachineLogic logic = getLogic();
		return logic != null ? logic.getReservoirPortStackInSlot(this, slot) : ITileInventory.super.getStackInSlot(slot);
	}
	
	@Override
	public @Nonnull NonNullList<ItemStack> getInventoryStacks() {
		MachineLogic logic = getLogic();
		return logic != null ? logic.getReservoirPortInventoryStacks(this) : backupStacks;
	}
	
	@Override
	public @Nonnull InventoryConnection[] getInventoryConnections() {
		MachineLogic logic = getLogic();
		return logic != null ? logic.getReservoirPortInventoryConnections(this) : backupInventoryConnections;
	}
	
	@Override
	public void setInventoryConnections(@Nonnull InventoryConnection[] connections) {
		backupInventoryConnections = connections;
	}
	
	@Override
	public ItemOutputSetting getItemOutputSetting(int slot) {
		return ItemOutputSetting.DEFAULT;
	}
	
	@Override
	public void setItemOutputSetting(int slot, ItemOutputSetting setting) {}
	
	@Override
	public ItemStack decrStackSize(int slot, int amount) {
		MachineLogic logic = getLogic();
		return logic != null ? logic.decrReservoirPortStackSize(this, slot, amount) : ITileInventory.super.decrStackSize(slot, amount);
	}
	
	@Override
	public ItemStack removeStackFromSlot(int slot) {
		MachineLogic logic = getLogic();
		return logic != null ? logic.removeReservoirPortStackFromSlot(this, slot) : ITileInventory.super.removeStackFromSlot(slot);
	}
	
	@Override
	public void setInventorySlotContents(int slot, ItemStack stack) {
		MachineLogic logic = getLogic();
		if (logic != null) {
			logic.setReservoirPortInventorySlotContents(this, slot, stack);
		}
		else {
			ITileInventory.super.setInventorySlotContents(slot, stack);
		}
	}
	
	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		MachineLogic logic = getLogic();
		return logic != null && logic.isReservoirPortItemValid(this, slot, stack);
	}
	
	@Override
	public boolean canInsertItem(int slot, ItemStack stack, EnumFacing side) {
		return ITileInventory.super.canInsertItem(slot, stack, side) && isItemValidForSlot(slot, stack);
	}
	
	@Override
	public boolean hasConfigurableInventoryConnections() {
		return true;
	}
	
	@Override
	public void pushStacksToSide(@Nonnull EnumFacing side) {
		TileEntity tile = getTileWorld().getTileEntity(getTilePos().offset(side));
		if (tile == null) {
			return;
		}
		
		if (tile instanceof ITilePassive tilePassive && !tilePassive.canPushItemsTo()) {
			return;
		}
		
		IItemHandler adjInv = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, side.getOpposite());
		if (adjInv == null || adjInv.getSlots() < 1) {
			return;
		}
		
		boolean pushed = false;
		NonNullList<ItemStack> stacks = getInventoryStacks();
		for (int i = 0; i < stacks.size(); ++i) {
			pushed |= pushSlotToHandler(adjInv, stacks, side, i);
		}
		
		if (pushed) {
			refreshMachineActivity();
		}
	}
	
	@Override
	public void clearAllSlots() {
		MachineLogic logic = getLogic();
		if (logic != null) {
			logic.clearReservoirPortInventory(this);
		}
		else {
			ITileInventory.super.clearAllSlots();
		}
	}
	
	// Fluids
	
	@Override
	public @Nonnull List<Tank> getTanks() {
		MachineLogic logic = getLogic();
		return logic != null ? logic.getReservoirPortTanks(this) : backupTanks;
	}
	
	@Override
	@Nonnull
	public FluidConnection[] getFluidConnections() {
		MachineLogic logic = getLogic();
		return logic != null ? logic.getReservoirPortFluidConnections(this) : backupFluidConnections;
	}
	
	@Override
	public void setFluidConnections(@Nonnull FluidConnection[] connections) {
		backupFluidConnections = connections;
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
		TileEntity tile = getTileWorld().getTileEntity(getTilePos().offset(side));
		if (tile == null) {
			return;
		}
		
		if (tile instanceof ITilePassive tilePassive && !tilePassive.canPushFluidsTo()) {
			return;
		}
		
		IFluidHandler adjStorage = tile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side.getOpposite());
		if (adjStorage == null) {
			return;
		}
		
		List<Tank> tanks = getTanks();
		if (!tanks.isEmpty()) {
			Tank tank = tanks.get(0);
			onWrapperDrain(tank.drain(adjStorage.fill(tank.drain(tank.getCapacity(), false), true), true), true);
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
	
	@Override
	public boolean hasConfigurableFluidConnections() {
		return true;
	}
	
	@Override
	public boolean isFluidValidForTank(int tankNumber, FluidStack stack) {
		MachineLogic logic = getLogic();
		return logic != null && logic.isReservoirPortFluidValid(this, tankNumber, stack);
	}
	
	@Override
	public void clearAllTanks() {
		ITileFluid.super.clearAllTanks();
		
		MachineLogic logic = getLogic();
		if (logic != null) {
			logic.onReservoirPortTanksCleared(this);
		}
	}
	
	// IMultitoolLogic
	
	@Override
	public boolean onUseMultitool(ItemStack multitool, EntityPlayerMP player, World worldIn, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (player.isSneaking()) {
		
		}
		else {
			if (getMultiblock() != null) {
				if (getItemSorption(facing, 0) != ItemSorption.IN || getTankSorption(facing, 0) != TankSorption.IN) {
					for (EnumFacing side : EnumFacing.VALUES) {
						setItemSorption(side, 0, ItemSorption.IN);
						setTankSorption(side, 0, TankSorption.IN);
					}
					setActivity(false);
					player.sendMessage(new TextComponentString(Lang.localize("nc.block.port_toggle") + " " + TextFormatting.DARK_AQUA + Lang.localize("nc.block.port_mode.input") + " " + TextFormatting.WHITE + Lang.localize("nc.block.port_toggle.mode")));
				}
				else {
					for (EnumFacing side : EnumFacing.VALUES) {
						setItemSorption(side, 0, ItemSorption.OUT);
						setTankSorption(side, 0, TankSorption.OUT);
					}
					setActivity(true);
					player.sendMessage(new TextComponentString(Lang.localize("nc.block.port_toggle") + " " + TextFormatting.RED + Lang.localize("nc.block.port_mode.output") + " " + TextFormatting.WHITE + Lang.localize("nc.block.port_toggle.mode")));
				}
				markDirtyAndNotify(true);
				return true;
			}
		}
		return super.onUseMultitool(multitool, player, worldIn, facing, hitX, hitY, hitZ);
	}
	
	// NBT
	
	@Override
	public NBTTagCompound writeAll(NBTTagCompound nbt) {
		super.writeAll(nbt);
		writeInventoryConnections(nbt);
		writeFluidConnections(nbt);
		return nbt;
	}
	
	@Override
	public void readAll(NBTTagCompound nbt) {
		super.readAll(nbt);
		readInventoryConnections(nbt);
		readFluidConnections(nbt);
	}
	
	// Capability
	
	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing side) {
		MachineLogic logic = getLogic();
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return logic != null ? logic.hasReservoirPortItemCapability(this, side) : !getInventoryStacks().isEmpty() && hasInventorySideCapability(side);
		}
		else if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY || (ModCheck.mekanismLoaded() && enable_mek_gas && capability == CapabilityHelper.GAS_HANDLER_CAPABILITY)) {
			return logic != null ? logic.hasReservoirPortFluidCapability(this, side) : hasFluidSideCapability(side);
		}
		return super.hasCapability(capability, side);
	}
	
	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing side) {
		MachineLogic logic = getLogic();
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			if (logic != null) {
				if (logic.hasReservoirPortItemCapability(this, side)) {
					return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(logic.getReservoirPortItemHandler(this, side));
				}
			}
			else if (!getInventoryStacks().isEmpty() && hasInventorySideCapability(side)) {
				return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(getItemHandler(side));
			}
			return null;
		}
		else if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
			if (logic != null) {
				if (logic.hasReservoirPortFluidCapability(this, side)) {
					return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(logic.getReservoirPortFluidHandler(this, nonNullSide(side)));
				}
			}
			else if (hasFluidSideCapability(side)) {
				return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(getFluidSide(nonNullSide(side)));
			}
			return null;
		}
		else if (ModCheck.mekanismLoaded() && capability == CapabilityHelper.GAS_HANDLER_CAPABILITY) {
			if (enable_mek_gas) {
				if (logic != null) {
					if (logic.hasReservoirPortFluidCapability(this, side)) {
						return CapabilityHelper.GAS_HANDLER_CAPABILITY.cast(getGasWrapper());
					}
				}
				else if (hasFluidSideCapability(side)) {
					return CapabilityHelper.GAS_HANDLER_CAPABILITY.cast(getGasWrapper());
				}
			}
			return null;
		}
		return super.getCapability(capability, side);
	}
}
