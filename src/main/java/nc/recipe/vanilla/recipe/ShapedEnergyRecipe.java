package nc.recipe.vanilla.recipe;

import javax.annotation.Nonnull;

import nc.item.energy.IChargableItem;
import nc.tile.internal.energy.EnergyStorage;
import nc.util.NBTHelper;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
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
			long energy = 0L;
			for (int i = 0; i < inv.getSizeInventory(); ++i) {
				ItemStack stack = inv.getStackInSlot(i);
				if (stack.getItem() instanceof IChargableItem) {
					energy += ((IChargableItem) stack.getItem()).getEnergyStored(stack);
				}
			}
			
			IChargableItem item = (IChargableItem) result.getItem();
			new EnergyStorage(item.getMaxEnergyStored(result), item.getMaxTransfer(result), energy).writeToNBT(NBTHelper.getStackNBT(result), "energyStorage");
		}
		return result;
	}
}
