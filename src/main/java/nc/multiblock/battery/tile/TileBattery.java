package nc.multiblock.battery.tile;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import gregtech.api.capability.GregtechCapabilities;
import ic2.api.energy.EnergyNet;
import ic2.api.energy.tile.IEnergyAcceptor;
import ic2.api.energy.tile.IEnergyEmitter;
import ic2.api.energy.tile.IEnergySink;
import ic2.api.energy.tile.IEnergySource;
import nc.ModCheck;
import nc.config.NCConfig;
import nc.multiblock.battery.BatteryMultiblock;
import nc.multiblock.battery.BatteryType;
import nc.multiblock.tile.TileMultiblockPart;
import nc.tile.dummy.IInterfaceable;
import nc.tile.energy.IEnergySpread;
import nc.tile.energy.ITileEnergy;
import nc.tile.internal.energy.EnergyConnection;
import nc.tile.internal.energy.EnergyStorage;
import nc.tile.internal.energy.EnergyTileWrapper;
import nc.tile.internal.energy.EnergyTileWrapperGT;
import nc.util.NCMath;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fml.common.Optional;

@Optional.InterfaceList({@Optional.Interface(iface = "ic2.api.energy.tile.IEnergyTile", modid = "ic2"), @Optional.Interface(iface = "ic2.api.energy.tile.IEnergySink", modid = "ic2"), @Optional.Interface(iface = "ic2.api.energy.tile.IEnergySource", modid = "ic2")})
public class TileBattery extends TileMultiblockPart<BatteryMultiblock> implements IEnergySpread, IEnergySink, IEnergySource, IInterfaceable {
	
	public static class VoltaicPileBasic extends TileBattery {
		
		public VoltaicPileBasic() {
			super(BatteryType.VOLTAIC_PILE_BASIC);
		}
	}
	
	public static class VoltaicPileAdvanced extends TileBattery {
		
		public VoltaicPileAdvanced() {
			super(BatteryType.VOLTAIC_PILE_ADVANCED);
		}
	}
	
	public static class VoltaicPileDU extends TileBattery {
		
		public VoltaicPileDU() {
			super(BatteryType.VOLTAIC_PILE_DU);
		}
	}
	
	public static class VoltaicPileElite extends TileBattery {
		
		public VoltaicPileElite() {
			super(BatteryType.VOLTAIC_PILE_ELITE);
		}
	}
	
	public static class LithiumIonBatteryBasic extends TileBattery {
		
		public LithiumIonBatteryBasic() {
			super(BatteryType.LITHIUM_ION_BATTERY_BASIC);
		}
	}
	
	public static class LithiumIonBatteryAdvanced extends TileBattery {
		
		public LithiumIonBatteryAdvanced() {
			super(BatteryType.LITHIUM_ION_BATTERY_ADVANCED);
		}
	}
	
	public static class LithiumIonBatteryDU extends TileBattery {
		
		public LithiumIonBatteryDU() {
			super(BatteryType.LITHIUM_ION_BATTERY_DU);
		}
	}
	
	public static class LithiumIonBatteryElite extends TileBattery {
		
		public LithiumIonBatteryElite() {
			super(BatteryType.LITHIUM_ION_BATTERY_ELITE);
		}
	}
	
	private final EnergyStorage backupStorage = new EnergyStorage(1);
	
	private @Nonnull EnergyConnection[] energyConnections;
	private boolean[] ignoreSide = new boolean[] {false, false, false, false, false, false};
	
	private @Nonnull EnergyTileWrapper[] energySides;
	private @Nonnull EnergyTileWrapperGT[] energySidesGT;
	
	private boolean ic2reg = false;
	
	public final long capacity;
	private final int energyTier;
	
	private TileBattery(BatteryType type) {
		this(type.getCapacity(), type.getEnergyTier());
	}
	
	public TileBattery(long capacity, int energyTier) {
		super(BatteryMultiblock.class);
		this.capacity = capacity;
		this.energyTier = energyTier;
		energyConnections = ITileEnergy.energyConnectionAll(EnergyConnection.IN);
		energySides = ITileEnergy.getDefaultEnergySides(this);
		energySidesGT = ITileEnergy.getDefaultEnergySidesGT(this);
	}
	
	private boolean ignoreSide(EnumFacing side) {
		return side == null ? false : ignoreSide[side.getIndex()];
	}
	
	@Override
	public void onMachineAssembled(BatteryMultiblock controller) {
		doStandardNullControllerResponse(controller);
		//if (getWorld().isRemote) return;
	}
	
	@Override
	public void onMachineBroken() {
		//if (getWorld().isRemote) return;
		//getWorld().setBlockState(getPos(), getWorld().getBlockState(getPos()), 2);
	}
	
	@Override
	public BatteryMultiblock createNewMultiblock() {
		return new BatteryMultiblock(world);
	}
	
	@Override
	public void onAdded() {
		super.onAdded();
		if (ModCheck.ic2Loaded()) addTileToENet();
	}
	
	@Override
	public void update() {
		super.update();
		if(!world.isRemote) {
			pushEnergy();
		}
	}
	
	@Override
	public void pushEnergyToSide(@Nonnull EnumFacing side) {
		if (!ignoreSide(side)) {
			IEnergySpread.super.pushEnergyToSide(side);
		}
	}
	
	public void onMultiblockRefresh() {
		for (EnumFacing side : EnumFacing.VALUES) {
			ignoreSide[side.getIndex()] = world.getTileEntity(pos.offset(side)) instanceof TileBattery;
		}
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
		return getMultiblock() != null ? getMultiblock().getEnergyStorage() : backupStorage;
	}
	
	@Override
	public EnergyConnection[] getEnergyConnections() {
		return energyConnections;
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
		return Math.min(Math.pow(2, 2*getSourceTier() + 3), (double)getEnergyStorage().extractEnergy(getEnergyStorage().getMaxTransfer(), true) / (double)NCConfig.rf_per_eu);
	}
	
	@Override
	@Optional.Method(modid = "ic2")
	public double getDemandedEnergy() {
		return Math.min(Math.pow(2, 2*getSinkTier() + 3), (double)getEnergyStorage().receiveEnergy(getEnergyStorage().getMaxTransfer(), true) / (double)NCConfig.rf_per_eu);
	}
	
	/* The normal conversion is 4 RF to 1 EU, but for RF generators, this is OP, so the ratio is instead 16:1 */
	@Override
	@Optional.Method(modid = "ic2")
	public void drawEnergy(double amount) {
		getEnergyStorage().extractEnergy((int) (NCConfig.rf_per_eu * amount), false);
	}
	
	@Override
	@Optional.Method(modid = "ic2")
	public double injectEnergy(EnumFacing directionFrom, double amount, double voltage) {
		int energyReceived = getEnergyStorage().receiveEnergy((int) (NCConfig.rf_per_eu * amount), true);
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
	
	@Override
	public int getEUSourceTier() {
		return energyTier;
	}
	
	@Override
	public int getEUSinkTier() {
		return energyTier;
	}
	
	@Override
	public boolean hasConfigurableEnergyConnections() {
		return true;
	}
	
	// NBT
	
	@Override
	public NBTTagCompound writeAll(NBTTagCompound nbt) {
		super.writeAll(nbt);
		writeEnergyConnections(nbt);
		nbt.setByteArray("ignoreSide", NCMath.booleansToBytes(ignoreSide));
		return nbt;
	}
	
	@Override
	public void readAll(NBTTagCompound nbt) {
		super.readAll(nbt);
		readEnergyConnections(nbt);
		boolean[] arr = NCMath.bytesToBooleans(nbt.getByteArray("ignoreSide"));
		if (arr.length == 6) ignoreSide = arr;
	}
	
	// Capability
	
	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing side) {
		if (!ignoreSide(side) && (capability == CapabilityEnergy.ENERGY || (ModCheck.gregtechLoaded() && NCConfig.enable_gtce_eu && capability == GregtechCapabilities.CAPABILITY_ENERGY_CONTAINER))) {
			return hasEnergySideCapability(side);
		}
		return super.hasCapability(capability, side);
	}
	
	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing side) {
		if (!ignoreSide(side)) {
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
		}
		return super.getCapability(capability, side);
	}
}
