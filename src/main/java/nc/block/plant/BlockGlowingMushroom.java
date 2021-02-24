package nc.block.plant;

import java.util.Random;

import nc.init.NCBlocks;
import nc.worldgen.biome.NCBiomes;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;

public class BlockGlowingMushroom extends NCBlockMushroom {
	
	public BlockGlowingMushroom() {
		super();
		setLightLevel(1F);
	}
	
	@Override
	public boolean canGrow(World worldIn, BlockPos pos, IBlockState state, boolean isClient) {
		Biome biome = worldIn.getChunk(pos).getBiome(pos, worldIn.getBiomeProvider());
		return biome != null && (biome == NCBiomes.NUCLEAR_WASTELAND || BiomeDictionary.hasType(biome, Type.NETHER));
	}
	
	@Override
	protected HugeMushroomGenerator getHugeMushroomGenerator(World worldIn, Random rand, BlockPos pos) {
		return new HugeMushroomGenerator(NCBlocks.glowing_mushroom_block, rand.nextBoolean());
	}
}
