package nc.recipe.vanilla.ingredient;

import javax.annotation.Nullable;

import nc.util.StackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.fluids.*;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;

public class BucketIngredient extends Ingredient {
	
	private final FluidStack fluidStack;
	
	public BucketIngredient(String fluidName) {
		super(StackHelper.getBucket(fluidName));
		fluidStack = FluidRegistry.getFluidStack(fluidName, Fluid.BUCKET_VOLUME);
	}
	
	@Override
	public boolean isSimple() {
		return false;
	}
	
	@Override
	public boolean apply(@Nullable ItemStack input) {
		if (input == null || input.isEmpty()) {
			return false;
		}
		
		IFluidHandlerItem handler = input.getCount() > 1 ? FluidUtil.getFluidHandler(input.copy()) : FluidUtil.getFluidHandler(input);
		if (handler == null) {
			return false;
		}
		
		return fluidStack.isFluidStackIdentical(handler.drain(Fluid.BUCKET_VOLUME, false));
	}
}
