package nc.network.gui;

import io.netty.buffer.ByteBuf;
import nc.tile.fluid.ITileFluid;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class ToggleInputTanksSeparatedPacket implements IMessage {
	
	boolean messageValid;
	
	BlockPos pos;
	boolean inputTanksSeparated;
	
	public ToggleInputTanksSeparatedPacket() {
		messageValid = false;
	}
	
	public ToggleInputTanksSeparatedPacket(ITileFluid machine) {
		pos = machine.getTilePos();
		inputTanksSeparated = machine.getInputTanksSeparated();
		messageValid = true;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		try {
			pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
			inputTanksSeparated = buf.readBoolean();
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
		buf.writeBoolean(inputTanksSeparated);
	}
	
	public static class Handler implements IMessageHandler<ToggleInputTanksSeparatedPacket, IMessage> {
		
		@Override
		public IMessage onMessage(ToggleInputTanksSeparatedPacket message, MessageContext ctx) {
			if (!message.messageValid && ctx.side != Side.SERVER) return null;
			FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> processMessage(message, ctx));
			return null;
		}
		
		void processMessage(ToggleInputTanksSeparatedPacket message, MessageContext ctx) {
			TileEntity tile = ctx.getServerHandler().player.getServerWorld().getTileEntity(message.pos);
			if (tile instanceof ITileFluid) {
				ITileFluid machine = (ITileFluid) tile;
				machine.setInputTanksSeparated(message.inputTanksSeparated);
				tile.markDirty();
			}
		}
	}
}
