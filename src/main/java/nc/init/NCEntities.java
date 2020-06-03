package nc.init;

import static nc.config.NCConfig.*;

import nc.*;
import nc.entity.EntityFeralGhoul;
import nc.worldgen.biome.NCBiomes;
import net.minecraft.entity.*;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.EntityRegistry;

public class NCEntities {
	
	public static void register() {
		registerEntity("feral_ghoul", EntityFeralGhoul.class, 0, 0x967D73, 0x302C28);
		if (register_entity[0]) {
			EntityRegistry.addSpawn(EntityFeralGhoul.class, Short.MAX_VALUE, 1, 1, EnumCreatureType.MONSTER, NCBiomes.NUCLEAR_WASTELAND);
		}
	}
	
	private static void registerEntity(String name, Class<? extends Entity> clazz, int entityId, int color1, int color2) {
		EntityRegistry.registerModEntity(new ResourceLocation(Global.MOD_ID, name), clazz, Global.MOD_ID + "." + name, entityId, NuclearCraft.instance, entity_tracking_range, 1, true, color1, color2);
	}
}
