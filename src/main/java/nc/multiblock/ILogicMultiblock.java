package nc.multiblock;

import java.lang.reflect.Constructor;
import java.util.Map.Entry;

import javax.annotation.Nonnull;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import nc.multiblock.TileBeefBase.SyncReason;
import nc.tile.fluid.ITileFluid;
import nc.tile.inventory.ITileInventory;
import nc.util.SuperMap;
import nc.util.SuperMap.SuperMapEntry;
import net.minecraft.nbt.NBTTagCompound;

public interface ILogicMultiblock<LOGIC extends MultiblockLogic, T extends ITileLogicMultiblockPart> {
	
	public @Nonnull LOGIC getLogic();
	
	public void setLogic(String logicID);
	
	public default @Nonnull LOGIC getNewLogic(Constructor<? extends LOGIC> constructor) {
		try {
			return constructor.newInstance(getLogic());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return getLogic();
	}
	
	public static class PartSuperMap<T extends ITileMultiblockPart> extends SuperMap<Long, T, Long2ObjectMap<? extends T>> {
		
		@Override
		public <TYPE extends T> Long2ObjectMap<? extends T> backup(Class<TYPE> clazz) {
			return new Long2ObjectOpenHashMap<>();
		}
	}
	
	public SuperMap<Long, T, Long2ObjectMap<? extends T>> getPartSuperMap();
	
	public default <TYPE extends T> Long2ObjectMap<TYPE> getPartMap(Class<TYPE> type) {
		return getPartSuperMap().get(type);
	}
	
	public default void onPartAdded(ITileMultiblockPart newPart) {
		for (Entry<Class<? extends T>, Long2ObjectMap<? extends T>> superMapEntryRaw : getPartSuperMap().entrySet()) {
			addBlockForSuperMapEntry(new SuperMapEntry(superMapEntryRaw), newPart);
		}
	}
	
	public default <TYPE extends T> void addBlockForSuperMapEntry(SuperMapEntry<Long, TYPE> superMapEntry, ITileMultiblockPart newPart) {
		if (superMapEntry.getKey().isInstance(newPart)) {
			superMapEntry.getValue().put(newPart.getTilePos().toLong(), superMapEntry.getKey().cast(newPart));
		}
	}
	
	public default void onPartRemoved(ITileMultiblockPart oldPart) {
		for (Entry<Class<? extends T>, Long2ObjectMap<? extends T>> superMapEntryRaw : getPartSuperMap().entrySet()) {
			removeBlockForSuperMapEntry(new SuperMapEntry(superMapEntryRaw), oldPart);
		}
	}
	
	public default <TYPE extends T> void removeBlockForSuperMapEntry(SuperMapEntry<Long, TYPE> superMapEntry, ITileMultiblockPart oldPart) {
		if (superMapEntry.getKey().isInstance(oldPart)) {
			superMapEntry.getValue().remove(oldPart.getTilePos().toLong());
		}
	}
	
	public default void writeLogicNBT(NBTTagCompound data, SyncReason syncReason) {
		data.setString("logicID", getLogic().getID());
		NBTTagCompound logicTag = new NBTTagCompound();
		getLogic().writeToLogicTag(logicTag, syncReason);
		data.setTag("logic", logicTag);
	}
	
	public default void readLogicNBT(NBTTagCompound data, SyncReason syncReason) {
		if (syncReason == SyncReason.FullSync && data.hasKey("logicID")) {
			setLogic(data.getString("logicID"));
		}
		if (data.hasKey("logic")) {
			getLogic().readFromLogicTag(data.getCompoundTag("logic"), syncReason);
		}
	}
	
	public default void clearAllMaterial() {
		for (Entry<Class<? extends T>, Long2ObjectMap<? extends T>> superMapEntryRaw : getPartSuperMap().entrySet()) {
			for (T tile : superMapEntryRaw.getValue().values()) {
				if (tile instanceof ITileInventory) {
					((ITileInventory)tile).clearAllSlots();
				}
				if (tile instanceof ITileFluid) {
					((ITileFluid)tile).clearAllTanks();
				}
			}
		}
	}
}
