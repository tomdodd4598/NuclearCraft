package nc.radiation.environment;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

import nc.tile.radiation.IRadiationEnvironmentHandler;
import nc.util.SetList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;
import net.minecraftforge.fml.relauncher.Side;

public class RadiationEnvironmentHandler {
	
	private Random rand = new Random();
	
	private static final List<RadiationEnvironmentInfo> ENVIRONMENT = new SetList<RadiationEnvironmentInfo>();
	private static final List<RadiationEnvironmentInfo> ENVIRONMENT_BACKUP = new SetList<RadiationEnvironmentInfo>();
	
	@SubscribeEvent
	public void updateRadiationEnvironment(WorldTickEvent event) {
		if (event.phase != Phase.START || event.side == Side.CLIENT || !(event.world instanceof WorldServer)) return;
		int count = Math.min(4, ENVIRONMENT.size());
		
		while (count > 0) {
			count--;
			RadiationEnvironmentInfo info = ENVIRONMENT.get(rand.nextInt(ENVIRONMENT.size()));
			for (IRadiationEnvironmentHandler tile : info.tileList) tile.handleRadiationEnvironmentInfo(info);
			ENVIRONMENT.remove(info);
			ENVIRONMENT_BACKUP.add(info);
		}
		
		if (ENVIRONMENT.isEmpty()) {
			ENVIRONMENT.addAll(ENVIRONMENT_BACKUP);
			ENVIRONMENT_BACKUP.clear();
		}
	}
	
	public static void addTile(IRadiationEnvironmentHandler tile, BlockPos pos) {
		RadiationEnvironmentInfo newInfo = new RadiationEnvironmentInfo(pos, tile);
		int index = RadiationEnvironmentHandler.ENVIRONMENT.indexOf(newInfo);
		if (index < 0) RadiationEnvironmentHandler.ENVIRONMENT.add(newInfo);
		else RadiationEnvironmentHandler.ENVIRONMENT.get(index).tileList.add(tile);
	}
	
	public static void removeTile(IRadiationEnvironmentHandler tile) {
		Iterator<RadiationEnvironmentInfo> infoIterator = ENVIRONMENT.iterator();
		
		while (infoIterator.hasNext()) {
			RadiationEnvironmentInfo info = infoIterator.next();
			if (info == null) {
				infoIterator.remove();
			} else {
				info.tileList.remove(tile);
				if (info.tileList.isEmpty()) infoIterator.remove();
			}
		}
		
		Iterator<RadiationEnvironmentInfo> backupInfoIterator = ENVIRONMENT_BACKUP.iterator();
		
		while (backupInfoIterator.hasNext()) {
			RadiationEnvironmentInfo info = backupInfoIterator.next();
			if (info == null) {
				backupInfoIterator.remove();
			} else {
				info.tileList.remove(tile);
				if (info.tileList.isEmpty()) backupInfoIterator.remove();
			}
		}
	}
}
