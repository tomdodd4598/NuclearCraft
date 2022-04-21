package nc.network.multiblock;

import io.netty.buffer.ByteBuf;
import nc.multiblock.qComputer.tile.TileQuantumComputerQubit;
import nc.network.tile.TileUpdatePacket;
import net.minecraft.util.math.BlockPos;

public class QuantumComputerQubitRenderPacket extends TileUpdatePacket {
	
	public float measureColor;
	
	public QuantumComputerQubitRenderPacket() {
		
	}
	
	public QuantumComputerQubitRenderPacket(BlockPos pos, float measureColor) {
		this.pos = pos;
		this.measureColor = measureColor;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
		measureColor = buf.readFloat();
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(pos.getX());
		buf.writeInt(pos.getY());
		buf.writeInt(pos.getZ());
		buf.writeFloat(measureColor);
	}
	
	public static class Handler extends TileUpdatePacket.Handler<QuantumComputerQubitRenderPacket, TileQuantumComputerQubit> {
		
		@Override
		protected void onTileUpdatePacket(QuantumComputerQubitRenderPacket message, TileQuantumComputerQubit qubit) {
			qubit.onTileUpdatePacket(message);
		}
	}
}
