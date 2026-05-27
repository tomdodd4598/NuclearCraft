package nc.init;

import nc.*;
import nc.entity.EntityFeralGhoul;
import nc.worldgen.biome.NCBiomes;
import net.minecraft.entity.*;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.EntityRegistry;

import static nc.config.NCConfig.*;

public class NCEntities {
	
	public static void register() {
		registerEntity(Global.MOD_ID, "feral_ghoul", EntityFeralGhoul.class, 0, 0x967D73, 0x302C28);
		registerEntity(Global.MOD_ID, "glowing_ghoul", EntityFeralGhoul.Glowing.class, 1, 0xAAC76A, 0x85714A);
		if (register_entity[0]) {
			EntityRegistry.addSpawn(EntityFeralGhoul.class, Short.MAX_VALUE, 1, 1, EnumCreatureType.MONSTER, NCBiomes.NUCLEAR_WASTELAND);
		}
		if (register_entity[1]) {
			EntityRegistry.addSpawn(EntityFeralGhoul.Glowing.class, Short.MAX_VALUE, 1, 1, EnumCreatureType.MONSTER, NCBiomes.NUCLEAR_WASTELAND);
		}
	}
	
	private static void registerEntity(String modId, String name, Class<? extends Entity> clazz, int entityId, int color1, int color2) {
		EntityRegistry.registerModEntity(new ResourceLocation(modId, name), clazz, modId + "." + name, entityId, NuclearCraft.instance, entity_tracking_range, 1, true, color1, color2);
	}
}
