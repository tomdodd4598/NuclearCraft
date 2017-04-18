package nc.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryEnderChest;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemEnderChest extends ItemNC {
	
	public ItemEnderChest(String nam, String... lines) {
		super("", nam, lines);
	}

	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
		InventoryEnderChest inventoryenderchest = player.getInventoryEnderChest();
		if (!world.isRemote) {
            player.displayGUIChest(inventoryenderchest);
		}
	    return stack;
	}
}
