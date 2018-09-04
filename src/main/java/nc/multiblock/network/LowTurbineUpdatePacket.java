package nc.multiblock.network;

import io.netty.buffer.ByteBuf;
import nc.multiblock.lowTurbine.LowTurbine;
import nc.multiblock.lowTurbine.tile.TileLowTurbineController;
import net.minecraft.util.math.BlockPos;

public class LowTurbineUpdatePacket extends MultiblockUpdatePacket {
	
	protected boolean isLowTurbineOn;
	
	public LowTurbineUpdatePacket() {
		messageValid = false;
	}
	
	public LowTurbineUpdatePacket(BlockPos pos, boolean isLowTurbineOn) {
		this.pos = pos;
		this.isLowTurbineOn = isLowTurbineOn;
		
		messageValid = true;
	}
	
	@Override
	public void readMessage(ByteBuf buf) {
		pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
		isLowTurbineOn = buf.readBoolean();
	}
	
	@Override
	public void writeMessage(ByteBuf buf) {
		buf.writeInt(pos.getX());
		buf.writeInt(pos.getY());
		buf.writeInt(pos.getZ());
		buf.writeBoolean(isLowTurbineOn);
	}
	
	public static class Handler extends MultiblockUpdatePacket.Handler<LowTurbineUpdatePacket, LowTurbine, TileLowTurbineController> {

		public Handler() {
			super(TileLowTurbineController.class);
		}
		
		@Override
		protected void onPacket(LowTurbineUpdatePacket message, LowTurbine reactor) {
			reactor.onPacket(message.isLowTurbineOn);
		}
	}
}
