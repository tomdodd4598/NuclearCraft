package nc.recipe.vanilla.recipe;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;

@FunctionalInterface
public interface RecipeSupplier<T extends IRecipe> {
	
	T get(ResourceLocation location, ItemStack output, Object... inputs);
}
