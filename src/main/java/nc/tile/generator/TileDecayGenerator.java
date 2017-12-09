package nc.tile.generator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import nc.config.NCConfig;
import nc.energy.EnumStorage.EnergyConnection;
import nc.init.NCBlocks;
import nc.tile.energy.TileEnergy;
import nc.util.OreStackHelper;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.oredict.OreDictionary;

public class TileDecayGenerator extends TileEnergy {
	
	static final String[] DECAY_BLOCK_NAMES = new String[] {"blockThorium", "blockUranium", "blockDepletedThorium", "blockDepletedUranium"};
	static final Block[] DECAY_PATHS = new Block[] {NCBlocks.block_depleted_thorium, NCBlocks.block_depleted_uranium};
	Random rand = new Random();
	
	public TileDecayGenerator() {
		super(200*maxPower(), EnergyConnection.OUT);
	}
	
	public void update() {
		super.update();
		if(!world.isRemote) {
			storage.changeEnergyStored(getGenerated());
			pushEnergy();
		}
	}
	
	private static int maxPower() {
		int maxPower = NCConfig.decay_power[0];
		for (int i = 1; i < NCConfig.decay_power.length; i++) {
			if (NCConfig.decay_power[i] > maxPower) maxPower = NCConfig.decay_power[i];
		}
		return maxPower;
	}
	
	public int getGenerated() {
		int power = 0;
		for (EnumFacing side : EnumFacing.VALUES) {
			power += powerFromOreName(getPos().offset(side));
		}
		return power;
	}
	
	public int getSourceTier() {
		return 1;
	}
	
	public int getSinkTier() {
		return 4;
	}
	
	private int powerFromOreName(BlockPos pos) {
		
		List<NonNullList<ItemStack>> types = new ArrayList<NonNullList<ItemStack>>();
		for (int i = 0; i < DECAY_BLOCK_NAMES.length; i++) {
			types.add(i, OreDictionary.getOres(DECAY_BLOCK_NAMES[i]));
		}
		
		ItemStack stack = OreStackHelper.blockToStack(world.getBlockState(pos));
		for (int i = 0; i < types.size(); i++) {
			for (ItemStack oreStack : types.get(i)) if (oreStack.isItemEqual(stack)) {
				if (i < 2) {
					if (rand.nextInt(36000) == 0) world.setBlockState(pos, DECAY_PATHS[i].getDefaultState());
				}
				return NCConfig.decay_power[i];
			}
		}
		
		return 0;
	}
}
