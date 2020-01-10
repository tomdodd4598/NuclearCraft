package nc.network.gui;

import io.netty.buffer.ByteBuf;
import nc.tile.fluid.ITileFluid;
import nc.tile.internal.fluid.TankSorption;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class ResetTankSorptionsPacket implements IMessage {
	
	boolean messageValid;
	
	BlockPos pos;
	int tank;
	boolean defaults;
	
	public ResetTankSorptionsPacket() {
		messageValid = false;
	}
	
	public ResetTankSorptionsPacket(ITileFluid machine, int tank, boolean defaults) {
		pos = machine.getTilePos();
		this.tank = tank;
		this.defaults = defaults;
		messageValid = true;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		try {
			pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
			tank = buf.readInt();
			defaults = buf.readBoolean();
		} catch (IndexOutOfBoundsException e) {
			e.printStackTrace();
			return;
		}
		messageValid = true;
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		if (!messageValid) return;
		buf.writeInt(pos.getX());
		buf.writeInt(pos.getY());
		buf.writeInt(pos.getZ());
		buf.writeInt(tank);
		buf.writeBoolean(defaults);
	}
	
	public static class Handler implements IMessageHandler<ResetTankSorptionsPacket, IMessage> {
		
		@Override
		public IMessage onMessage(ResetTankSorptionsPacket message, MessageContext ctx) {
			if (!message.messageValid && ctx.side != Side.SERVER) return null;
			FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> processMessage(message, ctx));
			return null;
		}
		
		void processMessage(ResetTankSorptionsPacket message, MessageContext ctx) {
			TileEntity tile = ctx.getServerHandler().player.getServerWorld().getTileEntity(message.pos);
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
				machine.markDirtyAndNotify();
			}
		}
	}
}
