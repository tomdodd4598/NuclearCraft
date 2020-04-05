package nc.recipe.other;

import java.util.ArrayList;
import java.util.List;

import nc.config.NCConfig;
import nc.init.NCBlocks;
import nc.recipe.ProcessorRecipeHandler;
import nc.util.Lang;
import nc.util.NCMath;
import nc.util.UnitHelper;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

public class CollectorRecipes extends ProcessorRecipeHandler {
	
	public CollectorRecipes() {
		super("collector", 1, 0, 1, 1);
	}
	
	@Override
	public void addRecipes() {
		if (NCConfig.register_passive[0]) {
			String cobblePerTick = " " + Lang.localise("nuclearcraft.cobblestone") + "/t";
			addRecipe(NCBlocks.cobblestone_generator, new ItemStack(Blocks.COBBLESTONE), emptyFluidStack(), NCMath.sigFigs(NCConfig.processor_passive_rate[0], 5) + cobblePerTick);
			addRecipe(NCBlocks.cobblestone_generator_compact, new ItemStack(Blocks.COBBLESTONE), emptyFluidStack(), NCMath.sigFigs(NCConfig.processor_passive_rate[0]*8, 5) + cobblePerTick);
			addRecipe(NCBlocks.cobblestone_generator_dense, new ItemStack(Blocks.COBBLESTONE), emptyFluidStack(), NCMath.sigFigs(NCConfig.processor_passive_rate[0]*64, 5) + cobblePerTick);
		}
		
		if (NCConfig.register_passive[1]) {
			addRecipe(NCBlocks.water_source, emptyItemStack(), fluidStack("water", 1000), UnitHelper.prefix(NCConfig.processor_passive_rate[1], 5, "B/t", -1));
			addRecipe(NCBlocks.water_source_compact, emptyItemStack(), fluidStack("water", 1000), UnitHelper.prefix(NCConfig.processor_passive_rate[1]*8, 5, "B/t", -1));
			addRecipe(NCBlocks.water_source_dense, emptyItemStack(), fluidStack("water", 1000), UnitHelper.prefix(NCConfig.processor_passive_rate[1]*64, 5, "B/t", -1));
		}
		
		if (NCConfig.register_passive[2]) {
			addRecipe(NCBlocks.nitrogen_collector, emptyItemStack(), fluidStack("nitrogen", 1000), UnitHelper.prefix(NCConfig.processor_passive_rate[2], 5, "B/t", -1));
			addRecipe(NCBlocks.nitrogen_collector_compact, emptyItemStack(), fluidStack("nitrogen", 1000), UnitHelper.prefix(NCConfig.processor_passive_rate[2]*8, 5, "B/t", -1));
			addRecipe(NCBlocks.nitrogen_collector_dense, emptyItemStack(), fluidStack("nitrogen", 1000), UnitHelper.prefix(NCConfig.processor_passive_rate[2]*64, 5, "B/t", -1));
		}
	}
	
	@Override
	public List fixExtras(List extras) {
		List fixed = new ArrayList(1);
		fixed.add(extras.size() > 0 && extras.get(0) instanceof String ? (String) extras.get(0) : null);
		return fixed;
	}
}
