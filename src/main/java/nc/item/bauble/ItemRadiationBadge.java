package nc.item.bauble;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import nc.ModCheck;
import nc.capability.radiation.entity.IEntityRads;
import nc.capability.radiation.sink.IRadiationSink;
import nc.config.NCConfig;
import nc.init.NCSounds;
import nc.item.NCItem;
import nc.util.Lang;
import nc.util.UnitHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Optional;

@Optional.Interface(iface = "baubles.api.IBauble", modid = "baubles")
public class ItemRadiationBadge extends NCItem implements IBauble {
	
	private static final String EXPOSURE = Lang.localise("item.nuclearcraft.radiation_badge.exposure");
	private static final String BADGE_BROKEN = Lang.localise("item.nuclearcraft.radiation_badge.broken");
	
	public ItemRadiationBadge(String... tooltip) {
		super(tooltip);
		maxStackSize = 1;
	}
	
	@Override
	@Optional.Method(modid = "baubles")
	public BaubleType getBaubleType(ItemStack stack) {
		return BaubleType.TRINKET;
	}
	
	@Override
	public boolean showDurabilityBar(ItemStack stack) {
		if (!stack.hasCapability(IRadiationSink.CAPABILITY_RADIATION_SINK, null)) return false;
		IRadiationSink radiation = stack.getCapability(IRadiationSink.CAPABILITY_RADIATION_SINK, null);
		if (radiation == null) return false;
		return radiation.getRadiationLevel() > 0D;
	}
	
	@Override
	public double getDurabilityForDisplay(ItemStack stack) {
		if (!stack.hasCapability(IRadiationSink.CAPABILITY_RADIATION_SINK, null)) return 0D;
		IRadiationSink badge = stack.getCapability(IRadiationSink.CAPABILITY_RADIATION_SINK, null);
		if (badge == null) return 0D;
		return MathHelper.clamp(badge.getRadiationLevel()/NCConfig.radiation_badge_durability, 0D, 1D);
	}
	
	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected) {
		if (!ModCheck.baublesLoaded() && entity instanceof EntityPlayer) updateBadge(stack, (EntityPlayer) entity);
	}
	
	@Override
	@Optional.Method(modid = "baubles")
	public void onWornTick(ItemStack stack, EntityLivingBase entity) {
		if (entity instanceof EntityPlayer) updateBadge(stack, (EntityPlayer) entity);
	}
	
	private static void updateBadge(ItemStack stack, EntityPlayer player) {
		if (player.hasCapability(IEntityRads.CAPABILITY_ENTITY_RADS, null)) {
			IEntityRads entityRads = player.getCapability(IEntityRads.CAPABILITY_ENTITY_RADS, null);
			if (entityRads == null || entityRads.isRadiationUndetectable() || !stack.hasCapability(IRadiationSink.CAPABILITY_RADIATION_SINK, null)) return;
			IRadiationSink badge = stack.getCapability(IRadiationSink.CAPABILITY_RADIATION_SINK, null);
			if (badge == null) return;
			int infoCount = MathHelper.floor(badge.getRadiationLevel()/(NCConfig.radiation_badge_info_rate*NCConfig.radiation_badge_durability));
			badge.setRadiationLevel(badge.getRadiationLevel() + entityRads.getRadiationLevel());
			World world = player.getEntityWorld();
			if (badge.getRadiationLevel() >= NCConfig.radiation_badge_durability) {
				if (!world.isRemote) {
					player.sendMessage(new TextComponentString(TextFormatting.ITALIC + EXPOSURE + " " + UnitHelper.prefix(badge.getRadiationLevel(), 3, "Rads")));
					player.sendMessage(new TextComponentString(TextFormatting.ITALIC + BADGE_BROKEN));
				}
				else {
					player.playSound(NCSounds.chems_wear_off, 0.65F, 1F);
				}
				stack.shrink(1);
			}
			else if (!world.isRemote && infoCount != MathHelper.floor(badge.getRadiationLevel()/(NCConfig.radiation_badge_info_rate*NCConfig.radiation_badge_durability))) {
				player.sendMessage(new TextComponentString(TextFormatting.ITALIC + EXPOSURE + " " + UnitHelper.prefix(badge.getRadiationLevel(), 3, "Rads")));
			}
		}
	}
}
