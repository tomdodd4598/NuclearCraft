package nc.item;

import nc.capability.radiation.entity.IEntityRads;
import nc.init.NCSounds;
import nc.util.*;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.stats.StatList;
import net.minecraft.util.*;
import net.minecraft.util.text.*;
import net.minecraft.world.World;

import static nc.config.NCConfig.*;

public class ItemRadaway extends NCItem {
	
	private static final String RADAWAY_COOLDOWN = Lang.localize("message.nuclearcraft.radaway_cooling_down");
	
	private final boolean slow;
	
	public ItemRadaway(boolean slow, String... tooltip) {
		super(tooltip);
		this.slow = slow;
	}
	
	@Override
	public ItemStack onItemUseFinish(ItemStack stack, World world, EntityLivingBase entity) {
		if (entity instanceof EntityPlayer player) {
			if (!player.hasCapability(IEntityRads.CAPABILITY_ENTITY_RADS, null)) {
				return stack;
			}
			IEntityRads playerRads = player.getCapability(IEntityRads.CAPABILITY_ENTITY_RADS, null);
			if (playerRads == null) {
				return stack;
			}
			if (playerRads.canConsumeRadaway()) {
				world.playSound(null, player.posX, player.posY, player.posZ, NCSounds.radaway, SoundCategory.PLAYERS, (float) (0.5D * radiation_sound_volumes[1]), 1F);
				onRadawayConsumed(stack, world, player);
				player.addStat(StatList.getObjectUseStats(this));
				if (player instanceof EntityPlayerMP) {
					CriteriaTriggers.CONSUME_ITEM.trigger((EntityPlayerMP) player, stack);
				}
				if (!player.isCreative()) {
					stack.shrink(1);
				}
				playerRads.setConsumedMedicine(true);
				playerRads.setRadawayCooldown(radiation_radaway_cooldown);
				if (radiation_radaway_cooldown >= 10D) {
					sendCooldownMessage(world, player, playerRads, false);
				}
			}
			else {
				playerRads.setConsumedMedicine(false);
			}
		}
		return stack;
	}
	
	private static void sendCooldownMessage(World world, EntityPlayer player, IEntityRads playerRads, boolean playSound) {
		if (playerRads.getRadawayCooldown() > 0D && playerRads.getMessageCooldownTime() <= 0) {
			if (playSound && world.isRemote) {
				player.playSound(NCSounds.chems_wear_off, (float) (0.5D * radiation_sound_volumes[4]), 1F);
			}
			if (!world.isRemote) {
				playerRads.setMessageCooldownTime(20);
				player.sendMessage(new TextComponentString(TextFormatting.ITALIC + RADAWAY_COOLDOWN + " " + UnitHelper.applyTimeUnitShort(Math.ceil(playerRads.getRadawayCooldown()), 3, 1)));
			}
		}
	}
	
	private void onRadawayConsumed(ItemStack stack, World world, EntityPlayer player) {
		if (world.isRemote || !player.hasCapability(IEntityRads.CAPABILITY_ENTITY_RADS, null)) {
			return;
		}
		IEntityRads playerRads = player.getCapability(IEntityRads.CAPABILITY_ENTITY_RADS, null);
		if (playerRads == null) {
			return;
		}
		playerRads.setRadawayBuffer(slow, playerRads.getRadawayBuffer(slow) + (slow ? radiation_radaway_slow_amount : radiation_radaway_amount));
		if (!slow) {
			playerRads.setRecentRadawayAddition(radiation_radaway_amount);
		}
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
		if (!player.hasCapability(IEntityRads.CAPABILITY_ENTITY_RADS, null)) {
			return actionResult(false, stack);
		}
		IEntityRads playerRads = player.getCapability(IEntityRads.CAPABILITY_ENTITY_RADS, null);
		if (playerRads == null) {
			return actionResult(false, stack);
		}
		
		if (!playerRads.canConsumeRadaway()) {
			playerRads.setConsumedMedicine(false);
			sendCooldownMessage(world, player, playerRads, true);
			return actionResult(false, stack);
		}
		if (!playerRads.isTotalRadsNegligible()) {
			player.setActiveHand(hand);
			return actionResult(true, stack);
		}
		else {
			return actionResult(false, stack);
		}
	}
}
