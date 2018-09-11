package nc.tile.energy;

import gregtech.api.capability.GregtechCapabilities;
import gregtech.api.capability.IEnergyContainer;
import ic2.api.energy.EnergyNet;
import ic2.api.energy.tile.IEnergyAcceptor;
import ic2.api.energy.tile.IEnergyEmitter;
import ic2.api.energy.tile.IEnergySink;
import ic2.api.energy.tile.IEnergySource;
import ic2.api.energy.tile.IEnergyTile;
import nc.ModCheck;
import nc.config.NCConfig;
import nc.tile.NCTile;
import nc.tile.internal.energy.EnergyConnection;
import nc.tile.internal.energy.EnergyStorage;
import nc.tile.internal.energy.EnergyTileWrapper;
import nc.tile.internal.energy.EnergyTileWrapperGT;
import nc.tile.passive.ITilePassive;
import nc.util.EnergyHelper;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fml.common.Optional;

@Optional.InterfaceList({@Optional.Interface(iface = "ic2.api.energy.tile.IEnergyTile", modid = "ic2"), @Optional.Interface(iface = "ic2.api.energy.tile.IEnergySink", modid = "ic2"), @Optional.Interface(iface = "ic2.api.energy.tile.IEnergySource", modid = "ic2")})
public abstract class TileEnergy extends NCTile implements ITileEnergy, IEnergyTile, IEnergySink, IEnergySource {
	
	private EnergyConnection[] energyConnections;
	protected boolean configurableEnergyConnections;
	private EnergyTileWrapper[] energySides;
	private EnergyTileWrapperGT[] energySidesGT;
	private final EnergyStorage storage;
	public boolean isEnergyTileSet = true;
	public boolean ic2reg = false;
	
	public TileEnergy(int capacity, EnergyConnection[] energyConnections) {
		this(capacity, capacity, capacity, energyConnections);
	}
	
	public TileEnergy(int capacity, int maxTransfer, EnergyConnection[] energyConnections) {
		this(capacity, maxTransfer, maxTransfer, energyConnections);
	}
	
	public TileEnergy(int capacity, int maxReceive, int maxExtract, EnergyConnection[] energyConnections) {
		super();
		storage = new EnergyStorage(capacity, maxReceive, maxExtract);
		this.energyConnections = energyConnections;
		energySides = getDefaultEnergySides(this);
		energySidesGT = getDefaultEnergySidesGT(this);
	}
	
	@Override
	public void update() {
		super.update();
	}
	
	@Override
	public void onAdded() {
		super.onAdded();
		if (ModCheck.ic2Loaded()) addTileToENet();
	}
	
	/*@Override
	public void validate() {
		super.validate();
	}*/
	
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
	
	// Forge Energy
	
	@Override
	public EnergyStorage getEnergyStorage() {
		return storage;
	}
	
	// IC2 Energy
	
	@Override
	@Optional.Method(modid = "ic2")
	public boolean acceptsEnergyFrom(IEnergyEmitter emitter, EnumFacing side) {
		return getEnergyConnection(side).canReceive();
	}
	
	@Override
	@Optional.Method(modid = "ic2")
	public boolean emitsEnergyTo(IEnergyAcceptor receiver, EnumFacing side) {
		return getEnergyConnection(side).canExtract();
	}
	
	@Override
	@Optional.Method(modid = "ic2")
	public double getOfferedEnergy() {
		return Math.min(Math.pow(2, 2*getSourceTier() + 3), (double)getEnergyStorage().extractEnergy(getEnergyStorage().getMaxExtract(), true) / (double)NCConfig.rf_per_eu);
	}
	
	@Override
	@Optional.Method(modid = "ic2")
	public double getDemandedEnergy() {
		return Math.min(Math.pow(2, 2*getSinkTier() + 3), (double)getEnergyStorage().receiveEnergy(getEnergyStorage().getMaxReceive(), true) / (double)NCConfig.rf_per_eu);
	}
	
	/** The normal conversion is 4 RF to 1 EU, but for RF generators, this is OP, so the ratio is instead 16:1 */
	@Override
	@Optional.Method(modid = "ic2")
	public void drawEnergy(double amount) {
		getEnergyStorage().extractEnergy((int) (NCConfig.rf_per_eu * amount), false);
	}
	
	@Override
	@Optional.Method(modid = "ic2")
	public double injectEnergy(EnumFacing directionFrom, double amount, double voltage) {
		int energyReceived = getEnergyStorage().receiveEnergy((int) ((double)NCConfig.rf_per_eu * amount), true);
		getEnergyStorage().receiveEnergy(energyReceived, false);
		return amount - (double)energyReceived/(double)NCConfig.rf_per_eu;
	}
	
	@Override
	@Optional.Method(modid = "ic2")
	public int getSourceTier() {
		return getEUSourceTier();
	}
	
	@Override
	@Optional.Method(modid = "ic2")
	public int getSinkTier() {
		return getEUSinkTier();
	}
	
	@Override
	public abstract int getEUSourceTier();
	
	@Override
	public abstract int getEUSinkTier();
	
	@Optional.Method(modid = "ic2")
	public void addTileToENet() {
		if (!world.isRemote && ModCheck.ic2Loaded() && !ic2reg) {
			EnergyNet.instance.addTile(this);
			ic2reg = true;
		}
	}
	
	@Optional.Method(modid = "ic2")
	public void removeTileFromENet() {
		if (!world.isRemote && ModCheck.ic2Loaded() && ic2reg) {
			EnergyNet.instance.removeTile(this);
			ic2reg = false;
		}
	}
	
	// NBT
	
	@Override
	public NBTTagCompound writeEnergy(NBTTagCompound nbt) {
		nbt.setInteger("energy", getEnergyStorage().getEnergyStored());
		nbt.setInteger("capacity", getEnergyStorage().getMaxEnergyStored());
		nbt.setInteger("maxReceive", getEnergyStorage().getMaxReceive());
		nbt.setInteger("maxExtract", getEnergyStorage().getMaxExtract());
		return nbt;
	}
	
	@Override
	public void readEnergy(NBTTagCompound nbt) {
		getEnergyStorage().setEnergyStored(nbt.getInteger("energy"));
		getEnergyStorage().setStorageCapacity(nbt.getInteger("capacity"));
		getEnergyStorage().setMaxReceive(nbt.getInteger("maxReceive"));
		getEnergyStorage().setMaxExtract(nbt.getInteger("maxExtract"));
	}
	
	@Override
	public NBTTagCompound writeEnergyConnections(NBTTagCompound nbt) {
		for (int i = 0; i < 6; i++) nbt.setInteger("energyConnections" + i, energyConnections[i].ordinal());
		return nbt;
	}
	
	@Override
	public void readEnergyConnections(NBTTagCompound nbt) {
		if (configurableEnergyConnections) for (int i = 0; i < 6; i++) energyConnections[i] = EnergyConnection.values()[nbt.getInteger("energyConnections" + i)];
	}
	
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
	
	// Energy Connections
	
	public static EnergyConnection[] energyConnectionAll(EnergyConnection connection) {
		EnergyConnection[] array = new EnergyConnection[6];
		for (int i = 0; i < 6; i++) array[i] = connection;
		return array;
	}
	
	@Override
	public EnergyConnection getEnergyConnection(EnumFacing side) {
		return energyConnections[side.getIndex()];
	}
	
	@Override
	public void setEnergyConnection(EnergyConnection energyConnection, EnumFacing side) {
		energyConnections[side.getIndex()] = energyConnection;
	}
	
	public void setEnergyConnectionAll(EnergyConnection energyConnection) {
		energyConnections = energyConnectionAll(energyConnection);
	}
	
	@Override
	public void toggleEnergyConnection(EnumFacing side) {
		if (ModCheck.ic2Loaded()) removeTileFromENet();
		setEnergyConnection(getEnergyConnection(side).next(), side);
		markAndRefresh();
		if (ModCheck.ic2Loaded()) addTileToENet();
	}
	
	public void pushEnergy() {
		if (getEnergyStorage().getEnergyStored() <= 0) return;
		for (EnumFacing side : EnumFacing.VALUES) pushEnergyToSide(side);
	}
	
	public void spreadEnergy() {
		if (!NCConfig.passive_permeation || getEnergyStorage().getEnergyStored() <= 0) return;
		for (EnumFacing side : EnumFacing.VALUES) spreadEnergyToSide(side);
	}
	
	public void pushEnergyToSide(EnumFacing side) {
		if (getEnergyStorage().getEnergyStored() <= 0 || !getEnergyConnection(side).canExtract()) return;
		
		TileEntity tile = world.getTileEntity(getPos().offset(side));
		
		if (tile instanceof ITileEnergy) if (!((ITileEnergy) tile).getEnergyConnection(side.getOpposite()).canReceive()) return;
		if (tile instanceof ITilePassive) if (!((ITilePassive) tile).canPushEnergyTo()) return;
		
		IEnergyStorage adjStorage = tile == null ? null : tile.getCapability(CapabilityEnergy.ENERGY, side.getOpposite());
		
		if (adjStorage != null && getEnergyStorage().canExtract()) {
			getEnergyStorage().extractEnergy(adjStorage.receiveEnergy(getEnergyStorage().extractEnergy(getEnergyStorage().getMaxEnergyStored(), true), false), false);
			return;
		}
		if (ModCheck.ic2Loaded()) {
			if (tile instanceof IEnergySink) {
				getEnergyStorage().extractEnergy((int) Math.round(((IEnergySink) tile).injectEnergy(side.getOpposite(), getEnergyStorage().extractEnergy(getEnergyStorage().getMaxEnergyStored(), true)/NCConfig.rf_per_eu, getSourceTier())*NCConfig.rf_per_eu), false);
				return;
			}
		}
		if (ModCheck.gregtechLoaded()) {
			IEnergyContainer adjStorageGT = tile == null ? null : tile.getCapability(GregtechCapabilities.CAPABILITY_ENERGY_CONTAINER, side.getOpposite());
			if (adjStorageGT != null && getEnergyStorage().canExtract()) {
				int voltage = Math.min(EnergyHelper.getMaxEUFromTier(getEUSourceTier()), getEnergyStorage().getEnergyStored()/NCConfig.rf_per_eu);
				getEnergyStorage().extractEnergy((int)Math.min(voltage*adjStorageGT.acceptEnergyFromNetwork(side.getOpposite(), voltage, 1)*NCConfig.rf_per_eu, Integer.MAX_VALUE), false);
				return;
			}
		}
	}
	
	public void spreadEnergyToSide(EnumFacing side) {
		if (getEnergyStorage().getEnergyStored() <= 0 || !getEnergyConnection(side).canConnect()) return;
		
		TileEntity tile = world.getTileEntity(getPos().offset(side));
		
		if (!(tile instanceof IEnergySpread)) return;
		if (tile instanceof ITilePassive) if (!((ITilePassive) tile).canPushEnergyTo()) return;
		IEnergyStorage adjStorage = tile == null ? null : tile.getCapability(CapabilityEnergy.ENERGY, side.getOpposite());
		
		if (adjStorage != null && getEnergyStorage().canExtract()) {
			int maxExtract = (getEnergyStorage().getEnergyStored() - adjStorage.getEnergyStored())/2;
			if (maxExtract > 0) getEnergyStorage().extractEnergy(adjStorage.receiveEnergy(getEnergyStorage().extractEnergy(maxExtract, true), false), false);
		}
	}
	
	// Capability
	
	public static EnergyTileWrapper[] getDefaultEnergySides(ITileEnergy tile) {
		return new EnergyTileWrapper[] {new EnergyTileWrapper(tile, EnumFacing.DOWN), new EnergyTileWrapper(tile, EnumFacing.UP), new EnergyTileWrapper(tile, EnumFacing.NORTH), new EnergyTileWrapper(tile, EnumFacing.SOUTH), new EnergyTileWrapper(tile, EnumFacing.WEST), new EnergyTileWrapper(tile, EnumFacing.EAST)};
	}
	
	public static EnergyTileWrapperGT[] getDefaultEnergySidesGT(ITileEnergy tile) {
		return new EnergyTileWrapperGT[] {new EnergyTileWrapperGT(tile, EnumFacing.DOWN), new EnergyTileWrapperGT(tile, EnumFacing.UP), new EnergyTileWrapperGT(tile, EnumFacing.NORTH), new EnergyTileWrapperGT(tile, EnumFacing.SOUTH), new EnergyTileWrapperGT(tile, EnumFacing.WEST), new EnergyTileWrapperGT(tile, EnumFacing.EAST)};
	}
	
	@Override
	public EnergyTileWrapper getEnergySide(EnumFacing side) {
		return side == null ? energySides[0] : energySides[side.getIndex()];
	}
	
	@Override
	public EnergyTileWrapperGT getEnergySideGT(EnumFacing side) {
		return side == null ? energySidesGT[0] : energySidesGT[side.getIndex()];
	}
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing side) {
		if (capability == CapabilityEnergy.ENERGY) return getEnergySide(side) != null;
		if (ModCheck.gregtechLoaded()) if (capability == GregtechCapabilities.CAPABILITY_ENERGY_CONTAINER) return getEnergySideGT(side) != null;
		return super.hasCapability(capability, side);
	}
	
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing side) {
		if (capability == CapabilityEnergy.ENERGY) return (T) getEnergySide(side);
		if (ModCheck.gregtechLoaded()) if (capability == GregtechCapabilities.CAPABILITY_ENERGY_CONTAINER) return (T) getEnergySideGT(side);
		return super.getCapability(capability, side);
	}
}
