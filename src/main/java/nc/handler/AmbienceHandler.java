package nc.handler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.*;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.*;

import java.util.Random;

import static nc.config.NCConfig.wasteland_dimension;
import static nc.init.NCSounds.wasteland_ambience;

@SideOnly(Side.CLIENT)
public class AmbienceHandler {
	
	private final Random random = new Random();
	
	private ISound currentSound = null;
	private int soundCooldown = 200;
	
	@SubscribeEvent
	public void onClientTick(TickEvent.ClientTickEvent event) {
		if (event.phase != TickEvent.Phase.END) {
			return;
		}
		
		Minecraft minecraft = Minecraft.getMinecraft();
		
		if (minecraft.world == null || minecraft.player == null || minecraft.player.dimension != wasteland_dimension) {
			if (currentSound != null) {
				minecraft.getSoundHandler().stopSound(currentSound);
				currentSound = null;
			}
			soundCooldown = 200;
			return;
		}
		
		net.minecraft.client.audio.SoundHandler soundHandler = minecraft.getSoundHandler();
		if (currentSound != null) {
			if (soundHandler.isSoundPlaying(currentSound)) {
				return;
			}
			currentSound = null;
		}
		
		if (soundCooldown-- > 0) {
			return;
		}
		
		SoundEvent sound = wasteland_ambience[random.nextInt(wasteland_ambience.length)];
		soundHandler.playSound(currentSound = PositionedSoundRecord.getMusicRecord(sound));
		
		soundCooldown = 200 + random.nextInt(200);
	}
}
