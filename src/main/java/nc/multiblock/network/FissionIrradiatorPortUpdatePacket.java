package nc.multiblock.network;

import io.netty.buffer.ByteBuf;
import nc.network.tile.TileUpdatePacket;
import nc.tile.ITileGui;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;

public class FissionIrradiatorPortUpdatePacket extends FissionPortUpdatePacket {
	
	public ItemStack filterStack;
	
	public FissionIrradiatorPortUpdatePacket() {
		super();
	}
	
	public FissionIrradiatorPortUpdatePacket(BlockPos pos, BlockPos masterPortPos, NonNullList<ItemStack> filterStacks) {
		super(pos, masterPortPos);
		filterStack = filterStacks.get(0);
	}
	
	@Override
	public void readMessage(ByteBuf buf) {
		super.readMessage(buf);
		filterStack = ByteBufUtils.readItemStack(buf);
	}
	
	@Override
	public void writeMessage(ByteBuf buf) {
		super.writeMessage(buf);
		ByteBufUtils.writeItemStack(buf, filterStack);
	}
	
	public static class Handler extends TileUpdatePacket.Handler<FissionIrradiatorPortUpdatePacket, ITileGui> {
		
		@Override
		protected void onPacket(FissionIrradiatorPortUpdatePacket message, ITileGui processor) {
			processor.onGuiPacket(message);
		}
	}
}
