package nc.multiblock.battery;

import javax.annotation.Nonnull;

import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import nc.multiblock.Multiblock;
import nc.multiblock.battery.tile.TileBattery;
import nc.multiblock.network.MultiblockUpdatePacket;
import nc.multiblock.tile.ITileMultiblockPart;
import nc.multiblock.tile.TileBeefAbstract.SyncReason;
import nc.tile.internal.energy.EnergyStorage;
import nc.util.NCMath;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class BatteryMultiblock extends Multiblock<TileBattery, MultiblockUpdatePacket> {
	
public static final ObjectSet<Class<? extends TileBattery>> PART_CLASSES = new ObjectOpenHashSet<>();
	
	protected final PartSuperMap<TileBattery> partSuperMap = new PartSuperMap<>();
	
	protected final @Nonnull EnergyStorage storage = new EnergyStorage(1);
	protected int comparatorStrength = 0;
	
	protected boolean refreshEnergy = false;
	
	public BatteryMultiblock(World world) {
		super(world);
		for (Class<? extends TileBattery> clazz : PART_CLASSES) {
			partSuperMap.equip(clazz);
		}
	}
	
	@Override
	public PartSuperMap<TileBattery> getPartSuperMap() {
		return partSuperMap;
	}
	
	public @Nonnull EnergyStorage getEnergyStorage() {
		return storage;
	}
	
	@Override
	public void onAttachedPartWithMultiblockData(ITileMultiblockPart part, NBTTagCompound data) {
		syncDataFrom(data, SyncReason.FullSync);
	}
	
	@Override
	protected void onBlockAdded(ITileMultiblockPart newPart) {
		onPartAdded(newPart);
	}
	
	@Override
	protected void onBlockRemoved(ITileMultiblockPart oldPart) {
		onPartRemoved(oldPart);
	}
	
	@Override
	protected void onMachineAssembled(boolean alreadyAssembled) {
		refreshMultiblock();
	}
	
	@Override
	protected void onMachineRestored() {
		refreshMultiblock();
	}
	
	protected void refreshMultiblock() {
		if (!WORLD.isRemote) {
			long capacity = 0L;
			for (TileBattery battery : getPartMap(TileBattery.class).values()) {
				capacity += battery.capacity;
				battery.onMultiblockRefresh();
			}
			storage.setStorageCapacity(capacity);
			storage.setMaxTransfer(NCMath.toInt(capacity));
			refreshEnergy = true;
		}
	}
	
	@Override
	protected void onMachinePaused() {}
	
	@Override
	protected void onMachineDisassembled() {}
	
	@Override
	protected int getMinimumNumberOfBlocksForAssembledMachine() {
		return 1;
	}
	
	@Override
	protected int getMaximumXSize() {
		return Integer.MAX_VALUE;
	}
	
	@Override
	protected int getMaximumZSize() {
		return Integer.MAX_VALUE;
	}
	
	@Override
	protected int getMaximumYSize() {
		return Integer.MAX_VALUE;
	}
	
	@Override
	protected boolean isMachineWhole(Multiblock multiblock) {
		return true;
	}
	
	@Override
	protected void onAssimilate(Multiblock assimilated) {
		refreshMultiblock();
		if (assimilated instanceof BatteryMultiblock) {
			storage.mergeEnergyStorage(((BatteryMultiblock)assimilated).storage);
		}
	}
	
	@Override
	protected void onAssimilated(Multiblock assimilator) {}
	
	@Override
	protected boolean updateServer() {
		if (refreshEnergy) {
			storage.cullEnergyStored();
			refreshEnergy = false;
		}
		
		boolean shouldUpdate = false;
		int compStrength = getComparatorStrength();
		if (comparatorStrength != compStrength) {
			shouldUpdate = true;
		}
		comparatorStrength = compStrength;
		if (shouldUpdate) {
			for (TileBattery battery : getPartMap(TileBattery.class).values()) {
				battery.markDirty();
			}
		}
		return shouldUpdate;
	}
	
	public int getComparatorStrength() {
		return NCMath.getComparatorSignal(storage.getEnergyStoredLong(), storage.getMaxEnergyStoredLong(), 0D);
	}
	
	@Override
	protected void updateClient() {}
	
	@Override
	protected boolean isBlockGoodForInterior(World world, int x, int y, int z, Multiblock multiblock) {
		return true;
	}
	
	@Override
	public void syncDataFrom(NBTTagCompound data, SyncReason syncReason) {
		readEnergy(storage, data, "energyStorage");
		comparatorStrength = data.getInteger("comparatorStrength");
	}
	
	@Override
	public void syncDataTo(NBTTagCompound data, SyncReason syncReason) {
		writeEnergy(storage, data, "energyStorage");
		data.setInteger("comparatorStrength", comparatorStrength);
	}
	
	@Override
	protected MultiblockUpdatePacket getUpdatePacket() {
		return null;
	}
	
	@Override
	public void onPacket(MultiblockUpdatePacket message) {}
}
