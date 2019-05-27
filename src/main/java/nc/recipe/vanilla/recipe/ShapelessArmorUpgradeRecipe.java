package nc.recipe.vanilla.recipe;

import javax.annotation.Nonnull;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public class ShapelessArmorUpgradeRecipe extends ShapelessOreRecipe {
	
	public ShapelessArmorUpgradeRecipe(ResourceLocation group, @Nonnull ItemStack result, Object... recipe) {
		super(group, result, recipe);
		isSimple = false;
	}
	
	@Override
	public boolean isDynamic() {
		return true;
	}
	
	@Override
	public @Nonnull ItemStack getCraftingResult(@Nonnull InventoryCrafting inv) {
		ItemStack output = this.output.copy();
		
		for (int i = 0; i < inv.getSizeInventory(); i++) {
			ItemStack stack = inv.getStackInSlot(i);
			if (!stack.isEmpty() && stack.getItem() instanceof ItemArmor) {
				if (!output.hasTagCompound()) {
					output.setTagCompound(new NBTTagCompound());
				}
				NBTTagCompound tag = output.getTagCompound().copy();
				if (stack.hasTagCompound()) {
					output.getTagCompound().merge(stack.getTagCompound());
				}
				output.getTagCompound().merge(tag);
				break;
			}
		}
		return output;
	}
}
