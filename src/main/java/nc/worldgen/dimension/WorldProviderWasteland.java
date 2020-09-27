package nc.worldgen.dimension;

import nc.worldgen.biome.NCBiomes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.*;
import net.minecraft.world.biome.BiomeProviderSingle;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.IChunkGenerator;

public class WorldProviderWasteland extends WorldProvider {
	
	@Override
	protected void init() {
		hasSkyLight = true;
		biomeProvider = new BiomeProviderSingle(NCBiomes.NUCLEAR_WASTELAND);
	}
	
	@Override
	public DimensionType getDimensionType() {
		return NCWorlds.wastelandDimType;
	}
	
	@Override
	public boolean isSurfaceWorld() {
		return false;
	}
	
	@Override
	public boolean isNether() {
		return false;
	}
	
	@Override
	public boolean canRespawnHere() {
		return false;
	}
	
	@Override
	public double getMovementFactor() {
		return 0.125D;
	}
	
	@Override
	public IChunkGenerator createChunkGenerator() {
		return new ChunkGeneratorWasteland(world);
	}
	
	// Forge Start
	
	@Override
	public boolean canDoLightning(Chunk chunk) {
		return true;
	}
	
	@Override
	public boolean canDoRainSnowIce(Chunk chunk) {
		return false;
	}
	
	@Override
	public boolean canSnowAt(BlockPos pos, boolean checkLight) {
		return false;
	}
}
