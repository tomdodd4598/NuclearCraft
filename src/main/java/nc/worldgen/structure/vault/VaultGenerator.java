package nc.worldgen.structure.vault;

import nc.config.NCConfig;
import nc.worldgen.biome.NCBiomes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.structure.*;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;

import javax.annotation.Nullable;
import java.util.Random;

public class VaultGenerator extends MapGenStructure {
	
	public static final String STRUCTURE_NAME = "NuclearCraftVault";
	
	public static boolean FORCE_GENERATION = false;
	public static int FORCE_CHUNK_X = Integer.MIN_VALUE;
	public static int FORCE_CHUNK_Z = Integer.MIN_VALUE;
	
	@Override
	public String getStructureName() {
		return STRUCTURE_NAME;
	}
	
	@Override
	protected boolean canSpawnStructureAtCoords(int chunkX, int chunkZ) {
		if (FORCE_GENERATION) {
			return chunkX == FORCE_CHUNK_X && chunkZ == FORCE_CHUNK_Z;
		}
		
		if (world == null || world.provider.getDimension() != NCConfig.wasteland_dimension || !world.getWorldInfo().isMapFeaturesEnabled()) {
			return false;
		}
		
		Random seedRandom = world.setRandomSeed(chunkX, chunkZ, 10384598);
		if (world.getPerWorldStorage().getOrLoadData(MapGenStructureData.class, "Village") instanceof MapGenStructureData villageData) {
			NBTTagCompound villageTag = villageData.getTagCompound();
			String key = MapGenStructureData.formatChunkCoords(chunkX, chunkZ);
			if (villageTag.hasKey(key, 10)) {
				NBTTagCompound startTag = villageTag.getCompoundTag(key);
				if (!startTag.hasKey("Valid") || startTag.getBoolean("Valid")) {
					return seedRandom.nextInt(2) == 0;
				}
			}
		}
		
		Biome biome = world.getBiome(new BlockPos((chunkX << 4) + 8, 0, (chunkZ << 4) + 8));
		if (biome == null || !(biome == NCBiomes.NUCLEAR_WASTELAND || BiomeDictionary.hasType(biome, Type.WASTELAND))) {
			return false;
		}
		
		return seedRandom.nextInt(8192) == 0;
	}
	
	@Override
	protected StructureStart getStructureStart(int chunkX, int chunkZ) {
		return new VaultStart(world, rand, chunkX, chunkZ);
	}
	
	@Nullable
	@Override
	public BlockPos getNearestStructurePos(World worldIn, BlockPos pos, boolean findUnexplored) {
		this.world = worldIn;
		return findNearestStructurePosBySpacing(worldIn, this, pos, 10384598, 8, 0x4E435654, false, 100, findUnexplored);
	}
	
	@Nullable
	public StructureBoundingBox forceGenerateAndGetBounds(WorldServer worldIn, int chunkX, int chunkZ) {
		boolean previousForce = FORCE_GENERATION;
		int previousForceChunkX = FORCE_CHUNK_X;
		int previousForceChunkZ = FORCE_CHUNK_Z;
		
		FORCE_GENERATION = true;
		FORCE_CHUNK_X = chunkX;
		FORCE_CHUNK_Z = chunkZ;
		
		try {
			worldIn.getChunkProvider().provideChunk(chunkX, chunkZ);
			generate(worldIn, chunkX, chunkZ, null);
			
			if (structureMap == null) {
				return null;
			}
			
			StructureStart structureStart = structureMap.get(ChunkPos.asLong(chunkX, chunkZ));
			if (structureStart == null || !structureStart.isSizeableStructure()) {
				return null;
			}
			
			StructureBoundingBox boundingBox = structureStart.getBoundingBox();
			if (boundingBox == null) {
				return null;
			}
			
			return new StructureBoundingBox(boundingBox.minX, boundingBox.minY, boundingBox.minZ, boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ);
		}
		finally {
			FORCE_GENERATION = previousForce;
			FORCE_CHUNK_X = previousForceChunkX;
			FORCE_CHUNK_Z = previousForceChunkZ;
		}
	}
}
