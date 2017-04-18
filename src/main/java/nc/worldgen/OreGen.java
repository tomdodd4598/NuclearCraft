package nc.worldgen;

import java.util.Random;

import nc.block.BlockOre;
import nc.config.NCConfig;
import nc.handler.EnumHandler.OreTypes;
import nc.init.NCBlocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

public class OreGen implements IWorldGenerator {
	
	private WorldGenerator copper;
	private WorldGenerator tin;
	private WorldGenerator lead;
	private WorldGenerator thorium;
	private WorldGenerator uranium;
	private WorldGenerator boron;
	private WorldGenerator lithium;
	private WorldGenerator magnesium;
	
	public OreGen() {
		copper = new WorldGenMinable(NCBlocks.ore.getDefaultState().withProperty(BlockOre.TYPE, OreTypes.COPPER), NCConfig.ore_size[0]);
		tin = new WorldGenMinable(NCBlocks.ore.getDefaultState().withProperty(BlockOre.TYPE, OreTypes.TIN), NCConfig.ore_size[1]);
		lead = new WorldGenMinable(NCBlocks.ore.getDefaultState().withProperty(BlockOre.TYPE, OreTypes.LEAD), NCConfig.ore_size[2]);
		thorium = new WorldGenMinable(NCBlocks.ore.getDefaultState().withProperty(BlockOre.TYPE, OreTypes.THORIUM), NCConfig.ore_size[3]);
		uranium = new WorldGenMinable(NCBlocks.ore.getDefaultState().withProperty(BlockOre.TYPE, OreTypes.URANIUM), NCConfig.ore_size[4]);
		boron = new WorldGenMinable(NCBlocks.ore.getDefaultState().withProperty(BlockOre.TYPE, OreTypes.BORON), NCConfig.ore_size[5]);
		lithium = new WorldGenMinable(NCBlocks.ore.getDefaultState().withProperty(BlockOre.TYPE, OreTypes.LITHIUM), NCConfig.ore_size[6]);
		magnesium = new WorldGenMinable(NCBlocks.ore.getDefaultState().withProperty(BlockOre.TYPE, OreTypes.MAGNESIUM), NCConfig.ore_size[7]);
	}

	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
		switch (world.provider.getDimension()) {
		case 0: //Overworld
			if (NCConfig.ore_gen[0]) runGenerator(copper, world, random, chunkX, chunkZ, NCConfig.ore_rate[0], NCConfig.ore_min_height[0], NCConfig.ore_max_height[0]);
			if (NCConfig.ore_gen[1]) runGenerator(tin, world, random, chunkX, chunkZ, NCConfig.ore_rate[1], NCConfig.ore_min_height[1], NCConfig.ore_max_height[1]);
			if (NCConfig.ore_gen[2]) runGenerator(lead, world, random, chunkX, chunkZ, NCConfig.ore_rate[2], NCConfig.ore_min_height[2], NCConfig.ore_max_height[2]);
			if (NCConfig.ore_gen[3]) runGenerator(thorium, world, random, chunkX, chunkZ, NCConfig.ore_rate[3], NCConfig.ore_min_height[3], NCConfig.ore_max_height[3]);
			if (NCConfig.ore_gen[4]) runGenerator(uranium, world, random, chunkX, chunkZ, NCConfig.ore_rate[4], NCConfig.ore_min_height[4], NCConfig.ore_max_height[4]);
			if (NCConfig.ore_gen[5]) runGenerator(boron, world, random, chunkX, chunkZ, NCConfig.ore_rate[5], NCConfig.ore_min_height[5], NCConfig.ore_max_height[5]);
			if (NCConfig.ore_gen[6]) runGenerator(lithium, world, random, chunkX, chunkZ, NCConfig.ore_rate[6], NCConfig.ore_min_height[6], NCConfig.ore_max_height[6]);
			if (NCConfig.ore_gen[7]) runGenerator(magnesium, world, random, chunkX, chunkZ, NCConfig.ore_rate[7], NCConfig.ore_min_height[7], NCConfig.ore_max_height[7]);
		case -1: //Nether
		case 1: //End
		case 6: //Aroma1997 Mining Dimension
			if (NCConfig.ore_gen[0]) runGenerator(copper, world, random, chunkX, chunkZ, NCConfig.ore_rate[0], NCConfig.ore_min_height[0], NCConfig.ore_max_height[0]);
			if (NCConfig.ore_gen[1]) runGenerator(tin, world, random, chunkX, chunkZ, NCConfig.ore_rate[1], NCConfig.ore_min_height[1], NCConfig.ore_max_height[1]);
			if (NCConfig.ore_gen[2]) runGenerator(lead, world, random, chunkX, chunkZ, NCConfig.ore_rate[2], NCConfig.ore_min_height[2], NCConfig.ore_max_height[2]);
			if (NCConfig.ore_gen[3]) runGenerator(thorium, world, random, chunkX, chunkZ, NCConfig.ore_rate[3], NCConfig.ore_min_height[3], NCConfig.ore_max_height[3]);
			if (NCConfig.ore_gen[4]) runGenerator(uranium, world, random, chunkX, chunkZ, NCConfig.ore_rate[4], NCConfig.ore_min_height[4], NCConfig.ore_max_height[4]);
			if (NCConfig.ore_gen[5]) runGenerator(boron, world, random, chunkX, chunkZ, NCConfig.ore_rate[5], NCConfig.ore_min_height[5], NCConfig.ore_max_height[5]);
			if (NCConfig.ore_gen[6]) runGenerator(lithium, world, random, chunkX, chunkZ, NCConfig.ore_rate[6], NCConfig.ore_min_height[6], NCConfig.ore_max_height[6]);
			if (NCConfig.ore_gen[7]) runGenerator(magnesium, world, random, chunkX, chunkZ, NCConfig.ore_rate[7], NCConfig.ore_min_height[7], NCConfig.ore_max_height[7]);
		}
	}
	
	private void runGenerator(WorldGenerator generator, World world, Random rand, int chunk_X, int chunk_Z, int chancesToSpawn, int minHeight, int maxHeight) {
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
}
