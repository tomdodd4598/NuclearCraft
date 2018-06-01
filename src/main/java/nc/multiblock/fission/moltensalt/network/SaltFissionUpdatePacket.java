package nc.multiblock.fission.moltensalt.network;

import io.netty.buffer.ByteBuf;
import nc.multiblock.fission.moltensalt.SaltFissionReactor;
import nc.multiblock.fission.moltensalt.tile.TileSaltFissionController;
import nc.util.NCUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class SaltFissionUpdatePacket implements IMessage {
	
	boolean messageValid;
	
	protected BlockPos pos;
	protected boolean isReactorOn;
	protected double cooling, heating, efficiency, heatMult, coolingRate;
	protected long capacity, heat;
	
	public SaltFissionUpdatePacket() {
		messageValid = false;
	}
	
	public SaltFissionUpdatePacket(BlockPos pos, boolean isReactorOn, double cooling, double heating, double efficiency, double heatMult, double coolingRate, long capacity, long heat) {
		this.pos = pos;
		this.isReactorOn = isReactorOn;
		this.cooling = cooling;
		this.heating = heating;
		this.efficiency = efficiency;
		this.heatMult = heatMult;
		this.coolingRate = coolingRate;
		this.capacity = capacity;
		this.heat = heat;
		
		messageValid = true;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		try {
			pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
			isReactorOn = buf.readBoolean();
			cooling = buf.readDouble();
			heating = buf.readDouble();
			efficiency = buf.readDouble();
			heatMult = buf.readDouble();
			coolingRate = buf.readDouble();
			capacity = buf.readLong();
			heat = buf.readLong();
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
		buf.writeBoolean(isReactorOn);
		buf.writeDouble(cooling);
		buf.writeDouble(heating);
		buf.writeDouble(efficiency);
		buf.writeDouble(heatMult);
		buf.writeDouble(coolingRate);
		buf.writeLong(capacity);
		buf.writeLong(heat);
	}
	
	public static class Handler implements IMessageHandler<SaltFissionUpdatePacket, IMessage> {

		@Override
		public IMessage onMessage(SaltFissionUpdatePacket message, MessageContext ctx) {
			if (!message.messageValid && ctx.side != Side.CLIENT) return null;
			Minecraft.getMinecraft().addScheduledTask(() -> processMessage(message));
			return null;
		}
		
		void processMessage(SaltFissionUpdatePacket message) {
			TileEntity tile = Minecraft.getMinecraft().player.world.getTileEntity(message.pos);
			if (!(tile instanceof TileSaltFissionController)) return;
			if (!(((TileSaltFissionController)tile).getMultiblockController() instanceof SaltFissionReactor)) return;
			
			SaltFissionReactor reactor = (SaltFissionReactor) ((TileSaltFissionController)tile).getMultiblockController();
			
			reactor.onPacket(message.isReactorOn, message.cooling, message.heating, message.efficiency, message.heatMult, message.coolingRate, message.capacity, message.heat);
		}
	}
}
