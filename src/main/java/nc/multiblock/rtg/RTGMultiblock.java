package nc.multiblock.rtg;

import javax.annotation.Nonnull;

import it.unimi.dsi.fastutil.objects.*;
import nc.multiblock.Multiblock;
import nc.multiblock.rtg.tile.TileRTG;
import nc.multiblock.tile.TileBeefAbstract.SyncReason;
import nc.tile.internal.energy.EnergyStorage;
import nc.util.NCMath;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class RTGMultiblock extends Multiblock<RTGMultiblock, TileRTG> {
	
	public static final ObjectSet<Class<? extends TileRTG>> PART_CLASSES = new ObjectOpenHashSet<>();
	
	protected final PartSuperMap<RTGMultiblock, TileRTG> partSuperMap = new PartSuperMap<>();
	
	protected final @Nonnull EnergyStorage storage = new EnergyStorage(1);
	protected long power = 0L;
	
	protected boolean refreshEnergy = false;
	
	public RTGMultiblock(World world) {
		super(world, RTGMultiblock.class, TileRTG.class);
		for (Class<? extends TileRTG> clazz : PART_CLASSES) {
			partSuperMap.equip(clazz);
		}
	}
	
	@Override
	public PartSuperMap<RTGMultiblock, TileRTG> getPartSuperMap() {
		return partSuperMap;
	}
	
	public @Nonnull EnergyStorage getEnergyStorage() {
		return storage;
	}
	
	@Override
	public void onAttachedPartWithMultiblockData(TileRTG part, NBTTagCompound data) {
		syncDataFrom(data, SyncReason.FullSync);
	}
	
	@Override
	protected void onBlockAdded(TileRTG newPart) {
		onPartAdded(newPart);
	}
	
	@Override
	protected void onBlockRemoved(TileRTG oldPart) {
		onPartRemoved(oldPart);
	}
	
	@Override
	protected void onMachineAssembled() {
		onMultiblockFormed();
	}
	
	@Override
	protected void onMachineRestored() {
		onMultiblockFormed();
	}
	
	protected void onMultiblockFormed() {
		if (!WORLD.isRemote) {
			long powerSum = 0L;
			for (TileRTG rtg : getParts(TileRTG.class)) {
				powerSum += rtg.power;
				rtg.onMultiblockRefresh();
			}
			this.power = powerSum;
			storage.setStorageCapacity(4 * powerSum);
			storage.setMaxTransfer(NCMath.toInt(4 * powerSum));
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
	protected boolean isMachineWhole() {
		return true;
	}
	
	@Override
	protected void onAssimilate(RTGMultiblock assimilated) {
		storage.mergeEnergyStorage(assimilated.storage);
		
		/*if (isAssembled()) {
			onMultiblockFormed();
		}*/
	}
	
	@Override
	protected void onAssimilated(RTGMultiblock assimilator) {}
	
	@Override
	protected boolean updateServer() {
		if (refreshEnergy) {
			storage.cullEnergyStored();
			refreshEnergy = false;
		}
		
		getEnergyStorage().changeEnergyStored(power);
		return false;
	}
	
	@Override
	protected void updateClient() {}
	
	@Override
	protected boolean isBlockGoodForInterior(World world, BlockPos pos) {
		return true;
	}
	
	@Override
	public void syncDataFrom(NBTTagCompound data, SyncReason syncReason) {
		readEnergy(storage, data, "energyStorage");
	}
	
	@Override
	public void syncDataTo(NBTTagCompound data, SyncReason syncReason) {
		writeEnergy(storage, data, "energyStorage");
	}
}
