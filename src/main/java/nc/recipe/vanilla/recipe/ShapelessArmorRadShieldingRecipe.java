package nc.recipe.vanilla.recipe;

import javax.annotation.Nonnull;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public class ShapelessArmorRadShieldingRecipe extends ShapelessOreRecipe {
	
	public ShapelessArmorRadShieldingRecipe(ResourceLocation group, @Nonnull ItemStack result, Object... recipe) {
		super(group, result, recipe);
		isSimple = false;
	}
	
	@Override
	public boolean isDynamic() {
		return true;
	}
	
	@Override
	public @Nonnull ItemStack getCraftingResult(@Nonnull InventoryCrafting inv) {
		for (int i = 0; i < inv.getSizeInventory(); i++) {
			ItemStack stack = inv.getStackInSlot(i).copy();
			if (!stack.isEmpty() && stack.getItem() instanceof ItemArmor) {
				if (!stack.hasTagCompound()) {
					stack.setTagCompound(new NBTTagCompound());
				}
				if (output.hasTagCompound()) {
					NBTTagCompound tag = stack.getTagCompound().copy();
					if (tag.getDouble("ncRadiationResistance") >= output.getTagCompound().getDouble("ncRadiationResistance")) {
						return ItemStack.EMPTY;
					}
					stack.getTagCompound().merge(output.getTagCompound());
					stack.getTagCompound().merge(tag);
					stack.getTagCompound().setDouble("ncRadiationResistance", output.getTagCompound().getDouble("ncRadiationResistance"));
				}
				return stack;
			}
		}
		return ItemStack.EMPTY;
	}
	
	@Override
	public boolean matches(@Nonnull InventoryCrafting inv, @Nonnull World world) {
		return super.matches(inv, world) && !getCraftingResult(inv).isEmpty();
	}
}
