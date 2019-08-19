package nc.multiblock;

import java.util.LinkedList;
import java.util.Random;
import java.util.Set;

import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import nc.Global;
import nc.NuclearCraft;
import nc.multiblock.network.MultiblockUpdatePacket;
import nc.multiblock.validation.IMultiblockValidator;
import nc.multiblock.validation.ValidationError;
import nc.network.PacketHandler;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.common.FMLLog;

/**
 * This class contains the base logic for "multiblocks". Conceptually, they are
 * meta-TileEntities. They govern the logic for an associated group of TileEntities.
 * 
 * Subordinate TileEntities implement the IMultiblockPart class and, generally, should not have an update() loop.
 */
public abstract class MultiblockBase<PACKET extends MultiblockUpdatePacket> implements IMultiblockValidator {
	public static final short DIMENSION_UNBOUNDED = -1;
	
	// Multiblock stuff - do not mess with
	public final World WORLD;
	
	// Disassembled -> Assembled; Assembled -> Disassembled OR Paused; Paused -> Assembled
	protected enum AssemblyState { Disassembled, Assembled, Paused }
	protected AssemblyState assemblyState;

	protected ObjectOpenHashSet<IMultiblockPart> connectedParts;
	
	public Random rand = new Random();
	
	/** This is a deterministically-picked coordinate that identifies this
	 * multiblock uniquely in its dimension.
	 * Currently, this is the coord with the lowest X, Y and Z coordinates, in that order of evaluation.
	 * i.e. If something has a lower X but higher Y/Z coordinates, it will still be the reference.
	 * If something has the same X but a lower Y coordinate, it will be the reference. Etc.
	 */
	private BlockPos referenceCoord;

	/**
	 * Minimum bounding box coordinate. Blocks do not necessarily exist at this coord if your machine
	 * is not a cube/rectangular prism.
	 */
	private BlockPos minimumCoord;

	/**
	 * Maximum bounding box coordinate. Blocks do not necessarily exist at this coord if your machine
	 * is not a cube/rectangular prism.
	 */
	private BlockPos maximumCoord;
	
	/**
	 * Set to true whenever a part is removed from this multiblock.
	 */
	private boolean shouldCheckForDisconnections;
	
	/**
	 * Set whenever we validate the multiblock
	 */
	private ValidationError lastValidationError;
	
	protected boolean debugMode;
	
	protected Set<EntityPlayer> playersToUpdate;
	
	protected MultiblockBase(World world) {
		// Multiblock stuff
		WORLD = world;
		connectedParts  = new ObjectOpenHashSet<IMultiblockPart>();

		referenceCoord = null;
		assemblyState = AssemblyState.Disassembled;

		minimumCoord = null;
		maximumCoord = null;

		shouldCheckForDisconnections = true;
		lastValidationError = null;
		
		debugMode = false;
		
		playersToUpdate = new ObjectOpenHashSet<EntityPlayer>();
	}

	public void setDebugMode(boolean active) {
		debugMode = active;
	}
	
	public boolean isDebugMode() {
		return debugMode;
	}
	
	/**
	 * Call when a block with cached save-delegate data is added to the multiblock.
	 * The part will be notified that the data has been used after this call completes.
	 * @param part The NBT tag containing this multiblock's data.
	 */
	public abstract void onAttachedPartWithMultiblockData(IMultiblockPart part, NBTTagCompound data);
	
	/**
	 * Check if a block is being tracked by this machine.
	 * @param blockCoord Coordinate to check.
	 * @return True if the tile entity at blockCoord is being tracked by this machine, false otherwise.
	 */
	public boolean hasBlock(BlockPos blockCoord) {
		return connectedParts.contains(blockCoord);
	}
	
	/**
	 * Attach a new part to this machine.
	 * @param part The part to add.
	 */
	public void attachBlock(IMultiblockPart part) {
		//IMultiblockPart candidate;
		BlockPos coord = part.getTilePos();

		if(!connectedParts.add(part))
			FMLLog.warning("[%s] Multiblock %s is double-adding part %d @ %s. This is unusual. If you encounter odd behavior, please tear down the machine and rebuild it.",
					(WORLD.isRemote ? "CLIENT" : "SERVER"), hashCode(), part.hashCode(), coord);

		part.onAttached(this);
		this.onBlockAdded(part);

		if(part.hasMultiblockSaveData()) {
			NBTTagCompound savedData = part.getMultiblockSaveData();
			onAttachedPartWithMultiblockData(part, savedData);
			part.onMultiblockDataAssimilated();
		}
		
		if(this.referenceCoord == null) {
			referenceCoord = coord;
			part.becomeMultiblockSaveDelegate();
		}
		else if(coord.compareTo(referenceCoord) < 0) {
			TileEntity te = this.WORLD.getTileEntity(referenceCoord);
			((IMultiblockPart)te).forfeitMultiblockSaveDelegate();
			
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

		if (this.minimumCoord != null) {

			curX = this.minimumCoord.getX();
			curY = this.minimumCoord.getY();
			curZ = this.minimumCoord.getZ();

			partCoord = partPos.getX();
			newX = partCoord < curX ? partCoord : curX;

			partCoord = partPos.getY();
			newY = partCoord < curY ? partCoord : curY;

			partCoord = partPos.getZ();
			newZ = partCoord < curZ ? partCoord : curZ;

			if ((newX != curX) || (newY != curY) || (newZ != curZ))
				this.minimumCoord = new BlockPos(newX, newY, newZ);
		}

		if (this.maximumCoord != null) {

			curX = this.maximumCoord.getX();
			curY = this.maximumCoord.getY();
			curZ = this.maximumCoord.getZ();

			partCoord = partPos.getX();
			newX = partCoord > curX ? partCoord : curX;

			partCoord = partPos.getY();
			newY = partCoord > curY ? partCoord : curY;

			partCoord = partPos.getZ();
			newZ = partCoord > curZ ? partCoord : curZ;

			if ((newX != curX) || (newY != curY) || (newZ != curZ))
				this.maximumCoord = new BlockPos(newX, newY, newZ);
		}

		REGISTRY.addDirtyMultiblock(WORLD, this);
	}

	/**
	 * Called when a new part is added to the machine. Good time to register things into lists.
	 * @param newPart The part being added.
	 */
	protected abstract void onBlockAdded(IMultiblockPart newPart);

	/**
	 * Called when a part is removed from the machine. Good time to clean up lists.
	 * @param oldPart The part being removed.
	 */
	protected abstract void onBlockRemoved(IMultiblockPart oldPart);
	
	/**
	 * Called when a machine is assembled from a disassembled state.
	 */
	protected abstract void onMachineAssembled();
	
	/**
	 * Called when a machine is restored to the assembled state from a paused state.
	 */
	protected abstract void onMachineRestored();

	/**
	 * Called when a machine is paused from an assembled state
	 * This generally only happens due to chunk-loads and other "system" events.
	 */
	protected abstract void onMachinePaused();
	
	/**
	 * Called when a machine is disassembled from an assembled state.
	 * This happens due to user or in-game actions (e.g. explosions)
	 */
	protected abstract void onMachineDisassembled();
	
	/**
	 * Callback whenever a part is removed (or will very shortly be removed) from a multiblock.
	 * Do housekeeping/callbacks, also nulls min/max coords.
	 * @param part The part being removed.
	 */
	private void onDetachBlock(IMultiblockPart part) {
		// Strip out this part
		part.onDetached(this);
		this.onBlockRemoved(part);
		part.forfeitMultiblockSaveDelegate();

		minimumCoord = maximumCoord = null;
		
		if(referenceCoord != null && referenceCoord.equals(part.getTilePos())) {
			referenceCoord = null;
		}
		
		shouldCheckForDisconnections = true;
	}
	
	/**
	 * Call to detach a block from this machine. Generally, this should be called
	 * when the tile entity is being released, e.g. on block destruction.
	 * @param part The part to detach from this machine.
	 * @param chunkUnloading Is this entity detaching due to the chunk unloading? If true, the multiblock will be paused instead of broken.
	 */
	public void detachBlock(IMultiblockPart part, boolean chunkUnloading) {
		if(chunkUnloading && this.assemblyState == AssemblyState.Assembled) {
			this.assemblyState = AssemblyState.Paused;
			this.onMachinePaused();
		}

		// Strip out this part
		onDetachBlock(part);
		if(!connectedParts.remove(part)) {
			BlockPos position = part.getTilePos();

			FMLLog.warning("[%s] Double-removing part (%d) @ %d, %d, %d, this is unexpected and may cause problems. If you encounter anomalies, please tear down the reactor and rebuild it.",
					this.WORLD.isRemote ? "CLIENT" : "SERVER", part.hashCode(), position.getX(), position.getY(), position.getZ());
		}

		if(connectedParts.isEmpty()) {
			// Destroy/unregister
			REGISTRY.addDeadMultiblock(this.WORLD, this);
			return;
		}

		REGISTRY.addDirtyMultiblock(this.WORLD,  this);

		// Find new save delegate if we need to.
		if(referenceCoord == null) {
			selectNewReferenceCoord();
		}
	}

	/**
	 * Helper method so we don't check for a whole machine until we have enough blocks
	 * to actually assemble it. This isn't as simple as xmax*ymax*zmax for non-cubic machines
	 * or for machines with hollow/complex interiors.
	 * @return The minimum number of blocks connected to the machine for it to be assembled.
	 */
	protected abstract int getMinimumNumberOfBlocksForAssembledMachine();

	/**
	 * Returns the maximum X dimension size of the machine, or -1 (DIMENSION_UNBOUNDED) to disable
	 * dimension checking in X. (This is not recommended.)
	 * @return The maximum X dimension size of the machine, or -1 
	 */
	protected abstract int getMaximumXSize();

	/**
	 * Returns the maximum Z dimension size of the machine, or -1 (DIMENSION_UNBOUNDED) to disable
	 * dimension checking in X. (This is not recommended.)
	 * @return The maximum Z dimension size of the machine, or -1 
	 */
	protected abstract int getMaximumZSize();

	/**
	 * Returns the maximum Y dimension size of the machine, or -1 (DIMENSION_UNBOUNDED) to disable
	 * dimension checking in X. (This is not recommended.)
	 * @return The maximum Y dimension size of the machine, or -1 
	 */
	protected abstract int getMaximumYSize();
	
	/**
	 * Returns the minimum X dimension size of the machine. Must be at least 1, because nothing else makes sense.
	 * @return The minimum X dimension size of the machine
	 */
	protected int getMinimumXSize() { return 1; }

	/**
	 * Returns the minimum Y dimension size of the machine. Must be at least 1, because nothing else makes sense.
	 * @return The minimum Y dimension size of the machine
	 */
	protected int getMinimumYSize() { return 1; }

	/**
	 * Returns the minimum Z dimension size of the machine. Must be at least 1, because nothing else makes sense.
	 * @return The minimum Z dimension size of the machine
	 */
	protected int getMinimumZSize() { return 1; }

	/**
	 * @return the last validation error encountered when trying to assemble the multiblock, or null if there is no error.
	 */
	@Override
	public ValidationError getLastError() {
		return this.lastValidationError;
	}

	/**
	 * Set a validation error
	 * @param error the error
	 */
	@Override
	public void setLastError(ValidationError error) {

		if (null == error)
			throw new IllegalArgumentException("The validation error can't be null");

		this.lastValidationError = error;
	}

	/**
	 * Set a validation error
	 * @param messageFormatStringResourceKey a translation key for a message or a message format string
	 * @param messageParameters optional parameters for a message format string
	 */
	@Override
	public void setLastError(String messageFormatStringResourceKey, BlockPos pos, Object... messageParameters) {
		this.lastValidationError = new ValidationError(messageFormatStringResourceKey, pos, messageParameters);
	}
	
	/**
	 * Checks if a machine is whole. If not, set a validation error using IMultiblockValidator.
	 */
	protected abstract boolean isMachineWhole(IMultiblockValidator validatorCallback);
	
	/**
	 * Check if the machine is whole or not.
	 * If the machine was not whole, but now is, assemble the machine.
	 * If the machine was whole, but no longer is, disassemble the machine.
	 * @return 
	 */
	public void checkIfMachineIsWhole() {
		AssemblyState oldState = this.assemblyState;

		this.lastValidationError = null;

		if (this.isMachineWhole(this)) {
			// This will alter assembly state
			assembleMachine(oldState);
		}
		else if(oldState == AssemblyState.Assembled) {
			// This will alter assembly state
			disassembleMachine();
		}
		// Else Paused, do nothing
	}
	
	/**
	 * Called when a machine becomes "whole" and should begin
	 * functioning as a game-logically finished machine.
	 * Calls onMachineAssembled on all attached parts.
	 */
	private void assembleMachine(AssemblyState oldState) {
		for(IMultiblockPart part : connectedParts) {
			part.onMachineAssembled(this);
		}
		
		this.assemblyState = AssemblyState.Assembled;
		if(oldState == AssemblyState.Paused) {
			onMachineRestored();
		}
		else {
			onMachineAssembled();
		}
	}
	
	/**
	 * Called when the machine needs to be disassembled.
	 * It is not longer "whole" and should not be functional, usually
	 * as a result of a block being removed.
	 * Calls onMachineBroken on all attached parts.
	 */
	private void disassembleMachine() {
		for(IMultiblockPart part : connectedParts) {
			part.onMachineBroken();
		}
		
		this.assemblyState = AssemblyState.Disassembled;
		onMachineDisassembled();
	}
	
	/**
	 * Assimilate another multiblock into this multiblock.
	 * Acquire all of the other multiblock's blocks and attach them
	 * to this one.
	 * 
	 * @param other The multiblock to merge into this one.
	 */
	public void assimilate(MultiblockBase other) {
		BlockPos otherReferenceCoord = other.getReferenceCoord();
		if(otherReferenceCoord != null && getReferenceCoord().compareTo(otherReferenceCoord) >= 0) {
			throw new IllegalArgumentException("The multiblock with the lowest minimum-coord value must consume the one with the higher coords");
		}

		//TileEntity te;
		Set<IMultiblockPart> partsToAcquire = new ObjectOpenHashSet<IMultiblockPart>(other.connectedParts);

		// releases all blocks and references gently so they can be incorporated into another multiblock
		other._onAssimilated(this);
		
		for(IMultiblockPart acquiredPart : partsToAcquire) {
			// By definition, none of these can be the minimum block.
			if(acquiredPart.isPartInvalid()) { continue; }
			
			connectedParts.add(acquiredPart);
			acquiredPart.onAssimilated(this);
			this.onBlockAdded(acquiredPart);
		}

		this.onAssimilate(other);
		other.onAssimilated(this);
	}
	
	/**
	 * Called when this machine is consumed by another multiblock.
	 * Essentially, forcibly tear down this object.
	 * @param otherMultiblock The multiblock consuming this multiblock.
	 */
	private void _onAssimilated(MultiblockBase otherMultiblock) {
		if(referenceCoord != null) {
			if (this.WORLD.isBlockLoaded(this.referenceCoord)) {
				TileEntity te = this.WORLD.getTileEntity(this.referenceCoord);
				if(te instanceof IMultiblockPart) {
					((IMultiblockPart)te).forfeitMultiblockSaveDelegate();
				}
			}
			this.referenceCoord = null;
		}

		connectedParts.clear();
	}
	
	/**
	 * Callback. Called after this multiblock assimilates all the blocks
	 * from another multiblock.
	 * Use this to absorb that multiblock's game data.
	 * @param assimilated The multiblock whose uniqueness was added to our own.
	 */
	protected abstract void onAssimilate(MultiblockBase assimilated);
	
	/**
	 * Callback. Called after this multiblock is assimilated into another multiblock.
	 * All blocks have been stripped out of this object and handed over to the
	 * other multiblock.
	 * This is intended primarily for cleanup.
	 * @param assimilator The multiblock which has assimilated this multiblock.
	 */
	protected abstract void onAssimilated(MultiblockBase assimilator);
	
	/**
	 * Driver for the update loop. If the machine is assembled, runs
	 * the game logic update method.
	 * @see nc.multiblock.MultiblockBase#updateServer()
	 */
	public final void updateMultiblockEntity() {
		if(connectedParts.isEmpty()) {
			// This shouldn't happen, but just in case...
			REGISTRY.addDeadMultiblock(this.WORLD, this);
			return;
		}

		if(this.assemblyState != AssemblyState.Assembled) {
			// Not assembled - don't run game logic
			return;
		}

		if(WORLD.isRemote) {
			updateClient();
		}
		else if(updateServer()) {
			// If this returns true, the server has changed its internal data. 
			// If our chunks are loaded (they should be), we must mark our chunks as dirty.
			if(minimumCoord != null && maximumCoord != null &&
				this.WORLD.isAreaLoaded(this.minimumCoord, this.maximumCoord)) {

				int minChunkX = WorldHelper.getChunkXFromBlock(this.minimumCoord);
				int minChunkZ = WorldHelper.getChunkZFromBlock(this.minimumCoord);
				int maxChunkX = WorldHelper.getChunkXFromBlock(this.maximumCoord);
				int maxChunkZ = WorldHelper.getChunkZFromBlock(this.maximumCoord);
				
				for(int x = minChunkX; x <= maxChunkX; x++) {
					for(int z = minChunkZ; z <= maxChunkZ; z++) {
						// Ensure that we save our data, even if the our save delegate is in has no TEs.
						Chunk chunkToSave = this.WORLD.getChunk(x, z);
						chunkToSave.markDirty();
					}
				}
			}
		}
		// Else: Server, but no need to save data.
	}
	
	/**
	 * The server-side update loop! Use this similarly to a TileEntity's update loop.
	 * You do not need to call your superclass' update() if you're directly
	 * derived from MultiblockBase. This is a callback.
	 * Note that this will only be called when the machine is assembled.
	 * @return True if the multiblock should save data, i.e. its internal game state has changed. False otherwise.
	 */
	protected abstract boolean updateServer();
	
	/**
	 * Client-side update loop. Generally, this shouldn't do anything, but if you want
	 * to do some interpolation or something, do it here.
	 */
	protected abstract void updateClient();
	
	// Validation helpers
	
	protected boolean standardLastError(int x, int y, int z, IMultiblockValidator validatorCallback) {
		validatorCallback.setLastError(Global.MOD_ID + ".multiblock_validation.invalid_block", new BlockPos(x, y, z), x, y, z, getBlock(x, y, z).getLocalizedName());
		return false;
	}
	
	/**
	 * The "frame" consists of the outer edges of the machine, plus the corners.
	 * 
	 * @param world World object for the world in which this multiblock is located.
	 * @param x X coordinate of the block being tested
	 * @param y Y coordinate of the block being tested
	 * @param z Z coordinate of the block being tested
	 */
	protected boolean isBlockGoodForFrame(World world, int x, int y, int z, IMultiblockValidator validatorCallback) {
		return standardLastError(x, y, z, validatorCallback);
	}

	/**
	 * The top consists of the top face, minus the edges.
	 * @param world World object for the world in which this multiblock is located.
	 * @param x X coordinate of the block being tested
	 * @param y Y coordinate of the block being tested
	 * @param z Z coordinate of the block being tested
	 */
	protected boolean isBlockGoodForTop(World world, int x, int y, int z, IMultiblockValidator validatorCallback) {
		return standardLastError(x, y, z, validatorCallback);
	}
	
	/**
	 * The bottom consists of the bottom face, minus the edges.
	 * @param world World object for the world in which this multiblock is located.
	 * @param x X coordinate of the block being tested
	 * @param y Y coordinate of the block being tested
	 * @param z Z coordinate of the block being tested
	 */
	protected boolean isBlockGoodForBottom(World world, int x, int y, int z, IMultiblockValidator validatorCallback) {
		return standardLastError(x, y, z, validatorCallback);
	}
	
	/**
	 * The sides consists of the N/E/S/W-facing faces, minus the edges.
	 * @param world World object for the world in which this multiblock is located.
	 * @param x X coordinate of the block being tested
	 * @param y Y coordinate of the block being tested
	 * @param z Z coordinate of the block being tested
	 */
	protected boolean isBlockGoodForSides(World world, int x, int y, int z, IMultiblockValidator validatorCallback) {
		return standardLastError(x, y, z, validatorCallback);
	}
	
	/**
	 * The interior is any block that does not touch blocks outside the machine.
	 * @param world World object for the world in which this multiblock is located.
	 * @param x X coordinate of the block being tested
	 * @param y Y coordinate of the block being tested
	 * @param z Z coordinate of the block being tested
	 */
	protected abstract boolean isBlockGoodForInterior(World world, int x, int y, int z, IMultiblockValidator validatorCallback);
	
	/**
	 * @return The reference coordinate, the block with the lowest x, y, z coordinates, evaluated in that order.
	 */
	public BlockPos getReferenceCoord() {
		if(referenceCoord == null) { selectNewReferenceCoord(); }
		return referenceCoord;
	}
	
	/**
	 * @return The number of blocks connected to this multiblock.
	 */
	public int getNumConnectedBlocks() { return connectedParts.size(); }

	/*
	 * Data synchronization
	 */

	/**
	 * Sync multiblock data from the given NBT compound
	 * @param data the data
	 * @param syncReason the reason why the synchronization is necessary
	 */
	protected abstract void syncDataFrom(NBTTagCompound data, TileBeefBase.SyncReason syncReason);

	/**
	 * Sync multiblock data to the given NBT compound
	 * @param data the data
	 * @param syncReason the reason why the synchronization is necessary
	 */
	protected abstract void syncDataTo(NBTTagCompound data, TileBeefBase.SyncReason syncReason);

	/**
	 * Force this multiblock to recalculate its minimum and maximum coordinates
	 * from the list of connected parts.
	 */
	public void recalculateMinMaxCoords() {

		int minX, minY, minZ, maxX, maxY, maxZ;
		int partCoord;
		BlockPos partPos;

		minX = minY = minZ = Integer.MAX_VALUE;
		maxX = maxY = maxZ = Integer.MIN_VALUE;

		for (IMultiblockPart part : this.connectedParts) {

			partPos = part.getTilePos();

			partCoord = partPos.getX();
			if (partCoord < minX) minX = partCoord;
			if (partCoord > maxX) maxX = partCoord;

			partCoord = partPos.getY();
			if (partCoord < minY) minY = partCoord;
			if (partCoord > maxY) maxY = partCoord;

			partCoord = partPos.getZ();
			if (partCoord < minZ) minZ = partCoord;
			if (partCoord > maxZ) maxZ = partCoord;
		}

		this.minimumCoord = new BlockPos(minX, minY, minZ);
		this.maximumCoord = new BlockPos(maxX, maxY, maxZ);
	}
	
	/**
	 * @return The minimum bounding-box coordinate containing this machine's blocks.
	 */
	public BlockPos getMinimumCoord() {
		if (minimumCoord == null) recalculateMinMaxCoords();
		return minimumCoord;
	}

	/**
	 * @return The maximum bounding-box coordinate containing this machine's blocks.
	 */
	public BlockPos getMaximumCoord() {
		if (maximumCoord == null) recalculateMinMaxCoords();
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
		return (int)(((long)getMinX() + (long)getMaxX())/2);
	}
	
	public int getMiddleY() {
		return (int)(((long)getMinY() + (long)getMaxY())/2);
	}
	
	public int getMiddleZ() {
		return (int)(((long)getMinZ() + (long)getMaxZ())/2);
	}

	/**
	 * Called when the save delegate's tile entity is being asked for its description packet
	 * @param data A fresh compound tag to write your multiblock data into
	 */
	//public abstract void formatDescriptionPacket(NBTTagCompound data);

	/**
	 * Called when the save delegate's tile entity receiving a description packet
	 * @param data A compound tag containing multiblock data to import
	 */
	//public abstract void decodeDescriptionPacket(NBTTagCompound data);

	/**
	 * @return True if this multiblock has no associated blocks, false otherwise
	 */
	public boolean isEmpty() {
		return connectedParts.isEmpty();
	}

	/**
	 * Tests whether this multiblock should consume the other multiblock
	 * and become the new multiblock master when the two multiblocks
	 * are adjacent. Assumes both multiblocks are the same type.
	 * @param otherMultiblock The other multiblock.
	 * @return True if this multiblock should consume the other, false otherwise.
	 */
	public boolean shouldConsume(MultiblockBase otherMultiblock) {
		if(!otherMultiblock.getClass().equals(getClass())) {
			throw new IllegalArgumentException("Attempting to merge two multiblocks with different master classes - this should never happen!");
		}
		
		if(otherMultiblock == this) { return false; } // Don't be silly, don't eat yourself.
		
		int res = _shouldConsume(otherMultiblock);
		if(res < 0) { return true; }
		else if(res > 0) { return false; }
		else {
			// Strip dead parts from both and retry
			FMLLog.warning("[%s] Encountered two multiblocks with the same reference coordinate. Auditing connected parts and retrying.", this.WORLD.isRemote ? "CLIENT" : "SERVER");
			auditParts();
			otherMultiblock.auditParts();
			
			res = _shouldConsume(otherMultiblock);
			if(res < 0) { return true; }
			else if(res > 0) { return false; }
			else {
				FMLLog.severe("My Multiblock (%d): size (%d), parts: %s", hashCode(), this.connectedParts.size(), this.getPartsListString());
				FMLLog.severe("Other Multiblock (%d): size (%d), coords: %s", otherMultiblock.hashCode(), otherMultiblock.connectedParts.size(), otherMultiblock.getPartsListString());
				throw new IllegalArgumentException("[" + (this.WORLD.isRemote ? "CLIENT" : "SERVER") + "] Two multiblocks with the same reference coord that somehow both have valid parts - this should never happen!");
			}

		}
	}
	
	private int _shouldConsume(MultiblockBase otherMultiblock) {
		BlockPos myCoord = getReferenceCoord();
		BlockPos theirCoord = otherMultiblock.getReferenceCoord();
		
		// Always consume other multiblocks if their reference coordinate is null - this means they're empty and can be assimilated on the cheap
		if(theirCoord == null) { return -1; }
		else { return myCoord.compareTo(theirCoord); }
	}
	
	private String getPartsListString() {
		StringBuilder sb = new StringBuilder();
		boolean first = true;
		BlockPos partPos;
		for(IMultiblockPart part : connectedParts) {
			if(!first) {
				sb.append(", ");
			}
			partPos = part.getTilePos();
			sb.append(String.format("(%d: %d, %d, %d)", part.hashCode(), partPos.getX(), partPos.getY(), partPos.getZ()));
			first = false;
		}
		
		return sb.toString();
	}
	
	/**
	 * Checks all of the parts in the multiblock. If any are dead or do not exist in the world, they are removed.
	 */
	private void auditParts() {
		ObjectOpenHashSet<IMultiblockPart> deadParts = new ObjectOpenHashSet<IMultiblockPart>();
		for(IMultiblockPart part : connectedParts) {
			if(part.isPartInvalid() || WORLD.getTileEntity(part.getTilePos()) != part) {
				onDetachBlock(part);
				deadParts.add(part);
			}
		}

		connectedParts.removeAll(deadParts);
		FMLLog.warning("[%s] Multiblock found %d dead parts during an audit, %d parts remain attached", this.WORLD.isRemote ? "CLIENT" : "SERVER", deadParts.size(), this.connectedParts.size());
	}

	/**
	 * Called when this machine may need to check for blocks that are no
	 * longer physically connected to the reference coordinate.
	 * @return
	 */
	public Set<IMultiblockPart> checkForDisconnections() {
		if(!this.shouldCheckForDisconnections) {
			return null;
		}
		
		if(this.isEmpty()) {
			REGISTRY.addDeadMultiblock(WORLD, this);
			return null;
		}
		
		//TileEntity te;
		//IChunkProvider chunkProvider = WORLD.getChunkProvider();

		// Invalidate our reference coord, we'll recalculate it shortly
		referenceCoord = null;
		
		// Reset visitations and find the minimum coordinate
		Set<IMultiblockPart> deadParts = new ObjectOpenHashSet<IMultiblockPart>();
		BlockPos position;
		IMultiblockPart referencePart = null;

		int originalSize = connectedParts.size();

		for(IMultiblockPart part : connectedParts) {
			position = part.getTilePos();
			// This happens during chunk unload.
			if (!this.WORLD.isBlockLoaded(position) || part.isPartInvalid()) {
				deadParts.add(part);
				onDetachBlock(part);
				continue;
			}
			
			if(WORLD.getTileEntity(position) != part) {
				deadParts.add(part);
				onDetachBlock(part);
				continue;
			}

			part.setUnvisited();
			part.forfeitMultiblockSaveDelegate();
			
			if(referenceCoord == null) {
				referenceCoord = position;
				referencePart = part;
			}
			else if(position.compareTo(referenceCoord) < 0) {
				referenceCoord = position;
				referencePart = part;
			}
		}
		
		connectedParts.removeAll(deadParts);
		deadParts.clear();
		
		if(referencePart == null || isEmpty()) {
			// There are no valid parts remaining. The entire multiblock was unloaded during a chunk unload. Halt.
			shouldCheckForDisconnections = false;
			REGISTRY.addDeadMultiblock(WORLD, this);
			return null;
		}
		else {
			referencePart.becomeMultiblockSaveDelegate();
		}

		// Now visit all connected parts, breadth-first, starting from reference coord's part
		IMultiblockPart part;
		LinkedList<IMultiblockPart> partsToCheck = new LinkedList<IMultiblockPart>();
		IMultiblockPart[] nearbyParts = null;
		int visitedParts = 0;

		partsToCheck.add(referencePart);
		
		while(!partsToCheck.isEmpty()) {
			part = partsToCheck.removeFirst();
			part.setVisited();
			visitedParts++;

			nearbyParts = part.getNeighboringParts(); // Chunk-safe on server, but not on client
			for(IMultiblockPart nearbyPart : nearbyParts) {
				// Ignore different machines
				if(nearbyPart.getMultiblock() != this) {
					continue;
				}

				if(!nearbyPart.isVisited()) {
					nearbyPart.setVisited();
					partsToCheck.add(nearbyPart);
				}
			}
		}
		
		// Finally, remove all parts that remain disconnected.
		Set<IMultiblockPart> removedParts = new ObjectOpenHashSet<IMultiblockPart>();
		for(IMultiblockPart orphanCandidate : connectedParts) {
			if (!orphanCandidate.isVisited()) {
				deadParts.add(orphanCandidate);
				orphanCandidate.onOrphaned(this, originalSize, visitedParts);
				onDetachBlock(orphanCandidate);
				removedParts.add(orphanCandidate);
			}
		}

		// Trim any blocks that were invalid, or were removed.
		connectedParts.removeAll(deadParts);
		
		// Cleanup. Not necessary, really.
		deadParts.clear();
		
		// Juuuust in case.
		if(referenceCoord == null) {
			selectNewReferenceCoord();
		}
		
		// We've run the checks from here on out.
		shouldCheckForDisconnections = false;
		
		return removedParts;
	}

	/**
	 * Detach all parts. Return a set of all parts which still
	 * have a valid tile entity. Chunk-safe.
	 * @return A set of all parts which still have a valid tile entity.
	 */
	public Set<IMultiblockPart> detachAllBlocks() {
		if(WORLD == null) { return new ObjectOpenHashSet<IMultiblockPart>(); }
		
		//IChunkProvider chunkProvider = WORLD.getChunkProvider();
		for(IMultiblockPart part : connectedParts) {
			if(this.WORLD.isBlockLoaded(part.getTilePos())) {
				onDetachBlock(part);
			}
		}

		Set<IMultiblockPart> detachedParts = connectedParts;
		connectedParts = new ObjectOpenHashSet<IMultiblockPart>();
		return detachedParts;
	}

	/**
	 * @return True if this multiblock machine is considered assembled and ready to go.
	 */
	public boolean isAssembled() {
		return this.assemblyState == AssemblyState.Assembled;
	}
	
	private void selectNewReferenceCoord() {
		//IChunkProvider chunkProvider = WORLD.getChunkProvider();
		IMultiblockPart theChosenOne = null;
		BlockPos position;

		referenceCoord = null;

		for(IMultiblockPart part : connectedParts) {
			position = part.getTilePos();
			if(part.isPartInvalid() || !this.WORLD.isBlockLoaded(position)) {
				// Chunk is unloading, skip this coord to prevent chunk thrashing
				continue;
			}

			if(referenceCoord == null || referenceCoord.compareTo(position) > 0) {
				referenceCoord = position;
				theChosenOne = part;
			}
		}

		if(theChosenOne != null) {
			theChosenOne.becomeMultiblockSaveDelegate();
		}
	}
	
	/**
	 * Marks the reference coord dirty & updateable.
	 * 
	 * On the server, this will mark the for a data-update, so that
	 * nearby clients will receive an updated description packet from the server
	 * after a short time. The block's chunk will also be marked dirty and the
	 * block's chunk will be saved to disk the next time chunks are saved.
	 * 
	 * On the client, this will mark the block for a rendering update.
	 */
	protected void markReferenceCoordForUpdate() {

		BlockPos rc = this.getReferenceCoord();

		if ((this.WORLD != null) && (rc != null))
			WorldHelper.notifyBlockUpdate(this.WORLD, rc, null, null);
	}
	
	/**
	 * Marks the reference coord dirty.
	 * 
	 * On the server, this marks the reference coord's chunk as dirty; the block (and chunk)
	 * will be saved to disk the next time chunks are saved. This does NOT mark it dirty for
	 * a description-packet update.
	 * 
	 * On the client, does nothing.
	 * @see MultiblockBase#markReferenceCoordForUpdate()
	 */
	protected void markReferenceCoordDirty() {
		if(WORLD == null || WORLD.isRemote) { return; }

		BlockPos referenceCoord = this.getReferenceCoord();
		if(referenceCoord == null) { return; }

		TileEntity saveTe = WORLD.getTileEntity(referenceCoord);
		WORLD.markChunkDirty(referenceCoord, saveTe);
	}

	/**
	 * Marks the whole multiblock for a render update on the client. On the server, this does nothing
	*/
	protected void markMultiblockForRenderUpdate() {
		this.WORLD.markBlockRangeForRenderUpdate(this.getMinimumCoord(), this.getMaximumCoord());
	}
	
	// Packets
	
	protected abstract PACKET getUpdatePacket();
	
	public abstract void onPacket(PACKET message);
	
	public void beginUpdatingPlayer(EntityPlayer playerToUpdate) {
		playersToUpdate.add(playerToUpdate);
		sendIndividualUpdate(playerToUpdate);
	}
	
	public void stopUpdatingPlayer(EntityPlayer playerToRemove) {
		playersToUpdate.remove(playerToRemove);
	}
	
	protected void sendUpdateToListeningPlayers() {
		for (EntityPlayer player : playersToUpdate) PacketHandler.instance.sendTo(getUpdatePacket(), (EntityPlayerMP) player);
	}
	
	protected void sendIndividualUpdate(EntityPlayer player) {
		if (WORLD.isRemote) return;
		PacketHandler.instance.sendTo(getUpdatePacket(), (EntityPlayerMP) player);
	}
	
	protected void sendUpdateToAllPlayers() {
		PacketHandler.instance.sendToAll(getUpdatePacket());
	}
	
	// Registry

	private static final IMultiblockRegistry REGISTRY;

	static {
		REGISTRY = NuclearCraft.proxy.initMultiblockRegistry();
	}
	
	// Block getters
	
	protected IBlockState getBlockState(int x, int y, int z) {
		return WORLD.getBlockState(new BlockPos(x, y, z));
	}
	
	protected Block getBlock(int x, int y, int z) {
		return WORLD.getBlockState(new BlockPos(x, y, z)).getBlock();
	}
}