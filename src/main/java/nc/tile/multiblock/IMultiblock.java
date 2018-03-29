package nc.tile.multiblock;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;

public interface IMultiblock {
	
	public MultiblockType getMultiblockType();
	
	// From Master
	
	public BlockPos getMasterPos();
	
	public void setMasterPos(BlockPos pos);
	
	public void deleteMasterPos();
	
	// To Master
	
	public void callForDisassembly();
	
	// NBT
	
	public NBTTagCompound writeMultiblockData(NBTTagCompound nbt);
	
	public void readMultiblockData(NBTTagCompound nbt);
}
