package nc.network.gui;

import io.netty.buffer.ByteBuf;
import nc.tile.ITile;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;

public class ToggleRedstoneControlPacket extends TileGuiPacket {
	
	protected boolean redstoneControl;
	
	public ToggleRedstoneControlPacket() {
		super();
	}
	
	public ToggleRedstoneControlPacket(ITile tile) {
		super(tile);
		redstoneControl = tile.getRedstoneControl();
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		super.fromBytes(buf);
		redstoneControl = buf.readBoolean();
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		super.toBytes(buf);
		buf.writeBoolean(redstoneControl);
	}
	
	public static class Handler extends TileGuiPacket.Handler<ToggleRedstoneControlPacket> {
		
		@Override
		protected void onPacket(ToggleRedstoneControlPacket message, EntityPlayerMP player, TileEntity tile) {
			if (tile instanceof ITile) {
				ITile machine = (ITile) tile;
				machine.setRedstoneControl(message.redstoneControl);
				tile.markDirty();
			}
		}
	}
}
