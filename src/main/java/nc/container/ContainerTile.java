package nc.container;

import javax.annotation.Nullable;

import nc.tile.ITile;
import nc.tile.inventory.ITileInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ContainerTile extends NCContainer {
	
	public final @Nullable IInventory invWrapper;
	
	public ContainerTile(ITileInventory tile) {
		super();
		invWrapper = tile.getInventory();
	}
	
	public ContainerTile(ITile tile) {
		super();
		invWrapper = null;
	}
	
	@Override
	public void addListener(IContainerListener listener) {
		super.addListener(listener);
		if (invWrapper != null) listener.sendAllWindowProperties(this, invWrapper);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void updateProgressBar(int id, int data) {
		if (invWrapper != null) invWrapper.setField(id, data);
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return invWrapper == null ? false : invWrapper.isUsableByPlayer(player);
	}
}
