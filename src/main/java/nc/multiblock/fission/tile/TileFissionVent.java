package nc.multiblock.fission.tile;

import static nc.block.property.BlockProperties.FACING_ALL;
import static nc.config.NCConfig.enable_mek_gas;

import java.util.*;

import javax.annotation.*;

import com.google.common.collect.Lists;

import nc.ModCheck;
import nc.multiblock.cuboidal.CuboidalPartPositionType;
import nc.multiblock.fission.FissionReactor;
import nc.tile.fluid.ITileFluid;
import nc.tile.internal.fluid.*;
import nc.tile.passive.ITilePassive;
import nc.util.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.text.*;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.capability.*;

public class TileFissionVent extends TileFissionPart implements ITickable, ITileFluid {
	
	private final @Nonnull List<Tank> backupTanks = Lists.newArrayList(new Tank(1, new ArrayList<>()), new Tank(1, new ArrayList<>()));
	
	private @Nonnull FluidConnection[] fluidConnections = ITileFluid.fluidConnectionAll(Lists.newArrayList(TankSorption.IN, TankSorption.NON));
	
	private @Nonnull final FluidTileWrapper[] fluidSides;
	
	private @Nonnull final GasTileWrapper gasWrapper;
	
	public TileFissionVent() {
		super(CuboidalPartPositionType.WALL);
		fluidSides = ITileFluid.getDefaultFluidSides(this);
		gasWrapper = new GasTileWrapper(this);
	}
	
	@Override
	public void onMachineAssembled(FissionReactor controller) {
		doStandardNullControllerResponse(controller);
		super.onMachineAssembled(controller);
		if (!getWorld().isRemote && getPartPosition().getFacing() != null) {
			getWorld().setBlockState(getPos(), getWorld().getBlockState(getPos()).withProperty(FACING_ALL, getPartPosition().getFacing()), 2);
		}
	}
	
	@Override
	public void onMachineBroken() {
		super.onMachineBroken();
	}
	
	@Override
	public void update() {
		EnumFacing facing = getPartPosition().getFacing();
		if (!world.isRemote && facing != null && !getTanks().get(1).isEmpty() && getTankSorption(facing, 1).canDrain()) {
			pushFluidToSide(facing);
		}
	}
	
	// Fluids
	
	@Override
	public @Nonnull List<Tank> getTanks() {
		return getMultiblock() != null ? getLogic().getVentTanks(backupTanks) : backupTanks;
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
		TileEntity tile = getTileWorld().getTileEntity(getTilePos().offset(side));
		if (tile == null || tile instanceof TileFissionVent) {
			return;
		}
		
		if (tile instanceof ITilePassive) {
			if (!((ITilePassive) tile).canPushFluidsTo()) {
				return;
			}
		}
		
		IFluidHandler adjStorage = tile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side.getOpposite());
		if (adjStorage == null) {
			return;
		}
		
		for (int i = 0; i < getTanks().size(); ++i) {
			Tank tank = getTanks().get(i);
			if (tank.getFluid() == null || !getTankSorption(side, i).canDrain()) {
				continue;
			}
			
			tank.drain(adjStorage.fill(tank.drain(tank.getCapacity(), false), true), true);
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
					player.sendMessage(new TextComponentString(Lang.localise("nc.block.vent_toggle") + " " + TextFormatting.DARK_AQUA + Lang.localise("nc.block.fission_vent_mode.input") + " " + TextFormatting.WHITE + Lang.localise("nc.block.vent_toggle.mode")));
				}
				else {
					for (EnumFacing side : EnumFacing.VALUES) {
						setTankSorption(side, 0, TankSorption.NON);
						setTankSorption(side, 1, TankSorption.OUT);
					}
					setActivity(true);
					player.sendMessage(new TextComponentString(Lang.localise("nc.block.vent_toggle") + " " + TextFormatting.RED + Lang.localise("nc.block.fission_vent_mode.output") + " " + TextFormatting.WHITE + Lang.localise("nc.block.vent_toggle.mode")));
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
		writeFluidConnections(nbt);
		return nbt;
	}
	
	@Override
	public void readAll(NBTTagCompound nbt) {
		super.readAll(nbt);
		readFluidConnections(nbt);
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
