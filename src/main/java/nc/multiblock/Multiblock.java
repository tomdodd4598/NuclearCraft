package nc.multiblock;

import it.unimi.dsi.fastutil.longs.*;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import nc.Global;
import nc.multiblock.internal.MultiblockValidationError;
import nc.tile.fluid.ITileFluid;
import nc.tile.internal.energy.EnergyStorage;
import nc.tile.internal.fluid.Tank;
import nc.tile.inventory.ITileInventory;
import nc.tile.multiblock.*;
import nc.util.*;
import nc.util.SuperMap.SuperMapEntry;
import net.minecraft.block.state.IBlockState;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.common.FMLLog;

import java.util.*;
import java.util.Map.Entry;

/**
 * This class contains the base logic for "multiblocks". Conceptually, they are meta-TileEntities. They govern the logic for an associated group of TileEntities.
 * Subordinate TileEntities implement the IMultiblockPart class and, generally, should not have an update() loop.
 */
public abstract class Multiblock<MULTIBLOCK extends Multiblock<MULTIBLOCK, T>, T extends ITileMultiblockPart<MULTIBLOCK, T>> implements IMultiblock<MULTIBLOCK, T> {
	
	public static final short DIMENSION_UNBOUNDED = -1;
	
	// Multiblock stuff - do not mess with
	public final World WORLD;
	
	// Disassembled -> Assembled; Assembled -> Disassembled OR Paused; Paused -> Assembled
	public enum AssemblyState {
		Disassembled,
		Assembled,
		Paused
	}
	
	public AssemblyState assemblyState;
	
	protected ObjectOpenHashSet<T> connectedParts;
	
	public Random rand = new Random();
	
	/**
	 * This is a deterministically-picked coordinate that identifies this multiblock uniquely in its dimension. Currently, this is the coord with the lowest X, Y and Z coordinates, in that order of evaluation. i.e. If something has a lower X but higher Y/Z coordinates, it will still be the reference. If something has the same X but a lower Y coordinate, it will be the reference. Etc.
	 */
	private BlockPos referenceCoord;
	
	/**
	 * Minimum bounding box coordinate. Blocks do not necessarily exist at this coord if your machine is not a cube/rectangular prism.
	 */
	private BlockPos minimumCoord;
	
	/**
	 * Maximum bounding box coordinate. Blocks do not necessarily exist at this coord if your machine is not a cube/rectangular prism.
	 */
	private BlockPos maximumCoord;
	
	/**
	 * Set to true whenever a part is removed from this multiblock.
	 */
	private boolean shouldCheckForDisconnections;
	
	/**
	 * Set whenever we validate the multiblock
	 */
	private MultiblockValidationError lastValidationError;
	
	private boolean debugMode;
	
	public final Class<MULTIBLOCK> multiblockClass;
	public final Class<T> tClass;
	
	protected Multiblock(World world, Class<MULTIBLOCK> multiblockClass, Class<T> tClass) {
		// Multiblock stuff
		WORLD = world;
		
		this.multiblockClass = multiblockClass;
		this.tClass = tClass;
		
		connectedParts = new ObjectOpenHashSet<>();
		
		referenceCoord = null;
		assemblyState = AssemblyState.Disassembled;
		
		minimumCoord = null;
		maximumCoord = null;
		
		shouldCheckForDisconnections = true;
		lastValidationError = null;
		
		debugMode = false;
	}
	
	@Override
	public World getWorld() {
		return WORLD;
	}
	
	public void setDebugMode(boolean active) {
		debugMode = active;
	}
	
	public boolean isDebugMode() {
		return debugMode;
	}
	
	/**
	 * Call when a block with cached save-delegate data is added to the multiblock. The part will be notified that the data has been used after this call completes.
	 *
	 * @param part The NBT tag containing this multiblock's data.
	 */
	public abstract void onAttachedPartWithMultiblockData(T part, NBTTagCompound data);
	
	public void attachBlockRaw(ITileMultiblockPart<?, ?> part) {
		if (tClass.isInstance(part)) {
			attachBlock(tClass.cast(part));
		}
	}
	
	/**
	 * Attach a new part to this machine.
	 *
	 * @param part The part to add.
	 */
	public void attachBlock(T part) {
		// IMultiblockPart candidate;
		BlockPos coord = part.getTilePos();
		
		if (!connectedParts.add(part)) {
			FMLLog.warning("[%s] Multiblock %s is double-adding part %d @ %s. This is unusual. If you encounter odd behavior, please tear down the multiblock and rebuild it.", WORLD.isRemote ? "CLIENT" : "SERVER", hashCode(), part.hashCode(), coord);
		}
		
		part.onAttached(multiblockClass.cast(this));
		onBlockAdded(part);
		
		if (part.hasMultiblockSaveData()) {
			NBTTagCompound savedData = part.getMultiblockSaveData();
			onAttachedPartWithMultiblockData(part, savedData);
			part.onMultiblockDataAssimilated();
		}
		
		TileEntity te = referenceCoord == null ? null : WORLD.getTileEntity(referenceCoord);
		if (!tClass.isInstance(te)) {
			referenceCoord = coord;
			part.becomeMultiblockSaveDelegate();
		}
		else if (coord.compareTo(referenceCoord) < 0) {
			tClass.cast(te).forfeitMultiblockSaveDelegate();
			referenceCoord = coord;
			part.becomeMultiblockSaveDelegate();
		}
		else {
			part.forfeitMultiblockSaveDelegate();
		}
		
		BlockPos partPos = part.getTilePos();
		int curX, curY, curZ;
		int newX, newY, newZ;
		int partCoord;
		
		if (minimumCoord != null) {
			
			curX = minimumCoord.getX();
			curY = minimumCoord.getY();
			curZ = minimumCoord.getZ();
			
			partCoord = partPos.getX();
			newX = Math.min(partCoord, curX);
			
			partCoord = partPos.getY();
			newY = Math.min(partCoord, curY);
			
			partCoord = partPos.getZ();
			newZ = Math.min(partCoord, curZ);
			
			if (newX != curX || newY != curY || newZ != curZ) {
				minimumCoord = new BlockPos(newX, newY, newZ);
			}
		}
		
		if (maximumCoord != null) {
			
			curX = maximumCoord.getX();
			curY = maximumCoord.getY();
			curZ = maximumCoord.getZ();
			
			partCoord = partPos.getX();
			newX = Math.max(partCoord, curX);
			
			partCoord = partPos.getY();
			newY = Math.max(partCoord, curY);
			
			partCoord = partPos.getZ();
			newZ = Math.max(partCoord, curZ);
			
			if (newX != curX || newY != curY || newZ != curZ) {
				maximumCoord = new BlockPos(newX, newY, newZ);
			}
		}
		
		MultiblockRegistry.INSTANCE.addDirtyMultiblock(WORLD, multiblockClass.cast(this));
	}
	
	/**
	 * Called when a new part is added to the machine. Good time to register things into lists.
	 *
	 * @param newPart The part being added.
	 */
	protected abstract void onBlockAdded(T newPart);
	
	/**
	 * Called when a part is removed from the machine. Good time to clean up lists.
	 *
	 * @param oldPart The part being removed.
	 */
	protected abstract void onBlockRemoved(T oldPart);
	
	/**
	 * Called when a machine is assembled from a disassembled state.
	 */
	protected abstract void onMachineAssembled();
	
	/**
	 * Called when a machine is restored to the assembled state from a paused state.
	 */
	protected abstract void onMachineRestored();
	
	/**
	 * Called when a machine is paused from an assembled state This generally only happens due to chunk-loads and other "system" events.
	 */
	protected abstract void onMachinePaused();
	
	/**
	 * Called when a machine is disassembled from an assembled state. This happens due to user or in-game actions (e.g. explosions)
	 */
	protected abstract void onMachineDisassembled();
	
	/**
	 * Callback whenever a part is removed (or will very shortly be removed) from a multiblock. Do housekeeping/callbacks, also nulls min/max coords.
	 *
	 * @param part The part being removed.
	 */
	private void onDetachBlock(T part) {
		// Strip out this part
		part.onDetached(multiblockClass.cast(this));
		onBlockRemoved(part);
		part.forfeitMultiblockSaveDelegate();
		
		minimumCoord = maximumCoord = null;
		
		if (referenceCoord != null && referenceCoord.equals(part.getTilePos())) {
			referenceCoord = null;
		}
		
		shouldCheckForDisconnections = true;
	}
	
	/**
	 * Call to detach a block from this machine. Generally, this should be called when the tile entity is being released, e.g. on block destruction.
	 *
	 * @param part           The part to detach from this machine.
	 * @param chunkUnloading Is this entity detaching due to the chunk unloading? If true, the multiblock will be paused instead of broken.
	 */
	public void detachBlock(T part, boolean chunkUnloading) {
		if (chunkUnloading && assemblyState == AssemblyState.Assembled) {
			assemblyState = AssemblyState.Paused;
			onMachinePaused();
		}
		
		// Strip out this part
		onDetachBlock(part);
		if (!connectedParts.remove(part)) {
			BlockPos position = part.getTilePos();
			
			FMLLog.warning("[%s] Double-removing part (%d) @ %d, %d, %d, this is unexpected and may cause problems. If you encounter anomalies, please tear down the multiblock and rebuild it.", WORLD.isRemote ? "CLIENT" : "SERVER", part.hashCode(), position.getX(), position.getY(), position.getZ());
		}
		
		if (connectedParts.isEmpty()) {
			// Destroy/unregister
			MultiblockRegistry.INSTANCE.addDeadMultiblock(WORLD, multiblockClass.cast(this));
			return;
		}
		
		MultiblockRegistry.INSTANCE.addDirtyMultiblock(WORLD, multiblockClass.cast(this));
		
		// Find new save delegate if we need to.
		if (referenceCoord == null) {
			selectNewReferenceCoord();
		}
	}
	
	/**
	 * Helper method so we don't check for a whole machine until we have enough blocks to actually assemble it. This isn't as simple as xmax*ymax*zmax for non-cubic machines or for machines with hollow/complex interiors.
	 *
	 * @return The minimum number of blocks connected to the machine for it to be assembled.
	 */
	protected abstract int getMinimumNumberOfBlocksForAssembledMachine();
	
	/**
	 * Returns the maximum X dimension size of the machine, or -1 (DIMENSION_UNBOUNDED) to disable dimension checking in X. (This is not recommended.)
	 *
	 * @return The maximum X dimension size of the machine, or -1
	 */
	protected abstract int getMaximumXSize();
	
	/**
	 * Returns the maximum Z dimension size of the machine, or -1 (DIMENSION_UNBOUNDED) to disable dimension checking in Z. (This is not recommended.)
	 *
	 * @return The maximum Z dimension size of the machine, or -1
	 */
	protected abstract int getMaximumZSize();
	
	/**
	 * Returns the maximum Y dimension size of the machine, or -1 (DIMENSION_UNBOUNDED) to disable dimension checking in Y. (This is not recommended.)
	 *
	 * @return The maximum Y dimension size of the machine, or -1
	 */
	protected abstract int getMaximumYSize();
	
	/**
	 * Returns the minimum X dimension size of the machine. Must be at least 1, because nothing else makes sense.
	 *
	 * @return The minimum X dimension size of the machine
	 */
	protected int getMinimumXSize() {
		return 1;
	}
	
	/**
	 * Returns the minimum Y dimension size of the machine. Must be at least 1, because nothing else makes sense.
	 *
	 * @return The minimum Y dimension size of the machine
	 */
	protected int getMinimumYSize() {
		return 1;
	}
	
	/**
	 * Returns the minimum Z dimension size of the machine. Must be at least 1, because nothing else makes sense.
	 *
	 * @return The minimum Z dimension size of the machine
	 */
	protected int getMinimumZSize() {
		return 1;
	}
	
	/**
	 * @return the last validation error encountered when trying to assemble the multiblock, or null if there is no error.
	 */
	public MultiblockValidationError getLastError() {
		return lastValidationError;
	}
	
	/**
	 * Set a validation error
	 *
	 * @param error the error
	 */
	public void setLastError(MultiblockValidationError error) {
		if (error == null) {
			throw new IllegalArgumentException("The validation error can't be null");
		}
		lastValidationError = error;
	}
	
	/**
	 * Set a validation error
	 *
	 * @param messageFormatStringResourceKey a translation key for a message or a message format string
	 * @param messageParameters              optional parameters for a message format string
	 */
	public void setLastError(String messageFormatStringResourceKey, BlockPos pos, Object... messageParameters) {
		lastValidationError = new MultiblockValidationError(messageFormatStringResourceKey, pos, messageParameters);
	}
	
	/**
	 * Checks if a machine is whole. If not, set a validation error using IMultiblockValidator.
	 */
	protected abstract boolean isMachineWhole();
	
	/**
	 * Check if the machine is whole or not. If the machine was not whole, but now is, assemble the machine. If the machine was whole, but no longer is, disassemble the machine.
	 */
	public void checkIfMachineIsWhole() {
		AssemblyState oldState = assemblyState;
		
		lastValidationError = null;
		
		if (isMachineWhole()) {
			// This will alter assembly state
			assembleMachine(oldState);
		}
		else if (oldState == AssemblyState.Assembled) {
			// This will alter assembly state
			_disassembleMachine();
		}
		// Else Paused, do nothing
	}
	
	/**
	 * Called when a machine becomes "whole" and should begin functioning as a game-logically finished machine. Calls onMachineAssembled on all attached parts.
	 */
	private void assembleMachine(AssemblyState oldState) {
		for (T part : connectedParts) {
			part.onMachineAssembled(multiblockClass.cast(this));
		}
		
		assemblyState = AssemblyState.Assembled;
		if (oldState == AssemblyState.Paused) {
			onMachineRestored();
		}
		else {
			onMachineAssembled();
		}
	}
	
	/**
	 * Called when the machine needs to be disassembled. It is not longer "whole" and should not be functional, usually as a result of a block being removed. Calls onMachineBroken on all attached parts.
	 */
	protected void _disassembleMachine() {
		for (T part : connectedParts) {
			part.onMachineBroken();
		}
		
		assemblyState = AssemblyState.Disassembled;
		onMachineDisassembled();
	}
	
	public void assimilateRaw(Multiblock<?, ?> other) {
		if (multiblockClass.isInstance(other)) {
			assimilate(multiblockClass.cast(other));
		}
	}
	
	/**
	 * Assimilate another multiblock into this multiblock. Acquire all the other multiblock's blocks and attach them to this one.
	 *
	 * @param other The multiblock to merge into this one.
	 */
	public void assimilate(MULTIBLOCK other) {
		BlockPos otherReferenceCoord = other.getReferenceCoord();
		if (otherReferenceCoord != null && getReferenceCoord().compareTo(otherReferenceCoord) >= 0) {
			throw new IllegalArgumentException("The multiblock with the lowest minimum-coord value must consume the one with the higher coords");
		}
		
		Set<T> partsToAcquire = new ObjectOpenHashSet<>(other.connectedParts);
		
		// Force disassembly of assimilated multiblock
		if (other.assemblyState == AssemblyState.Assembled) {
			other._disassembleMachine();
		}
		
		// Releases all blocks and references gently, so they can be incorporated into another multiblock
		other._onAssimilated(multiblockClass.cast(this));
		
		for (T acquiredPart : partsToAcquire) {
			// By definition, none of these can be the minimum block.
			if (acquiredPart.isPartInvalid()) {
				continue;
			}
			
			connectedParts.add(acquiredPart);
			acquiredPart.onAssimilated(multiblockClass.cast(this));
			onBlockAdded(acquiredPart);
		}
		
		onAssimilate(other);
		other.onAssimilated(multiblockClass.cast(this));
	}
	
	/**
	 * Called when this machine is consumed by another multiblock. Essentially, forcibly tear down this object.
	 *
	 * @param otherMultiblock The multiblock consuming this multiblock.
	 */
	protected void _onAssimilated(MULTIBLOCK otherMultiblock) {
		if (referenceCoord != null) {
			if (WORLD.isBlockLoaded(referenceCoord)) {
				TileEntity te = WORLD.getTileEntity(referenceCoord);
				if (tClass.isInstance(te)) {
					tClass.cast(te).forfeitMultiblockSaveDelegate();
				}
			}
			referenceCoord = null;
		}
		
		connectedParts.clear();
	}
	
	/**
	 * Callback. Called after this multiblock assimilates all the blocks from another multiblock. Use this to absorb that multiblock's game data.
	 *
	 * @param assimilated The multiblock whose uniqueness was added to our own.
	 */
	protected abstract void onAssimilate(MULTIBLOCK assimilated);
	
	/**
	 * Callback. Called after this multiblock is assimilated into another multiblock. All blocks have been stripped out of this object and handed over to the other multiblock. This is intended primarily for cleanup.
	 *
	 * @param assimilator The multiblock which has assimilated this multiblock.
	 */
	protected abstract void onAssimilated(MULTIBLOCK assimilator);
	
	/**
	 * Driver for the update loop. If the machine is assembled, runs the game logic update method.
	 *
	 * @see nc.multiblock.Multiblock#updateServer()
	 */
	public final void updateMultiblockEntity() {
		if (connectedParts.isEmpty()) {
			// This shouldn't happen, but just in case...
			MultiblockRegistry.INSTANCE.addDeadMultiblock(WORLD, multiblockClass.cast(this));
			return;
		}
		
		if (assemblyState != AssemblyState.Assembled) {
			// Not assembled - don't run game logic
			return;
		}
		
		if (WORLD.isRemote) {
			updateClient();
		}
		else if (updateServer()) {
			// If this returns true, the server has changed its internal data.
			// If our chunks are loaded (they should be), we must mark our chunks as dirty.
			markChunksDirty();
		}
		// Else: Server, but no need to save data.
	}
	
	public void markChunksDirty() {
		if (minimumCoord != null && maximumCoord != null && WORLD.isAreaLoaded(minimumCoord, maximumCoord)) {
			
			int minChunkX = MultiblockWorldHelper.getChunkXFromBlock(minimumCoord);
			int minChunkZ = MultiblockWorldHelper.getChunkZFromBlock(minimumCoord);
			int maxChunkX = MultiblockWorldHelper.getChunkXFromBlock(maximumCoord);
			int maxChunkZ = MultiblockWorldHelper.getChunkZFromBlock(maximumCoord);
			
			for (int x = minChunkX; x <= maxChunkX; ++x) {
				for (int z = minChunkZ; z <= maxChunkZ; ++z) {
					// Ensure that we save our data, even if the save delegate is in has no TEs.
					Chunk chunkToSave = WORLD.getChunk(x, z);
					chunkToSave.markDirty();
				}
			}
		}
	}
	
	/**
	 * The server-side update loop! Use this similarly to a TileEntity's update loop. You do not need to call your superclass' update() if you're directly derived from MultiblockBase. This is a callback. Note that this will only be called when the machine is assembled.
	 *
	 * @return True if the multiblock should save data, i.e. its internal game state has changed. False otherwise.
	 */
	protected abstract boolean updateServer();
	
	/**
	 * Client-side update loop. Generally, this shouldn't do anything, but if you want to do some interpolation or something, do it here.
	 */
	protected abstract void updateClient();
	
	// Validation helpers
	
	public boolean standardLastError(BlockPos pos) {
		setLastError(Global.MOD_ID + ".multiblock_validation.invalid_block", pos, pos.getX(), pos.getY(), pos.getZ(), WORLD.getBlockState(pos).getBlock().getLocalizedName());
		return false;
	}
	
	protected boolean isBlockGoodForFrame(World world, BlockPos pos) {
		return standardLastError(pos);
	}
	
	protected boolean isBlockGoodForTop(World world, BlockPos pos) {
		return standardLastError(pos);
	}
	
	protected boolean isBlockGoodForBottom(World world, BlockPos pos) {
		return standardLastError(pos);
	}
	
	protected boolean isBlockGoodForSides(World world, BlockPos pos) {
		return standardLastError(pos);
	}
	
	protected abstract boolean isBlockGoodForInterior(World world, BlockPos pos);
	
	/**
	 * @return The reference coordinate, the block with the lowest x, y, z coordinates, evaluated in that order.
	 */
	public BlockPos getReferenceCoord() {
		if (referenceCoord == null) {
			selectNewReferenceCoord();
		}
		return referenceCoord;
	}
	
	/**
	 * @return The number of blocks connected to this multiblock.
	 */
	public int getNumConnectedBlocks() {
		return connectedParts.size();
	}
	
	// Data synchronization
	
	/**
	 * Sync multiblock data from the given NBT compound
	 *
	 * @param data       the data
	 * @param syncReason the reason why the synchronization is necessary
	 */
	public abstract void syncDataFrom(NBTTagCompound data, TilePartAbstract.SyncReason syncReason);
	
	/**
	 * Sync multiblock data to the given NBT compound
	 *
	 * @param data       the data
	 * @param syncReason the reason why the synchronization is necessary
	 */
	public abstract void syncDataTo(NBTTagCompound data, TilePartAbstract.SyncReason syncReason);
	
	public NBTTagCompound writeStacks(NonNullList<ItemStack> stacks, NBTTagCompound data) {
		ItemStackHelper.saveAllItems(data, stacks);
		return data;
	}
	
	public void readStacks(NonNullList<ItemStack> stacks, NBTTagCompound data) {
		ItemStackHelper.loadAllItems(data, stacks);
	}
	
	public NBTTagCompound writeTanks(List<Tank> tanks, NBTTagCompound data, String name) {
		for (int i = 0; i < tanks.size(); ++i) {
			tanks.get(i).writeToNBT(data, name + i);
		}
		return data;
	}
	
	public void readTanks(List<Tank> tanks, NBTTagCompound data, String name) {
		for (int i = 0; i < tanks.size(); ++i) {
			tanks.get(i).readFromNBT(data, name + i);
		}
	}
	
	public NBTTagCompound writeEnergy(EnergyStorage storage, NBTTagCompound data, String string) {
		storage.writeToNBT(data, string);
		return data;
	}
	
	public void readEnergy(EnergyStorage storage, NBTTagCompound data, String string) {
		storage.readFromNBT(data, string);
	}
	
	/**
	 * Force this multiblock to recalculate its minimum and maximum coordinates from the list of connected parts.
	 */
	public void recalculateMinMaxCoords() {
		
		int minX, minY, minZ, maxX, maxY, maxZ;
		int partCoord;
		BlockPos partPos;
		
		minX = minY = minZ = Integer.MAX_VALUE;
		maxX = maxY = maxZ = Integer.MIN_VALUE;
		
		for (T part : connectedParts) {
			
			partPos = part.getTilePos();
			
			partCoord = partPos.getX();
			if (partCoord < minX) {
				minX = partCoord;
			}
			if (partCoord > maxX) {
				maxX = partCoord;
			}
			
			partCoord = partPos.getY();
			if (partCoord < minY) {
				minY = partCoord;
			}
			if (partCoord > maxY) {
				maxY = partCoord;
			}
			
			partCoord = partPos.getZ();
			if (partCoord < minZ) {
				minZ = partCoord;
			}
			if (partCoord > maxZ) {
				maxZ = partCoord;
			}
		}
		
		minimumCoord = new BlockPos(minX, minY, minZ);
		maximumCoord = new BlockPos(maxX, maxY, maxZ);
	}
	
	/**
	 * @return The minimum bounding-box coordinate containing this machine's blocks.
	 */
	public BlockPos getMinimumCoord() {
		if (minimumCoord == null) {
			recalculateMinMaxCoords();
		}
		return minimumCoord;
	}
	
	/**
	 * @return The maximum bounding-box coordinate containing this machine's blocks.
	 */
	public BlockPos getMaximumCoord() {
		if (maximumCoord == null) {
			recalculateMinMaxCoords();
		}
		return maximumCoord;
	}
	
	public int getMinX() {
		return getMinimumCoord().getX();
	}
	
	public int getMinY() {
		return getMinimumCoord().getY();
	}
	
	public int getMinZ() {
		return getMinimumCoord().getZ();
	}
	
	public int getMaxX() {
		return getMaximumCoord().getX();
	}
	
	public int getMaxY() {
		return getMaximumCoord().getY();
	}
	
	public int getMaxZ() {
		return getMaximumCoord().getZ();
	}
	
	public BlockPos getExtremeCoord(boolean maxX, boolean maxY, boolean maxZ) {
		return new BlockPos(maxX ? getMaxX() : getMinX(), maxY ? getMaxY() : getMinY(), maxZ ? getMaxZ() : getMinZ());
	}
	
	public int getMiddleX() {
		return NCMath.toInt(((long) getMinX() + (long) getMaxX()) / 2);
	}
	
	public int getMiddleY() {
		return NCMath.toInt(((long) getMinY() + (long) getMaxY()) / 2);
	}
	
	public int getMiddleZ() {
		return NCMath.toInt(((long) getMinZ() + (long) getMaxZ()) / 2);
	}
	
	public BlockPos getMiddleCoord() {
		return new BlockPos(getMiddleX(), getMiddleY(), getMiddleZ());
	}
	
	// public abstract void formatDescriptionPacket(NBTTagCompound data);
	
	// public abstract void decodeDescriptionPacket(NBTTagCompound data);
	
	/**
	 * @return True if this multiblock has no associated blocks, false otherwise
	 */
	public boolean isEmpty() {
		return connectedParts.isEmpty();
	}
	
	/**
	 * Tests whether this multiblock should consume the other multiblock and become the new multiblock master when the two multiblocks are adjacent. Assumes both multiblocks are the same type.
	 *
	 * @param otherMultiblock The other multiblock.
	 * @return True if this multiblock should consume the other, false otherwise.
	 */
	public boolean shouldConsume(@SuppressWarnings("rawtypes") Multiblock otherMultiblock) {
		if (!otherMultiblock.getClass().equals(getClass())) {
			throw new IllegalArgumentException("Attempting to merge two multiblocks with different master classes - this should never happen!");
		}
		
		if (otherMultiblock == this) {
			return false;
		} // Don't be silly, don't eat yourself.
		
		int res = _shouldConsume(otherMultiblock);
		if (res < 0) {
			return true;
		}
		else if (res > 0) {
			return false;
		}
		else {
			// Strip dead parts from both and retry
			FMLLog.warning("[%s] Encountered two multiblocks with the same reference coordinate. Auditing connected parts and retrying.", WORLD.isRemote ? "CLIENT" : "SERVER");
			auditParts();
			otherMultiblock.auditParts();
			
			res = _shouldConsume(otherMultiblock);
			if (res < 0) {
				return true;
			}
			else if (res > 0) {
				return false;
			}
			else {
				FMLLog.severe("My Multiblock (%d): size (%d), parts: %s", hashCode(), connectedParts.size(), getPartsListString());
				FMLLog.severe("Other Multiblock (%d): size (%d), coords: %s", otherMultiblock.hashCode(), otherMultiblock.connectedParts.size(), otherMultiblock.getPartsListString());
				throw new IllegalArgumentException("[" + (WORLD.isRemote ? "CLIENT" : "SERVER") + "] Two multiblocks with the same reference coord that somehow both have valid parts - this should never happen!");
			}
			
		}
	}
	
	private int _shouldConsume(@SuppressWarnings("rawtypes") Multiblock otherMultiblock) {
		BlockPos myCoord = getReferenceCoord();
		BlockPos theirCoord = otherMultiblock.getReferenceCoord();
		
		// Always consume other multiblocks if their reference coordinate is
		// null - this means they're empty and can be assimilated on the cheap
		if (theirCoord == null) {
			return -1;
		}
		else {
			return myCoord.compareTo(theirCoord);
		}
	}
	
	private String getPartsListString() {
		StringBuilder sb = new StringBuilder();
		boolean first = true;
		BlockPos partPos;
		for (T part : connectedParts) {
			if (!first) {
				sb.append(", ");
			}
			partPos = part.getTilePos();
			sb.append(String.format("(%d: %d, %d, %d)", part.hashCode(), partPos.getX(), partPos.getY(), partPos.getZ()));
			first = false;
		}
		
		return sb.toString();
	}
	
	/**
	 * Checks all the parts in the multiblock. If any are dead or do not exist in the world, they are removed.
	 */
	private void auditParts() {
		ObjectOpenHashSet<T> deadParts = new ObjectOpenHashSet<>();
		for (T part : connectedParts) {
			if (part.isPartInvalid() || WORLD.getTileEntity(part.getTilePos()) != part) {
				onDetachBlock(part);
				deadParts.add(part);
			}
		}
		
		connectedParts.removeAll(deadParts);
		FMLLog.warning("[%s] Multiblock found %d dead parts during an audit, %d parts remain attached", WORLD.isRemote ? "CLIENT" : "SERVER", deadParts.size(), connectedParts.size());
	}
	
	/**
	 * Called when this machine may need to check for blocks that are no longer physically connected to the reference coordinate.
	 */
	public Set<T> checkForDisconnections() {
		if (!shouldCheckForDisconnections) {
			return null;
		}
		
		if (isEmpty()) {
			MultiblockRegistry.INSTANCE.addDeadMultiblock(WORLD, multiblockClass.cast(this));
			return null;
		}
		
		// TileEntity te;
		// IChunkProvider chunkProvider = WORLD.getChunkProvider();
		
		// Invalidate our reference coord, we'll recalculate it shortly
		referenceCoord = null;
		
		// Reset visitations and find the minimum coordinate
		Set<T> deadParts = new ObjectOpenHashSet<>();
		BlockPos position;
		T referencePart = null;
		
		int originalSize = connectedParts.size();
		
		for (T part : connectedParts) {
			position = part.getTilePos();
			// This happens during chunk unload.
			if (!WORLD.isBlockLoaded(position) || part.isPartInvalid()) {
				deadParts.add(part);
				onDetachBlock(part);
				continue;
			}
			
			if (WORLD.getTileEntity(position) != part) {
				deadParts.add(part);
				onDetachBlock(part);
				continue;
			}
			
			part.setUnvisited();
			part.forfeitMultiblockSaveDelegate();
			
			if (referenceCoord == null) {
				referenceCoord = position;
				referencePart = part;
			}
			else if (position.compareTo(referenceCoord) < 0) {
				referenceCoord = position;
				referencePart = part;
			}
		}
		
		connectedParts.removeAll(deadParts);
		deadParts.clear();
		
		if (referencePart == null || isEmpty()) {
			// There are no valid parts remaining. The entire multiblock was
			// unloaded during a chunk unload. Halt.
			shouldCheckForDisconnections = false;
			MultiblockRegistry.INSTANCE.addDeadMultiblock(WORLD, multiblockClass.cast(this));
			return null;
		}
		else {
			referencePart.becomeMultiblockSaveDelegate();
		}
		
		// Now visit all connected parts, breadth-first, starting from reference
		// coord's part
		T part;
		LinkedList<T> partsToCheck = new LinkedList<>();
		List<T> nearbyParts = null;
		int visitedParts = 0;
		
		partsToCheck.add(referencePart);
		
		while (!partsToCheck.isEmpty()) {
			part = partsToCheck.removeFirst();
			part.setVisited();
			++visitedParts;
			
			nearbyParts = part.getNeighboringParts(); // Chunk-safe on server,
			// but not on client
			for (T nearbyPart : nearbyParts) {
				// Ignore different machines
				if (nearbyPart.getMultiblock() != this) {
					continue;
				}
				
				if (!nearbyPart.isVisited()) {
					nearbyPart.setVisited();
					partsToCheck.add(nearbyPart);
				}
			}
		}
		
		// Finally, remove all parts that remain disconnected.
		Set<T> removedParts = new ObjectOpenHashSet<>();
		for (T orphanCandidate : connectedParts) {
			if (!orphanCandidate.isVisited()) {
				deadParts.add(orphanCandidate);
				orphanCandidate.onOrphaned(multiblockClass.cast(this), originalSize, visitedParts);
				onDetachBlock(orphanCandidate);
				removedParts.add(orphanCandidate);
			}
		}
		
		// Trim any blocks that were invalid, or were removed.
		connectedParts.removeAll(deadParts);
		
		// Cleanup. Not necessary, really.
		deadParts.clear();
		
		// Just in case.
		if (referenceCoord == null) {
			selectNewReferenceCoord();
		}
		
		// We've run the checks from here on out.
		shouldCheckForDisconnections = false;
		
		return removedParts;
	}
	
	/**
	 * Detach all parts. Return a set of all parts which still have a valid tile entity. Chunk-safe.
	 *
	 * @return A set of all parts which still have a valid tile entity.
	 */
	public Set<T> detachAllBlocks() {
		if (WORLD == null) {
			return new ObjectOpenHashSet<>();
		}
		
		// IChunkProvider chunkProvider = WORLD.getChunkProvider();
		for (T part : connectedParts) {
			if (WORLD.isBlockLoaded(part.getTilePos())) {
				onDetachBlock(part);
			}
		}
		
		Set<T> detachedParts = connectedParts;
		connectedParts = new ObjectOpenHashSet<>();
		return detachedParts;
	}
	
	/**
	 * @return True if this multiblock machine is considered assembled and ready to go.
	 */
	public boolean isAssembled() {
		return assemblyState == AssemblyState.Assembled;
	}
	
	private void selectNewReferenceCoord() {
		// IChunkProvider chunkProvider = WORLD.getChunkProvider();
		T theChosenOne = null;
		BlockPos position;
		
		referenceCoord = null;
		
		for (T part : connectedParts) {
			position = part.getTilePos();
			if (part.isPartInvalid() || !WORLD.isBlockLoaded(position)) {
				// Chunk is unloading, skip this coord to prevent chunk
				// thrashing
				continue;
			}
			
			if (referenceCoord == null || referenceCoord.compareTo(position) > 0) {
				referenceCoord = position;
				theChosenOne = part;
			}
		}
		
		if (theChosenOne != null) {
			theChosenOne.becomeMultiblockSaveDelegate();
		}
	}
	
	/**
	 * Marks the reference coord dirty & updatable.
	 * On the server, this will mark the for a data-update, so that nearby clients will receive an updated description packet from the server after a short time. The block's chunk will also be marked dirty and the block's chunk will be saved to disk the next time chunks are saved.
	 * On the client, this will mark the block for a rendering update.
	 */
	public void markReferenceCoordForUpdate() {
		
		BlockPos rc = getReferenceCoord();
		
		if (WORLD != null && rc != null) {
			IBlockState state = WORLD.getBlockState(rc);
			WORLD.notifyBlockUpdate(rc, state, state, 3);
		}
	}
	
	/**
	 * Marks the reference coord dirty.
	 * On the server, this marks the reference coord's chunk as dirty; the block (and chunk) will be saved to disk the next time chunks are saved. This does NOT mark it dirty for a description-packet update.
	 * On the client, does nothing.
	 *
	 * @see Multiblock#markReferenceCoordForUpdate()
	 */
	public void markReferenceCoordDirty() {
		if (WORLD == null || WORLD.isRemote) {
			return;
		}
		
		BlockPos referencePos = getReferenceCoord();
		if (referencePos == null) {
			return;
		}
		
		TileEntity saveTe = WORLD.getTileEntity(referencePos);
		WORLD.markChunkDirty(referencePos, saveTe);
	}
	
	/**
	 * Marks the whole multiblock for a render update on the client. On the server, this does nothing
	 */
	public void markMultiblockForRenderUpdate() {
		WORLD.markBlockRangeForRenderUpdate(getMinimumCoord(), getMaximumCoord());
	}
	
	// Multiblock Parts
	
	public static class PartSuperMap<MULTIBLOCK extends Multiblock<MULTIBLOCK, T>, T extends ITileMultiblockPart<MULTIBLOCK, T>> extends SuperMap<Long, T, Long2ObjectMap<? extends T>> {
		
		@Override
		public <TYPE extends T> Long2ObjectMap<? extends T> backup(Class<TYPE> clazz) {
			return new Long2ObjectOpenHashMap<>();
		}
	}
	
	public abstract PartSuperMap<MULTIBLOCK, T> getPartSuperMap();
	
	public <TYPE extends T> Long2ObjectMap<TYPE> getPartMap(Class<TYPE> type) {
		return getPartSuperMap().get(type);
	}
	
	public <TYPE extends T> int getPartCount(Class<TYPE> type) {
		return getPartMap(type).size();
	}
	
	public <TYPE extends T> Collection<TYPE> getParts(Class<TYPE> type) {
		return getPartMap(type).values();
	}
	
	public <TYPE extends T> Iterator<TYPE> getPartIterator(Class<TYPE> type) {
		return getParts(type).iterator();
	}
	
	@SuppressWarnings({"unchecked", "rawtypes"})
	public void onPartAdded(T newPart) {
		for (Entry<Class<? extends T>, Long2ObjectMap<? extends T>> superMapEntryRaw : getPartSuperMap().entrySet()) {
			addBlockForSuperMapEntry(new SuperMapEntry(superMapEntryRaw), newPart);
		}
	}
	
	public <TYPE extends T> void addBlockForSuperMapEntry(SuperMapEntry<Long, TYPE> superMapEntry, T newPart) {
		if (superMapEntry.getKey().isInstance(newPart)) {
			superMapEntry.getValue().put(newPart.getTilePos().toLong(), superMapEntry.getKey().cast(newPart));
		}
	}
	
	@SuppressWarnings({"unchecked", "rawtypes"})
	public void onPartRemoved(T oldPart) {
		for (Entry<Class<? extends T>, Long2ObjectMap<? extends T>> superMapEntryRaw : getPartSuperMap().entrySet()) {
			removeBlockForSuperMapEntry(new SuperMapEntry(superMapEntryRaw), oldPart);
		}
	}
	
	public <TYPE extends T> void removeBlockForSuperMapEntry(SuperMapEntry<Long, TYPE> superMapEntry, T oldPart) {
		if (superMapEntry.getKey().isInstance(oldPart)) {
			superMapEntry.getValue().remove(oldPart.getTilePos().toLong());
		}
	}
	
	// Clear Material
	
	public void clearAllMaterial() {
		for (Entry<Class<? extends T>, Long2ObjectMap<? extends T>> superMapEntryRaw : getPartSuperMap().entrySet()) {
			for (T tile : superMapEntryRaw.getValue().values()) {
				if (tile instanceof ITileInventory) {
					((ITileInventory) tile).clearAllSlots();
				}
				if (tile instanceof ITileFluid) {
					((ITileFluid) tile).clearAllTanks();
				}
			}
		}
	}
}
