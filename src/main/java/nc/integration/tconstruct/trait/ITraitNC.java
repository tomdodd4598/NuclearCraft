package nc.integration.tconstruct.trait;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import slimeknights.tconstruct.library.traits.ITrait;

public interface ITraitNC extends ITrait {
	
	@Override
	public default void onUpdate(ItemStack tool, World world, Entity entity, int itemSlot, boolean isSelected) {
		
	}
}
