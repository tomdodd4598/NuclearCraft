package nc.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryEnderChest;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ItemPortableEnderChest extends NCItem {
	
	public ItemPortableEnderChest(String nameIn, String... tooltip) {
		super(nameIn, tooltip);
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		InventoryEnderChest inventoryenderchest = playerIn.getInventoryEnderChest();
		
		if (inventoryenderchest != null) {
			if (!worldIn.isRemote) {
				playerIn.displayGUIChest(inventoryenderchest);
				playerIn.addStat(StatList.ENDERCHEST_OPENED);
			}
		}
		return new ActionResult<ItemStack>(EnumActionResult.PASS, playerIn.getHeldItem(handIn));
	}
}