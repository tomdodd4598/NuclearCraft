package nc.radiation.environment;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import nc.tile.radiation.ITileRadiationEnvironment;
import nc.util.FourPos;

public class RadiationEnvironmentInfo {
	
	public final FourPos pos;
	public final Map<FourPos, ITileRadiationEnvironment> tileMap = new ConcurrentHashMap<FourPos, ITileRadiationEnvironment>();
	
	public RadiationEnvironmentInfo(FourPos pos) {
		this.pos = pos;
	}
	
	public RadiationEnvironmentInfo(FourPos pos, ITileRadiationEnvironment tile) {
		this.pos = pos;
		addToTileMap(tile);
	}
	
	public void addToTileMap(ITileRadiationEnvironment tile) {
		FourPos tilePos = tile.getFourPos();
		int tileDim = tilePos.getDimension();
		if (tileDim != pos.getDimension()) return;
		tileMap.put(tilePos, tile);
	}
}
