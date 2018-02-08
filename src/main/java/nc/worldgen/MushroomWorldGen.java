package nc.worldgen;

import java.util.Random;

import nc.block.NCBlockMushroom;
import nc.config.NCConfig;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

public class MushroomWorldGen implements IWorldGenerator {
	
	private final IBlockState mushroom;
	
	public MushroomWorldGen(IBlockState mushroom) {
		this.mushroom = mushroom;
	}
	
	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {}
	
	public void generateMushrooms(Random random, World world, BlockPos pos) {
		NCBlockMushroom block = (NCBlockMushroom) mushroom.getBlock();
		
		for (int i = 0; i < NCConfig.mushroom_gen_size; ++i) {
			BlockPos blockpos = pos.add(random.nextInt(8) - random.nextInt(8), random.nextInt(4) - random.nextInt(4), random.nextInt(8) - random.nextInt(8));
			
			if (world.isAirBlock(blockpos) && (!world.provider.hasSkyLight() || blockpos.getY() < world.getHeight() - 1) && block.canBlockStay(world, blockpos, mushroom)) {
				world.setBlockState(blockpos, mushroom, 2);
			}
		}
	}
}
