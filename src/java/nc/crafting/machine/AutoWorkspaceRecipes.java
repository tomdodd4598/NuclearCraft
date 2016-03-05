package nc.crafting.machine;

import nc.block.basic.NCBlocks;
import nc.crafting.NCRecipeHelper;
import net.minecraft.item.ItemStack;

public class AutoWorkspaceRecipes extends NCRecipeHelper {

	private static final AutoWorkspaceRecipes recipes = new AutoWorkspaceRecipes();

	public AutoWorkspaceRecipes(){
		super(9, 1);
	}
	public static final NCRecipeHelper instance() {
		return recipes;
	}

	public void addRecipes() {
		addRecipe(oreStack("plateBasic", 16), oreStack("ingotTough", 4), "", "", "", "", "", "", "", new ItemStack(NCBlocks.reactorBlock, 16));
		addRecipe(oreStack("ingotTough", 4), oreStack("plateBasic", 16), "", "", "", "", "", "", "", new ItemStack(NCBlocks.reactorBlock, 16));
	}
}