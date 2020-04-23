package nc.multiblock.rtg;

import javax.annotation.Nonnull;

import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import nc.multiblock.Multiblock;
import nc.multiblock.network.MultiblockUpdatePacket;
import nc.multiblock.rtg.tile.TileRTG;
import nc.multiblock.tile.ITileMultiblockPart;
import nc.multiblock.tile.TileBeefAbstract.SyncReason;
import nc.tile.internal.energy.EnergyStorage;
import nc.util.NCMath;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class RTGMultiblock extends Multiblock<TileRTG, MultiblockUpdatePacket> {
	
public static final ObjectSet<Class<? extends TileRTG>> PART_CLASSES = new ObjectOpenHashSet<>();
	
	protected final PartSuperMap<TileRTG> partSuperMap = new PartSuperMap<>();
	
	protected final @Nonnull EnergyStorage storage = new EnergyStorage(1);
	protected long power = 0L;
	
	protected boolean refreshEnergy = false;
	
	public RTGMultiblock(World world) {
		super(world);
		for (Class<? extends TileRTG> clazz : PART_CLASSES) {
			partSuperMap.equip(clazz);
		}
	}
	
	@Override
	public PartSuperMap<TileRTG> getPartSuperMap() {
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
	protected void onMachineAssembled(boolean wasAssembled) {
		refreshMultiblock();
	}
	
	@Override
	protected void onMachineRestored() {
		refreshMultiblock();
	}
	
	protected void refreshMultiblock() {
		if (!WORLD.isRemote) {
			long power = 0L;
			for (TileRTG rtg : getPartMap(TileRTG.class).values()) {
				power += rtg.power;
				rtg.onMultiblockRefresh();
			}
			this.power = power;
			storage.setStorageCapacity(4*power);
			storage.setMaxTransfer(NCMath.toInt(4*power));
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
		if (assimilated instanceof RTGMultiblock) {
			storage.mergeEnergyStorage(((RTGMultiblock)assimilated).storage);
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
		
		getEnergyStorage().changeEnergyStored(power);
		return false;
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
	}
	
	@Override
	public void syncDataTo(NBTTagCompound data, SyncReason syncReason) {
		writeEnergy(storage, data, "energyStorage");
	}
	
	@Override
	protected MultiblockUpdatePacket getUpdatePacket() {
		return null;
	}
	
	@Override
	public void onPacket(MultiblockUpdatePacket message) {}
}
