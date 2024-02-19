package nc.network.tile.processor;

import java.util.List;

import io.netty.buffer.ByteBuf;
import nc.network.tile.TileUpdatePacket;
import nc.tile.internal.fluid.Tank;
import nc.tile.internal.fluid.Tank.TankInfo;
import net.minecraft.util.math.BlockPos;

public abstract class ProcessorUpdatePacket extends TileUpdatePacket {
	
	public boolean isProcessing;
	public double time;
	public double baseProcessTime;
	public List<TankInfo> tankInfos;
	
	public ProcessorUpdatePacket() {
		super();
	}
	
	public ProcessorUpdatePacket(BlockPos pos, boolean isProcessing, double time, double baseProcessTime, List<Tank> tanks) {
		super(pos);
		this.isProcessing = isProcessing;
		this.time = time;
		this.baseProcessTime = baseProcessTime;
		tankInfos = TankInfo.getInfoList(tanks);
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		super.fromBytes(buf);
		isProcessing = buf.readBoolean();
		time = buf.readDouble();
		baseProcessTime = buf.readDouble();
		tankInfos = readTankInfos(buf);
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		super.toBytes(buf);
		buf.writeBoolean(isProcessing);
		buf.writeDouble(time);
		buf.writeDouble(baseProcessTime);
		writeTankInfos(buf, tankInfos);
	}
}
