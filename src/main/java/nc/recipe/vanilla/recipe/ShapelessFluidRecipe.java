package nc.recipe.vanilla.recipe;

import javax.annotation.Nonnull;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.fluids.*;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public class ShapelessFluidRecipe extends ShapelessOreRecipe {
	
	public ShapelessFluidRecipe(ResourceLocation group, @Nonnull ItemStack result, Object... recipe) {
		super(group, result, recipe);
	}
	
	@Override
	public boolean isDynamic() {
		return true;
	}
	
	/** Originally from KingLemming's CoFHCore: cofh.core.util.crafting.ShapelessFluidRecipeFactory */
	@Override
	public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv) {
		NonNullList<ItemStack> ret = NonNullList.withSize(inv.getSizeInventory(), ItemStack.EMPTY);
		for (int i = 0; i < ret.size(); ++i) {
			ItemStack stack = inv.getStackInSlot(i);
			IFluidHandlerItem handler = stack.getCount() > 1 ? FluidUtil.getFluidHandler(stack.copy()) : FluidUtil.getFluidHandler(stack);
			if (handler == null) {
				ret.set(i, ForgeHooks.getContainerItem(stack));
			}
			else {
				handler.drain(Fluid.BUCKET_VOLUME, true);
				ret.set(i, handler.getContainer().copy());
			}
		}
		return ret;
	}
}
