package nc.util;

import nc.tile.fluid.ITileFluid;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

public class FluidHelper {
	
	public static ItemStack getBucket(FluidStack fluidStack) {
		return FluidUtil.getFilledBucket(fluidStack);
	}
	
	public static ItemStack getBucket(Fluid fluid) {
		return getBucket(new FluidStack(fluid, Fluid.BUCKET_VOLUME));
	}
	
	public static ItemStack getBucket(String fluidName) {
		return getBucket(FluidRegistry.getFluid(fluidName));
	}
	
	// Accessing machine tanks - taken from net.minecraftforge.fluids.FluidUtil and modified to correctly handle ITileFluids
	
	public static boolean accessTanks(EntityPlayer player, EnumHand hand, EnumFacing facing, ITileFluid tile) {
		if (player == null || tile == null) return false;
		ItemStack heldItem = player.getHeldItem(hand);
		if (!heldItem.isEmpty()) {
			IItemHandler playerInventory = player.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
			if (playerInventory != null) {
				IFluidHandlerItem container = FluidUtil.getFluidHandler(ItemHandlerHelper.copyStackWithSize(heldItem, 1));
				if (container == null) return false;
				for (int i = 0; i < tile.getTanks().size(); i++) {
					FluidActionResult fluidActionResult = !tile.getTankSorption(facing, i).canDrain() ? FluidActionResult.FAILURE : FluidUtil.tryFillContainerAndStow(heldItem, tile.getTanks().get(i), playerInventory, Integer.MAX_VALUE, player, true);
					if (!fluidActionResult.isSuccess()) {
						if (tile.getTankSorption(facing, i).canFill() && tile.isNextToFill(facing, i, container.drain(Integer.MAX_VALUE, false))) {
							fluidActionResult = FluidUtil.tryEmptyContainerAndStow(heldItem, tile.getTanks().get(i), playerInventory, Integer.MAX_VALUE, player, true);
						}
					}
					if (fluidActionResult.isSuccess()) {
						player.setHeldItem(hand, fluidActionResult.getResult());
						return true;
					}
				}
			}
		}
		return false;
	}
}
