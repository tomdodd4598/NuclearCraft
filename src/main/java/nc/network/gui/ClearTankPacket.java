package nc.network.gui;

import io.netty.buffer.ByteBuf;
import nc.tile.fluid.ITileFluid;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;

public class ClearTankPacket extends TileGuiPacket {
	
	protected int tank;
	
	public ClearTankPacket() {
		super();
	}
	
	public ClearTankPacket(ITileFluid tile, int tank) {
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
	
	public static class Handler extends TileGuiPacket.Handler<ClearTankPacket> {
		
		@Override
		protected void onPacket(ClearTankPacket message, EntityPlayerMP player, TileEntity tile) {
			if (tile instanceof ITileFluid machine) {
                machine.clearTank(message.tank);
				tile.markDirty();
			}
		}
	}
}
