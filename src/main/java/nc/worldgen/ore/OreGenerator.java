package nc.worldgen.ore;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import nc.block.BlockMeta;
import nc.config.NCConfig;
import nc.init.NCBlocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

public class OreGenerator implements IWorldGenerator {
	
	private final WorldGenerator copper;
	private final WorldGenerator tin;
	private final WorldGenerator lead;
	private final WorldGenerator thorium;
	private final WorldGenerator uranium;
	private final WorldGenerator boron;
	private final WorldGenerator lithium;
	private final WorldGenerator magnesium;
	
	public OreGenerator() {
		copper = new WorldGenMinable(((BlockMeta)NCBlocks.ore).getStateFromMeta(0), NCConfig.ore_size[0], new UniversalOrePredicate());
		tin = new WorldGenMinable(((BlockMeta)NCBlocks.ore).getStateFromMeta(1), NCConfig.ore_size[1], new UniversalOrePredicate());
		lead = new WorldGenMinable(((BlockMeta)NCBlocks.ore).getStateFromMeta(2), NCConfig.ore_size[2], new UniversalOrePredicate());
		thorium = new WorldGenMinable(((BlockMeta)NCBlocks.ore).getStateFromMeta(3), NCConfig.ore_size[3], new UniversalOrePredicate());
		uranium = new WorldGenMinable(((BlockMeta)NCBlocks.ore).getStateFromMeta(4), NCConfig.ore_size[4], new UniversalOrePredicate());
		boron = new WorldGenMinable(((BlockMeta)NCBlocks.ore).getStateFromMeta(5), NCConfig.ore_size[5], new UniversalOrePredicate());
		lithium = new WorldGenMinable(((BlockMeta)NCBlocks.ore).getStateFromMeta(6), NCConfig.ore_size[6], new UniversalOrePredicate());
		magnesium = new WorldGenMinable(((BlockMeta)NCBlocks.ore).getStateFromMeta(7), NCConfig.ore_size[7], new UniversalOrePredicate());
	}

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
		List<Integer> dimList = new ArrayList<Integer>();
		for (int i = 0; i < NCConfig.ore_dims.length; i ++) dimList.add(NCConfig.ore_dims[i]);
		if (dimList.contains(world.provider.getDimension()) != NCConfig.ore_dims_list_type) generateOres(random, chunkX, chunkZ, world, chunkGenerator, chunkProvider);
	}
	
	private void generateOre(WorldGenerator generator, World world, Random rand, int chunk_X, int chunk_Z, int chancesToSpawn, int minHeight, int maxHeight) {
		if (minHeight < 0 || maxHeight > 256 || minHeight > maxHeight)
			throw new IllegalArgumentException("Illegal height arguments for WorldGenerator!");
		
		int heightDiff = maxHeight - minHeight + 1;
		for (int i = 0; i < chancesToSpawn; i ++) {
			int x = chunk_X * 16 + rand.nextInt(16);
			int y = minHeight + rand.nextInt(heightDiff);
			int z = chunk_Z * 16 + rand.nextInt(16);
			generator.generate(world, rand, new BlockPos(x, y, z));
		}
	}
	
	private void generateOres(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
		if (NCConfig.ore_gen[0]) generateOre(copper, world, random, chunkX, chunkZ, NCConfig.ore_rate[0], NCConfig.ore_min_height[0], NCConfig.ore_max_height[0]);
		if (NCConfig.ore_gen[1]) generateOre(tin, world, random, chunkX, chunkZ, NCConfig.ore_rate[1], NCConfig.ore_min_height[1], NCConfig.ore_max_height[1]);
		if (NCConfig.ore_gen[2]) generateOre(lead, world, random, chunkX, chunkZ, NCConfig.ore_rate[2], NCConfig.ore_min_height[2], NCConfig.ore_max_height[2]);
		if (NCConfig.ore_gen[3]) generateOre(thorium, world, random, chunkX, chunkZ, NCConfig.ore_rate[3], NCConfig.ore_min_height[3], NCConfig.ore_max_height[3]);
		if (NCConfig.ore_gen[4]) generateOre(uranium, world, random, chunkX, chunkZ, NCConfig.ore_rate[4], NCConfig.ore_min_height[4], NCConfig.ore_max_height[4]);
		if (NCConfig.ore_gen[5]) generateOre(boron, world, random, chunkX, chunkZ, NCConfig.ore_rate[5], NCConfig.ore_min_height[5], NCConfig.ore_max_height[5]);
		if (NCConfig.ore_gen[6]) generateOre(lithium, world, random, chunkX, chunkZ, NCConfig.ore_rate[6], NCConfig.ore_min_height[6], NCConfig.ore_max_height[6]);
		if (NCConfig.ore_gen[7]) generateOre(magnesium, world, random, chunkX, chunkZ, NCConfig.ore_rate[7], NCConfig.ore_min_height[7], NCConfig.ore_max_height[7]);
	}
	
	public static boolean showOre(int i) {
		return NCConfig.ore_gen[i] || !NCConfig.hide_disabled_ores;
	}
}
