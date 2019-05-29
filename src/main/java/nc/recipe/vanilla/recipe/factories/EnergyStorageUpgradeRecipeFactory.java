package nc.recipe.vanilla.recipe.factories;

import com.google.gson.JsonObject;
import nc.Global;
import nc.block.tile.BlockBattery;
import nc.init.NCItems;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.IRecipeFactory;
import net.minecraftforge.common.crafting.JsonContext;
import net.minecraftforge.oredict.ShapedOreRecipe;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class EnergyStorageUpgradeRecipeFactory implements IRecipeFactory {

  @Override
  public IRecipe parse(JsonContext context, JsonObject json) {
    ShapedOreRecipe recipe = ShapedOreRecipe.factory(context, json);

    CraftingHelper.ShapedPrimer primer = new CraftingHelper.ShapedPrimer();
    primer.width = recipe.getRecipeWidth();
    primer.height = recipe.getRecipeHeight();
    primer.mirrored = JsonUtils.getBoolean(json, "mirrored", true);
    primer.input = recipe.getIngredients();

    return new EnergyStorageUpgradeRecipe(new ResourceLocation(Global.MOD_ID, "energy_storage_upgrade"), recipe.getRecipeOutput(), primer);
  }

  public static class EnergyStorageUpgradeRecipe extends ShapedOreRecipe {
    public EnergyStorageUpgradeRecipe(ResourceLocation group, ItemStack result, CraftingHelper.ShapedPrimer primer) {
      super(group, result, primer);
    }

    @Override
    @Nonnull
    public ItemStack getCraftingResult(@Nonnull InventoryCrafting var1) {

      ItemStack newOutput = this.output.copy();
      List<ItemStack> stacks = new ArrayList<>();
      for (int i = 0; i < var1.getSizeInventory(); ++i) {
        ItemStack stack = var1.getStackInSlot(i);
        if (stack.getItem() == NCItems.lithium_ion_cell || stack.getItem() instanceof ItemBlock && ((ItemBlock) stack.getItem()).getBlock() instanceof BlockBattery)
          stacks.add(stack);
      }
      int energy = 0;
      for (ItemStack battery : stacks)
        energy += battery.getTagCompound().getInteger("energy");
      NBTTagCompound nbt = new NBTTagCompound();
      nbt.setInteger("energy", energy);
      newOutput.setTagCompound(nbt);
      return newOutput;
    }
  }
}
