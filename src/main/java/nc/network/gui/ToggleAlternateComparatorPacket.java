package nc.network.gui;

import io.netty.buffer.ByteBuf;
import nc.tile.ITile;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;

public class ToggleAlternateComparatorPacket extends TileGuiPacket {
	
	protected boolean alternateComparator;
	
	public ToggleAlternateComparatorPacket() {
		super();
	}
	
	public ToggleAlternateComparatorPacket(ITile tile) {
		super(tile);
		alternateComparator = tile.getAlternateComparator();
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		super.fromBytes(buf);
		alternateComparator = buf.readBoolean();
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		super.toBytes(buf);
		buf.writeBoolean(alternateComparator);
	}
	
	public static class Handler extends TileGuiPacket.Handler<ToggleAlternateComparatorPacket> {
		
		@Override
		protected void onPacket(ToggleAlternateComparatorPacket message, EntityPlayerMP player, TileEntity tile) {
			if (tile instanceof ITile) {
				ITile machine = (ITile) tile;
				machine.setAlternateComparator(message.alternateComparator);
				tile.markDirty();
			}
		}
	}
}
