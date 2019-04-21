package nc.radiation.environment;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import nc.config.NCConfig;
import nc.tile.radiation.ITileRadiationEnvironment;
import nc.util.MapHelper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;
import net.minecraftforge.fml.relauncher.Side;

public class RadiationEnvironmentHandler {
	
	private static final Map<BlockPos, RadiationEnvironmentInfo> ENVIRONMENT = new ConcurrentHashMap<BlockPos, RadiationEnvironmentInfo>();
	private static final Map<BlockPos, RadiationEnvironmentInfo> ENVIRONMENT_BACKUP = new ConcurrentHashMap<BlockPos, RadiationEnvironmentInfo>();
	
	@SubscribeEvent
	public void updateRadiationEnvironment(WorldTickEvent event) {
		if (!NCConfig.radiation_enabled_public) return;
		
		if (event.phase != Phase.END || event.side == Side.CLIENT || !(event.world instanceof WorldServer)) return;
		
		int count = Math.min(4, ENVIRONMENT.size());
		
		while (count > 0) {
			count--;
			
			Entry<BlockPos, RadiationEnvironmentInfo> environmentEntry = MapHelper.getNextEntry(ENVIRONMENT);
			if (environmentEntry == null) break;
			
			RadiationEnvironmentInfo info = environmentEntry.getValue();
			for (Entry<BlockPos, ITileRadiationEnvironment> infoEntry : info.tileMap.entrySet()) infoEntry.getValue().handleRadiationEnvironmentInfo(info);
			ENVIRONMENT.remove(environmentEntry.getKey());
			ENVIRONMENT_BACKUP.put(environmentEntry.getKey(), environmentEntry.getValue());
		}
		
		if (ENVIRONMENT.isEmpty()) {
			ENVIRONMENT.putAll(ENVIRONMENT_BACKUP);
			ENVIRONMENT_BACKUP.clear();
		}
	}
	
	public static void addTile(BlockPos pos, ITileRadiationEnvironment tile) {
		RadiationEnvironmentInfo newInfo = new RadiationEnvironmentInfo(pos, tile);
		if (!ENVIRONMENT.containsKey(pos)) RadiationEnvironmentHandler.ENVIRONMENT.put(pos, newInfo);
		else RadiationEnvironmentHandler.ENVIRONMENT.get(pos).tileMap.put(tile.getTilePos(), tile);
	}
	
	public static void removeTile(ITileRadiationEnvironment tile) {
		Iterator<Entry<BlockPos, RadiationEnvironmentInfo>> infoIterator = ENVIRONMENT.entrySet().iterator();
		
		while (infoIterator.hasNext()) {
			Entry<BlockPos, RadiationEnvironmentInfo> infoEntry = infoIterator.next();
			if (infoEntry == null || infoEntry.getValue() == null) infoIterator.remove();
			
			else {
				infoEntry.getValue().tileMap.remove(tile.getTilePos());
				if (infoEntry.getValue().tileMap.isEmpty()) infoIterator.remove();
			}
		}
		
		Iterator<Entry<BlockPos, RadiationEnvironmentInfo>> backupInfoIterator = ENVIRONMENT_BACKUP.entrySet().iterator();
		
		while (backupInfoIterator.hasNext()) {
			Entry<BlockPos, RadiationEnvironmentInfo> infoEntry = backupInfoIterator.next();
			if (infoEntry == null || infoEntry.getValue() == null) backupInfoIterator.remove();
			
			else {
				infoEntry.getValue().tileMap.remove(tile.getTilePos());
				if (infoEntry.getValue().tileMap.isEmpty()) backupInfoIterator.remove();
			}
		}
	}
}
