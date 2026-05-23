package nc.tile.generator;

import nc.recipe.*;
import nc.tile.dummy.IInterfaceable;
import nc.tile.energy.*;
import nc.tile.internal.energy.EnergyConnection;
import nc.util.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

import static nc.config.NCConfig.machine_update_rate;

public class TileDecayGenerator extends TileEnergy implements ITickable, IInterfaceable {
	
	Random rand = new Random();
	public int tickCount;
	
	protected BasicRecipe[] recipes = new BasicRecipe[6];
	
	protected int generatorCount;
	
	public TileDecayGenerator() {
		super(2L * RecipeStats.getDecayGeneratorMaxPower(), ITileEnergy.energyConnectionAll(EnergyConnection.OUT));
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
		++generatorCount;
		generatorCount %= machine_update_rate;
	}
	
	public int getGenerated() {
		double power = 0D;
		for (EnumFacing side : EnumFacing.VALUES) {
			power += decayGen(side);
		}
		return NCMath.toInt(machine_update_rate * power);
	}
	
	public double getRadiation() {
		double radiation = 0D;
		for (EnumFacing side : EnumFacing.VALUES) {
			BasicRecipe recipe = getDecayRecipe(side);
			if (recipe != null) {
				radiation += recipe.getDecayGeneratorRadiation();
			}
		}
		return machine_update_rate * radiation;
	}
	
	public double decayGen(EnumFacing side) {
		IBlockState result = getRecipeOutput(side);
		if (result == null) {
			return 0D;
		}
		if (rand.nextDouble() * getRecipeLifetime(side) / machine_update_rate < 1D) {
			world.setBlockState(pos.offset(side), result);
			refreshRecipe(side);
		}
		return getRecipePower(side);
	}
	
	@Override
	public void onBlockNeighborChanged(IBlockState state, World worldIn, BlockPos posIn, BlockPos fromPos) {
		super.onBlockNeighborChanged(state, worldIn, posIn, fromPos);
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
		return EnergyHelper.getEUTier(RecipeStats.getDecayGeneratorMaxPower());
	}
	
	// Recipe from BlockPos
	
	public BasicRecipe getDecayRecipe(EnumFacing side) {
		return recipes[side.getIndex()];
	}
	
	public double getRecipeLifetime(EnumFacing side) {
		BasicRecipe recipe = getDecayRecipe(side);
		return recipe == null ? 1200D : recipe.getDecayGeneratorLifetime();
	}
	
	public double getRecipePower(EnumFacing side) {
		BasicRecipe recipe = getDecayRecipe(side);
		return recipe == null ? 0D : recipe.getDecayGeneratorPower();
	}
	
	public IBlockState getRecipeOutput(EnumFacing side) {
		BasicRecipe recipe = getDecayRecipe(side);
		return recipe == null ? null : RecipeHelper.getBlockStateFromProductList(recipe.getItemProducts(), 0);
	}
}
