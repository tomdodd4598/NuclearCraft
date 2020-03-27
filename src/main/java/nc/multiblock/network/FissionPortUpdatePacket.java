package nc.multiblock.network;

import io.netty.buffer.ByteBuf;
import nc.network.tile.TileUpdatePacket;
import nc.tile.ITileGui;
import net.minecraft.util.math.BlockPos;

public class FissionPortUpdatePacket extends TileUpdatePacket {
	
	public BlockPos masterPortPos;
	
	public FissionPortUpdatePacket() {
		messageValid = false;
	}
	
	public FissionPortUpdatePacket(BlockPos pos, BlockPos masterPortPos) {
		this.pos = pos;
		this.masterPortPos = masterPortPos;
		
		messageValid = true;
	}
	
	@Override
	public void readMessage(ByteBuf buf) {
		pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
		masterPortPos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
	}
	
	@Override
	public void writeMessage(ByteBuf buf) {
		buf.writeInt(pos.getX());
		buf.writeInt(pos.getY());
		buf.writeInt(pos.getZ());
		buf.writeInt(masterPortPos.getX());
		buf.writeInt(masterPortPos.getY());
		buf.writeInt(masterPortPos.getZ());
	}
	
	public static class Handler extends TileUpdatePacket.Handler<FissionPortUpdatePacket, ITileGui> {
		
		@Override
		protected void onPacket(FissionPortUpdatePacket message, ITileGui processor) {
			processor.onGuiPacket(message);
		}
	}
}
