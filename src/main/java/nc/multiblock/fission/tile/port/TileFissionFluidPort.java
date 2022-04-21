package nc.multiblock.fission.tile.port;

import static nc.config.NCConfig.enable_mek_gas;
import static nc.util.PosHelper.DEFAULT_NON;

import java.util.*;

import javax.annotation.*;

import com.google.common.collect.Lists;

import nc.ModCheck;
import nc.recipe.BasicRecipeHandler;
import nc.tile.fluid.*;
import nc.tile.internal.fluid.*;
import nc.util.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.text.*;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

public abstract class TileFissionFluidPort<PORT extends TileFissionFluidPort<PORT, TARGET> & ITileFilteredFluid, TARGET extends IFissionPortTarget<PORT, TARGET> & ITileFilteredFluid> extends TileFissionPort<PORT, TARGET> implements ITileFilteredFluid {
	
	protected final @Nonnull List<Tank> tanks;
	protected final @Nonnull List<Tank> filterTanks;
	protected final int capacity;
	
	protected @Nonnull FluidConnection[] fluidConnections = ITileFluid.fluidConnectionAll(Lists.newArrayList(TankSorption.IN, TankSorption.OUT));
	
	protected @Nonnull FluidTileWrapper[] fluidSides;
	
	protected @Nonnull GasTileWrapper gasWrapper;
	
	protected final BasicRecipeHandler recipeHandler;
	
	public TileFissionFluidPort(Class<PORT> portClass, int capacity, List<String> validFluids, BasicRecipeHandler recipeHandler) {
		super(portClass);
		tanks = Lists.newArrayList(new Tank(capacity, validFluids), new Tank(capacity, new ArrayList<>()));
		filterTanks = Lists.newArrayList(new Tank(1000, validFluids), new Tank(1000, new ArrayList<>()));
		this.capacity = capacity;
		fluidSides = ITileFluid.getDefaultFluidSides(this);
		gasWrapper = new GasTileWrapper(this);
		this.recipeHandler = recipeHandler;
	}
	
	@Override
	public void update() {
		super.update();
		EnumFacing facing = getPartPosition().getFacing();
		if (!world.isRemote && facing != null && !getTanks().get(1).isEmpty() && getTankSorption(facing, 1).canDrain()) {
			pushFluidToSide(facing);
		}
	}
	
	@Override
	public void setInventoryStackLimit(int stackLimit) {}
	
	@Override
	public int getTankBaseCapacity() {
		return capacity;
	}
	
	@Override
	public void setTankCapacity(int capacity) {
		tanks.get(0).setCapacity(capacity);
		tanks.get(1).setCapacity(capacity);
	}
	
	@Override
	public boolean canModifyFilter(int tank) {
		return getMultiblock() != null ? !getMultiblock().isAssembled() : true;
	}
	
	@Override
	public void onFilterChanged(int tank) {
		markDirty();
	}
	
	// Fluids
	
	@Override
	public @Nonnull List<Tank> getTanks() {
		return !DEFAULT_NON.equals(masterPortPos) ? masterPort.getTanks() : tanks;
	}
	
	@Override
	public @Nonnull List<Tank> getTanksInternal() {
		return tanks;
	}
	
	@Override
	public @Nonnull List<Tank> getFilterTanks() {
		return !DEFAULT_NON.equals(masterPortPos) ? masterPort.getFilterTanks() : filterTanks;
	}
	
	@Override
	public @Nonnull FluidConnection[] getFluidConnections() {
		return fluidConnections;
	}
	
	@Override
	public void setFluidConnections(@Nonnull FluidConnection[] connections) {
		fluidConnections = connections;
	}
	
	@Override
	public @Nonnull FluidTileWrapper[] getFluidSides() {
		return fluidSides;
	}
	
	@Override
	public @Nonnull GasTileWrapper getGasWrapper() {
		return gasWrapper;
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
	
	// IMultitoolLogic
	
	@Override
	public boolean onUseMultitool(ItemStack multitool, EntityPlayer player, World worldIn, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (player.isSneaking()) {
			
		}
		else {
			if (getMultiblock() != null) {
				if (getTankSorption(facing, 0) != TankSorption.IN) {
					for (EnumFacing side : EnumFacing.VALUES) {
						setTankSorption(side, 0, TankSorption.IN);
						setTankSorption(side, 1, TankSorption.NON);
					}
					setActivity(false);
					player.sendMessage(new TextComponentString(Lang.localise("nc.block.port_toggle") + " " + TextFormatting.DARK_AQUA + Lang.localise("nc.block.fission_port_mode.input") + " " + TextFormatting.WHITE + Lang.localise("nc.block.port_toggle.mode")));
				}
				else {
					for (EnumFacing side : EnumFacing.VALUES) {
						setTankSorption(side, 0, TankSorption.NON);
						setTankSorption(side, 1, TankSorption.OUT);
					}
					setActivity(true);
					player.sendMessage(new TextComponentString(Lang.localise("nc.block.port_toggle") + " " + TextFormatting.RED + Lang.localise("nc.block.fission_port_mode.output") + " " + TextFormatting.WHITE + Lang.localise("nc.block.port_toggle.mode")));
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
		writeTanks(nbt);
		writeFluidConnections(nbt);
		writeTankSettings(nbt);
		return nbt;
	}
	
	@Override
	public void readAll(NBTTagCompound nbt) {
		super.readAll(nbt);
		readTanks(nbt);
		readFluidConnections(nbt);
		readTankSettings(nbt);
	}
	
	@Override
	public NBTTagCompound writeTanks(NBTTagCompound nbt) {
		ITileFilteredFluid.super.writeTanks(nbt);
		for (int i = 0; i < filterTanks.size(); ++i) {
			getTanks().get(i).writeToNBT(nbt, "filterTanks" + i + filterTanks.size());
		}
		return nbt;
	}
	
	@Override
	public void readTanks(NBTTagCompound nbt) {
		ITileFilteredFluid.super.readTanks(nbt);
		for (int i = 0; i < filterTanks.size(); ++i) {
			getTanks().get(i).readFromNBT(nbt, "filterTanks" + i + filterTanks.size());
		}
	}
	
	// Capability
	
	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing side) {
		if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY || ModCheck.mekanismLoaded() && enable_mek_gas && capability == CapabilityHelper.GAS_HANDLER_CAPABILITY) {
			return !getTanks().isEmpty() && hasFluidSideCapability(side);
		}
		return super.hasCapability(capability, side);
	}
	
	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing side) {
		if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
			if (!getTanks().isEmpty() && hasFluidSideCapability(side)) {
				return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(getFluidSide(nonNullSide(side)));
			}
			return null;
		}
		else if (ModCheck.mekanismLoaded() && capability == CapabilityHelper.GAS_HANDLER_CAPABILITY) {
			if (enable_mek_gas && !getTanks().isEmpty() && hasFluidSideCapability(side)) {
				return CapabilityHelper.GAS_HANDLER_CAPABILITY.cast(getGasWrapper());
			}
			return null;
		}
		return super.getCapability(capability, side);
	}
}
