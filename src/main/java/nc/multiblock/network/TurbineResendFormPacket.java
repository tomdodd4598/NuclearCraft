package nc.multiblock.network;

import io.netty.buffer.ByteBuf;
import nc.multiblock.turbine.Turbine;
import nc.multiblock.turbine.tile.ITurbinePart;
import nc.network.PacketHandler;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.*;
import net.minecraftforge.fml.relauncher.Side;

public class TurbineResendFormPacket implements IMessage {
	
	boolean messageValid;
	
	BlockPos pos;
	
	public TurbineResendFormPacket() {
		messageValid = false;
	}
	
	public TurbineResendFormPacket(BlockPos pos) {
		this.pos = pos;
		messageValid = true;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		try {
			pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
		}
		catch (IndexOutOfBoundsException e) {
			e.printStackTrace();
			return;
		}
		messageValid = true;
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		if (!messageValid) {
			return;
		}
		buf.writeInt(pos.getX());
		buf.writeInt(pos.getY());
		buf.writeInt(pos.getZ());
	}
	
	public static class Handler implements IMessageHandler<TurbineResendFormPacket, IMessage> {
		
		@Override
		public IMessage onMessage(TurbineResendFormPacket message, MessageContext ctx) {
			if (!message.messageValid && ctx.side != Side.SERVER) {
				return null;
			}
			FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> processMessage(message, ctx));
			return null;
		}
		
		void processMessage(TurbineResendFormPacket message, MessageContext ctx) {
			EntityPlayerMP player = ctx.getServerHandler().player;
			TileEntity tile = player.getServerWorld().getTileEntity(message.pos);
			if (tile == null) {
				return;
			}
			if (tile instanceof ITurbinePart) {
				Turbine turbine = ((ITurbinePart) tile).getMultiblock();
				if (turbine != null) {
					PacketHandler.instance.sendTo(turbine.getFormPacket(), player);
				}
			}
		}
	}
}
