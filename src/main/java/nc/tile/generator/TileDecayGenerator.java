package nc.tile.generator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import nc.config.NCConfig;
import nc.energy.EnumStorage.EnergyConnection;
import nc.init.NCBlocks;
import nc.tile.dummy.IInterfaceable;
import nc.tile.energy.TileEnergy;
import nc.util.OreStackHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.oredict.OreDictionary;

public class TileDecayGenerator extends TileEnergy implements IInterfaceable {
	
	static final String[] DECAY_BLOCK_NAMES = new String[] {"blockThorium", "blockUranium", "blockDepletedThorium", "blockDepletedUranium", "blockDepletedNeptunium", "blockDepletedPlutonium", "blockDepletedAmericium", "blockDepletedCurium", "blockDepletedBerkelium", "blockDepletedCalifornium"};
	static final IBlockState[] DECAY_PATHS = new IBlockState[] {NCBlocks.block_depleted_thorium.getDefaultState(), NCBlocks.block_depleted_uranium.getDefaultState()};
	Random rand = new Random();
	public int tickCount;
	
	public TileDecayGenerator() {
		super(2*NCConfig.generator_rf_per_eu*maxPower(), EnergyConnection.OUT);
	}
	
	@Override
	public void update() {
		super.update();
		if(!world.isRemote) {
			if (shouldCheck()) storage.changeEnergyStored(getGenerated());
			pushEnergy();
		}
	}
	
	public boolean shouldCheck() {
		if (tickCount > NCConfig.generator_update_rate) tickCount = 0; else tickCount++;
		return tickCount > NCConfig.generator_update_rate;
	}
	
	private static int maxPower() {
		int maxPower = getDecayPower(0);
		for (int i = 1; i < NCConfig.decay_power.length; i++) {
			if (getDecayPower(i) > maxPower) maxPower = getDecayPower(i);
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
	
	@Override
	public int getSourceTier() {
		return 1;
	}
	
	@Override
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
					if (rand.nextInt(36000/20) == 0) world.setBlockState(pos, DECAY_PATHS[i]);
				}
				return getDecayPower(i);
			}
		}
		return 0;
	}
	
	private static int getDecayPower(int i) {
		return (NCConfig.decay_power[i]*NCConfig.generator_update_rate)/20;
	}
}
