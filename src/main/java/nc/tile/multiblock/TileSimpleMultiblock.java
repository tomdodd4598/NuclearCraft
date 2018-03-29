package nc.tile.multiblock;

import nc.tile.NCTile;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.math.BlockPos;

public class TileSimpleMultiblock extends NCTile implements IMultiblock {
	
	BlockPos masterPos;
	final MultiblockType multiblockType;
	
	public TileSimpleMultiblock(MultiblockType type) {
		multiblockType = type;
	}
	
	@Override
	public MultiblockType getMultiblockType() {
		return multiblockType;
	}
	
	// From Master
	
	@Override
	public BlockPos getMasterPos() {
		return masterPos;
	}
	
	@Override
	public void setMasterPos(BlockPos pos) {
		masterPos = pos;
	}
	
	@Override
	public void deleteMasterPos() {
		masterPos = null;
	}
	
	// To Master
	
	@Override
	public void callForDisassembly() {
		if (masterPos != null) if (world.getTileEntity(masterPos) instanceof IMultiblockMaster) {
			IMultiblockMaster master = (IMultiblockMaster) world.getTileEntity(masterPos);
			master.disassemble();
		}
	}
	
	// NBT
	
	@Override
	public NBTTagCompound writeMultiblockData(NBTTagCompound nbt) {
		if (masterPos != null) nbt.setTag("masterPos", NBTUtil.createPosTag(masterPos));
		return nbt;
	}
	
	@Override
	public void readMultiblockData(NBTTagCompound nbt) {
		if (nbt.hasKey("masterPos", 10)) masterPos = NBTUtil.getPosFromTag(nbt.getCompoundTag("masterPos"));
	}
	
	@Override
	public NBTTagCompound writeAll(NBTTagCompound nbt) {
		super.writeAll(nbt);
		writeMultiblockData(nbt);
		return nbt;
	}
		
	@Override
	public void readAll(NBTTagCompound nbt) {
		super.readAll(nbt);
		readMultiblockData(nbt);
	}
}
