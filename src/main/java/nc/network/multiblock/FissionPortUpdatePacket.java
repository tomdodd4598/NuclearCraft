package nc.network.multiblock;

import io.netty.buffer.ByteBuf;
import nc.network.tile.TileUpdatePacket;
import nc.tile.ITileGui;
import net.minecraft.util.math.BlockPos;

public class FissionPortUpdatePacket extends TileUpdatePacket {
	
	public BlockPos masterPortPos;
	
	public FissionPortUpdatePacket() {
		
	}
	
	public FissionPortUpdatePacket(BlockPos pos, BlockPos masterPortPos) {
		this.pos = pos;
		this.masterPortPos = masterPortPos;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
		masterPortPos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(pos.getX());
		buf.writeInt(pos.getY());
		buf.writeInt(pos.getZ());
		buf.writeInt(masterPortPos.getX());
		buf.writeInt(masterPortPos.getY());
		buf.writeInt(masterPortPos.getZ());
	}
	
	public static class Handler extends TileUpdatePacket.Handler<FissionPortUpdatePacket, ITileGui<FissionPortUpdatePacket>> {
		
		@Override
		protected void onTileUpdatePacket(FissionPortUpdatePacket message, ITileGui<FissionPortUpdatePacket> processor) {
			processor.onTileUpdatePacket(message);
		}
	}
}
