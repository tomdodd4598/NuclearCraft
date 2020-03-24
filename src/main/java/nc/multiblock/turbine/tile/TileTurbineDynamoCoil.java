package nc.multiblock.turbine.tile;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import gregtech.api.capability.GregtechCapabilities;
import gregtech.api.capability.IEnergyContainer;
import ic2.api.energy.EnergyNet;
import ic2.api.energy.tile.IEnergyAcceptor;
import ic2.api.energy.tile.IEnergySink;
import ic2.api.energy.tile.IEnergySource;
import nc.ModCheck;
import nc.config.NCConfig;
import nc.multiblock.cuboidal.CuboidalPartPositionType;
import nc.multiblock.turbine.TurbineDynamoCoilType;
import nc.multiblock.turbine.Turbine;
import nc.tile.energy.ITileEnergy;
import nc.tile.internal.energy.EnergyConnection;
import nc.tile.internal.energy.EnergyStorage;
import nc.tile.internal.energy.EnergyTileWrapper;
import nc.tile.internal.energy.EnergyTileWrapperGT;
import nc.tile.passive.ITilePassive;
import nc.util.BlockPosHelper;
import nc.util.EnergyHelper;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fml.common.Optional;

@Optional.InterfaceList({@Optional.Interface(iface = "ic2.api.energy.tile.IEnergyTile", modid = "ic2"), @Optional.Interface(iface = "ic2.api.energy.tile.IEnergySink", modid = "ic2"), @Optional.Interface(iface = "ic2.api.energy.tile.IEnergySource", modid = "ic2")})
public class TileTurbineDynamoCoil extends TileTurbinePartBase implements ITileEnergy, IEnergySource {
	
	private final EnergyStorage backupStorage = new EnergyStorage(1);
	
	private final EnergyConnection[] energyConnections = ITileEnergy.energyConnectionAll(EnergyConnection.OUT);
	
	private final EnergyTileWrapper[] energySides = ITileEnergy.getDefaultEnergySides(this);
	private final EnergyTileWrapperGT[] energySidesGT = ITileEnergy.getDefaultEnergySidesGT(this);
	
	private boolean ic2reg = false;
	
	public final TurbineDynamoCoilType coilType;
	public boolean checked = false, isInValidPosition;
	
	public static class Magnesium extends TileTurbineDynamoCoil {
		
		public Magnesium() {
			super(TurbineDynamoCoilType.MAGNESIUM);
		}
	}
	
	public static class Beryllium extends TileTurbineDynamoCoil {
		
		public Beryllium() {
			super(TurbineDynamoCoilType.BERYLLIUM);
		}
	}
	
	public static class Aluminum extends TileTurbineDynamoCoil {
		
		public Aluminum() {
			super(TurbineDynamoCoilType.ALUMINUM);
		}
	}
	
	public static class Gold extends TileTurbineDynamoCoil {
		
		public Gold() {
			super(TurbineDynamoCoilType.GOLD);
		}
	}
	
	public static class Copper extends TileTurbineDynamoCoil {
		
		public Copper() {
			super(TurbineDynamoCoilType.COPPER);
		}
	}
	
	public static class Silver extends TileTurbineDynamoCoil {
		
		public Silver() {
			super(TurbineDynamoCoilType.SILVER);
		}
	}
	
	private TileTurbineDynamoCoil(TurbineDynamoCoilType coilType) {
		super(CuboidalPartPositionType.WALL);
		
		this.coilType = coilType;
	}
	
	@Override
	public void onMachineAssembled(Turbine controller) {
		doStandardNullControllerResponse(controller);
		super.onMachineAssembled(controller);
		if (getWorld().isRemote) return;
	}
	
	@Override
	public void onMachineBroken() {
		super.onMachineBroken();
		if (getWorld().isRemote) return;
		//getWorld().setBlockState(getPos(), getWorld().getBlockState(getPos()), 2);
	}
	
	public double contributeConductivity(int dynamoCoilCheckCount) {
		EnumFacing flowDir = getMultiblock().flowDir;
		
		if (!isMultiblockAssembled() || flowDir == null) {
			isInValidPosition = false;
			checked = true;
			return 0D;
		}
		
		switch (coilType) {
		case MAGNESIUM: {
			if (dynamoCoilCheckCount != 0) return 0D;
			for (EnumFacing dir : BlockPosHelper.getHorizontals(flowDir)) {
				if (isRotorBearing(dir)) {
					isInValidPosition = true;
					checked = true;
					return coilType.getConductivity();
				}
			}
			isInValidPosition = false;
			checked = true;
			return 0D;
		}
			
		case BERYLLIUM: {
			if (dynamoCoilCheckCount != 1) return 0D;
			for (EnumFacing dir : BlockPosHelper.getHorizontals(flowDir)) {
				if (isDynamoCoil(dir, TurbineDynamoCoilType.MAGNESIUM)) {
					isInValidPosition = true;
					checked = true;
					return coilType.getConductivity();
				}
			}
			isInValidPosition = false;
			checked = true;
			return 0D;
		}
			
		case ALUMINUM: {
			if (dynamoCoilCheckCount != 4) return 0D;
			for (EnumFacing dir : BlockPosHelper.getHorizontals(flowDir)) {
				if (isDynamoCoilExcluding(dir, TurbineDynamoCoilType.ALUMINUM)) {
					isInValidPosition = true;
					checked = true;
					return coilType.getConductivity();
				}
			}
			isInValidPosition = false;
			checked = true;
			return 0D;
		}
			
		case GOLD: {
			if (dynamoCoilCheckCount != 2) return 0D;
			for (EnumFacing dir : BlockPosHelper.getHorizontals(flowDir)) {
				if (isDynamoCoil(dir, TurbineDynamoCoilType.BERYLLIUM)) {
					isInValidPosition = true;
					checked = true;
					return coilType.getConductivity();
				}
			}
			isInValidPosition = false;
			checked = true;
			return 0D;
		}
			
		case COPPER: {
			if (dynamoCoilCheckCount != 3) return 0D;
			for (EnumFacing dir : BlockPosHelper.getHorizontals(flowDir)) {
				if (isDynamoCoil(dir, TurbineDynamoCoilType.GOLD)) {
					isInValidPosition = true;
					checked = true;
					return coilType.getConductivity();
				}
			}
			isInValidPosition = false;
			checked = true;
			return 0D;
		}
			
		case SILVER: {
			if (dynamoCoilCheckCount != 3) return 0D;
			boolean magnesium = false;
			boolean gold = false;
			for (EnumFacing dir : BlockPosHelper.getHorizontals(flowDir)) {
				if (!magnesium) if (isDynamoCoil(dir, TurbineDynamoCoilType.MAGNESIUM)) magnesium = true;
				if (!gold) if (isDynamoCoil(dir, TurbineDynamoCoilType.GOLD)) gold = true;
				if (magnesium && gold) {
					isInValidPosition = true;
					checked = true;
					return coilType.getConductivity();
				}
			}
			isInValidPosition = false;
			checked = true;
			return 0D;
		}
			
		default: {
			isInValidPosition = false;
			return 0D;
		}
		}
	}
	
	private boolean isRotorBearing(EnumFacing dir) {
		return world.getTileEntity(pos.offset(dir)) instanceof TileTurbineRotorBearing;
	}
	
	private boolean isDynamoCoil(EnumFacing dir, TurbineDynamoCoilType coilType) {
		TileEntity tile = world.getTileEntity(pos.offset(dir));
		if (!(tile instanceof TileTurbineDynamoCoil)) return false;
		TileTurbineDynamoCoil dynamoCoil = (TileTurbineDynamoCoil) tile;
		return dynamoCoil.isInValidPosition && dynamoCoil.coilType == coilType;
	}
	
	private boolean isDynamoCoilExcluding(EnumFacing dir, TurbineDynamoCoilType coilType) {
		TileEntity tile = world.getTileEntity(pos.offset(dir));
		if (!(tile instanceof TileTurbineDynamoCoil)) return false;
		TileTurbineDynamoCoil dynamoCoil = (TileTurbineDynamoCoil) tile;
		return dynamoCoil.isInValidPosition && dynamoCoil.coilType != coilType;
	}
	
	@Override
	public void update() {
		super.update();
		if(!world.isRemote) {
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
	public @Nonnull EnergyConnection[] getEnergyConnections() {
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
		if (!getEnergyConnection(side).canExtract()) return;
		
		TileEntity tile = getTileWorld().getTileEntity(getTilePos().offset(side));
		if (tile == null || tile instanceof TileTurbinePartBase) return;
		
		if (tile instanceof ITileEnergy) if (!((ITileEnergy) tile).getEnergyConnection(side.getOpposite()).canReceive()) return;
		if (tile instanceof ITilePassive) if (!((ITilePassive) tile).canPushEnergyTo()) return;
		
		IEnergyStorage adjStorage = tile.getCapability(CapabilityEnergy.ENERGY, side.getOpposite());
		
		if (adjStorage != null && getEnergyStorage().canExtract()) {
			getEnergyStorage().extractEnergy(adjStorage.receiveEnergy(getEnergyStorage().extractEnergy(getEnergyStorage().getMaxEnergyStored(), true), false), false);
			return;
		}
		
		if (getEnergyStorage().getEnergyStored() < NCConfig.rf_per_eu) return;
		
		if (ModCheck.ic2Loaded()) {
			if (tile instanceof IEnergySink) {
				getEnergyStorage().extractEnergy((int) Math.round(((IEnergySink) tile).injectEnergy(side.getOpposite(), getEnergyStorage().extractEnergy(getEnergyStorage().getMaxEnergyStored(), true)/NCConfig.rf_per_eu, getEUSourceTier())*NCConfig.rf_per_eu), false);
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
