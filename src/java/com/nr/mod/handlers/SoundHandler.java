package com.nr.mod.handlers;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;

public class SoundHandler {
	
	public static void MobSound(String name, World world, Entity entity, float volume, float pitch) {
		world.playSoundAtEntity(entity, "nr:mobs/" + name, (float) volume, (float) pitch);
	}

}
