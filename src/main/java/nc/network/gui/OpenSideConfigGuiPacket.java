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

public class OpenSideConfigGuiPacket implements IMessage {
	
	protected BlockPos pos;
	
	public OpenSideConfigGuiPacket() {
		
	}
	
	public OpenSideConfigGuiPacket(ITileGui<?> machine) {
		pos = machine.getTilePos();
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(pos.getX());
		buf.writeInt(pos.getY());
		buf.writeInt(pos.getZ());
	}
	
	public static class Handler implements IMessageHandler<OpenSideConfigGuiPacket, IMessage> {
		
		@Override
		public IMessage onMessage(OpenSideConfigGuiPacket message, MessageContext ctx) {
			if (ctx.side == Side.SERVER) {
				FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> processMessage(message, ctx));
			}
			return null;
		}
		
		void processMessage(OpenSideConfigGuiPacket message, MessageContext ctx) {
			EntityPlayerMP player = ctx.getServerHandler().player;
			World world = player.getServerWorld();
			if (!world.isBlockLoaded(message.pos) || !world.isBlockModifiable(player, message.pos)) {
				return;
			}
			TileEntity tile = world.getTileEntity(message.pos);
			if (tile instanceof ITileGui) {
				FMLNetworkHandler.openGui(player, NuclearCraft.instance, ((ITileGui<?>) tile).getGuiID() + 1000, player.getServerWorld(), message.pos.getX(), message.pos.getY(), message.pos.getZ());
				((ITileGui<?>) tile).addTileUpdatePacketListener(player);
			}
		}
	}
}
