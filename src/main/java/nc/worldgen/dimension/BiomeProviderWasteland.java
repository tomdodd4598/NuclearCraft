package nc.worldgen.dimension;

import it.unimi.dsi.fastutil.objects.*;
import nc.worldgen.biome.NCBiomes;
import net.minecraft.init.Biomes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.*;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;
import java.util.*;

public class BiomeProviderWasteland extends BiomeProvider {
	
	public static final List<Pair<Biome, Integer>> BIOME_INFO = Arrays.asList(Pair.of(NCBiomes.NUCLEAR_WASTELAND, 8), Pair.of(Biomes.DESERT, 5), Pair.of(Biomes.SAVANNA, 3), Pair.of(Biomes.FOREST, 2), Pair.of(Biomes.SWAMPLAND, 2), Pair.of(Biomes.OCEAN, 2), Pair.of(Biomes.STONE_BEACH, 1));
	
	private final long seed;
	
	private final ObjectList<Biome> biomes = new ObjectArrayList<>();
	private final ObjectList<Pair<Biome, Integer>> biomeWeightPairs = new ObjectArrayList<>();
	private int totalWeight = 0;
	
	public BiomeProviderWasteland(long seed) {
		this.seed = seed;
		
		for (Pair<Biome, Integer> pair : BIOME_INFO) {
			Biome biome = pair.getLeft();
			int weight = pair.getRight() == null ? 0 : pair.getRight();
			if (biome == null || weight <= 0) {
				continue;
			}
			totalWeight += weight;
			biomeWeightPairs.add(Pair.of(biome, totalWeight));
			if (!biomes.contains(biome)) {
				biomes.add(biome);
			}
		}
		
		if (biomeWeightPairs.isEmpty()) {
			totalWeight = 1;
			biomeWeightPairs.add(Pair.of(NCBiomes.NUCLEAR_WASTELAND, 1));
			biomes.add(NCBiomes.NUCLEAR_WASTELAND);
		}
	}
	
	@Override
	public List<Biome> getBiomesToSpawnIn() {
		return biomes;
	}
	
	@Override
	public Biome getBiome(BlockPos pos) {
		return getBiome(pos, NCBiomes.NUCLEAR_WASTELAND);
	}
	
	@Override
	public Biome getBiome(BlockPos pos, Biome defaultBiome) {
		Biome biome = getBiomeAt(pos.getX(), pos.getZ());
		return biome == null ? defaultBiome : biome;
	}
	
	@Override
	public Biome[] getBiomesForGeneration(@Nullable Biome[] biomes, int x, int z, int width, int height) {
		if (biomes == null || biomes.length < width * height) {
			biomes = new Biome[width * height];
		}
		
		for (int dz = 0; dz < height; ++dz) {
			for (int dx = 0; dx < width; ++dx) {
				biomes[dx + dz * width] = getBiomeAt((x + dx) << 2, (z + dz) << 2);
			}
		}
		
		return biomes;
	}
	
	@Override
	public Biome[] getBiomes(@Nullable Biome[] listToReuse, int x, int z, int width, int length) {
		return getBiomes(listToReuse, x, z, width, length, true);
	}
	
	@Override
	public Biome[] getBiomes(@Nullable Biome[] listToReuse, int x, int z, int width, int length, boolean cacheFlag) {
		if (listToReuse == null || listToReuse.length < width * length) {
			listToReuse = new Biome[width * length];
		}
		
		for (int dz = 0; dz < length; ++dz) {
			for (int dx = 0; dx < width; ++dx) {
				listToReuse[dx + dz * width] = getBiomeAt(x + dx, z + dz);
			}
		}
		
		return listToReuse;
	}
	
	@Override
	public boolean areBiomesViable(int x, int z, int radius, List<Biome> allowed) {
		if (allowed == null || allowed.isEmpty()) {
			return false;
		}
		
		int step = 4;
		int minX = x - radius, minZ = z - radius;
		int maxX = x + radius, maxZ = z + radius;
		
		for (int sampleZ = minZ; sampleZ <= maxZ; sampleZ += step) {
			for (int sampleX = minX; sampleX <= maxX; sampleX += step) {
				if (!allowed.contains(getBiomeAt(sampleX, sampleZ))) {
					return false;
				}
			}
		}
		
		return true;
	}
	
	@Nullable
	@Override
	public BlockPos findBiomePosition(int x, int z, int range, List<Biome> biomes, Random random) {
		if (biomes == null || biomes.isEmpty()) {
			return null;
		}
		
		int step = 4;
		int minX = x - range, minZ = z - range;
		int maxX = x + range, maxZ = z + range;
		BlockPos chosenPos = null;
		int matchCount = 0;
		
		for (int sampleZ = minZ; sampleZ <= maxZ; sampleZ += step) {
			for (int sampleX = minX; sampleX <= maxX; sampleX += step) {
				if (!biomes.contains(getBiomeAt(sampleX, sampleZ))) {
					continue;
				}
				if (chosenPos == null || random.nextInt(++matchCount) == 0) {
					chosenPos = new BlockPos(sampleX, 0, sampleZ);
				}
			}
		}
		
		return chosenPos;
	}
	
	private Biome getBiomeAt(int x, int z) {
		double warpPrimaryX = sampleFractalNoise(x / 64D, z / 64D, seed ^ 0x4CF5AD432745937FL);
		double warpPrimaryZ = sampleFractalNoise(x / 64D, z / 64D, seed ^ 0x9E3779B97F4A7C15L);
		double warpSecondaryX = sampleFractalNoise((x + 4096D) / (64D * 0.5D), (z - 4096D) / (64D * 0.5D), seed ^ 0xC2B2AE3D27D4EB4FL);
		double warpSecondaryZ = sampleFractalNoise((x - 8192D) / (64D * 0.5D), (z + 8192D) / (64D * 0.5D), seed ^ 0x165667B19E3779F9L);
		double warpedX = x + 64D * (warpPrimaryX * 0.72D + warpSecondaryX * 0.28D);
		double warpedZ = z + 64D * (warpPrimaryZ * 0.72D + warpSecondaryZ * 0.28D);
		double rotatedX = (warpedX + warpedZ) * 0.7071067811865476D;
		double rotatedZ = (warpedZ - warpedX) * 0.7071067811865476D;
		
		int regionSize = 128;
		int baseRegionX = (int) Math.floor(rotatedX / regionSize);
		int baseRegionZ = (int) Math.floor(rotatedZ / regionSize);
		Biome nearestBiome = NCBiomes.NUCLEAR_WASTELAND;
		double nearestDistanceSq = Double.MAX_VALUE;
		
		for (int dz = -1; dz <= 1; ++dz) {
			for (int dx = -1; dx <= 1; ++dx) {
				int regionX = baseRegionX + dx;
				int regionZ = baseRegionZ + dz;
				long regionSeed = longHash(seed ^ (long) regionX * 341873128712L ^ (long) regionZ * 132897987541L);
				double offsetX = ((getUniformDouble(regionSeed ^ 0x9E3779B97F4A7C15L) * 2D) - 1D) * 0.5D * regionSize;
				double offsetZ = ((getUniformDouble(regionSeed ^ 0xC2B2AE3D27D4EB4FL) * 2D) - 1D) * 0.5D * regionSize;
				double centerX = (regionX + 0.5D) * regionSize + offsetX;
				double centerZ = (regionZ + 0.5D) * regionSize + offsetZ;
				
				double distanceX = rotatedX - centerX;
				double distanceZ = rotatedZ - centerZ;
				double distanceSq = distanceX * distanceX + distanceZ * distanceZ;
				
				if (distanceSq < nearestDistanceSq) {
					nearestDistanceSq = distanceSq;
					int roll = (int) ((longHash(regionSeed ^ 0x94D049BB133111EBL) & Long.MAX_VALUE) % totalWeight) + 1;
					for (Pair<Biome, Integer> biomeWeightPair : biomeWeightPairs) {
						if (roll <= biomeWeightPair.getRight()) {
							nearestBiome = biomeWeightPair.getLeft();
							break;
						}
					}
				}
			}
		}
		
		return nearestBiome;
	}
	
	private double sampleFractalNoise(double x, double z, long seedOffset) {
		double n0 = sampleValueNoise(x, z, seedOffset);
		double n1 = sampleValueNoise(x * 2D, z * 2D, seedOffset ^ 0x9E3779B97F4A7C15L);
		double n2 = sampleValueNoise(x * 4D, z * 4D, seedOffset ^ 0xC2B2AE3D27D4EB4FL);
		return n0 * 0.6D + n1 * 0.28D + n2 * 0.12D;
	}
	
	private double sampleValueNoise(double x, double z, long seedOffset) {
		int x0 = (int) Math.floor(x);
		int z0 = (int) Math.floor(z);
		double tx = x - x0;
		double tz = z - z0;
		double sx = tx * tx * (3D - 2D * tx);
		double sz = tz * tz * (3D - 2D * tz);
		
		double v00 = getUniformNoise(seed ^ seedOffset, x0, z0);
		double v10 = getUniformNoise(seed ^ seedOffset, x0 + 1, z0);
		double v01 = getUniformNoise(seed ^ seedOffset, x0, z0 + 1);
		double v11 = getUniformNoise(seed ^ seedOffset, x0 + 1, z0 + 1);
		
		double ix0 = v00 + (v10 - v00) * sx;
		double ix1 = v01 + (v11 - v01) * sx;
		return ix0 + (ix1 - ix0) * sz;
	}
	
	private static long longHash(long value) {
		value ^= value >>> 33;
		value *= 0xFF51AFD7ED558CCDL;
		value ^= value >>> 33;
		value *= 0xC4CEB9FE1A85EC53L;
		value ^= value >>> 33;
		return value;
	}
	
	private static double getUniformDouble(long value) {
		return ((longHash(value) >>> 11) & ((1L << 53) - 1)) * 0x1P-53;
	}
	
	private static double getUniformNoise(long seed, int x, int z) {
		long hash = longHash(seed ^ (long) x * 0x9E3779B97F4A7C15L ^ (long) z * 0xC2B2AE3D27D4EB4FL);
		return getUniformDouble(hash) * 2D - 1D;
	}
}
