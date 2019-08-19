package nc.item;

import nc.capability.radiation.entity.IEntityRads;
import nc.config.NCConfig;
import nc.init.NCSounds;
import nc.util.Lang;
import nc.util.UnitHelper;
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
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

public class ItemRadX extends NCItem {
	
	private static final String RAD_X_COOLDOWN = Lang.localise("message.nuclearcraft.rad_x_cooling_down");
	
	public ItemRadX(String... tooltip) {
		super(tooltip);
	}
	
	@Override
	public ItemStack onItemUseFinish(ItemStack stack, World world, EntityLivingBase entity) {
		if (entity instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer)entity;
			if (!player.hasCapability(IEntityRads.CAPABILITY_ENTITY_RADS, null)) return stack;
			IEntityRads playerRads = player.getCapability(IEntityRads.CAPABILITY_ENTITY_RADS, null);
			if (playerRads == null) return stack;
			if (playerRads.canConsumeRadX()) {
				world.playSound((EntityPlayer)null, player.posX, player.posY, player.posZ, NCSounds.rad_x, SoundCategory.PLAYERS, 0.5F, 1F);
				onRadXConsumed(stack, world, player);
				player.addStat(StatList.getObjectUseStats(this));
				if (player instanceof EntityPlayerMP) CriteriaTriggers.CONSUME_ITEM.trigger((EntityPlayerMP)player, stack);
				stack.shrink(1);
				playerRads.setConsumedMedicine(true);
				playerRads.setRadXCooldown(NCConfig.radiation_rad_x_cooldown);
				if (NCConfig.radiation_rad_x_cooldown >= 10D) sendCooldownMessage(world, player, playerRads, false);
			}
			else {
				playerRads.setConsumedMedicine(false);
			}
		}
		return stack;
	}
	
	private static void sendCooldownMessage(World world, EntityPlayer player, IEntityRads playerRads, boolean playSound) {
		if (playerRads.getRadXCooldown() > 0D) {
			if (playSound && world.isRemote) player.playSound(NCSounds.chems_wear_off, 0.5F, 1F);
			if (!world.isRemote) player.sendMessage(new TextComponentString(TextFormatting.ITALIC + RAD_X_COOLDOWN + " " + UnitHelper.applyTimeUnitShort(Math.ceil(playerRads.getRadXCooldown()), 2, 1)));
		}
	}
	
	private static void onRadXConsumed(ItemStack stack, World world, EntityPlayer player) {
		if (world.isRemote || !player.hasCapability(IEntityRads.CAPABILITY_ENTITY_RADS, null)) return;
		IEntityRads playerRads = player.getCapability(IEntityRads.CAPABILITY_ENTITY_RADS, null);
		if (playerRads == null) return;
		playerRads.setInternalRadiationResistance(playerRads.getInternalRadiationResistance() + NCConfig.radiation_rad_x_amount);
		playerRads.setRecentRadXAddition(NCConfig.radiation_rad_x_amount);
		playerRads.setRadXUsed(true);
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
		
		if (!playerRads.canConsumeRadX()) {
			playerRads.setConsumedMedicine(false);
			sendCooldownMessage(world, player, playerRads, true);
			return actionResult(false, stack);
		}
		player.setActiveHand(hand);
		return actionResult(true, stack);
	}
}