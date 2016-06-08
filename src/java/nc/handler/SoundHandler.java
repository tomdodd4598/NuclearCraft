package nc.handler;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;

public class SoundHandler {
	
	public static void MobSound(String name, World world, Entity entity, float volume, float pitch) {
		world.playSoundAtEntity(entity, "nc:mobs/" + name, (float) volume, (float) pitch);
	}
}
