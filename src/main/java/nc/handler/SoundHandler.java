package nc.handler;

import nc.Global;
import nc.util.NCUtil;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;

public class SoundHandler {
	
	private static int size = 0;
	
	public static SoundEvent FUSION_RUN;
	public static final int FUSION_RUN_TIME = 66;
	
	public static SoundEvent ACCELERATOR_RUN;
	public static final int ACCELERATOR_RUN_TIME = 66;
	
	public static void init() {
		size = SoundEvent.REGISTRY.getKeys().size();
		
		FUSION_RUN = register("block.fusion_run");
		ACCELERATOR_RUN = register("block.accelerator_run");
	}
	
	public static SoundEvent register(String name) {
		ResourceLocation location = new ResourceLocation(Global.MOD_ID, "sound." + name);
		SoundEvent event = new SoundEvent(location);
		
		SoundEvent.REGISTRY.register(size, location, event);
		size++;
		NCUtil.getLogger().info("Registered sound " + name);
		return event;
	}
}
