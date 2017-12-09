package nc.tile;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

public abstract class NCTile extends TileEntity implements ITickable {
	
	public boolean isAdded;
	public boolean isMarkedDirty;
	
	public NCTile() {
		super();
	}
	
	public void update() {
		if (!isAdded) {
			onAdded();
			isAdded = true;
		}
		if (isMarkedDirty) {
			markDirty();
			isMarkedDirty = false;
		}
	}
	
	public void onAdded() {
		if (world.isRemote) {
			getWorld().markBlockRangeForRenderUpdate(pos, pos);
			getWorld().getChunkFromBlockCoords(getPos()).setChunkModified();
		}
		markDirty();
	}
	
	public ITextComponent getDisplayName() {
		if (getBlockType() != null) return new TextComponentTranslation(getBlockType().getLocalizedName()); else return null;
	}
	
	// NBT
	
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		writeAll(nbt);
		return nbt;
	}
	
	public NBTTagCompound writeAll(NBTTagCompound nbt) {
		return nbt;
	}
	
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		readAll(nbt);
	}
	
	public void readAll(NBTTagCompound nbt) {}
	
	/*public NBTTagCompound getTileData() {
		NBTTagCompound nbt = new NBTTagCompound();
		writeToNBT(nbt);
		return nbt;
	}*/
	
	public SPacketUpdateTileEntity getUpdatePacket() {
		NBTTagCompound nbt = new NBTTagCompound();
		writeToNBT(nbt);
		int metadata = getBlockMetadata();
		return new SPacketUpdateTileEntity(pos, metadata, nbt);
	}

	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet) {
		readFromNBT(packet.getNbtCompound());
	}

	public NBTTagCompound getUpdateTag() {
		NBTTagCompound nbt = new NBTTagCompound();
		writeToNBT(nbt);
		return nbt;
	}

	public void handleUpdateTag(NBTTagCompound tag) {
		super.handleUpdateTag(tag);
	}
}
