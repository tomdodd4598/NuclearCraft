package nc.multiblock.fission.tile;

import static nc.block.property.BlockProperties.FACING_ALL;
import static nc.config.NCConfig.enable_gtce_eu;
import static nc.config.NCConfig.rf_per_eu;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import gregtech.api.capability.GregtechCapabilities;
import gregtech.api.capability.IEnergyContainer;
import ic2.api.energy.EnergyNet;
import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergyAcceptor;
import ic2.api.energy.tile.IEnergySink;
import ic2.api.energy.tile.IEnergySource;
import nc.ModCheck;
import nc.config.NCConfig;
import nc.multiblock.cuboidal.CuboidalPartPositionType;
import nc.multiblock.fission.FissionReactor;
import nc.multiblock.fission.block.BlockFissionPowerPort;
import nc.multiblock.turbine.tile.TileTurbinePart;
import nc.tile.energy.ITileEnergy;
import nc.tile.internal.energy.EnergyConnection;
import nc.tile.internal.energy.EnergyStorage;
import nc.tile.internal.energy.EnergyTileWrapper;
import nc.tile.internal.energy.EnergyTileWrapperGT;
import nc.tile.internal.fluid.TankSorption;
import nc.tile.passive.ITilePassive;
import nc.util.EnergyHelper;
import nc.util.Lang;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fml.common.Optional;

@Optional.InterfaceList({@Optional.Interface(iface = "ic2.api.energy.tile.IEnergyTile", modid = "ic2"), @Optional.Interface(iface = "ic2.api.energy.tile.IEnergySink", modid = "ic2"), @Optional.Interface(iface = "ic2.api.energy.tile.IEnergySource", modid = "ic2")})
public class TileFissionPowerPort extends TileFissionPart implements ITileEnergy, IEnergySource {
	
	protected final EnergyStorage backupStorage = new EnergyStorage(1);
	
	protected final EnergyConnection[] energyConnections = ITileEnergy.energyConnectionAll(EnergyConnection.OUT);
	
	protected final EnergyTileWrapper[] energySides = ITileEnergy.getDefaultEnergySides(this);
	protected final EnergyTileWrapperGT[] energySidesGT = ITileEnergy.getDefaultEnergySidesGT(this);
	
	protected boolean ic2reg = false;
	
	public TileFissionPowerPort() {
		super(CuboidalPartPositionType.WALL);
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
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
		return oldState.getBlock() != newState.getBlock();
	}
	
	@Override
	public void update() {
		super.update();
		EnumFacing facing = getPartPosition().getFacing();
		if (!world.isRemote && facing != null && getEnergyStored() > 0 && getEnergyConnection(facing).canExtract()) {
			pushEnergyToSide(facing);
		}
	}
	
	@Override
	public void onAdded() {
		super.onAdded();
		if (ModCheck.ic2Loaded()) {
			addTileToENet();
		}
	}
	
	@Override
	public void invalidate() {
		super.invalidate();
		if (ModCheck.ic2Loaded()) {
			removeTileFromENet();
		}
	}
	
	@Override
	public void onChunkUnload() {
		super.onChunkUnload();
		if (ModCheck.ic2Loaded()) {
			removeTileFromENet();
		}
	}
	
	@Override
	public EnergyStorage getEnergyStorage() {
		return getMultiblock() != null ? getLogic().getPowerPortEnergyStorage(backupStorage) : backupStorage;
	}
	
	@Override
	public EnergyConnection[] getEnergyConnections() {
		return energyConnections;
	}
	
	@Override
	public int getEUSourceTier() {
		return getMultiblock() != null ? getLogic().getPowerPortEUSourceTier() : 1;
	}
	
	@Override
	public int getEUSinkTier() {
		return 1;
	}
	
	@Override
	public @Nonnull EnergyTileWrapper[] getEnergySides() {
		return energySides;
	}
	
	@Override
	public @Nonnull EnergyTileWrapperGT[] getEnergySidesGT() {
		return energySidesGT;
	}
	
	// IC2 Energy
	
	@Override
	@Optional.Method(modid = "ic2")
	public boolean emitsEnergyTo(IEnergyAcceptor receiver, EnumFacing side) {
		return getEnergyConnection(side).canExtract();
	}
	
	@Override
	@Optional.Method(modid = "ic2")
	public double getOfferedEnergy() {
		return Math.min(Math.pow(2, 2 * getSourceTier() + 3), (double) getEnergyStorage().extractEnergy(getEnergyStorage().getMaxTransfer(), true) / (double) rf_per_eu);
	}
	
	@Override
	@Optional.Method(modid = "ic2")
	public void drawEnergy(double amount) {
		getEnergyStorage().extractEnergy((int) (rf_per_eu * amount), false);
	}
	
	@Override
	@Optional.Method(modid = "ic2")
	public int getSourceTier() {
		return getEUSourceTier();
	}
	
	@Override
	@Optional.Method(modid = "ic2")
	public void addTileToENet() {
		if (!world.isRemote && ModCheck.ic2Loaded() && NCConfig.enable_ic2_eu && !ic2reg) {
			MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this));
			ic2reg = true;
		}
	}
	
	@Override
	@Optional.Method(modid = "ic2")
	public void removeTileFromENet() {
		if (!world.isRemote && ModCheck.ic2Loaded() && ic2reg) {
			MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
			ic2reg = false;
		}
	}
	
	@Override
	public boolean hasConfigurableEnergyConnections() {
		return true;
	}
	
	// IMultitoolLogic
	
	@Override
	public boolean onUseMultitool(ItemStack multitoolStack, EntityPlayer player, World world, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (player.isSneaking()) {
			
		}
		else {
			if (getMultiblock() != null) {
				if (getEnergyConnection(facing) != EnergyConnection.OUT) {
					for (EnumFacing side : EnumFacing.VALUES) {
						setEnergyConnection(EnergyConnection.OUT, side);
					}
					setActivity(false);
					player.sendMessage(new TextComponentString(Lang.localise("nc.block.port_toggle") + " " + TextFormatting.GOLD + Lang.localise("nc.block.fission_port_mode.output") + " " + TextFormatting.WHITE + Lang.localise("nc.block.port_toggle.mode")));
				}
				else {
					for (EnumFacing side : EnumFacing.VALUES) {
						setEnergyConnection(EnergyConnection.IN, side);
					}
					setActivity(true);
					player.sendMessage(new TextComponentString(Lang.localise("nc.block.port_toggle") + " " + TextFormatting.DARK_AQUA + Lang.localise("nc.block.fission_port_mode.input") + " " + TextFormatting.WHITE + Lang.localise("nc.block.port_toggle.mode")));
				}
				markDirtyAndNotify(true);
				return true;
			}
		}
		return super.onUseMultitool(multitoolStack, player, world, facing, hitX, hitY, hitZ);
	}
	
	// NBT
	
	@Override
	public NBTTagCompound writeAll(NBTTagCompound nbt) {
		super.writeAll(nbt);
		writeEnergy(nbt);
		writeEnergyConnections(nbt);
		return nbt;
	}
	
	@Override
	public void readAll(NBTTagCompound nbt) {
		super.readAll(nbt);
		readEnergy(nbt);
		readEnergyConnections(nbt);
	}
	
	// Capability
	
	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing side) {
		if (capability == CapabilityEnergy.ENERGY || ModCheck.gregtechLoaded() && enable_gtce_eu && capability == GregtechCapabilities.CAPABILITY_ENERGY_CONTAINER) {
			return hasEnergySideCapability(side);
		}
		return super.hasCapability(capability, side);
	}
	
	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing side) {
		if (capability == CapabilityEnergy.ENERGY) {
			if (hasEnergySideCapability(side)) {
				return (T) getEnergySide(nonNullSide(side));
			}
			return null;
		}
		else if (ModCheck.gregtechLoaded() && capability == GregtechCapabilities.CAPABILITY_ENERGY_CONTAINER) {
			if (enable_gtce_eu && hasEnergySideCapability(side)) {
				return (T) getEnergySideGT(nonNullSide(side));
			}
			return null;
		}
		return super.getCapability(capability, side);
	}
}
