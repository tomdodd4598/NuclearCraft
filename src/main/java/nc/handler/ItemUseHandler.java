package nc.handler;

import nc.capability.radiation.entity.IEntityRads;
import nc.config.NCConfig;
import nc.init.NCSounds;
import nc.radiation.RadSources;
import nc.radiation.RadiationHelper;
import net.minecraft.client.util.RecipeItemHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ItemUseHandler {
	
	@SubscribeEvent
	public void onFoodEaten(LivingEntityUseItemEvent.Finish event) {
		final ItemStack stack = event.getItem();
		if (stack.isEmpty()) return;
		
		final EntityLivingBase entity = event.getEntityLiving();
		if (entity == null) return;
		
		if (NCConfig.radiation_enabled_public && stack.getItem() instanceof ItemFood) {
			int packed = RecipeItemHelper.pack(stack);
			if (!RadSources.FOOD_RAD_MAP.containsKey(packed)) return;
			
			IEntityRads entityRads = RadiationHelper.getEntityRadiation(entity);
			if (entityRads == null) return;
			
			double rads = RadSources.FOOD_RAD_MAP.get(packed);
			double resistance = RadSources.FOOD_RESISTANCE_MAP.get(packed);
			
			if (rads > 0D) {
				entityRads.setPoisonBuffer(entityRads.getPoisonBuffer() + rads);
				entityRads.setRecentPoisonAddition(rads);
				entity.playSound(NCSounds.rad_poisoning, 1F - (float)Math.pow(10D, -Math.sqrt(rads)/10D), 1F);
			}
			else {
				entityRads.setRadawayBuffer(false, entityRads.getRadawayBuffer(false) - rads);
				entityRads.setRecentRadawayAddition(-rads);
				entity.playSound(NCSounds.radaway, 1F - (float)Math.pow(10D, -Math.sqrt(-rads)/10D), 1F);
			}
			
			if (entityRads.getRadXCooldown() <= 0D) {
				entityRads.setInternalRadiationResistance(entityRads.getInternalRadiationResistance() + resistance);
				entityRads.setRecentRadXAddition(Math.abs(resistance));
				if (resistance > 0D) entity.playSound(NCSounds.rad_x, 1F - (float)Math.pow(10D, -5D*Math.sqrt(resistance)/NCConfig.radiation_rad_x_amount), 1F);
				else entity.playSound(NCSounds.chems_wear_off, 1F - (float)Math.pow(10D, -5D*Math.sqrt(-resistance)/NCConfig.radiation_rad_x_amount), 1F);
			}
		}
	}
}
