package nc.handler;

import static nc.config.NCConfig.*;

import nc.capability.radiation.entity.IEntityRads;
import nc.init.NCSounds;
import nc.radiation.*;
import net.minecraft.client.util.RecipeItemHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.*;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.fml.common.eventhandler.*;

public class ItemUseHandler {
	
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onFoodEaten(LivingEntityUseItemEvent.Finish event) {
		if (event.isCanceled()) {
			return;
		}
		
		final ItemStack stack = event.getItem();
		if (stack.isEmpty()) {
			return;
		}
		
		final EntityLivingBase entity = event.getEntityLiving();
		if (entity == null) {
			return;
		}
		
		if (radiation_enabled_public && stack.getItem() instanceof ItemFood) {
			int packed = RecipeItemHelper.pack(stack);
			if (!RadSources.FOOD_RAD_MAP.containsKey(packed)) {
				return;
			}
			
			IEntityRads entityRads = RadiationHelper.getEntityRadiation(entity);
			if (entityRads == null) {
				return;
			}
			
			double rads = RadSources.FOOD_RAD_MAP.get(packed);
			double resistance = RadSources.FOOD_RESISTANCE_MAP.get(packed);
			
			if (rads > 0D) {
				entityRads.setPoisonBuffer(entityRads.getPoisonBuffer() + rads);
				entityRads.setRecentPoisonAddition(rads);
				entity.playSound(NCSounds.rad_poisoning, (float) ((1D - Math.pow(10D, -Math.sqrt(rads) / 10D)) * radiation_sound_volumes[3]), 1F);
			}
			else {
				entityRads.setRadawayBuffer(false, entityRads.getRadawayBuffer(false) - rads);
				entityRads.setRecentRadawayAddition(-rads);
				entity.playSound(NCSounds.radaway, (float) ((1D - Math.pow(10D, -Math.sqrt(-rads) / 10D)) * radiation_sound_volumes[3]), 1F);
			}
			
			if (entityRads.getRadXCooldown() <= 0D) {
				entityRads.setInternalRadiationResistance(entityRads.getInternalRadiationResistance() + resistance);
				entityRads.setRecentRadXAddition(Math.abs(resistance));
				if (resistance > 0D) {
					entity.playSound(NCSounds.rad_x, (float) ((1D - Math.pow(10D, -5D * Math.sqrt(resistance) / radiation_rad_x_amount)) * radiation_sound_volumes[3]), 1F);
				}
				else {
					entity.playSound(NCSounds.chems_wear_off, (float) ((1D - Math.pow(10D, -5D * Math.sqrt(-resistance) / radiation_rad_x_amount)) * radiation_sound_volumes[3]), 1F);
				}
			}
		}
	}
}
