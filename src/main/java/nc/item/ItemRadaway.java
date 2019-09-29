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

public class ItemRadaway extends NCItem {
	
	private static final String RADAWAY_COOLDOWN = Lang.localise("message.nuclearcraft.radaway_cooling_down");
	
	private final boolean slow;
	
	public ItemRadaway(boolean slow, String... tooltip) {
		super(tooltip);
		this.slow = slow;
	}
	
	@Override
	public ItemStack onItemUseFinish(ItemStack stack, World world, EntityLivingBase entity) {
		if (entity instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer)entity;
			if (!player.hasCapability(IEntityRads.CAPABILITY_ENTITY_RADS, null)) return stack;
			IEntityRads playerRads = player.getCapability(IEntityRads.CAPABILITY_ENTITY_RADS, null);
			if (playerRads == null) return stack;
			if (playerRads.canConsumeRadaway()) {
				world.playSound((EntityPlayer)null, player.posX, player.posY, player.posZ, NCSounds.radaway, SoundCategory.PLAYERS, 0.5F, 1F);
				onRadawayConsumed(stack, world, player);
				player.addStat(StatList.getObjectUseStats(this));
				if (player instanceof EntityPlayerMP) CriteriaTriggers.CONSUME_ITEM.trigger((EntityPlayerMP)player, stack);
				stack.shrink(1);
				playerRads.setConsumedMedicine(true);
				playerRads.setRadawayCooldown(NCConfig.radiation_radaway_cooldown);
				if (NCConfig.radiation_radaway_cooldown >= 10D) sendCooldownMessage(world, player, playerRads, false);
			}
			else {
				playerRads.setConsumedMedicine(false);
			}
		}
		return stack;
	}
	
	private static void sendCooldownMessage(World world, EntityPlayer player, IEntityRads playerRads, boolean playSound) {
		if (playerRads.getRadawayCooldown() > 0D) {
			if (playSound && world.isRemote) player.playSound(NCSounds.chems_wear_off, 0.5F, 1F);
			player.sendMessage(new TextComponentString(TextFormatting.ITALIC + RADAWAY_COOLDOWN + " " + UnitHelper.applyTimeUnitShort(Math.ceil(playerRads.getRadawayCooldown()), 2, 1)));
		}
	}
	
	private void onRadawayConsumed(ItemStack stack, World world, EntityPlayer player) {
		if (world.isRemote || !player.hasCapability(IEntityRads.CAPABILITY_ENTITY_RADS, null)) return;
		IEntityRads playerRads = player.getCapability(IEntityRads.CAPABILITY_ENTITY_RADS, null);
		if (playerRads == null) return;
		playerRads.setRadawayBuffer(slow, playerRads.getRadawayBuffer(slow) + (slow ? NCConfig.radiation_radaway_slow_amount : NCConfig.radiation_radaway_amount));
		if (!slow) playerRads.setRecentRadawayAddition(NCConfig.radiation_radaway_amount);
	}
	
	@Override
	public int getMaxItemUseDuration(ItemStack stack) {
		return 16;
	}
	
	@Override
	public EnumAction getItemUseAction(ItemStack stack) {
		return EnumAction.BOW;
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		ItemStack stack = player.getHeldItem(hand);
		if (!player.hasCapability(IEntityRads.CAPABILITY_ENTITY_RADS, null)) return actionResult(false, stack);
		IEntityRads playerRads = player.getCapability(IEntityRads.CAPABILITY_ENTITY_RADS, null);
		if (playerRads == null) return actionResult(false, stack);
		
		if (!playerRads.canConsumeRadaway()) {
			playerRads.setConsumedMedicine(false);
			sendCooldownMessage(world, player, playerRads, true);
			return actionResult(false, stack);
		}
		if (!playerRads.isTotalRadsNegligible()) {
			player.setActiveHand(hand);
			return actionResult(true, stack);
		}
		else return actionResult(false, stack);
	}
}