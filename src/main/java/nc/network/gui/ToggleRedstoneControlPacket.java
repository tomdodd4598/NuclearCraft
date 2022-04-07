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

public class ToggleRedstoneControlPacket implements IMessage {
	
	protected BlockPos pos;
	protected boolean redstoneControl;
	
	public ToggleRedstoneControlPacket() {
		
	}
	
	public ToggleRedstoneControlPacket(ITile machine) {
		pos = machine.getTilePos();
		redstoneControl = machine.getRedstoneControl();
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
		redstoneControl = buf.readBoolean();
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(pos.getX());
		buf.writeInt(pos.getY());
		buf.writeInt(pos.getZ());
		buf.writeBoolean(redstoneControl);
	}
	
	public static class Handler implements IMessageHandler<ToggleRedstoneControlPacket, IMessage> {
		
		@Override
		public IMessage onMessage(ToggleRedstoneControlPacket message, MessageContext ctx) {
			if (ctx.side == Side.SERVER) {
				FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> processMessage(message, ctx));
			}
			return null;
		}
		
		void processMessage(ToggleRedstoneControlPacket message, MessageContext ctx) {
			EntityPlayerMP player = ctx.getServerHandler().player;
			World world = player.getServerWorld();
			if (!world.isBlockLoaded(message.pos) || !world.isBlockModifiable(player, message.pos)) {
				return;
			}
			TileEntity tile = world.getTileEntity(message.pos);
			if (tile instanceof ITile) {
				ITile machine = (ITile) tile;
				machine.setRedstoneControl(message.redstoneControl);
				tile.markDirty();
			}
		}
	}
}
