package nc.multiblock.network;

import io.netty.buffer.ByteBuf;
import nc.multiblock.fission.FissionCluster;
import nc.network.tile.TileUpdatePacket;
import nc.tile.ITileGui;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;

public class SolidFissionCellUpdatePacket extends TileUpdatePacket {
	
	public BlockPos masterPortPos;
	public ItemStack filterStack;
	public long clusterHeatStored, clusterHeatCapacity;
	public double time, baseProcessTime;
	
	public SolidFissionCellUpdatePacket() {
		messageValid = false;
	}
	
	public SolidFissionCellUpdatePacket(BlockPos pos, BlockPos masterPortPos, NonNullList<ItemStack> filterStacks, FissionCluster cluster, double time, double baseProcessTime) {
		this.pos = pos;
		this.masterPortPos = masterPortPos;
		filterStack = filterStacks.get(0);
		clusterHeatStored = cluster == null ? -1L : cluster.heatBuffer.getHeatStored();
		clusterHeatCapacity = cluster == null ? -1L : cluster.heatBuffer.getHeatCapacity();
		this.time = time;
		this.baseProcessTime = baseProcessTime;
		
		messageValid = true;
	}
	
	@Override
	public void readMessage(ByteBuf buf) {
		pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
		masterPortPos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
		filterStack = ByteBufUtils.readItemStack(buf);
		clusterHeatStored = buf.readLong();
		clusterHeatCapacity = buf.readLong();
		time = buf.readDouble();
		baseProcessTime = buf.readDouble();
	}
	
	@Override
	public void writeMessage(ByteBuf buf) {
		buf.writeInt(pos.getX());
		buf.writeInt(pos.getY());
		buf.writeInt(pos.getZ());
		buf.writeInt(masterPortPos.getX());
		buf.writeInt(masterPortPos.getY());
		buf.writeInt(masterPortPos.getZ());
		ByteBufUtils.writeItemStack(buf, filterStack);
		buf.writeLong(clusterHeatStored);
		buf.writeLong(clusterHeatCapacity);
		buf.writeDouble(time);
		buf.writeDouble(baseProcessTime);
	}
	
	public static class Handler extends TileUpdatePacket.Handler<SolidFissionCellUpdatePacket, ITileGui> {
		
		@Override
		protected void onPacket(SolidFissionCellUpdatePacket message, ITileGui processor) {
			processor.onGuiPacket(message);
		}
	}
}
