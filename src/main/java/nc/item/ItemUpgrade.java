package nc.item;

import nc.enumm.MetaEnums.UpgradeType;
import nc.init.NCItems;
import nc.util.StackHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;

public class ItemUpgrade extends NCItemMeta<UpgradeType> {
	
	public ItemUpgrade(Class<UpgradeType> enumm, TextFormatting infoColor, String[]... tooltips) {
		super(enumm, infoColor, tooltips);
	}
	
	public ItemUpgrade(Class<UpgradeType> enumm, String[]... tooltips) {
		super(enumm, tooltips);
	}
	
	// Allow upgrades to be right-clicked into machines
	@Override
	public boolean doesSneakBypassUse(ItemStack stack, IBlockAccess world, BlockPos pos, EntityPlayer player) {
		return stack.getItem() == NCItems.upgrade;
	}
	
	@Override
	public int getItemStackLimit(ItemStack stack) {
		if (stack.getItem() == NCItems.upgrade) {
			return values[StackHelper.getMetadata(stack)].getMaxStackSize();
		}
		else {
			return super.getItemStackLimit(stack);
		}
	}
}
