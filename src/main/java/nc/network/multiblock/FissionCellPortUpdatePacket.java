package nc.network.multiblock;

import io.netty.buffer.ByteBuf;
import nc.network.tile.TileUpdatePacket;
import nc.tile.ITileGui;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;

public class FissionCellPortUpdatePacket extends FissionPortUpdatePacket {
	
	public ItemStack filterStack;
	
	public FissionCellPortUpdatePacket() {
		super();
	}
	
	public FissionCellPortUpdatePacket(BlockPos pos, BlockPos masterPortPos, NonNullList<ItemStack> filterStacks) {
		super(pos, masterPortPos);
		filterStack = filterStacks.get(0);
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		super.fromBytes(buf);
		filterStack = ByteBufUtils.readItemStack(buf);
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		super.toBytes(buf);
		ByteBufUtils.writeItemStack(buf, filterStack);
	}
	
	public static class Handler extends TileUpdatePacket.Handler<FissionCellPortUpdatePacket, ITileGui<FissionCellPortUpdatePacket>> {
		
		@Override
		protected void onTileUpdatePacket(FissionCellPortUpdatePacket message, ITileGui<FissionCellPortUpdatePacket> processor) {
			processor.onTileUpdatePacket(message);
		}
	}
}
