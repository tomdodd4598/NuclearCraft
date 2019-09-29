package nc.recipe.other;

import nc.config.NCConfig;
import nc.init.NCBlocks;
import nc.recipe.ProcessorRecipeHandler;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

public class CollectorRecipes extends ProcessorRecipeHandler {
	
	public CollectorRecipes() {
		super("collector", 1, 0, 1, 1);
	}
	
	@Override
	public void addRecipes() {
		if (NCConfig.register_passive[0]) {
			addRecipe(NCBlocks.cobblestone_generator, new ItemStack(Blocks.COBBLESTONE, NCConfig.processor_passive_rate[0]), emptyFluidStack());
			addRecipe(NCBlocks.cobblestone_generator_compact, new ItemStack(Blocks.COBBLESTONE, NCConfig.processor_passive_rate[0]*8), emptyFluidStack());
			addRecipe(NCBlocks.cobblestone_generator_dense, new ItemStack(Blocks.COBBLESTONE, NCConfig.processor_passive_rate[0]*64), emptyFluidStack());
		}
		
		if (NCConfig.register_passive[1]) {
			addRecipe(NCBlocks.water_source, emptyItemStack(), fluidStack("water", NCConfig.processor_passive_rate[1]));
			addRecipe(NCBlocks.water_source_compact, emptyItemStack(), fluidStack("water", NCConfig.processor_passive_rate[1]*8));
			addRecipe(NCBlocks.water_source_dense, emptyItemStack(), fluidStack("water", NCConfig.processor_passive_rate[1]*64));
		}
		
		if (NCConfig.register_passive[2]) {
			addRecipe(NCBlocks.nitrogen_collector, emptyItemStack(), fluidStack("nitrogen", NCConfig.processor_passive_rate[2]));
			addRecipe(NCBlocks.nitrogen_collector_compact, emptyItemStack(), fluidStack("nitrogen", NCConfig.processor_passive_rate[2]*8));
			addRecipe(NCBlocks.nitrogen_collector_dense, emptyItemStack(), fluidStack("nitrogen", NCConfig.processor_passive_rate[2]*64));
		}
	}
}
