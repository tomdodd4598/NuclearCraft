package nc.multiblock.tile;

import javax.annotation.Nullable;

import nc.block.tile.IDynamicState;
import nc.capability.radiation.source.*;
import nc.tile.ITile;
import nc.util.NCMath;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.*;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.*;

/** A base class for modded tile entities
 *
 * Partially ported from TileCoFHBase https://github.com/CoFH/CoFHCore/blob/master/src/main/java/cofh/core/block/TileCoFHBase.java */
public abstract class TileBeefAbstract extends TileEntity implements ITile {
	
	private boolean isRedstonePowered = false, alternateComparator = false, redstoneControl = false;
	
	private final IRadiationSource radiation;
	
	public TileBeefAbstract() {
		super();
		radiation = new RadiationSource(0D);
	}
	
	@Override
	public void onLoad() {
		if (world.isRemote) {
			world.markBlockRangeForRenderUpdate(pos, pos);
			refreshIsRedstonePowered(world, pos);
			markDirty();
			updateComparatorOutputLevel();
		}
	}
	
	@Override
	public TileEntity getTile() {
		return this;
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
	
	@Override
	public ITextComponent getDisplayName() {
		return getBlockType() == null ? null : new TextComponentTranslation(getBlockType().getLocalizedName());
	}
	
	@Override
	public boolean shouldRefresh(World worldIn, BlockPos posIn, IBlockState oldState, IBlockState newState) {
		return oldState.getBlock() != newState.getBlock();
	}
	
	public boolean isUseableByPlayer(EntityPlayer entityplayer) {
		BlockPos position = pos;
		
		if (world.getTileEntity(position) != this) {
			return false;
		}
		
		return entityplayer.getDistanceSq(position.getX() + 0.5D, position.getY() + 0.5D, position.getZ() + 0.5D) <= 64D;
	}
	
	@Override
	public final void markTileDirty() {
		markDirty();
	}
	
	@Override
	public void markDirty() {
		if (world != null) {
			getBlockMetadata();
			world.markChunkDirty(pos, this);
		}
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
			return IRadiationSource.CAPABILITY_RADIATION_SOURCE.cast(radiation);
		}
		return super.getCapability(capability, side);
	}
	
	// GUI management
	
	/** Check if the tile entity has a GUI or not Override in derived classes to return true if your tile entity got a GUI */
	public boolean canOpenGui(World worldIn, BlockPos posistion, IBlockState state) {
		return false;
	}
	
	/** Open the specified GUI
	 *
	 * @param player
	 *            the player currently interacting with your block/tile entity
	 * @param guiId
	 *            the GUI to open
	 * @return true if the GUI was opened, false otherwise */
	public boolean openGui(Object mod, EntityPlayer player, int guiId) {
		
		player.openGui(mod, guiId, world, pos.getX(), pos.getY(), pos.getZ());
		return true;
	}
	
	/** Returns a Server side Container to be displayed to the user.
	 *
	 * @param guiId
	 *            the GUI ID mumber
	 * @param player
	 *            the player currently interacting with your block/tile entity
	 * @return A GuiScreen/Container to be displayed to the user, null if none. */
	public Object getServerGuiElement(int guiId, EntityPlayer player) {
		return null;
	}
	
	/** Returns a Container to be displayed to the user. On the client side, this needs to return a instance of GuiScreen On the server side, this needs to return a instance of Container
	 *
	 * @param guiId
	 *            the GUI ID mumber
	 * @param player
	 *            the player currently interacting with your block/tile entity
	 * @return A GuiScreen/Container to be displayed to the user, null if none. */
	public Object getClientGuiElement(int guiId, EntityPlayer player) {
		return null;
	}
	
	// NBT
	
	public enum SyncReason {
		FullSync, // full sync from storage
		NetworkUpdate // update from the other side
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound data) {
		super.writeToNBT(data);
		writeAll(data);
		syncDataTo(data, SyncReason.FullSync);
		return data;
	}
	
	public NBTTagCompound writeAll(NBTTagCompound data) {
		data.setBoolean("isRedstonePowered", isRedstonePowered);
		data.setBoolean("alternateComparator", alternateComparator);
		data.setBoolean("redstoneControl", redstoneControl);
		if (shouldSaveRadiation()) {
			writeRadiation(data);
		}
		return data;
	}
	
	public NBTTagCompound writeRadiation(NBTTagCompound data) {
		data.setDouble("radiationLevel", getRadiationSource().getRadiationLevel());
		return data;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound data) {
		super.readFromNBT(data);
		readAll(data);
		syncDataFrom(data, SyncReason.FullSync);
	}
	
	public void readAll(NBTTagCompound data) {
		isRedstonePowered = data.getBoolean("isRedstonePowered");
		alternateComparator = data.getBoolean("alternateComparator");
		redstoneControl = data.getBoolean("redstoneControl");
		if (shouldSaveRadiation()) {
			readRadiation(data);
		}
	}
	
	public void readRadiation(NBTTagCompound data) {
		if (data.hasKey("radiationLevel")) {
			getRadiationSource().setRadiationLevel(data.getDouble("radiationLevel"));
		}
	}
	
	@Override
	public NBTTagCompound getUpdateTag() {
		NBTTagCompound data = super.writeToNBT(new NBTTagCompound());
		writeAll(data);
		syncDataTo(data, SyncReason.NetworkUpdate);
		return data;
	}
	
	@Override
	public void handleUpdateTag(NBTTagCompound data) {
		super.readFromNBT(data);
		readAll(data);
		syncDataFrom(data, SyncReason.NetworkUpdate);
	}
	
	@Nullable
	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		NBTTagCompound data = super.writeToNBT(new NBTTagCompound());
		writeAll(data);
		syncDataTo(data, SyncReason.NetworkUpdate);
		return new SPacketUpdateTileEntity(pos, getBlockMetadata(), data);
	}
	
	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet) {
		NBTTagCompound data = packet.getNbtCompound();
		super.readFromNBT(data);
		readAll(data);
		syncDataFrom(data, SyncReason.NetworkUpdate);
		if (getBlockType() instanceof IDynamicState) {
			notifyBlockUpdate();
		}
	}
	
	/** Sync tile entity data from the given NBT compound
	 * 
	 * @param data
	 *            the data
	 * @param syncReason
	 *            the reason why the synchronization is necessary */
	protected abstract void syncDataFrom(NBTTagCompound data, SyncReason syncReason);
	
	/** Sync tile entity data to the given NBT compound
	 * 
	 * @param data
	 *            the data
	 * @param syncReason
	 *            the reason why the synchronization is necessary */
	protected abstract void syncDataTo(NBTTagCompound data, SyncReason syncReason);
	
	// TESR
	
	@Override
	@SideOnly(Side.CLIENT)
	public double getMaxRenderDistanceSquared() {
		return NCMath.sq(16D * FMLClientHandler.instance().getClient().gameSettings.renderDistanceChunks);
	}
}
