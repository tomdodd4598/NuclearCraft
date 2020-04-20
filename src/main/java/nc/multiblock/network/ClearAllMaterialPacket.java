package nc.multiblock.network;

import io.netty.buffer.ByteBuf;
import nc.multiblock.Multiblock;
import nc.multiblock.tile.ITileMultiblockPart;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class ClearAllMaterialPacket implements IMessage {
	
	boolean messageValid;
	
	BlockPos pos;
	
	public ClearAllMaterialPacket() {
		messageValid = false;
	}
	
	public ClearAllMaterialPacket(BlockPos pos) {
		this.pos = pos;
		messageValid = true;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		try {
			pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
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
	}
	
	public static class Handler implements IMessageHandler<ClearAllMaterialPacket, IMessage> {
		
		@Override
		public IMessage onMessage(ClearAllMaterialPacket message, MessageContext ctx) {
			if (!message.messageValid && ctx.side != Side.SERVER) return null;
			FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> processMessage(message, ctx));
			return null;
		}
		
		void processMessage(ClearAllMaterialPacket message, MessageContext ctx) {
			TileEntity tile = ctx.getServerHandler().player.getServerWorld().getTileEntity(message.pos);
			if (tile == null) return;
			if(tile instanceof ITileMultiblockPart) {
				Multiblock multiblock = ((ITileMultiblockPart)tile).getMultiblock();
				multiblock.clearAllMaterial();
			}
		}
	}
}
