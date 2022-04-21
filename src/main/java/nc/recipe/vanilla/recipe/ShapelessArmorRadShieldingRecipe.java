package nc.recipe.vanilla.recipe;

import static nc.config.NCConfig.radiation_horse_armor_public;

import javax.annotation.Nonnull;

import nc.util.*;
import net.minecraft.inventory.InventoryCrafting;
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
		for (int i = 0; i < inv.getSizeInventory(); ++i) {
			ItemStack stack = inv.getStackInSlot(i).copy();
			if (!stack.isEmpty() && ArmorHelper.isArmor(stack.getItem(), radiation_horse_armor_public)) {
				if (output.hasTagCompound()) {
					NBTTagCompound nbt = NBTHelper.getStackNBT(stack), nbtCopy = nbt.copy(), outputNBT = output.getTagCompound();
					if (nbtCopy.getDouble("ncRadiationResistance") >= outputNBT.getDouble("ncRadiationResistance")) {
						return ItemStack.EMPTY;
					}
					nbt.merge(outputNBT);
					nbt.merge(nbtCopy);
					nbt.setDouble("ncRadiationResistance", outputNBT.getDouble("ncRadiationResistance"));
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
