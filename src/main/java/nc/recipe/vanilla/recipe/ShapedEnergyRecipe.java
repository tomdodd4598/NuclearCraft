package nc.recipe.vanilla.recipe;

import javax.annotation.Nonnull;

import nc.item.energy.IChargableItem;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class ShapedEnergyRecipe extends ShapedOreRecipe {
	
	public ShapedEnergyRecipe(ResourceLocation group, @Nonnull ItemStack result, Object... recipe) {
		super(group, result, recipe);
	}
	
	@Override
	public boolean isDynamic() {
		return true;
	}
	
	@Override
	@Nonnull
	public ItemStack getCraftingResult(@Nonnull InventoryCrafting inv) {
		ItemStack result = output.copy();
		if (result.getItem() instanceof IChargableItem) {
			int energy = 0;
			for (int i = 0; i < inv.getSizeInventory(); i++) {
				ItemStack stack = inv.getStackInSlot(i);
				if (stack.getItem() instanceof IChargableItem) {
					energy += ((IChargableItem)stack.getItem()).getEnergyStored(stack);
				}
			}
			NBTTagCompound nbt = result.hasTagCompound() ? result.getTagCompound() : new NBTTagCompound();
			nbt.setInteger("energy", Math.min(energy, ((IChargableItem)result.getItem()).getMaxEnergyStored(result)));
			result.setTagCompound(nbt);
		}
		return result;
	}
}
