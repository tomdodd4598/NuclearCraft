package nc.tile.rtg;

import gregtech.api.capability.GregtechCapabilities;
import ic2.api.energy.tile.*;
import it.unimi.dsi.fastutil.objects.*;
import nc.ModCheck;
import nc.multiblock.rtg.*;
import nc.tile.energy.ITileEnergy;
import nc.tile.internal.energy.*;
import nc.tile.multiblock.TileMultiblockPart;
import nc.util.*;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fml.common.Optional;

import javax.annotation.*;

import static nc.config.NCConfig.enable_gtce_eu;

@Optional.Interface(iface = "ic2.api.energy.tile.IEnergySource", modid = "ic2")
public class TileRTG extends TileMultiblockPart<RTGMultiblock, TileRTG> implements ITickable, ITileEnergy, IEnergySource {
	
	public static final Object2LongMap<String> DYN_POWER_MAP = new Object2LongOpenHashMap<>();
	public static final Object2DoubleMap<String> DYN_RADIATION_MAP = new Object2DoubleOpenHashMap<>();
	
	public static class Uranium extends TileRTG {
		
		public Uranium() {
			super(RTGType.URANIUM);
		}
	}
	
	public static class Plutonium extends TileRTG {
		
		public Plutonium() {
			super(RTGType.PLUTONIUM);
		}
	}
	
	public static class Americium extends TileRTG {
		
		public Americium() {
			super(RTGType.AMERICIUM);
		}
	}
	
	public static class Californium extends TileRTG {
		
		public Californium() {
			super(RTGType.CALIFORNIUM);
		}
	}
	
	protected String rtgType;
	
	private final EnergyStorage backupStorage = new EnergyStorage(0L);
	
	private @Nonnull
	final EnergyConnection[] energyConnections;
	private boolean[] ignoreSide = new boolean[] {false, false, false, false, false, false};
	
	private @Nonnull
	final EnergyTileWrapper[] energySides;
	private @Nonnull
	final EnergyTileWrapperGT[] energySidesGT;
	
	private boolean ic2reg = false;
	
	public long power;
	public double radiation;
	
	/**
	 * Don't use this constructor!
	 */
	public TileRTG() {
		super(RTGMultiblock.class, TileRTG.class);
		energyConnections = ITileEnergy.energyConnectionAll(EnergyConnection.OUT);
		energySides = ITileEnergy.getDefaultEnergySides(this);
		energySidesGT = ITileEnergy.getDefaultEnergySidesGT(this);
	}
	
	public TileRTG(String rtgType, long power, double radiation) {
		this();
		this.power = power;
		this.radiation = radiation;
		getRadiationSource().setRadiationLevel(radiation);
	}
	
	protected TileRTG(RTGType type) {
		this(type.getName(), type.getPower(), type.getRadiation());
	}
	
	private boolean ignoreSide(EnumFacing side) {
		return side != null && ignoreSide[side.getIndex()];
	}
	
	@Override
	public void onMachineAssembled(RTGMultiblock multiblock) {
		doStandardNullControllerResponse(multiblock);
	}
	
	@Override
	public void onMachineBroken() {
	
	}
	
	@Override
	public RTGMultiblock createNewMultiblock() {
		return new RTGMultiblock(world);
	}
	
	@Override
	public void onLoad() {
		super.onLoad();
		if (ModCheck.ic2Loaded()) {
			addTileToENet();
		}
	}
	
	@Override
	public void update() {
		if (!world.isRemote) {
			pushEnergy();
		}
	}
	
	@Override
	public void pushEnergyToSide(@Nonnull EnumFacing side) {
		if (!ignoreSide(side)) {
			ITileEnergy.super.pushEnergyToSide(side);
		}
	}
	
	public void onMultiblockRefresh() {
		for (EnumFacing side : EnumFacing.VALUES) {
			ignoreSide[side.getIndex()] = world.getTileEntity(pos.offset(side)) instanceof TileRTG;
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
		RTGMultiblock multiblock = getMultiblock();
		return multiblock != null ? multiblock.getEnergyStorage() : backupStorage;
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
	public boolean getIC2Reg() {
		return ic2reg;
	}
	
	@Override
	public void setIC2Reg(boolean ic2reg) {
		this.ic2reg = ic2reg;
	}
	
	@Override
	public int getSinkTier() {
		return 10;
	}
	
	@Override
	public int getSourceTier() {
		return EnergyHelper.getEUTier(power);
	}
	
	@Override
	@Optional.Method(modid = "ic2")
	public boolean emitsEnergyTo(IEnergyAcceptor receiver, EnumFacing side) {
		return ITileEnergy.super.emitsEnergyTo(receiver, side);
	}
	
	@Override
	@Optional.Method(modid = "ic2")
	public double getOfferedEnergy() {
		return ITileEnergy.super.getOfferedEnergy();
	}
	
	@Override
	@Optional.Method(modid = "ic2")
	public void drawEnergy(double amount) {
		ITileEnergy.super.drawEnergy(amount);
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
		
		
		if (rtgType == null) {
			nbt.setLong("power", power);
			nbt.setDouble("radiationRate", radiation);
		}
		else {
			nbt.setString("rtgType", rtgType);
		}
		
		return nbt;
	}
	
	@Override
	public void readAll(NBTTagCompound nbt) {
		super.readAll(nbt);
		readEnergyConnections(nbt);
		boolean[] arr = NCMath.bytesToBooleans(nbt.getByteArray("ignoreSide"));
		if (arr.length == 6) {
			ignoreSide = arr;
		}
		
		if (nbt.hasKey("power")) {
			power = nbt.getLong("power");
			if (nbt.hasKey("radiationRate")) {
				radiation = nbt.getDouble("radiationRate");
			}
		}
		else if (nbt.hasKey("rtgType")) {
			rtgType = nbt.getString("rtgType");
			
			if (DYN_POWER_MAP.containsKey(rtgType)) {
				power = DYN_POWER_MAP.getLong(rtgType);
			}
			if (DYN_RADIATION_MAP.containsKey(rtgType)) {
				radiation = DYN_RADIATION_MAP.getDouble(rtgType);
				getRadiationSource().setRadiationLevel(radiation);
			}
		}
	}
	
	// Capability
	
	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing side) {
		if (!ignoreSide(side) && (capability == CapabilityEnergy.ENERGY || (ModCheck.gregtechLoaded() && enable_gtce_eu && capability == GregtechCapabilities.CAPABILITY_ENERGY_CONTAINER))) {
			return hasEnergySideCapability(side);
		}
		return super.hasCapability(capability, side);
	}
	
	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing side) {
		if (!ignoreSide(side)) {
			if (capability == CapabilityEnergy.ENERGY) {
				if (hasEnergySideCapability(side)) {
					return CapabilityEnergy.ENERGY.cast(getEnergySide(nonNullSide(side)));
				}
				return null;
			}
			else if (ModCheck.gregtechLoaded() && capability == GregtechCapabilities.CAPABILITY_ENERGY_CONTAINER) {
				if (enable_gtce_eu && hasEnergySideCapability(side)) {
					return GregtechCapabilities.CAPABILITY_ENERGY_CONTAINER.cast(getEnergySideGT(nonNullSide(side)));
				}
				return null;
			}
		}
		return super.getCapability(capability, side);
	}
}
