package nc.recipe.processor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import nc.config.NCConfig;
import nc.recipe.BaseRecipeHandler;
import nc.util.StringHelper;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.oredict.OreDictionary;

public class IngotFormerRecipes extends BaseRecipeHandler {
	
	public IngotFormerRecipes() {
		super(0, 1, 1, 0, false);
	}

	@Override
	public void addRecipes() {
		ingotForm();
		
		// Tinkers' Construct		
		ingotForm("Manyullyn");
		ingotForm("Alubrass");
		ingotForm("Pigiron");
		ingotForm("Brass");
		ingotForm("Bronze");
		ingotForm("Electrum");
		ingotForm("Steel");
	}
	
	public void ingotForm(String metal) {
		addRecipe(fluidStack(metal.toLowerCase(), 144), "ingot" + metal, NCConfig.processor_time[10]);
	}
	
	public void ingotForm() {
		List<String> oreList = Arrays.asList(OreDictionary.getOreNames());
		ArrayList<Fluid> fluidValueList = new ArrayList(FluidRegistry.getRegisteredFluids().values());
		ArrayList<String>fluidList = new ArrayList<String>();
		for (Fluid fluid : fluidValueList) {
			fluidList.add(fluid.getName());
		}
		for (String fluidName : fluidList) {
			String ingot = "ingot" + StringHelper.capitalize(fluidName);
			if (oreList.contains(ingot)) addRecipe(fluidStack(fluidName, 144), ingot, NCConfig.processor_time[10]);
		}
	}

	@Override
	public String getRecipeName() {
		return "ingot_former";
	}
}
