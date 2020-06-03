package nc.container;

import javax.annotation.Nullable;

import nc.tile.ITileGui;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.*;
import net.minecraftforge.fml.relauncher.*;

public class ContainerTile<T extends ITileGui> extends NCContainer {
	
	protected final @Nullable IInventory inv;
	
	public ContainerTile(T tile) {
		super();
		inv = tile instanceof IInventory ? (IInventory) tile : null;
	}
	
	@Override
	public void addListener(IContainerListener listener) {
		super.addListener(listener);
		if (inv != null) {
			listener.sendAllWindowProperties(this, inv);
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void updateProgressBar(int id, int data) {
		if (inv != null) {
			inv.setField(id, data);
		}
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return inv == null ? false : inv.isUsableByPlayer(player);
	}
}
