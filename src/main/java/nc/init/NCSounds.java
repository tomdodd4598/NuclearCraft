package nc.init;

import it.unimi.dsi.fastutil.objects.*;
import nc.Global;
import net.minecraft.util.*;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class NCSounds {
	
	public static final ObjectSet<String> TICKABLE_SOUNDS = new ObjectOpenHashSet<>();
	
	public static SoundEvent fusion_run;
	public static SoundEvent turbine_run;
	
	public static SoundEvent geiger_tick;
	public static SoundEvent radaway;
	public static SoundEvent rad_x;
	public static SoundEvent chems_wear_off;
	public static SoundEvent rad_poisoning;
	
	// public static SoundEvent feral_ghoul_ambient;
	// public static SoundEvent feral_ghoul_hurt;
	public static SoundEvent feral_ghoul_death;
	// public static SoundEvent feral_ghoul_fall;
	// public static SoundEvent feral_ghoul_step;
	public static SoundEvent feral_ghoul_charge;
	
	public static SoundEvent wanderer;
	public static SoundEvent end_of_the_world;
	public static SoundEvent money_for_nothing;
	public static SoundEvent hyperspace;
	
	public static void init() {
		fusion_run = register("block.nc.fusion_run", true);
		turbine_run = register("block.nc.turbine_run", true);
		
		geiger_tick = register("player.nc.geiger_tick");
		radaway = register("player.nc.radaway");
		rad_x = register("player.nc.rad_x");
		chems_wear_off = register("player.nc.chems_wear_off");
		rad_poisoning = register("player.nc.rad_poisoning");
		
		// feral_ghoul_ambient = register("entity.nc.feral_ghoul_ambient");
		// feral_ghoul_hurt = register("entity.nc.feral_ghoul_hurt");
		feral_ghoul_death = register("entity.nc.feral_ghoul_death");
		// feral_ghoul_step = register("entity.nc.feral_ghoul_step");
		// feral_ghoul_fall = register("entity.nc.feral_ghoul_fall");
		feral_ghoul_charge = register("entity.nc.feral_ghoul_charge");
		
		wanderer = register("music.nc.wanderer");
		end_of_the_world = register("music.nc.end_of_the_world");
		money_for_nothing = register("music.nc.money_for_nothing");
		hyperspace = register("music.nc.hyperspace");
	}
	
	private static SoundEvent register(String name) {
		return register(name, false);
	}
	
	private static SoundEvent register(String name, boolean tickable) {
		ResourceLocation location = new ResourceLocation(Global.MOD_ID, name);
		if (tickable) {
			TICKABLE_SOUNDS.add(location.toString());
		}
		SoundEvent event = new SoundEvent(location);
		
		ForgeRegistries.SOUND_EVENTS.register(event.setRegistryName(location));
		return event;
	}
}
