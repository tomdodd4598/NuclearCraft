package nc.network.multiblock;

import nc.multiblock.Multiblock;
import nc.multiblock.tile.IMultiblockController;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.*;
import net.minecraftforge.fml.relauncher.Side;

public abstract class MultiblockUpdatePacket implements IMessage {
	
	protected BlockPos pos;
	
	public MultiblockUpdatePacket() {
		
	}
	
	public static abstract class Handler<MESSAGE extends MultiblockUpdatePacket, MULTIBLOCK extends Multiblock, CONTROLLER extends IMultiblockController<MULTIBLOCK>> implements IMessageHandler<MESSAGE, IMessage> {
		
		protected final Class<CONTROLLER> controllerClass;
		
		public Handler(Class<CONTROLLER> controllerClass) {
			this.controllerClass = controllerClass;
		}
		
		@Override
		public IMessage onMessage(MESSAGE message, MessageContext ctx) {
			if (ctx.side == Side.CLIENT) {
				Minecraft.getMinecraft().addScheduledTask(() -> processMessage(message));
			}
			return null;
		}
		
		protected void processMessage(MESSAGE message) {
			TileEntity tile = Minecraft.getMinecraft().player.world.getTileEntity(message.pos);
			if (controllerClass.isInstance(tile)) {
				CONTROLLER controller = (CONTROLLER) tile;
				if (controller.getMultiblock() != null) {
					onPacket(message, controller.getMultiblock());
				}
			}
		}
		
		protected void onPacket(MESSAGE message, MULTIBLOCK multiblock) {
			multiblock.onPacket(message);
		}
	}
}
