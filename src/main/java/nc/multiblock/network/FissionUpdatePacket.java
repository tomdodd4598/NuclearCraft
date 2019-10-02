package nc.multiblock.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.BlockPos;

public abstract class FissionUpdatePacket extends MultiblockUpdatePacket {
	
	public boolean isReactorOn;
	public int clusterCount, fuelComponentCount;
	public long cooling, rawHeating, totalHeatMult, usefulPartCount, capacity, heat;
	public double effectiveHeating, meanHeatMult, totalEfficiency, meanEfficiency, sparsityEfficiencyMult, heatingOutputRate;
	
	public FissionUpdatePacket() {
		messageValid = false;
	}
	
	public FissionUpdatePacket(BlockPos pos, boolean isReactorOn, int clusterCount, long cooling, long rawHeating, double effectiveHeating, long totalHeatMult, double meanHeatMult, int fuelComponentCount, long usefulPartCount, double totalEfficiency, double meanEfficiency, double sparsityEfficiencyMult, long capacity, long heat, double heatingOutputRate) {
		this.pos = pos;
		this.isReactorOn = isReactorOn;
		this.clusterCount = clusterCount;
		this.cooling = cooling;
		this.rawHeating = rawHeating;
		this.effectiveHeating = effectiveHeating;
		this.totalHeatMult = totalHeatMult;
		this.meanHeatMult = meanHeatMult;
		this.fuelComponentCount = fuelComponentCount;
		this.usefulPartCount = usefulPartCount;
		this.totalEfficiency = totalEfficiency;
		this.meanEfficiency = meanEfficiency;
		this.sparsityEfficiencyMult = sparsityEfficiencyMult;
		this.capacity = capacity;
		this.heat = heat;
		this.heatingOutputRate = heatingOutputRate;
		
		messageValid = true;
	}
	
	@Override
	public void readMessage(ByteBuf buf) {
		pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
		isReactorOn = buf.readBoolean();
		clusterCount = buf.readInt();
		cooling = buf.readLong();
		rawHeating = buf.readLong();
		effectiveHeating = buf.readDouble();
		totalHeatMult = buf.readLong();
		meanHeatMult = buf.readDouble();
		fuelComponentCount = buf.readInt();
		usefulPartCount = buf.readLong();
		totalEfficiency = buf.readDouble();
		meanEfficiency = buf.readDouble();
		sparsityEfficiencyMult = buf.readDouble();
		capacity = buf.readLong();
		heat = buf.readLong();
		heatingOutputRate = buf.readDouble();
	}
	
	@Override
	public void writeMessage(ByteBuf buf) {
		buf.writeInt(pos.getX());
		buf.writeInt(pos.getY());
		buf.writeInt(pos.getZ());
		buf.writeBoolean(isReactorOn);
		buf.writeInt(clusterCount);
		buf.writeLong(cooling);
		buf.writeLong(rawHeating);
		buf.writeDouble(effectiveHeating);
		buf.writeLong(totalHeatMult);
		buf.writeDouble(meanHeatMult);
		buf.writeInt(fuelComponentCount);
		buf.writeLong(usefulPartCount);
		buf.writeDouble(totalEfficiency);
		buf.writeDouble(meanEfficiency);
		buf.writeDouble(sparsityEfficiencyMult);
		buf.writeLong(capacity);
		buf.writeLong(heat);
		buf.writeDouble(heatingOutputRate);
	}
}
