package nc.item.bauble;

import static nc.config.NCConfig.radiation_require_counter;

import baubles.api.*;
import nc.capability.radiation.entity.IEntityRads;
import nc.item.NCItem;
import nc.radiation.*;
import nc.util.Lang;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Optional;

@Optional.Interface(iface = "baubles.api.IBauble", modid = "baubles")
public class ItemGeigerCounter extends NCItem implements IBauble {
	
	private static final String RADIATION = Lang.localise("item.nuclearcraft.geiger_counter.rads");
	
	public ItemGeigerCounter(String... tooltip) {
		super(tooltip);
		maxStackSize = 1;
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		if (worldIn.isRemote) {
			RayTraceResult rayTrace = Minecraft.getMinecraft().objectMouseOver;
			if (!(rayTrace != null && rayTrace.typeOfHit == Type.ENTITY)) {
				IEntityRads playerRads = RadiationHelper.getEntityRadiation(playerIn);
				if (playerRads != null) {
					playerIn.sendMessage(new TextComponentString(RadiationHelper.getRadsTextColor(playerRads) + RADIATION + " " + (playerRads.isTotalRadsNegligible() ? "0 Rad" : RadiationHelper.radsPrefix(playerRads.getTotalRads(), false)) + " [" + Math.round(playerRads.getRadsPercentage()) + "%], " + RadiationHelper.getRawRadiationTextColor(playerRads) + (playerRads.isRawRadiationNegligible() ? "0 Rad/t" : RadiationHelper.radsPrefix(playerRads.getRawRadiationLevel(), true))));
				}
			}
		}
		return new ActionResult<>(EnumActionResult.PASS, playerIn.getHeldItem(handIn));
	}
	
	@Override
	public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer player, EntityLivingBase entity, EnumHand hand) {
		if (!entity.world.isRemote) {
			IEntityRads entityRads = RadiationHelper.getEntityRadiation(entity);
			if (entityRads != null) {
				player.sendMessage(new TextComponentString(RadiationHelper.getRadsTextColor(entityRads) + RADIATION + " " + (entityRads.isTotalRadsNegligible() ? "0 Rad" : RadiationHelper.radsPrefix(entityRads.getTotalRads(), false)) + " [" + Math.round(entityRads.getRadsPercentage()) + "%], " + RadiationHelper.getRadiationTextColor(entityRads) + (entityRads.isRadiationNegligible() ? "0 Rad/t" : RadiationHelper.radsPrefix(entityRads.getRadiationLevel(), true))));
			}
		}
		return false;
	}
	
	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected) {
		if (!world.isRemote || !radiation_require_counter || !(entity instanceof EntityPlayer)) {
			return;
		}
		EntityPlayer player = (EntityPlayer) entity;
		if (isStackOnHotbar(stack, player)) {
			RadiationHandler.playGeigerSound(player);
		}
	}
	
	@Override
	@Optional.Method(modid = "baubles")
	public BaubleType getBaubleType(ItemStack stack) {
		return BaubleType.TRINKET;
	}
}
