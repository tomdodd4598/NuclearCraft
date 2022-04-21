package nc.network.multiblock;

import nc.multiblock.*;
import nc.multiblock.tile.*;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.*;
import net.minecraftforge.fml.relauncher.Side;

public abstract class MultiblockUpdatePacket implements IMessage {
	
	protected BlockPos pos;
	
	public MultiblockUpdatePacket() {
		
	}
	
	public static abstract class Handler<MULTIBLOCK extends Multiblock<MULTIBLOCK, T> & IPacketMultiblock<MULTIBLOCK, T, PACKET>, T extends ITileMultiblockPart<MULTIBLOCK, T>, PACKET extends MultiblockUpdatePacket, CONTROLLER extends IMultiblockController<MULTIBLOCK, T, PACKET, CONTROLLER>, P extends MultiblockUpdatePacket> implements IMessageHandler<P, IMessage> {
		
		protected final Class<CONTROLLER> controllerClass;
		
		public Handler(Class<CONTROLLER> controllerClass) {
			this.controllerClass = controllerClass;
		}
		
		@Override
		public IMessage onMessage(P message, MessageContext ctx) {
			if (ctx.side == Side.CLIENT) {
				Minecraft.getMinecraft().addScheduledTask(() -> processMessage(message));
			}
			return null;
		}
		
		protected void processMessage(P message) {
			TileEntity tile = Minecraft.getMinecraft().player.world.getTileEntity(message.pos);
			if (controllerClass.isInstance(tile)) {
				CONTROLLER controller = controllerClass.cast(tile);
				if (controller.getMultiblock() != null) {
					onPacket(message, controller.getMultiblock());
				}
			}
		}
		
		protected abstract void onPacket(P message, MULTIBLOCK multiblock);
	}
}
