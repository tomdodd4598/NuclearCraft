package nc.network.gui;

import io.netty.buffer.ByteBuf;
import nc.tile.fluid.ITileFluid;
import nc.tile.internal.fluid.TankSorption;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;

public class ResetTankSorptionsPacket extends TileGuiPacket {
	
	protected int tank;
	protected boolean defaults;
	
	public ResetTankSorptionsPacket() {
		super();
	}
	
	public ResetTankSorptionsPacket(ITileFluid tile, int tank, boolean defaults) {
		super(tile);
		this.tank = tank;
		this.defaults = defaults;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		super.fromBytes(buf);
		tank = buf.readInt();
		defaults = buf.readBoolean();
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		super.toBytes(buf);
		buf.writeInt(tank);
		buf.writeBoolean(defaults);
	}
	
	public static class Handler extends TileGuiPacket.Handler<ResetTankSorptionsPacket> {
		
		@Override
		protected void onPacket(ResetTankSorptionsPacket message, EntityPlayerMP player, TileEntity tile) {
			if (tile instanceof ITileFluid) {
				ITileFluid machine = (ITileFluid) tile;
				for (EnumFacing side : EnumFacing.VALUES) {
					if (message.defaults) {
						machine.setTankSorption(side, message.tank, machine.getFluidConnection(side).getDefaultTankSorption(message.tank));
					}
					else {
						machine.setTankSorption(side, message.tank, TankSorption.NON);
					}
				}
				machine.markDirtyAndNotify(true);
			}
		}
	}
}
