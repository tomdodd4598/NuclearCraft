package nc.multiblock;

import javax.annotation.Nullable;

import nc.block.tile.IActivatable;
import nc.capability.radiation.source.IRadiationSource;
import nc.capability.radiation.source.RadiationSource;
import nc.tile.ITile;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;

/**
 * A base class for modded tile entities
 *
 * Partially ported from TileCoFHBase
 * https://github.com/CoFH/CoFHCore/blob/master/src/main/java/cofh/core/block/TileCoFHBase.java
 */
public abstract class TileBeefBase extends TileEntity implements ITile, ITickable {
	
	public boolean isAdded = false, isMarkedDirty = false;
	private boolean isRedstonePowered = false, alternateComparator = false, redstoneControl = false;
	
	private IRadiationSource radiation;
	
	public TileBeefBase() {
		super();
		radiation = new RadiationSource(0D);
	}
	
	@Override
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
			world.markBlockRangeForRenderUpdate(pos, pos);
			world.getChunk(pos).markDirty();
			refreshIsRedstonePowered(world, pos);
			markDirty();
		}
	}
	
	@Override
	public World getTileWorld() {
		return world;
	}
	
	@Override
	public BlockPos getTilePos() {
		return pos;
	}
	
	@Override
	public Block getTileBlockType() {
		return getBlockType();
	}

	@Override
	public int getTileBlockMeta() {
		return getBlockMetadata();
	}
	
	@Override
	public IRadiationSource getRadiationSource() {
		return radiation;
	}
	
	public boolean isUseableByPlayer(EntityPlayer entityplayer) {
		BlockPos position = pos;
		
		if (world.getTileEntity(position) != this) return false;
		
		return entityplayer.getDistanceSq(position.getX() + 0.5D, position.getY() + 0.5D, position.getZ() + 0.5D) <= 64D;
	}
	
	@Override
	public void setState(boolean isActive, TileEntity tile) {
		if (getBlockType() instanceof IActivatable) {
			((IActivatable)getBlockType()).setState(isActive, tile);
		}
	}
	
	@Override
	public void markDirtyAndNotify() {
		markDirty();
		world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 3);
		world.notifyNeighborsOfStateChange(pos, getBlockType(), true);
	}
	
	/** Never override this! */
	@Override
	public void markTileDirty() {
		markDirty();
	}
	
	// Redstone
	
	@Override
	public boolean getIsRedstonePowered() {
		return isRedstonePowered;
	}
	
	@Override
	public void setIsRedstonePowered(boolean isRedstonePowered) {
		this.isRedstonePowered = isRedstonePowered;
	}
	
	@Override
	public boolean getAlternateComparator() {
		return alternateComparator;
	}
	
	@Override
	public void setAlternateComparator(boolean alternate) {
		alternateComparator = alternate;
	}
	
	@Override
	public boolean getRedstoneControl() {
		return redstoneControl;
	}
	
	@Override
	public void setRedstoneControl(boolean redstoneControl) {
		this.redstoneControl = redstoneControl;
	}
	
	// Capabilities
	
	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing side) {
		if (capability == IRadiationSource.CAPABILITY_RADIATION_SOURCE) {
			return radiation != null;
		}
		return super.hasCapability(capability, side);
	}
	
	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing side) {
		if (capability == IRadiationSource.CAPABILITY_RADIATION_SOURCE) {
			return (T) radiation;
		}
		return super.getCapability(capability, side);
	}
	
	/*
	GUI management
	 */
	
	/**
	 * Check if the tile entity has a GUI or not
	 * Override in derived classes to return true if your tile entity got a GUI
	 */
	public boolean canOpenGui(World world, BlockPos posistion, IBlockState state) {
		return false;
	}
	
	/**
	 * Open the specified GUI
	 *
	 * @param player the player currently interacting with your block/tile entity
	 * @param guiId the GUI to open
	 * @return true if the GUI was opened, false otherwise
	 */
	public boolean openGui(Object mod, EntityPlayer player, int guiId) {

		player.openGui(mod, guiId, world, pos.getX(), pos.getY(), pos.getZ());
		return true;
	}
	
	/**
	 * Returns a Server side Container to be displayed to the user.
	 *
	 * @param guiId the GUI ID mumber
	 * @param player the player currently interacting with your block/tile entity
	 * @return A GuiScreen/Container to be displayed to the user, null if none.
	 */
	public Object getServerGuiElement(int guiId, EntityPlayer player) {
		return null;
	}
	
	/**
	 * Returns a Container to be displayed to the user. On the client side, this
	 * needs to return a instance of GuiScreen On the server side, this needs to
	 * return a instance of Container
	 *
	 * @param guiId the GUI ID mumber
	 * @param player the player currently interacting with your block/tile entity
	 * @return A GuiScreen/Container to be displayed to the user, null if none.
	 */
	public Object getClientGuiElement(int guiId, EntityPlayer player) {
		return null;
	}
	
	/*
	TileEntity synchronization
	 */
	
	public enum SyncReason {
		FullSync,	   // full sync from storage
		NetworkUpdate   // update from the other side
	}
	
	public void readRadiation(NBTTagCompound data) {
		if (data.hasKey("radiationLevel")) getRadiationSource().setRadiationLevel(data.getDouble("radiationLevel"));
	}
	
	public NBTTagCompound writeRadiation(NBTTagCompound data) {
		data.setDouble("radiationLevel", getRadiationSource().getRadiationLevel());
		return data;
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound data) {
		super.writeToNBT(data);
		syncDataTo(super.writeToNBT(data), SyncReason.FullSync);
		writeAll(data);
		return data;
	}
	
	public NBTTagCompound writeAll(NBTTagCompound data) {
		data.setBoolean("isRedstonePowered", isRedstonePowered);
		data.setBoolean("alternateComparator", alternateComparator);
		data.setBoolean("redstoneControl", redstoneControl);
		if (shouldSaveRadiation()) writeRadiation(data);
		return data;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound data) {
		super.readFromNBT(data);
		syncDataFrom(data, SyncReason.FullSync);
		readAll(data);
	}
	
	public void readAll(NBTTagCompound data) {
		isRedstonePowered = data.getBoolean("isRedstonePowered");
		alternateComparator = data.getBoolean("alternateComparator");
		redstoneControl = data.getBoolean("redstoneControl");
		if (shouldSaveRadiation()) readRadiation(data);
	}
	
	@Override
	public void handleUpdateTag(NBTTagCompound data) {
		super.handleUpdateTag(data);
		syncDataFrom(data, SyncReason.NetworkUpdate);
	}
	
	@Override
	public NBTTagCompound getUpdateTag() {
		NBTTagCompound data = super.getUpdateTag();
		writeAll(data);
		syncDataTo(data, SyncReason.NetworkUpdate);
		return data;
	}
	
	@Override
	public final void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet) {
		readAll(packet.getNbtCompound());
		syncDataFrom(packet.getNbtCompound(), SyncReason.NetworkUpdate);
	}
	
	@Nullable
	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		NBTTagCompound data = new NBTTagCompound();
		writeAll(data);
		int metadata = getBlockMetadata();
		syncDataTo(data, SyncReason.NetworkUpdate);
		return new SPacketUpdateTileEntity(pos, metadata, data);
	}
	
	/**
	 * Sync tile entity data from the given NBT compound
	 * @param data the data
	 * @param syncReason the reason why the synchronization is necessary
	 */
	protected abstract void syncDataFrom(NBTTagCompound data, SyncReason syncReason);
	
	/**
	 * Sync tile entity data to the given NBT compound
	 * @param data the data
	 * @param syncReason the reason why the synchronization is necessary
	 */
	protected abstract void syncDataTo(NBTTagCompound data, SyncReason syncReason);
}
