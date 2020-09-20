package nc.worldgen.decoration;

import static nc.config.NCConfig.mushroom_gen_size;

import java.util.Random;

import net.minecraft.block.BlockBush;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

public class WorldGenBush implements IWorldGenerator {
	
	private final IBlockState bush;
	
	public WorldGenBush(IBlockState bush) {
		this.bush = bush;
	}
	
	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {}
	
	public void generateBush(Random random, World world, BlockPos pos) {
		BlockBush block = (BlockBush) bush.getBlock();
		
		for (int i = 0; i < mushroom_gen_size; ++i) {
			BlockPos blockpos = pos.add(random.nextInt(8) - random.nextInt(8), random.nextInt(4) - random.nextInt(4), random.nextInt(8) - random.nextInt(8));
			
			if (world.isAirBlock(blockpos) && (!world.provider.hasSkyLight() || blockpos.getY() < world.getHeight() - 1) && block.canBlockStay(world, blockpos, bush)) {
				world.setBlockState(blockpos, bush, 2);
			}
		}
	}
}
