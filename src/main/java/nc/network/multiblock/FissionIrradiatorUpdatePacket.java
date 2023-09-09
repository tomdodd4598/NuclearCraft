package nc.network.multiblock;

import io.netty.buffer.ByteBuf;
import nc.multiblock.fission.FissionCluster;
import nc.network.tile.TileUpdatePacket;
import nc.tile.ITileGui;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;

public class FissionIrradiatorUpdatePacket extends TileUpdatePacket {
	
	public BlockPos masterPortPos;
	public ItemStack filterStack;
	public long clusterHeatStored, clusterHeatCapacity;
	public boolean isProcessing;
	public double time, baseProcessTime;
	
	public FissionIrradiatorUpdatePacket() {
		super();
	}
	
	public FissionIrradiatorUpdatePacket(BlockPos pos, BlockPos masterPortPos, NonNullList<ItemStack> filterStacks, FissionCluster cluster, boolean isProcessing, double time, double baseProcessTime) {
		super(pos);
		this.masterPortPos = masterPortPos;
		filterStack = filterStacks.get(0);
		clusterHeatStored = cluster == null ? -1L : cluster.heatBuffer.getHeatStored();
		clusterHeatCapacity = cluster == null ? -1L : cluster.heatBuffer.getHeatCapacity();
		this.isProcessing = isProcessing;
		this.time = time;
		this.baseProcessTime = baseProcessTime;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		super.fromBytes(buf);
		masterPortPos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
		filterStack = ByteBufUtils.readItemStack(buf);
		clusterHeatStored = buf.readLong();
		clusterHeatCapacity = buf.readLong();
		isProcessing = buf.readBoolean();
		time = buf.readDouble();
		baseProcessTime = buf.readDouble();
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		super.toBytes(buf);
		buf.writeInt(masterPortPos.getX());
		buf.writeInt(masterPortPos.getY());
		buf.writeInt(masterPortPos.getZ());
		ByteBufUtils.writeItemStack(buf, filterStack);
		buf.writeLong(clusterHeatStored);
		buf.writeLong(clusterHeatCapacity);
		buf.writeBoolean(isProcessing);
		buf.writeDouble(time);
		buf.writeDouble(baseProcessTime);
	}
	
	public static class Handler extends TileUpdatePacket.Handler<FissionIrradiatorUpdatePacket, ITileGui<FissionIrradiatorUpdatePacket>> {
		
		@Override
		protected void onTileUpdatePacket(FissionIrradiatorUpdatePacket message, ITileGui<FissionIrradiatorUpdatePacket> processor) {
			processor.onTileUpdatePacket(message);
		}
	}
}
