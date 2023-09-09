package nc.network.multiblock;

import io.netty.buffer.ByteBuf;
import nc.network.tile.TileUpdatePacket;
import nc.tile.ITileGui;
import net.minecraft.util.math.BlockPos;

public class FissionPortUpdatePacket extends TileUpdatePacket {
	
	public BlockPos masterPortPos;
	
	public FissionPortUpdatePacket() {
		super();
	}
	
	public FissionPortUpdatePacket(BlockPos pos, BlockPos masterPortPos) {
		super(pos);
		this.masterPortPos = masterPortPos;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		super.fromBytes(buf);
		masterPortPos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		super.toBytes(buf);
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
