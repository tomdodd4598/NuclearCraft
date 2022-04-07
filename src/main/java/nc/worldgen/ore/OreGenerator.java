package nc.worldgen.ore;

import static nc.config.NCConfig.*;

import java.util.Random;

import it.unimi.dsi.fastutil.ints.*;
import nc.block.BlockMeta;
import nc.enumm.MetaEnums;
import nc.init.NCBlocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraftforge.fml.common.IWorldGenerator;

public class OreGenerator implements IWorldGenerator {
	
	protected static IntSet ore_dim_set;
	
	protected final WorldGenOre[] ores;
	
	protected static class WorldGenOre extends WorldGenMinable {
		
		public WorldGenOre(int meta) {
			super(((BlockMeta<?>) NCBlocks.ore).getStateFromMeta(meta), ore_size[meta] + 2, new UniversalOrePredicate());
		}
	}
	
	public OreGenerator() {
		if (ore_dim_set == null) {
			ore_dim_set = new IntOpenHashSet(ore_dims);
		}
		
		ores = new WorldGenOre[8];
		for (int i = 0; i < MetaEnums.OreType.values().length; ++i) {
			ores[i] = new WorldGenOre(i);
		}
	}
	
	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
		if (ore_dim_set.contains(world.provider.getDimension()) != ore_dims_list_type) {
			generateOres(random, chunkX, chunkZ, world, chunkGenerator, chunkProvider);
		}
	}
	
	protected void generateOres(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
		for (int i = 0; i < MetaEnums.OreType.values().length; ++i) {
			if (ore_gen[i]) {
				generateOre(ores[i], world, random, chunkX, chunkZ, ore_rate[i], ore_min_height[i], ore_max_height[i]);
			}
		}
	}
	
	public static void generateOre(WorldGenOre generator, World world, Random rand, int chunk_X, int chunk_Z, int chancesToSpawn, int minHeight, int maxHeight) {
		if (minHeight < 0 || maxHeight >= world.getHeight() || minHeight > maxHeight) {
			throw new IllegalArgumentException("Illegal height arguments!");
		}
		
		int heightDiff = maxHeight - minHeight + 1;
		for (int i = 0; i < chancesToSpawn; ++i) {
			int x = chunk_X * 16 + rand.nextInt(16);
			int y = minHeight + rand.nextInt(heightDiff);
			int z = chunk_Z * 16 + rand.nextInt(16);
			generator.generate(world, rand, new BlockPos(x, y, z));
		}
	}
}
