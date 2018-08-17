package nc.network;

import io.netty.buffer.ByteBuf;
import nc.tile.generator.TileFusionCore;
import nc.util.NCUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class FusionUpdatePacket implements IMessage {
	
	boolean messageValid;
	
	protected BlockPos pos;
	protected boolean isProcessing;
	protected double efficiency;
	protected boolean computerActivated;
	
	public FusionUpdatePacket() {
		messageValid = false;
	}
	
	public FusionUpdatePacket(BlockPos pos, boolean isProcessing, double efficiency, boolean computerActivated) {
		this.pos = pos;
		this.isProcessing = isProcessing;
		this.efficiency = efficiency;
		this.computerActivated = computerActivated;
		
		messageValid = true;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		try {
			pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
			isProcessing = buf.readBoolean();
			efficiency = buf.readDouble();
			computerActivated = buf.readBoolean();
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
		buf.writeBoolean(isProcessing);
		buf.writeDouble(efficiency);
		buf.writeBoolean(computerActivated);
	}
	
	public static class Handler implements IMessageHandler<FusionUpdatePacket, IMessage> {

		@Override
		public IMessage onMessage(FusionUpdatePacket message, MessageContext ctx) {
			if (!message.messageValid && ctx.side != Side.CLIENT) return null;
			Minecraft.getMinecraft().addScheduledTask(() -> processMessage(message));
			return null;
		}
		
		void processMessage(FusionUpdatePacket message) {
			TileEntity tile = Minecraft.getMinecraft().player.world.getTileEntity(message.pos);
			if (!(tile instanceof TileFusionCore)) return;
			
			((TileFusionCore)tile).onFusionPacket(message.isProcessing, message.efficiency, message.computerActivated);
		}
	}
}
