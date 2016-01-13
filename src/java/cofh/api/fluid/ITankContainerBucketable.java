package cofh.api.fluid;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.IFluidHandler;

/**
 * @author Emy
 *
 * Extends the IFluidHandler interface to allow manual draining/filling via buckets.
 *
 * what am I even doing here
 */
public interface ITankContainerBucketable extends IFluidHandler {

	/**
	 * Called to determine if the {@link IFluidHandler} should be filled by buckets.
	 * @param stack The {@link ItemStack} being used to fill the IFluidHandler
	 * @return True if the IFluidHandler is allowed to be filled with <tt>stack</tt>
	 */
	public boolean allowBucketFill(ItemStack stack);

	/**
	 * Called to determine if the {@link IFluidHandler} should be drained by buckets.
	 * @param stack The {@link ItemStack} being used to drain the IFluidHandler
	 * @return True if the IFluidHandler is allowed to be drained with <tt>stack</tt>
	 */
	public boolean allowBucketDrain(ItemStack stack);

}