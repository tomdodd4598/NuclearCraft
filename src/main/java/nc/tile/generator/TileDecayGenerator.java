package nc.tile.generator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import nc.config.NCConfig;
import nc.recipe.IItemIngredient;
import nc.recipe.NCRecipes;
import nc.recipe.ProcessorRecipe;
import nc.recipe.ProcessorRecipeHandler;
import nc.tile.dummy.IInterfaceable;
import nc.tile.energy.TileEnergy;
import nc.tile.internal.energy.EnergyConnection;
import nc.tile.internal.fluid.Tank;
import nc.util.EnergyHelper;
import nc.util.ItemStackHelper;
import nc.util.RecipeHelper;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;

public class TileDecayGenerator extends TileEnergy implements IInterfaceable {
	
	Random rand = new Random();
	public int tickCount;
	
	public final NCRecipes.Type decayGenRecipeType;
	protected ProcessorRecipe[] recipes = new ProcessorRecipe[6];
	
	public TileDecayGenerator() {
		super(maxPower(), energyConnectionAll(EnergyConnection.OUT));
		decayGenRecipeType = NCRecipes.Type.DECAY_GENERATOR;
	}
	
	public ProcessorRecipeHandler getRecipeHandler() {
		return decayGenRecipeType.getRecipeHandler();
	}
	
	@Override
	public void update() {
		super.update();
		for (EnumFacing side : EnumFacing.VALUES) {
			recipes[side.getIndex()] = getRecipeHandler().getRecipeFromInputs(Arrays.asList(ItemStackHelper.blockStateToStack(world.getBlockState(getPos().offset(side)))), new ArrayList<Tank>());
		}
		if(!world.isRemote) {
			tickTile();
			if (shouldTileCheck()) getEnergyStorage().changeEnergyStored(getGenerated());
			pushEnergy();
		}
	}
	
	private static int maxPower() {
		int max = 0;
		List<ProcessorRecipe> recipes = NCRecipes.Type.DECAY_GENERATOR.getRecipeHandler().getRecipes();
		for (ProcessorRecipe recipe : recipes) {
			if (recipe == null || recipe.extras().size() < 2) continue;
			if (recipe.extras().get(1) instanceof Double) {
				max = (int) Math.max(max, (double) recipe.extras().get(1));
			}
		}
		return 6*max*NCConfig.machine_update_rate/20;
	}
	
	public int getGenerated() {
		int power = 0;
		for (EnumFacing side : EnumFacing.VALUES) {
			power += decayGen(side);
		}
		return power;
	}
	
	public int decayGen(EnumFacing side) {
		ProcessorRecipe recipe = getDecayRecipe(side);
		if (recipe == null) return 0;
		ItemStack stack = getOutput(side);
		if (stack.isEmpty() || stack == null) return 0;
		Block block = ItemStackHelper.getBlockFromStack(stack);
		if (block == null) return 0;
		int meta = stack.getMetadata();
		if (rand.nextDouble()*getRecipeLifetime(side) < 1D) getWorld().setBlockState(getPos().offset(side), block.getStateFromMeta(meta));
		return getRecipePower(side);
	}
	
	// IC2
	
	@Override
	public int getSourceTier() {
		return EnergyHelper.getEUTier(maxPower());
	}
	
	@Override
	public int getSinkTier() {
		return 4;
	}
	
	// Recipe from BlockPos
	
	public ProcessorRecipe getDecayRecipe(EnumFacing side) {
		return recipes[side.getIndex()];
	}
	
	public double getRecipeLifetime(EnumFacing side) {
		ProcessorRecipe recipe = getDecayRecipe(side);
		if (recipe == null || recipe.extras().isEmpty()) return 1200D;
		if (recipe.extras().get(0) instanceof Double) return ((double) recipe.extras().get(0))/NCConfig.machine_update_rate;
		return 1200D/NCConfig.machine_update_rate;
	}
	
	public int getRecipePower(EnumFacing side) {
		ProcessorRecipe recipe = getDecayRecipe(side);
		if (recipe == null || recipe.extras().size() < 2) return 5;
		if (recipe.extras().get(1) instanceof Double) return (int)(((double) recipe.extras().get(1))*NCConfig.machine_update_rate/20);
		return 5*NCConfig.machine_update_rate/20;
	}
	
	public ItemStack getOutput(EnumFacing side) {
		ProcessorRecipe recipe = getDecayRecipe(side);
		if (recipe == null) return ItemStack.EMPTY;
		List<IItemIngredient> outputs = recipe.itemProducts();
		ItemStack output = RecipeHelper.getItemStackFromIngredientList(outputs, 0);
		if (output != null) return output;
		return ItemStack.EMPTY;
	}
}
