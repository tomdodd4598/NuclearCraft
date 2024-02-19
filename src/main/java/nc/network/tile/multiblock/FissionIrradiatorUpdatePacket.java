package nc.network.tile.multiblock;

import java.util.List;

import io.netty.buffer.ByteBuf;
import nc.multiblock.fission.FissionCluster;
import nc.network.tile.TileUpdatePacket;
import nc.network.tile.processor.ProcessorUpdatePacket;
import nc.tile.ITilePacket;
import nc.tile.internal.fluid.Tank;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;

public class FissionIrradiatorUpdatePacket extends ProcessorUpdatePacket {
	
	public BlockPos masterPortPos;
	public List<ItemStack> filterStacks;
	public long clusterHeatStored, clusterHeatCapacity;
	
	public FissionIrradiatorUpdatePacket() {
		super();
	}
	
	public FissionIrradiatorUpdatePacket(BlockPos pos, boolean isProcessing, double time, double baseProcessTime, List<Tank> tanks, BlockPos masterPortPos, NonNullList<ItemStack> filterStacks, FissionCluster cluster) {
		super(pos, isProcessing, time, baseProcessTime, tanks);
		this.masterPortPos = masterPortPos;
		this.filterStacks = filterStacks;
		clusterHeatStored = cluster == null ? -1L : cluster.heatBuffer.getHeatStored();
		clusterHeatCapacity = cluster == null ? -1L : cluster.heatBuffer.getHeatCapacity();
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		super.fromBytes(buf);
		masterPortPos = readPos(buf);
		filterStacks = readStacks(buf);
		clusterHeatStored = buf.readLong();
		clusterHeatCapacity = buf.readLong();
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		super.toBytes(buf);
		writePos(buf, masterPortPos);
		writeStacks(buf, filterStacks);
		buf.writeLong(clusterHeatStored);
		buf.writeLong(clusterHeatCapacity);
	}
	
	public static class Handler extends TileUpdatePacket.Handler<FissionIrradiatorUpdatePacket, ITilePacket<FissionIrradiatorUpdatePacket>> {}
}
