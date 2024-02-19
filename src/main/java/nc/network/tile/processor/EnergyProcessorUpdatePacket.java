package nc.network.tile.processor;

import java.util.List;

import io.netty.buffer.ByteBuf;
import nc.tile.ITilePacket;
import nc.tile.internal.fluid.Tank;
import net.minecraft.util.math.BlockPos;

public class EnergyProcessorUpdatePacket extends ProcessorUpdatePacket {
	
	public double baseProcessPower;
	public long energyStored;
	
	public EnergyProcessorUpdatePacket() {
		super();
	}
	
	public EnergyProcessorUpdatePacket(BlockPos pos, boolean isProcessing, double time, double baseProcessTime, List<Tank> tanks, double baseProcessPower, long energyStored) {
		super(pos, isProcessing, time, baseProcessTime, tanks);
		this.baseProcessPower = baseProcessPower;
		this.energyStored = energyStored;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		super.fromBytes(buf);
		baseProcessPower = buf.readDouble();
		energyStored = buf.readLong();
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		super.toBytes(buf);
		buf.writeDouble(baseProcessPower);
		buf.writeLong(energyStored);
	}
	
	public static class Handler extends ProcessorUpdatePacket.Handler<EnergyProcessorUpdatePacket, ITilePacket<EnergyProcessorUpdatePacket>> {}
}
