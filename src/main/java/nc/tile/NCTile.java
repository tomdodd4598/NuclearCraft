package nc.tile;

import nc.block.tile.IActivatable;
import nc.capability.radiation.IRadiationSource;
import nc.capability.radiation.RadiationSource;
import nc.config.NCConfig;
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
	
	public boolean isAdded;
	public boolean isMarkedDirty;
	
	public int tickCount;
	
	public boolean alternateComparator;
	
	private IRadiationSource radiation;
	
	public NCTile() {
		super();
		radiation = new RadiationSource();
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
			getWorld().markBlockRangeForRenderUpdate(pos, pos);
			getWorld().getChunkFromBlockCoords(getPos()).markDirty();
		}
		markDirty();
	}
	
	@Override
	public void tickTile() {
		tickCount++; tickCount %= NCConfig.machine_update_rate;
	}
	
	@Override
	public boolean shouldTileCheck() {
		return tickCount == 0;
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
	
	// State Updating
	
	public void markAndRefresh() {
		markAndRefresh(getPos(), world.getBlockState(getPos()));
	}
	
	public void markAndRefresh(IBlockState newState) {
		markAndRefresh(getPos(), newState);
	}
	
	public void markAndRefresh(BlockPos pos, IBlockState newState) {
		markDirty();
		world.notifyBlockUpdate(pos, world.getBlockState(pos), newState, 3);
		world.notifyNeighborsOfStateChange(pos, getBlockType(), true);
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
		nbt.setBoolean("alternateComparator", alternateComparator);
		if (shouldSaveRadiation()) writeRadiation(nbt);
		return nbt;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		readAll(nbt);
	}
	
	public void readAll(NBTTagCompound nbt) {
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
	
	public boolean getAlternateComparator() {
		return alternateComparator;
	}
	
	public void setAlternateComparator(boolean alternate) {
		alternateComparator = alternate;
	}
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing side) {
		if (capability == IRadiationSource.CAPABILITY_RADIATION_SOURCE) return radiation != null;
		return super.hasCapability(capability, side);
	}
	
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing side) {
		if (capability == IRadiationSource.CAPABILITY_RADIATION_SOURCE) return (T) radiation;
		return super.getCapability(capability, side);
	}
}
