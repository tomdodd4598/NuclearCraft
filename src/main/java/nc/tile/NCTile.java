package nc.tile;

import javax.annotation.Nullable;

import nc.block.tile.IActivatable;
import nc.capability.radiation.source.IRadiationSource;
import nc.capability.radiation.source.RadiationSource;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;

public abstract class NCTile extends TileEntity implements ITickable, ITile {
	
	public boolean isAdded = false;
	public boolean isMarkedDirty = false;
	
	private boolean isRedstonePowered = false;
	private boolean alternateComparator = false;
	
	private IRadiationSource radiation;
	
	public NCTile() {
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
			markTileDirty();
			isMarkedDirty = false;
		}
	}
	
	public void onAdded() {
		if (world.isRemote) {
			getWorld().markBlockRangeForRenderUpdate(pos, pos);
			getWorld().getChunkFromBlockCoords(getPos()).markDirty();
			refreshIsRedstonePowered(world, pos);
			markTileDirty();
		}
	}
	
	@Override
	public World getTileWorld() {
		return getWorld();
	}
	
	@Override
	public BlockPos getTilePos() {
		return getPos();
	}
	
	@Override
	public void markTileDirty() {
		markDirty();
	}
	
	@Override
	public Block getTileBlockType() {
		return getBlockType();
	}
	
	@Override
	public IRadiationSource getRadiationSource() {
		return radiation;
	}
	
	@Override
	public ITextComponent getDisplayName() {
		if (getBlockType() != null) return new TextComponentTranslation(getBlockType().getLocalizedName()); else return null;
	}
	
	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
		String oldName = oldState.getBlock().getUnlocalizedName().toString();
		String newName = newState.getBlock().getUnlocalizedName().toString();
		if (oldName.contains("_idle") || oldName.contains("_active")) {
			return !oldName.replace("_idle","").replace("_active","").equals(newName.replace("_idle","").replace("_active",""));
		}
		return oldState.getBlock() != newState.getBlock();
	}
	
	public IBlockState getDefaultBlockState() {
		return world.getBlockState(pos).getBlock().getDefaultState();
	}
	
	@Override
	public void setState(boolean isActive) {
		if (getBlockType() instanceof IActivatable) ((IActivatable)getBlockType()).setState(isActive, world, pos);
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
	
	// NBT
	
	public NBTTagCompound writeRadiation(NBTTagCompound nbt) {
		nbt.setDouble("radiationLevel", getRadiationSource().getRadiationLevel());
		return nbt;
	}
	
	public void readRadiation(NBTTagCompound nbt) {
		if (nbt.hasKey("radiationLevel")) getRadiationSource().setRadiationLevel(nbt.getDouble("radiationLevel"));
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		writeAll(nbt);
		return nbt;
	}
	
	public NBTTagCompound writeAll(NBTTagCompound nbt) {
		nbt.setBoolean("isRedstonePowered", getIsRedstonePowered());
		nbt.setBoolean("alternateComparator", getAlternateComparator());
		if (shouldSaveRadiation()) writeRadiation(nbt);
		return nbt;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		readAll(nbt);
	}
	
	public void readAll(NBTTagCompound nbt) {
		setIsRedstonePowered(nbt.getBoolean("isRedstonePowered"));
		setAlternateComparator(nbt.getBoolean("alternateComparator"));
		if (shouldSaveRadiation()) readRadiation(nbt);
	}
	
	/*public NBTTagCompound getTileData() {
		NBTTagCompound nbt = new NBTTagCompound();
		writeToNBT(nbt);
		return nbt;
	}*/
	
	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		NBTTagCompound nbt = new NBTTagCompound();
		writeToNBT(nbt);
		int metadata = getBlockMetadata();
		return new SPacketUpdateTileEntity(pos, metadata, nbt);
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet) {
		readFromNBT(packet.getNbtCompound());
	}

	@Override
	public NBTTagCompound getUpdateTag() {
		NBTTagCompound nbt = new NBTTagCompound();
		writeToNBT(nbt);
		return nbt;
	}

	@Override
	public void handleUpdateTag(NBTTagCompound tag) {
		super.handleUpdateTag(tag);
	}
	
	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing side) {
		if (capability == IRadiationSource.CAPABILITY_RADIATION_SOURCE) return radiation != null;
		return super.hasCapability(capability, side);
	}
	
	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing side) {
		if (capability == IRadiationSource.CAPABILITY_RADIATION_SOURCE) return (T) radiation;
		return super.getCapability(capability, side);
	}
	
	// For when the raw TE capabilities need to be reached:
	
	protected boolean hasCapabilityDefault(Capability<?> capability, @Nullable EnumFacing side) {
		return super.hasCapability(capability, side);
	}
	
	protected <T> T getCapabilityDefault(Capability<T> capability, @Nullable EnumFacing side) {
		return super.getCapability(capability, side);
	}
}
