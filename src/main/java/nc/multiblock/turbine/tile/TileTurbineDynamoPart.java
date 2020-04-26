package nc.multiblock.turbine.tile;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import gregtech.api.capability.GregtechCapabilities;
import gregtech.api.capability.IEnergyContainer;
import ic2.api.energy.EnergyNet;
import ic2.api.energy.tile.IEnergyAcceptor;
import ic2.api.energy.tile.IEnergySink;
import ic2.api.energy.tile.IEnergySource;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import nc.ModCheck;
import nc.config.NCConfig;
import nc.multiblock.cuboidal.CuboidalPartPositionType;
import nc.multiblock.turbine.Turbine;
import nc.tile.energy.ITileEnergy;
import nc.tile.internal.energy.EnergyConnection;
import nc.tile.internal.energy.EnergyStorage;
import nc.tile.internal.energy.EnergyTileWrapper;
import nc.tile.internal.energy.EnergyTileWrapperGT;
import nc.tile.passive.ITilePassive;
import nc.util.EnergyHelper;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fml.common.Optional;

@Optional.InterfaceList({@Optional.Interface(iface = "ic2.api.energy.tile.IEnergyTile", modid = "ic2"), @Optional.Interface(iface = "ic2.api.energy.tile.IEnergySink", modid = "ic2"), @Optional.Interface(iface = "ic2.api.energy.tile.IEnergySource", modid = "ic2")})
public abstract class TileTurbineDynamoPart extends TileTurbinePart implements ITileEnergy, IEnergySource {
	
	protected final EnergyStorage backupStorage = new EnergyStorage(1);
	
	protected final EnergyConnection[] energyConnections = ITileEnergy.energyConnectionAll(EnergyConnection.OUT);
	
	protected final EnergyTileWrapper[] energySides = ITileEnergy.getDefaultEnergySides(this);
	protected final EnergyTileWrapperGT[] energySidesGT = ITileEnergy.getDefaultEnergySidesGT(this);
	
	protected boolean ic2reg = false;
	
	public final String partName;
	public final Double conductivity;
	public boolean isSearched = false, isInValidPosition = false;
	
	public TileTurbineDynamoPart(String partName, Double conductivity) {
		super(CuboidalPartPositionType.WALL);
		this.partName = partName;
		this.conductivity = conductivity;
	}
	
	@Override
	public void onMachineAssembled(Turbine controller) {
		doStandardNullControllerResponse(controller);
		super.onMachineAssembled(controller);
		//if (getWorld().isRemote) return;
	}
	
	@Override
	public void onMachineBroken() {
		super.onMachineBroken();
		//if (getWorld().isRemote) return;
		//getWorld().setBlockState(getPos(), getWorld().getBlockState(getPos()), 2);
	}
	
	public void dynamoSearch(ObjectSet<TileTurbineDynamoPart> cache) {
		if (isSearched || !isDynamoCoilValid()) return;
		
		isSearched = true;
		cache.add(this);
		
		for (EnumFacing dir : EnumFacing.VALUES) {
			TileTurbineDynamoPart part = getMultiblock().getPartMap(TileTurbineDynamoPart.class).get(getTilePos().offset(dir).toLong());
			if (part != null) part.dynamoSearch(cache);
		}
	}
	
	public boolean isDynamoCoilValid() {
		if (isInValidPosition) return true;
		return isInValidPosition = checkDynamoCoilValid();
	}
	
	protected abstract boolean checkDynamoCoilValid();
	
	protected boolean isRotorBearing(BlockPos pos) {
		return getMultiblock().getPartMap(TileTurbineRotorBearing.class).get(pos.toLong()) != null;
	}
	
	protected boolean isCoilConnector(BlockPos pos) {
		return isDynamoCoil(pos, "connector");
	}
	
	protected boolean isDynamoCoil(BlockPos pos, String coilName) {
		TileTurbineDynamoPart part = getMultiblock().getPartMap(TileTurbineDynamoPart.class).get(pos.toLong());
		return part instanceof TileTurbineDynamoCoil ? part.isInValidPosition && (coilName == null || part.partName.equals(coilName)) : false;
	}
	
	public boolean isSearchRoot() {
		return false;
	}
	
	@Override
	public void update() {
		super.update();
		if (!world.isRemote) {
			pushEnergy();
		}
	}
	
	@Override
	public void onAdded() {
		super.onAdded();
		if (ModCheck.ic2Loaded()) addTileToENet();
	}
	
	@Override
	public void invalidate() {
		super.invalidate();
		if (ModCheck.ic2Loaded()) removeTileFromENet();
	}
	
	@Override
	public void onChunkUnload() {
		super.onChunkUnload();
		if (ModCheck.ic2Loaded()) removeTileFromENet();
	}
	
	@Override
	public EnergyStorage getEnergyStorage() {
		if (!isInValidPosition || !isMultiblockAssembled()) return backupStorage;
		return getMultiblock().energyStorage;
		
	}
	
	@Override
	public EnergyConnection[] getEnergyConnections() {
		return energyConnections;
	}
	
	@Override
	public int getEUSourceTier() {
		if (!isInValidPosition || !isMultiblockAssembled()) return 1;
		return EnergyHelper.getEUTier(getMultiblock().power);
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
	
	// Energy Pushing
	
	@Override
	public void pushEnergyToSide(@Nonnull EnumFacing side) {
		if (!getEnergyConnection(side).canExtract() || getEnergyStorage().getEnergyStored() == 0) return;
		
		TileEntity tile = getTileWorld().getTileEntity(getTilePos().offset(side));
		if (tile == null || tile instanceof TileTurbinePart) return;
		
		if (tile instanceof ITileEnergy) if (!((ITileEnergy)tile).getEnergyConnection(side.getOpposite()).canReceive()) return;
		if (tile instanceof ITilePassive) if (!((ITilePassive)tile).canPushEnergyTo()) return;
		
		IEnergyStorage adjStorage = tile.getCapability(CapabilityEnergy.ENERGY, side.getOpposite());
		
		if (adjStorage != null && getEnergyStorage().canExtract()) {
			getEnergyStorage().extractEnergy(adjStorage.receiveEnergy(getEnergyStorage().extractEnergy(getEnergyStorage().getMaxEnergyStored(), true), false), false);
			return;
		}
		
		if (getEnergyStorage().getEnergyStored() < NCConfig.rf_per_eu) return;
		
		if (ModCheck.ic2Loaded()) {
			if (tile instanceof IEnergySink) {
				getEnergyStorage().extractEnergy((int) Math.round(((IEnergySink)tile).injectEnergy(side.getOpposite(), getEnergyStorage().extractEnergy(getEnergyStorage().getMaxEnergyStored(), true)/NCConfig.rf_per_eu, getEUSourceTier())*NCConfig.rf_per_eu), false);
				return;
			}
		}
		if (NCConfig.enable_gtce_eu && ModCheck.gregtechLoaded()) {
			IEnergyContainer adjStorageGT = tile.getCapability(GregtechCapabilities.CAPABILITY_ENERGY_CONTAINER, side.getOpposite());
			if (adjStorageGT != null && getEnergyStorage().canExtract()) {
				int voltage = MathHelper.clamp(getEnergyStorage().getEnergyStored()/NCConfig.rf_per_eu, 1, EnergyHelper.getMaxEUFromTier(getEUSourceTier()));
				getEnergyStorage().extractEnergy((int)Math.min(voltage*adjStorageGT.acceptEnergyFromNetwork(side.getOpposite(), voltage, 1)*NCConfig.rf_per_eu, Integer.MAX_VALUE), false);
				return;
			}
		}
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
		return Math.min(Math.pow(2, 2*getSourceTier() + 3), (double)getEnergyStorage().extractEnergy(getEnergyStorage().getMaxTransfer(), true) / (double)NCConfig.rf_per_eu);
	}
	
	@Override
	@Optional.Method(modid = "ic2")
	public void drawEnergy(double amount) {
		getEnergyStorage().extractEnergy((int) (NCConfig.rf_per_eu * amount), false);
	}
	
	@Override
	@Optional.Method(modid = "ic2")
	public int getSourceTier() {
		return getEUSourceTier();
	}
	
	@Override
	@Optional.Method(modid = "ic2")
	public void addTileToENet() {
		if (!world.isRemote && ModCheck.ic2Loaded() && !ic2reg) {
			EnergyNet.instance.addTile(this);
			ic2reg = true;
		}
	}
	
	@Override
	@Optional.Method(modid = "ic2")
	public void removeTileFromENet() {
		if (!world.isRemote && ModCheck.ic2Loaded() && ic2reg) {
			EnergyNet.instance.removeTile(this);
			ic2reg = false;
		}
	}
	
	// NBT
	
	@Override
	public NBTTagCompound writeAll(NBTTagCompound nbt) {
		super.writeAll(nbt);
		writeEnergy(nbt);
		writeEnergyConnections(nbt);
		nbt.setBoolean("isInValidPosition", isInValidPosition);
		return nbt;
	}
	
	@Override
	public void readAll(NBTTagCompound nbt) {
		super.readAll(nbt);
		readEnergy(nbt);
		readEnergyConnections(nbt);
		isInValidPosition = nbt.getBoolean("isInValidPosition");
	}
	
	// Capability
	
	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing side) {
		if (capability == CapabilityEnergy.ENERGY || (ModCheck.gregtechLoaded() && NCConfig.enable_gtce_eu && capability == GregtechCapabilities.CAPABILITY_ENERGY_CONTAINER)) {
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
			if (NCConfig.enable_gtce_eu && hasEnergySideCapability(side)) {
				return (T) getEnergySideGT(nonNullSide(side));
			}
			return null;
		}
		return super.getCapability(capability, side);
	}
}
