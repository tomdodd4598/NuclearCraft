package nc.integration.groovyscript;

import com.cleanroommc.groovyscript.api.GroovyBlacklist;
import com.cleanroommc.groovyscript.api.documentation.annotations.MethodDescription;
import com.cleanroommc.groovyscript.api.documentation.annotations.MethodDescription.Type;
import com.cleanroommc.groovyscript.helper.Alias;
import com.cleanroommc.groovyscript.registry.VirtualizedRegistry;
import com.google.common.base.CaseFormat;
import nc.recipe.*;
import nc.recipe.ingredient.*;
import nc.util.*;

import javax.annotation.Nullable;
import java.util.*;

public abstract class GSBasicRecipeRegistry extends VirtualizedRegistry<BasicRecipe> {
	
	@GroovyBlacklist
	protected final Lazy<BasicRecipeHandler> recipeHandler;
	
	public GSBasicRecipeRegistry(String name) {
		super(Alias.generateOf(name, CaseFormat.LOWER_UNDERSCORE));
		this.recipeHandler = new Lazy<>(() -> NCRecipes.getHandler(name));
	}
	
	@GroovyBlacklist
	protected BasicRecipeHandler getRecipeHandler() {
		return recipeHandler.get();
	}
	
	@GroovyBlacklist
	@Override
	public void onReload() {
		BasicRecipeHandler recipeHandler = getRecipeHandler();
		removeScripted().forEach(recipeHandler::removeRecipe);
		restoreFromBackup().forEach(recipeHandler::addRecipe);
		getRecipeHandler().preReload();
	}
	
	@GroovyBlacklist
	@Override
	public void afterScriptLoad() {
		getRecipeHandler().postReload();
	}
	
	@GroovyBlacklist
	protected @Nullable BasicRecipe addRecipeInternal(Object... objects) {
		BasicRecipeHandler recipeHandler = getRecipeHandler();
		List<Object> objectList = Arrays.asList(objects);
		BasicRecipe recipe = recipeHandler.buildRecipe(StreamHelper.map(objectList.subList(0, recipeHandler.itemInputLastIndex), GSHelper::buildAdditionItemIngredient), StreamHelper.map(objectList.subList(recipeHandler.itemInputLastIndex, recipeHandler.fluidInputLastIndex), GSHelper::buildAdditionFluidIngredient), StreamHelper.map(objectList.subList(recipeHandler.fluidInputLastIndex, recipeHandler.itemOutputLastIndex), GSHelper::buildAdditionItemIngredient), StreamHelper.map(objectList.subList(recipeHandler.itemOutputLastIndex, recipeHandler.fluidOutputLastIndex), GSHelper::buildAdditionFluidIngredient), objectList.subList(recipeHandler.fluidOutputLastIndex, objects.length), recipeHandler.isShapeless);
		
		if (recipeHandler.addRecipe(recipe) != null) {
			addScripted(recipe);
			return recipe;
		}
		else {
			return null;
		}
	}
	
	@GroovyBlacklist
	protected void removeRecipeWithInputInternal(Object... inputs) {
		BasicRecipeHandler recipeHandler = getRecipeHandler();
		List<Object> inputList = Arrays.asList(inputs);
		List<IItemIngredient> itemIngredients = StreamHelper.map(inputList.subList(0, recipeHandler.itemInputSize), GSHelper::buildRemovalItemIngredient);
		List<IFluidIngredient> fluidIngredients = StreamHelper.map(inputList.subList(recipeHandler.itemInputSize, recipeHandler.itemInputSize + recipeHandler.fluidInputSize), GSHelper::buildRemovalFluidIngredient);
		BasicRecipe recipe = recipeHandler.getRecipeFromIngredients(itemIngredients, fluidIngredients);
		while (recipeHandler.removeRecipe(recipe)) {
			addBackup(recipe);
			recipe = recipeHandler.getRecipeFromIngredients(itemIngredients, fluidIngredients);
		}
	}
	
	@GroovyBlacklist
	protected void removeRecipeWithOutputInternal(Object... outputs) {
		BasicRecipeHandler recipeHandler = getRecipeHandler();
		List<Object> outputList = Arrays.asList(outputs);
		List<IItemIngredient> itemProducts = StreamHelper.map(outputList.subList(0, recipeHandler.itemOutputSize), GSHelper::buildRemovalItemIngredient);
		List<IFluidIngredient> fluidProducts = StreamHelper.map(outputList.subList(recipeHandler.itemOutputSize, recipeHandler.itemOutputSize + recipeHandler.fluidOutputSize), GSHelper::buildRemovalFluidIngredient);
		BasicRecipe recipe = recipeHandler.getRecipeFromProducts(itemProducts, fluidProducts);
		while (recipeHandler.removeRecipe(recipe)) {
			addBackup(recipe);
			recipe = recipeHandler.getRecipeFromProducts(itemProducts, fluidProducts);
		}
	}
	
	@GroovyBlacklist
	protected void removeAllRecipesInternal() {
		BasicRecipeHandler recipeHandler = getRecipeHandler();
		recipeHandler.getRecipeList().forEach(this::addBackup);
		recipeHandler.removeAllRecipes();
	}
	
	@MethodDescription(type = Type.QUERY)
	@Override
	public String getName() {
		return super.getName();
	}
	
	@MethodDescription(type = Type.QUERY)
	public List<BasicRecipe> getRecipeList() {
		return getRecipeHandler().getRecipeList();
	}
	
	@MethodDescription(type = Type.QUERY)
	public int getItemInputSize() {
		return getRecipeHandler().getItemInputSize();
	}
	
	@MethodDescription(type = Type.QUERY)
	public int getFluidInputSize() {
		return getRecipeHandler().getFluidInputSize();
	}
	
	@MethodDescription(type = Type.QUERY)
	public int getItemOutputSize() {
		return getRecipeHandler().getItemOutputSize();
	}
	
	@MethodDescription(type = Type.QUERY)
	public int getFluidOutputSize() {
		return getRecipeHandler().getFluidOutputSize();
	}
	
	@MethodDescription(type = Type.QUERY)
	public boolean isShapeless() {
		return getRecipeHandler().isShapeless();
	}
	
	@MethodDescription(type = Type.ADDITION)
	public void addRecipe(Object... objects) {
		addRecipeInternal(objects);
	}
	
	@MethodDescription(type = Type.REMOVAL)
	public void removeRecipeWithInput(Object... inputs) {
		removeRecipeWithInputInternal(inputs);
	}
	
	@MethodDescription(type = Type.REMOVAL)
	public void removeRecipeWithOutput(Object... outputs) {
		removeRecipeWithOutputInternal(outputs);
	}
	
	@MethodDescription(type = Type.REMOVAL)
	public void removeAllRecipes() {
		removeAllRecipesInternal();
	}
}
