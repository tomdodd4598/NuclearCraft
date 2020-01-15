package nc.network.tile;

import java.util.List;

import io.netty.buffer.ByteBuf;
import nc.tile.ITileGui;
import nc.tile.internal.fluid.Tank;
import nc.tile.internal.fluid.Tank.TankInfo;
import net.minecraft.util.math.BlockPos;

public class ProcessorUpdatePacket extends TileUpdatePacket {
	
	public double time;
	public int energyStored;
	public double baseProcessTime;
	public double baseProcessPower;
	public byte numberOfTanks;
	public List<TankInfo> tanksInfo;
	
	public ProcessorUpdatePacket() {
		messageValid = false;
	}
	
	public ProcessorUpdatePacket(BlockPos pos, double time, int energyStored, double baseProcessTime, double baseProcessPower, List<Tank> tanks) {
		this.pos = pos;
		this.time = time;
		this.energyStored = energyStored;
		this.baseProcessTime = baseProcessTime;
		this.baseProcessPower = baseProcessPower;
		numberOfTanks = (byte) tanks.size();
		tanksInfo = TankInfo.infoList(tanks);
		
		messageValid = true;
	}
	
	@Override
	public void readMessage(ByteBuf buf) {
		pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
		time = buf.readDouble();
		energyStored = buf.readInt();
		baseProcessTime = buf.readDouble();
		baseProcessPower = buf.readDouble();
		numberOfTanks = buf.readByte();
		tanksInfo = TankInfo.readBuf(buf, numberOfTanks);
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
		buf.writeByte(numberOfTanks);
		for (TankInfo info : tanksInfo) info.writeBuf(buf);
	}
	
	public static class Handler extends TileUpdatePacket.Handler<ProcessorUpdatePacket, ITileGui> {
		
		@Override
		protected void onPacket(ProcessorUpdatePacket message, ITileGui processor) {
			processor.onGuiPacket(message);
		}
	}
}
