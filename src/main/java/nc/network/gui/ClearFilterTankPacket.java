package nc.network.gui;

import io.netty.buffer.ByteBuf;
import nc.tile.fluid.ITileFilteredFluid;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;

public class ClearFilterTankPacket extends TileGuiPacket {
	
	protected int tank;
	
	public ClearFilterTankPacket() {
		super();
	}
	
	public ClearFilterTankPacket(ITileFilteredFluid tile, int tank) {
		super(tile);
		this.tank = tank;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		super.fromBytes(buf);
		tank = buf.readInt();
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		super.toBytes(buf);
		buf.writeInt(tank);
	}
	
	public static class Handler extends TileGuiPacket.Handler<ClearFilterTankPacket> {
		
		@Override
		protected void onPacket(ClearFilterTankPacket message, EntityPlayerMP player, TileEntity tile) {
			if (tile instanceof ITileFilteredFluid) {
				ITileFilteredFluid machine = (ITileFilteredFluid) tile;
				machine.clearFilterTank(message.tank);
				tile.markDirty();
			}
		}
	}
}
