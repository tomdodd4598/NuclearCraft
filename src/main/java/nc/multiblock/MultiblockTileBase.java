package nc.multiblock;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import nc.Global;
import nc.NuclearCraft;
import nc.multiblock.validation.IMultiblockValidator;
import nc.util.NCUtil;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.FMLLog;

/**
 * Base logic class for Multiblock-connected tile entities. Most multiblock machines
 * should derive from this and implement their game logic in certain abstract methods.
 */
public abstract class MultiblockTileBase<T extends MultiblockBase> extends TileBeefBase implements IMultiblockPart<T> {
	
	private T multiblock;
	protected final Class<T> tClass;
	
	private boolean visited;
	
	private boolean saveMultiblockData;
	private NBTTagCompound cachedMultiblockData;
	private boolean paused;

	public MultiblockTileBase(Class<T> tClass) {
		super();
		multiblock = null;
		this.tClass = tClass;
		visited = false;
		saveMultiblockData = false;
		paused = false;
		cachedMultiblockData = null;
	}
	
	@Override
	public Class<T> getMultiblockType() {
		return tClass;
	}

	///// Multiblock Connection Base Logic
	@Override
	public Set<T> attachToNeighbors() {
		Set<T> multiblocks = null;
		T bestMultiblock = null;
		
		// Look for a compatible multiblock in our neighboring parts.
		IMultiblockPart<T>[] partsToCheck = getNeighboringParts();
		for(IMultiblockPart<T> neighborPart : partsToCheck) {
			if(neighborPart.isConnected()) {
				T candidate = neighborPart.getMultiblock();
				if(!candidate.getClass().equals(getMultiblockType())) {
					// Skip multiblocks with incompatible types
					continue;
				}
				
				if(multiblocks == null) {
					multiblocks = new HashSet<T>();
					bestMultiblock = candidate;
				}
				else if(!multiblocks.contains(candidate) && candidate.shouldConsume(bestMultiblock)) {
					bestMultiblock = candidate;
				}

				multiblocks.add(candidate);
			}
		}
		
		// If we've located a valid neighboring multiblock, attach to it.
		if(bestMultiblock != null) {
			// attachBlock will call onAttached, which will set the multiblock.
			this.multiblock = bestMultiblock;
			bestMultiblock.attachBlock(this);
		}

		return multiblocks;
	}

	@Override
	public void assertDetached() {
		if(this.multiblock != null) {
			BlockPos coord = this.getWorldPosition();

			FMLLog.info("[assert] Part @ (%d, %d, %d) should be detached already, but detected that it was not. This is not a fatal error, and will be repaired, but is unusual.",
					coord.getX(), coord.getY(), coord.getZ());
			this.multiblock = null;
		}
	}



	@Override
	protected void syncDataFrom(NBTTagCompound data, SyncReason syncReason) {

		if (SyncReason.FullSync == syncReason) {

			// We can't directly initialize a multiblock yet, so we cache the data here until
			// we receive a validate() call, which creates the multiblock and hands off the cached data.
			if (data.hasKey("multiblockData")) {
				this.cachedMultiblockData = data.getCompoundTag("multiblockData");
			}

		} else {

			if(data.hasKey("multiblockData")) {
				NBTTagCompound tag = data.getCompoundTag("multiblockData");
				if(isConnected()) {
					getMultiblock().syncDataFrom(tag, syncReason);
				}
				else {
					// This part hasn't been added to a machine yet, so cache the data.
					this.cachedMultiblockData = tag;
				}
			}
		}
	}

	@Override
	protected void syncDataTo(NBTTagCompound data, SyncReason syncReason) {

		if (SyncReason.FullSync == syncReason) {

			if (isMultiblockSaveDelegate() && isConnected()) {
				NBTTagCompound multiblockData = new NBTTagCompound();
				this.multiblock.syncDataTo(multiblockData, syncReason);
				data.setTag("multiblockData", multiblockData);
			}

		} else {

			if (this.isMultiblockSaveDelegate() && isConnected()) {
				NBTTagCompound tag = new NBTTagCompound();
				getMultiblock().syncDataTo(tag, syncReason);
				data.setTag("multiblockData", tag);
			}

		}
	}

	/**
	 * Called when a block is removed by game actions, such as a player breaking the block
	 * or the block being changed into another block.
	 * @see net.minecraft.tileentity.TileEntity#invalidate()
	 */
	@Override
	public void invalidate() {
		super.invalidate();
		detachSelf(false);
	}
	
	/**
	 * Called from Minecraft's tile entity loop, after all tile entities have been ticked,
	 * as the chunk in which this tile entity is contained is unloading.
	 * Happens before the Forge TickEnd event.
	 * @see net.minecraft.tileentity.TileEntity#onChunkUnload()
	 */
	@Override
	public void onChunkUnload() {
		super.onChunkUnload();
		detachSelf(true);
	}

	/**
	 * This is called when a block is being marked as valid by the chunk, but has not yet fully
	 * been placed into the world's TileEntity cache. this.WORLD, xCoord, yCoord and zCoord have
	 * been initialized, but any attempts to read data about the world can cause infinite loops -
	 * if you call getTileEntity on this TileEntity's coordinate from within validate(), you will
	 * blow your call stack.
	 * 
	 * TL;DR: Here there be dragons.
	 * @see net.minecraft.tileentity.TileEntity#validate()
	 */
	@Override
	public void validate() {
		super.validate();
        REGISTRY.onPartAdded(this.getWorld(), this);
	}

	@Override
	public boolean hasMultiblockSaveData() {
		return this.cachedMultiblockData != null;
	}
	
	@Override
	public NBTTagCompound getMultiblockSaveData() {
		return this.cachedMultiblockData;
	}
	
	@Override
	public void onMultiblockDataAssimilated() {
		this.cachedMultiblockData = null;
	}

	@Override
	public abstract void onMachineAssembled(T multiblock);

	@Override
	public abstract void onMachineBroken();
	
	@Override
	public abstract void onMachineActivated();

	@Override
	public abstract void onMachineDeactivated();

	@Override
	public boolean isConnected() {
		return (multiblock != null);
	}

	@Override
	public T getMultiblock() {
		return multiblock;
	}

	@Override
	public void becomeMultiblockSaveDelegate() {
		this.saveMultiblockData = true;
	}

	@Override
	public void forfeitMultiblockSaveDelegate() {
		this.saveMultiblockData = false;
	}
	
	@Override
	public boolean isMultiblockSaveDelegate() { return this.saveMultiblockData; }

	@Override
	public void setUnvisited() {
		this.visited = false;
	}
	
	@Override
	public void setVisited() {
		this.visited = true;
	}
	
	@Override
	public boolean isVisited() {
		return this.visited;
	}

	@Override
	public void onAssimilated(T newMultiblock) {
		assert(this.multiblock != newMultiblock);
		this.multiblock = newMultiblock;
	}
	
	@Override
	public void onAttached(T newMultiblock) {
		this.multiblock = newMultiblock;
	}
	
	@Override
	public void onDetached(T oldMultiblock) {
		this.multiblock = null;
	}

	@Override
	public abstract T createNewMultiblock();
	
	@Override
	public IMultiblockPart<T>[] getNeighboringParts() {

		TileEntity te;
		List<IMultiblockPart<T>> neighborParts = new ArrayList<IMultiblockPart<T>>();
		BlockPos neighborPosition, partPosition = this.getWorldPosition();

		for (EnumFacing facing : EnumFacing.VALUES) {

			neighborPosition = partPosition.offset(facing);
			te = this.getWorld().getTileEntity(neighborPosition);

			if (te instanceof IMultiblockPart)
				neighborParts.add((IMultiblockPart<T>)te);
		}

		return neighborParts.toArray(new IMultiblockPart[neighborParts.size()]);
	}
	
	@Override
	public void onOrphaned(T multiblock, int oldSize, int newSize) {
		this.markDirty();
		getWorld().markChunkDirty(this.getWorldPosition(), this);
	}

	@Override
	public BlockPos getWorldPosition() {
		return this.pos;
	}

	@Override
	public boolean isPartInvalid() {
		return this.isInvalid();
	}

	//// Helper functions for notifying neighboring blocks
	protected void notifyNeighborsOfBlockChange() {
		getWorld().notifyNeighborsOfStateChange(this.getWorldPosition(), this.getBlockType(), true);
	}

	@Deprecated // not implemented yet
	protected void notifyNeighborsOfTileChange() {
		//WORLD.func_147453_f(xCoord, yCoord, zCoord, getBlockType());

	}
	
	// BlockState getting
	
	protected IBlockState getBlockState(BlockPos pos) {
		return getWorld().getBlockState(pos);
	}
	
	protected Block getBlock(BlockPos pos) {
		return getBlockState(pos).getBlock();
	}
	
	// Validator standard errors
	
	protected void doStandardNullControllerResponse(T controller) {
		if (controller == null) {
			throw nullControllerError();
		}

		if (getMultiblock() == null) {
			nullControllerWarn();
			onAttached(controller);
		}
	}
	
	protected void setStandardLastError(IMultiblockValidator validator) {
		validator.setLastError(Global.MOD_ID + ".multiblock_validation.invalid_block", getPos(), getPos().getX(), getPos().getY(), getPos().getZ(), getBlock(getPos()).getLocalizedName());
	}
	
	protected IllegalArgumentException nullControllerError() {
		return new IllegalArgumentException("Attempted to attach " + getBlock(getPos()).getLocalizedName() + " to a null controller. This should never happen - please report this bug to the NuclearCraft GitHub repo!");
	}
	
	protected void nullControllerWarn() {
		NCUtil.getLogger().warn(getBlock(getPos()).getLocalizedName() + " at (%d, %d, %d) is being assembled without being attached to a controller. It is recommended that the multiblock is completely disassambled and rebuilt if these errors continue!", getPos().getX(), getPos().getY(), getPos().getZ());
	}
	
	///// Private/Protected Logic Helpers
	/*
	 * Detaches this block from its multiblock. Calls detachBlock() and clears the multiblock member.
	 */
	protected void detachSelf(boolean chunkUnloading) {
		if(this.multiblock != null) {
			// Clean part out of multiblock
			this.multiblock.detachBlock(this, chunkUnloading);

			// The above should call onDetached, but, just in case...
			this.multiblock = null;
		}

		// Clean part out of lists in the registry
        REGISTRY.onPartRemovedFromWorld(getWorld(), this);
	}

	/**
	 * IF the part is connected to a multiblock, marks the whole multiblock for a render update on the client.
	 * On the server, this does nothing
	 */
	protected void markMultiblockForRenderUpdate() {

		T multiblock = this.getMultiblock();

		if (null != multiblock)
			multiblock.markMultiblockForRenderUpdate();
	}
	
	// Registry

    private static final IMultiblockRegistry REGISTRY;

    static {
        REGISTRY = NuclearCraft.proxy.initMultiblockRegistry();
    }
}
