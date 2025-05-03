package nc.worldgen.biome;

import nc.util.NCMath;
import net.minecraft.block.BlockSand;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.ChunkPrimer;

import java.util.Random;

public class NCBiome extends Biome {
	
	public IBlockState airBlock = AIR;
	public IBlockState bedrockBlock = BEDROCK;
	public IBlockState stoneBlock = STONE;
	public IBlockState gravelBlock = GRAVEL;
	public IBlockState waterBlock = WATER;
	public IBlockState frozenBlock = ICE;
	
	public NCBiome(BiomeProperties properties) {
		super(properties);
	}
	
	@Override
	public void genTerrainBlocks(World worldIn, Random rand, ChunkPrimer chunkPrimerIn, int x, int z, double noiseVal) {
		generateTerrain(worldIn, rand, chunkPrimerIn, x, z, noiseVal);
	}
	
	public void generateTerrain(World worldIn, Random rand, ChunkPrimer chunkPrimerIn, int x, int z, double noiseVal) {
		int seaLevel = worldIn.getSeaLevel();
		IBlockState topBlockState = topBlock;
		IBlockState fillerBlockState = fillerBlock;
		int heightCount = -1;
		int noise = NCMath.toInt(noiseVal / 3D + 3D + rand.nextDouble() * 0.25D);
		int chunkPosX = x & 15;
		int chunkPosZ = z & 15;
		BlockPos.MutableBlockPos mutableblockpos = new BlockPos.MutableBlockPos();
		
		for (int posY = 255; posY >= 0; --posY) {
			if (posY <= rand.nextInt(5)) {
				chunkPrimerIn.setBlockState(chunkPosX, posY, chunkPosZ, bedrockBlock);
			}
			else {
				IBlockState currentBlockState = chunkPrimerIn.getBlockState(chunkPosX, posY, chunkPosZ);
				
				if (currentBlockState.getMaterial().equals(Material.AIR)) {
					heightCount = -1;
				}
				else if (currentBlockState.getBlock() == stoneBlock.getBlock()) {
					if (heightCount == -1) {
						if (noise <= 0) {
							topBlockState = airBlock;
							fillerBlockState = stoneBlock;
						}
						else if (posY >= seaLevel - 4 && posY <= seaLevel + 1) {
							topBlockState = topBlock;
							fillerBlockState = fillerBlock;
						}
						
						if (posY < seaLevel && (topBlockState == null || topBlockState.getMaterial().equals(Material.AIR))) {
							if (getTemperature(mutableblockpos.setPos(x, posY, z)) < 0.15F) {
								topBlockState = frozenBlock;
							}
							else {
								topBlockState = waterBlock;
							}
						}
						
						heightCount = noise;
						
						if (posY >= seaLevel - 1) {
							chunkPrimerIn.setBlockState(chunkPosX, posY, chunkPosZ, topBlockState);
						}
						else if (posY < seaLevel - 7 - noise) {
							topBlockState = airBlock;
							fillerBlockState = stoneBlock;
							chunkPrimerIn.setBlockState(chunkPosX, posY, chunkPosZ, gravelBlock);
						}
						else {
							chunkPrimerIn.setBlockState(chunkPosX, posY, chunkPosZ, fillerBlockState);
						}
					}
					else if (heightCount > 0) {
						--heightCount;
						chunkPrimerIn.setBlockState(chunkPosX, posY, chunkPosZ, fillerBlockState);
						
						if (heightCount == 0 && fillerBlockState == Blocks.SAND && noise > 1) {
							heightCount = rand.nextInt(4) + Math.max(0, posY - 63);
							fillerBlockState = getSandstoneTerrainBlock(chunkPosX, posY, chunkPosZ, fillerBlockState);
						}
					}
				}
			}
		}
	}
	
	public IBlockState getSandstoneTerrainBlock(int chunkPosX, int posY, int chunkPosZ, IBlockState sandState) {
		return sandState.getValue(BlockSand.VARIANT) == BlockSand.EnumType.RED_SAND ? RED_SANDSTONE : SANDSTONE;
	}
}
