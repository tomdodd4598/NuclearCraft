package nc.radiation;

import com.google.common.collect.Lists;

import nc.init.NCBlocks;
import nc.recipe.ProcessorRecipeHandler;
import net.minecraft.init.Blocks;

public class RadBlockEffects extends ProcessorRecipeHandler {
	
	public RadBlockEffects() {
		super("rad_block_mutations", 1, 0, 1, 0);
	}
	
	@Override
	public void addRecipes() {
		addRecipe(Lists.newArrayList("dirt", "grass"), NCBlocks.dry_earth, 10D);
		addRecipe(Lists.newArrayList("treeLeaves", "vine"), Blocks.AIR, 1D);
		addRecipe(Lists.newArrayList("treeSapling", "cropWheat", "cropPotato", "cropCarrot"), Blocks.AIR, 4D);
	}
}
