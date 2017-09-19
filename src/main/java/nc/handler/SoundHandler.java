package nc.handler;

import nc.Global;
import nc.util.NCUtil;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;

public class SoundHandler {
	
	private static int size = 0;
	
	public static SoundEvent FUSION_RUN;
	public static final int FUSION_RUN_TIME = 67;
	
	public static SoundEvent ACCELERATOR_RUN;
	public static final int ACCELERATOR_RUN_TIME = 67;
	
	public static SoundEvent GEIGER_TICK;
	
	public static SoundEvent WANDERER;
	public static SoundEvent END_OF_THE_WORLD;
	
	public static void init() {
		size = SoundEvent.REGISTRY.getKeys().size();
		
		FUSION_RUN = register("block.fusion_run");
		ACCELERATOR_RUN = register("block.accelerator_run");
		GEIGER_TICK = register("player.geiger_tick");
		WANDERER = register("music.wanderer");
		END_OF_THE_WORLD = register("music.end_of_the_world");
	}
	
	public static SoundEvent register(String name) {
		ResourceLocation location = new ResourceLocation(Global.MOD_ID, name);
		SoundEvent event = new SoundEvent(location);
		
		SoundEvent.REGISTRY.register(size, location, event);
		size++;
		NCUtil.getLogger().info("Registered sound " + name);
		return event;
	}
}
