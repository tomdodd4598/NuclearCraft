package nc.integration.jei.category.info.builder;

import nc.integration.jei.category.JEISimpleRecipeCategory;
import nc.integration.jei.category.info.*;
import nc.integration.jei.wrapper.*;
import nc.tile.processor.info.builder.ContainerInfoBuilder;

import java.util.List;

public class JEISimpleCategoryInfoBuilder<WRAPPER extends JEISimpleRecipeWrapper<WRAPPER>> extends ContainerInfoBuilder<JEISimpleCategoryInfoBuilder<WRAPPER>> {
	
	public final Class<WRAPPER> jeiRecipeClass;
	public final JEIRecipeWrapperFunction<WRAPPER, JEISimpleRecipeCategory<WRAPPER>, JEISimpleCategoryInfo<WRAPPER>> jeiRecipeFunction;
	
	public final List<Object> jeiCrafters;
	
	public final List<JEIContainerConnection> jeiContainerConnections;
	
	public JEISimpleCategoryInfoBuilder(String modId, String name, Class<WRAPPER> jeiRecipeClass, JEIRecipeWrapperFunction<WRAPPER, JEISimpleRecipeCategory<WRAPPER>, JEISimpleCategoryInfo<WRAPPER>> jeiRecipeFunction, List<Object> jeiCrafters, List<JEIContainerConnection> jeiContainerConnections) {
		super(modId, name);
		
		this.jeiRecipeClass = jeiRecipeClass;
		this.jeiRecipeFunction = jeiRecipeFunction;
		
		this.jeiCrafters = jeiCrafters;
		
		this.jeiContainerConnections = jeiContainerConnections;
	}
	
	public JEISimpleCategoryInfo<WRAPPER> buildCategoryInfo() {
		return new JEISimpleCategoryInfo<>(this);
	}
}
