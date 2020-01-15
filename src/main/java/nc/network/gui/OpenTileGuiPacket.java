package nc.network.gui;

import io.netty.buffer.ByteBuf;
import nc.NuclearCraft;
import nc.tile.ITileGui;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.internal.FMLNetworkHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class OpenTileGuiPacket implements IMessage {
	
	boolean messageValid;
	
	BlockPos pos;
	
	public OpenTileGuiPacket() {
		messageValid = false;
	}
	
	public OpenTileGuiPacket(ITileGui machine) {
		pos = machine.getTilePos();
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
	
	public static class Handler implements IMessageHandler<OpenTileGuiPacket, IMessage> {
		
		@Override
		public IMessage onMessage(OpenTileGuiPacket message, MessageContext ctx) {
			if (!message.messageValid && ctx.side != Side.SERVER) return null;
			FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> processMessage(message, ctx));
			return null;
		}
		
		void processMessage(OpenTileGuiPacket message, MessageContext ctx) {
			EntityPlayerMP player = ctx.getServerHandler().player;
			TileEntity tile = player.getServerWorld().getTileEntity(message.pos);
			if (tile instanceof ITileGui) {
				FMLNetworkHandler.openGui(player, NuclearCraft.instance, ((ITileGui) tile).getGuiID(), player.getServerWorld(), message.pos.getX(), message.pos.getY(), message.pos.getZ());
				((ITileGui) tile).beginUpdatingPlayer(player);
			}
		}
	}
}
