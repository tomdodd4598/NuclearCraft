package nc.handler;

import nc.Global;
import nc.util.NCUtil;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class SoundHandler {
	
	private static int size = 0;
	
	public static SoundEvent fusion_run;
	public static final int FUSION_RUN_TIME = 67;
	
	public static SoundEvent accelerator_run;
	public static final int ACCELERATOR_RUN_TIME = 67;
	
	public static SoundEvent geiger_tick;
	
	public static SoundEvent wanderer;
	public static SoundEvent end_of_the_world;
	public static SoundEvent money_for_nothing;
	
	public static void init() {
		size = SoundEvent.REGISTRY.getKeys().size();
		
		fusion_run = register("block.fusion_run");
		accelerator_run = register("block.accelerator_run");
		geiger_tick = register("player.geiger_tick");
		wanderer = register("music.wanderer");
		end_of_the_world = register("music.end_of_the_world");
		money_for_nothing = register("music.money_for_nothing");
	}
	
	public static SoundEvent register(String name) {
		ResourceLocation location = new ResourceLocation(Global.MOD_ID, name);
		SoundEvent event = new SoundEvent(location);
		
		ForgeRegistries.SOUND_EVENTS.register(event.setRegistryName(location));
		size++;
		NCUtil.getLogger().info("Registered sound " + name);
		return event;
	}
}
