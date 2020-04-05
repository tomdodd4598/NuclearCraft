package nc.radiation;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Lists;

import nc.init.NCBlocks;
import nc.recipe.ProcessorRecipeHandler;
import net.minecraft.init.Blocks;

public class RadBlockEffects {
	
	public static class RadiationBlockMutation extends ProcessorRecipeHandler {
		
		public RadiationBlockMutation() {
			super("radiation_block_mutation", 1, 0, 1, 0);
		}
		
		@Override
		public void addRecipes() {
			addRecipe(Lists.newArrayList("dirt", "grass"), NCBlocks.wasteland_earth, 10D);
			addRecipe(Lists.newArrayList("treeLeaves", "vine"), Blocks.AIR, 1D);
			addRecipe(Lists.newArrayList("treeSapling", "cropWheat", "cropPotato", "cropCarrot"), Blocks.AIR, 4D);
		}
		
		@Override
		public List fixExtras(List extras) {
			List fixed = new ArrayList(1);
			fixed.add(extras.size() > 0 && extras.get(0) instanceof Double ? (double) extras.get(0) : Double.MAX_VALUE);
			return fixed;
		}
	}
	
	public static class RadiationBlockPurification extends ProcessorRecipeHandler {
		
		public RadiationBlockPurification() {
			super("radiation_block_purification", 1, 0, 1, 0);
		}
		
		@Override
		public void addRecipes() {
			addRecipe(NCBlocks.wasteland_earth, "dirt", 0.001D);
		}
		
		@Override
		public List fixExtras(List extras) {
			List fixed = new ArrayList(1);
			fixed.add(extras.size() > 0 && extras.get(0) instanceof Double ? (double) extras.get(0) : 0D);
			return fixed;
		}
	}
}
