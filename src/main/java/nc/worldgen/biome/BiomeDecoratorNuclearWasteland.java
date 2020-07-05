package nc.worldgen.biome;

import java.util.Random;

import nc.init.NCBlocks;
import nc.worldgen.decoration.WorldGenBush;
import nc.worldgen.ore.UniversalOrePredicate;
import net.minecraft.block.BlockStone;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.*;
import net.minecraft.world.gen.feature.*;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.terraingen.*;
import net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate.EventType;

public class BiomeDecoratorNuclearWasteland extends BiomeDecorator {
	
	private final int dirtSize = 4;
	private final int gravelSize = 26;
	private final int graniteSize = 20;
	private final int dioriteSize = 20;
	private final int andesiteSize = 20;
	private final int coalSize = 10;
	private final int ironSize = 9;
	private final int goldSize = 9;
	private final int redstoneSize = 9;
	private final int diamondSize = 8;
	private final int lapisSize = 8;
	
	private final int dirtCount = 5;
	private final int gravelCount = 10;
	private final int dioriteCount = 7;
	private final int graniteCount = 7;
	private final int andesiteCount = 7;
	private final int coalCount = 10;
	private final int ironCount = 10;
	private final int goldCount = 4;
	private final int redstoneCount = 8;
	private final int diamondCount = 1;
	private final int lapisCount = 1;
	
	private final int lapisCenterHeight = 6;
	private final int lapisSpread = 16;
	
	private final int oreGenMinHeight = 0;
	
	private final int dirtMaxHeight = 30;
	private final int gravelMaxHeight = 90;
	private final int dioriteMaxHeight = 50;
	private final int graniteMaxHeight = 50;
	private final int andesiteMaxHeight = 50;
	private final int coalMaxHeight = 64;
	private final int ironMaxHeight = 64;
	private final int goldMaxHeight = 48;
	private final int redstoneMaxHeight = 16;
	private final int diamondMaxHeight = 16;
	
	protected WorldGenBush bushGen;
	
	public BiomeDecoratorNuclearWasteland() {
		dirtGen = new WorldGenMinable(Blocks.DIRT.getDefaultState(), dirtSize, new UniversalOrePredicate());
		gravelOreGen = new WorldGenMinable(Blocks.GRAVEL.getDefaultState(), gravelSize, new UniversalOrePredicate());
		graniteGen = new WorldGenMinable(Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.GRANITE), graniteSize, new UniversalOrePredicate());
		dioriteGen = new WorldGenMinable(Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.DIORITE), dioriteSize, new UniversalOrePredicate());
		andesiteGen = new WorldGenMinable(Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.ANDESITE), andesiteSize, new UniversalOrePredicate());
		coalGen = new WorldGenMinable(Blocks.COAL_ORE.getDefaultState(), coalSize, new UniversalOrePredicate());
		ironGen = new WorldGenMinable(Blocks.IRON_ORE.getDefaultState(), ironSize, new UniversalOrePredicate());
		goldGen = new WorldGenMinable(Blocks.GOLD_ORE.getDefaultState(), goldSize, new UniversalOrePredicate());
		redstoneGen = new WorldGenMinable(Blocks.REDSTONE_ORE.getDefaultState(), redstoneSize, new UniversalOrePredicate());
		diamondGen = new WorldGenMinable(Blocks.DIAMOND_ORE.getDefaultState(), diamondSize, new UniversalOrePredicate());
		lapisGen = new WorldGenMinable(Blocks.LAPIS_ORE.getDefaultState(), lapisSize, new UniversalOrePredicate());
	}
	
	/**
	 * This is the function where ore generation and things like flowers are generated.
	 */
	@Override
	public void decorate(World worldIn, Random random, Biome biome, BlockPos pos) {
		if (decorating) {
			throw new RuntimeException("Already decorating");
		}
		else {
			chunkPos = pos;
			genDecorations(biome, worldIn, random);
			decorating = false;
		}
	}
	
	/**
	 * This is where things like trees are generated.
	 */
	@Override
	protected void genDecorations(Biome biomeIn, World worldIn, Random random) {
		MinecraftForge.EVENT_BUS.post(new DecorateBiomeEvent.Pre(worldIn, random, chunkPos));
		
		generateOres(worldIn, random);
		
		generate(worldIn, random, chunkPos, DecorateBiomeEvent.Decorate.EventType.SAND_PASS2, gravelGen, gravelPatchesPerChunk);
		generateTrees(worldIn, biomeIn, random, chunkPos);
		generateFlowers(worldIn, biomeIn, random, chunkPos);
		generateGrass(worldIn, biomeIn, random, chunkPos);
		
		if (generateFalls) {
			// generateFalls(worldIn, random, chunkPos); Disabling due to weird crash.
		}
		MinecraftForge.EVENT_BUS.post(new DecorateBiomeEvent.Post(worldIn, random, chunkPos));
	}
	
	private void generateTrees(World worldIn, Biome biomeIn, Random random, BlockPos chunkPos) {
		int treesToGen = treesPerChunk;
		
		if (random.nextFloat() < extraTreeChance) {
			++treesToGen;
		}
		
		if (TerrainGen.decorate(worldIn, random, chunkPos, DecorateBiomeEvent.Decorate.EventType.TREE)) {
			for (int numTreesGenerated = 0; numTreesGenerated < treesToGen; ++numTreesGenerated) {
				int treeX = random.nextInt(16) + 8;
				int treeZ = random.nextInt(16) + 8;
				WorldGenAbstractTree treeGen = biomeIn.getRandomTreeFeature(random);
				treeGen.setDecorationDefaults();
				BlockPos blockpos = worldIn.getHeight(chunkPos.add(treeX, 0, treeZ));
				
				if (treeGen.generate(worldIn, random, blockpos)) {
					treeGen.generateSaplings(worldIn, random, blockpos);
				}
			}
		}
	}
	
	private void generateFlowers(World worldIn, Biome biomeIn, Random random, BlockPos chunkPos) {
		if (TerrainGen.decorate(worldIn, random, chunkPos, DecorateBiomeEvent.Decorate.EventType.FLOWERS)) {
			for (int numFlowersGenerated = 0; numFlowersGenerated < flowersPerChunk; ++numFlowersGenerated) {
				int flowerX = random.nextInt(16) + 8;
				int flowerZ = random.nextInt(16) + 8;
				int yRange = worldIn.getHeight(chunkPos.add(flowerX, 0, flowerZ)).getY() + 32;
				
				bushGen = new WorldGenBush(NCBlocks.glowing_mushroom.getDefaultState());
				
				if (yRange > 0) {
					int flowerY = random.nextInt(yRange);
					BlockPos flowerBlockPos = chunkPos.add(flowerX, flowerY, flowerZ);
					bushGen.generateBush(random, worldIn, flowerBlockPos);
				}
			}
		}
	}
	
	private void generateGrass(World worldIn, Biome biomeIn, Random random, BlockPos chunkPos) {
		if (TerrainGen.decorate(worldIn, random, chunkPos, DecorateBiomeEvent.Decorate.EventType.GRASS)) {
			for (int numGrassPerChunk = 0; numGrassPerChunk < grassPerChunk; ++numGrassPerChunk) {
				int grassX = random.nextInt(16) + 8;
				int grassZ = random.nextInt(16) + 8;
				int yRange = worldIn.getHeight(chunkPos.add(grassX, 0, grassZ)).getY() * 2;
				
				if (yRange > 0) {
					int grassY = random.nextInt(yRange);
					biomeIn.getRandomWorldGenForGrass(random).generate(worldIn, random, chunkPos.add(grassX, grassY, grassZ));
				}
			}
		}
	}
	
	private static void generateFalls(World worldIn, Random random, BlockPos chunkPos) {
		if (TerrainGen.decorate(worldIn, random, chunkPos, DecorateBiomeEvent.Decorate.EventType.LAKE_WATER)) {
			for (int k5 = 0; k5 < 50; ++k5) {
				int i10 = random.nextInt(16) + 8;
				int l13 = random.nextInt(16) + 8;
				int i17 = random.nextInt(248) + 8;
				
				if (i17 > 0) {
					int k19 = random.nextInt(i17);
					BlockPos blockpos6 = chunkPos.add(i10, k19, l13);
					new WorldGenLiquids(Blocks.FLOWING_WATER).generate(worldIn, random, blockpos6);
				}
			}
		}
		
		if (TerrainGen.decorate(worldIn, random, chunkPos, DecorateBiomeEvent.Decorate.EventType.LAKE_LAVA)) {
			for (int l5 = 0; l5 < 20; ++l5) {
				int j10 = random.nextInt(16) + 8;
				int i14 = random.nextInt(16) + 8;
				int j17 = random.nextInt(random.nextInt(random.nextInt(240) + 8) + 8);
				BlockPos blockpos3 = chunkPos.add(j10, j17, i14);
				new WorldGenLiquids(Blocks.FLOWING_LAVA).generate(worldIn, random, blockpos3);
			}
		}
	}
	
	private static void generate(World worldIn, Random random, BlockPos chunkPos, EventType eventType, WorldGenerator generator, int countPerChunk) {
		if (TerrainGen.decorate(worldIn, random, chunkPos, eventType)) {
			for (int count = 0; count < countPerChunk; ++count) {
				int randX = random.nextInt(16) + 8;
				int randZ = random.nextInt(16) + 8;
				generator.generate(worldIn, random, worldIn.getTopSolidOrLiquidBlock(chunkPos.add(randX, 0, randZ)));
			}
		}
	}
	
	/**
	 * Generates ores in the current chunk
	 */
	@Override
	protected void generateOres(World worldIn, Random random) {
		MinecraftForge.ORE_GEN_BUS.post(new OreGenEvent.Pre(worldIn, random, chunkPos));
		if (TerrainGen.generateOre(worldIn, random, dirtGen, chunkPos, OreGenEvent.GenerateMinable.EventType.DIRT)) {
			genStandardOre1(worldIn, random, dirtCount, dirtGen, oreGenMinHeight, dirtMaxHeight);
		}
		if (TerrainGen.generateOre(worldIn, random, gravelOreGen, chunkPos, OreGenEvent.GenerateMinable.EventType.GRAVEL)) {
			genStandardOre1(worldIn, random, gravelCount, gravelOreGen, oreGenMinHeight, gravelMaxHeight);
		}
		if (TerrainGen.generateOre(worldIn, random, dioriteGen, chunkPos, OreGenEvent.GenerateMinable.EventType.DIORITE)) {
			genStandardOre1(worldIn, random, dioriteCount, dioriteGen, oreGenMinHeight, dioriteMaxHeight);
		}
		if (TerrainGen.generateOre(worldIn, random, graniteGen, chunkPos, OreGenEvent.GenerateMinable.EventType.GRANITE)) {
			genStandardOre1(worldIn, random, graniteCount, graniteGen, oreGenMinHeight, graniteMaxHeight);
		}
		if (TerrainGen.generateOre(worldIn, random, andesiteGen, chunkPos, OreGenEvent.GenerateMinable.EventType.ANDESITE)) {
			genStandardOre1(worldIn, random, andesiteCount, andesiteGen, oreGenMinHeight, andesiteMaxHeight);
		}
		if (TerrainGen.generateOre(worldIn, random, coalGen, chunkPos, OreGenEvent.GenerateMinable.EventType.COAL)) {
			genStandardOre1(worldIn, random, coalCount, coalGen, oreGenMinHeight, coalMaxHeight);
		}
		if (TerrainGen.generateOre(worldIn, random, ironGen, chunkPos, OreGenEvent.GenerateMinable.EventType.IRON)) {
			genStandardOre1(worldIn, random, ironCount, ironGen, oreGenMinHeight, ironMaxHeight);
		}
		if (TerrainGen.generateOre(worldIn, random, goldGen, chunkPos, OreGenEvent.GenerateMinable.EventType.GOLD)) {
			genStandardOre1(worldIn, random, goldCount, goldGen, oreGenMinHeight, goldMaxHeight);
		}
		if (TerrainGen.generateOre(worldIn, random, redstoneGen, chunkPos, OreGenEvent.GenerateMinable.EventType.REDSTONE)) {
			genStandardOre1(worldIn, random, redstoneCount, redstoneGen, oreGenMinHeight, redstoneMaxHeight);
		}
		if (TerrainGen.generateOre(worldIn, random, diamondGen, chunkPos, OreGenEvent.GenerateMinable.EventType.DIAMOND)) {
			genStandardOre1(worldIn, random, diamondCount, diamondGen, oreGenMinHeight, diamondMaxHeight);
		}
		if (TerrainGen.generateOre(worldIn, random, lapisGen, chunkPos, OreGenEvent.GenerateMinable.EventType.LAPIS)) {
			genStandardOre2(worldIn, random, lapisCount, lapisGen, lapisCenterHeight, lapisSpread);
		}
		MinecraftForge.ORE_GEN_BUS.post(new OreGenEvent.Post(worldIn, random, chunkPos));
	}
}
