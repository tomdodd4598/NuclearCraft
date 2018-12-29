package nc.tile.generator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import nc.config.NCConfig;
import nc.recipe.NCRecipes;
import nc.recipe.ProcessorRecipe;
import nc.recipe.ProcessorRecipeHandler;
import nc.recipe.ingredient.IItemIngredient;
import nc.tile.dummy.IInterfaceable;
import nc.tile.energy.ITileEnergy;
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
	
	public static final double DEFAULT_LIFETIME = 1200D/(double)NCConfig.machine_update_rate;
	public static final int DEFAULT_POWER = (int) (5D*NCConfig.machine_update_rate/20D);
	
	protected int generatorCount;
	
	public TileDecayGenerator() {
		super(maxPower(), ITileEnergy.energyConnectionAll(EnergyConnection.OUT));
		decayGenRecipeType = NCRecipes.Type.DECAY_GENERATOR;
	}
	
	public ProcessorRecipeHandler getRecipeHandler() {
		return decayGenRecipeType.getRecipeHandler();
	}
	
	@Override
	public void update() {
		super.update();
		if(!world.isRemote) {
			tickGenerator();
			if (generatorCount == 0) {
				for (EnumFacing side : EnumFacing.VALUES) {
					List<ItemStack> input = Arrays.asList(ItemStackHelper.blockStateToStack(world.getBlockState(getPos().offset(side))));
					if (recipes[side.getIndex()] == null || !recipes[side.getIndex()].matchingInputs(input, new ArrayList<Tank>())) {
						recipes[side.getIndex()] = getRecipeHandler().getRecipeFromInputs(input, new ArrayList<Tank>());
					}
				}
				getEnergyStorage().changeEnergyStored(getGenerated());
				getRadiationSource().setRadiationLevel(getRadiation());
			}
			pushEnergy();
		}
	}
	
	public void tickGenerator() {
		generatorCount++; generatorCount %= NCConfig.machine_update_rate;
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
		return max*NCConfig.machine_update_rate;
	}
	
	public int getGenerated() {
		int power = 0;
		for (EnumFacing side : EnumFacing.VALUES) {
			power += decayGen(side);
		}
		return power;
	}
	
	public double getRadiation() {
		double radiation = 0D;
		for (EnumFacing side : EnumFacing.VALUES) {
			if (getDecayRecipe(side) != null) radiation += getDecayRecipe(side).getDecayRadiation();
		}
		return radiation;
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
	public int getEUSourceTier() {
		return EnergyHelper.getEUTier(maxPower());
	}
	
	@Override
	public int getEUSinkTier() {
		return 4;
	}
	
	// Recipe from BlockPos
	
	public ProcessorRecipe getDecayRecipe(EnumFacing side) {
		return recipes[side.getIndex()];
	}
	
	public double getRecipeLifetime(EnumFacing side) {
		if (getDecayRecipe(side) == null) return DEFAULT_LIFETIME;
		return getDecayRecipe(side).getDecayLifetime();
	}
	
	public int getRecipePower(EnumFacing side) {
		if (getDecayRecipe(side) == null) return DEFAULT_POWER;
		return getDecayRecipe(side).getDecayPower();
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
