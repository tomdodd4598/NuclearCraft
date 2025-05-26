package nc.tile.battery;

import gregtech.api.capability.GregtechCapabilities;
import ic2.api.energy.tile.*;
import it.unimi.dsi.fastutil.objects.*;
import nc.ModCheck;
import nc.block.battery.*;
import nc.multiblock.battery.BatteryMultiblock;
import nc.tile.dummy.IInterfaceable;
import nc.tile.energy.ITileEnergy;
import nc.tile.internal.energy.*;
import nc.tile.multiblock.TileMultiblockPart;
import nc.util.*;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.util.text.*;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fml.common.Optional;

import javax.annotation.*;

import static nc.config.NCConfig.enable_gtce_eu;

@Optional.InterfaceList({@Optional.Interface(iface = "ic2.api.energy.tile.IEnergySink", modid = "ic2"), @Optional.Interface(iface = "ic2.api.energy.tile.IEnergySource", modid = "ic2")})
public class TileBattery extends TileMultiblockPart<BatteryMultiblock, TileBattery> implements ITickable, ITileEnergy, IEnergySink, IEnergySource, IInterfaceable {
	
	public static final Object2LongMap<String> DYN_CAPACITY_MAP = new Object2LongOpenHashMap<>();
	public static final Object2IntMap<String> DYN_ENERGY_TIER_MAP = new Object2IntOpenHashMap<>();
	
	public static class VoltaicPileBasic extends TileBattery {
		
		public VoltaicPileBasic() {
			super(BatteryBlockType.VOLTAIC_PILE_BASIC);
		}
	}
	
	public static class VoltaicPileAdvanced extends TileBattery {
		
		public VoltaicPileAdvanced() {
			super(BatteryBlockType.VOLTAIC_PILE_ADVANCED);
		}
	}
	
	public static class VoltaicPileDU extends TileBattery {
		
		public VoltaicPileDU() {
			super(BatteryBlockType.VOLTAIC_PILE_DU);
		}
	}
	
	public static class VoltaicPileElite extends TileBattery {
		
		public VoltaicPileElite() {
			super(BatteryBlockType.VOLTAIC_PILE_ELITE);
		}
	}
	
	public static class LithiumIonBatteryBasic extends TileBattery {
		
		public LithiumIonBatteryBasic() {
			super(BatteryBlockType.LITHIUM_ION_BATTERY_BASIC);
		}
	}
	
	public static class LithiumIonBatteryAdvanced extends TileBattery {
		
		public LithiumIonBatteryAdvanced() {
			super(BatteryBlockType.LITHIUM_ION_BATTERY_ADVANCED);
		}
	}
	
	public static class LithiumIonBatteryDU extends TileBattery {
		
		public LithiumIonBatteryDU() {
			super(BatteryBlockType.LITHIUM_ION_BATTERY_DU);
		}
	}
	
	public static class LithiumIonBatteryElite extends TileBattery {
		
		public LithiumIonBatteryElite() {
			super(BatteryBlockType.LITHIUM_ION_BATTERY_ELITE);
		}
	}
	
	protected String batteryType;
	
	protected final EnergyStorage backupStorage = new EnergyStorage(0L);
	
	protected @Nonnull
	final EnergyConnection[] energyConnections;
	protected boolean[] ignoreSide = new boolean[] {false, false, false, false, false, false};
	
	protected @Nonnull
	final EnergyTileWrapper[] energySides;
	protected @Nonnull
	final EnergyTileWrapperGT[] energySidesGT;
	
	protected boolean ic2reg = false;
	
	public long waitingEnergy = 0L;
	public long capacity;
	protected int energyTier;
	
	/**
	 * Don't use this constructor!
	 */
	public TileBattery() {
		super(BatteryMultiblock.class, TileBattery.class);
		energyConnections = ITileEnergy.energyConnectionAll(EnergyConnection.IN);
		energySides = ITileEnergy.getDefaultEnergySides(this);
		energySidesGT = ITileEnergy.getDefaultEnergySidesGT(this);
	}
	
	public TileBattery(String batteryType, long capacity, int energyTier) {
		this();
		this.batteryType = batteryType;
		this.capacity = capacity;
		this.energyTier = energyTier;
	}
	
	protected TileBattery(IBatteryBlockType type) {
		this(type.toString(), type.getCapacity(), type.getEnergyTier());
	}
	
	protected boolean ignoreSide(EnumFacing side) {
		return side != null && ignoreSide[side.getIndex()];
	}
	
	@Override
	public void onMachineAssembled(BatteryMultiblock multiblock) {
		doStandardNullControllerResponse(multiblock);
	}
	
	@Override
	public void onMachineBroken() {}
	
	@Override
	public BatteryMultiblock createNewMultiblock() {
		return new BatteryMultiblock(world);
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
			if (waitingEnergy != 0L && getMultiblock() != null) {
				getEnergyStorage().changeEnergyStored(waitingEnergy);
				waitingEnergy = 0L;
			}
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
			ignoreSide[side.getIndex()] = world.getTileEntity(pos.offset(side)) instanceof TileBattery;
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
		BatteryMultiblock multiblock = getMultiblock();
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
		return energyTier;
	}
	
	@Override
	public int getSourceTier() {
		return energyTier;
	}
	
	@Override
	@Optional.Method(modid = "ic2")
	public boolean acceptsEnergyFrom(IEnergyEmitter emitter, EnumFacing side) {
		return ITileEnergy.super.acceptsEnergyFrom(emitter, side);
	}
	
	@Override
	@Optional.Method(modid = "ic2")
	public double getDemandedEnergy() {
		return ITileEnergy.super.getDemandedEnergy();
	}
	
	@Override
	@Optional.Method(modid = "ic2")
	public double injectEnergy(EnumFacing directionFrom, double amount, double voltage) {
		return ITileEnergy.super.injectEnergy(directionFrom, amount, voltage);
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
	
	// IMultitoolLogic
	
	@Override
	public boolean onUseMultitool(ItemStack multitool, EntityPlayerMP player, World world, EnumFacing facing, float hitX, float hitY, float hitZ) {
		boolean opposite = player.isSneaking();
		EnumFacing side = opposite ? facing.getOpposite() : facing;
		toggleEnergyConnection(side, EnergyConnection.Type.DEFAULT);
		EnergyConnection energyConnection = getEnergyConnection(side);
		player.sendMessage(new TextComponentString(Lang.localize(opposite ? "nc.block.energy_toggle_opposite" : "nc.block.energy_toggle") + " " + energyConnection.getTextColor() + Lang.localize("nc.block.setting." + energyConnection.getName())));
		return true;
	}
	
	// NBT
	
	@Override
	public NBTTagCompound writeAll(NBTTagCompound nbt) {
		super.writeAll(nbt);
		writeEnergyConnections(nbt);
		nbt.setLong("waitingEnergy", waitingEnergy);
		
		if (batteryType == null) {
			nbt.setLong("capacity", capacity);
			nbt.setInteger("energyTier", energyTier);
		}
		else {
			nbt.setString("batteryType", batteryType);
		}
		
		nbt.setByteArray("ignoreSide", NCMath.booleansToBytes(ignoreSide));
		return nbt;
	}
	
	@Override
	public void readAll(NBTTagCompound nbt) {
		super.readAll(nbt);
		readEnergyConnections(nbt);
		waitingEnergy = nbt.getLong("waitingEnergy");
		
		if (nbt.hasKey("capacity") && nbt.hasKey("energyTier")) {
			capacity = nbt.getLong("capacity");
			energyTier = nbt.getInteger("energyTier");
		}
		else if (nbt.hasKey("batteryType")) {
			batteryType = nbt.getString("batteryType");
			
			if (DYN_CAPACITY_MAP.containsKey(batteryType)) {
				capacity = DYN_CAPACITY_MAP.getLong(batteryType);
			}
			if (DYN_ENERGY_TIER_MAP.containsKey(batteryType)) {
				energyTier = DYN_ENERGY_TIER_MAP.getInt(batteryType);
			}
		}
		
		boolean[] arr = NCMath.bytesToBooleans(nbt.getByteArray("ignoreSide"));
		if (arr.length == 6) {
			ignoreSide = arr;
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
