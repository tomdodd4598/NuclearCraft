package nc.radiation.environment;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import nc.tile.radiation.ITileRadiationEnvironment;
import net.minecraft.util.math.BlockPos;

public class RadiationEnvironmentInfo {
	
	public final BlockPos pos;
	public final Map<BlockPos, ITileRadiationEnvironment> tileMap = new ConcurrentHashMap<BlockPos, ITileRadiationEnvironment>();
	
	public RadiationEnvironmentInfo(BlockPos pos) {
		this.pos = pos;
	}
	
	public RadiationEnvironmentInfo(BlockPos pos, ITileRadiationEnvironment tile) {
		this.pos = pos;
		tileMap.put(tile.getTilePos(), tile);
	}
}
