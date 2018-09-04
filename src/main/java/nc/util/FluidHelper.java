package nc.util;

import java.util.List;

import nc.tile.internal.fluid.Tank;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;

public class FluidHelper {
	
	public static boolean accessTankArray(EntityPlayer player, EnumHand hand, List<Tank> tanks) {
		ItemStack heldItem = player.getHeldItem(hand);
		for (int i = 0; i < tanks.size(); i++) {
			boolean accessedTank = accessTank(player, hand, tanks.get(i));
			if (accessedTank) return true;
		}
		return false;
	}
	
	public static boolean accessTank(EntityPlayer player, EnumHand hand, Tank tank) {
		if (player == null || player.getHeldItem(hand) == null) return false;
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
