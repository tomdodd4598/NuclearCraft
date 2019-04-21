package nc.multiblock;

import net.minecraft.world.World;

public interface IMultiblockRegistry {

	/**
	 * Register a new part in the system. The part has been created either through user action or via a chunk loading.
	 * @param world The world into which this part is loading.
	 * @param part The part being loaded.
	 */
	void onPartAdded(World world, IMultiblockPart part);

	/**
	 * Call to remove a part from world lists.
	 * @param world The world from which a multiblock part is being removed.
	 * @param part The part being removed.
	 */
	void onPartRemovedFromWorld(World world, IMultiblockPart part);

	/**
	 * Call to mark a multiblock as dead. It should only be marked as dead
	 * when it has no connected parts. It will be removed after the next world tick.
	 * @param world The world formerly containing the multiblock
	 * @param multiblock The dead multiblock
	 */
	void addDeadMultiblock(World world, MultiblockBase multiblock);

	/**
	 * Call to mark a multiblock as dirty. Dirty means that parts have
	 * been added or removed this tick.
	 * @param world The world containing the multiblock
	 * @param multiblock The dirty multiblock
	 */
	void addDirtyMultiblock(World world, MultiblockBase multiblock);
}
