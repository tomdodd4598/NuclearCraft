package nc.network.gui;

import io.netty.buffer.ByteBuf;
import nc.tile.ITile;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.*;
import net.minecraftforge.fml.relauncher.Side;

public class ToggleAlternateComparatorPacket implements IMessage {
	
	protected BlockPos pos;
	protected boolean alternateComparator;
	
	public ToggleAlternateComparatorPacket() {
		
	}
	
	public ToggleAlternateComparatorPacket(ITile machine) {
		pos = machine.getTilePos();
		alternateComparator = machine.getAlternateComparator();
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
		alternateComparator = buf.readBoolean();
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(pos.getX());
		buf.writeInt(pos.getY());
		buf.writeInt(pos.getZ());
		buf.writeBoolean(alternateComparator);
	}
	
	public static class Handler implements IMessageHandler<ToggleAlternateComparatorPacket, IMessage> {
		
		@Override
		public IMessage onMessage(ToggleAlternateComparatorPacket message, MessageContext ctx) {
			if (ctx.side == Side.SERVER) {
				FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> processMessage(message, ctx));
			}
			return null;
		}
		
		void processMessage(ToggleAlternateComparatorPacket message, MessageContext ctx) {
			EntityPlayerMP player = ctx.getServerHandler().player;
			World world = player.getServerWorld();
			if (!world.isBlockLoaded(message.pos) || !world.isBlockModifiable(player, message.pos)) {
				return;
			}
			TileEntity tile = world.getTileEntity(message.pos);
			if (tile instanceof ITile) {
				ITile machine = (ITile) tile;
				machine.setAlternateComparator(message.alternateComparator);
				tile.markDirty();
			}
		}
	}
}
