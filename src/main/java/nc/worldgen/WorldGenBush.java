package nc.worldgen;

import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class WorldGenBush extends WorldGenerator {
	
	private final IBlockState bushBlock;
	private final IBlockState groundBlock;
	
	public WorldGenBush(IBlockState bush, IBlockState ground) {
		bushBlock = bush;
		groundBlock = ground;
	}
	
	@Override
	public boolean generate(World world, Random rand, BlockPos position) {
		if (world.isAirBlock(position) && world.getBlockState(position.down()) == groundBlock) {
			world.setBlockState(position, bushBlock, 2);
		}
		return true;
	}
}