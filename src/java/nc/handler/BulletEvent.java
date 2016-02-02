package nc.handler;

import cpw.mods.fml.common.eventhandler.Cancelable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerEvent;

@Cancelable
public class BulletEvent extends PlayerEvent
{
    public final ItemStack gun;
    
    public BulletEvent(EntityPlayer player, ItemStack gun)
    {
        super(player);
        this.gun = gun;
    }
}