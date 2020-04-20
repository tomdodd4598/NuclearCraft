package nc.network.tile;

import java.util.List;

import io.netty.buffer.ByteBuf;
import nc.tile.ITileGui;
import nc.tile.internal.fluid.Tank;
import nc.tile.internal.fluid.Tank.TankInfo;
import net.minecraft.util.math.BlockPos;

public class ProcessorUpdatePacket extends TileUpdatePacket {
	
	public boolean isProcessing;
	public double time;
	public int energyStored;
	public double baseProcessTime;
	public double baseProcessPower;
	public List<TankInfo> tanksInfo;
	
	public ProcessorUpdatePacket() {
		messageValid = false;
	}
	
	public ProcessorUpdatePacket(BlockPos pos, boolean isProcessing, double time, int energyStored, double baseProcessTime, double baseProcessPower, List<Tank> tanks) {
		this.pos = pos;
		this.isProcessing = isProcessing;
		this.time = time;
		this.energyStored = energyStored;
		this.baseProcessTime = baseProcessTime;
		this.baseProcessPower = baseProcessPower;
		tanksInfo = TankInfo.infoList(tanks);
		
		messageValid = true;
	}
	
	@Override
	public void readMessage(ByteBuf buf) {
		pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
		isProcessing = buf.readBoolean();
		time = buf.readDouble();
		energyStored = buf.readInt();
		baseProcessTime = buf.readDouble();
		baseProcessPower = buf.readDouble();
		byte numberOfTanks = buf.readByte();
		tanksInfo = TankInfo.readBuf(buf, numberOfTanks);
	}
	
	@Override
	public void writeMessage(ByteBuf buf) {
		buf.writeInt(pos.getX());
		buf.writeInt(pos.getY());
		buf.writeInt(pos.getZ());
		buf.writeBoolean(isProcessing);
		buf.writeDouble(time);
		buf.writeInt(energyStored);
		buf.writeDouble(baseProcessTime);
		buf.writeDouble(baseProcessPower);
		buf.writeByte(tanksInfo.size());
		for (TankInfo info : tanksInfo) info.writeBuf(buf);
	}
	
	public static class Handler extends TileUpdatePacket.Handler<ProcessorUpdatePacket, ITileGui> {
		
		@Override
		protected void onPacket(ProcessorUpdatePacket message, ITileGui processor) {
			processor.onGuiPacket(message);
		}
	}
}
