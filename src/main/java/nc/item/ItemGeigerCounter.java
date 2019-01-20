package nc.item;

import java.util.Random;

import nc.capability.radiation.IEntityRads;
import nc.config.NCConfig;
import nc.handler.SoundHandler;
import nc.util.Lang;
import nc.util.RadiationHelper;
import nc.util.UnitHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class ItemGeigerCounter extends NCItem {
	
	private Random rand = new Random();
	
	private static final String RADIATION = Lang.localise("item.nuclearcraft.geiger_counter.rads");
	
	public ItemGeigerCounter(String nameIn, String... tooltip) {
		super(nameIn, tooltip);
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		if (worldIn.isRemote && playerIn.hasCapability(IEntityRads.CAPABILITY_ENTITY_RADS, null)) {
			IEntityRads playerRads = playerIn.getCapability(IEntityRads.CAPABILITY_ENTITY_RADS, null);
			if (playerRads != null) {
				playerIn.sendMessage(new TextComponentString(RadiationHelper.getRadsTextColor(playerRads) + RADIATION + " " + (playerRads.isTotalRadsNegligible() ? "0 Rads/t" : UnitHelper.prefix(playerRads.getTotalRads(), 3, "Rads", 0, -8)) + " [" + playerRads.getRadsPercentage() + "%], " + RadiationHelper.getRadiationTextColor(playerRads) + (playerRads.isRadiationNegligible() ? "0 Rads/t" : UnitHelper.prefix(playerRads.getRadiationLevel(), 3, "Rads/t", 0, -8))));
			}
		}
		return new ActionResult<ItemStack>(EnumActionResult.PASS, playerIn.getHeldItem(handIn));
	}
	
	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected) {
		if (!world.isRemote || !NCConfig.radiation_require_counter || !(entity instanceof EntityPlayer)) return;
		EntityPlayer player = (EntityPlayer) entity;
		if (isStackOnHotbar(stack, player) && player.hasCapability(IEntityRads.CAPABILITY_ENTITY_RADS, null)) {
			IEntityRads entityRads = player.getCapability(IEntityRads.CAPABILITY_ENTITY_RADS, null);
			if (entityRads == null) return;
			if (entityRads.isRadiationUndetectable()) return;
			double soundChance = Math.cbrt(entityRads.getRadiationLevel()/200D);
			float soundVolume = MathHelper.clamp((float)(8F*soundChance), 0.55F, 1.1F);
			for (int i = 0; i < 2; i++) if (rand.nextDouble() < soundChance) player.playSound(SoundHandler.geiger_tick, soundVolume + rand.nextFloat()*0.12F, 0.92F + rand.nextFloat()*0.16F);
		}
	}
}