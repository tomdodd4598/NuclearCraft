package nc.network.gui;

import io.netty.buffer.ByteBuf;
import nc.tile.internal.inventory.ItemSorption;
import nc.tile.inventory.ITileInventory;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;

public class ToggleItemSorptionPacket extends TileGuiPacket {
	
	protected int side;
	protected int slot;
	protected int sorption;
	
	public ToggleItemSorptionPacket() {
		super();
	}
	
	public ToggleItemSorptionPacket(ITileInventory tile, EnumFacing side, int slot, ItemSorption sorption) {
		super(tile);
		this.side = side.getIndex();
		this.slot = slot;
		this.sorption = sorption.ordinal();
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		super.fromBytes(buf);
		side = buf.readInt();
		slot = buf.readInt();
		sorption = buf.readInt();
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		super.toBytes(buf);
		buf.writeInt(side);
		buf.writeInt(slot);
		buf.writeInt(sorption);
	}
	
	public static class Handler extends TileGuiPacket.Handler<ToggleItemSorptionPacket> {
		
		@Override
		protected void onPacket(ToggleItemSorptionPacket message, EntityPlayerMP player, TileEntity tile) {
			if (tile instanceof ITileInventory machine) {
                machine.setItemSorption(EnumFacing.byIndex(message.side), message.slot, ItemSorption.values()[message.sorption]);
				machine.markDirtyAndNotify(true);
			}
		}
	}
}
