package nc.item;

import nc.capability.radiation.IEntityRads;
import nc.config.NCConfig;
import nc.handler.SoundHandler;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

public class ItemRadX extends NCItem {
	
	public ItemRadX(String nameIn, String... tooltip) {
		super(nameIn, tooltip);
	}
	
	@Override
	public ItemStack onItemUseFinish(ItemStack stack, World world, EntityLivingBase entity) {
		if (entity instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer)entity;
			if (!player.hasCapability(IEntityRads.CAPABILITY_ENTITY_RADS, null)) return stack;
			IEntityRads playerRads = player.getCapability(IEntityRads.CAPABILITY_ENTITY_RADS, null);
			if (playerRads == null) return stack;
			if (!playerRads.getConsumedMedicine()) {
				world.playSound((EntityPlayer)null, player.posX, player.posY, player.posZ, SoundHandler.rad_x, SoundCategory.PLAYERS, 0.5F, 1F);
				onRadawayConsumed(stack, world, player);
				player.addStat(StatList.getObjectUseStats(this));
				if (player instanceof EntityPlayerMP) CriteriaTriggers.CONSUME_ITEM.trigger((EntityPlayerMP)player, stack);
				stack.shrink(1);
				playerRads.setConsumedMedicine(true);
			}
			else playerRads.setConsumedMedicine(false);
		}
		return stack;
	}
	
	private void onRadawayConsumed(ItemStack stack, World world, EntityPlayer player) {
		if (world.isRemote || !player.hasCapability(IEntityRads.CAPABILITY_ENTITY_RADS, null)) return;
		IEntityRads playerRads = player.getCapability(IEntityRads.CAPABILITY_ENTITY_RADS, null);
		if (playerRads == null) return;
		playerRads.setRadiationResistance(playerRads.getRadiationResistance() + NCConfig.radiation_rad_x_amount);
	}
	
	@Override
	public int getMaxItemUseDuration(ItemStack stack) {
		return 16;
	}
	
	@Override
	public EnumAction getItemUseAction(ItemStack stack) {
		return EnumAction.DRINK;
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		ItemStack stack = player.getHeldItem(hand);
		if (!player.hasCapability(IEntityRads.CAPABILITY_ENTITY_RADS, null)) return actionResult(false, stack);
		IEntityRads playerRads = player.getCapability(IEntityRads.CAPABILITY_ENTITY_RADS, null);
		if (playerRads == null) return actionResult(false, stack);
		
		if (playerRads.getConsumedMedicine()) {
			playerRads.setConsumedMedicine(false);
			return actionResult(false, stack);
		}
		player.setActiveHand(hand);
		return actionResult(true, stack);
	}
}