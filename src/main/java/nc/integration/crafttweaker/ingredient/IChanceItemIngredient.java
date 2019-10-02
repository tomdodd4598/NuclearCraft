package nc.integration.crafttweaker.ingredient;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.nuclearcraft.IChanceItemIngredient")
@ZenRegister
public interface IChanceItemIngredient extends IIngredient {
	
	@ZenMethod
	public static IChanceItemIngredient create(IIngredient ingredient, int chancePercent, @Optional int minStackSize) {
		return new CTChanceItemIngredient(ingredient, chancePercent, minStackSize);
	}
	
	public IIngredient getInternalIngredient();
	
	public int getChancePercent();
	
	public int getMinStackSize();
}
