package nc.network.tile.multiblock;

import io.netty.buffer.ByteBuf;
import nc.network.tile.TileUpdatePacket;
import nc.tile.quantum.TileQuantumComputerQubit;
import net.minecraft.util.math.BlockPos;

public class QuantumComputerQubitRenderPacket extends TileUpdatePacket {
	
	public float measureColor;
	
	public QuantumComputerQubitRenderPacket() {
		super();
	}
	
	public QuantumComputerQubitRenderPacket(BlockPos pos, float measureColor) {
		super(pos);
		this.measureColor = measureColor;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		super.fromBytes(buf);
		measureColor = buf.readFloat();
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		super.toBytes(buf);
		buf.writeFloat(measureColor);
	}
	
	public static class Handler extends TileUpdatePacket.Handler<QuantumComputerQubitRenderPacket, TileQuantumComputerQubit> {}
}
