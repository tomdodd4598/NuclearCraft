package nc.worldgen.decoration;

import java.util.Random;

import nc.util.NCMath;
import net.minecraft.block.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

public class BushGenerator implements IWorldGenerator {
	
	protected final IBlockState bush;
	protected final int genSize, genRate;
	
	public BushGenerator(IBlockState bush, int genSize, int genRate) {
		this.bush = bush;
		this.genSize = genSize;
		this.genRate = genRate;
	}
	
	@Override
	public void generate(Random rand, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
		if (shouldGenerate() && rand.nextInt(Math.max(1, NCMath.toInt(400D / genRate))) == 0) {
			BlockPos chunkPos = new BlockPos(chunkX * 16 + 8, rand.nextInt(world.getHeight()), chunkZ * 16 + 8);
			if (canGenerate(rand, world, chunkPos)) {
				generateBush(rand, world, chunkPos.add(rand.nextInt(16), 0, rand.nextInt(16)));
			}
		}
	}
	
	public boolean shouldGenerate() {
		return genSize > 0 && genRate > 0;
	}
	
	public boolean canGenerate(Random rand, World world, BlockPos chunkPos) {
		return true;
	}
	
	public void generateBush(Random rand, World world, BlockPos sourcePos) {
		Block block = bush.getBlock();
		for (int i = 0; i < genSize; ++i) {
			int height = MathHelper.clamp(sourcePos.getY() + rand.nextInt(4) - rand.nextInt(4), 1, world.getHeight() - 1);
			BlockPos genPos = new BlockPos(sourcePos.getX() + rand.nextInt(8) - rand.nextInt(8), height, sourcePos.getZ() + rand.nextInt(8) - rand.nextInt(8));
			
			boolean canBlockStay = block instanceof BlockBush ? ((BlockBush) block).canBlockStay(world, genPos, bush) : true;
			
			if (world.isAirBlock(genPos) && (!world.provider.hasSkyLight() || genPos.getY() < world.getHeight() - 1) && canBlockStay) {
				world.setBlockState(genPos, bush, 2);
			}
		}
	}
}
