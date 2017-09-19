package nc.recipe;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

public class RecipeOreStack implements IIngredient, IRecipeStack {
	
	public String oreString;
	public boolean isFluid;
	public ArrayList<ItemStack> cachedItemRegister;
	public ArrayList<FluidStack> cachedFluidRegister;
	public int stackSize;

	public RecipeOreStack(String oreType, StackType stacktype, int stackSize) {
		oreString = oreType;
		cachedItemRegister = new ArrayList<ItemStack>(OreDictionary.getOres(oreType));
		cachedFluidRegister = new ArrayList<FluidStack>();
		ArrayList<Fluid> fluidList = new ArrayList<Fluid>(FluidRegistry.getRegisteredFluids().values());
		for (Fluid fluid : fluidList) {
			if (fluid.getName() == oreType.toLowerCase()) cachedFluidRegister.add(new FluidStack(fluid, stackSize));
		}
		if (cachedFluidRegister.isEmpty() && stacktype.isFluid()) {
			if (FluidRegistry.getFluid(oreType.toLowerCase()) != null) cachedFluidRegister.add(new FluidStack(FluidRegistry.getFluid(oreType.toLowerCase()), stackSize));
		}
		if (!stacktype.isItem() && !stacktype.isFluid()) {
			if (cachedItemRegister.isEmpty()) isFluid = true; else isFluid = false;
		}
		else isFluid = stacktype.isFluid();
		this.stackSize = stackSize;
	}

	public Object getIngredient() {
		if (isFluid) {
			//if (cachedFluidRegister.size() < 1) return cachedFluidRegister;
			FluidStack fluid = cachedFluidRegister.get(0).copy();
			fluid.amount = stackSize;
			return fluid;
		}
		//if (cachedItemRegister.size() < 1) return cachedItemRegister;
		ItemStack item = cachedItemRegister.get(0).copy();
		item.stackSize = stackSize;
		return item;
	}

	public Object getOutputStack() {
		Object stack = isFluid ? cachedFluidRegister.get(0).copy() : cachedItemRegister.get(0).copy();
		if (isFluid) {
			FluidStack fluidstack = (FluidStack)stack;
			fluidstack.amount = stackSize;
			return fluidstack;
		}
		ItemStack itemstack = (ItemStack)stack;
		itemstack.stackSize = stackSize;
		return itemstack;
	}

	public boolean matches(Object object, SorptionType type) {
		if (object instanceof RecipeOreStack) {
			RecipeOreStack oreStack = (RecipeOreStack)object;
			if (oreStack.oreString.equals(oreString) && oreStack.stackSize >= stackSize) {
				return true;
			}
		} else if (object instanceof String) {
			return oreString.equals(object);
		} else if (object instanceof ItemStack && type.checkStackSize(stackSize, ((ItemStack) object).stackSize)) {
			int oreID = OreDictionary.getOreID(oreString);
			for (int ID : OreDictionary.getOreIDs((ItemStack)object)) {
				if (oreID == ID) {
					return true;
				}
			}
		} else if (object instanceof FluidStack && type.checkStackSize(stackSize, ((FluidStack) object).amount)) {
			String fluidName = FluidRegistry.getFluidName((FluidStack)object);
			if (oreString == fluidName) {
				return true;
			}
		}
		return false;
	}

	public int getStackSize() {
		return stackSize;
	}

	public List<Object> getIngredientList() {
		if (isFluid) {
			List<Object> fluidCollection = new ArrayList<Object>();
			for (FluidStack fluid : cachedFluidRegister) {
				FluidStack fluidStack = fluid.copy();
				fluidStack.amount = stackSize;
				fluidCollection.add(fluidStack);
			}
			return fluidCollection;
		}
		List<Object> itemCollection = new ArrayList<Object>();
		for (ItemStack item : cachedItemRegister) {
			ItemStack itemStack = item.copy();
			itemStack.stackSize = stackSize;
			itemCollection.add(itemStack);
		}
		return itemCollection;
	}
}
