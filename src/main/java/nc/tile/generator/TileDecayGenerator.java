package nc.tile.generator;

import java.util.ArrayList;
import java.util.Random;

import nc.config.NCConfig;
import nc.recipe.BaseRecipeHandler;
import nc.recipe.IIngredient;
import nc.recipe.IRecipe;
import nc.recipe.NCRecipes;
import nc.recipe.RecipeMethods;
import nc.tile.dummy.IInterfaceable;
import nc.tile.energy.TileEnergy;
import nc.tile.internal.energy.EnergyConnection;
import nc.util.EnergyHelper;
import nc.util.ItemStackHelper;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public class TileDecayGenerator extends TileEnergy implements IInterfaceable {
	
	Random rand = new Random();
	public int tickCount;
	
	public final NCRecipes.Type decayGenRecipeType;
	
	public TileDecayGenerator() {
		super(NCConfig.generator_rf_per_eu*maxPower(), energyConnectionAll(EnergyConnection.OUT));
		decayGenRecipeType = NCRecipes.Type.DECAY_GENERATOR;
	}
	
	public BaseRecipeHandler getRecipeHandler() {
		return decayGenRecipeType.getRecipeHandler();
	}
	
	@Override
	public void update() {
		super.update();
		if(!world.isRemote) {
			if (shouldCheck()) getEnergyStorage().changeEnergyStored(getGenerated());
			pushEnergy();
		}
	}
	
	public boolean shouldCheck() {
		if (tickCount > NCConfig.generator_update_rate) tickCount = 0; else tickCount++;
		return tickCount > NCConfig.generator_update_rate;
	}
	
	private static int maxPower() {
		int max = 0;
		ArrayList<IRecipe> recipes = NCRecipes.Type.DECAY_GENERATOR.getRecipeHandler().getRecipes();
		for (IRecipe recipe : recipes) {
			if (recipe == null) continue;
			if (recipe.extras().get(1) instanceof Integer) {
				max = Math.max(max, (int) recipe.extras().get(1));
			}
		}
		return 6*max*NCConfig.generator_update_rate/20;
	}
	
	public int getGenerated() {
		int power = 0;
		for (EnumFacing side : EnumFacing.VALUES) {
			power += decayGen(getPos().offset(side));
		}
		return power;
	}
	
	public int decayGen(BlockPos pos) {
		IRecipe recipe = getDecayRecipe(pos);
		if (recipe == null) return 0;
		ItemStack stack = getOutput(pos);
		if (stack.isEmpty() || stack == null) return 0;
		Block block = ItemStackHelper.getBlockFromStack(stack);
		if (block == null) return 0;
		int meta = stack.getMetadata();
		if (rand.nextDouble()*getRecipeLifetime(pos) < 1D) getWorld().setBlockState(pos, block.getStateFromMeta(meta));
		return getRecipePower(pos);
	}
	
	// IC2
	
	@Override
	public int getSourceTier() {
		return EnergyHelper.getEUSourceTier(maxPower());
	}
	
	@Override
	public int getSinkTier() {
		return 4;
	}
	
	// Recipe from BlockPos
	
	public IRecipe getDecayRecipe(BlockPos pos) {
		return getRecipeHandler().getRecipeFromInputs(new Object[] {ItemStackHelper.blockStateToStack(world.getBlockState(pos))});
	}
	
	public double getRecipeLifetime(BlockPos pos) {
		IRecipe recipe = getDecayRecipe(pos);
		if (recipe == null) return 1200D;
		if (recipe.extras().get(0) instanceof Double) return ((double) recipe.extras().get(0))/NCConfig.generator_update_rate;
		return 1200D/NCConfig.generator_update_rate;
	}
	
	public int getRecipePower(BlockPos pos) {
		IRecipe recipe = getDecayRecipe(pos);
		if (recipe == null) return 5;
		if (recipe.extras().get(1) instanceof Integer) return ((int) recipe.extras().get(1))*NCConfig.generator_update_rate/20;
		return 5*NCConfig.generator_update_rate/20;
	}
	
	public ItemStack getOutput(BlockPos pos) {
		IRecipe recipe = getDecayRecipe(pos);
		if (recipe == null) return ItemStack.EMPTY;
		ArrayList<IIngredient> outputs = recipe.outputs();
		Object output = RecipeMethods.getIngredientFromList(outputs, 0);
		if (output instanceof ItemStack) return (ItemStack) output;
		return ItemStack.EMPTY;
	}
}
