package nc.multiblock;

import net.minecraft.client.Minecraft;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public final class MultiblockEventHandler {
	
	@SubscribeEvent(priority = EventPriority.NORMAL)
	public void onChunkLoad(final ChunkEvent.Load loadEvent) {
		Chunk chunk = loadEvent.getChunk();
		MultiblockRegistry.INSTANCE.onChunkLoaded(loadEvent.getWorld(), chunk.x, chunk.z);
	}
	
	@SubscribeEvent(priority = EventPriority.NORMAL)
	public void onWorldUnload(final WorldEvent.Unload unloadWorldEvent) {
		MultiblockRegistry.INSTANCE.onWorldUnloaded(unloadWorldEvent.getWorld());
	}
	
	@SubscribeEvent
	public void onWorldTick(final TickEvent.WorldTickEvent event) {
		if (TickEvent.Phase.START == event.phase) {
			MultiblockRegistry.INSTANCE.tickStart(event.world);
		}
	}
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onClientTick(final TickEvent.ClientTickEvent event) {
		if (TickEvent.Phase.START == event.phase) {
			MultiblockRegistry.INSTANCE.tickStart(Minecraft.getMinecraft().world);
		}
	}
}
