package nc.worldgen.dimension;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import nc.worldgen.biome.NCBiomes;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldEntitySpawner;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.ChunkGeneratorSettings;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.NoiseGeneratorOctaves;
import net.minecraft.world.gen.NoiseGeneratorPerlin;
import net.minecraft.world.gen.feature.WorldGenDungeons;
import net.minecraft.world.gen.structure.MapGenMineshaft;
import net.minecraft.world.gen.structure.MapGenScatteredFeature;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.terraingen.InitMapGenEvent;
import net.minecraftforge.event.terraingen.InitNoiseGensEvent;
import net.minecraftforge.event.terraingen.PopulateChunkEvent;
import net.minecraftforge.event.terraingen.TerrainGen;
import net.minecraftforge.fluids.FluidRegistry;

public class ChunkGeneratorWasteland implements IChunkGenerator
{
	protected static final IBlockState STONE = Blocks.STONE.getDefaultState();
	protected Biome biome = NCBiomes.NUCLEAR_WASTELAND;
	private final Random rand;
	private NoiseGeneratorOctaves minLimitPerlinNoise;
	private NoiseGeneratorOctaves maxLimitPerlinNoise;
	private NoiseGeneratorOctaves mainPerlinNoise;
	private NoiseGeneratorPerlin surfaceNoise;
	public NoiseGeneratorOctaves scaleNoise;
	public NoiseGeneratorOctaves depthNoise;
	public NoiseGeneratorOctaves forestNoise;
	private final World world;
	private final boolean mapFeaturesEnabled;
	private final WorldType terrainType;
	private final double[] heightMap;
	private final float[] biomeWeights;
	private ChunkGeneratorSettings settings;
	private IBlockState oceanBlock = FluidRegistry.getFluid("corium").getBlock().getDefaultState();
	private double[] depthBuffer = new double[256];
	private MapGenMineshaft mineshaftGenerator = new MapGenMineshaft();
	private MapGenScatteredFeature scatteredFeatureGenerator = new MapGenScatteredFeature();
	double[] mainNoiseRegion;
	double[] minLimitRegion;
	double[] maxLimitRegion;
	double[] depthRegion;

	public ChunkGeneratorWasteland(World worldIn)
	{
		{
			mineshaftGenerator = (MapGenMineshaft)TerrainGen.getModdedMapGen(mineshaftGenerator, InitMapGenEvent.EventType.MINESHAFT);
			scatteredFeatureGenerator = (MapGenScatteredFeature)TerrainGen.getModdedMapGen(scatteredFeatureGenerator, InitMapGenEvent.EventType.SCATTERED_FEATURE);
		}
		this.world = worldIn;
		this.mapFeaturesEnabled = world.getWorldInfo().isMapFeaturesEnabled();
		this.terrainType = worldIn.getWorldInfo().getTerrainType();
		this.rand = new Random(world.getSeed());
		String generatorOptions = world.getWorldInfo().getGeneratorOptions();
		
		this.minLimitPerlinNoise = new NoiseGeneratorOctaves(this.rand, 16);
		this.maxLimitPerlinNoise = new NoiseGeneratorOctaves(this.rand, 16);
		this.mainPerlinNoise = new NoiseGeneratorOctaves(this.rand, 8);
		this.surfaceNoise = new NoiseGeneratorPerlin(this.rand, 4);
		this.scaleNoise = new NoiseGeneratorOctaves(this.rand, 10);
		this.depthNoise = new NoiseGeneratorOctaves(this.rand, 16);
		this.forestNoise = new NoiseGeneratorOctaves(this.rand, 8);
		this.heightMap = new double[825];
		this.biomeWeights = new float[25];

		for (int i = -2; i <= 2; ++i)
		{
			for (int j = -2; j <= 2; ++j)
			{
				float f = 10F / MathHelper.sqrt(i * i + j * j + 0.2F);
				this.biomeWeights[i + 2 + (j + 2) * 5] = f;
			}
		}

		if (generatorOptions != null)
		{
			this.settings = ChunkGeneratorSettings.Factory.jsonToFactory(generatorOptions).build();
			worldIn.setSeaLevel(this.settings.seaLevel);
		}

		InitNoiseGensEvent.ContextOverworld ctx =
				new InitNoiseGensEvent.ContextOverworld(minLimitPerlinNoise, maxLimitPerlinNoise, mainPerlinNoise, surfaceNoise, scaleNoise, depthNoise, forestNoise);
		ctx = TerrainGen.getModdedNoiseGenerators(worldIn, this.rand, ctx);
		this.minLimitPerlinNoise = ctx.getLPerlin1();
		this.maxLimitPerlinNoise = ctx.getLPerlin2();
		this.mainPerlinNoise = ctx.getPerlin();
		this.surfaceNoise = ctx.getHeight();
		this.scaleNoise = ctx.getScale();
		this.depthNoise = ctx.getDepth();
		this.forestNoise = ctx.getForest();
	}

	public void setBlocksInChunk(int x, int z, ChunkPrimer primer)
	{
		this.generateHeightmap(x * 4, 0, z * 4);

		for (int i = 0; i < 4; ++i)
		{
			int j = i * 5;
			int k = (i + 1) * 5;

			for (int l = 0; l < 4; ++l)
			{
				int i1 = (j + l) * 33;
				int j1 = (j + l + 1) * 33;
				int k1 = (k + l) * 33;
				int l1 = (k + l + 1) * 33;

				for (int i2 = 0; i2 < 32; ++i2)
				{
					double d1 = this.heightMap[i1 + i2];
					double d2 = this.heightMap[j1 + i2];
					double d3 = this.heightMap[k1 + i2];
					double d4 = this.heightMap[l1 + i2];
					double d5 = (this.heightMap[i1 + i2 + 1] - d1) * 0.125D;
					double d6 = (this.heightMap[j1 + i2 + 1] - d2) * 0.125D;
					double d7 = (this.heightMap[k1 + i2 + 1] - d3) * 0.125D;
					double d8 = (this.heightMap[l1 + i2 + 1] - d4) * 0.125D;

					for (int j2 = 0; j2 < 8; ++j2)
					{
						double d10 = d1;
						double d11 = d2;
						double d12 = (d3 - d1) * 0.25D;
						double d13 = (d4 - d2) * 0.25D;

						for (int k2 = 0; k2 < 4; ++k2)
						{
							double d16 = (d11 - d10) * 0.25D;
							double lvt_45_1_ = d10 - d16;

							for (int l2 = 0; l2 < 4; ++l2)
							{
								if ((lvt_45_1_ += d16) > 0D)
								{
									primer.setBlockState(i * 4 + k2, i2 * 8 + j2, l * 4 + l2, STONE);
								}
								else if (i2 * 8 + j2 < this.settings.seaLevel)
								{
									primer.setBlockState(i * 4 + k2, i2 * 8 + j2, l * 4 + l2, this.oceanBlock);
								}
							}

							d10 += d12;
							d11 += d13;
						}

						d1 += d5;
						d2 += d6;
						d3 += d7;
						d4 += d8;
					}
				}
			}
		}
	}

	public void replaceBiomeBlocks(int x, int z, ChunkPrimer primer)
	{
		if (!ForgeEventFactory.onReplaceBiomeBlocks(this, x, z, primer, this.world)) return;
		this.depthBuffer = this.surfaceNoise.getRegion(this.depthBuffer, x * 16, z * 16, 16, 16, 0.0625D, 0.0625D, 1D);

		for (int i = 0; i < 16; ++i)
		{
			for (int j = 0; j < 16; ++j)
			{
				biome.genTerrainBlocks(this.world, this.rand, primer, x * 16 + i, z * 16 + j, this.depthBuffer[j + i * 16]);
			}
		}
	}

	/**
	 * Generates the chunk at the specified position, from scratch
	 */
	@Override
	public Chunk generateChunk(int x, int z)
	{
		this.rand.setSeed(x * 341873128712L + z * 132897987541L);
		ChunkPrimer chunkprimer = new ChunkPrimer();
		this.setBlocksInChunk(x, z, chunkprimer);
		this.replaceBiomeBlocks(x, z, chunkprimer);

		if (this.mapFeaturesEnabled)
		{
			if (this.settings.useMineShafts)
			{
				this.mineshaftGenerator.generate(this.world, x, z, chunkprimer);
			}
		}

		Chunk chunk = new Chunk(this.world, chunkprimer, x, z);
		byte[] abyte = chunk.getBiomeArray();

		for (int i = 0; i < abyte.length; ++i)
		{
			abyte[i] = (byte)Biome.getIdForBiome(biome);
		}

		chunk.generateSkylightMap();
		return chunk;
	}

	private void generateHeightmap(int p_185978_1_, int p_185978_2_, int p_185978_3_)
	{
		this.depthRegion = this.depthNoise.generateNoiseOctaves(this.depthRegion, p_185978_1_, p_185978_3_, 5, 5, this.settings.depthNoiseScaleX, this.settings.depthNoiseScaleZ, this.settings.depthNoiseScaleExponent);
		float f = this.settings.coordinateScale;
		float f1 = this.settings.heightScale;
		this.mainNoiseRegion = this.mainPerlinNoise.generateNoiseOctaves(this.mainNoiseRegion, p_185978_1_, p_185978_2_, p_185978_3_, 5, 33, 5, f / this.settings.mainNoiseScaleX, f1 / this.settings.mainNoiseScaleY, f / this.settings.mainNoiseScaleZ);
		this.minLimitRegion = this.minLimitPerlinNoise.generateNoiseOctaves(this.minLimitRegion, p_185978_1_, p_185978_2_, p_185978_3_, 5, 33, 5, f, f1, f);
		this.maxLimitRegion = this.maxLimitPerlinNoise.generateNoiseOctaves(this.maxLimitRegion, p_185978_1_, p_185978_2_, p_185978_3_, 5, 33, 5, f, f1, f);
		int i = 0;
		int j = 0;

		for (int k = 0; k < 5; ++k)
		{
			for (int l = 0; l < 5; ++l)
			{
				float f2 = 0F;
				float f3 = 0F;
				float f4 = 0F;
				for (int j1 = -2; j1 <= 2; ++j1)
				{
					for (int k1 = -2; k1 <= 2; ++k1)
					{
						float f5 = this.settings.biomeDepthOffSet + biome.getBaseHeight() * this.settings.biomeDepthWeight;
						float f6 = this.settings.biomeScaleOffset + biome.getHeightVariation() * this.settings.biomeScaleWeight;

						if (this.terrainType == WorldType.AMPLIFIED && f5 > 0F)
						{
							f5 = 1F + f5 * 2F;
							f6 = 1F + f6 * 4F;
						}

						float f7 = this.biomeWeights[j1 + 2 + (k1 + 2) * 5] / (f5 + 2F);

						f2 += f6 * f7;
						f3 += f5 * f7;
						f4 += f7;
					}
				}

				f2 = f2 / f4;
				f3 = f3 / f4;
				f2 = f2 * 0.9F + 0.1F;
				f3 = (f3 * 4F - 1F) / 8F;
				double d7 = this.depthRegion[j] / 8000D;

				if (d7 < 0D)
				{
					d7 = -d7 * 0.3D;
				}

				d7 = d7 * 3D - 2D;

				if (d7 < 0D)
				{
					d7 = d7 / 2D;

					if (d7 < -1D)
					{
						d7 = -1D;
					}

					d7 = d7 / 1.4D;
					d7 = d7 / 2D;
				}
				else
				{
					if (d7 > 1D)
					{
						d7 = 1D;
					}

					d7 = d7 / 8D;
				}

				++j;
				double d8 = f3;
				double d9 = f2;
				d8 = d8 + d7 * 0.2D;
				d8 = d8 * this.settings.baseSize / 8D;
				double d0 = this.settings.baseSize + d8 * 4D;

				for (int l1 = 0; l1 < 33; ++l1)
				{
					double d1 = (l1 - d0) * this.settings.stretchY * 128D / 256D / d9;

					if (d1 < 0D)
					{
						d1 *= 4D;
					}

					double d2 = this.minLimitRegion[i] / this.settings.lowerLimitScale;
					double d3 = this.maxLimitRegion[i] / this.settings.upperLimitScale;
					double d4 = (this.mainNoiseRegion[i] / 10D + 1D) / 2D;
					double d5 = MathHelper.clampedLerp(d2, d3, d4) - d1;

					if (l1 > 29)
					{
						double d6 = (l1 - 29) / 3F;
						d5 = d5 * (1D - d6) + -10D * d6;
					}

					this.heightMap[i] = d5;
					++i;
				}
			}
		}
	}
	
	@Override
	public void populate(int x, int z)
	{
		BlockFalling.fallInstantly = true;
		int i = x * 16;
		int j = z * 16;
		BlockPos blockpos = new BlockPos(i, 0, j);
		Biome biome = this.world.getBiome(blockpos.add(16, 0, 16));
		this.rand.setSeed(this.world.getSeed());
		long k = this.rand.nextLong() / 2L * 2L + 1L;
		long l = this.rand.nextLong() / 2L * 2L + 1L;
		this.rand.setSeed(x * k + z * l ^ this.world.getSeed());
		boolean flag = false;
		ChunkPos chunkpos = new ChunkPos(x, z);

		ForgeEventFactory.onChunkPopulate(true, this, this.world, this.rand, x, z, flag);

		if (this.mapFeaturesEnabled)
		{
			if (this.settings.useMineShafts)
			{
				this.mineshaftGenerator.generateStructure(this.world, this.rand, chunkpos);
			}
		}

		if (this.settings.useDungeons)
		if (TerrainGen.populate(this, this.world, this.rand, x, z, flag, PopulateChunkEvent.Populate.EventType.DUNGEON))
		{
			for (int j2 = 0; j2 < this.settings.dungeonChance; ++j2)
			{
				int i3 = this.rand.nextInt(16) + 8;
				int l3 = this.rand.nextInt(256);
				int l1 = this.rand.nextInt(16) + 8;
				(new WorldGenDungeons()).generate(this.world, this.rand, blockpos.add(i3, l3, l1));
			}
		}

		biome.decorate(this.world, this.rand, new BlockPos(i, 0, j));
		if (TerrainGen.populate(this, this.world, this.rand, x, z, flag, PopulateChunkEvent.Populate.EventType.ANIMALS))
		WorldEntitySpawner.performWorldGenSpawning(this.world, biome, i + 8, j + 8, 16, 16, this.rand);
		blockpos = blockpos.add(8, 0, 8);

		ForgeEventFactory.onChunkPopulate(false, this, this.world, this.rand, x, z, flag);

		BlockFalling.fallInstantly = false;
	}

	/**
	 * Called to generate additional structures after initial worldgen, used by ocean monuments
	 */
	@Override
	public boolean generateStructures(Chunk chunkIn, int x, int z)
	{
		return false;
	}

	@Override
	public List<Biome.SpawnListEntry> getPossibleCreatures(EnumCreatureType creatureType, BlockPos pos)
	{
		Biome biome = this.world.getBiome(pos);

		if (this.mapFeaturesEnabled)
		{
			if (creatureType == EnumCreatureType.MONSTER && this.scatteredFeatureGenerator.isSwampHut(pos))
			{
				return this.scatteredFeatureGenerator.getMonsters();
			}
		}

		return biome.getSpawnableList(creatureType);
	}

	@Override
	public boolean isInsideStructure(World worldIn, String structureName, BlockPos pos)
	{
		if (!this.mapFeaturesEnabled)
		{
			return false;
		}
		else
		{
			return this.mineshaftGenerator.isInsideStructure(pos) && "Mineshaft".equals(structureName) && this.mineshaftGenerator != null;
		}
	}

	@Override
	@Nullable
	public BlockPos getNearestStructurePos(World worldIn, String structureName, BlockPos position, boolean findUnexplored)
	{
		if (!this.mapFeaturesEnabled)
		{
			return null;
		}
		else
		{
			return "Mineshaft".equals(structureName) && this.mineshaftGenerator != null ? this.mineshaftGenerator.getNearestStructurePos(worldIn, position, findUnexplored) : null;
		}
	}

	/**
	 * Recreates data about structures intersecting given chunk (used for example by getPossibleCreatures), without
	 * placing any blocks. When called for the first time before any chunk is generated - also initializes the internal
	 * state needed by getPossibleCreatures.
	 */
	@Override
	public void recreateStructures(Chunk chunkIn, int x, int z)
	{
		if (this.mapFeaturesEnabled)
		{
			if (this.settings.useMineShafts)
			{
				this.mineshaftGenerator.generate(this.world, x, z, (ChunkPrimer)null);
			}
		}
	}
}