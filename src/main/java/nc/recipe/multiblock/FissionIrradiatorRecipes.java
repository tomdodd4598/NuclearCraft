package nc.recipe.multiblock;

import static nc.config.NCConfig.*;

import java.util.List;

import com.google.common.collect.Lists;

import nc.radiation.RadSources;
import nc.recipe.BasicRecipeHandler;

public class FissionIrradiatorRecipes extends BasicRecipeHandler {
	
	public FissionIrradiatorRecipes() {
		super("fission_irradiator", 1, 0, 1, 0);
	}
	
	@Override
	public void addRecipes() {
		addRecipe(Lists.newArrayList("ingotThorium", "dustThorium"), "dustTBP", 160000, fission_irradiator_heat_per_flux[0], fission_irradiator_efficiency[0], 0, -1, RadSources.THORIUM);
		addRecipe(Lists.newArrayList("ingotTBP", "dustTBP"), "dustProtactinium233", 2720000, fission_irradiator_heat_per_flux[1], fission_irradiator_efficiency[1], 0, -1, RadSources.TBP);
		addRecipe(Lists.newArrayList("ingotBismuth", "dustBismuth"), "dustPolonium", 1920000, fission_irradiator_heat_per_flux[2], fission_irradiator_efficiency[2], 0, -1, RadSources.BISMUTH);
	}
	
	@Override
	protected List<Object> fixedExtras(List<Object> extras) {
		ExtrasFixer fixer = new ExtrasFixer(extras);
		fixer.add(Integer.class, 1);
		fixer.add(Double.class, 0D);
		fixer.add(Double.class, 0D);
		fixer.add(Integer.class, 0);
		fixer.add(Integer.class, -1);
		fixer.add(Double.class, 0D);
		return fixer.fixed;
	}
}
