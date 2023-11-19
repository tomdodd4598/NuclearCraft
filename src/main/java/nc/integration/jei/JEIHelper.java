package nc.integration.jei;

import java.util.List;

import it.unimi.dsi.fastutil.ints.*;
import it.unimi.dsi.fastutil.objects.*;
import mezz.jei.api.gui.*;
import mezz.jei.api.ingredients.IIngredients;
import nc.recipe.IngredientSorption;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class JEIHelper {
	
	protected static abstract class RecipeIngredientMapper<T, INFO> {
		
		public Object2ObjectMap<IngredientSorption, Int2ObjectMap<INFO>> internal = new Object2ObjectOpenHashMap<>();
		
		protected RecipeIngredientMapper() {
			internal.put(IngredientSorption.INPUT, new Int2ObjectOpenHashMap<>());
			internal.put(IngredientSorption.OUTPUT, new Int2ObjectOpenHashMap<>());
		}
		
		protected void put(IngredientSorption type, int groupIndex, INFO info) {
			internal.get(type).put(groupIndex, info);
		}
		
		public abstract void apply(T group, IIngredients ingredients);
	}
	
	public static class RecipeItemMapper extends RecipeIngredientMapper<IGuiItemStackGroup, RecipeItemInfo> {
		
		public void put(IngredientSorption type, int groupIndex, int slotIndex, int stackX, int stackY) {
			put(type, groupIndex, new RecipeItemInfo(slotIndex, stackX, stackY));
		}
		
		@Override
		public void apply(IGuiItemStackGroup items, IIngredients ingredients) {
			for (Object2ObjectMap.Entry<IngredientSorption, Int2ObjectMap<RecipeItemInfo>> entry : internal.object2ObjectEntrySet()) {
				boolean isInput = entry.getKey().equals(IngredientSorption.INPUT);
				List<List<ItemStack>> stackLists = isInput ? ingredients.getInputs(ItemStack.class) : ingredients.getOutputs(ItemStack.class);
				
				for (Int2ObjectMap.Entry<RecipeItemInfo> mapping : entry.getValue().int2ObjectEntrySet()) {
					RecipeItemInfo info = mapping.getValue();
					items.init(info.slotIndex, isInput, info.stackX, info.stackY);
					items.set(info.slotIndex, stackLists.get(mapping.getIntKey()));
				}
			}
		}
	}
	
	public static class RecipeFluidMapper extends RecipeIngredientMapper<IGuiFluidStackGroup, RecipeFluidInfo> {
		
		public void put(IngredientSorption type, int groupIndex, int tankIndex, int tankX, int tankY, int tankW, int tankH) {
			put(type, groupIndex, new RecipeFluidInfo(tankIndex, tankX, tankY, tankW, tankH));
		}
		
		@Override
		public void apply(IGuiFluidStackGroup fluids, IIngredients ingredients) {
			for (Object2ObjectMap.Entry<IngredientSorption, Int2ObjectMap<RecipeFluidInfo>> entry : internal.object2ObjectEntrySet()) {
				boolean isInput = entry.getKey().equals(IngredientSorption.INPUT);
				List<List<FluidStack>> fluidLists = isInput ? ingredients.getInputs(FluidStack.class) : ingredients.getOutputs(FluidStack.class);
				
				for (Int2ObjectMap.Entry<RecipeFluidInfo> mapping : entry.getValue().int2ObjectEntrySet()) {
					RecipeFluidInfo info = mapping.getValue();
					List<FluidStack> fluidList = fluidLists.get(mapping.getIntKey());
					FluidStack stack = fluidList.isEmpty() ? null : fluidList.get(fluidList.size() - 1);
					fluids.init(info.tankIndex, isInput, info.tankX, info.tankY, info.tankW, info.tankH, stack == null ? 1000 : Math.max(1, stack.amount), true, null);
					fluids.set(info.tankIndex, stack == null ? null : fluidList);
				}
			}
		}
	}
	
	protected static class RecipeItemInfo {
		
		public int slotIndex, stackX, stackY;
		
		public RecipeItemInfo(int slotIndex, int stackX, int stackY) {
			this.slotIndex = slotIndex;
			this.stackX = stackX;
			this.stackY = stackY;
		}
	}
	
	protected static class RecipeFluidInfo {
		
		public int tankIndex, tankX, tankY, tankW, tankH;
		
		public RecipeFluidInfo(int tankIndex, int tankX, int tankY, int tankW, int tankH) {
			this.tankIndex = tankIndex;
			this.tankX = tankX;
			this.tankY = tankY;
			this.tankW = tankW;
			this.tankH = tankH;
		}
	}
}
