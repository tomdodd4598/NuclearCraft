package nc.network.gui;

import io.netty.buffer.ByteBuf;
import nc.tile.internal.inventory.ItemSorption;
import nc.tile.inventory.ITileInventory;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;

public class ResetItemSorptionsPacket extends TileGuiPacket {
	
	protected int slot;
	protected boolean defaults;
	
	public ResetItemSorptionsPacket() {
		super();
	}
	
	public ResetItemSorptionsPacket(ITileInventory tile, int slot, boolean defaults) {
		super(tile);
		this.slot = slot;
		this.defaults = defaults;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		super.fromBytes(buf);
		slot = buf.readInt();
		defaults = buf.readBoolean();
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		super.toBytes(buf);
		buf.writeInt(slot);
		buf.writeBoolean(defaults);
	}
	
	public static class Handler extends TileGuiPacket.Handler<ResetItemSorptionsPacket> {
		
		@Override
		protected void onPacket(ResetItemSorptionsPacket message, EntityPlayerMP player, TileEntity tile) {
			if (tile instanceof ITileInventory) {
				ITileInventory machine = (ITileInventory) tile;
				for (EnumFacing side : EnumFacing.VALUES) {
					if (message.defaults) {
						machine.setItemSorption(side, message.slot, machine.getInventoryConnection(side).getDefaultItemSorption(message.slot));
					}
					else {
						machine.setItemSorption(side, message.slot, ItemSorption.NON);
					}
				}
				machine.markDirtyAndNotify(true);
			}
		}
	}
}
