package nc.multiblock.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.BlockPos;

public abstract class FissionUpdatePacket extends MultiblockUpdatePacket {
	
	public boolean isReactorOn;
	public int clusterCount, fuelComponentCount;
	public long cooling, heating, totalHeatMult, usefulPartCount, capacity, heat;
	public double meanHeatMult, totalEfficiency, meanEfficiency, sparsityEfficiencyMult;
	
	public FissionUpdatePacket() {
		messageValid = false;
	}
	
	public FissionUpdatePacket(BlockPos pos, boolean isReactorOn, int clusterCount, long cooling, long heating, long totalHeatMult, double meanHeatMult, int fuelComponentCount, long usefulPartCount, double totalEfficiency, double meanEfficiency, double sparsityEfficiencyMult, long capacity, long heat) {
		this.pos = pos;
		this.isReactorOn = isReactorOn;
		this.clusterCount = clusterCount;
		this.cooling = cooling;
		this.heating = heating;
		this.totalHeatMult = totalHeatMult;
		this.meanHeatMult = meanHeatMult;
		this.fuelComponentCount = fuelComponentCount;
		this.usefulPartCount = usefulPartCount;
		this.totalEfficiency = totalEfficiency;
		this.meanEfficiency = meanEfficiency;
		this.sparsityEfficiencyMult = sparsityEfficiencyMult;
		this.capacity = capacity;
		this.heat = heat;
		
		messageValid = true;
	}
	
	@Override
	public void readMessage(ByteBuf buf) {
		pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
		isReactorOn = buf.readBoolean();
		clusterCount = buf.readInt();
		cooling = buf.readLong();
		heating = buf.readLong();
		totalHeatMult = buf.readLong();
		meanHeatMult = buf.readDouble();
		fuelComponentCount = buf.readInt();
		usefulPartCount = buf.readLong();
		totalEfficiency = buf.readDouble();
		meanEfficiency = buf.readDouble();
		sparsityEfficiencyMult = buf.readDouble();
		capacity = buf.readLong();
		heat = buf.readLong();
	}
	
	@Override
	public void writeMessage(ByteBuf buf) {
		buf.writeInt(pos.getX());
		buf.writeInt(pos.getY());
		buf.writeInt(pos.getZ());
		buf.writeBoolean(isReactorOn);
		buf.writeInt(clusterCount);
		buf.writeLong(cooling);
		buf.writeLong(heating);
		buf.writeLong(totalHeatMult);
		buf.writeDouble(meanHeatMult);
		buf.writeInt(fuelComponentCount);
		buf.writeLong(usefulPartCount);
		buf.writeDouble(totalEfficiency);
		buf.writeDouble(meanEfficiency);
		buf.writeDouble(sparsityEfficiencyMult);
		buf.writeLong(capacity);
		buf.writeLong(heat);
	}
}
