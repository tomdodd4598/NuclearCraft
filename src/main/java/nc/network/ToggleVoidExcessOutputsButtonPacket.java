package nc.network;

import io.netty.buffer.ByteBuf;
import nc.tile.fluid.ITileFluid;
import nc.util.NCUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class ToggleVoidExcessOutputsButtonPacket implements IMessage {
	
	boolean messageValid;
	
	BlockPos pos;
	boolean voidExcessOutputs;
	
	public ToggleVoidExcessOutputsButtonPacket() {
		messageValid = false;
	}
	
	public ToggleVoidExcessOutputsButtonPacket(ITileFluid machine) {
		pos = machine.getPos();
		voidExcessOutputs = machine.getVoidExcessOutputs();
		messageValid = true;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		try {
			pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
			voidExcessOutputs = buf.readBoolean();
		} catch (IndexOutOfBoundsException ioe) {
			NCUtil.getLogger().catching(ioe);
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
		buf.writeBoolean(voidExcessOutputs);
	}
	
	public static class Handler implements IMessageHandler<ToggleVoidExcessOutputsButtonPacket, IMessage> {
		
		@Override
		public IMessage onMessage(ToggleVoidExcessOutputsButtonPacket message, MessageContext ctx) {
			if (!message.messageValid && ctx.side != Side.SERVER) return null;
			FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> processMessage(message, ctx));
			return null;
		}
		
		void processMessage(ToggleVoidExcessOutputsButtonPacket message, MessageContext ctx) {
			TileEntity tile = ctx.getServerHandler().player.getServerWorld().getTileEntity(message.pos);
			if (tile == null) return;
			if(tile instanceof ITileFluid) {
				ITileFluid machine = (ITileFluid) tile;
				machine.setVoidExcessOutputs(message.voidExcessOutputs);
				ctx.getServerHandler().player.getServerWorld().getTileEntity(message.pos).markDirty();
			}
		}
	}
}
