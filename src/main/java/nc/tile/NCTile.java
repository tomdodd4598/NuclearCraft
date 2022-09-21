package nc.tile;

import javax.annotation.Nullable;

import nc.block.tile.IDynamicState;
import nc.capability.radiation.source.*;
import nc.util.NCMath;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
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

public abstract class NCTile extends TileEntity implements ITile {
	
	private boolean isRedstonePowered = false, alternateComparator = false, redstoneControl = false;
	
	private final IRadiationSource radiation;
	
	public NCTile() {
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
		Block block = getBlockType();
		return block == null ? null : new TextComponentTranslation(block.getLocalizedName());
	}
	
	@Override
	public boolean shouldRefresh(World worldIn, BlockPos posIn, IBlockState oldState, IBlockState newState) {
		return oldState.getBlock() != newState.getBlock();
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
	
	// NBT
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		writeAll(nbt);
		return nbt;
	}
	
	public NBTTagCompound writeAll(NBTTagCompound nbt) {
		nbt.setBoolean("isRedstonePowered", isRedstonePowered);
		nbt.setBoolean("alternateComparator", alternateComparator);
		nbt.setBoolean("redstoneControl", redstoneControl);
		if (shouldSaveRadiation()) {
			writeRadiation(nbt);
		}
		return nbt;
	}
	
	public NBTTagCompound writeRadiation(NBTTagCompound nbt) {
		nbt.setDouble("radiationLevel", getRadiationSource().getRadiationLevel());
		return nbt;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		readAll(nbt);
	}
	
	public void readAll(NBTTagCompound nbt) {
		isRedstonePowered = nbt.getBoolean("isRedstonePowered");
		alternateComparator = nbt.getBoolean("alternateComparator");
		redstoneControl = nbt.getBoolean("redstoneControl");
		if (shouldSaveRadiation()) {
			readRadiation(nbt);
		}
	}
	
	public void readRadiation(NBTTagCompound nbt) {
		if (nbt.hasKey("radiationLevel")) {
			getRadiationSource().setRadiationLevel(nbt.getDouble("radiationLevel"));
		}
	}
	
	@Override
	public NBTTagCompound getUpdateTag() {
		return writeToNBT(new NBTTagCompound());
	}
	
	@Override
	public void handleUpdateTag(NBTTagCompound tag) {
		super.handleUpdateTag(tag);
	}
	
	@Nullable
	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		return new SPacketUpdateTileEntity(pos, getBlockMetadata(), writeToNBT(new NBTTagCompound()));
	}
	
	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet) {
		readFromNBT(packet.getNbtCompound());
		if (getBlockType() instanceof IDynamicState) {
			notifyBlockUpdate();
		}
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
	
	// For when the raw TE capabilities need to be reached:
	
	protected boolean hasCapabilityDefault(Capability<?> capability, @Nullable EnumFacing side) {
		return super.hasCapability(capability, side);
	}
	
	protected <T> T getCapabilityDefault(Capability<T> capability, @Nullable EnumFacing side) {
		return super.getCapability(capability, side);
	}
	
	// TESR
	
	@Override
	@SideOnly(Side.CLIENT)
	public double getMaxRenderDistanceSquared() {
		return NCMath.sq(16D * FMLClientHandler.instance().getClient().gameSettings.renderDistanceChunks);
	}
}
