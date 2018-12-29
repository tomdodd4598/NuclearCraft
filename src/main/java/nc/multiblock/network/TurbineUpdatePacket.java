package nc.multiblock.network;

import io.netty.buffer.ByteBuf;
import nc.multiblock.turbine.Turbine;
import nc.multiblock.turbine.tile.TileTurbineController;
import net.minecraft.util.math.BlockPos;

public class TurbineUpdatePacket extends MultiblockUpdatePacket {
	
	protected boolean isTurbineOn;
	
	public TurbineUpdatePacket() {
		messageValid = false;
	}
	
	public TurbineUpdatePacket(BlockPos pos, boolean isTurbineOn) {
		this.pos = pos;
		this.isTurbineOn = isTurbineOn;
		
		messageValid = true;
	}
	
	@Override
	public void readMessage(ByteBuf buf) {
		pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
		isTurbineOn = buf.readBoolean();
	}
	
	@Override
	public void writeMessage(ByteBuf buf) {
		buf.writeInt(pos.getX());
		buf.writeInt(pos.getY());
		buf.writeInt(pos.getZ());
		buf.writeBoolean(isTurbineOn);
	}
	
	public static class Handler extends MultiblockUpdatePacket.Handler<TurbineUpdatePacket, Turbine, TileTurbineController> {

		public Handler() {
			super(TileTurbineController.class);
		}
		
		@Override
		protected void onPacket(TurbineUpdatePacket message, Turbine reactor) {
			reactor.onPacket(message.isTurbineOn);
		}
	}
}
