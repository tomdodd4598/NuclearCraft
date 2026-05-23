package nc.radiation;

import com.google.common.collect.Lists;
import nc.init.NCBlocks;
import nc.recipe.BasicRecipeHandler;
import net.minecraft.init.Blocks;

import java.util.List;

public class RadBlockEffects {
	
	public static class RadiationBlockMutation extends BasicRecipeHandler {
		
		public RadiationBlockMutation() {
			super("radiation_block_mutation", 1, 0, 1, 0);
		}
		
		@Override
		public void addRecipes() {
			addRecipe(Lists.newArrayList("dirt", "grass"), NCBlocks.wasteland_earth, 10D);
			addRecipe(Lists.newArrayList("treeLeaves", "vine"), Blocks.AIR, 1D);
			addRecipe("treeSapling", Blocks.DEADBUSH, 4D);
		}
		
		@Override
		public List<Object> fixedExtras(List<Object> extras) {
			ExtrasFixer fixer = new ExtrasFixer(extras);
			fixer.add(Double.class, Double.MAX_VALUE);
			return fixer.fixed;
		}
	}
	
	public static class RadiationBlockPurification extends BasicRecipeHandler {
		
		public RadiationBlockPurification() {
			super("radiation_block_purification", 1, 0, 1, 0);
		}
		
		@Override
		public void addRecipes() {
			addRecipe(NCBlocks.wasteland_earth, "dirt", 0.001D);
		}
		
		@Override
		public List<Object> fixedExtras(List<Object> extras) {
			ExtrasFixer fixer = new ExtrasFixer(extras);
			fixer.add(Double.class, 0D);
			return fixer.fixed;
		}
	}
	
	public static class WastelandBlockMapping extends BasicRecipeHandler {
		
		public WastelandBlockMapping() {
			super("wasteland_block_mapping", 1, 0, 1, 0);
		}
		
		@Override
		public void addRecipes() {
		
		}
		
		@Override
		public List<Object> fixedExtras(List<Object> extras) {
			ExtrasFixer fixer = new ExtrasFixer(extras);
			return fixer.fixed;
		}
	}
}
