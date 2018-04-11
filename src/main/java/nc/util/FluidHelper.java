package nc.util;

import nc.tile.internal.Tank;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;

public class FluidHelper {
	
	public static final int BUCKET_VOLUME = 1000;
	
	public static final int INGOT_ORE_VOLUME = 324;
	public static final int INGOT_VOLUME = 144;
	public static final int NUGGET_VOLUME = INGOT_VOLUME/9;
	public static final int INGOT_BLOCK_VOLUME = INGOT_VOLUME*9;
	
	public static final int FRAGMENT_VOLUME = INGOT_VOLUME/4;
	public static final int SHARD_VOLUME = INGOT_VOLUME/2;
	
	public static final int GEM_ORE_VOLUME = 1665;
	public static final int GEM_VOLUME = 666;
	public static final int GEM_NUGGET_VOLUME = GEM_VOLUME/9;
	public static final int GEM_BLOCK_VOLUME = GEM_VOLUME*9;
	
	public static final int GLASS_VOLUME = BUCKET_VOLUME;
	public static final int GLASS_PANE_VOLUME = (GLASS_VOLUME*6)/16;
	public static final int BRICK_VOLUME = INGOT_VOLUME;
	public static final int BRICK_BLOCK_VOLUME = BRICK_VOLUME*4;
	
	public static final int SEARED_BLOCK_VOLUME = INGOT_VOLUME*2;
	public static final int SEARED_MATERIAL_VOLUME = INGOT_VOLUME/2;
	
	public static final int SLIMEBALL_VOLUME = 250;
	
	public static final int REDSTONE_DUST_VOLUME = 100;
	public static final int REDSTONE_BLOCK_VOLUME = REDSTONE_DUST_VOLUME*9;
	public static final int GLOWSTONE_DUST_VOLUME = 250;
	public static final int GLOWSTONE_BLOCK_VOLUME = GLOWSTONE_DUST_VOLUME*4;
	public static final int COAL_DUST_VOLUME = 100;
	public static final int ENDER_PEARL_VOLUME = 250;
	
	public static final int EUM_DUST_VOLUME = 250;
	
	public static final int OXIDIZING_VOLUME = 400;
	
	public static final int PARTICLE_VOLUME = 10;
	
	public static boolean accessTankArray(EntityPlayer player, EnumHand hand, Tank[] tanks) {
		ItemStack heldItem = player.getHeldItem(hand);
		for (int i = 0; i < tanks.length; i++) {
			boolean accessedTank = accessTank(player, hand, tanks[i]);
			if (accessedTank) return true;
		}
		return false;
	}
	
	public static boolean accessTank(EntityPlayer player, EnumHand hand, Tank tank) {
		ItemStack heldItem = player.getHeldItem(hand);
		if (heldItem == null) return false;
		return FluidUtil.interactWithFluidHandler(player, hand, tank);
	}
	
	public static ItemStack getBucket(FluidStack fluidStack) {
		return FluidUtil.getFilledBucket(fluidStack);
	}
	
	public static ItemStack getBucket(Fluid fluid) {
		return getBucket(new FluidStack(fluid, 1000));
	}
	
	public static ItemStack getBucket(String fluidName) {
		return getBucket(FluidRegistry.getFluid(fluidName));
	}
}
