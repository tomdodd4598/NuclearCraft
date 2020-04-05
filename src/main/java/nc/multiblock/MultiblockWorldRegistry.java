package nc.multiblock;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import nc.multiblock.tile.ITileMultiblockPart;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLLog;

/**
 * This class manages all the multiblocks that exist in a given world,
 * either client- or server-side.
 * You must create different registries for server and client worlds.
 *
 * @author Erogenous Beef
 */
final class MultiblockWorldRegistry {

	private World worldObj;

	private final ObjectSet<Multiblock> multiblocks;		// Active multiblocks
	private final ObjectSet<Multiblock> dirtyMultiblocks;	// Multiblocks whose parts lists have changed
	private final ObjectSet<Multiblock> deadMultiblocks;	// Multiblocks which are empty

	// A list of orphan parts - parts which currently have no master, but should seek one this tick
	// Indexed by the hashed chunk coordinate
	// This can be added-to asynchronously via chunk loads!
	private ObjectSet<ITileMultiblockPart> orphanedParts;

	// A list of parts which have been detached during internal operations
	private final ObjectSet<ITileMultiblockPart> detachedParts;

	// A list of parts whose chunks have not yet finished loading
	// They will be added to the orphan list when they are finished loading.
	// Indexed by the hashed chunk coordinate
	// This can be added-to asynchronously via chunk loads!
	private final Long2ObjectMap<Set<ITileMultiblockPart>> partsAwaitingChunkLoad;

	// Mutexes to protect lists which may be changed due to asynchronous events, such as chunk loads
	private final Object partsAwaitingChunkLoadMutex;
	private final Object orphanedPartsMutex;

	public MultiblockWorldRegistry(final World world) {
		worldObj = world;

		multiblocks = new ObjectOpenHashSet<>();
		deadMultiblocks = new ObjectOpenHashSet<>();
		dirtyMultiblocks = new ObjectOpenHashSet<>();

		detachedParts = new ObjectOpenHashSet<>();
		orphanedParts = new ObjectOpenHashSet<>();

		partsAwaitingChunkLoad = new Long2ObjectOpenHashMap<>();
		partsAwaitingChunkLoadMutex = new Object();
		orphanedPartsMutex = new Object();
	}

	/**
	 * Called before Tile Entities are ticked in the world. Run game logic.
	 */
	public void tickStart() {
		if(multiblocks.size() > 0) {
			for(Multiblock multiblock : multiblocks) {
				if(multiblock.WORLD == worldObj && multiblock.WORLD.isRemote == worldObj.isRemote) {
					if(multiblock.isEmpty()) {
						// This happens on the server when the user breaks the last block. It's fine.
						// Mark 'er dead and move on.
						deadMultiblocks.add(multiblock);
					}
					else {
						// Run the game logic for this world
						multiblock.updateMultiblockEntity();
					}
				}
			}
		}
	}

	/**
	 * Called prior to processing multiblocks. Do bookkeeping.
	 */
	public void processMultiblockChanges() {
		BlockPos coord;

		// Merge pools - sets of adjacent machines which should be merged later on in processing
		List<Set<Multiblock>> mergePools = null;
		if(orphanedParts.size() > 0) {
			Set<ITileMultiblockPart> orphansToProcess = null;

			// Keep the synchronized block small. We can't iterate over orphanedParts directly
			// because the client does not know which chunks are actually loaded, so attachToNeighbors()
			// is not chunk-safe on the client, because Minecraft is stupid.
			// It's possible to polyfill this, but the polyfill is too slow for comfort.
			synchronized(orphanedPartsMutex) {
				if(orphanedParts.size() > 0) {
					orphansToProcess = orphanedParts;
					orphanedParts = new ObjectOpenHashSet<>();
				}
			}

			if(orphansToProcess != null && orphansToProcess.size() > 0) {
				//IChunkProvider chunkProvider = this.worldObj.getChunkProvider();
				Set<Multiblock> compatibleMultiblocks;

				// Process orphaned blocks
				// These are blocks that exist in a valid chunk and require a multiblock
				for(ITileMultiblockPart orphan : orphansToProcess) {
					coord = orphan.getTilePos();
					if(!this.worldObj.isBlockLoaded(coord)) {
						continue;
					}

					// This can occur on slow machines.
					if(orphan.isPartInvalid()) { continue; }

					if(MultiblockWorldRegistry.getMultiblockPartFromWorld(worldObj, coord) != orphan) {
						// This block has been replaced by another.
						continue;
					}

					// THIS IS THE ONLY PLACE WHERE PARTS ATTACH TO MACHINES
					// Try to attach to a neighbor's master multiblock
					compatibleMultiblocks = orphan.attachToNeighbors();
					if(compatibleMultiblocks == null) {
						// FOREVER ALONE! Create and register a new multiblock.
						// THIS IS THE ONLY PLACE WHERE NEW CONTROLLERS ARE CREATED.
						Multiblock newMultiblock = orphan.createNewMultiblock();
						newMultiblock.attachBlock(orphan);
						this.multiblocks.add(newMultiblock);
					}
					else if(compatibleMultiblocks.size() > 1) {
						if(mergePools == null) { mergePools = new ArrayList<>(); }

						// THIS IS THE ONLY PLACE WHERE MERGES ARE DETECTED
						// Multiple compatible multiblocks indicates an impending merge.
						// Locate the appropriate merge pool(s)
						//boolean hasAddedToPool = false;
						List<Set<Multiblock>> candidatePools = new ArrayList<>();
						for(Set<Multiblock> candidatePool : mergePools) {
							if(!Collections.disjoint(candidatePool, compatibleMultiblocks)) {
								// They share at least one element, so that means they will all touch after the merge
								candidatePools.add(candidatePool);
							}
						}

						if(candidatePools.size() <= 0) {
							// No pools nearby, create a new merge pool
							mergePools.add(compatibleMultiblocks);
						}
						else if(candidatePools.size() == 1) {
							// Only one pool nearby, simply add to that one
							candidatePools.get(0).addAll(compatibleMultiblocks);
						}
						else {
							// Multiple pools- merge into one, then add the compatible multiblocks
							Set<Multiblock> masterPool = candidatePools.get(0);
							Set<Multiblock> consumedPool;
							for(int i = 1; i < candidatePools.size(); i++) {
								consumedPool = candidatePools.get(i);
								masterPool.addAll(consumedPool);
								mergePools.remove(consumedPool);
							}
							masterPool.addAll(compatibleMultiblocks);
						}
					}
				}
			}
		}

		if(mergePools != null && mergePools.size() > 0) {
			// Process merges - any machines that have been marked for merge should be merged
			// into the "master" machine.
			// To do this, we combine lists of machines that are touching one another and therefore
			// should voltron the fuck up.
			for(Set<Multiblock> mergePool : mergePools) {
				// Search for the new master machine, which will take over all the blocks contained in the other machines
				Multiblock newMaster = null;
				for(Multiblock multiblock : mergePool) {
					if(newMaster == null || multiblock.shouldConsume(newMaster)) {
						newMaster = multiblock;
					}
				}

				if(newMaster == null) {
					FMLLog.severe("Multiblock system checked a merge pool of size %d, found no master candidates. This should never happen.", mergePool.size());
				}
				else {
					// Merge all the other machines into the master machine, then unregister them
					addDirtyMultiblock(newMaster);
					for(Multiblock multiblock : mergePool) {
						if(multiblock != newMaster) {
							newMaster.assimilate(multiblock);
							addDeadMultiblock(multiblock);
							addDirtyMultiblock(newMaster);
						}
					}
				}
			}
		}

		// Process splits and assembly
		// Any multiblocks which have had parts removed must be checked to see if some parts are no longer
		// physically connected to their master.
		if(dirtyMultiblocks.size() > 0) {
			Set<ITileMultiblockPart> newlyDetachedParts = null;
			for(Multiblock multiblock : dirtyMultiblocks) {
				// Tell the machine to check if any parts are disconnected.
				// It should return a set of parts which are no longer connected.
				// POSTCONDITION: The multiblock must have informed those parts that
				// they are no longer connected to this machine.
				newlyDetachedParts = multiblock.checkForDisconnections();

				if(!multiblock.isEmpty()) {
					multiblock.recalculateMinMaxCoords();
					multiblock.checkIfMachineIsWhole();
				}
				else {
					addDeadMultiblock(multiblock);
				}

				if(newlyDetachedParts != null && newlyDetachedParts.size() > 0) {
					// Multiblock has shed some parts - add them to the detached list for delayed processing
					detachedParts.addAll(newlyDetachedParts);
				}
			}

			dirtyMultiblocks.clear();
		}

		// Unregister dead multiblocks
		if(deadMultiblocks.size() > 0) {
			for(Multiblock multiblock : deadMultiblocks) {
				// Go through any multiblocks which have marked themselves as potentially dead.
				// Validate that they are empty/dead, then unregister them.
				if(!multiblock.isEmpty()) {
					FMLLog.severe("Found a non-empty multiblock. Forcing it to shed its blocks and die. This should never happen!");
					detachedParts.addAll(multiblock.detachAllBlocks());
				}

				// THIS IS THE ONLY PLACE WHERE CONTROLLERS ARE UNREGISTERED.
				this.multiblocks.remove(multiblock);
			}

			deadMultiblocks.clear();
		}

		// Process detached blocks
		// Any blocks which have been detached this tick should be moved to the orphaned
		// list, and will be checked next tick to see if their chunk is still loaded.
		for(ITileMultiblockPart part : detachedParts) {
			// Ensure parts know they're detached
			part.assertDetached();
		}

		addAllOrphanedPartsThreadsafe(detachedParts);
		detachedParts.clear();
	}

	/**
	 * Called when a multiblock part is added to the world, either via chunk-load or user action.
	 * If its chunk is loaded, it will be processed during the next tick.
	 * If the chunk is not loaded, it will be added to a list of objects waiting for a chunkload.
	 * @param part The part which is being added to this world.
	 */
	public void onPartAdded(final ITileMultiblockPart part) {
		BlockPos worldLocation = part.getTilePos();

		if(!this.worldObj.isBlockLoaded(worldLocation)) {
			// Part goes into the waiting-for-chunk-load list
			Set<ITileMultiblockPart> partSet;
			long chunkHash = WorldHelper.getChunkXZHashFromBlock(worldLocation);

			synchronized(partsAwaitingChunkLoadMutex) {
				if(!partsAwaitingChunkLoad.containsKey(chunkHash)) {
					partSet = new ObjectOpenHashSet<>();
					partsAwaitingChunkLoad.put(chunkHash, partSet);
				}
				else {
					partSet = partsAwaitingChunkLoad.get(chunkHash);
				}

				partSet.add(part);
			}
		}
		else {
			// Part goes into the orphan queue, to be checked this tick
			addOrphanedPartThreadsafe(part);
		}
	}

	/**
	 * Called when a part is removed from the world, via user action or via chunk unloads.
	 * This part is removed from any lists in which it may be, and its machine is marked for recalculation.
	 * @param part The part which is being removed.
	 */
	public void onPartRemovedFromWorld(final ITileMultiblockPart part) {
		final BlockPos coord = part.getTilePos();
		if(coord != null) {
			long hash = WorldHelper.getChunkXZHashFromBlock(coord);

			if(partsAwaitingChunkLoad.containsKey(hash)) {
				synchronized(partsAwaitingChunkLoadMutex) {
					if(partsAwaitingChunkLoad.containsKey(hash)) {
						partsAwaitingChunkLoad.get(hash).remove(part);
						if(partsAwaitingChunkLoad.get(hash).size() <= 0) {
							partsAwaitingChunkLoad.remove(hash);
						}
					}
				}
			}
		}

		detachedParts.remove(part);
		if(orphanedParts.contains(part)) {
			synchronized(orphanedPartsMutex) {
				orphanedParts.remove(part);
			}
		}

		part.assertDetached();
	}

	/**
	 * Called when the world which this World Registry represents is fully unloaded from the system.
	 * Does some housekeeping just to be nice.
	 */
	public void onWorldUnloaded() {
		multiblocks.clear();
		deadMultiblocks.clear();
		dirtyMultiblocks.clear();

		detachedParts.clear();

		synchronized(partsAwaitingChunkLoadMutex) {
			partsAwaitingChunkLoad.clear();
		}

		synchronized(orphanedPartsMutex) {
			orphanedParts.clear();
		}

		worldObj = null;
	}

	/**
	 * Called when a chunk has finished loading. Adds all of the parts which are awaiting
	 * load to the list of parts which are orphans and therefore will be added to machines
	 * after the next world tick.
	 *
	 * @param chunkX Chunk X coordinate (world coordate >> 4) of the chunk that was loaded
	 * @param chunkZ Chunk Z coordinate (world coordate >> 4) of the chunk that was loaded
	 */
	public void onChunkLoaded(final int chunkX, final int chunkZ) {
		final long chunkHash = ChunkPos.asLong(chunkX, chunkZ);
		if(partsAwaitingChunkLoad.containsKey(chunkHash)) {
			synchronized(partsAwaitingChunkLoadMutex) {
				if(partsAwaitingChunkLoad.containsKey(chunkHash)) {
					addAllOrphanedPartsThreadsafe(partsAwaitingChunkLoad.get(chunkHash));
					partsAwaitingChunkLoad.remove(chunkHash);
				}
			}
		}
	}

	/**
	 * Registers a multiblock as dead. It will be cleaned up at the end of the next world tick.
	 * Note that a multiblock must shed all of its blocks before being marked as dead, or the system
	 * will complain at you.
	 *
	 * @param deadMultiblock The multiblock which is dead.
	 */
	public void addDeadMultiblock(Multiblock deadMultiblock) {
		this.deadMultiblocks.add(deadMultiblock);
	}

	/**
	 * Registers a multiblock as dirty - its list of attached blocks has changed, and it
	 * must be re-checked for assembly and, possibly, for orphans.
	 *
	 * @param dirtyMultiblock The dirty multiblock.
	 */
	public void addDirtyMultiblock(Multiblock dirtyMultiblock) {
		this.dirtyMultiblocks.add(dirtyMultiblock);
	}

	/**
	 * Use this only if you know what you're doing. You should rarely need to iterate
	 * over all multiblocks in a world!
	 *
	 * @return An (unmodifiable) set of multiblocks which are active in this world.
	 */
	public Set<Multiblock> getMultiblocks() {
		return Collections.unmodifiableSet(multiblocks);
	}

	/* *** INTERNAL HELPERS *** */

	protected static ITileMultiblockPart getMultiblockPartFromWorld(final World world, final BlockPos position) {

		TileEntity te = world.getTileEntity(position);

		return te instanceof ITileMultiblockPart ? (ITileMultiblockPart)te : null;
	}

	private void addOrphanedPartThreadsafe(final ITileMultiblockPart part) {
		synchronized(orphanedPartsMutex) {
			orphanedParts.add(part);
		}
	}

	private void addAllOrphanedPartsThreadsafe(final Collection<? extends ITileMultiblockPart> parts) {
		synchronized(orphanedPartsMutex) {
			orphanedParts.addAll(parts);
		}
	}
}