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

public class ToggleVoidUnusableFluidInputPacket implements IMessage {
	
	boolean messageValid;
	
	BlockPos pos;
	boolean voidUnusableFluidInput;
	int tankNumber;
	
	public ToggleVoidUnusableFluidInputPacket() {
		messageValid = false;
	}
	
	public ToggleVoidUnusableFluidInputPacket(ITileFluid machine, int tankNumber) {
		pos = machine.getTilePos();
		voidUnusableFluidInput = machine.getVoidUnusableFluidInput(tankNumber);
		this.tankNumber = tankNumber;
		messageValid = true;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		try {
			pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
			voidUnusableFluidInput = buf.readBoolean();
			tankNumber = buf.readInt();
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
		buf.writeBoolean(voidUnusableFluidInput);
		buf.writeInt(tankNumber);
	}
	
	public static class Handler implements IMessageHandler<ToggleVoidUnusableFluidInputPacket, IMessage> {
		
		@Override
		public IMessage onMessage(ToggleVoidUnusableFluidInputPacket message, MessageContext ctx) {
			if (!message.messageValid && ctx.side != Side.SERVER) return null;
			FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> processMessage(message, ctx));
			return null;
		}
		
		void processMessage(ToggleVoidUnusableFluidInputPacket message, MessageContext ctx) {
			TileEntity tile = ctx.getServerHandler().player.getServerWorld().getTileEntity(message.pos);
			if (tile instanceof ITileFluid) {
				ITileFluid machine = (ITileFluid) tile;
				machine.setVoidUnusableFluidInput(message.tankNumber, message.voidUnusableFluidInput);
				tile.markDirty();
			}
		}
	}
}
