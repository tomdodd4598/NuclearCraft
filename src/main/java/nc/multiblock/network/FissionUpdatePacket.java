package nc.multiblock.network;

import io.netty.buffer.ByteBuf;
import nc.tile.internal.heat.HeatBuffer;
import net.minecraft.util.math.BlockPos;

public abstract class FissionUpdatePacket extends MultiblockUpdatePacket {
	
	public boolean isReactorOn;
	public long cooling, rawHeating, totalHeatMult, usefulPartCount, heatStored, heatCapacity;
	public int clusterCount, fuelComponentCount;
	public double meanHeatMult, totalEfficiency, meanEfficiency, sparsityEfficiencyMult;
	
	public FissionUpdatePacket() {
		messageValid = false;
	}
	
	public FissionUpdatePacket(BlockPos pos, boolean isReactorOn, HeatBuffer heatBuffer, int clusterCount, long cooling, long rawHeating, long totalHeatMult, double meanHeatMult, int fuelComponentCount, long usefulPartCount, double totalEfficiency, double meanEfficiency, double sparsityEfficiencyMult) {
		this.pos = pos;
		this.isReactorOn = isReactorOn;
		heatStored = heatBuffer.getHeatStored();
		heatCapacity = heatBuffer.getHeatCapacity();
		this.clusterCount = clusterCount;
		this.cooling = cooling;
		this.rawHeating = rawHeating;
		this.totalHeatMult = totalHeatMult;
		this.meanHeatMult = meanHeatMult;
		this.fuelComponentCount = fuelComponentCount;
		this.usefulPartCount = usefulPartCount;
		this.totalEfficiency = totalEfficiency;
		this.meanEfficiency = meanEfficiency;
		this.sparsityEfficiencyMult = sparsityEfficiencyMult;
		
		messageValid = true;
	}
	
	@Override
	public void readMessage(ByteBuf buf) {
		pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
		isReactorOn = buf.readBoolean();
		heatStored = buf.readLong();
		heatCapacity = buf.readLong();
		clusterCount = buf.readInt();
		cooling = buf.readLong();
		rawHeating = buf.readLong();
		totalHeatMult = buf.readLong();
		meanHeatMult = buf.readDouble();
		fuelComponentCount = buf.readInt();
		usefulPartCount = buf.readLong();
		totalEfficiency = buf.readDouble();
		meanEfficiency = buf.readDouble();
		sparsityEfficiencyMult = buf.readDouble();
	}
	
	@Override
	public void writeMessage(ByteBuf buf) {
		buf.writeInt(pos.getX());
		buf.writeInt(pos.getY());
		buf.writeInt(pos.getZ());
		buf.writeBoolean(isReactorOn);
		buf.writeLong(heatStored);
		buf.writeLong(heatCapacity);
		buf.writeInt(clusterCount);
		buf.writeLong(cooling);
		buf.writeLong(rawHeating);
		buf.writeLong(totalHeatMult);
		buf.writeDouble(meanHeatMult);
		buf.writeInt(fuelComponentCount);
		buf.writeLong(usefulPartCount);
		buf.writeDouble(totalEfficiency);
		buf.writeDouble(meanEfficiency);
		buf.writeDouble(sparsityEfficiencyMult);
	}
}
