package nc.network.tile.multiblock;

import java.util.List;

import io.netty.buffer.ByteBuf;
import nc.multiblock.fission.FissionCluster;
import nc.network.tile.TileUpdatePacket;
import nc.network.tile.processor.ProcessorUpdatePacket;
import nc.tile.ITilePacket;
import nc.tile.internal.fluid.Tank;
import nc.tile.internal.fluid.Tank.TankInfo;
import net.minecraft.util.math.BlockPos;

public class SaltFissionVesselUpdatePacket extends ProcessorUpdatePacket {
	
	public BlockPos masterPortPos;
	public List<TankInfo> filterTankInfos;
	public long clusterHeatStored, clusterHeatCapacity;
	
	public SaltFissionVesselUpdatePacket() {
		super();
	}
	
	public SaltFissionVesselUpdatePacket(BlockPos pos, boolean isProcessing, double time, double baseProcessTime, List<Tank> tanks, BlockPos masterPortPos, List<Tank> filterTanks, FissionCluster cluster) {
		super(pos, isProcessing, time, baseProcessTime, tanks);
		this.masterPortPos = masterPortPos;
		filterTankInfos = TankInfo.getInfoList(filterTanks);
		clusterHeatStored = cluster == null ? -1L : cluster.heatBuffer.getHeatStored();
		clusterHeatCapacity = cluster == null ? -1L : cluster.heatBuffer.getHeatCapacity();
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		super.fromBytes(buf);
		masterPortPos = readPos(buf);
		filterTankInfos = readTankInfos(buf);
		clusterHeatStored = buf.readLong();
		clusterHeatCapacity = buf.readLong();
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		super.toBytes(buf);
		writePos(buf, masterPortPos);
		writeTankInfos(buf, filterTankInfos);
		buf.writeLong(clusterHeatStored);
		buf.writeLong(clusterHeatCapacity);
	}
	
	public static class Handler extends TileUpdatePacket.Handler<SaltFissionVesselUpdatePacket, ITilePacket<SaltFissionVesselUpdatePacket>> {}
}
