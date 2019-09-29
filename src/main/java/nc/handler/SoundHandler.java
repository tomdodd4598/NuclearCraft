package nc.handler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import nc.init.NCSounds;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.ITickableSound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.Sound;
import net.minecraft.client.audio.SoundEventAccessor;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class SoundHandler {
	
	/* =========================== TE sound handling - thanks to the Mekanism devs for this system! =========================== */
	
	private static final Minecraft MC = Minecraft.getMinecraft();
	private static Long2ObjectMap<ISound> soundMap = new Long2ObjectOpenHashMap<>();
	
	private static void playSound(ISound sound) {
		MC.getSoundHandler().playSound(sound);
	}
	
	public static ISound startTileSound(SoundEvent soundEvent, BlockPos pos, float volume, float pitch) {
		// First, check to see if there's already a sound playing at the desired location
		ISound sound = soundMap.get(pos.toLong());
		if (sound == null || !MC.getSoundHandler().isSoundPlaying(sound)) {
			// No sound playing, start one up - we assume that tile sounds will play until explicitly stopped
			sound = new PositionedSoundRecord(soundEvent.getSoundName(), SoundCategory.BLOCKS, volume, pitch, true, 0, ISound.AttenuationType.LINEAR, pos.getX() + 0.5F, pos.getY() + 0.5F, pos.getZ() + 0.5F) {
				@Override
				public float getVolume() {
					if (this.sound == null) {
						this.createAccessor(MC.getSoundHandler());
					}
					return super.getVolume();
				}
			};
			
			// Start the sound
			playSound(sound);
			
			// N.B. By the time playSound returns, our expectation is that our wrapping-detector handler has fired
			// and dealt with any muting interceptions and, CRITICALLY, updated the soundMap with the final ISound.
			sound = soundMap.get(pos.toLong());
		}
		return sound;
	}
	
	public static void stopTileSound(BlockPos pos) {
		long posKey = pos.toLong();
		ISound sound = soundMap.get(posKey);
		if (sound != null) {
			MC.getSoundHandler().stopSound(sound);
			soundMap.remove(posKey);
		}
	}
	
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void onTilePlaySound(PlaySoundEvent event) {
		// Ignore any sound event which is null or is happening in a muffled check
		ISound resultSound = event.getResultSound();
		if (resultSound == null) {
			return;
		}
		
		// Ignore any sound event outside this mod namespace
		ResourceLocation soundLoc = event.getSound().getSoundLocation();
		if (!NCSounds.TICKABLE_SOUNDS.contains(soundLoc.toString())) {
			return;
		}
		
		// At this point, we've got a known tickable block sound
		resultSound = new TileSound(event.getSound(), resultSound.getVolume());
		event.setResultSound(resultSound);
		
		// Finally, update our soundMap so that we can actually have a shot at stopping this sound; note that we also
		// need to "un-offset" the sound position so that we build the correct key for the sound map
		BlockPos pos = new BlockPos(resultSound.getXPosF() - 0.5F, resultSound.getYPosF() - 0.5F, resultSound.getZPosF() - 0.5F);
		soundMap.put(pos.toLong(), resultSound);
	}
	
	private static class TileSound implements ITickableSound {
		
		private ISound sound;
		private float volume;
		private boolean donePlaying = false;
		
		TileSound(ISound sound, float volume) {
			this.sound = sound;
			this.volume = volume;
		}
		
		@Override
		public void update() {}
		
		@Override
		public boolean isDonePlaying() {
			return donePlaying;
		}
		
		@Override
		public float getVolume() {
			return volume;
		}
		
		@Override
		public @Nonnull ResourceLocation getSoundLocation() {
			return sound.getSoundLocation();
		}
		
		@Override
		public @Nullable SoundEventAccessor createAccessor(net.minecraft.client.audio.SoundHandler handler) {
			return sound.createAccessor(handler);
		}
		
		@Override
		public @Nonnull Sound getSound() {
			return sound.getSound();
		}
		
		@Override
		public @Nonnull SoundCategory getCategory() {
			return sound.getCategory();
		}
		
		@Override
		public boolean canRepeat() {
			return sound.canRepeat();
		}
		
		@Override
		public int getRepeatDelay() {
			return sound.getRepeatDelay();
		}
		
		@Override
		public float getPitch() {
			return sound.getPitch();
		}
		
		@Override
		public float getXPosF() {
			return sound.getXPosF();
		}
		
		@Override
		public float getYPosF() {
			return sound.getYPosF();
		}
		
		@Override
		public float getZPosF() {
			return sound.getZPosF();
		}
		
		@Override
		public @Nonnull AttenuationType getAttenuationType() {
			return sound.getAttenuationType();
		}
	}
	
	public static class SoundInfo {
		
		public ISound sound;
		public BlockPos pos;
		
		public SoundInfo(ISound sound, BlockPos pos) {
			this.sound = sound;
			this.pos = pos;
		}
	}
}
