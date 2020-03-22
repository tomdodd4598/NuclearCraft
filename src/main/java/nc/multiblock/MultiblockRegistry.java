package nc.multiblock;

import java.util.HashMap;

import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLLog;

public final class MultiblockRegistry implements IMultiblockRegistry {
	
	private MultiblockWorldRegistry getMultiblockRegistry(final World world) {
		if (this._registries.containsKey(world)) {
			return this._registries.get(world);
		} else {
			MultiblockWorldRegistry registry = new MultiblockWorldRegistry(world);
			this._registries.put(world, registry);
			return registry;
		}
	}
	
	 /**
	 * Register a new part in the system. The part has been created either through user action or via a chunk loading.
	 * @param world The world into which this part is loading.
	 * @param part The part being loaded.
	 */
	@Override
	public void onPartAdded(final World world, final IMultiblockPart part) {
		getMultiblockRegistry(world).onPartAdded(part);
	}

	/**
	 * Call to remove a part from world lists.
	 * @param world The world from which a multiblock part is being removed.
	 * @param part The part being removed.
	 */
	@Override
	public void onPartRemovedFromWorld(final World world, final IMultiblockPart part) {

		if (this._registries.containsKey(world))
			this._registries.get(world).onPartRemovedFromWorld(part);
	}

	/**
	 * Call to mark a multiblock as dead. It should only be marked as dead
	 * when it has no connected parts. It will be removed after the next world tick.
	 * @param world The world formerly containing the multiblock
	 * @param multiblock The dead multiblock
	 */
	@Override
	public void addDeadMultiblock(final World world, final MultiblockBase multiblock) {

		if (this._registries.containsKey(world))
			this._registries.get(world).addDeadMultiblock(multiblock);
		else
			FMLLog.warning("Multiblock %d in world %s marked as dead, but that world is not tracked! Multiblock is being ignored.", multiblock.hashCode(), world);
	}

	/**
	 * Call to mark a multiblock as dirty. Dirty means that parts have
	 * been added or removed this tick.
	 * @param world The world containing the multiblock
	 * @param multiblock The dirty multiblock
	 */
	@Override
	public void addDirtyMultiblock(final World world, final MultiblockBase multiblock) {
		if (!this._registries.containsKey(world)) {
			FMLLog.warning("Adding a dirty multiblock to a world that has no registered multiblocks! Creating new registry...");
			//throw new IllegalArgumentException("Adding a dirty multiblock to a world that has no registered multiblocks!");
		}
		getMultiblockRegistry(world).addDirtyMultiblock(multiblock);
	}

	/*
	Private implementation
	 */

	/**
	 * Called before Tile Entities are ticked in the world. Do bookkeeping here.
	 * @param world The world being ticked
	 */
	protected void tickStart(final World world) {

		if (this._registries.containsKey(world)) {

			final MultiblockWorldRegistry registry = this._registries.get(world);

			registry.processMultiblockChanges();
			registry.tickStart();
		}
	}

	/**
	 * Called when the world has finished loading a chunk.
	 * @param world The world which has finished loading a chunk
	 * @param chunkX The X coordinate of the chunk
	 * @param chunkZ The Z coordinate of the chunk
	 */
	protected void onChunkLoaded(final World world, final int chunkX, final int chunkZ) {

		if (this._registries.containsKey(world))
			this._registries.get(world).onChunkLoaded(chunkX, chunkZ);
	}

	/**
	 * Called whenever a world is unloaded. Unload the relevant registry, if we have one.
	 * @param world The world being unloaded.
	 */
	protected void onWorldUnloaded(final World world) {

		if (this._registries.containsKey(world)) {
			this._registries.get(world).onWorldUnloaded();
			this._registries.remove(world);
		}
	}

	private MultiblockRegistry() {
		this._registries = new HashMap<World, MultiblockWorldRegistry>(2);
	}

	private HashMap<World, MultiblockWorldRegistry> _registries;

	public static final MultiblockRegistry INSTANCE = new MultiblockRegistry();
}
