package nc.worldgen.decoration;

import static nc.config.NCConfig.*;

import java.util.Random;

import nc.worldgen.biome.NCBiomes;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;

public class MushroomGenerator extends BushGenerator {
	
	public MushroomGenerator(IBlockState bush) {
		super(bush, mushroom_gen_size, mushroom_gen_rate);
	}
	
	@Override
	public boolean shouldGenerate() {
		return mushroom_gen && super.shouldGenerate();
	}
	
	@Override
	public boolean canGenerate(Random rand, World world, BlockPos chunkPos) {
		Biome biome = world.getChunk(chunkPos).getBiome(chunkPos, world.getBiomeProvider());
		return biome != null && (biome == NCBiomes.NUCLEAR_WASTELAND || BiomeDictionary.hasType(biome, Type.NETHER));
	}
}
