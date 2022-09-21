package nc.container;

import javax.annotation.Nullable;

import nc.network.tile.TileUpdatePacket;
import nc.tile.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.*;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.relauncher.*;

public class ContainerTile<TILE extends TileEntity & ITileGui<TILE, PACKET, INFO>, PACKET extends TileUpdatePacket, INFO extends TileContainerInfo<TILE>> extends NCContainer {
	
	protected final @Nullable IInventory inv;
	protected final INFO info;
	
	public ContainerTile(TILE tile) {
		super();
		inv = tile instanceof IInventory ? (IInventory) tile : null;
		info = tile.getContainerInfo();
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
