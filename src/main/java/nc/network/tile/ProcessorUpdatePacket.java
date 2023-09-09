package nc.network.tile;

import java.util.List;

import io.netty.buffer.ByteBuf;
import nc.tile.ITilePacket;
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
		super();
	}
	
	public ProcessorUpdatePacket(BlockPos pos, boolean isProcessing, double time, int energyStored, double baseProcessTime, double baseProcessPower, List<Tank> tanks) {
		super(pos);
		this.isProcessing = isProcessing;
		this.time = time;
		this.energyStored = energyStored;
		this.baseProcessTime = baseProcessTime;
		this.baseProcessPower = baseProcessPower;
		tanksInfo = TankInfo.infoList(tanks);
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		super.fromBytes(buf);
		isProcessing = buf.readBoolean();
		time = buf.readDouble();
		energyStored = buf.readInt();
		baseProcessTime = buf.readDouble();
		baseProcessPower = buf.readDouble();
		byte numberOfTanks = buf.readByte();
		tanksInfo = TankInfo.readBuf(buf, numberOfTanks);
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		super.toBytes(buf);
		buf.writeBoolean(isProcessing);
		buf.writeDouble(time);
		buf.writeInt(energyStored);
		buf.writeDouble(baseProcessTime);
		buf.writeDouble(baseProcessPower);
		buf.writeByte(tanksInfo.size());
		for (TankInfo info : tanksInfo) {
			info.writeBuf(buf);
		}
	}
	
	public static class Handler extends TileUpdatePacket.Handler<ProcessorUpdatePacket, ITilePacket<ProcessorUpdatePacket>> {
		
		@Override
		protected void onTileUpdatePacket(ProcessorUpdatePacket message, ITilePacket<ProcessorUpdatePacket> processor) {
			processor.onTileUpdatePacket(message);
		}
	}
}
