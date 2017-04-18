package nc.handler;

import nc.item.NCItems;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AnvilUpdateEvent;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

// HUGE thanks to lfja, Hlaaftana and OreCruncher of the FTB Forums for help with this.
// Many Many thanks to OreCruncher for providing his code as a source for me to work with, and pretty much just copy over to here
// With a few Minor changes here and there. 

public final class AnvilRepairHandler {

	public static int getItemDamage(final ItemStack stack) {
		assert stack != null;
		return stack.getItemDamage();
	}
	
	public static boolean isRepairable(final ItemStack stack) {
		final Item item = stack.getItem();
		return item != null ? item.isRepairable() : false;
	}
	
	public static void setItemName(final ItemStack stack, final String name) {
		if (stack != null)
			stack.setStackDisplayName(name);
	}
	
	private static final int RENAME_COST = 5;

	private boolean isValidRepairItem(final Item tool, final ItemStack stack) {

		if (stack != null && tool != null) {
			if (tool == NCItems.bronzeAxe || tool == NCItems.bronzeHoe || tool == NCItems.bronzePickaxe || tool == NCItems.bronzeShovel || tool == NCItems.bronzeSword) return stack.getItem() == new ItemStack(NCItems.material, 1, 6).getItem();
			if (tool == NCItems.toughAlloyAxe || tool == NCItems.toughAlloyHoe || tool == NCItems.toughAlloyPickaxe || tool == NCItems.toughAlloyShovel || tool == NCItems.toughAlloySword) return stack.getItem() == new ItemStack(NCItems.material, 1, 7).getItem();
			if (tool == NCItems.dUAxe || tool == NCItems.dUHoe || tool == NCItems.dUPickaxe || tool == NCItems.dUShovel || tool == NCItems.dUSword) return stack.getItem() == new ItemStack(NCItems.parts, 1, 8).getItem();
			if (tool == NCItems.boronAxe || tool == NCItems.boronHoe || tool == NCItems.boronPickaxe || tool == NCItems.boronShovel || tool == NCItems.boronSword) return stack.getItem() == new ItemStack(NCItems.material, 1, 43).getItem();
			
			if (tool == NCItems.toughAlloyPaxel || tool == NCItems.toughBow) return stack.getItem() == new ItemStack(NCItems.material, 1, 7).getItem();
			if (tool == NCItems.dUPaxel) return stack.getItem() == new ItemStack(NCItems.parts, 1, 8).getItem();
		}

		return false;
	}
	
	private boolean canBeRepaired(final ItemStack stack) {
		return isRepairable(stack) && stack.isItemDamaged();
	}

	@SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled = false)
	public void onAnvilChange(final AnvilUpdateEvent event) {

		final ItemStack itemToRepair = event.left;
		final ItemStack repairMaterial = event.right;

		if (itemToRepair == null || repairMaterial == null || !isValidRepairItem(itemToRepair.getItem(), repairMaterial))
			return;

		// Make a copy of the item and figure out any rename
		// cost. Items can be renamed even if they are not
		// normally repaired in an anvil.
		event.output = itemToRepair.copy();
		if (!event.name.isEmpty()) {
			event.cost = RENAME_COST;
			event.materialCost = 1;
			setItemName(event.output, event.name);
			
		}

		// Figure out a repair cost if the item is damaged
		if (canBeRepaired(itemToRepair)) {

			int repairAmount = 5000;
			
			if (itemToRepair.getItem() == NCItems.bronzeAxe || itemToRepair.getItem() == NCItems.bronzeHoe || itemToRepair.getItem() == NCItems.bronzePickaxe || itemToRepair.getItem() == NCItems.bronzeShovel || itemToRepair.getItem() == NCItems.bronzeSword) repairAmount = 300;
			if (itemToRepair.getItem() == NCItems.toughAlloyAxe || itemToRepair.getItem() == NCItems.toughAlloyHoe || itemToRepair.getItem() == NCItems.toughAlloyPickaxe || itemToRepair.getItem() == NCItems.toughAlloyShovel || itemToRepair.getItem() == NCItems.toughAlloySword || itemToRepair.getItem() == NCItems.toughBow) repairAmount = 2500/2;
			if (itemToRepair.getItem() == NCItems.dUAxe || itemToRepair.getItem() == NCItems.dUHoe || itemToRepair.getItem() == NCItems.dUPickaxe || itemToRepair.getItem() == NCItems.dUShovel || itemToRepair.getItem() == NCItems.dUSword) repairAmount = 6400/2;
			if (itemToRepair.getItem() == NCItems.boronAxe || itemToRepair.getItem() == NCItems.boronHoe || itemToRepair.getItem() == NCItems.boronPickaxe || itemToRepair.getItem() == NCItems.boronShovel || itemToRepair.getItem() == NCItems.boronSword) repairAmount = 1200;
			
			if (itemToRepair.getItem() == NCItems.toughAlloyPaxel) repairAmount = 15000/8;
			if (itemToRepair.getItem() == NCItems.dUPaxel) repairAmount = 32000/8;

			// Figure out the quantity needed to fully repair the item
			final int itemDamage = getItemDamage(itemToRepair);
			int howManyUnits = (int) Math.ceil(itemDamage / repairAmount);
			if (itemDamage % repairAmount != 0)
				howManyUnits++;

			// Cap it
			howManyUnits = Math.min(howManyUnits, repairMaterial.stackSize);
			final int damageRepaired = Math.min(itemDamage, howManyUnits * repairAmount);

			event.cost += (int) Math.round(2 + 2 * itemDamage / repairAmount);
			event.materialCost += howManyUnits;
			event.output.setItemDamage(getItemDamage(event.output) - damageRepaired);
		}
	}

	public AnvilRepairHandler() {
	}

	public static void register() {
		MinecraftForge.EVENT_BUS.register(new AnvilRepairHandler());
	}
}