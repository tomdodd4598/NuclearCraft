package nc.integration.crafttweaker.ingredient;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.nuclearcraft.IChanceFluidIngredient")
@ZenRegister
public interface IChanceFluidIngredient extends IIngredient {
	
	@ZenMethod
	public static IChanceFluidIngredient create(IIngredient ingredient, int chancePercent, int stackDiff, @Optional int minStackSize) {
		return new CTChanceFluidIngredient(ingredient, chancePercent, stackDiff, minStackSize);
	}
	
	public IIngredient getInternalIngredient();
	
	public int getChancePercent();
	
	public int getStackDiff();
	
	public int getMinStackSize();
}
