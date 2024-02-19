package nc.network.tile.multiblock.port;

import java.util.List;

import io.netty.buffer.ByteBuf;
import nc.network.tile.TileUpdatePacket;
import nc.tile.ITilePacket;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;

public class ItemPortUpdatePacket extends TileUpdatePacket {
	
	public BlockPos masterPortPos;
	public List<ItemStack> filterStacks;
	
	public ItemPortUpdatePacket() {
		super();
	}
	
	public ItemPortUpdatePacket(BlockPos pos, BlockPos masterPortPos, NonNullList<ItemStack> filterStacks) {
		super(pos);
		this.masterPortPos = masterPortPos;
		this.filterStacks = filterStacks;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		super.fromBytes(buf);
		masterPortPos = readPos(buf);
		filterStacks = readStacks(buf);
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		super.toBytes(buf);
		writePos(buf, masterPortPos);
		writeStacks(buf, filterStacks);
	}
	
	public static class Handler extends TileUpdatePacket.Handler<ItemPortUpdatePacket, ITilePacket<ItemPortUpdatePacket>> {}
}
