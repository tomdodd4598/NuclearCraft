package nc.worldgen.biome;

import java.util.*;

import nc.entity.EntityFeralGhoul;
import nc.init.NCBlocks;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.*;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.feature.*;
import net.minecraftforge.fml.relauncher.*;

public class BiomeNuclearWasteland extends Biome {
	
	public BiomeNuclearWasteland() {
		super(new BiomeProperties("Nuclear Wasteland").setBaseHeight(0.12F).setHeightVariation(0.02F).setTemperature(2F).setWaterColor(0x994C00).setRainDisabled());
		
		topBlock = NCBlocks.wasteland_earth.getDefaultState();
		fillerBlock = Blocks.SAND.getDefaultState();
		
		decorator = createBiomeDecorator();
		
		setSpawnables();
		addFlowers();
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public int getSkyColorByTemp(float currentTemperature) {
		return 0x9DA071;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public int getGrassColorAtPos(BlockPos pos) {
		return getModdedBiomeGrassColor(0x994C00);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public int getFoliageColorAtPos(BlockPos pos) {
		return getModdedBiomeFoliageColor(0x994C00);
	}
	
	/**
	 * Allocate a new BiomeDecorator for this BiomeGenBase
	 */
	@Override
	public BiomeDecorator createBiomeDecorator() {
		BiomeDecorator biomeDecorator = new BiomeDecoratorNuclearWasteland();
		
		biomeDecorator.waterlilyPerChunk = 0;
		biomeDecorator.treesPerChunk = 0;
		biomeDecorator.extraTreeChance = 0F;
		biomeDecorator.flowersPerChunk = 20;
		biomeDecorator.grassPerChunk = 3;
		biomeDecorator.deadBushPerChunk = 2;
		biomeDecorator.mushroomsPerChunk = 0;
		biomeDecorator.reedsPerChunk = 0;
		biomeDecorator.cactiPerChunk = 0;
		biomeDecorator.gravelPatchesPerChunk = 2;
		biomeDecorator.sandPatchesPerChunk = 2;
		biomeDecorator.clayPerChunk = 0;
		biomeDecorator.bigMushroomsPerChunk = 0;
		biomeDecorator.generateFalls = false;
		
		return getModdedBiomeDecorator(biomeDecorator);
	}
	
	@Override
	public WorldGenAbstractTree getRandomTreeFeature(Random rand) {
		return TREE_FEATURE;
	}
	
	@Override
	public WorldGenerator getRandomWorldGenForGrass(Random rand) {
		return new WorldGenTallGrass(BlockTallGrass.EnumType.DEAD_BUSH);
	}
	
	@Override
	public BlockFlower.EnumFlowerType pickRandomFlower(Random rand, BlockPos pos) {
		return BlockFlower.EnumFlowerType.DANDELION;
	}
	
	public List<FlowerEntry> getFlowerList() {
		return flowers;
	}
	
	private void addFlowers() {
		flowers.clear();
		addFlower(NCBlocks.glowing_mushroom.getDefaultState(), 10);
	}
	
	private void setSpawnables() {
		spawnableCreatureList.clear();
		spawnableMonsterList.clear();
		spawnableWaterCreatureList.clear();
		spawnableCaveCreatureList.clear();
		
		spawnableCreatureList.add(new Biome.SpawnListEntry(EntityFeralGhoul.class, Short.MAX_VALUE, 1, 2));
		spawnableMonsterList.add(new Biome.SpawnListEntry(EntityFeralGhoul.class, Short.MAX_VALUE, 1, 2));
	}
	
	@Override
	public void decorate(World worldIn, Random rand, BlockPos pos) {
		decorator.decorate(worldIn, rand, this, pos);
	}
	
	@Override
	public void genTerrainBlocks(World worldIn, Random rand, ChunkPrimer chunkPrimerIn, int x, int z, double noiseVal) {
		int seaLevel = worldIn.getSeaLevel();
		IBlockState topBlockState = topBlock;
		IBlockState fillerBlockState = fillerBlock;
		int heightCount = -1;
		int noise = (int) (noiseVal / 3D + 3D + rand.nextDouble() * 0.25D);
		int chunkPosX = x & 15;
		int chunkPosZ = z & 15;
		BlockPos.MutableBlockPos mutableblockpos = new BlockPos.MutableBlockPos();
		
		for (int posY = 255; posY >= 0; --posY) {
			if (posY <= rand.nextInt(5)) {
				chunkPrimerIn.setBlockState(chunkPosX, posY, chunkPosZ, BEDROCK);
			}
			else {
				IBlockState currentBlockState = chunkPrimerIn.getBlockState(chunkPosX, posY, chunkPosZ);
				
				if (currentBlockState.getMaterial() == Material.AIR) {
					heightCount = -1;
				}
				else if (currentBlockState.getBlock() == Blocks.STONE) {
					if (heightCount == -1) {
						if (noise <= 0) {
							topBlockState = AIR;
							fillerBlockState = STONE;
						}
						else if (posY >= seaLevel - 4 && posY <= seaLevel + 1) {
							topBlockState = topBlock;
							fillerBlockState = fillerBlock;
						}
						
						if (posY < seaLevel && (topBlockState == null || topBlockState.getMaterial() == Material.AIR)) {
							if (getTemperature(mutableblockpos.setPos(x, posY, z)) < 0.15F) {
								topBlockState = ICE;
							}
							else {
								topBlockState = WATER;
							}
						}
						
						heightCount = noise;
						
						if (posY >= seaLevel - 1) {
							chunkPrimerIn.setBlockState(chunkPosX, posY, chunkPosZ, topBlockState);
						}
						else if (posY < seaLevel - 7 - noise) {
							topBlockState = AIR;
							fillerBlockState = STONE;
							chunkPrimerIn.setBlockState(chunkPosX, posY, chunkPosZ, GRAVEL);
						}
						else {
							chunkPrimerIn.setBlockState(chunkPosX, posY, chunkPosZ, fillerBlockState);
						}
					}
					else if (heightCount > 0) {
						--heightCount;
						chunkPrimerIn.setBlockState(chunkPosX, posY, chunkPosZ, fillerBlockState);
						
						if (heightCount == 0 && fillerBlockState == fillerBlock && noise > 1) {
							heightCount = rand.nextInt(4) + Math.max(0, posY - 63);
							fillerBlockState = topBlock;
						}
					}
				}
			}
		}
	}
}
