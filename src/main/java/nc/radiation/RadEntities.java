package nc.radiation;

import it.unimi.dsi.fastutil.objects.Object2DoubleMap;
import it.unimi.dsi.fastutil.objects.Object2DoubleOpenHashMap;
import nc.config.NCConfig;
import nc.util.RegistryHelper;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.registry.EntityEntry;

public class RadEntities {
	
	public static final Object2DoubleMap<Class<? extends Entity>> MAX_RADS_MAP = new Object2DoubleOpenHashMap<>();
	
	public static void init() {
		for (String entityInfo : NCConfig.max_entity_rads) {
			int scorePos = entityInfo.lastIndexOf('_');
			if (scorePos == -1) continue;
			EntityEntry entityEntry = RegistryHelper.getEntityEntry(entityInfo.substring(0, scorePos));
			if (entityEntry != null) MAX_RADS_MAP.put(entityEntry.getEntityClass(), Double.parseDouble(entityInfo.substring(scorePos + 1)));
		}
	}
}
