package nc.network.gui;

import io.netty.buffer.ByteBuf;
import nc.NuclearCraft;
import nc.tile.ITileGui;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.internal.FMLNetworkHandler;
import net.minecraftforge.fml.common.network.simpleimpl.*;
import net.minecraftforge.fml.relauncher.Side;

public class OpenGuiPacket implements IMessage {
	
	private BlockPos pos;
	private int guiID = -1;
	
	public OpenGuiPacket() {
		
	}
	
	public OpenGuiPacket(BlockPos pos, int guiID) {
		this.pos = pos;
		this.guiID = guiID;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
		guiID = buf.readInt();
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(pos.getX());
		buf.writeInt(pos.getY());
		buf.writeInt(pos.getZ());
		buf.writeInt(guiID);
	}
	
	public static class Handler implements IMessageHandler<OpenGuiPacket, IMessage> {
		
		@Override
		public IMessage onMessage(OpenGuiPacket message, MessageContext ctx) {
			if (ctx.side == Side.SERVER) {
				FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> processMessage(message, ctx));
			}
			return null;
		}
		
		void processMessage(OpenGuiPacket message, MessageContext ctx) {
			EntityPlayerMP player = ctx.getServerHandler().player;
			World world = player.getServerWorld();
			if (!world.isBlockLoaded(message.pos) || !world.isBlockModifiable(player, message.pos)) {
				return;
			}
			TileEntity tile = world.getTileEntity(message.pos);
			FMLNetworkHandler.openGui(player, NuclearCraft.instance, message.guiID, player.getServerWorld(), message.pos.getX(), message.pos.getY(), message.pos.getZ());
			if (tile instanceof ITileGui) {
				((ITileGui) tile).beginUpdatingPlayer(player);
			}
		}
	}
}
