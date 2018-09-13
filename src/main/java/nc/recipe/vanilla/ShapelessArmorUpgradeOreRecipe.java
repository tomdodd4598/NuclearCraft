package nc.recipe.vanilla;

import javax.annotation.Nonnull;

import net.minecraft.block.Block;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public class ShapelessArmorUpgradeOreRecipe extends ShapelessOreRecipe {
	
	public ShapelessArmorUpgradeOreRecipe(ResourceLocation group, Block result, Object... recipe) {
		this(group, new ItemStack(result), recipe);
	}
	
	public ShapelessArmorUpgradeOreRecipe(ResourceLocation group, Item  result, Object... recipe) {
		this(group, new ItemStack(result), recipe);
	}
	
	public ShapelessArmorUpgradeOreRecipe(ResourceLocation group, @Nonnull ItemStack result, Object... recipe) {
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
		int meta = 0;
		
		for (int i = 0; i < inv.getSizeInventory(); i++) {
			ItemStack stack = inv.getStackInSlot(i);
			if (!stack.isEmpty() && stack.getItem() instanceof ItemArmor) {
				meta = stack.getMetadata();
				if (!output.hasTagCompound()) output.setTagCompound(new NBTTagCompound());
				NBTTagCompound tag = output.getTagCompound().copy();
				if (stack.hasTagCompound()) output.getTagCompound().merge(stack.getTagCompound());
				output.getTagCompound().merge(tag);
				break;
			}
		}
		output.setItemDamage(meta);
		return output;
	}
}
