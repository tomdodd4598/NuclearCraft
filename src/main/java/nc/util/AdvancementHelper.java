package nc.util;

import java.util.Set;
import java.util.UUID;

import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class AdvancementHelper {
	
	// Thanks go to the BuildCraft devs for the methods below!
	
	private static final Set<ResourceLocation> ERRONEOUS_ADVANCEMENTS = new ObjectOpenHashSet<>();

	public static void unlockAdvancement(EntityPlayer player, ResourceLocation advancementName) {
		if (player instanceof EntityPlayerMP) {
			EntityPlayerMP playerMP = (EntityPlayerMP) player;
			AdvancementManager advancementManager = playerMP.getServerWorld().getAdvancementManager();
			Advancement advancement = advancementManager.getAdvancement(advancementName);
			if (advancement != null) {
				playerMP.getAdvancements().grantCriterion(advancement, "code_trigger");
			} else if (ERRONEOUS_ADVANCEMENTS.add(advancementName)) {
				NCUtil.getLogger().warn("Attempted to trigger erroneous NuclearCraft advancement: " + advancementName);
			}
		}
	}

	public static boolean unlockAdvancement(UUID player, ResourceLocation advancementName) {
		Entity entity = FMLCommonHandler.instance().getMinecraftServerInstance().getEntityFromUuid(player);
		if (entity != null && entity instanceof EntityPlayerMP) {
			unlockAdvancement((EntityPlayer) entity, advancementName);
			return true;
		}
		return false;
	}
}
