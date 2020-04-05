package nc.radiation.environment;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import nc.config.NCConfig;
import nc.tile.radiation.ITileRadiationEnvironment;
import nc.util.FourPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;

public class RadiationEnvironmentHandler {
	
	private static final ConcurrentMap<FourPos, RadiationEnvironmentInfo> ENVIRONMENT = new ConcurrentHashMap<>();
	private static final ConcurrentMap<FourPos, RadiationEnvironmentInfo> ENVIRONMENT_BACKUP = new ConcurrentHashMap<>();
	
	@SubscribeEvent
	public void updateRadiationEnvironment(TickEvent.WorldTickEvent event) {
		if (!NCConfig.radiation_enabled_public) return;
		
		if (event.phase != TickEvent.Phase.END || event.side == Side.CLIENT || !(event.world instanceof WorldServer)) return;
		int dim = event.world.provider.getDimension();
		
		int count = Math.min((1 + NCConfig.radiation_world_chunks_per_tick)/2, ENVIRONMENT.size());
		Iterator<Entry<FourPos, RadiationEnvironmentInfo>> environmentIter = ENVIRONMENT.entrySet().iterator();
		Entry<FourPos, RadiationEnvironmentInfo> environmentEntry;
		
		while (count > 0) {
			count--;
			
			if (environmentIter.hasNext()) {
				environmentEntry = environmentIter.next();
			}
			else break;
			if (environmentEntry == null) break;
			
			FourPos pos = environmentEntry.getKey();
			RadiationEnvironmentInfo info = environmentEntry.getValue();
			
			if (pos.getDimension() == dim) {
				for (Entry<FourPos, ITileRadiationEnvironment> infoEntry : info.tileMap.entrySet()) {
					infoEntry.getValue().handleRadiationEnvironmentInfo(info);
				}
			}
			
			ENVIRONMENT.remove(pos);
			ENVIRONMENT_BACKUP.put(pos, info);
		}
		
		if (ENVIRONMENT.isEmpty()) {
			ENVIRONMENT.putAll(ENVIRONMENT_BACKUP);
			ENVIRONMENT_BACKUP.clear();
		}
	}
	
	public static void addTile(FourPos pos, ITileRadiationEnvironment tile) {
		RadiationEnvironmentInfo newInfo = new RadiationEnvironmentInfo(pos, tile);
		if (!ENVIRONMENT.containsKey(pos)) RadiationEnvironmentHandler.ENVIRONMENT.put(pos, newInfo);
		else RadiationEnvironmentHandler.ENVIRONMENT.get(pos).addToTileMap(tile);
	}
	
	public static void removeTile(ITileRadiationEnvironment tile) {
		Iterator<Entry<FourPos, RadiationEnvironmentInfo>> infoIterator = ENVIRONMENT.entrySet().iterator();
		
		while (infoIterator.hasNext()) {
			Entry<FourPos, RadiationEnvironmentInfo> infoEntry = infoIterator.next();
			if (infoEntry == null || infoEntry.getKey() == null || infoEntry.getValue() == null) {
				infoIterator.remove();
			}
			else if (tile.getFourPos().getDimension() == infoEntry.getKey().getDimension()) {
				infoEntry.getValue().tileMap.remove(tile.getFourPos());
				if (infoEntry.getValue().tileMap.isEmpty()) infoIterator.remove();
			}
		}
		
		Iterator<Entry<FourPos, RadiationEnvironmentInfo>> backupInfoIterator = ENVIRONMENT_BACKUP.entrySet().iterator();
		
		while (backupInfoIterator.hasNext()) {
			Entry<FourPos, RadiationEnvironmentInfo> infoEntry = backupInfoIterator.next();
			if (infoEntry == null || infoEntry.getKey() == null || infoEntry.getValue() == null) {
				backupInfoIterator.remove();
			}
			else if (tile.getFourPos().getDimension() == infoEntry.getKey().getDimension()) {
				infoEntry.getValue().tileMap.remove(tile.getFourPos());
				if (infoEntry.getValue().tileMap.isEmpty()) backupInfoIterator.remove();
			}
		}
	}
}
