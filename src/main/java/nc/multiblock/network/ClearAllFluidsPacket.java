package nc.multiblock.network;

import io.netty.buffer.ByteBuf;
import nc.multiblock.IMultiblockFluid;
import nc.multiblock.IMultiblockPart;
import nc.multiblock.MultiblockBase;
import nc.util.NCUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class ClearAllFluidsPacket implements IMessage {
	
	boolean messageValid;
	
	BlockPos pos;
	
	public ClearAllFluidsPacket() {
		messageValid = false;
	}
	
	public ClearAllFluidsPacket(BlockPos pos) {
		this.pos = pos;
		messageValid = true;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		try {
			pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
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
	}
	
	public static class Handler implements IMessageHandler<ClearAllFluidsPacket, IMessage> {
		
		@Override
		public IMessage onMessage(ClearAllFluidsPacket message, MessageContext ctx) {
			if (!message.messageValid && ctx.side != Side.SERVER) return null;
			FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> processMessage(message, ctx));
			return null;
		}
		
		void processMessage(ClearAllFluidsPacket message, MessageContext ctx) {
			TileEntity tile = ctx.getServerHandler().player.getServerWorld().getTileEntity(message.pos);
			if (tile == null) return;
			if(tile instanceof IMultiblockPart) {
				MultiblockBase multiblock = ((IMultiblockPart) tile).getMultiblock();
				if (multiblock instanceof IMultiblockFluid) ((IMultiblockFluid) multiblock).clearAllFluids();
			}
		}
	}
}
