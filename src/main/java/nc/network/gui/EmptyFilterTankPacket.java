package nc.network.gui;

import io.netty.buffer.ByteBuf;
import nc.tile.fluid.ITileFilteredFluid;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class EmptyFilterTankPacket implements IMessage {
	
	boolean messageValid;
	
	BlockPos pos;
	int tankNo;
	
	public EmptyFilterTankPacket() {
		messageValid = false;
	}
	
	public EmptyFilterTankPacket(ITileFilteredFluid machine, int tankNo) {
		pos = machine.getTilePos();
		this.tankNo = tankNo;
		messageValid = true;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		try {
			pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
			tankNo = buf.readInt();
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
		buf.writeInt(tankNo);
	}
	
	public static class Handler implements IMessageHandler<EmptyFilterTankPacket, IMessage> {
		
		@Override
		public IMessage onMessage(EmptyFilterTankPacket message, MessageContext ctx) {
			if (!message.messageValid && ctx.side != Side.SERVER) return null;
			FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> processMessage(message, ctx));
			return null;
		}
		
		void processMessage(EmptyFilterTankPacket message, MessageContext ctx) {
			TileEntity tile = ctx.getServerHandler().player.getServerWorld().getTileEntity(message.pos);
			if (tile instanceof ITileFilteredFluid) {
				ITileFilteredFluid machine = (ITileFilteredFluid) tile;
				machine.clearFilterTank(message.tankNo);
				tile.markDirty();
			}
		}
	}
}
