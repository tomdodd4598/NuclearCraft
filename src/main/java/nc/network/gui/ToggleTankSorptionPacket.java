package nc.network.gui;

import io.netty.buffer.ByteBuf;
import nc.tile.fluid.ITileFluid;
import nc.tile.internal.fluid.TankSorption;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;

public class ToggleTankSorptionPacket extends TileGuiPacket {
	
	protected int side;
	protected int tank;
	protected int sorption;
	
	public ToggleTankSorptionPacket() {
		super();
	}
	
	public ToggleTankSorptionPacket(ITileFluid tile, EnumFacing side, int tank, TankSorption sorption) {
		super(tile);
		this.side = side.getIndex();
		this.tank = tank;
		this.sorption = sorption.ordinal();
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		super.fromBytes(buf);
		side = buf.readInt();
		tank = buf.readInt();
		sorption = buf.readInt();
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		super.toBytes(buf);
		buf.writeInt(side);
		buf.writeInt(tank);
		buf.writeInt(sorption);
	}
	
	public static class Handler extends TileGuiPacket.Handler<ToggleTankSorptionPacket> {
		
		@Override
		protected void onPacket(ToggleTankSorptionPacket message, EntityPlayerMP player, TileEntity tile) {
			if (tile instanceof ITileFluid machine) {
                machine.setTankSorption(EnumFacing.byIndex(message.side), message.tank, TankSorption.values()[message.sorption]);
				machine.markDirtyAndNotify(true);
			}
		}
	}
}
