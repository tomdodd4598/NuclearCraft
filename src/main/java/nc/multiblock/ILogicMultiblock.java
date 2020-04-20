package nc.multiblock;

import java.lang.reflect.Constructor;

import javax.annotation.Nonnull;

import nc.multiblock.tile.ITileLogicMultiblockPart;
import nc.multiblock.tile.TileBeefAbstract.SyncReason;
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
}
