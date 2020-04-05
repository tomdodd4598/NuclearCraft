package nc.multiblock.network;

import io.netty.buffer.ByteBuf;
import nc.multiblock.Multiblock;
import nc.multiblock.tile.TileMultiblockPart;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public abstract class MultiblockUpdatePacket implements IMessage {
	
	protected boolean messageValid;
	protected BlockPos pos;
	
	public MultiblockUpdatePacket() {
		messageValid = false;
	}
	
	public abstract void readMessage(ByteBuf buf);
	
	public abstract void writeMessage(ByteBuf buf);
	
	@Override
	public void fromBytes(ByteBuf buf) {
		try {
			readMessage(buf);
		} catch (IndexOutOfBoundsException e) {
			e.printStackTrace();
			return;
		}
		messageValid = true;
	}

	@Override
	public void toBytes(ByteBuf buf) {
		if (!messageValid) return;
		writeMessage(buf);
	}
	
	public static abstract class Handler<MESSAGE extends MultiblockUpdatePacket, MULTIBLOCK extends Multiblock, CONTROLLER extends TileMultiblockPart<MULTIBLOCK>> implements IMessageHandler<MESSAGE, IMessage> {
		
		protected final Class<CONTROLLER> controllerClass;
		
		public Handler(Class<CONTROLLER> controllerClass) {
			this.controllerClass = controllerClass;
		}
		
		@Override
		public IMessage onMessage(MESSAGE message, MessageContext ctx) {
			if (!message.messageValid && ctx.side != Side.CLIENT) return null;
			Minecraft.getMinecraft().addScheduledTask(() -> processMessage(message));
			return null;
		}
		
		protected void processMessage(MESSAGE message) {
			TileEntity tile = Minecraft.getMinecraft().player.world.getTileEntity(message.pos);
			if (controllerClass.isInstance(tile)) {
				CONTROLLER controller = (CONTROLLER) tile;
				if (controller.getMultiblock() != null) onPacket(message, controller.getMultiblock());
			}
		}
		
		protected void onPacket(MESSAGE message, MULTIBLOCK multiblock) {
			multiblock.onPacket(message);
		}
	}
}
