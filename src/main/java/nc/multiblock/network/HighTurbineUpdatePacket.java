package nc.multiblock.network;

import io.netty.buffer.ByteBuf;
import nc.multiblock.highTurbine.HighTurbine;
import nc.multiblock.highTurbine.tile.TileHighTurbineController;
import net.minecraft.util.math.BlockPos;

public class HighTurbineUpdatePacket extends MultiblockUpdatePacket {
	
	protected boolean isHighTurbineOn;
	
	public HighTurbineUpdatePacket() {
		messageValid = false;
	}
	
	public HighTurbineUpdatePacket(BlockPos pos, boolean isHighTurbineOn) {
		this.pos = pos;
		this.isHighTurbineOn = isHighTurbineOn;
		
		messageValid = true;
	}
	
	@Override
	public void readMessage(ByteBuf buf) {
		pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
		isHighTurbineOn = buf.readBoolean();
	}
	
	@Override
	public void writeMessage(ByteBuf buf) {
		buf.writeInt(pos.getX());
		buf.writeInt(pos.getY());
		buf.writeInt(pos.getZ());
		buf.writeBoolean(isHighTurbineOn);
	}
	
	public static class Handler extends MultiblockUpdatePacket.Handler<HighTurbineUpdatePacket, HighTurbine, TileHighTurbineController> {

		public Handler() {
			super(TileHighTurbineController.class);
		}
		
		@Override
		protected void onPacket(HighTurbineUpdatePacket message, HighTurbine reactor) {
			reactor.onPacket(message.isHighTurbineOn);
		}
	}
}
