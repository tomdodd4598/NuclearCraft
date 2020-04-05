package nc.multiblock.tile;

import java.util.ArrayList;
import java.util.Set;

import com.google.common.collect.Lists;

import nc.multiblock.Multiblock;
import nc.recipe.ProcessorRecipe;
import nc.recipe.ProcessorRecipeHandler;
import nc.recipe.RecipeInfo;
import nc.tile.IMultitoolLogic;
import nc.util.ItemStackHelper;

/*
 * A multiblock library for making irregularly-shaped multiblock machines
 *
 * Original author: Erogenous Beef
 * https://github.com/erogenousbeef/BeefCore
 *
 * Ported to Minecraft 1.9 by ZeroNoRyouki
 * https://github.com/ZeroNoRyouki/ZeroCore
 */

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;

/**
 * Basic interface for a multiblock machine part.
 * Preferably, you should derive from MultiblockTileEntityBase,
 * which does all the hard work for you.
 * 
 * {@link nc.multiblock.tile.TileMultiblockPart}
 */
public interface ITileMultiblockPart<MULTIBLOCK extends Multiblock> extends IMultitoolLogic {
	
	/**
	 * @return True if this block is connected to a multiblock. False otherwise.
	 */
	boolean isConnected();
	
	/**
	 * @return The attached multiblock for this tile entity. 
	 */
	MULTIBLOCK getMultiblock();
	
	boolean isPartInvalid();
	
	// Multiblock connection-logic callbacks
	
	/**
	 * Called after this block has been attached to a new multiblock.
	 * @param newMultiblock The new multiblock to which this tile entity is attached.
	 */
	void onAttached(MULTIBLOCK newMultiblock);
	
	/**
	 * Called after this block has been detached from a multiblock.
	 * @param multiblock The multiblock that no longer controls this tile entity.
	 */
	void onDetached(MULTIBLOCK multiblock);
	
	/**
	 * Called when this block is being orphaned. Use this to copy game-data values that
	 * should persist despite a machine being broken.
	 * This should NOT mark the part as disconnected. onDetached will be called immediately afterwards.
	 * @see #onDetached(T)
	 * @param oldMultiblock The multiblock which is orphaning this block. 
	 * @param oldMultiblockSize The number of connected blocks in the multiblock prior to shedding orphans.
	 * @param newMultiblockSize The number of connected blocks in the multiblock after shedding orphans.
	 */
	void onOrphaned(MULTIBLOCK oldMultiblock, int oldMultiblockSize, int newMultiblockSize);
	
	// Multiblock fuse/split helper methods. Here there be dragons.
	/**
	 * Factory method. Creates a new multiblock and returns it.
	 * Does not attach this tile entity to it.
	 * Override this in your game code!
	 * @return A new Multiblock, derived from MultiblockBase.
	 */
	MULTIBLOCK createNewMultiblock();
	
	/**
	 * Retrieve the type of multiblock which governs this part.
	 * Used to ensure that incompatible multiblocks are not merged.
	 * @return The class/type of the multiblock which governs this type of part.
	 */
	Class<MULTIBLOCK> getMultiblockType();
	
	/**
	 * Called when this block is moved from its current multiblock into a new multiblock.
	 * A special case of attach/detach, done here for efficiency to avoid triggering
	 * lots of recalculation logic.
	 * @param newMultiblock The new multiblock into which this tile entity is being merged.
	 */
	void onAssimilated(MULTIBLOCK newMultiblock);
	
	// Multiblock connection data access.
	// You generally shouldn't toy with these!
	// They're for use by Multiblocks.
	
	/**
	 * Set that this block has been visited by your validation algorithms.
	 */
	void setVisited();
	
	/**
	 * Set that this block has not been visited by your validation algorithms;
	 */
	void setUnvisited();
	
	/**
	 * @return True if this block has been visited by your validation algorithms since the last reset.
	 */
	boolean isVisited();
	
	/**
	 * Called when this block becomes the designated block for saving data and
	 * transmitting data across the wire.
	 */
	void becomeMultiblockSaveDelegate();
	
	/**
	 * Called when this block is no longer the designated block for saving data
	 * and transmitting data across the wire.
	 */
	void forfeitMultiblockSaveDelegate();
	
	/**
	 * Is this block the designated save/load & network delegate?
	 */
	boolean isMultiblockSaveDelegate();
	
	/**
	 * Returns an array containing references to neighboring IMultiblockPart tile entities.
	 * Primarily a utility method. Only works after tileentity construction, so it cannot be used in
	 * MultiblockBase::attachBlock.
	 * 
	 * This method is chunk-safe on the server; it will not query for parts in chunks that are unloaded.
	 * Note that no method is chunk-safe on the client, because ChunkProviderClient is stupid.
	 * @return An array of references to neighboring IMultiblockPart tile entities.
	 */
	ITileMultiblockPart<MULTIBLOCK>[] getNeighboringParts();
	
	// Multiblock business-logic callbacks - implement these!
	/**
	 * Called when a machine is fully assembled from the disassembled state, meaning
	 * it was broken by a player/entity action, not by chunk unloads.
	 * Note that, for non-square machines, the min/max coordinates may not actually be part
	 * of the machine! They form an outer bounding box for the whole machine itself.
	 * @param multiblockBase The multiblock to which this part is being assembled.
	 */
	void onMachineAssembled(MULTIBLOCK multiblock);
	
	/**
	 * Called when the machine is broken for game reasons, e.g. a player removed a block
	 * or an explosion occurred.
	 */
	void onMachineBroken();
	
	/**
	 * Called when the user activates the machine. This is not called by default, but is included
	 * as most machines have this game-logical concept.
	 */
	void onMachineActivated();
	
	/**
	 * Called when the user deactivates the machine. This is not called by default, but is included
	 * as most machines have this game-logical concept.
	 */
	void onMachineDeactivated();
	
	// Block events
	/**
	 * Called when this part should check its neighbors.
	 * This method MUST NOT cause additional chunks to load.
	 * ALWAYS check to see if a chunk is loaded before querying for its tile entity
	 * This part should inform the multiblock that it is attaching at this time.
	 * @return A Set of multiblocks to which this object would like to attach. It should have attached to one of the multiblocks in this list. Return null if there are no compatible multiblocks nearby. 
	 */
	Set<MULTIBLOCK> attachToNeighbors();
	
	/**
	 * Assert that this part is detached. If not, log a warning and set the part's multiblock to null.
	 * Do NOT fire the full disconnection logic.
	 */
	void assertDetached();
	
	/**
	 * @return True if a part has multiblock game-data saved inside it.
	 */
	boolean hasMultiblockSaveData();
	
	/**
	 * @return The part's saved multiblock game-data in NBT format, or null if there isn't any.
	 */
	NBTTagCompound getMultiblockSaveData();
	
	/**
	 * Called after a block is added and the multiblock has incorporated the part's saved
	 * multiblock game-data into itself. Generally, you should clear the saved data here.
	 */
	void onMultiblockDataAssimilated();
	
	// Helper methods
	
	public default ProcessorRecipe blockRecipe(ProcessorRecipeHandler recipeHandler, BlockPos pos) {
		RecipeInfo<ProcessorRecipe> recipeInfo = recipeHandler.getRecipeInfoFromInputs(Lists.newArrayList(ItemStackHelper.blockStateToStack(getTileWorld().getBlockState(pos))), new ArrayList<>());
		return recipeInfo == null ? null : recipeInfo.getRecipe();
	}
}
