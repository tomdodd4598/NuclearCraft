package nc.network.gui;

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

public class ToggleTanksEmptyUnusableButtonPacket implements IMessage {
	
	boolean messageValid;
	
	BlockPos pos;
	boolean emptyUnusable;
	
	public ToggleTanksEmptyUnusableButtonPacket() {
		messageValid = false;
	}
	
	public ToggleTanksEmptyUnusableButtonPacket(ITileFluid machine) {
		pos = machine.getTilePos();
		emptyUnusable = machine.getEmptyUnusableTankInputs();
		messageValid = true;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		try {
			pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
			emptyUnusable = buf.readBoolean();
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
		buf.writeBoolean(emptyUnusable);
	}
	
	public static class Handler implements IMessageHandler<ToggleTanksEmptyUnusableButtonPacket, IMessage> {
		
		@Override
		public IMessage onMessage(ToggleTanksEmptyUnusableButtonPacket message, MessageContext ctx) {
			if (!message.messageValid && ctx.side != Side.SERVER) return null;
			FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> processMessage(message, ctx));
			return null;
		}
		
		void processMessage(ToggleTanksEmptyUnusableButtonPacket message, MessageContext ctx) {
			TileEntity tile = ctx.getServerHandler().player.getServerWorld().getTileEntity(message.pos);
			if (tile == null) return;
			if(tile instanceof ITileFluid) {
				ITileFluid machine = (ITileFluid) tile;
				machine.setEmptyUnusableTankInputs(message.emptyUnusable);
				ctx.getServerHandler().player.getServerWorld().getTileEntity(message.pos).markDirty();
			}
		}
	}
}
