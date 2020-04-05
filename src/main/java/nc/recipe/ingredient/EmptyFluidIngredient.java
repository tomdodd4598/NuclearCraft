package nc.recipe.ingredient;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Lists;

import crafttweaker.api.minecraft.CraftTweakerMC;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import nc.recipe.IngredientMatchResult;
import nc.recipe.IngredientSorption;
import nc.tile.internal.fluid.Tank;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.Optional;

public class EmptyFluidIngredient implements IFluidIngredient {

	public EmptyFluidIngredient() {}
	
	@Override
	public FluidStack getStack() {
		return null;
	}
	
	@Override
	public List<FluidStack> getInputStackList() {
		return new ArrayList<>();
	}
	
	@Override
	public List<FluidStack> getInputStackHashingList() {
		return Lists.newArrayList((FluidStack)null);
	}
	
	@Override
	public List<FluidStack> getOutputStackList() {
		return new ArrayList<>();
	}

	@Override
	public int getMaxStackSize(int ingredientNumber) {
		return 0;
	}
	
	@Override
	public void setMaxStackSize(int stackSize) {
		
	}
	
	@Override
	public String getIngredientName() {
		return "null";
	}

	@Override
	public String getIngredientNamesConcat() {
		return "null";
	}
	
	@Override
	public IntList getFactors() {
		return new IntArrayList();
	}
	
	@Override
	public IFluidIngredient getFactoredIngredient(int factor) {
		return new EmptyFluidIngredient();
	}
	
	@Override
	public IngredientMatchResult match(Object object, IngredientSorption sorption) {
		if (object == null) {
			return IngredientMatchResult.PASS_0;
		}
		if (object instanceof Tank) {
			return new IngredientMatchResult(((Tank)object).getFluid() == null, 0);
		}
		return new IngredientMatchResult(object instanceof EmptyFluidIngredient, 0);
	}
	
	@Override
	public boolean isValid() {
		return true;
	}
	
	// CraftTweaker
	
	@Override
	@Optional.Method(modid = "crafttweaker")
	public crafttweaker.api.item.IIngredient ct() {
		return CraftTweakerMC.getILiquidStack(null);
	}
}
