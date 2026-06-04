package nc.network.multiblock;

import io.netty.buffer.ByteBuf;
import nc.tile.internal.heat.HeatBuffer;
import net.minecraft.util.math.BlockPos;

public abstract class FissionUpdatePacket extends MultiblockUpdatePacket {
	
	public boolean isReactorOn;
	public long cooling, rawHeating, usefulPartCount, heatStored, heatCapacity;
	public int clusterCount;
	public double meanHeatMult, meanEfficiency, sparsityEfficiencyMult;
	
	public FissionUpdatePacket() {
		super();
	}
	
	public FissionUpdatePacket(BlockPos pos, boolean isReactorOn, HeatBuffer heatBuffer, int clusterCount, long cooling, long rawHeating, double meanHeatMult, long usefulPartCount, double meanEfficiency, double sparsityEfficiencyMult) {
		super(pos);
		this.isReactorOn = isReactorOn;
		heatStored = heatBuffer.getHeatStored();
		heatCapacity = heatBuffer.getHeatCapacity();
		this.clusterCount = clusterCount;
		this.cooling = cooling;
		this.rawHeating = rawHeating;
		this.meanHeatMult = meanHeatMult;
		this.usefulPartCount = usefulPartCount;
		this.meanEfficiency = meanEfficiency;
		this.sparsityEfficiencyMult = sparsityEfficiencyMult;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		super.fromBytes(buf);
		isReactorOn = buf.readBoolean();
		heatStored = buf.readLong();
		heatCapacity = buf.readLong();
		clusterCount = buf.readInt();
		cooling = buf.readLong();
		rawHeating = buf.readLong();
		meanHeatMult = buf.readDouble();
		usefulPartCount = buf.readLong();
		meanEfficiency = buf.readDouble();
		sparsityEfficiencyMult = buf.readDouble();
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		super.toBytes(buf);
		buf.writeBoolean(isReactorOn);
		buf.writeLong(heatStored);
		buf.writeLong(heatCapacity);
		buf.writeInt(clusterCount);
		buf.writeLong(cooling);
		buf.writeLong(rawHeating);
		buf.writeDouble(meanHeatMult);
		buf.writeLong(usefulPartCount);
		buf.writeDouble(meanEfficiency);
		buf.writeDouble(sparsityEfficiencyMult);
	}
}
