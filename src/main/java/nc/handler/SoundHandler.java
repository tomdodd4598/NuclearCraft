package nc.handler;

import nc.Global;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class SoundHandler {
	
	public static SoundEvent fusion_run;
	public static final int FUSION_RUN_TIME = 67;
	
	public static SoundEvent accelerator_run;
	public static final int ACCELERATOR_RUN_TIME = 67;
	
	public static SoundEvent geiger_tick;
	public static SoundEvent radaway;
	public static SoundEvent rad_x;
	public static SoundEvent chems_wear_off;
	public static SoundEvent rad_poisoning;
	
	//public static SoundEvent feral_ghoul_ambient;
	//public static SoundEvent feral_ghoul_hurt;
	public static SoundEvent feral_ghoul_death;
	//public static SoundEvent feral_ghoul_step;
	//public static SoundEvent feral_ghoul_fall;
	public static SoundEvent feral_ghoul_charge;
	
	public static SoundEvent wanderer;
	public static SoundEvent end_of_the_world;
	public static SoundEvent money_for_nothing;
	public static SoundEvent hyperspace;
	
	public static void init() {
		fusion_run = register("block.fusion_run");
		accelerator_run = register("block.accelerator_run");
		
		geiger_tick = register("player.geiger_tick");
		radaway = register("player.radaway");
		rad_x = register("player.rad_x");
		chems_wear_off = register("player.chems_wear_off");
		rad_poisoning = register("player.rad_poisoning");
		
		//feral_ghoul_ambient = register("entity.feral_ghoul_ambient");
		//feral_ghoul_hurt = register("entity.feral_ghoul_hurt");
		feral_ghoul_death = register("entity.feral_ghoul_death");
		//feral_ghoul_step = register("entity.feral_ghoul_step");
		//feral_ghoul_fall = register("entity.feral_ghoul_fall");
		feral_ghoul_charge = register("entity.feral_ghoul_charge");
		
		wanderer = register("music.wanderer");
		end_of_the_world = register("music.end_of_the_world");
		money_for_nothing = register("music.money_for_nothing");
		hyperspace = register("music.hyperspace");
	}
	
	public static SoundEvent register(String name) {
		ResourceLocation location = new ResourceLocation(Global.MOD_ID, name);
		SoundEvent event = new SoundEvent(location);
		
		ForgeRegistries.SOUND_EVENTS.register(event.setRegistryName(location));
		return event;
	}
}
