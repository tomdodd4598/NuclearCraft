package nc.tile;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public interface IMultitoolLogic extends ITile {
	
	default boolean onUseMultitool(ItemStack multitool, EntityPlayerMP player, World world, EnumFacing facing, float hitX, float hitY, float hitZ) {
		return false;
	}
}
