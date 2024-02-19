package nc.integration.jei.category.info.builder;

import java.util.List;

import nc.integration.jei.category.*;
import nc.integration.jei.category.info.*;
import nc.integration.jei.wrapper.*;
import nc.tile.processor.info.builder.ContainerInfoBuilder;

public class JEISimpleCategoryInfoBuilder<WRAPPER extends JEISimpleRecipeWrapper<WRAPPER>> extends ContainerInfoBuilder<JEISimpleCategoryInfoBuilder<WRAPPER>> {
	
	public final Class<WRAPPER> jeiRecipeClass;
	public final JEIRecipeWrapperFunction<WRAPPER, JEISimpleRecipeCategory<WRAPPER>, JEISimpleCategoryInfo<WRAPPER>> jeiRecipeFunction;
	
	public final List<Object> jeiCrafters;
	
	public JEISimpleCategoryInfoBuilder(String modId, String name, Class<WRAPPER> jeiRecipeClass, JEIRecipeWrapperFunction<WRAPPER, JEISimpleRecipeCategory<WRAPPER>, JEISimpleCategoryInfo<WRAPPER>> jeiRecipeFunction, List<Object> jeiCrafters) {
		super(modId, name);
		
		this.jeiRecipeClass = jeiRecipeClass;
		this.jeiRecipeFunction = jeiRecipeFunction;
		
		this.jeiCrafters = jeiCrafters;
	}
	
	public JEISimpleCategoryInfo<WRAPPER> buildCategoryInfo() {
		return new JEISimpleCategoryInfo<>(modId, name, jeiRecipeClass, jeiRecipeFunction, jeiCrafters, guiWH, itemInputGuiXYWH, fluidInputGuiXYWH, itemOutputGuiXYWH, fluidOutputGuiXYWH, playerGuiXY, progressBarGuiXYWHUV, jeiCategoryEnabled, jeiCategoryUid, jeiTitle, jeiTexture, jeiBackgroundXYWH, jeiTooltipXYWH, jeiClickAreaXYWH);
	}
}
