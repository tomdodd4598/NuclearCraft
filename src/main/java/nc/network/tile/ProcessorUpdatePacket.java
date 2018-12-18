package nc.network.tile;

import io.netty.buffer.ByteBuf;
import nc.tile.IGui;
import net.minecraft.util.math.BlockPos;

public class ProcessorUpdatePacket extends TileUpdatePacket {
	
	public double time;
	public int energyStored;
	public double baseProcessTime;
	public double baseProcessPower;
	
	public ProcessorUpdatePacket() {
		messageValid = false;
	}
	
	public ProcessorUpdatePacket(BlockPos pos, double time, int energyStored, double baseProcessTime, double baseProcessPower) {
		this.pos = pos;
		this.time = time;
		this.energyStored = energyStored;
		this.baseProcessTime = baseProcessTime;
		this.baseProcessPower = baseProcessPower;
		
		messageValid = true;
	}
	
	@Override
	public void readMessage(ByteBuf buf) {
		pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
		time = buf.readDouble();
		energyStored = buf.readInt();
		baseProcessTime = buf.readDouble();
		baseProcessPower = buf.readDouble();
	}
	
	@Override
	public void writeMessage(ByteBuf buf) {
		buf.writeInt(pos.getX());
		buf.writeInt(pos.getY());
		buf.writeInt(pos.getZ());
		buf.writeDouble(time);
		buf.writeInt(energyStored);
		buf.writeDouble(baseProcessTime);
		buf.writeDouble(baseProcessPower);
	}
	
	public static class Handler extends TileUpdatePacket.Handler<ProcessorUpdatePacket, IGui> {
		
		@Override
		protected void onPacket(ProcessorUpdatePacket message, IGui processor) {
			processor.onGuiPacket(message);
		}
	}
}
