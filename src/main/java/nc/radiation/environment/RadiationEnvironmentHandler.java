package nc.radiation.environment;

import nc.tile.radiation.ITileRadiationEnvironment;
import nc.util.FourPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;

import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.*;

import static nc.config.NCConfig.*;

public class RadiationEnvironmentHandler {
	
	private static final ConcurrentMap<FourPos, RadiationEnvironmentInfo> ENVIRONMENT_MAP = new ConcurrentHashMap<>();
	
	private static final ConcurrentMap<Integer, ConcurrentLinkedQueue<FourPos>> ENVIRONMENT_POS_QUEUE_MAP = new ConcurrentHashMap<>();
	private static final Set<FourPos> ENVIRONMENT_POS_SET = ConcurrentHashMap.newKeySet();
	
	private static final ConcurrentMap<FourPos, FourPos> TILE_TO_ENVIRONMENT_POS_MAP = new ConcurrentHashMap<>();
	
	@SubscribeEvent
	public void updateRadiationEnvironment(TickEvent.WorldTickEvent event) {
		if (!radiation_enabled_public) {
			return;
		}
		
		if (event.phase != TickEvent.Phase.END || event.side == Side.CLIENT || !(event.world instanceof WorldServer world)) {
			return;
		}
		
		ConcurrentLinkedQueue<FourPos> environmentPosQueue = ENVIRONMENT_POS_QUEUE_MAP.get(world.provider.getDimension());
		if (environmentPosQueue == null) {
			return;
		}
		
		int count = Math.max(0, (1 + radiation_world_chunks_per_tick) / 2);
		while (count > 0) {
			--count;
			
			FourPos environmentPos = environmentPosQueue.poll();
			if (environmentPos == null) {
				break;
			}
			ENVIRONMENT_POS_SET.remove(environmentPos);
			
			RadiationEnvironmentInfo info = ENVIRONMENT_MAP.get(environmentPos);
			if (info == null) {
				continue;
			}
			
			if (info.tileMap.isEmpty()) {
				ENVIRONMENT_MAP.remove(environmentPos, info);
				continue;
			}
			
			for (Entry<FourPos, ITileRadiationEnvironment> infoEntry : info.tileMap.entrySet()) {
				infoEntry.getValue().handleRadiationEnvironmentInfo(info);
			}
			
			if (ENVIRONMENT_MAP.get(environmentPos) == info && !info.tileMap.isEmpty() && ENVIRONMENT_POS_SET.add(environmentPos)) {
				environmentPosQueue.offer(environmentPos);
			}
		}
	}
	
	public static void addTile(FourPos pos, ITileRadiationEnvironment tile) {
		FourPos tilePos = tile.getFourPos();
		FourPos prevEnvironmentPos = TILE_TO_ENVIRONMENT_POS_MAP.put(tilePos, pos);
		if (prevEnvironmentPos != null && !prevEnvironmentPos.equals(pos)) {
			RadiationEnvironmentInfo prevInfo = ENVIRONMENT_MAP.get(prevEnvironmentPos);
			if (prevInfo != null) {
				prevInfo.tileMap.remove(tilePos);
				if (prevInfo.tileMap.isEmpty()) {
					ENVIRONMENT_MAP.remove(prevEnvironmentPos, prevInfo);
				}
			}
		}
		
		ENVIRONMENT_MAP.compute(pos, (environmentPos, info) -> {
			if (info == null) {
				return new RadiationEnvironmentInfo(environmentPos, tile);
			}
			info.addToTileMap(tile);
			return info;
		});
		
		if (ENVIRONMENT_POS_SET.add(pos)) {
			ENVIRONMENT_POS_QUEUE_MAP.computeIfAbsent(pos.getDimension(), key -> new ConcurrentLinkedQueue<>()).offer(pos);
		}
	}
	
	public static void removeTile(ITileRadiationEnvironment tile) {
		FourPos tilePos = tile.getFourPos();
		FourPos environmentPos = TILE_TO_ENVIRONMENT_POS_MAP.remove(tilePos);
		if (environmentPos != null) {
			RadiationEnvironmentInfo info = ENVIRONMENT_MAP.get(environmentPos);
			if (info != null) {
				if (info.tileMap.remove(tilePos) != null) {
					if (info.tileMap.isEmpty()) {
						ENVIRONMENT_MAP.remove(environmentPos, info);
					}
					return;
				}
				if (info.tileMap.isEmpty()) {
					ENVIRONMENT_MAP.remove(environmentPos, info);
				}
			}
		}
		
		int dim = tilePos.getDimension();
		for (Entry<FourPos, RadiationEnvironmentInfo> infoEntry : ENVIRONMENT_MAP.entrySet()) {
			FourPos pos = infoEntry.getKey();
			RadiationEnvironmentInfo info = infoEntry.getValue();
			if (pos.getDimension() == dim) {
				info.tileMap.remove(tilePos);
				if (info.tileMap.isEmpty()) {
					ENVIRONMENT_MAP.remove(pos, info);
				}
			}
		}
	}
}
