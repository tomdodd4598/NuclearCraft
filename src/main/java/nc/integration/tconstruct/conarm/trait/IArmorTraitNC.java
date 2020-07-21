package nc.integration.tconstruct.conarm.trait;

import c4.conarm.lib.traits.IArmorTrait;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public interface IArmorTraitNC extends IArmorTrait {
	
	@Override
	public default void onAbilityTick(int var1, World var2, EntityPlayer var3) {
		
	}
}
