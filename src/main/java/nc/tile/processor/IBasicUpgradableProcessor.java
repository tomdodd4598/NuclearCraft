package nc.tile.processor;

import nc.init.NCItems;
import nc.network.tile.processor.ProcessorUpdatePacket;
import nc.tile.ITileInstallable;
import nc.tile.processor.info.ProcessorContainerInfoImpl;
import nc.util.PrimitiveFunction.ToBooleanBiFunction;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraftforge.items.*;

public interface IBasicUpgradableProcessor<TILE extends TileEntity & IBasicUpgradableProcessor<TILE, PACKET>, PACKET extends ProcessorUpdatePacket> extends IProcessor<TILE, PACKET, ProcessorContainerInfoImpl.BasicUpgradableProcessorContainerInfo<TILE, PACKET>>, ITileInstallable {
	
	default boolean tryInstall(EntityPlayer player, EnumHand hand, EnumFacing facing) {
		ItemStack held = player.getHeldItem(hand);
		
		ToBooleanBiFunction<Integer, ItemStack> tryInstallUpgrade = (x, y) -> {
			if (held.isItemEqual(y)) {
				IItemHandler inv = getTile().getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing);
				if (inv != null && inv.isItemValid(x, held)) {
					if (player.isSneaking()) {
						player.setHeldItem(EnumHand.MAIN_HAND, inv.insertItem(x, held, false));
						return true;
					}
					else {
						if (inv.insertItem(x, y, false).isEmpty()) {
							player.getHeldItem(hand).shrink(1);
							return true;
						}
					}
				}
			}
			return false;
		};
		
		ProcessorContainerInfoImpl.BasicUpgradableProcessorContainerInfo<TILE, PACKET> info = getContainerInfo();
		return tryInstallUpgrade.applyAsBoolean(info.speedUpgradeSlot, new ItemStack(NCItems.upgrade, 1, 0)) || tryInstallUpgrade.applyAsBoolean(info.energyUpgradeSlot, new ItemStack(NCItems.upgrade, 1, 1));
	}
}
