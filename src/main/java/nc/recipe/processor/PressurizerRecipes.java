package nc.recipe.processor;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Lists;

import nc.init.NCItems;
import nc.recipe.ProcessorRecipeHandler;
import nc.util.OreDictHelper;
import nc.util.RegistryHelper;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraftforge.oredict.OreDictionary;

public class PressurizerRecipes extends ProcessorRecipeHandler {
	
	public PressurizerRecipes() {
		super("pressurizer", 1, 0, 1, 0);
	}

	@Override
	public void addRecipes() {
		addRecipe("dustGraphite", "coal", 1D, 1D);
		addRecipe("ingotGraphite", "ingotPyrolyticCarbon", 1D, 1D);
		addRecipe("dustDiamond", "gemDiamond", 1D, 1D);
		addRecipe("dustRhodochrosite", "gemRhodochrosite", 1D, 1D);
		addRecipe(Lists.newArrayList("dustQuartz", "dustNetherQuartz"), "gemQuartz", 1D, 1D);
		addRecipe(oreStack("dustObsidian", 4), Blocks.OBSIDIAN, 1.5D, 1.5D);
		addRecipe("dustBoronNitride", "gemBoronNitride", 1D, 1D);
		addRecipe("dustFluorite", "gemFluorite", 1D, 1D);
		addRecipe("dustVilliaumite", "gemVilliaumite", 1D, 1D);
		addRecipe("dustCarobbiite", "gemCarobbiite", 1D, 1D);
		addRecipe(oreStackList(Lists.newArrayList("dustWheat", "foodFlour"), 2), NCItems.graham_cracker, 0.25D, 0.5D);
		
		// IC2
		addRecipe(oreStack("dustClay", 4), "dustSiliconDioxide", 1D, 1D);
		
		// Tech Reborn
		addRecipe(RegistryHelper.itemStackFromRegistry("techreborn:part:34"), RegistryHelper.itemStackFromRegistry("techreborn:plates:2"), 1D, 1D);
		
		// AE2
		addRecipe("dustEnder", Items.ENDER_PEARL, 1D, 1D);
		
		addPlatePressingRecipes();
	}
	
	private static final List<String> PLATE_BLACKLIST = Lists.newArrayList("Graphite");
	
	public void addPlatePressingRecipes() {
		for (String ore : OreDictionary.getOreNames()) {
			if (ore.startsWith("plate")) {
				String type = ore.substring(5);
				if (PLATE_BLACKLIST.contains(type)) continue;
				String ingot = "ingot" + type, gem = "gem" + type;
				if (OreDictHelper.oreExists(ingot)) {
					addRecipe(ingot, ore, 1D, 1D);
				} else if (OreDictHelper.oreExists(gem)) {
					addRecipe(gem, ore, 1D, 1D);
				}
			}
			if (ore.startsWith("plateDense")) {
				String plate = "plate" + ore.substring(10);
				if (OreDictHelper.oreExists(plate)) {
					addRecipe(oreStack(plate, 9), ore, 2D, 2D);
				}
			}
		}
	}
	
	@Override
	public List fixExtras(List extras) {
		List fixed = new ArrayList(3);
		fixed.add(extras.size() > 0 && extras.get(0) instanceof Double ? (double) extras.get(0) : 1D);
		fixed.add(extras.size() > 1 && extras.get(1) instanceof Double ? (double) extras.get(1) : 1D);
		fixed.add(extras.size() > 2 && extras.get(2) instanceof Double ? (double) extras.get(2) : 0D);
		return fixed;
	}
}
