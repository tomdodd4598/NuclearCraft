package nc.multiblock;

import nc.tile.multiblock.ITileLogicMultiblockPart;
import nc.tile.multiblock.TilePartAbstract.SyncReason;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nonnull;
import java.util.function.UnaryOperator;

public interface ILogicMultiblock<MULTIBLOCK extends Multiblock<MULTIBLOCK, T> & ILogicMultiblock<MULTIBLOCK, LOGIC, T>, LOGIC extends MultiblockLogic<MULTIBLOCK, LOGIC, T>, T extends ITileLogicMultiblockPart<MULTIBLOCK, LOGIC, T>> extends IMultiblock<MULTIBLOCK, T> {
	
	@Nonnull LOGIC getLogic();
	
	void setLogic(String logicID);
	
	default @Nonnull LOGIC getNewLogic(UnaryOperator<LOGIC> constructor) {
		return constructor.apply(getLogic());
	}
	
	default void writeLogicNBT(NBTTagCompound data, SyncReason syncReason) {
		data.setString("logicID", getLogic().getID());
		NBTTagCompound logicTag = new NBTTagCompound();
		getLogic().writeToLogicTag(logicTag, syncReason);
		data.setTag("logic", logicTag);
	}
	
	default void readLogicNBT(NBTTagCompound data, SyncReason syncReason) {
		if (syncReason == SyncReason.FullSync && data.hasKey("logicID")) {
			setLogic(data.getString("logicID"));
		}
		if (data.hasKey("logic")) {
			getLogic().readFromLogicTag(data.getCompoundTag("logic"), syncReason);
		}
	}
}
