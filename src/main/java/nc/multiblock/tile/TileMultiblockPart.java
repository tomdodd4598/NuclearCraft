package nc.multiblock.tile;

import java.util.*;

import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import nc.Global;
import nc.multiblock.*;
import nc.util.NCUtil;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.FMLLog;

/** Base logic class for Multiblock-connected tile entities. Most multiblock machines should derive from this and implement their game logic in certain abstract methods. */
public abstract class TileMultiblockPart<MULTIBLOCK extends Multiblock<MULTIBLOCK, T>, T extends ITileMultiblockPart<MULTIBLOCK, T>> extends TileBeefAbstract implements ITileMultiblockPart<MULTIBLOCK, T> {
	
	private MULTIBLOCK multiblock;
	public final Class<MULTIBLOCK> multiblockClass;
	public final Class<T> tClass;
	
	private boolean visited;
	
	private boolean saveMultiblockData;
	private NBTTagCompound cachedMultiblockData;
	// private boolean paused;
	
	public TileMultiblockPart(Class<MULTIBLOCK> multiblockClass, Class<T> tClass) {
		super();
		multiblock = null;
		this.multiblockClass = multiblockClass;
		this.tClass = tClass;
		visited = false;
		saveMultiblockData = false;
		// paused = false;
		cachedMultiblockData = null;
	}
	
	///// Multiblock Connection Base Logic
	@Override
	public Set<MULTIBLOCK> attachToNeighbors() {
		Set<MULTIBLOCK> multiblocks = null;
		MULTIBLOCK bestMultiblock = null;
		
		// Look for a compatible multiblock in our neighboring parts.
		List<T> partsToCheck = getNeighboringParts();
		for (T neighborPart : partsToCheck) {
			if (neighborPart.isConnected()) {
				MULTIBLOCK candidate = neighborPart.getMultiblock();
				if (!candidate.getClass().equals(multiblockClass)) {
					// Skip multiblocks with incompatible types
					continue;
				}
				
				if (multiblocks == null) {
					multiblocks = new ObjectOpenHashSet<>();
					bestMultiblock = candidate;
				}
				else if (!multiblocks.contains(candidate) && candidate.shouldConsume(bestMultiblock)) {
					bestMultiblock = candidate;
				}
				
				multiblocks.add(candidate);
			}
		}
		
		// If we've located a valid neighboring multiblock, attach to it.
		if (bestMultiblock != null) {
			// attachBlock will call onAttached, which will set the multiblock.
			multiblock = bestMultiblock;
			bestMultiblock.attachBlock(tClass.cast(this));
		}
		
		return multiblocks;
	}
	
	@Override
	public void assertDetached() {
		if (multiblock != null) {
			BlockPos coord = pos;
			
			FMLLog.info("[assert] Part @ (%d, %d, %d) should be detached already, but detected that it was not. This is not a fatal error, and will be repaired, but is unusual.", coord.getX(), coord.getY(), coord.getZ());
			multiblock = null;
		}
	}
	
	@Override
	protected void syncDataFrom(NBTTagCompound data, SyncReason syncReason) {
		if (SyncReason.FullSync == syncReason) {
			// We can't directly initialize a multiblock controller yet, so we cache the data here until
			// we receive a validate() call, which creates the controller and hands off the cached data.
			if (data.hasKey("multiblockData")) {
				cachedMultiblockData = data.getCompoundTag("multiblockData");
			}
		}
		else {
			if (data.hasKey("multiblockData")) {
				NBTTagCompound tag = data.getCompoundTag("multiblockData");
				if (isConnected()) {
					getMultiblock().syncDataFrom(tag, syncReason);
				}
				else {
					// This part hasn't been added to a machine yet, so cache the data.
					cachedMultiblockData = tag;
				}
			}
		}
	}
	
	@Override
	protected void syncDataTo(NBTTagCompound data, SyncReason syncReason) {
		
		if (SyncReason.FullSync == syncReason) {
			
			if (isMultiblockSaveDelegate() && isConnected()) {
				NBTTagCompound multiblockData = new NBTTagCompound();
				multiblock.syncDataTo(multiblockData, syncReason);
				data.setTag("multiblockData", multiblockData);
			}
			
		}
		else {
			
			if (isMultiblockSaveDelegate() && isConnected()) {
				NBTTagCompound tag = new NBTTagCompound();
				multiblock.syncDataTo(tag, syncReason);
				data.setTag("multiblockData", tag);
			}
			
		}
	}
	
	/** Called when a block is removed by game actions, such as a player breaking the block or the block being changed into another block.
	 * 
	 * @see net.minecraft.tileentity.TileEntity#invalidate() */
	@Override
	public void invalidate() {
		super.invalidate();
		detachSelf(false);
	}
	
	/** Called from Minecraft's tile entity loop, after all tile entities have been ticked, as the chunk in which this tile entity is contained is unloading. Happens before the Forge TickEnd event.
	 * 
	 * @see net.minecraft.tileentity.TileEntity#onChunkUnload() */
	@Override
	public void onChunkUnload() {
		super.onChunkUnload();
		detachSelf(true);
	}
	
	/** This is called when a block is being marked as valid by the chunk, but has not yet fully been placed into the world's TileEntity cache. WORLD, xCoord, yCoord and zCoord have been initialized, but any attempts to read data about the world can cause infinite loops - if you call getTileEntity on this TileEntity's coordinate from within validate(), you will blow your call stack.
	 * 
	 * TL;DR: Here there be dragons.
	 * 
	 * @see net.minecraft.tileentity.TileEntity#validate() */
	@Override
	public void validate() {
		super.validate();
		MultiblockRegistry.INSTANCE.onPartAdded(world, tClass.cast(this));
	}
	
	@Override
	public boolean hasMultiblockSaveData() {
		return cachedMultiblockData != null;
	}
	
	@Override
	public NBTTagCompound getMultiblockSaveData() {
		return cachedMultiblockData;
	}
	
	@Override
	public void onMultiblockDataAssimilated() {
		cachedMultiblockData = null;
	}
	
	@Override
	public abstract void onMachineAssembled(MULTIBLOCK multiblockIn);
	
	@Override
	public abstract void onMachineBroken();
	
	@Override
	public void onMachineActivated() {}
	
	@Override
	public void onMachineDeactivated() {}
	
	@Override
	public boolean isConnected() {
		return multiblock != null;
	}
	
	@Override
	public MULTIBLOCK getMultiblock() {
		return multiblock;
	}
	
	@Override
	public void becomeMultiblockSaveDelegate() {
		saveMultiblockData = true;
	}
	
	@Override
	public void forfeitMultiblockSaveDelegate() {
		saveMultiblockData = false;
	}
	
	@Override
	public boolean isMultiblockSaveDelegate() {
		return saveMultiblockData;
	}
	
	@Override
	public void setUnvisited() {
		visited = false;
	}
	
	@Override
	public void setVisited() {
		visited = true;
	}
	
	@Override
	public boolean isVisited() {
		return visited;
	}
	
	@Override
	public void onAssimilated(MULTIBLOCK newMultiblock) {
		assert multiblock != newMultiblock;
		multiblock = newMultiblock;
	}
	
	@Override
	public void onAttached(MULTIBLOCK newMultiblock) {
		multiblock = newMultiblock;
	}
	
	@Override
	public void onDetached(MULTIBLOCK oldMultiblock) {
		multiblock = null;
	}
	
	@Override
	public abstract MULTIBLOCK createNewMultiblock();
	
	@Override
	public Class<MULTIBLOCK> getMultiblockClass() {
		return multiblockClass;
	}
	
	@Override
	public Class<T> getPartClass() {
		return tClass;
	}
	
	@Override
	public List<T> getNeighboringParts() {
		
		TileEntity te;
		List<T> neighborParts = new ArrayList<>();
		BlockPos neighborPosition, partPosition = pos;
		
		for (EnumFacing facing : EnumFacing.VALUES) {
			
			neighborPosition = partPosition.offset(facing);
			te = world.getTileEntity(neighborPosition);
			
			if (tClass.isInstance(te)) {
				neighborParts.add(tClass.cast(te));
			}
		}
		
		return neighborParts;
	}
	
	@Override
	public void onOrphaned(MULTIBLOCK oldMultiblock, int oldSize, int newSize) {
		markDirty();
	}
	
	@Override
	public boolean isPartInvalid() {
		return isInvalid();
	}
	
	public boolean isMultiblockAssembled() {
		return multiblock != null && multiblock.isAssembled();
	}
	
	// Validator standard errors
	
	protected void doStandardNullControllerResponse(MULTIBLOCK controller) {
		if (controller == null) {
			throw nullControllerError();
		}
		
		if (multiblock == null) {
			nullControllerWarn();
			onAttached(controller);
		}
	}
	
	protected void setStandardLastError(MULTIBLOCK multiblock) {
		multiblock.setLastError(Global.MOD_ID + ".multiblock_validation.invalid_block", getPos(), getPos().getX(), getPos().getY(), getPos().getZ(), getBlock(getPos()).getLocalizedName());
	}
	
	protected IllegalArgumentException nullControllerError() {
		return new IllegalArgumentException("Attempted to attach " + getBlock(getPos()).getLocalizedName() + " to a null controller. This should never happen - please report this bug to the NuclearCraft GitHub repo!");
	}
	
	protected void nullControllerWarn() {
		NCUtil.getLogger().warn(getBlock(getPos()).getLocalizedName() + " at (%d, %d, %d) is being assembled without being attached to a controller. It is recommended that the multiblock is completely disassambled and rebuilt if these errors continue!", getPos().getX(), getPos().getY(), getPos().getZ());
	}
	
	// Private/Protected Logic Helpers
	/** Detaches this block from its multiblock. Calls detachBlock() and clears the multiblock member. */
	protected void detachSelf(boolean chunkUnloading) {
		if (multiblock != null) {
			// Clean part out of multiblock
			multiblock.detachBlock(tClass.cast(this), chunkUnloading);
			
			// The above should call onDetached, but, just in case...
			multiblock = null;
		}
		
		// Clean part out of lists in the registry
		MultiblockRegistry.INSTANCE.onPartRemovedFromWorld(world, tClass.cast(this));
	}
	
	/** IF the part is connected to a multiblock, marks the whole multiblock for a render update on the client. On the server, this does nothing */
	protected void markMultiblockForRenderUpdate() {
		if (multiblock != null) {
			multiblock.markMultiblockForRenderUpdate();
		}
	}
}
