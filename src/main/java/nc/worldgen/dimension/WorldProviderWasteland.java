package nc.worldgen.dimension;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.chunk.Chunk;

public class WorldProviderWasteland extends WorldProvider {

	@Override
	protected void init() {
		hasSkyLight = true;
		biomeProvider = new BiomeProviderWasteland();
	}
	
	@Override
	public DimensionType getDimensionType() {
		return NCWorlds.WASTELAND_DIM_TYPE;
	}
	
	@Override
	public boolean isSurfaceWorld() {
		return false;
	}

	/*======================================= Forge Start =========================================*/

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
