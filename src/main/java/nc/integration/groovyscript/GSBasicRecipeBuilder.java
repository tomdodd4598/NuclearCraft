package nc.integration.groovyscript;

import com.cleanroommc.groovyscript.api.IIngredient;
import com.cleanroommc.groovyscript.api.*;
import com.cleanroommc.groovyscript.helper.recipe.AbstractRecipeBuilder;
import it.unimi.dsi.fastutil.ints.*;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import nc.recipe.*;
import nc.recipe.ingredient.*;

import javax.annotation.Nullable;
import java.util.List;
import java.util.stream.*;

public class GSBasicRecipeBuilder<BUILDER extends GSBasicRecipeBuilder<BUILDER>> extends AbstractRecipeBuilder<BasicRecipe> {
	
	public final GSBasicRecipeRegistry registry;
	
	public final Int2ObjectMap<Object> fullOutputMap = new Int2ObjectOpenHashMap<>();
	public final Int2ObjectMap<Object> fullFluidOutputMap = new Int2ObjectOpenHashMap<>();
	
	public final ObjectArrayList<Object> extras = new ObjectArrayList<>();
	
	public GSBasicRecipeBuilder(GSBasicRecipeRegistry registry) {
		this.registry = registry;
	}
	
	@SuppressWarnings("unchecked")
	protected BUILDER getThis() {
		return (BUILDER) this;
	}
	
	@GroovyBlacklist
	protected BasicRecipeHandler getRecipeHandler() {
		return registry.getRecipeHandler();
	}
	
	@GroovyBlacklist
	protected GSBasicRecipeBuilder<BUILDER> outputObject(Object output) {
		fullOutputMap.put(this.output.size(), output);
		this.output.add(GSHelper.buildAdditionItemIngredient(output).getStack());
		return this;
	}
	
	@GroovyBlacklist
	protected <T> GSBasicRecipeBuilder<BUILDER> outputObjects(T[] outputs) {
		for (T output : outputs) {
			outputObject(output);
		}
		return this;
	}
	
	public GSBasicRecipeBuilder<BUILDER> output(IIngredient output) {
		return outputObject(output);
	}
	
	public GSBasicRecipeBuilder<BUILDER> output(IIngredient... outputs) {
		return outputObjects(outputs);
	}
	
	public GSBasicRecipeBuilder<BUILDER> output(IItemIngredient output) {
		return outputObject(output);
	}
	
	public GSBasicRecipeBuilder<BUILDER> output(IItemIngredient... outputs) {
		return outputObjects(outputs);
	}
	
	@GroovyBlacklist
	protected GSBasicRecipeBuilder<BUILDER> fluidOutputObject(Object output) {
		fullFluidOutputMap.put(fluidOutput.size(), output);
		fluidOutput.add(GSHelper.buildAdditionFluidIngredient(output).getStack());
		return this;
	}
	
	@GroovyBlacklist
	protected <T> GSBasicRecipeBuilder<BUILDER> fluidOutputObjects(T[] outputs) {
		for (T output : outputs) {
			fluidOutputObject(output);
		}
		return this;
	}
	
	public GSBasicRecipeBuilder<BUILDER> fluidOutput(IFluidIngredient output) {
		return fluidOutputObject(output);
	}
	
	public GSBasicRecipeBuilder<BUILDER> fluidOutput(IFluidIngredient... outputs) {
		return fluidOutputObjects(outputs);
	}
	
	public BUILDER setExtra(int index, Object extra) {
		extras.ensureCapacity(index + 1);
		if (extras.size() <= index) {
			extras.size(index + 1);
		}
		extras.set(index, extra);
		return getThis();
	}
	
	@Override
	public String getErrorMsg() {
		return "Error building NuclearCraft " + getRecipeHandler().getName() + " recipe";
	}
	
	@Override
	public void validate(GroovyLog.Msg msg) {
		BasicRecipeHandler recipeHandler = getRecipeHandler();
		validateItems(msg, 0, recipeHandler.itemInputSize, 0, recipeHandler.itemOutputSize);
		validateFluids(msg, 0, recipeHandler.fluidInputSize, 0, recipeHandler.fluidOutputSize);
	}
	
	@Override
	public @Nullable BasicRecipe register() {
		if (!validate()) {
			return null;
		}
		
		BasicRecipeHandler recipeHandler = getRecipeHandler();
		while (input.size() < recipeHandler.itemInputSize) {
			input.add(null);
		}
		while (fluidInput.size() < recipeHandler.fluidInputSize) {
			fluidInput.add(null);
		}
		while (output.size() < recipeHandler.itemOutputSize) {
			output.add(null);
		}
		while (fluidOutput.size() < recipeHandler.fluidOutputSize) {
			fluidOutput.add(null);
		}
		
		List<Object> fullOutput = IntStream.range(0, recipeHandler.itemOutputSize).mapToObj(x -> fullOutputMap.getOrDefault(x, output.get(x))).collect(Collectors.toList());
		List<Object> fullFluidOutput = IntStream.range(0, recipeHandler.fluidOutputSize).mapToObj(x -> fullFluidOutputMap.getOrDefault(x, fluidOutput.get(x))).collect(Collectors.toList());
		return registry.addRecipeInternal(Stream.of(input, fluidInput, fullOutput, fullFluidOutput, extras).flatMap(List::stream).toArray());
	}
}
