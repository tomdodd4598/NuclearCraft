package nc.worldgen.decoration;

import java.util.Random;

import nc.config.NCConfig;
import nc.init.NCBlocks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.fml.common.IWorldGenerator;

public class BushGenerator implements IWorldGenerator {
	
	private final WorldGenBush glowing_mushroom;
	
	public BushGenerator() {
		glowing_mushroom = new WorldGenBush(NCBlocks.glowing_mushroom.getDefaultState());
	}
	
	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
		generateNether(random, chunkX, chunkZ, world, NCBlocks.glowing_mushroom.getDefaultState());
	}
	
	public void generateNether(Random random, int chunkX, int chunkZ, World world, IBlockState mushroom) {
		if (!NCConfig.mushroom_gen || NCConfig.mushroom_gen_size <= 0 || NCConfig.mushroom_gen_rate <= 0) return;
		
		int genRarity = Math.max(400/NCConfig.mushroom_gen_rate, 1);
		
		int xSpawn, ySpawn, zSpawn;

		int xPos = chunkX * 16 + 8;
		int zPos = chunkZ * 16 + 8;
		BlockPos chunkPos = new BlockPos(xPos, 0, zPos);
		BlockPos position;

		Biome biome = world.getChunkFromBlockCoords(chunkPos).getBiome(chunkPos, world.getBiomeProvider());
		if (biome == null) return;
		
		if (BiomeDictionary.hasType(biome, Type.NETHER)) {
			if (random.nextInt(genRarity) == 0) {
				xSpawn = xPos + random.nextInt(16);
				ySpawn = random.nextInt(128);
				zSpawn = zPos + random.nextInt(16);
				position = new BlockPos(xSpawn, ySpawn, zSpawn);
				
				glowing_mushroom.generateBush(random, world, position);
			}
		}
	}
}
