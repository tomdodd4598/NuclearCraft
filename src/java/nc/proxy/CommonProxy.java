package nc.proxy;

import net.minecraft.entity.player.EntityPlayer;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class CommonProxy {
	
	public void registerRenderThings() {}
	public void registerSounds() {}
	public void registerTileEntitySpecialRenderer() {}
	
	public EntityPlayer getPlayerEntity(MessageContext ctx) {
        return ctx.getServerHandler().playerEntity;
    }
}
