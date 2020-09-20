package nc.tile.generator;

import static nc.config.NCConfig.machine_update_rate;

import java.util.*;

import nc.recipe.*;
import nc.tile.dummy.IInterfaceable;
import nc.tile.energy.*;
import nc.tile.internal.energy.EnergyConnection;
import nc.util.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileDecayGenerator extends TileEnergy implements ITickable, IInterfaceable {
	
	Random rand = new Random();
	public int tickCount;
	
	protected ProcessorRecipe[] recipes = new ProcessorRecipe[6];
	
	protected int generatorCount;
	
	public TileDecayGenerator() {
		super(maxPower(), ITileEnergy.energyConnectionAll(EnergyConnection.OUT));
	}
	
	@Override
	public void onLoad() {
		super.onLoad();
		for (EnumFacing side : EnumFacing.VALUES) {
			refreshRecipe(side);
		}
	}
	
	@Override
	public void update() {
		if (!world.isRemote) {
			tickGenerator();
			if (generatorCount == 0) {
				getEnergyStorage().changeEnergyStored(getGenerated());
				getRadiationSource().setRadiationLevel(getRadiation());
			}
			pushEnergy();
		}
	}
	
	public void tickGenerator() {
		generatorCount++;
		generatorCount %= machine_update_rate;
	}
	
	private static int maxPower() {
		double max = 0D;
		List<ProcessorRecipe> recipes = NCRecipes.decay_generator.getRecipeList();
		for (ProcessorRecipe recipe : recipes) {
			if (recipe == null) {
				continue;
			}
			max = Math.max(max, recipe.getDecayPower());
		}
		return (int) (machine_update_rate * max);
	}
	
	public int getGenerated() {
		double power = 0D;
		for (EnumFacing side : EnumFacing.VALUES) {
			power += decayGen(side);
		}
		return (int) (machine_update_rate * power);
	}
	
	public double getRadiation() {
		double radiation = 0D;
		for (EnumFacing side : EnumFacing.VALUES) {
			if (getDecayRecipe(side) != null) {
				radiation += getDecayRecipe(side).getDecayRadiation();
			}
		}
		return machine_update_rate * radiation;
	}
	
	public double decayGen(EnumFacing side) {
		if (getDecayRecipe(side) == null) {
			return 0D;
		}
		ItemStack stack = getOutput(side);
		if (stack == null || stack.isEmpty()) {
			return 0D;
		}
		if (rand.nextDouble() * getRecipeLifetime(side) / machine_update_rate < 1D) {
			IBlockState block = StackHelper.getBlockStateFromStack(stack);
			if (block == null) {
				return 0D;
			}
			getWorld().setBlockState(pos.offset(side), block);
			refreshRecipe(side);
		}
		return getRecipePower(side);
	}
	
	@Override
	public void onBlockNeighborChanged(IBlockState state, World world, BlockPos pos, BlockPos fromPos) {
		super.onBlockNeighborChanged(state, world, pos, fromPos);
		for (EnumFacing side : EnumFacing.VALUES) {
			refreshRecipe(side);
		}
	}
	
	public void refreshRecipe(EnumFacing side) {
		recipes[side.getIndex()] = RecipeHelper.blockRecipe(NCRecipes.decay_generator, world, pos.offset(side));
	}
	
	// IC2
	
	@Override
	public int getSinkTier() {
		return 10;
	}
	
	@Override
	public int getSourceTier() {
		return EnergyHelper.getEUTier(maxPower());
	}
	
	// Recipe from BlockPos
	
	public ProcessorRecipe getDecayRecipe(EnumFacing side) {
		return recipes[side.getIndex()];
	}
	
	public double getRecipeLifetime(EnumFacing side) {
		if (getDecayRecipe(side) == null) {
			return 1200D;
		}
		return getDecayRecipe(side).getDecayLifetime();
	}
	
	public double getRecipePower(EnumFacing side) {
		if (getDecayRecipe(side) == null) {
			return 0D;
		}
		return getDecayRecipe(side).getDecayPower();
	}
	
	public ItemStack getOutput(EnumFacing side) {
		if (getDecayRecipe(side) == null) {
			return ItemStack.EMPTY;
		}
		ItemStack output = RecipeHelper.getItemStackFromIngredientList(getDecayRecipe(side).getItemProducts(), 0);
		return output != null ? output : ItemStack.EMPTY;
	}
}
