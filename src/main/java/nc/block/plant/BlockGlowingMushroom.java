package nc.block.plant;

import java.util.Random;

import nc.init.NCBlocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockGlowingMushroom extends NCBlockMushroom {
	
	public BlockGlowingMushroom() {
		super();
		setLightLevel(1F);
	}
	
	@Override
	protected boolean canGrowHuge() {
		return true;
	}
	
	@Override
	protected HugeMushroomGenerator getHugeMushroomGenerator(World worldIn, Random rand, BlockPos pos) {
		return new HugeMushroomGenerator(NCBlocks.glowing_mushroom_block, rand.nextBoolean());
	}
}
