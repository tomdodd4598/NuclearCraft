package nc.recipe.vanilla;

import javax.annotation.Nonnull;

import net.minecraft.block.Block;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public class ShapelessOreArmorUpgradingRecipe extends ShapelessOreRecipe {
	
	public ShapelessOreArmorUpgradingRecipe(ResourceLocation group, Block result, Object... recipe) {
		this(group, new ItemStack(result), recipe);
	}
	
	public ShapelessOreArmorUpgradingRecipe(ResourceLocation group, Item  result, Object... recipe) {
		this(group, new ItemStack(result), recipe);
	}
	
	public ShapelessOreArmorUpgradingRecipe(ResourceLocation group, NonNullList<Ingredient> input, @Nonnull ItemStack result) {
		super(group, input, result);
	}
	
	public ShapelessOreArmorUpgradingRecipe(ResourceLocation group, @Nonnull ItemStack result, Object... recipe) {
		super(group, result, recipe);
	}
	
	@Override
	public boolean isDynamic() {
		return true;
	}

	@Override
	@Nonnull
	public ItemStack getCraftingResult(@Nonnull InventoryCrafting inv) {
		ItemStack output = this.output.copy();
		
		for (int i = 0; i < inv.getSizeInventory(); i++) {
			ItemStack stack = inv.getStackInSlot(i);
			if (!stack.isEmpty() && stack.getItem() instanceof ItemArmor) {
				if (!output.hasTagCompound()) output.setTagCompound(new NBTTagCompound());
				NBTTagCompound tag = output.getTagCompound().copy();
				if (stack.hasTagCompound()) output.getTagCompound().merge(stack.getTagCompound());
				output.getTagCompound().merge(tag);
				break;
			}
		}
		
		return output;
	}
}
